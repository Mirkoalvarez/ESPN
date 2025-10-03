package com.example.espnapp.ui.teams

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.espnapp.R
import com.example.espnapp.common.UiState
import com.example.espnapp.databinding.ActivityTeamsBinding
import com.example.espnapp.model.teams.Team
import com.example.espnapp.ui.teamdetail.TeamDetailActivity


class TeamsActivity : AppCompatActivity() {
    private lateinit var b: ActivityTeamsBinding
    private val vm: TeamsViewModel by viewModels()
    private val adapter = TeamsAdapter { team -> openTeam(team) }

    private val leagueOptions = listOf(
        LeagueOption("esp.1", R.string.league_spanish),
        LeagueOption("eng.1", R.string.league_premier),
        LeagueOption("arg.1", R.string.league_argentine),
        LeagueOption("usa.1", R.string.league_mls)
    )
    private var selectedLeagueId = leagueOptions.first().id
    private var isFirstSelection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTeamsBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter
        setupLeagueSpinner()
        b.viewError.btnRetry.setOnClickListener { vm.load(selectedLeagueId) }

        vm.state.observe(this) {
            when (it) {
                is UiState.Loading -> render(loading = true)
                is UiState.Success -> { adapter.submit(it.data); render(list = it.data.isNotEmpty()) }
                is UiState.Empty -> render(empty = true)
                is UiState.Error -> render(error = it.message)
            }
        }
    }

    private fun openTeam(team: Team) {
        val i = Intent(this, TeamDetailActivity::class.java)
        i.putExtra(TeamDetailActivity.EXTRA_TEAM_ID, team.id)
        i.putExtra(TeamDetailActivity.EXTRA_TEAM_NAME, team.displayName)
        i.putExtra(TeamDetailActivity.EXTRA_TEAM_LOGO, team.logo)
        i.putExtra(TeamDetailActivity.EXTRA_LEAGUE_ID, selectedLeagueId)
        startActivity(i)
    }

    private fun setupLeagueSpinner() {
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            leagueOptions.map { getString(it.labelRes) }
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        b.spinnerLeague.adapter = spinnerAdapter
        b.spinnerLeague.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val newLeague = leagueOptions.getOrNull(position)?.id ?: return
                val shouldLoad = newLeague != selectedLeagueId || isFirstSelection
                if (shouldLoad) {
                    selectedLeagueId = newLeague
                    vm.load(selectedLeagueId)
                    isFirstSelection = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    private fun render(loading:Boolean=false, list:Boolean=false, empty:Boolean=false, error:String?=null) {
        b.progress.visibility = if (loading) View.VISIBLE else View.GONE
        b.recycler.visibility = if (list) View.VISIBLE else View.GONE
        b.viewEmpty.root.visibility = if (empty) View.VISIBLE else View.GONE
        b.viewError.root.visibility = if (error!=null) View.VISIBLE else View.GONE
        b.viewError.txtError.text = error ?: ""
    }

    private data class LeagueOption(val id: String, val labelRes: Int)
}
