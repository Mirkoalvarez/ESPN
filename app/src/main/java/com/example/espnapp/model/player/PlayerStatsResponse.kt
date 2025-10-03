package com.example.espnapp.model.player

import com.google.gson.annotations.SerializedName

data class PlayerStatsResponse(
    @SerializedName("splits") val splits: Splits?
)

data class Splits(@SerializedName("categories") val categories: List<Category>?)
data class Category(
    @SerializedName("name") val name: String?,
    @SerializedName("stats") val stats: List<Stat>?
)
data class Stat(
    @SerializedName("name") val name: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("value") val value: String?
)
