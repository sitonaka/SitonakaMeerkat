package com.example.sitonakameerkat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sitonakameerkat.MainActivity
import com.example.sitonakameerkat.database.MyDatabase
import com.example.sitonakameerkat.logger
import com.example.sitonakameerkat.screen.dialog.LoginInputDialog
import com.example.sitonakameerkat.server.GHUsers
import com.example.sitonakameerkat.ui.theme.SitonakaMeerkatTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun CommScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val dao = MyDatabase.getDatabase(LocalContext.current).ghDao()
    val users = dao.getAllUsersFlow().collectAsState(initial = listOf())
    var login by rememberSaveable { mutableStateOf("sitonaka") }
    var isShowInputDialog by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Login:")
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1F)
                    .wrapContentHeight()
                    .background(Color.LightGray)
                    .clickable {
                        logger.trace("Box onClick")
                        isShowInputDialog = true
                    }
            ) {
                Text(text = login)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                scope.launch {
                    runCatching {
                        HttpClient(CIO) {
                            expectSuccess = true
                            install(ContentNegotiation) {
                                json(
                                    Json {
                                        prettyPrint = true
                                        isLenient = true
                                        ignoreUnknownKeys = true
                                    }
                                )
                            }
                            install(Logging)
                        }.use { client ->
                            val ghUsers: GHUsers =
                                client.get("https://api.github.com/users/$login").body()
                            dao.insertUsers(ghUsers)
                        }
                    }.onFailure {
                        MainActivity.dialog(it.localizedMessage ?: "error")
                    }
                }
            }) {
                Text(text = "USERS API")
            }
        }
        HorizontalDivider()
        UsersItems(list = users.value)
    }
    if (isShowInputDialog) {
        LoginInputDialog(onDismiss = {
            isShowInputDialog = false
        }) {
            isShowInputDialog = false
            login = it
        }
    }
}

@Composable
fun UsersItems(list: List<GHUsers>) {
    LazyColumn {
        items(list) {
            UsersItem(ghUsers = it)
            HorizontalDivider()
        }
    }
}

@Composable
fun UsersItem(ghUsers: GHUsers) {
    Column {
        Text(text = ghUsers.login, style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "updateAt", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ghUsers.updatedAt, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommScreenPreview() {
    SitonakaMeerkatTheme {
        CommScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun UsersItemsPreview() {
    SitonakaMeerkatTheme {
        UsersItems(
            listOf(
                GHUsers(
                    id = 0,
                    login = "login name",
                    publicRepos = 99,
                    reposUrl = "http://google.com",
                    updatedAt = "2000/12/31"
                ),
                GHUsers(
                    id = 1,
                    login = "login name 2",
                    publicRepos = 999,
                    reposUrl = "https://google.com",
                    updatedAt = "2001/12/31"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsersItemPreview() {
    SitonakaMeerkatTheme {
        UsersItem(
            GHUsers(
                id = 0,
                login = "login name",
                publicRepos = 99,
                reposUrl = "http://google.com",
                updatedAt = "2000/12/31"
            )
        )
    }
}
