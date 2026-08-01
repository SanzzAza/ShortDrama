package com.dramafy.app.ui.screens.bookmall

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dramafy.app.data.model.BookMallSection
import com.dramafy.app.data.model.BookMallTab
import com.dramafy.app.data.model.DramaItem
import com.dramafy.app.di.NetworkModule
import com.dramafy.app.ui.components.DramaCardVertical
import com.dramafy.app.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BrowseUiState(
    val isLoading: Boolean = true,
    val tabs: List<BookMallTab> = emptyList(),
    val selectedTab: String = "all",
    val sections: List<BookMallSection> = emptyList(),
    val allItems: List<DramaItem> = emptyList(),
    val error: String? = null
)

class BrowseViewModel : ViewModel() {

    private val repository = NetworkModule.repository

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    init {
        loadBrowse()
    }

    fun loadBrowse() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val tabsResult = repository.getBookMallTabs()
            tabsResult.fold(
                onSuccess = { tabs ->
                    _uiState.value = _uiState.value.copy(tabs = tabs)
                },
                onFailure = { /* ignore */ }
            )

            val mallResult = repository.getBookMall()
            mallResult.fold(
                onSuccess = { response ->
                    val allItems = response.resolvedSections.flatMap { section -> section.displayItems }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sections = response.resolvedSections.filter { section -> section.displayItems.isNotEmpty() },
                        allItems = allItems
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    fun selectTab(tabId: String) {
        _uiState.value = _uiState.value.copy(selectedTab = tabId)
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return BrowseViewModel() as T
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onDramaClick: (String) -> Unit,
    viewModel: BrowseViewModel = viewModel(factory = BrowseViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())
                Text(
                    text = "Browse",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                if (uiState.tabs.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            val isSelected = uiState.selectedTab == "all"
                            Surface(
                                onClick = { viewModel.selectTab("all") },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Primary else ChipBg
                            ) {
                                Text(
                                    text = "All",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) Color.White else ChipText,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        items(uiState.tabs) { tab ->
                            val isSelected = uiState.selectedTab == tab.id
                            Surface(
                                onClick = { viewModel.selectTab(tab.id) },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Primary else ChipBg
                            ) {
                                Text(
                                    text = tab.name.ifBlank { tab.title },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) Color.White else ChipText,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Failed to load", color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.loadBrowse() }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                gridItems(uiState.allItems) { drama ->
                    DramaCardVertical(
                        drama = drama,
                        onClick = { onDramaClick(drama.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
