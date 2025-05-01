package com.example.sitonakameerkat.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sitonakameerkat.logger
import com.example.sitonakameerkat.ui.theme.SitonakaMeerkatTheme

enum class SubScreen {
    HOME,
    COMM,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiScreen() {
    var subScreen by rememberSaveable { mutableStateOf(SubScreen.HOME) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Sitonaka Meerkat") },
                actions = {
                    IconButton(
                        onClick = {
                            logger.trace("Settings Button Click")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(selected = subScreen == SubScreen.HOME, onClick = {
                    logger.trace("Home Button Click")
                    subScreen = SubScreen.HOME
                }, icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                    )
                }, label = { Text(text = "Home") })
                NavigationBarItem(selected = subScreen == SubScreen.COMM, onClick = {
                    logger.trace("Comm Button Click")
                    subScreen = SubScreen.COMM
                }, icon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Comm",
                    )
                }, label = { Text(text = "Comm") })
            }
        }
    ) { innerPadding ->
        when (subScreen) {
            SubScreen.HOME -> {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            SubScreen.COMM -> {
                CommScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiScreenPreview() {
    SitonakaMeerkatTheme {
        MultiScreen()
    }
}
