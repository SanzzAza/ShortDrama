package com.dramafy.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dramafy.app.data.model.BookMallSection
import com.dramafy.app.data.model.DramaItem
import com.dramafy.app.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val banners: List<DramaItem> = emptyList(),
    val trending: List<DramaItem> = emptyList(),
    val sections: List<BookMallSection> = emptyList(),
    val newReleases: List<DramaItem> = emptyList(),
    val topRated: List<DramaItem> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = NetworkModule.repository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.getBookMall()
            result.fold(
                onSuccess = { response ->
                    val allItems = response.resolvedSections.flatMap { section -> section.displayItems }
                    val banners = response.banners.ifEmpty {
                        allItems.take(5)
                    }

                    // Extract sections from response
                    val sections = response.resolvedSections

                    // Try to identify section types by name/title
                    val trending = findSectionItems(sections, listOf("trending", "popular", "hot", "trending now"))
                    val newReleases = findSectionItems(sections, listOf("new", "latest", "new release", "recent"))
                    val topRated = findSectionItems(sections, listOf("top rated", "top", "best", "recommended"))

                    _uiState.value = HomeUiState(
                        isLoading = false,
                        banners = banners,
                        trending = trending.ifEmpty { allItems.take(10) },
                        sections = sections.filter { it.displayItems.isNotEmpty() },
                        newReleases = newReleases.ifEmpty { allItems.drop(10).take(10) },
                        topRated = topRated.ifEmpty { allItems.drop(20).take(10) }
                    )
                },
                onFailure = { error ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        error = error.message ?: "Failed to load content"
                    )
                }
            )
        }
    }

    private fun findSectionItems(
        sections: List<BookMallSection>,
        keywords: List<String>
    ): List<DramaItem> {
        return sections.firstOrNull { section ->
            val name = (section.title.ifBlank { section.name }).lowercase()
            keywords.any { keyword -> name.contains(keyword) }
        }?.displayItems ?: emptyList()
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel() as T
            }
        }
    }
}
