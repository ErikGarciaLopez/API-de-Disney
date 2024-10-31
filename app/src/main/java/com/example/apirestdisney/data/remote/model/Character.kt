package com.example.apirestdisney.data.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterDetail(
    @SerializedName("data")
    var data: List<Data>
)

data class Data (
    @SerializedName("name")
    var name: String,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("films")
    var films: List<String>
)