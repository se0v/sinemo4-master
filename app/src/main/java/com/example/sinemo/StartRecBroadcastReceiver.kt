package com.example.sinemo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

open class StartRecBroadcastReceiver: BroadcastReceiver() {
    private var startRec = false
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(p0: Context?, p1: Intent?) {
        val receivedNotificationCode: Int = p1!!.getIntExtra("Notification Code", -1)
        Log.d("RNC", receivedNotificationCode.toString())
        startInterceptedNotificationRec(receivedNotificationCode)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startInterceptedNotificationRec(notificationCode: Int) {
        when (notificationCode) {
            NotificationListener.InterceptedNotificationCode.TELEGRAM_CODE -> if (isRecording) stopRecording(
                audioViewModel) else startRecording()
            NotificationListener.InterceptedNotificationCode.OTHER_APPS_CODE -> startRec = false
        }
        Log.d("CHECK", startRec.toString())
    }
}