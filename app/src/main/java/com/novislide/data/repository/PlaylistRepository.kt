package com.novislide.data.repository

import com.novislide.domain.model.MediaItem

interface PlaylistRepository {
    suspend fun getPlaylistsByScreenKey(screenKey: String): List<MediaItem>

    suspend fun getModified(screenKey: String): Long
}