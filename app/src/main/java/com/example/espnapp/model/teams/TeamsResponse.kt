package com.example.espnapp.model.teams

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TeamsResponse(
    @SerializedName("sports") val sports: List<Sport>?
)

data class Sport(@SerializedName("leagues") val leagues: List<League>?)
data class League(@SerializedName("teams") val teams: List<TeamContainer>?)
data class TeamContainer(@SerializedName("team") val team: Team?)
data class Team(
    @SerializedName("id") val id: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("shortDisplayName") val shortName: String?,
    @SerializedName("logos") val logos: List<Logo>?
    ) : Serializable {
        val logo: String?
            get() = logos?.firstOrNull()?.href
    }

data class Logo(
    @SerializedName("href") val href: String?
) : Serializable
