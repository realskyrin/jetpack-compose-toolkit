import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min

data class CardInfo(val id: Int, val title: String, val color: Color)

/**
 * 这个组件是一个卡片轮播。每张卡片都被放置在一个圆形的路径上，位置由角度决定。
 * 角度根据卡片的索引和轮播的偏移量计算得出。
 * 卡片的缩放比例和位置都是根据其相对于轮播中心的角度动态计算的，从而模拟出了3D轮播的效果
 */
@Composable
fun Carousel(
    modifier: Modifier = Modifier,
    cards: List<CardInfo>,
    radius: Dp = 250.dp, // Radius for the carousel
    onClick: (card: CardInfo) -> Unit = {},
) {
    var offset by remember { mutableFloatStateOf(0f) }
    val animatedOffset = animateFloatAsState(targetValue = offset, label = "offset animation")

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        offset -= dragAmount.x / 100
                    },
                    onDragEnd = {
                        val nearestCardAngle = findNearestCardAngle(offset, cards.size)
                        offset = nearestCardAngle
                    },
                )
            }
    ) {
        cards.forEachIndexed { index, card ->
            /**
             * 在这段代码中，cards.size % 2 == 1 是用来判断卡片的数量是否为奇数的。
             * 如果卡片的数量是奇数，那么每张卡片的偏移索引就是其在卡片列表中的索引加上 0.5，否则偏移索引就是其在卡片列表中的索引。
             * 这样做的目的是为了确保卡片能够均匀地分布在轮播的圆周上。
             * 当卡片的数量是奇数时，轮播的中心位置会落在两张卡片的中间，所以需要将每张卡片的偏移索引加上 0.5，以使得卡片能够均匀地分布在轮播的圆周上。
             * 当卡片的数量是偶数时，轮播的中心位置会正好落在一张卡片上，所以每张卡片的偏移索引就是其在卡片列表中的索引。
             * 所以，这段代码的作用就是根据卡片的数量和卡片在列表中的索引，计算出卡片在轮播中的偏移索引。
             */
            val offsetIndex = if (cards.size % 2 == 1) index + 0.5 else index.toDouble()

            /**
             * 卡片的角度是根据卡片在轮播中的位置计算出来的。每张卡片都被放置在一个圆形的路径上，这个路径的中心就是轮播的中心。每张卡片的位置都由一个角度来决定，这个角度是相对于轮播中心的。
             *
             * 这里的角度计算公式是 `(2 * PI / cards.size) * offsetIndex - PI / 2 + animatedOffset`。这个公式的含义是：
             *
             * - `2 * PI / cards.size`：这部分是计算出每张卡片之间的角度间隔。因为一个完整的圆的角度是 `2 * PI`，所以这里除以卡片的数量就可以得到每张卡片之间应该有多大的角度间隔。
             * - `offsetIndex`：这是卡片的索引，经过一定的调整以确保卡片能均匀分布在圆周上。
             * - `- PI / 2`：这部分是为了调整卡片的起始位置，使得第一张卡片位于轮播的最上方。
             * - `offset`：这是一个偏移量，用于在用户拖动轮播时更新卡片的位置。
             *
             * 所以，这里的“卡片角度”实际上是指卡片在轮播中的位置，用角度来表示。
             */
            val angle = (2 * PI / cards.size) * offsetIndex - PI / 2 + animatedOffset.value

            /**
             * 在这段代码中，cos(angle) 是用来获取卡片相对于轮播中心的水平偏移量的。
             * angle 是卡片相对于轮播中心的角度，cos 函数会将这个角度转换为一个在 -1 和 1 之间的值。
             * 这个值会随着 angle 的增加而周期性地变化，当 angle 为 0 或 2π 时，cos(angle) 的值为 1，表示卡片位于轮播的最右侧；
             * 当 angle 为 π 时，cos(angle) 的值为 -1，表示卡片位于轮播的最左侧。
             * radius.value 是卡片轮播的半径，它决定了卡片离轮播中心的距离。将 cos(angle) 与 radius.value 相乘，就可以得到卡片在x轴上的位置。
             * 所以，这段代码的作用就是根据卡片在轮播中的角度，计算出卡片在x轴上的位置。
             */
            val x = cos(angle) * radius.value

            /**
             * 在这段代码中，angle 是卡片相对于轮播中心的原始角度。
             * 首先，我们将 PI / 2 加到 angle 上，这是为了将角度范围从原来的 [0, 2π] 转换为 [-π, π]。
             * 然后，我们对 2 * PI 取余数，确保角度值在 [0, 2π] 范围内。最后，我们再减去 PI，将角度范围最终转换为 [-π, π]。
             * 这样，无论原始的 angle 是多少，adjustedAngle 都会是一个在 [-π, π] 范围内的角度值。
             * 这对于后续的计算非常有用，因为 cos 函数在这个范围内的输出是 [-1, 1]，这可以直接用于计算卡片的缩放比例。
             */
            val adjustedAngle = (angle + PI / 2) % (2 * PI) - PI

            /**
             * 这行代码是用来计算卡片的缩放比例的。`lerp`函数用于在两个值之间进行线性插值。
             * 在这个上下文中，`start`是`0.6f`，`stop`是`1f`。这两个值代表了卡片的最小和最大缩放值。
             * `fraction`是通过`(cos(adjustedAngle) + 1) / 2`计算出来的。
             *
             * `adjustedAngle`是卡片相对于轮播中心的角度，经过调整后的范围在`[-π, π]`之间。
             * `cos`函数用于将这个角度转换为`-1`和`1`之间的值。将这个值加上`1`然后除以`2`，
             * 可以将范围转换为`[0, 1]`，这样就可以作为`lerp`函数中的`fraction`了。
             *
             * 所以，卡片的缩放比例是由其相对于轮播中心的角度决定的。
             * 卡片越接近中心（即`adjustedAngle`越小），缩放比例就越接近`1f`（卡片看起来更大）。
             * 卡片离中心越远，缩放比例就越接近`0.6f`（卡片看起来更小）。
             */
            val scale = lerp(0.6f, 1f, (cos(adjustedAngle) + 1) / 2)

            Card(
                modifier = Modifier
                    .size(200.dp, 150.dp)
                    .graphicsLayer(
                        translationX = x.toFloat(),
                        scaleX = scale,
                        scaleY = scale,
                    )
                    .align(Alignment.Center)
                    .zIndex(scale)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onClick(card) },
                colors = CardDefaults.cardColors(
                    containerColor = card.color,
                )
            ) {
                Text(modifier = Modifier.padding(16.dp), text = card.title)
            }
        }
    }
}

