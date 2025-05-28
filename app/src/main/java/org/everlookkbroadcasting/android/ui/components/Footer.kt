package org.everlookkbroadcasting.android.ui.components

import VolumeUp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.everlookkbroadcasting.android.R
import org.everlookkbroadcasting.android.ui.theme.Blue
import org.everlookkbroadcasting.android.ui.theme.DarkWhite
import org.everlookkbroadcasting.android.ui.theme.Grey90

@Composable
fun Footer(
    currentVolume: Float,
    onValueChange: (Float) -> Unit,
    maxVolume: Float
) {
    // Footer: Title and volume control
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Everlook Broadcasting Co.",
            color = DarkWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.volume_up),
                contentDescription = "Volume",
                tint = Color.White
            )
            Slider(
                value = currentVolume,
                colors = SliderDefaults.colors(
                    activeTrackColor = Blue,
                    thumbColor = Blue,
                    inactiveTrackColor = Grey90
                ),
                onValueChange = onValueChange,
                valueRange = 0f..maxVolume,
                modifier = Modifier
                    .width(280.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}
