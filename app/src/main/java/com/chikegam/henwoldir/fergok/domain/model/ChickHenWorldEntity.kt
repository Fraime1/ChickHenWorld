package com.chikegam.henwoldir.fergok.domain.model

import com.google.gson.annotations.SerializedName


data class ChickHenWorldEntity (
    @SerializedName("ok")
    val chickHenWorldOk: String,
    @SerializedName("url")
    val chickHenWorldUrl: String,
    @SerializedName("expires")
    val chickHenWorldExpires: Long,
)