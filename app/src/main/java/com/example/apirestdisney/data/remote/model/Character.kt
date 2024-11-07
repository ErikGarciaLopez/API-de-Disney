package com.example.apirestdisney.data.remote.model

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("data")
    var data: List<Data>
)

data class Data (
    @SerializedName("_id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("films")
    var films: List<String>
)