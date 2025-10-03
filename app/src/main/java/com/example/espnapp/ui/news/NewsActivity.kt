package com.example.espnapp.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.espnapp.R
import com.example.espnapp.common.UiState
import com.example.espnapp.databinding.ActivityNewsBinding
import com.example.espnapp.ui.matches.MatchesActivity
import com.example.espnapp.ui.matches.setOnItemSelectedListenerCompat
import com.example.espnapp.ui.teams.TeamsActivity

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val vm: NewsViewModel by viewModels()
    private val adapter = NewsAdapter { url ->
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private val leagues = listOf(
        "Premier League" to "eng.1",
        "MLS" to "usa.1",
        "Europa League" to "uefa.europa",
        "Liga argentina" to "arg.1",
        "Liga española" to "esp.1"
    )
    private var selectedLeagueId = DEFAULT_LEAGUE_ID
    private var selectedIndex = 0

    private val initialLeagueId: String by lazy {
        intent.getStringExtra(EXTRA_LEAGUE_ID) ?: DEFAULT_LEAGUE_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.spnLeague.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            leagues.map { it.first }
        )

        val initialIndex = leagues.indexOfFirst { it.second == initialLeagueId }.takeIf { it >= 0 } ?: 0
        updateLeagueSelection(initialIndex, triggerLoad = false)
        binding.spnLeague.setSelection(initialIndex)

        binding.spnLeague.setOnItemSelectedListenerCompat { position ->
            if (position != selectedIndex) {
                updateLeagueSelection(position)
            }
        }

        binding.btnGoMatches.setOnClickListener {
            startActivity(Intent(this, MatchesActivity::class.java))
        }
        binding.btnGoTeams.setOnClickListener {
            startActivity(Intent(this, TeamsActivity::class.java))
        }
        binding.viewError.btnRetry.setOnClickListener {
            render(loading = true)
            vm.loadNews(selectedLeagueId)
        }

        vm.state.observe(this) { state ->
            when (state) {
                is UiState.Loading -> render(loading = true)
                is UiState.Success -> {
                    adapter.submitList(state.data)
                    render(list = state.data.isNotEmpty())
                }
                is UiState.Empty -> render(empty = true)
                is UiState.Error -> render(error = state.message)
            }
        }
        render(loading = true)
        vm.loadNews(selectedLeagueId)
    }

    private fun render(
        loading: Boolean = false,
        list: Boolean = false,
        empty: Boolean = false,
        error: String? = null
    ) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.recycler.visibility = if (list) View.VISIBLE else View.GONE
        binding.viewEmpty.root.visibility = if (empty) View.VISIBLE else View.GONE
        binding.viewError.root.visibility = if (error != null) View.VISIBLE else View.GONE
        binding.viewError.txtError.text = error ?: ""
    }

    private fun updateLeagueSelection(position: Int, triggerLoad: Boolean = true) {
        selectedIndex = position
        selectedLeagueId = leagues[position].second
        binding.txtLeagueTitle.text = getString(R.string.news_league_title, leagues[position].first)
        if (triggerLoad) {
            render(loading = true)
            vm.loadNews(selectedLeagueId)
        }
    }

    companion object {
        const val EXTRA_LEAGUE_ID = "extra_league_id"
        private const val DEFAULT_LEAGUE_ID = "eng.1"
    }
}
