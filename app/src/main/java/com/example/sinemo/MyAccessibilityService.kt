package com.example.sinemo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import java.io.IOException
import java.util.Date
var numRec = 0
var output: String = ""
var mediaRecorder: MediaRecorder? = null
var state: Boolean = false

var isRecording = false
val audioViewModel = AudioViewModel()
@Suppress("DEPRECATION")
class MyAccessibilityService : AccessibilityService() {
    private val tag = "RecorderService"
    @SuppressLint("NewApi")
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName == "org.telegram.messenger" || packageName == "com.instagram.android") {
                if (!isRecording) {
                    Log.d("Window is", "open")
                    startRecording()
                    isRecording = true
                }
            } else {
                if (isRecording) {
                    Log.d("Window is", "closed")
                    stopRecording(audioViewModel)
                    isRecording = false
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.v(tag, "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.v(tag, "onServiceConnected")
        val info = AccessibilityServiceInfo()
        info.flags = AccessibilityServiceInfo.DEFAULT
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }

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
}