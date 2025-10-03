package com.example.espnapp.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.espnapp.common.UiState
import com.example.espnapp.data.remote.EspnApis
import com.example.espnapp.model.news.Article
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _state = MutableLiveData<UiState<List<Article>>>()
    val state: LiveData<UiState<List<Article>>> = _state

    private var currentLeague = DEFAULT_LEAGUE

    fun loadNews(league: String? = null) {
        val targetLeague = league ?: currentLeague
        currentLeague = targetLeague
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val res = EspnApis.site.getLeagueNews(targetLeague)
                val list = res.articles.orEmpty()
                _state.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list)
            } catch (e: Exception) {
                _state.value = UiState.Error("Failed to load news.", e)
            }
        }
    }

    companion object {
        private const val DEFAULT_LEAGUE = "eng.1"
    }
}