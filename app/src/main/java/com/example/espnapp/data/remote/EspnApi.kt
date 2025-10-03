package com.example.espnapp.data.remote

import com.example.espnapp.model.news.NewsResponse
import com.example.espnapp.model.scoreboard.ScoreboardResponse
import com.example.espnapp.model.teams.TeamsResponse
import com.example.espnapp.model.roster.TeamDetailResponse
import com.example.espnapp.model.player.PlayerStatsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EspnSiteApi {
    // EPL news
    @GET("apis/site/v2/sports/soccer/eng.1/news")
    suspend fun getPremierLeagueNews(): NewsResponse

    // Scoreboard by league and date (YYYYMMDD)
    @GET("apis/site/v2/sports/soccer/{league}/scoreboard")
    suspend fun getScoreboard(
        @Path("league") league: String,
        @Query("dates") yyyymmdd: String
    ): ScoreboardResponse

    // EPL Teams
    @GET("apis/site/v2/sports/soccer/eng.1/teams")
    suspend fun getPremierLeagueTeams(): TeamsResponse

    // Team detail with roster enabled (?enable=roster)
    @GET("apis/site/v2/sports/soccer/eng.1/teams/{teamId}")
    suspend fun getTeamWithRoster(
        @Path("teamId") teamId: String,
        @Query("enable") enable: String = "roster"
    ): TeamDetailResponse
}

interface EspnCoreApi {
    // Player stats (core v2)
    @GET("v2/sports/soccer/athletes/{athleteId}/statistics")
    suspend fun getPlayerStats(
        @Path("athleteId") athleteId: String,
        @Query("season") season: Int
    ): PlayerStatsResponse
}

object EspnApis {
    val site = RetrofitProvider.site.create(EspnSiteApi::class.java)
    val core = RetrofitProvider.core.create(EspnCoreApi::class.java)
}
