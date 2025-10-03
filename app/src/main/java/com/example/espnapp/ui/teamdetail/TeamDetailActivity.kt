package com.example.espnapp.ui.teamdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.espnapp.databinding.ActivityTeamDetailBinding
import com.example.espnapp.data.remote.EspnApis
import com.example.espnapp.model.roster.Athlete
import com.example.espnapp.model.roster.AthleteGroup
import com.example.espnapp.ui.player.PlayerDetailActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonElement

class TeamDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_TEAM_ID = "team_id"
        const val EXTRA_TEAM_NAME = "team_name"
        const val EXTRA_TEAM_LOGO = "team_logo"
    }

    private lateinit var b: ActivityTeamDetailBinding
    private val adapter = RosterAdapter { openPlayer(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTeamDetailBinding.inflate(layoutInflater)
        setContentView(b.root)

        val teamId = intent.getStringExtra(EXTRA_TEAM_ID) ?: return finish()
        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter

        b.txtTitle.text = intent.getStringExtra(EXTRA_TEAM_NAME) ?: ""
        val logo = intent.getStringExtra(EXTRA_TEAM_LOGO)
        if (!logo.isNullOrBlank()) Picasso.get().load(logo).into(b.imgLogo)

        b.viewError.btnRetry.setOnClickListener { load(teamId) }
        load(teamId)
    }

    private fun load(teamId: String) {
        render(loading = true)
        lifecycleScope.launch {
            try {
                val res = EspnApis.site.getTeamWithRoster(teamId)
                val list = parseAthletes(res.team?.athletes)
                if (list.isEmpty()) render(empty = true) else {
                    adapter.submit(list)
                    render(list = true)
                }
            } catch (e: Exception) {
                render(error = "Failed to load roster.")
            }
        }
    }

    private fun openPlayer(a: Athlete) {
        val i = Intent(this, PlayerDetailActivity::class.java)
        i.putExtra(PlayerDetailActivity.EXTRA_PLAYER_ID, a.id)
        i.putExtra(PlayerDetailActivity.EXTRA_PLAYER_NAME, a.displayName)
        i.putExtra(PlayerDetailActivity.EXTRA_PLAYER_POS, a.position?.abbr)
        i.putExtra(PlayerDetailActivity.EXTRA_PLAYER_PHOTO, a.headshot?.href)
        startActivity(i)
    }

    private fun render(loading:Boolean=false, list:Boolean=false, empty:Boolean=false, error:String?=null) {
        b.progress.visibility = if (loading) View.VISIBLE else View.GONE
        b.recycler.visibility = if (list) View.VISIBLE else View.GONE
        b.viewEmpty.root.visibility = if (empty) View.VISIBLE else View.GONE
        b.viewError.root.visibility = if (error!=null) View.VISIBLE else View.GONE
        b.viewError.txtError.text = error ?: ""
    }

    private fun parseAthletes(athletesJson: JsonElement?): List<Athlete> {
        if (athletesJson == null || !athletesJson.isJsonArray) return emptyList()
        val arr = athletesJson.asJsonArray
        if (arr.size() == 0) return emptyList()

        return if (arr[0].isJsonObject && arr[0].asJsonObject.has("items")) {
            // Shape A: grouped -> List<AthleteGroup> then flatten
            val type = object : TypeToken<List<AthleteGroup>>() {}.type
            val groups: List<AthleteGroup> = Gson().fromJson(arr, type)
            groups.flatMap { it.items ?: emptyList() }
        } else {
            // Shape B: flat -> List<Athlete>
            val type = object : TypeToken<List<Athlete>>() {}.type
            Gson().fromJson(arr, type)
        }
    }

}
