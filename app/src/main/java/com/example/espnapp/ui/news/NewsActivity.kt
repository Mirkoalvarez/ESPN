package com.example.espnapp.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.espnapp.common.UiState
import com.example.espnapp.databinding.ActivityNewsBinding
import com.example.espnapp.ui.matches.MatchesActivity
import com.example.espnapp.ui.teams.TeamsActivity
import androidx.recyclerview.widget.LinearLayoutManager

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val vm: NewsViewModel by viewModels()
    private val adapter = NewsAdapter { url ->
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.btnGoMatches.setOnClickListener {
            startActivity(Intent(this, MatchesActivity::class.java))
        }
        binding.btnGoTeams.setOnClickListener {
            startActivity(Intent(this, TeamsActivity::class.java))
        }
        binding.viewError.btnRetry.setOnClickListener { vm.loadNews() }

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
        vm.loadNews()
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
}
