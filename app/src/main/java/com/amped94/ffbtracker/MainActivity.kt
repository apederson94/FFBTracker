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
import com.amped94.ffbtracker.data.model.data.SleeperUser
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
    val user by viewModel.user.observeAsState(initial = SleeperUser())
    val leagues by viewModel.leagues.observeAsState(initial = listOf())
    val players by viewModel.players.observeAsState(initial = listOf())

    Column {
        Text("User Info")
        Divider()
        Text(user.username)
        Text(user.userId)
        Text(user.displayName)
        Text(user.avatar)

        Divider()
        Text("Leagues")
        Divider()

        leagues.forEach {
            Text(it.leagueId)
        }

        Divider()
        Text("Players")
        Divider()

        players.forEach {
            Row {
                Column {
                    Text("PlayerId")
                    Text(it.id)
                }
                Column {
                    Text("League Names")
                    it.leagues.forEach {
                        Text(it.name)
                    }
                }
            }
        }


    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FFBTrackerTheme {
        Greeting("Android")
    }
}