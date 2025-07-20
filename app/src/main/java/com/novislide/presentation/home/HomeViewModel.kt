package com.novislide.presentation.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novislide.data.repository.PlaylistRepository
import com.novislide.domain.model.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PlaylistRepository
) : ViewModel() {
    private var refreshJob: Job? = null

    private val _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())
    val mediaItems: StateFlow<List<MediaItem>> = _mediaItems.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    var localModified: Long = 0

    init {
        _loading.value = true
        _error.value = null
    }

    /**
     * Starts a repeating job that refreshes media items every 10 minutes for the given [screenKey].
     */
    fun loadMediaItems(screenKey: String) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (isActive) {
                fetchMediaItems(screenKey)
                delay(10 * 60 * 1000L) // 10 minutes
            }
        }
    }

    /**
     * Fetches the latest media items if the 'modified' timestamp has changed.
     * Updates the local state accordingly.
     *
     * Marked as @VisibleForTesting to allow direct testing of the data fetching logic without delay or scheduling.
     */
    @VisibleForTesting
    suspend fun fetchMediaItems(screenKey: String) {
        try {
            val modified = repository.getModified(screenKey)

            if (localModified < modified) {
                localModified = modified

                val items = repository.getPlaylistsByScreenKey(screenKey)
                _mediaItems.value = items
            }
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }
}
