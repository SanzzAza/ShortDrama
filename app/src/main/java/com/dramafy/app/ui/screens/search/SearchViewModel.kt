package com.dramafy.app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dramafy.app.data.model.DramaItem
import com.dramafy.app.di.NetworkModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val suggestions: List<String> = emptyList(),
    val results: List<DramaItem> = emptyList(),
    val hasSearched: Boolean = false,
    val error: String? = null
)

class SearchViewModel : ViewModel() {

    private val repository = NetworkModule.repository
    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        // Debounce suggestions
        searchJob?.cancel()
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300)
                val result = repository.searchSuggest(query)
                result.fold(
                    onSuccess = { suggestions ->
                        _uiState.value = _uiState.value.copy(suggestions = suggestions)
                    },
                    onFailure = {
                        _uiState.value = _uiState.value.copy(suggestions = emptyList())
                    }
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(suggestions = emptyList())
        }
    }

    fun search(query: String = _uiState.value.query) {
        if (query.isBlank()) return

        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            query = query,
            isSearching = true,
            hasSearched = true,
            suggestions = emptyList(),
            error = null
        )

        viewModelScope.launch {
            val result = repository.search(query)
            result.fold(
                onSuccess = { items ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        results = items
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        error = error.message,
                        results = emptyList()
                    )
                }
            )
        }
    }

    fun clearSearch() {
        _uiState.value = SearchUiState()
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel() as T
            }
        }
    }
}