// 计算最接近当前偏移量的卡片角度
fun findNearestCardAngle(currentOffset: Float, cardCount: Int): Float {
    // 将当前偏移量标准化到 [0, 2*PI) 范围内
    val normalizedOffset =
        ((currentOffset % (2 * PI)).toFloat() + (2 * PI).toFloat()) % (2 * PI).toFloat()
    val singleCardAngle = (2 * PI / cardCount).toFloat()
    var nearestCardAngle = 0f
    var minDifference = Float.MAX_VALUE

    for (i in 0 until cardCount) {
        val targetAngle = i * singleCardAngle
        var difference = abs(targetAngle - normalizedOffset)

        // 考虑循环轮播的情况，比如从最后一张卡片平滑过渡到第一张卡片
        difference = min(difference, (2 * PI).toFloat() - difference)

        if (difference < minDifference) {
            minDifference = difference
            nearestCardAngle = targetAngle
        }
    }

    // 调整 nearestCardAngle 以使其尽可能接近当前 offset
    // 如果 nearestCardAngle 是 0，且卡片在轮播中靠左，那么就需要将 nearestCardAngle 调整为 2*PI
    nearestCardAngle += if (nearestCardAngle == 0f && normalizedOffset > 1) {
        (currentOffset - normalizedOffset) + (2 * PI).toFloat()
    } else {
        (currentOffset - normalizedOffset)
    }

    return nearestCardAngle
}

fun lerp(start: Float, stop: Float, fraction: Double): Float =
    (1 - fraction.toFloat()) * start + fraction.toFloat() * stop
