package com.mansu.universe
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

/** Parallax stars (slower = farther) and two nebula glows, drawn on the GPU-backed Compose canvas. */
@Composable
fun Galaxy(modifier: Modifier = Modifier, starCount: Int = 220) {
    val stars = remember { List(starCount) { Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) } }
    var t by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) { while (true) withFrameNanos { t = it / 1e9f } }
    Canvas(modifier.fillMaxSize()) {
        drawRect(Color(0xFF050818))
        val c1 = Offset(size.width * .75f, size.height * .25f)
        drawCircle(Brush.radialGradient(listOf(Color(0x666A3CD8), Color.Transparent), c1, size.width * .8f), size.width * .8f, c1)
        val c2 = Offset(size.width * .15f, size.height * .8f)
        drawCircle(Brush.radialGradient(listOf(Color(0x552850E0), Color.Transparent), c2, size.width * .7f), size.width * .7f, c2)
        for ((x, y, p) in stars) {
            val a = .35f + .65f * abs(sin(t * (.4f + p) + p * 9f))
            drawCircle(Color(0xFFDCE6FF).copy(alpha = a), .6f + p * 1.6f, Offset(x * size.width, (y * size.height + t * p * 5f) % size.height))
        }
    }
}
