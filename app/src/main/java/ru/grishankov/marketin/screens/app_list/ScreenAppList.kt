package ru.grishankov.marketin.screens.app_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ru.grishankov.marketin.data.dto.AppShortDataDto
import ru.grishankov.marketin.navigation.NavController
import ru.grishankov.marketin.navigation.ScreenRoute
import ru.grishankov.marketin.navigation.bundle
import ru.grishankov.marketin.screens.BaseScreen
import ru.grishankov.marketin.util.ui.components.RemoteImage
import ru.grishankov.marketin.util.ui.theme.GrayBg
import ru.grishankov.marketin.util.ui.theme.GrayText

class ScreenAppList(
    private val navController: NavController
) : BaseScreen() {

    companion object {
        private val ShapeLogoApp = RoundedCornerShape(5.dp)
    }

    @Composable
    override fun OnStart() {

        val viewModel = koinViewModel<ScreenAppListViewModel>()

        val scaffoldState = rememberScaffoldState()
        val appListState = viewModel.appListState.collectAsState()

        val listApp = remember { mutableStateListOf<AppShortDataDto>() }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    title = { Text(text = "Список приложений") },
                    elevation = 5.dp
                )
            },
            content = { paddgings ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddgings)
                        .background(GrayBg)
                ) {

                    if (appListState.value.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn {
                            items(listApp) { app ->
                                Box(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .clickable { navController.navigate(ScreenRoute.ScreenAppData, bundle = bundle("id" to app.id)) }
                                        .padding(16.dp),
                                    content = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RemoteImage(
                                                    imageUrl = app.logoUrl, modifier = Modifier
                                                        .size(60.dp)
                                                        .clip(ShapeLogoApp)
                                                )
                                                Spacer(modifier = Modifier.size(16.dp))
                                                Column {
                                                    Text(text = app.label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                                    Text(text = app.tags.joinToString(", "), fontWeight = FontWeight.Normal, color = GrayText, fontSize = 12.sp)
                                                }
                                            }
                                            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
                                        }
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        )


        LaunchedEffect(key1 = Unit, block = {
            viewModel.getAppList()
        })

        LaunchedEffect(key1 = appListState.value, block = {
            with(appListState.value) {
                content?.also {
                    listApp.clear()
                    listApp += it.list
                }
                error?.also {

                }
            }
        })
    }
}