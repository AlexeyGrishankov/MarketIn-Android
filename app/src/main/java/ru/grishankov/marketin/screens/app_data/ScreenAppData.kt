package ru.grishankov.marketin.screens.app_data

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.navigation.NavController
import ru.grishankov.marketin.navigation.args
import ru.grishankov.marketin.screens.BaseScreen
import ru.grishankov.marketin.util.ui.components.RemoteImage
import ru.grishankov.marketin.util.ui.icons.Download
import ru.grishankov.marketin.util.ui.theme.GrayText
import ru.grishankov.marketin.util.ui.theme.MainColor

class ScreenAppData(
    private val navController: NavController,
) : BaseScreen() {

    private val appId by lazy {
        navController.args.getInt("id", -1)
    }

    companion object {
        private val ShapeLogoApp = RoundedCornerShape(5.dp)
        private val ShapeTag = RoundedCornerShape(5.dp)
        private val ColorTag = MainColor.copy(alpha = 0.5f)
        private val EmptyAppData = AppDataDto(-1, "", "", emptyList(), "", null, emptyList(), "")
    }

    @Composable
    override fun OnStart() {
        val viewModel = koinViewModel<ScreenAppDataViewModel>()

        LaunchedEffect(key1 = Unit, block = {
            viewModel.getAppData(appId)
        })

        val scaffoldState = rememberScaffoldState()
        val appDataState = viewModel.appDataState.collectAsState()

        val isInstall by viewModel.isInstallState.collectAsState()
        val isNeedUpdate by viewModel.isNeedUpdateState.collectAsState()

        var appData by remember { mutableStateOf(EmptyAppData) }
        val context = LocalContext.current
        val packageManager = context.packageManager

        var isDescriptionTab by remember { mutableStateOf(true) }

        val isDownloading by viewModel.isDownloading.collectAsState()

        val statusAction by viewModel.appActionState.collectAsState()

        val installResult = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { viewModel.setStatusInstalled() }
        )

        val removeResult = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { viewModel.downloadApk(appData.versions.firstOrNull(), context, installResult) }
        )

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    title = { Text(text = "Приложение") },
                    elevation = 5.dp,
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigateBack() },
                            content = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null) }
                        )
                    }
                )
            },
            content = { paddgings ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddgings)
                        .background(Color.White)
                ) {

                    if (appDataState.value.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(modifier = Modifier.weight(7.5f)) {
                                    RemoteImage(
                                        imageUrl = appData.logoUrl, modifier = Modifier
                                            .size(90.dp)
                                            .clip(ShapeLogoApp)
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))

                                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.heightIn(min = 90.dp)) {
                                        Row {
                                            appData.tags.take(2).forEach { tag ->
                                                Box(
                                                    modifier = Modifier
                                                        .padding(2.dp)
                                                        .clip(ShapeTag)
                                                        .border(width = 0.8.dp, color = ColorTag, shape = ShapeTag)
                                                        .padding(6.dp)
                                                ) {
                                                    Text(text = tag, color = ColorTag, fontSize = 10.sp)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(4.dp))
                                        }

                                        Text(text = appData.label, fontWeight = FontWeight.Medium, fontSize = 20.sp)

                                        Column {
                                            Text(text = appData.lastVersion?.title ?: "", fontSize = 12.sp, color = GrayText)

                                            val textSub = when {
                                                statusAction.isDeleting -> "Удаление прошлой версии.."
                                                statusAction.isInstalling -> "Установка.."
                                                statusAction.isDone && !isInstall -> "Не установлено"
                                                statusAction.isDone && isNeedUpdate -> "Доступно обновление"
                                                else -> "Установлено"
                                            }
                                            Text(text = textSub, fontSize = 12.sp, color = GrayText)
                                        }
                                    }
                                }
                                if (appData.versions.isNotEmpty() && (!isInstall || isNeedUpdate)) {
                                    if (isDownloading) {
                                        Box(modifier = Modifier.weight(1.78f), contentAlignment = Alignment.Center) {
                                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        }
                                    } else {
                                        IconButton(
                                            modifier = Modifier.weight(1.78f),
                                            onClick = {
                                                if (isNeedUpdate) {
                                                    viewModel.removeApp(appData, removeResult)
                                                } else {
                                                    viewModel.downloadApk(appData.versions.firstOrNull(), context, installResult)
                                                }
                                            },
                                            content = { Icon(imageVector = Download, contentDescription = null, tint = MainColor) }
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, color = MainColor)
                            ) {

                                val colorSelected = if (isDescriptionTab) MainColor else Color.White
                                val colorUnselected = if (!isDescriptionTab) MainColor else Color.White

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .background(colorSelected)
                                    .clickable { isDescriptionTab = true }
                                    .padding(11.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Описание", color = colorUnselected, fontWeight = FontWeight.Medium)
                                }

                                Box(modifier = Modifier
                                    .weight(1f)
                                    .background(colorUnselected)
                                    .clickable {
                                        isDescriptionTab = false
                                    }
                                    .padding(11.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Версии", color = colorSelected, fontWeight = FontWeight.Medium)
                                }
                            }

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (isDescriptionTab) {
                                    Box(modifier = Modifier.padding(16.dp)) {
                                        Text(text = appData.desc)
                                    }
                                } else {
                                    LazyColumn {
                                        if (appData.versions.isEmpty()) {
                                            item {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(50.dp), contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "Список версий пуст", color = GrayText, fontSize = 12.sp)
                                                }
                                            }
                                        } else {
                                            items(appData.versions) { version ->
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(16.dp)
                                                ) {
                                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                        Text(text = version.title)
                                                        Text(text = "06.12.2022", color = GrayText, fontSize = 12.sp)
                                                    }
                                                    Text(text = version.description, color = GrayText, fontSize = 10.sp)
                                                }
                                                Divider()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )

        LaunchedEffect(key1 = statusAction, block = {
            if (statusAction.isDone) {
                viewModel.getAppList(packageManager, appData)
                viewModel.removeInstaller(context)
            }
        })

        LaunchedEffect(key1 = appDataState.value, block = {
            with(appDataState.value) {
                content?.also { app ->
                    appData = app
                    viewModel.getAppList(packageManager, app)
                }
                error?.also { }
            }
        })
    }
}