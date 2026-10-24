package com.mansu.universe
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = darkColorScheme()) { App() } }
    }
}

private val Gold = Color(0xFFF4DFB0); private val Ice = Color(0xFFDCE6FF)

@Composable
fun App() {
    var entered by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    val now by produceState(LocalDateTime.now()) { while (true) { delay(1000); value = LocalDateTime.now() } }
    Box(Modifier.fillMaxSize()) {
        Galaxy()
        LazyColumn(Modifier.fillMaxSize().systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally, contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(40.dp)) {
            item { AgeSection(now) }
            item { CakeSection() }
            item { Sec("A Little Something From My Heart") { Button({ dialog = Config.GIFT_TITLE to Config.GIFT_LETTER }) { Text("Open the gift 🎁") } } }
            item { Sec("Open These Whenever Your Heart Needs Me") { Config.OPEN_WHEN.forEach { c -> OutlinedButton({ dialog = c }, Modifier.fillMaxWidth()) { Text(c.first) } } } }
            item { HeartSection() }
            item { AnnivSection(now) }
            item { Sec("One Last Letter Before You Go…") { Button({ dialog = "For Mansu ♡" to Config.FINAL_LETTER }) { Text("Open the envelope 💌") } } }
        }
        dialog?.let { (t, m) -> AlertDialog(onDismissRequest = { dialog = null }, confirmButton = { TextButton({ dialog = null }) { Text("Close") } }, title = { Text(t, fontFamily = FontFamily.Serif) }, text = { Column(Modifier.heightIn(max = 400.dp).verticalScrollCompat()) { Text(m) } }) }
        if (!entered) Intro { entered = true }
    }
}

@Composable private fun Modifier.verticalScrollCompat() = this.then(Modifier.verticalScroll(rememberScrollState()))

@Composable
fun Sec(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, fontFamily = FontFamily.Serif, fontSize = 26.sp, color = Ice, textAlign = TextAlign.Center); content()
    }
}

@Composable
fun Intro(onEnter: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { delay(800); step = 1; delay(4000); step = 2; delay(2500); step = 3 }
    Box(Modifier.fillMaxSize().background(Color(0xEE02040D)).padding(32.dp), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("Somewhere in this endless universe,\nthere is a girl who makes my world beautiful.", Modifier.alpha(animateFloatAsState(if (step >= 1) 1f else 0f, tween(2000), label = "a").value), fontFamily = FontFamily.Serif, fontSize = 22.sp, color = Ice, textAlign = TextAlign.Center)
            Text("${Config.NAME}, this little universe was made just for you. ♡", Modifier.alpha(animateFloatAsState(if (step >= 2) 1f else 0f, tween(2000), label = "b").value), fontFamily = FontFamily.Serif, fontSize = 20.sp, color = Gold, textAlign = TextAlign.Center)
            if (step >= 3) Button(onEnter) { Text("ENTER YOUR UNIVERSE") }
        }
    }
}

@Composable
fun CounterRow(s: Span) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
        listOf("Years" to s.years, "Months" to s.months, "Days" to s.days, "Hours" to s.hours, "Min" to s.minutes, "Sec" to s.seconds).forEach { (l, v) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("$v", fontSize = 22.sp, fontFamily = FontFamily.Serif, color = Ice); Text(l, fontSize = 11.sp, color = Color(0xFF9FB0E6)) }
        }
    }
}

@Composable
fun AgeSection(now: LocalDateTime) {
    val s = span(Config.BIRTH, now); val bday = isBirthday(now)
    Sec(if (bday) "Happy ${s.years}rd Birthday, ${Config.NAME}!" else "${Config.NAME}'s Age") {
        Text(if (bday) "Chapter ${s.years} — A New Year of Your Beautiful Life\nYour ${s.years}rd birthday begins your ${s.years + 1}th year of life." else "Chapter ${s.years}: living your ${s.years + 1}th year of life", color = Gold, textAlign = TextAlign.Center)
        CounterRow(s)
        if (!bday) { val n = span(now, nextBirthday(now)); Text("Next birthday in ${n.months + n.years * 12} mo ${n.days} d ${n.hours} h ${n.minutes} m ${n.seconds} s", color = Ice) }
    }
}

