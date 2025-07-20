package com.novislide.data

import com.novislide.data.remote.model.ModifiedResponse
import com.novislide.data.remote.model.PlaylistResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NoviAPI {
    @GET("PlayerBackend/screen/playlistItems/{key}")
    suspend fun getPlaylists(@Path("key") screenKey: String): PlaylistResponse

    @GET("PlayerBackend/screen/playlistItems/{key}")
    suspend fun getModified(@Path("key") screenKey: String): ModifiedResponse
}