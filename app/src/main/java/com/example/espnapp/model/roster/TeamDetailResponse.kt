package com.example.espnapp.model.roster

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonElement

// Wrapper for team detail when enabling roster via ?enable=roster
// { "team": { "athletes": [ { "items": [...] }, ... ] } }
data class TeamDetailResponse(
    @SerializedName("team") val team: TeamNode?
)

data class TeamNode(
    @SerializedName("athletes") val athletes: JsonElement?
)