package org.everlookkbroadcasting.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.everlookkbroadcasting.android.ui.screens.StreamPlayerScreen
import org.everlookkbroadcasting.android.ui.theme.EverlookBroadcastingTheme
import com.shakebugs.shake.Shake

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Shake.setCrashReportingEnabled(true)
        Shake.start(this, BuildConfig.SHAKE_API_KEY)

        enableEdgeToEdge()
        setContent {
            EverlookBroadcastingTheme {
                StreamPlayerScreen()
            }
        }
    }
}

