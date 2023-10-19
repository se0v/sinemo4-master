@file:Suppress("DEPRECATION")

package com.example.sinemo
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
var output: String = ""
var mediaRecorder: MediaRecorder? = null
private var state: Boolean = false
var numRec = 0
var isRecording = false
val audioViewModel = AudioViewModel()
private val amplitudeJob = Job()
private val amplitudeScope = CoroutineScope(Dispatchers.Default + amplitudeJob)

@RequiresApi(Build.VERSION_CODES.Q)
fun startRecording() {
    try {
        isRecording = true
        numRec++
        output = Environment.getExternalStorageDirectory().absolutePath + "/recording$numRec.ogg"
        mediaRecorder = MediaRecorder().apply{
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.OGG)
        setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
        setAudioSamplingRate(48000)
        setOutputFile(output)}
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
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

        amplitudeScope.launch {
            val currentTimeMillis = System.currentTimeMillis()
            val date = Date(currentTimeMillis)
            @SuppressLint("SimpleDateFormat")
            val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy ")
            val formattedDate = dateFormat.format(date)
            audioViewModel.addRecord(
                DataRecord(
                    heading = "recording$numRec",
                    subtext = formattedDate.toString(),
                    audioPath = output
                )
            )
        }
    }
}

