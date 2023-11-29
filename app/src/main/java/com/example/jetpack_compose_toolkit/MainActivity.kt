package com.example.jetpack_compose_toolkit

import CardInfo
import Carousel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpack_compose_toolkit.ui.theme.JetpackcomposetoolkitTheme
import com.example.jetpack_compose_toolkit.util.showToast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackcomposetoolkitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    Carousel(
        cards = listOf(
            CardInfo(1, "Card 1", Color.Red),
            CardInfo(2, "Card 2", Color.Green),
            CardInfo(3, "Card 3", Color.Blue),
        ),
        radius = 250.dp,
        onClick = { card ->
            ctx.showToast(card.title)
        },
    )
}
