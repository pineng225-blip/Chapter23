package com.mansu.universe
import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.*
import kotlin.math.sqrt

/** Local-only microphone loudness detector. Nothing is recorded, stored or uploaded. Detects sustained loud noise, not "perfect breath". */
class BlowDetector(private val onBlow: () -> Unit, private val onError: (String) -> Unit) {
    private var job: Job? = null
    @SuppressLint("MissingPermission")
    fun start(scope: CoroutineScope) {
        if (job != null) return
        job = scope.launch(Dispatchers.IO) {
            val rate = 16000
            val min = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
            if (min <= 0) { withContext(Dispatchers.Main) { onError("No microphone available.") }; return@launch }
            val rec = try { AudioRecord(MediaRecorder.AudioSource.MIC, rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, min * 2) } catch (e: Exception) { null }
            if (rec == null || rec.state != AudioRecord.STATE_INITIALIZED) { withContext(Dispatchers.Main) { onError("Couldn't open the microphone.") }; return@launch }
            val buf = ShortArray(1024); var smooth = 0.0; var loudMs = 0; var floor = 0.02
            try {
                rec.startRecording()
                while (isActive) {
                    val n = rec.read(buf, 0, buf.size); if (n <= 0) continue
                    var s = 0.0; for (i in 0 until n) { val v = buf[i] / 32768.0; s += v * v }
                    smooth = smooth * .8 + sqrt(s / n) * .2
                    if (smooth < floor * 2) floor = floor * .98 + smooth * .02 // adaptive noise floor
                    if (smooth > maxOf(.12, floor * 4)) loudMs += n * 1000 / rate else loudMs = 0
                    if (loudMs > 450) { withContext(Dispatchers.Main) { onBlow() }; break }
                }
            } finally { try { rec.stop() } catch (_: Exception) {}; rec.release() }
        }
    }
    fun stop() { job?.cancel(); job = null }
}
