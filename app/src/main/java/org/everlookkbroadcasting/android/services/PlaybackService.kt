package org.everlookkbroadcasting.android.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import org.everlookkbroadcasting.android.R
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaSession
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import org.everlookkbroadcasting.android.BuildConfig

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "media_playback_channel"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(object : MediaSession.Callback {
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: List<MediaItem>
                ): ListenableFuture<List<MediaItem>> {
                    val updatedItems = mediaItems.map { item ->
                        when (item.mediaId) {
                            "stream" -> createStreamItem()
                            else -> item
                        }
                    }
                    return Futures.immediateFuture(updatedItems)
                }
            })
            .build()

        startForeground(NOTIFICATION_ID, createNotification())
        player.setMediaItem(createStreamItem())
        player.setHandleAudioBecomingNoisy(true)
    }


    private fun createStreamItem(): MediaItem {
        // Prepare the stream immediately so Android Auto shows "Now Playing"
        return MediaItem.Builder()
            .setMediaId("stream")
            .setUri(BuildConfig.RADIO_STREAM_URL)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Turtle Music Radio")
                    .setArtist("Everlooking Broadcasting")
                    .setIsPlayable(true)
                    .setMediaType(MediaMetadata.MEDIA_TYPE_RADIO_STATION)
                    .setArtworkData(
                        resources.openRawResource(+R.drawable.radio_logo).readBytes(),
                        MediaMetadata.PICTURE_TYPE_FRONT_COVER
                    )
                    .build()
            )
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Media Playback",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Media playback controls"
            setShowBadge(false)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): android.app.Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Turtle Music Radio")
            .setContentText("Ready to play")
            .setSmallIcon(R.drawable.radio_logo) // Make sure this icon exists
            .setOngoing(true)
            .setSilent(true)
            .build()
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
