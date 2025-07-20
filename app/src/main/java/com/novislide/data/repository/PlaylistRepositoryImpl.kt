package com.novislide.data.repository

import com.novislide.data.NoviAPI
import com.novislide.domain.model.MediaItem
import javax.inject.Inject

private const val GET_MEDIA_BASE_URL = "https://test.onsignage.com/PlayerBackend/creative/get/"

class PlaylistRepositoryImpl @Inject constructor(private val api: NoviAPI) : PlaylistRepository {

    /**
     * Fetches playlists for the given [screenKey] and converts them into a list
     * of domain [MediaItem]s ordered by their [orderKey].
     */
    override suspend fun getPlaylistsByScreenKey(screenKey: String): List<MediaItem> {

        return api.getPlaylists(screenKey).playlists
            .flatMap { it.playlistItems }
            .sortedBy { it.orderKey }
            .map { item ->
                MediaItem(
                    mediaUrl = GET_MEDIA_BASE_URL + item.creativeKey,
                    duration = item.duration * 1000L
                )
            }


    }

    /**
     * Returns the timestamp of the last modification for the playlist
     * associated with [screenKey].
     */
    override suspend fun getModified(screenKey: String): Long {
        return api.getModified(screenKey).modified
    }
}
