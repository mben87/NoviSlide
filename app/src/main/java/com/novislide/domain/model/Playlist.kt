package com.novislide.domain.model

import com.google.gson.annotations.SerializedName

data class Playlist(
    @SerializedName("playlistItems") val playlistItems: List<PlaylistItem>
)