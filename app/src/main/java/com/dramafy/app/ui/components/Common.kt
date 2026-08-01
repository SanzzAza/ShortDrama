package com.dramafy.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dramafy.app.ui.theme.SurfaceVariant

@Composable
fun ShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        ),
        label = "shimmer_translate"
    )

    return Brush.linearGradient(
        colors = listOf(
            SurfaceVariant,
            Color(0xFF2A2A36),
            SurfaceVariant
        ),
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
fun ShimmerCard(
    width: Dp = 140.dp,
    height: Dp = 190.dp,
    modifier: Modifier = Modifier
) {
    val brush = ShimmerBrush()
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(brush)
    )
}

@Composable
fun LoadingShimmerRow(
    modifier: Modifier = Modifier,
    itemCount: Int = 4
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(itemCount) {
            ShimmerCard()
        }
    }
}
