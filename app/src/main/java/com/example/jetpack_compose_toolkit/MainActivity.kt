package com.example.jetpack_compose_toolkit

import CardCarousel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Sample1()
                        Spacer(modifier = Modifier.padding(16.dp))
                        Sample2()
                        Spacer(modifier = Modifier.padding(16.dp))
                        Sample3()
                    }
                }
            }
        }
    }
}

@Composable
fun Sample1() {
    val ctx = LocalContext.current
    val cards = listOf(
        "Card 1",
        "Card 2",
        "Card 3"
    )

    CardCarousel(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        cards = cards,
        sensitivity = 0.5f,
        elevation = 8.dp,
        cardWith = 200.dp,
        cardHeight = 300.dp,
        onClick = { card ->
            ctx.showToast("Clicked on $card")
        },
        cardContent = { card ->
            when (card) {
                "Card 1" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = card)
                    }
                }

                "Card 2" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Green),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = card)
                    }
                }

                "Card 3" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = card)
                    }
                }
            }
        }
    )
}

@Composable
fun Sample2() {
    val ctx = LocalContext.current
    val cards = listOf(
        "Card 1",
        "Card 2",
        "Card 3",
    )

    CardCarousel(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        cards = cards,
        elevation = 8.dp,
        onClick = { card ->
            ctx.showToast("Clicked on $card")
        },
        cardContent = { card ->
            when (card) {
                "Card 1" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = card)
                    }
                }

                "Card 2" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Green),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = card)
                    }
                }

                "Card 3" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = card)
                    }
                }
            }
        }
    )
}
@Composable
fun Sample3() {
    val ctx = LocalContext.current
    val cards = listOf(
        "Card 1",
        "Card 2",
        "Card 3",
    )

    CardCarousel(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        cards = cards,
        elevation = 8.dp,
        sensitivity = 5f,
        border = BorderStroke(2.dp,Color.Black),
        onClick = { card ->
            ctx.showToast("Clicked on $card")
        },
        cardContent = { card ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Text(text = card)
            }
        }
    )
}
