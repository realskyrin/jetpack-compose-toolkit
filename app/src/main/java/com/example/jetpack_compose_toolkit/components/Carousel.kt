import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min

/**
 * A carousel of cards that can be dragged and rotated.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param cards The list of cards to be displayed.
 * @param radius The radius of the circle on which the cards are placed.
 * @param cardWith The width of the cards.
 * @param cardHeight The height of the cards.
 * @param elevation The elevation of the cards.
 * @param cornerRadius The cornerRadius of the cards.
 * @param sensitivity The sensitivity of the drag gesture.
 * @param maxScale The maximum scale of the cards.
 * @param minScale The minimum scale of the cards.
 * @param border The border of the cards.
 * @param onClick The callback to be invoked when a card is clicked.
 * @param cardContent The content of the card.
 */
@Composable
fun <T> CardCarousel(
    modifier: Modifier = Modifier,
    cards: List<T>,
    radius: Dp = 250.dp,
    cardWith: Dp = 200.dp,
    cardHeight: Dp = 150.dp,
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    sensitivity: Float = 1f,
    maxScale: Float = 1f,
    minScale: Float = 0.6f,
    border: BorderStroke? = null,
    onClick: (card: T) -> Unit = {},
    cardContent: @Composable (card: T) -> Unit,
) {
    var offset by remember { mutableFloatStateOf(0f) }
    val animatedOffset = animateFloatAsState(targetValue = offset, label = "offset animation")

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        offset -= dragAmount.x / 100 * sensitivity
                    },
                    onDragEnd = {
                        val nearestCardAngle = findNearestCardAngle(offset, cards.size)
                        offset = nearestCardAngle
                    },
                )
            }
    ) {
        cards.forEachIndexed { index, card ->
            val offsetIndex = if (cards.size % 2 == 1) index + 0.5 else index.toDouble()
            val angle = (2 * PI / cards.size) * offsetIndex - PI / 2 + animatedOffset.value
            val x = cos(angle) * radius.value
            val adjustedAngle = (angle + PI / 2) % (2 * PI) - PI
            val scale = lerp(minScale, maxScale, (cos(adjustedAngle) + 1) / 2)

            Card(
                modifier = Modifier
                    .size(cardWith, cardHeight)
                    .graphicsLayer(
                        translationX = x.toFloat(),
                        scaleX = scale,
                        scaleY = scale,
                    )
                    .align(Alignment.Center)
                    .zIndex(scale)
                    .clip(RoundedCornerShape(cornerRadius))
                    .clickable { onClick(card) },
                elevation = CardDefaults.cardElevation(elevation),
                shape = RoundedCornerShape(cornerRadius),
                border = border,
            ) {
                cardContent(card)
            }
        }
    }
}

private fun findNearestCardAngle(currentOffset: Float, cardCount: Int): Float {
    val normalizedOffset =
        ((currentOffset % (2 * PI)).toFloat() + (2 * PI).toFloat()) % (2 * PI).toFloat()
    val singleCardAngle = (2 * PI / cardCount).toFloat()
    var nearestCardAngle = 0f
    var minDifference = Float.MAX_VALUE

    for (i in 0 until cardCount) {
        val targetAngle = i * singleCardAngle
        var difference = abs(targetAngle - normalizedOffset)
        difference = min(difference, (2 * PI).toFloat() - difference)

        if (difference < minDifference) {
            minDifference = difference
            nearestCardAngle = targetAngle
        }
    }

    nearestCardAngle += if (nearestCardAngle == 0f && normalizedOffset > 1.05) {
        (currentOffset - normalizedOffset) + (2 * PI).toFloat()
    } else {
        (currentOffset - normalizedOffset)
    }

    return nearestCardAngle
}

private fun lerp(start: Float, stop: Float, fraction: Double): Float =
    (1 - fraction.toFloat()) * start + fraction.toFloat() * stop
