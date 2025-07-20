package com.novislide.presentation.home

import android.util.Log
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

    fun loadMediaItems(screenKey: String) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val modified = repository.getModified(screenKey)
                    Log.d("HomeViewModel", "Modified: $modified")

                    if (localModified != modified) {
                        localModified = modified

                        val items = repository.getPlaylistsByScreenKey(screenKey)
                        _mediaItems.value = items
                    }
                } catch (e: Exception) {
                    _error.value = e.message
                } finally {
                    _loading.value = false
                }
                Log.d("HomeViewModel", "Refreshing media items")
                delay(10 * 1000L) // 10 minutes
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }
}
