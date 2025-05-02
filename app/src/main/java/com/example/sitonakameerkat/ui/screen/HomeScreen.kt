package com.example.sitonakameerkat.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sitonakameerkat.MainActivity
import com.example.sitonakameerkat.ui.screen.dialog.MessageDialog
import com.example.sitonakameerkat.ui.theme.SitonakaMeerkatTheme
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var location by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier) {
        Text(text = "Location  $location")
        Button(onClick = {
            scope.launch {
                runCatching {
                    MainActivity.location()
                }.onSuccess {
                    location = it
                }.onFailure {
                    message = it.localizedMessage ?: "error"
                }
            }
        }) {
            Text(text = "Get Location")
        }
    }
    MessageDialog(message = message) { message = "" }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SitonakaMeerkatTheme {
        HomeScreen()
    }
}
