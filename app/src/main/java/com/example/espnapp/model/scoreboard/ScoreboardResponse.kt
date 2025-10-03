package com.example.espnapp.model.scoreboard

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ScoreboardResponse(
    @SerializedName("events") val events: List<Event>?
)

data class Event(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("competitions") val competitions: List<Competition>?,
    @SerializedName("status") val status: Status?
) : Serializable

data class Competition(
    @SerializedName("competitors") val competitors: List<Competitor>?,
    @SerializedName("venue") val venue: Venue?
) : Serializable

data class Competitor(
    @SerializedName("id") val id: String?,
    @SerializedName("homeAway") val homeAway: String?,
    @SerializedName("score") val score: String?,
    @SerializedName("team") val team: Team?
) : Serializable

data class Team(
    @SerializedName("id") val id: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("logo") val logo: String?
) : Serializable

data class Venue(@SerializedName("fullName") val fullName: String?) : Serializable
data class Status(@SerializedName("type") val type: StatusType?) : Serializable
data class StatusType(@SerializedName("description") val description: String?) : Serializable
