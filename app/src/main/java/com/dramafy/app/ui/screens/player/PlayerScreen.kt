package com.dramafy.app.ui.screens.player

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.dramafy.app.ui.theme.*

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun PlayerScreen(
    dramaId: String,
    startEpisode: Int,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(dramaId) {
        viewModel.loadPlayer(dramaId, startEpisode)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setVideoScalingMode(androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
        }
    }

    // Set video source when URL changes
    LaunchedEffect(uiState.videoUrl) {
        if (uiState.videoUrl.isNotBlank()) {
            exoPlayer.setMediaItem(MediaItem.fromUri(uiState.videoUrl))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Video player
        if (uiState.videoUrl.isNotBlank()) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No video available",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Top overlay with back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(8.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // Episode selector at bottom
        if (uiState.episodes.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            ) {
                // Current episode info
                val currentEp = uiState.episodes.getOrNull(uiState.currentEpisode)
                if (currentEp != null) {
                    Text(
                        text = currentEp.displayName,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(uiState.episodes) { index, episode ->
                        val isSelected = index == uiState.currentEpisode
                        Surface(
                            onClick = { viewModel.selectEpisode(index) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Primary else Color.White.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${episode.displayEpisode}",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
