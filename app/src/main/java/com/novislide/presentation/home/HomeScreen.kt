package com.novislide.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import com.novislide.domain.model.MediaItem
import com.novislide.domain.model.MediaType
import kotlinx.coroutines.delay
import androidx.media3.common.MediaItem as ExoMediaItem

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val mediaItems by viewModel.mediaItems.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val screenKey = "e490b14d-987d-414f-a822-1e7703b37ce4"

    LaunchedEffect(Unit) {
        viewModel.loadMediaItems(screenKey)
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Text(
            text = error ?: "Error",
            modifier = Modifier.padding(16.dp)
        )
    } else if (mediaItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No media items found",
                fontSize = 32.sp
            )
        }
    } else {
        Slideshow(mediaItems)
    }
}

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

/** Renders either an image or a video based on [item.mediaType]. */
@Composable
fun MediaItemView(item: MediaItem) {
    when (item.mediaType) {
        MediaType.IMAGE -> {
            Image(
                painter = rememberAsyncImagePainter(item.mediaUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        MediaType.VIDEO -> {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = ExoPlayer.Builder(context).build().apply {
                            setMediaItem(ExoMediaItem.fromUri(item.mediaUrl))
                            repeatMode = ExoPlayer.REPEAT_MODE_ALL
                            playWhenReady = true
                            prepare()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        MediaType.UNKNOWN -> {
        }
    }
}
