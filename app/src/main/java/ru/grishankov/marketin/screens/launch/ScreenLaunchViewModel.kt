package ru.grishankov.marketin.screens.launch

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import ru.grishankov.marketin.BuildConfig
import ru.grishankov.marketin.data.dto.MarketUpdate
import ru.grishankov.marketin.data.dto.MarketUpdateReq
import ru.grishankov.marketin.data.repositories.ApplicationsRepo
import ru.grishankov.marketin.screens.launch.state.MarketUpdateState
import ru.grishankov.marketin.util.extension.downloadFile
import ru.grishankov.marketin.util.extension.openFile
import ru.grishankov.marketin.util.network.DownloadResult
import java.io.File

class ScreenLaunchViewModel(
    private val repo: ApplicationsRepo,
    private val httpClient: HttpClient,
) : ViewModel() {

    private val _marketUpdateState = MutableStateFlow(MarketUpdateState())
    val marketUpdateState = _marketUpdateState.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading = _isDownloading.asStateFlow()

    private val _isDownloaded = MutableStateFlow(false)
    val isDownloaded = _isDownloaded.asStateFlow()

    fun checkVersion() {
        _marketUpdateState.value = MarketUpdateState(isLoading = true)
        viewModelScope.launch {
            val req = MarketUpdateReq(
                currentVersion = BuildConfig.VERSION_NAME
            )
            repo.getLastVersion(req).onEach { data ->
                _marketUpdateState.value = MarketUpdateState(data = data.data, error = data.error)
            }.collect()
        }
    }

    fun install(
        market: MarketUpdate,
        context: Context,
        installer: ManagedActivityResultLauncher<Intent, ActivityResult>,
    ) {
        _isDownloading.value = true
        viewModelScope.launch {
            val file = File(context.filesDir, "MarketIn-${market.lastVersion}.apk")
            context.openFile(file, installer)
        }
    }

    fun download(
        market: MarketUpdate,
        context: Context,
    ) {
        _isDownloading.value = true
        val file = File(context.filesDir, "MarketIn-${market.lastVersion}.apk")
        file.createNewFile()
        viewModelScope.launch(Dispatchers.IO) {
            httpClient.downloadFile(file, market.urlDownload).collect {
                withContext(Dispatchers.Main) {
                    when (it) {
                        is DownloadResult.Success -> {
                            _isDownloading.value = false
                            _isDownloaded.value = true
                        }
                        is DownloadResult.Error -> {
                            _isDownloading.value = false
                            _isDownloaded.value = false
                            Toast.makeText(context, "Error while downloading", Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}