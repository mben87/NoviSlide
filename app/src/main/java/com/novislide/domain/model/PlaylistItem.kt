package com.novislide.domain.model

import com.google.gson.annotations.SerializedName

data class PlaylistItem(
    @SerializedName("orderKey") val orderKey: Int,
    @SerializedName("playlistKey") val playlistKey: String,
    @SerializedName("creativeKey") val creativeKey: String,
    @SerializedName("duration") val duration: Int
)