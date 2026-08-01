package com.dramafy.app.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dramafy.app.data.model.DramaItem
import com.dramafy.app.ui.components.*
import com.dramafy.app.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onDramaClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Scaffold(
        containerColor = Background,
        topBar = {
            // Minimal top bar with search and notification
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Text(
                    text = "DramaFy",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Banner / Featured carousel
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceVariant)
                    )
                }
            } else if (uiState.banners.isNotEmpty()) {
                item {
                    BannerCarousel(
                        items = uiState.banners,
                        onItemClick = { onDramaClick(it.id) }
                    )
                }
            }

            // Trending section
            if (uiState.isLoading) {
                item {
                    Column {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .width(120.dp)
                                .height(22.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(SurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LoadingShimmerRow()
                    }
                }
            } else if (uiState.trending.isNotEmpty()) {
                item {
                    SectionHeader(title = "Trending Now")
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.trending) { drama ->
                            DramaCardVertical(
                                drama = drama,
                                onClick = { onDramaClick(drama.id) }
                            )
                        }
                    }
                }
            }

            // Additional sections from API
            if (!uiState.isLoading) {
                items(uiState.sections.take(4)) { section ->
                    val title = section.title.ifBlank { section.name }
                    if (title.isNotBlank() && section.displayItems.isNotEmpty()) {
                        SectionHeader(title = title)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(section.displayItems.take(10)) { drama ->
                                DramaCardVertical(
                                    drama = drama,
                                    onClick = { onDramaClick(drama.id) }
                                )
                            }
                        }
                    }
                }
            }

            // New Releases
            if (!uiState.isLoading && uiState.newReleases.isNotEmpty()) {
                item {
                    SectionHeader(title = "New Releases")
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.newReleases) { drama ->
                            DramaCardCompact(
                                drama = drama,
                                onClick = { onDramaClick(drama.id) }
                            )
                        }
                    }
                }
            }

            // Error state
            if (uiState.error != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Something went wrong",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadHome() },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerCarousel(
    items: List<DramaItem>,
    onItemClick: (DramaItem) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    val configuration = LocalConfiguration.current

    // Auto-scroll
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSpacing = 12.dp
            ) { page ->
                val drama = items[page]
                DramaCardFeatured(
                    drama = drama,
                    onClick = { onItemClick(drama) }
                )
            }
        }

        // Page indicators
        if (items.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(items.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage) Primary
                                else Color.White.copy(alpha = 0.2f)
                            )
                    )
                }
            }
        }
    }
}
