package com.cabledrop.gauge.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.di.ServiceLocator
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.ui.HudBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    val palette = AppTheme.colors
    val transition = rememberInfiniteTransition(label = "splash")
    val glow by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    HudBackground(
        overlay = listOf(
            Color(0xFF060C11),
            Color(0xE60B141B),
            Color(0xF2101C24)
        ),
        gridAlpha = 0.35f,
        bracketAlpha = 0.45f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(240.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                palette.primary.copy(alpha = 0.33f * glow),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.minDimension / 2f
                        ),
                        radius = size.minDimension / 2f
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier
                        .size(148.dp)
                        .scale(0.94f + glow * 0.06f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                color = palette.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelLarge,
                color = palette.textTertiary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            Canvas(
                modifier = Modifier
                    .width(220.dp)
                    .height(16.dp)
            ) {
                val midY = size.height / 2f
                drawLine(
                    color = palette.surfaceAlt,
                    start = Offset(0f, midY),
                    end = Offset(size.width, midY),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                val headX = size.width * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(palette.primary, Color.Transparent),
                        center = Offset(headX, midY),
                        radius = 12.dp.toPx()
                    ),
                    radius = 12.dp.toPx(),
                    center = Offset(headX, midY)
                )
                drawCircle(
                    color = palette.primary,
                    radius = 2.5.dp.toPx(),
                    center = Offset(headX, midY)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(200.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = palette.primary,
                trackColor = palette.surfaceAlt
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.splash_status),
                style = MaterialTheme.typography.labelSmall,
                color = palette.textTertiary
            )
        }
    }
}