@Composable
fun AnnivSection(now: LocalDateTime) {
    Sec("Our Story, Written in the Stars") {
        Text("Since February 9, 2022, my favorite story has been us.", color = Gold, textAlign = TextAlign.Center)
        CounterRow(span(Config.ANNIV, now))
        if (now.month == Config.ANNIV.month && now.dayOfMonth == Config.ANNIV.dayOfMonth) Text("✨ Happy anniversary! ✨", fontSize = 24.sp, color = Gold)
    }
}

@Composable
fun CakeSection() {
    val ctx = LocalContext.current; val scope = rememberCoroutineScope()
    var out by remember { mutableStateOf(false) }; var msg by remember { mutableStateOf("Take a deep breath, and blow out your candles. ♡") }
    val detector = remember { BlowDetector({ out = true }, { msg = "$it Use the button instead." }) }
    DisposableEffect(Unit) { onDispose { detector.stop() } }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok -> if (ok) { msg = "Listening… blow gently. Audio stays on this phone."; detector.start(scope) } else msg = "Microphone permission denied. Use the button instead." }
    val flame by rememberInfiniteTransition(label = "f").animateFloat(.85f, 1.15f, infiniteRepeatable(tween(250), RepeatMode.Reverse), label = "flame")
    val fade by animateFloatAsState(if (out) 0f else 1f, tween(1200), label = "fade")
    Sec("Make a wish, my love…") {
        Canvas(Modifier.size(240.dp)) {
            val w = size.width
            drawRoundRect(Brush.verticalGradient(listOf(Color(0xFF3B4FC4), Color(0xFF1A2670))), Offset(w * .12f, w * .55f), Size(w * .76f, w * .35f), CornerRadius(24f))
            drawRoundRect(Color(0xFFE9E1FF), Offset(w * .18f, w * .42f), Size(w * .64f, w * .18f), CornerRadius(20f))
            listOf(.35f, .5f, .65f).forEach { x ->
                drawRect(Color(0xFFF4DFB0), Offset(w * x - 6f, w * .26f), Size(12f, w * .16f))
                if (fade > 0f) {
                    drawCircle(Color(0x44FFB347).copy(alpha = .3f * fade), 40f * fade, Offset(w * x, w * .2f))
                    drawOval(Color(0xFFFFB347).copy(alpha = fade), Offset(w * x - 8f * flame * fade, w * .2f - 6f * fade), Size(16f * flame * fade, 34f * flame * fade))
                } else drawCircle(Color(0x33FFFFFF), 10f, Offset(w * x, w * .15f))
            }
        }
        if (out) Text("Happy 23rd Birthday, ${Config.NAME}!\nMay every beautiful dream of yours come true.", color = Gold, fontFamily = FontFamily.Serif, fontSize = 20.sp, textAlign = TextAlign.Center) else {
            Text(msg, color = Ice, textAlign = TextAlign.Center)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button({ if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) { msg = "Listening… blow gently."; detector.start(scope) } else launcher.launch(Manifest.permission.RECORD_AUDIO) }) { Text("🎤 Use microphone") }
                OutlinedButton({ detector.stop(); out = true }) { Text("Blow Out Candles") }
            }
            Text("The microphone is analysed on-device only; nothing is recorded or uploaded.", fontSize = 12.sp, color = Color(0xFF9FB0E6), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun HeartSection() {
    val vib = LocalContext.current.getSystemService(Vibrator::class.java)
    var held by remember { mutableStateOf(false) }
    val beat by rememberInfiniteTransition(label = "b").animateFloat(1f, 1.18f, infiniteRepeatable(tween(400), RepeatMode.Reverse), label = "beat")
    Sec("Feel My Heart Beating for You") {
        Text("Touch and hold my heart to feel it beating for you.", color = Ice, textAlign = TextAlign.Center)
        Text("♥", fontSize = 120.sp, color = Color(0xFFFF6FA0), modifier = Modifier.scale(if (held) beat else 1f).pointerInput(Unit) {
            detectTapGestures(onPress = {
                held = true
                coroutineScope {
                    val j = launch { while (true) { if (vib?.hasVibrator() == true) vib.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 60, 100, 60), -1)); delay(800) } }
                    try { tryAwaitRelease() } finally { j.cancel(); vib?.cancel(); held = false }
                }
            })
        })
        Text("Even across the distance, imagine my heart beating for you. ♡", color = Gold, textAlign = TextAlign.Center)
        if (vib?.hasVibrator() != true) Text("This device has no vibration motor, so only the visual beat plays.", fontSize = 12.sp, color = Color(0xFF9FB0E6))
    }
}
