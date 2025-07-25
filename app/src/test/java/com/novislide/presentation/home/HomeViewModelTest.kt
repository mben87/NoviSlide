package com.novislide.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.novislide.data.repository.PlaylistRepositoryImpl
import com.novislide.domain.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PlaylistRepositoryImpl
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mock()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMediaItems sets mediaItems on success`() = runTest {
        val dummyItems = listOf(
            MediaItem("http://test.com/image1.jpg", 5),
            MediaItem("http://test.com/video1.mp4", 10)
        )
        `when`(repository.getPlaylistsByScreenKey("Test")).thenReturn(dummyItems)
        `when`(repository.getModified("Test")).thenReturn(111111)

        viewModel.fetchMediaItems("Test")

        viewModel.mediaItems.test {
            assertEquals(dummyItems, awaitItem())
        }
    }

    @Test
    fun `fetchMediaItems sets error on failure`() = runTest {
        `when`(repository.getPlaylistsByScreenKey("Test")).thenThrow(RuntimeException("Network error"))
        `when`(repository.getModified("Test")).thenReturn(111111)

        viewModel.fetchMediaItems("Test")
        val error = viewModel.error.first { it != null }

        assertEquals(error, "Network error")
        assertEquals(emptyList<MediaItem>(), viewModel.mediaItems.value)
    }

    @Test
    fun `fetchMediaItems sets loading correctly`() = runTest {
        `when`(repository.getPlaylistsByScreenKey("Test")).thenReturn(emptyList())
        `when`(repository.getModified("Test")).thenReturn(111111)

        viewModel.loading.test {
            assertEquals(true, awaitItem()) // initial
            viewModel.fetchMediaItems("Test")
            assertEquals(false, awaitItem()) // loading ended
        }
    }
}