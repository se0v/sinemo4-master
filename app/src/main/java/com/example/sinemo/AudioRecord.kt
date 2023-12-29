@file:Suppress("DEPRECATION")

package com.example.sinemo

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.util.Date

var maxAmplitude = 0
val handler = Handler(Looper.getMainLooper())
val soundArr = mutableListOf<Float>()
val addArr = object : Runnable {
    override fun run() {
        if (mediaRecorder != null) {
            maxAmplitude = (20 * kotlin.math.log10(mediaRecorder!!.maxAmplitude / 1.0)).toInt()

            // Установка ограничений на значения
            val normalizedAmplitude = maxAmplitude.toFloat() / 200
            val clampedAmplitude = normalizedAmplitude.coerceIn(0F, 1F)

            soundArr.add(clampedAmplitude)

            handler.postDelayed(this, 100L)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
fun startRecording() {
    try {
        isRecording = true
        numRec++
        output = Environment.getExternalStorageDirectory().absolutePath + "/recording$numRec.ogg"
        mediaRecorder = MediaRecorder().apply{
            //setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            //setAudioSource(MediaRecorder.AudioSource.MIC)
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            setOutputFormat(MediaRecorder.OutputFormat.OGG)
            setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
            setAudioSamplingRate(48000)
            setOutputFile(output)}
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            handler.postDelayed(addArr, 100L)
            Log.d("REC", state.toString())
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            mediaRecorder = null
        } catch (e: IOException) {
            e.printStackTrace()
            mediaRecorder = null
        }
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
}

@SuppressLint("MissingPermission")
fun stopRecording(audioViewModel: AudioViewModel) {
    isRecording = false
    if (state) {
        try {
            mediaRecorder?.stop()
        } catch (stopException: RuntimeException) {
            stopException.printStackTrace()
        } finally {
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
        }
        state = false
        Log.d("REC", state.toString())
        handler.removeCallbacks(addArr)
        maxAmplitude = 0

        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        @SuppressLint("SimpleDateFormat")
        val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy ")
        val formattedDate = dateFormat.format(date)
        audioViewModel.addRecord(
            DataRecord(
                heading = "recording$numRec",
                subtext = formattedDate,
                audioPath = output
            )
        )
    }
}