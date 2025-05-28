import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DottedSpinningThrobber(
    modifier: Modifier = Modifier,
    dotCount: Int = 12,
    dotColor: Color = Color.Gray,
    dotRadius: Dp = 6.dp,
    throbberRadius: Dp = 24.dp,
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition()
    // Animate a float from 0 to 1, repeating forever
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing)
        )
    )

    Canvas(
        modifier = modifier.size(throbberRadius * 2)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radiusPx = (throbberRadius - dotRadius).toPx()
        val dotRadiusPx = dotRadius.toPx()

        repeat(dotCount) { i ->
            // Calculate the angle for this dot
            val angle = 2 * PI * i / dotCount
            // Determine the animation phase for this dot
            val dotPhase = ((progress * dotCount) - i + dotCount) % dotCount / dotCount
            // Animate the alpha (opacity) so the dots "spin"
            val alpha = 0.3f + 0.7f * (1f - dotPhase).coerceIn(0f, 1f)
            // Position of the dot
            val x = center.x + radiusPx * cos(angle).toFloat()
            val y = center.y + radiusPx * sin(angle).toFloat()

            drawCircle(
                color = dotColor.copy(alpha = alpha),
                radius = dotRadiusPx,
                center = Offset(x, y)
            )
        }
    }
}
