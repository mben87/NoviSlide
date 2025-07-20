package com.novislide.data.repository

import com.novislide.data.NoviAPI
import com.novislide.data.remote.model.ModifiedResponse
import com.novislide.data.remote.model.PlaylistResponse
import com.novislide.domain.model.MediaItem
import com.novislide.domain.model.Playlist
import com.novislide.domain.model.PlaylistItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PlaylistRepositoryTest {

    private lateinit var api: NoviAPI
    private lateinit var repository: PlaylistRepositoryImpl

    @Before
    fun setUp() {
        api = mock(NoviAPI::class.java)
        repository = PlaylistRepositoryImpl(api)
    }

    @Test
    fun `getPlaylistsByScreenKey returns correct media items`() = runBlocking {
        val screenKey = "test_screen"
        val mockResponse = PlaylistResponse(
            screenKey,
            playlists = listOf(
                Playlist(
                    playlistItems = listOf(
                        PlaylistItem(
                            creativeKey = "abc123",
                            duration = 5,
                            orderKey = 0,
                            playlistKey = "123"
                        ),
                        PlaylistItem(
                            creativeKey = "xyz789",
                            duration = 10,
                            orderKey = 1,
                            playlistKey = "afd"
                        )
                    )
                )
            )
        )

        `when`(api.getPlaylists(screenKey)).thenReturn(mockResponse)
        `when`(api.getModified(screenKey)).thenReturn(ModifiedResponse(1000L))

        val result = repository.getPlaylistsByScreenKey(screenKey)

        val expected = listOf(
            MediaItem(
                mediaUrl = "https://test.onsignage.com/PlayerBackend/creative/get/abc123",
                duration = 5000L
            ),
            MediaItem(
                mediaUrl = "https://test.onsignage.com/PlayerBackend/creative/get/xyz789",
                duration = 10000L
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getPlaylistsByScreenKey returns empty list when no playlists returned`() = runBlocking {
        val screenKey = "empty_screen"
        val mockResponse = PlaylistResponse(screenKey, playlists = emptyList())

        `when`(api.getPlaylists(screenKey)).thenReturn(mockResponse)
        `when`(api.getModified(screenKey)).thenReturn(ModifiedResponse(1000L))

        val result = repository.getPlaylistsByScreenKey(screenKey)

        assertEquals(emptyList<MediaItem>(), result)
    }

    @Test
    fun `getPlaylistsByScreenKey returns empty list when playlists contain no items`() =
        runBlocking {
            val screenKey = "no_items_screen"
            val mockResponse = PlaylistResponse(
                screenKey,
                playlists = listOf(
                    Playlist(playlistItems = emptyList())
                )
            )

            `when`(api.getPlaylists(screenKey)).thenReturn(mockResponse)
            `when`(api.getModified(screenKey)).thenReturn(ModifiedResponse(1000L))

            val result = repository.getPlaylistsByScreenKey(screenKey)

            assertEquals(emptyList<MediaItem>(), result)
        }

    @Test
    fun `getPlaylistsByScreenKey merges items from multiple playlists`() = runBlocking {
        val screenKey = "multi_playlist"
        val mockResponse = PlaylistResponse(
            screenKey,
            playlists = listOf(
                Playlist(
                    playlistItems = listOf(
                        PlaylistItem(
                            creativeKey = "key1",
                            duration = 3,
                            orderKey = 0,
                            playlistKey = "p1"
                        )
                    )
                ),
                Playlist(
                    playlistItems = listOf(
                        PlaylistItem(
                            creativeKey = "key2",
                            duration = 7,
                            orderKey = 1,
                            playlistKey = "p2"
                        )
                    )
                )
            )
        )

        `when`(api.getPlaylists(screenKey)).thenReturn(mockResponse)
        `when`(api.getModified(screenKey)).thenReturn(ModifiedResponse(1000L))

        val result = repository.getPlaylistsByScreenKey(screenKey)

        val expected = listOf(
            MediaItem("https://test.onsignage.com/PlayerBackend/creative/get/key1", 3000L),
            MediaItem("https://test.onsignage.com/PlayerBackend/creative/get/key2", 7000L)
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getPlaylistsByScreenKey throws error when API fails`() = runBlocking {
        val screenKey = "error_screen"
        val exceptionMessage = "Network error"

        `when`(api.getPlaylists(screenKey)).thenThrow(RuntimeException(exceptionMessage))
        `when`(api.getModified(screenKey)).thenReturn(ModifiedResponse(1000L))

        val exception = assertThrows(RuntimeException::class.java) {
            runBlocking {
                repository.getPlaylistsByScreenKey(screenKey)
            }
        }

        assertEquals(exceptionMessage, exception.message)
    }
}