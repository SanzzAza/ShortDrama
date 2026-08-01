package com.dramafy.app.ui.screens.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dramafy.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    dramaId: String,
    onBack: () -> Unit,
    onPlayClick: (Int) -> Unit,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModel.factory(dramaId))
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
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
                    Text(
                        text = "Failed to load details",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.loadDetail(dramaId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Retry")
                    }
                }
            }
        } else {
            val book = uiState.book

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                // Hero section with cover
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    // Background cover
                    AsyncImage(
                        model = book?.displayCover,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlays
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Background.copy(alpha = 0.5f),
                                        Background
                                    )
                                )
                            )
                    )

                    // Cover card + info
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Cover image
                        AsyncImage(
                            model = book?.displayCover,
                            contentDescription = book?.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Info
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = book?.title ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Stats row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if ((book?.rating ?: 0.0) > 0) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = Tertiary
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = String.format("%.1f", book?.rating ?: 0.0),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                if ((book?.displayEpisodes ?: 0) > 0) {
                                    Text(
                                        text = "${book?.displayEpisodes} Eps",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = TextSecondary
                                    )
                                }

                                if ((book?.year ?: 0) > 0) {
                                    Text(
                                        text = "${book?.year}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Genre chips
                            val genres = (book?.genre ?: emptyList()) + (book?.tags ?: emptyList())
                            if (genres.isNotEmpty()) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    genres.take(3).forEach { genre ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = SurfaceVariant
                                        ) {
                                            Text(
                                                text = genre,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = ChipText
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Play button
                    Button(
                        onClick = { onPlayClick(0) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (uiState.episodes.isNotEmpty()) "Watch Now" else "Play",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Bookmark
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF33333F)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Share
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF33333F)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Synopsis
                val synopsis = book?.displayDescription
                if (!synopsis.isNullOrBlank()) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Synopsis",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = synopsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Cast
                val cast = book?.cast ?: emptyList()
                if (cast.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Cast",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cast.joinToString(", "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Episodes
                if (uiState.episodes.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Episodes",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uiState.episodes.size} Episodes",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Episodes grid
                        val columns = 5
                        val rows = (uiState.episodes.size + columns - 1) / columns
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(rows.coerceAtMost(10)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = (rows.coerceAtMost(10) * 52).dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.episodes) { episode ->
                                Surface(
                                    onClick = { onPlayClick(episode.displayEpisode - 1) },
                                    shape = RoundedCornerShape(10.dp),
                                    color = SurfaceVariant,
                                    border = if (episode.isFree) null else BorderStroke(
                                        1.dp, Color(0xFF33333F)
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(56.dp)
                                            .height(44.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "${episode.displayEpisode}",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            if (episode.isLocked) {
                                                Icon(
                                                    imageVector = Icons.Default.Lock,
                                                    contentDescription = "Locked",
                                                    modifier = Modifier.size(10.dp),
                                                    tint = TextTertiary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
