package com.dramafy.app.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dramafy.app.data.model.BookDetail
import com.dramafy.app.data.model.Episode
import com.dramafy.app.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val book: BookDetail? = null,
    val episodes: List<Episode> = emptyList(),
    val error: String? = null
)

class DetailViewModel : ViewModel() {

    private val repository = NetworkModule.repository

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadDetail(dramaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load book detail
            val bookResult = repository.getBook(dramaId)
            bookResult.fold(
                onSuccess = { book ->
                    _uiState.value = _uiState.value.copy(book = book)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    return@launch
                }
            )

            // Load episodes/series
            val seriesResult = repository.getSeries(dramaId)
            seriesResult.fold(
                onSuccess = { seriesData ->
                    val episodes = seriesData?.episodeList ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        episodes = episodes
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            )
        }
    }

    companion object {
        fun factory(dramaId: String) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                val vm = DetailViewModel()
                vm.loadDetail(dramaId)
                return vm as T
            }
        }
    }
}
