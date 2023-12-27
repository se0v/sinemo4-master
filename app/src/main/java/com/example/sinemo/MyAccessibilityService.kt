package com.example.sinemo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.util.Log
import android.view.accessibility.AccessibilityEvent
var numRec = 0
var output: String = ""
var mediaRecorder: MediaRecorder? = null
var state: Boolean = false

var isRecording = false
val audioViewModel = AudioViewModel()
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
}