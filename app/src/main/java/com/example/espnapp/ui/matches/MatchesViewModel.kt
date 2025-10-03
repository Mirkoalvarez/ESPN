package com.example.espnapp.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.espnapp.common.UiState
import com.example.espnapp.data.remote.EspnApis
import com.example.espnapp.model.scoreboard.Event
import kotlinx.coroutines.launch

class MatchesViewModel : ViewModel() {
    private val _state = MutableLiveData<UiState<List<Event>>>()
    val state: LiveData<UiState<List<Event>>> = _state

    fun load(league: String, yyyymmdd: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val res = EspnApis.site.getScoreboard(league, yyyymmdd)
                val list = res.events.orEmpty()
                _state.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list)
            } catch (e: Exception) {
                _state.value = UiState.Error("Failed to load matches.", e)
            }
        }
    }
}
