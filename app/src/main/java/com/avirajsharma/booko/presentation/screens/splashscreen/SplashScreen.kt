package com.avirajsharma.booko.presentation.screens.splashscreen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avirajsharma.booko.R

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {

    var startAnimation by remember { mutableStateOf(false) }

    // Scale animation with a slight overshoot for a "pop" effect
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = { OvershootInterpolator(3f).getInterpolation(it) }
        ),
        label = "entrance_scale"
    )

    // Subtle continuous pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0CDB5), // Matches system splash background
                        Color(0xFFF2C9AD)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.booko),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(
                        scaleX = scale * pulseScale,
                        scaleY = scale * pulseScale,
                        alpha = scale.coerceIn(0f, 1f)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Text with Slide + Fade entrance
            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(initialOffsetY = { 40 })
            ) {
                Text(
                    text = "Explore and Download Free Books",
                    fontSize = 20.sp,
                    color = Color(0xFF9d6950), // Changed to White for visibility
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}