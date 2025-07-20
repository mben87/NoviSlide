package com.novislide.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import com.novislide.domain.model.MediaItem
import com.novislide.domain.model.MediaType
import androidx.media3.common.MediaItem as ExoMediaItem

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