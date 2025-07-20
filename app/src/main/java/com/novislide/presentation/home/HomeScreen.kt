package com.novislide.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    val greeting by viewModel.greeting.collectAsState()
    HomeScreen(greeting = greeting, onShowGreeting = {
        viewModel.loadGreeting("Android")
    })
}

@Composable
fun HomeScreen(greeting: String, onShowGreeting: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (greeting.isNotBlank()) {
            Text(text = greeting)
        }
        Button(onClick = onShowGreeting) {
            Text("Load Greeting")
        }
    }
}
