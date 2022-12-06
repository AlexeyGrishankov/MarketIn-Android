package ru.grishankov.marketin.screens.app_data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.data.dto.AppVersionDto
import ru.grishankov.marketin.data.repositories.ApplicationsRepo
import ru.grishankov.marketin.screens.app_data.state.AppActionState
import ru.grishankov.marketin.screens.app_data.state.AppDataState
import ru.grishankov.marketin.util.extension.downloadFile
import ru.grishankov.marketin.util.extension.openFile
import ru.grishankov.marketin.util.network.DownloadResult
import java.io.File


class ScreenAppDataViewModel(
    private val applicationsRepo: ApplicationsRepo,
    private val httpClient: HttpClient
) : ViewModel() {

    private val _appDataState = MutableStateFlow(AppDataState())
    val appDataState = _appDataState.asStateFlow()

    private val _isInstallState = MutableStateFlow(true)
    val isInstallState = _isInstallState.asStateFlow()

    private val _isNeedUpdateState = MutableStateFlow(false)
    val isNeedUpdateState = _isNeedUpdateState.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading = _isDownloading.asStateFlow()

    private val _appActionState = MutableStateFlow(AppActionState())
    val appActionState = _appActionState.asStateFlow()

    fun getAppData(appId: Int) {
        _appDataState.value = AppDataState(isLoading = true)
        viewModelScope.launch {
            applicationsRepo.getAppData(appId).onEach { data ->
                _appDataState.value = AppDataState(content = data.data, error = data.error)
            }.collect()
        }
    }

    fun setStatusInstalled() {
        _appActionState.value = AppActionState()
    }

    fun removeApp(app: AppDataDto, remover: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        viewModelScope.launch {
            _appActionState.value = AppActionState(isDeleting = true, isDone = false)
            val packageUri = Uri.parse("package:${app.packageName}")

            @Suppress("DEPRECATION")
            val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri)
            remover.launch(uninstallIntent)
        }
    }

    fun removeInstaller(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            context.filesDir.deleteRecursively()
        }
    }

    fun downloadApk(appVersionDto: AppVersionDto?, context: Context, installer: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        if (appVersionDto == null) return
        _appActionState.value = AppActionState(isInstalling = true, isDone = false)
        val file = File(context.filesDir, "${appVersionDto.title}.apk")
        file.createNewFile()
        viewModelScope.launch(Dispatchers.IO) {
            _isDownloading.value = true
            httpClient.downloadFile(file, appVersionDto.url).collect {
                withContext(Dispatchers.Main) {
                    when (it) {
                        is DownloadResult.Success -> {
                            _isDownloading.value = false
                            context.openFile(file, installer)
                        }

                        is DownloadResult.Error -> {
                            _isDownloading.value = false
                            Toast.makeText(context, "Error while downloading", Toast.LENGTH_LONG).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun getAppList(packageManager: PackageManager, app: AppDataDto) {
        viewModelScope.launch {
            val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val flags = PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                packageManager.getInstalledApplications(flags)
            } else {
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            }

            val appList = mutableListOf<ApplicationInfo>()
            apps.forEach { info ->
                try {
                    if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                        appList.add(info)
                    }
                } catch (t: Throwable) {
                    Log.e("CHECK_APP", t.message, t)
                }
            }

            val appInfo = appList.find { it.packageName == app.packageName }
            if (appInfo != null) {
                val packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0)
                val versionCode = packageInfo.versionCode
                val lastVersionCode = app.versions.last().versionCode

                if (versionCode < lastVersionCode) {
                    _isInstallState.value = true
                    _isNeedUpdateState.value = true
                } else {
                    _isInstallState.value = true
                    _isNeedUpdateState.value = false
                }
            } else {
                _isInstallState.value = false
                _isNeedUpdateState.value = false
            }
        }
    }

    fun startApp(context: Context, packageManager: PackageManager, appData: AppDataDto) {
        viewModelScope.launch {
            packageManager.getLaunchIntentForPackage(appData.packageName)?.also { context.startActivity(it) }
        }
    }
}