package org.everlookkbroadcasting.android.services

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.exoplayer.ExoPlayer

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        // Maybe for later use. This enables maybe more stable playback if iffy connection
        // Apparently experimental
        // val loadControl = DefaultLoadControl.Builder()
        //     .setBufferDurationsMs(
        //         5000,            // Minimum buffer to maintain during playback
        //         5000,            // Maximum buffer (optional: can be higher)
        //         5000,            // Buffer required before starting playback
        //         5000             // Buffer required after rebuffering
        //     ).build()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession?.player?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaSession?.release()
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
}
