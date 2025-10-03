package com.example.espnapp.ui.teams

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.espnapp.common.UiState
import com.example.espnapp.databinding.ActivityTeamsBinding
import com.example.espnapp.model.teams.Team
import com.example.espnapp.ui.teamdetail.TeamDetailActivity
import androidx.recyclerview.widget.LinearLayoutManager

class TeamsActivity : AppCompatActivity() {
    private lateinit var b: ActivityTeamsBinding
    private val vm: TeamsViewModel by viewModels()
    private val adapter = TeamsAdapter { team -> openTeam(team) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTeamsBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.recycler.layoutManager = LinearLayoutManager(this)
        b.recycler.adapter = adapter
        b.viewError.btnRetry.setOnClickListener { vm.load() }

        vm.state.observe(this) {
            when (it) {
                is UiState.Loading -> render(loading = true)
                is UiState.Success -> { adapter.submit(it.data); render(list = it.data.isNotEmpty()) }
                is UiState.Empty -> render(empty = true)
                is UiState.Error -> render(error = it.message)
            }
        }
        vm.load()
    }

    private fun openTeam(team: Team) {
        val i = Intent(this, TeamDetailActivity::class.java)
        i.putExtra(TeamDetailActivity.EXTRA_TEAM_ID, team.id)
        i.putExtra(TeamDetailActivity.EXTRA_TEAM_NAME, team.displayName)
        i.putExtra(TeamDetailActivity.EXTRA_TEAM_LOGO, team.logo)
        startActivity(i)
    }

    private fun render(loading:Boolean=false, list:Boolean=false, empty:Boolean=false, error:String?=null) {
        b.progress.visibility = if (loading) View.VISIBLE else View.GONE
        b.recycler.visibility = if (list) View.VISIBLE else View.GONE
        b.viewEmpty.root.visibility = if (empty) View.VISIBLE else View.GONE
        b.viewError.root.visibility = if (error!=null) View.VISIBLE else View.GONE
        b.viewError.txtError.text = error ?: ""
    }
}
