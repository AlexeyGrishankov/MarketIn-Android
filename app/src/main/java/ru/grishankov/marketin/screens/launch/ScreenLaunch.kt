package ru.grishankov.marketin.screens.launch

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import ru.grishankov.marketin.BuildConfig
import ru.grishankov.marketin.navigation.NavController
import ru.grishankov.marketin.navigation.ScreenRoute
import ru.grishankov.marketin.screens.BaseScreen
import ru.grishankov.marketin.util.ui.theme.MainColor

class ScreenLaunch(
    val navController: NavController
) : BaseScreen() {

    @Composable
    override fun OnStart() {
        val viewModel = koinViewModel<ScreenLaunchViewModel>()

        val scaffoldState = rememberScaffoldState()
        val marketVersionState by viewModel.marketUpdateState.collectAsState()

        var isOpenDialogUpdate by remember { mutableStateOf(false) }

        val isDownloading by viewModel.isDownloading.collectAsState()
        val isDownloaded by viewModel.isDownloaded.collectAsState()

        var fakeLoading by remember { mutableStateOf(false) }

        val context = LocalContext.current

        val installResult = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {  }
        )

        Scaffold(scaffoldState = scaffoldState, content = { paddgings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddgings)
                    .background(Color.White)
            ) {
                if (marketVersionState.isLoading || isDownloading || fakeLoading) {
                   Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                       Text(text = "MarketIn", color = MainColor, fontSize = 20.sp)
                       Spacer(modifier = Modifier.height(20.dp))
                       CircularProgressIndicator()
                       Spacer(modifier = Modifier.height(20.dp))
                       Text(text = "${BuildConfig.VERSION_NAME}.${BuildConfig.VERSION_CODE}", color = Color.LightGray, fontSize = 12.sp)
                   }
                }
            }
        })

        AnimatedVisibility(
            visible = isOpenDialogUpdate,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AlertDialog(
                backgroundColor = Color.White,
                title = { Text(modifier = Modifier.fillMaxWidth(), text = "Доступно обновление. Установить?") },
                onDismissRequest = {},
                buttons = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Button(onClick = {
                            isOpenDialogUpdate = false
                            viewModel.download(marketVersionState.data!!, context)
                        }) {
                            Text(text = "Ок")
                        }
                    }
                },
            )
        }


        LaunchedEffect(key1 = Unit, block = {
            viewModel.checkVersion()
        })

        LaunchedEffect(key1 = isDownloaded, block = {
            if (isDownloaded) {
                viewModel.install(marketVersionState.data!!, context, installResult)
            }
        })

        LaunchedEffect(key1 = marketVersionState, block = {
            marketVersionState.data?.also {
                if (!it.isNeedUpdate) {
                    fakeLoading = true
                    delay(1000)
                    navController.navigate(route = ScreenRoute.ScreenAppList)
                } else {
                    isOpenDialogUpdate = true
                }
            }
        })
    }
}