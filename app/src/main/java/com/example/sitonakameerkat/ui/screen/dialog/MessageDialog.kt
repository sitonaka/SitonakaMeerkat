package com.example.sitonakameerkat.ui.screen.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sitonakameerkat.ui.theme.SitonakaMeerkatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDialog(message: String, onDismiss: () -> Unit) {
    if (message.isNotEmpty()) {
        BasicAlertDialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = message)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAlertDialogPreview() {
    SitonakaMeerkatTheme {
        MessageDialog("Test message") {}
    }
}
