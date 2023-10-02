package com.example.sinemo

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class AppLaunchReceiver : BroadcastReceiver() {

    private val telegramPackage = "org.telegram.messenger"

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AppLaunchReceiver", "Received broadcast")
        if (isTelegramRunning(context)) {
            startRecording()
        }
    }

    @SuppressLint("ServiceCast")
    private fun isTelegramRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = activityManager.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.processName == telegramPackage && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }
}