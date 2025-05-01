package com.example.sitonakameerkat.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sitonakameerkat.ui.theme.SitonakaMeerkatTheme

enum class MyScreen {
    SPLASH,
    MULTI,
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MyScreen.SPLASH.name,
        modifier = modifier
    ) {
        composable(MyScreen.SPLASH.name) {
            SplashScreen {
                navController.navigate(MyScreen.MULTI.name) {
                    popUpTo(MyScreen.SPLASH.name) {
                        inclusive = true
                    }
                }
            }
        }
        composable(MyScreen.MULTI.name) {
            MultiScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SitonakaMeerkatTheme {
        MainScreen()
    }
}
