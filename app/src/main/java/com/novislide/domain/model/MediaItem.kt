package com.novislide.domain.model

data class MediaItem(
    val mediaUrl: String,
    val duration: Long
) {

    val mediaType: MediaType
        get() = MediaType.fromUrl(mediaUrl)
}

enum class MediaType {
    IMAGE,
    VIDEO,
    UNKNOWN;

    companion object {
        fun fromUrl(url: String): MediaType {
            return when {
                url.endsWith(".jpg", true) || url.endsWith(".jpeg", true) || url.endsWith(
                    ".png",
                    true
                ) || url.endsWith(".gif", true) ->
                    IMAGE

                url.endsWith(".mp4", true) || url.endsWith(".mov", true) || url.endsWith(
                    ".webm",
                    true
                ) ->
                    VIDEO

                else -> UNKNOWN
            }
        }
    }
}