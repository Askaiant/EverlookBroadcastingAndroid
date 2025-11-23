package org.everlookkbroadcasting.android.ui.components

import DottedSpinningThrobber
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.everlookkbroadcasting.android.R
import org.everlookkbroadcasting.android.models.PlayerState
import org.everlookkbroadcasting.android.ui.theme.Black100
import org.everlookkbroadcasting.android.ui.theme.Black40
import org.everlookkbroadcasting.android.ui.theme.Black80
import org.everlookkbroadcasting.android.ui.utils.createGradientBrush

@Composable
fun PlayerControls(
    playerState: PlayerState,
    onPlayPauseClick: () -> Unit,
    onVolumeDown: () -> Unit,
    onVolumeUp: () -> Unit
) {

    // Circular container for controls
    Box(
        modifier = Modifier
            .size(260.dp)
            .shadow(
                elevation = 4.dp,
                shape = CircleShape,
                spotColor = Black80
            )
            .background(
                brush = createGradientBrush(
                    colors = listOf(Black100, Black40),
                    isVertical = true
                ),
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Top Row: Controls
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                NoRippleIconButton(onClick = onVolumeDown) {
                    Icon(
                        painter = painterResource(R.drawable.volume_down),
                        contentDescription = "Volume Down",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = Black80
                        )
                        .background(
                            brush = createGradientBrush(
                                colors = listOf(Black100, Black40),
                                isVertical = true
                            ),
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (playerState) {
                        PlayerState.Idle -> {
                            NoRippleIconButton(onClick = onPlayPauseClick) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        PlayerState.Loading -> {
                            DottedSpinningThrobber(
                                dotCount = 7,
                                dotColor = Color.White,
                                dotRadius = 6.dp,
                                throbberRadius = 20.dp,
                                animationDuration = 900
                            )
                        }
                        PlayerState.Playing -> {
                            NoRippleIconButton(onClick = onPlayPauseClick) {
                                Icon(
                                    painter = painterResource(R.drawable.pause),
                                    contentDescription = "Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                NoRippleIconButton(onClick = onVolumeUp) {
                    Icon(
                        painter = painterResource(R.drawable.volume_up),
                        contentDescription = "Volume Up",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}