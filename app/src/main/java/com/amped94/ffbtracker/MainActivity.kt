package com.amped94.ffbtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.amped94.ffbtracker.data.model.db.entity.UserAndLeagues
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.ui.theme.FFBTrackerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FFBTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main()
                }
            }
        }
    }
}

@Composable
fun Main() {
    val viewModel by remember { mutableStateOf(MainViewModel()) }
    val user by viewModel.user.observeAsState()

    Column {
        Text("User Info")
        Divider()
        user?.let {
            UserInfo(it)
        }
    }
}

@Composable
fun UserInfo(user: UserAndLeagues) {
    Column {
        CustomText(title = "Username: ", content = user.user.username)
        CustomText(title = "Account ID: ", content = user.user.accountId)
        CustomText(title = "Type: ", content = user.user.type.toString())

        user.leagues.forEach { league ->
            CustomText(title = "League Name: ", content = league.league.name)

            league.players.forEach { player ->
                CustomText(title = "Player Name: ", content = player.name)
                CustomText(title = "Player Team: ", content = player.team)
            }
        }
    }
}

@Composable
fun CustomText(title: String, content: String) {
    Row {
        Text(title)
        Text(content)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FFBTrackerTheme {
        CustomText(title = "TEST", content = "TEST2")
    }
}