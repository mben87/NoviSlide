package com.novislide.data

import com.novislide.data.remote.model.PlaylistResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface NoviAPI {
    @GET("PlayerBackend/screen/playlistItems/{key}")
    suspend fun getPlaylists(@Path("key") screenKey: String): PlaylistResponse
}