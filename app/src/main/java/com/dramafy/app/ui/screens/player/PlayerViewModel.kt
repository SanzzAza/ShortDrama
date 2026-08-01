package com.dramafy.app.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dramafy.app.data.model.Episode
import com.dramafy.app.data.model.VideoItem
import com.dramafy.app.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerUiState(
    val isLoading: Boolean = true,
    val episodes: List<Episode> = emptyList(),
    val videos: List<VideoItem> = emptyList(),
    val currentEpisode: Int = 0,
    val videoUrl: String = "",
    val error: String? = null
)

class PlayerViewModel : ViewModel() {

    private val repository = NetworkModule.repository

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadPlayer(dramaId: String, startEpisode: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load series episodes
            val seriesResult = repository.getSeries(dramaId)
            seriesResult.fold(
                onSuccess = { seriesData ->
                    val episodes = seriesData?.episodeList ?: emptyList()
                    val currentEp = episodes.getOrNull(startEpisode)
                    val videoUrl = currentEp?.displayVideoUrl ?: ""

                    _uiState.value = PlayerUiState(
                        isLoading = false,
                        episodes = episodes,
                        currentEpisode = startEpisode,
                        videoUrl = videoUrl
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )

            // Also try multi-video
            val multiResult = repository.getMultiVideo(dramaId)
            multiResult.fold(
                onSuccess = { data ->
                    val videos = data?.videos ?: data?.list ?: data?.data ?: emptyList()
                    if (videos.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(videos = videos)
                        if (_uiState.value.videoUrl.isBlank()) {
                            videos.getOrNull(startEpisode)?.let {
                                _uiState.value = _uiState.value.copy(videoUrl = it.displayUrl)
                            }
                        }
                    }
                },
                onFailure = { /* ignore, series data is sufficient */ }
            )
        }
    }

    fun selectEpisode(index: Int) {
        val episodes = _uiState.value.episodes
        val episode = episodes.getOrNull(index) ?: return
        val videoUrl = episode.displayVideoUrl

        _uiState.value = _uiState.value.copy(
            currentEpisode = index,
            videoUrl = videoUrl
        )
    }
}
