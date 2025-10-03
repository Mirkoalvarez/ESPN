package com.example.espnapp.ui.teams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.espnapp.common.UiState
import com.example.espnapp.data.remote.EspnApis
import com.example.espnapp.model.teams.Team
import kotlinx.coroutines.launch

class TeamsViewModel : ViewModel() {
    private val _state = MutableLiveData<UiState<List<Team>>>()
    val state: LiveData<UiState<List<Team>>> = _state

    fun load(leagueId: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val resp = EspnApis.site.getTeams(leagueId)
                val teams = resp.sports.orEmpty()
                    .flatMap { it.leagues.orEmpty() }
                    .flatMap { it.teams.orEmpty() }
                    .mapNotNull { it.team }
                _state.value = if (teams.isEmpty()) UiState.Empty else UiState.Success(teams)
            } catch (e: Exception) {
                _state.value = UiState.Error("Failed to load teams.", e)
            }
        }
    }
}
