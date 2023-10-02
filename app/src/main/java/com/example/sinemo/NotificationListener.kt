package com.example.sinemo
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
class NotificationListener: NotificationListenerService() {
    private object ApplicationPackageNames {
        const val TELEGRAM_PACK_NAME = "org.telegram.messenger"
    }
    object InterceptedNotificationCode {
        const val TELEGRAM_CODE = 1
        const val OTHER_APPS_CODE = 2 //ignore all notification
    }
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
    override fun onNotificationPosted(sbn: StatusBarNotification?) {

        val notificationCode: Int = matchNotificationCode(sbn!!)

        if (notificationCode != InterceptedNotificationCode.OTHER_APPS_CODE) {
            val intent = Intent("com.example.sinemo")
            intent.putExtra("Notification Code", notificationCode)
            sendBroadcast(intent)
        }
    }
    private fun matchNotificationCode(sbn: StatusBarNotification): Int {
        val packageName = sbn.packageName
        return if (packageName == ApplicationPackageNames.TELEGRAM_PACK_NAME) {
            InterceptedNotificationCode.TELEGRAM_CODE
        } else {
            InterceptedNotificationCode.OTHER_APPS_CODE
        }
    }
}