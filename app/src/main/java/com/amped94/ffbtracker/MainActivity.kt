package com.amped94.ffbtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.platform.LocalContext
import com.amped94.ffbtracker.ui.composable.Main
import com.amped94.ffbtracker.ui.theme.FFBTrackerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.fitsSystemWindows = true

        setContent {
            MaterialTheme(colorScheme = if (isSystemInDarkTheme()) dynamicDarkColorScheme(
                LocalContext.current) else dynamicLightColorScheme(LocalContext.current)
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Main()
                }
            }
        }
    }
}