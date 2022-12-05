package ru.grishankov.marketin

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.grishankov.marketin.navigation.CustomNavigationHost
import ru.grishankov.marketin.navigation.ScreenRoute
import ru.grishankov.marketin.navigation.rememberNavController
import ru.grishankov.marketin.util.ui.theme.MarketInTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController by rememberNavController(startDestination = ScreenRoute.ScreenLaunch)
            MarketInTheme {
                // A surface container using the "background" color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    CustomNavigationHost(navController = navController)
                }
            }
        }
    }
}