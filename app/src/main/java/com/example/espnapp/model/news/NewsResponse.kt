package com.example.espnapp.model.news

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsResponse(
    @SerializedName("articles") val articles: List<Article>?
)

data class Article(
    @SerializedName("headline") val headline: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("images") val images: List<Image>?,
    @SerializedName("links") val links: Links?
) : Serializable

data class Image(@SerializedName("url") val url: String?) : Serializable

data class Links(@SerializedName("web") val web: WebLink?) : Serializable
data class WebLink(@SerializedName("href") val href: String?) : Serializable
