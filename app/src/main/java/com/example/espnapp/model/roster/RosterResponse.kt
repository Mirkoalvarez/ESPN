package com.example.espnapp.model.roster

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RosterResponse(@SerializedName("athletes") val athletes: List<AthleteGroup>?)

data class AthleteGroup(
    @SerializedName("items") val items: List<Athlete>?
)

data class Athlete(
    @SerializedName("id") val id: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("position") val position: Position?,
    @SerializedName("headshot") val headshot: Headshot?
) : Serializable

data class Position(@SerializedName("abbreviation") val abbr: String?) : Serializable
data class Headshot(@SerializedName("href") val href: String?) : Serializable
