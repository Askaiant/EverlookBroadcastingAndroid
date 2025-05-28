package org.everlookkbroadcasting.android.ui.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import org.everlookkbroadcasting.android.R
import org.everlookkbroadcasting.android.models.PlayerState
import org.everlookkbroadcasting.android.ui.components.PlayerControls
import org.everlookkbroadcasting.android.ui.components.Footer
import org.everlookkbroadcasting.android.ui.theme.Black90


@Composable
fun StreamPlayerScreen() {
    val streamUrl = "https://radio.turtle-music.org/stream"
    val context = LocalContext.current
    var playerState by remember { mutableStateOf(PlayerState.Idle) }
    var exoPlayer: ExoPlayer? by remember { mutableStateOf(null) }


    // Play/Pause logic
    val onPlayPauseClick = {
        when (playerState) {
            PlayerState.Idle -> {
                playerState = PlayerState.Loading
                val player = ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(streamUrl))
                    prepare()
                    playWhenReady = true
                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(state: Int) {
                            if (state == Player.STATE_READY) {
                                playerState = PlayerState.Playing
                            }
                        }
                        override fun onPlayerError(error: PlaybackException) {
                            playerState = PlayerState.Idle
                        }
                    })
                }
                exoPlayer = player
            }
            PlayerState.Playing -> {
                exoPlayer?.pause()
                exoPlayer?.release()
                exoPlayer = null
                playerState = PlayerState.Idle
            }
            PlayerState.Loading -> {
                // Optionally allow canceling loading
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    var currentVolume by remember { mutableStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "android.media.VOLUME_CHANGED_ACTION") {
                    val newVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    currentVolume = newVolume
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter("android.media.VOLUME_CHANGED_ACTION"))
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    val onValueChange: (Float) -> Unit = {
        val vol = it.toInt().coerceIn(0, maxVolume)
        println(vol)
        currentVolume = vol
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0)
    }

    val volumeUp = {
        val newVol = currentVolume + 1
        if (newVol > maxVolume) {
            // Do nothing if newVol exceeds maxVolume
        } else {
            currentVolume = newVol
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0)
        }
    }

    val volumeDown = {
        val newVol = currentVolume - 1
        if (newVol < 0) {
            // Do nothing if newVol exceeds maxVolume
        } else {
            currentVolume = newVol
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0)
        }
    }

    // Main container with padding and background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black90) // Bootstrap light background
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Album art.value
            Image(
                painter = painterResource(id = R.drawable.radio_logo),
                contentDescription = "Radio Logo",
                modifier = Modifier
                    .size(360.dp)
            )
            PlayerControls(
                playerState = playerState,
                onPlayPauseClick = onPlayPauseClick,
                onVolumeDown = volumeDown,
                onVolumeUp = volumeUp
            )
            Footer(
                currentVolume = currentVolume.toFloat(),
                onValueChange = onValueChange,
                maxVolume = maxVolume.toFloat()
            )
        }
    }
}

