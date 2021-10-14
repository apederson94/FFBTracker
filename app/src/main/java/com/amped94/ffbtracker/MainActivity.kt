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
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef
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
    val playersAndLeagues by viewModel.playersAndLeagues.observeAsState()

    Column {
        user?.let { user ->
            playersAndLeagues?.let { playersAndLeagues ->
                UserInfo(playersAndLeagues)
            }
        } ?: Text("Loading...")
    }
}

@Composable
fun UserInfo(playerData: List<PlayerAndLeagues>) {
    Column {

        playerData.forEach { player ->
            CustomText(title = "Player Name: ", content = player.player.name)

            player.leagues.forEach {
                Text(it.name)
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