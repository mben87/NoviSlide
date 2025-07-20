package com.novislide.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.novislide.domain.model.MediaItem
import kotlinx.coroutines.delay

/**
 * Cycles through the given [items] and displays the current one with a fade
 * transition.
 */
@Composable
fun Slideshow(items: List<MediaItem>) {
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex) {
        val durationMillis = (items[currentIndex].duration).coerceAtLeast(1000L)
        delay(durationMillis)
        currentIndex = (currentIndex + 1) % items.size
    }

    Crossfade(
        targetState = items[currentIndex],
        animationSpec = tween(durationMillis = 2000), // animation duration
        label = "MediaCrossfade"
    ) { item ->
        MediaItemView(item)
    }
}