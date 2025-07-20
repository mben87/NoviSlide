package com.novislide.data.remote.model

import com.google.gson.annotations.SerializedName

data class ModifiedResponse(
    @SerializedName("modified") val modified: Long
)