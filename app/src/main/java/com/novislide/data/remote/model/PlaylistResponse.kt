package com.novislide.data.remote.model

import com.google.gson.annotations.SerializedName
import com.novislide.domain.model.Playlist

data class PlaylistResponse(
    @SerializedName("screenKey") val screenKey: String,
    @SerializedName("playlists") val playlists: List<Playlist>
)