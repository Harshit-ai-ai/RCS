package com.example.rcslocationtracking.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class RideNotificationService :
    NotificationListenerService() {

    override fun onNotificationPosted(
        sbn: StatusBarNotification?
    ) {

        if (sbn == null) return

        val packageName =
            sbn.packageName

        val text =
            sbn.notification
                .extras
                .getCharSequence(
                    "android.text"
                )
                ?.toString()
                ?: ""

        if (
            packageName.contains("uber")
            ||
            packageName.contains("ola")
        ) {

            Log.d(
                "RIDE",
                text
            )

            // later:
            // parse driver
            // parse cab number
            // parse destination
            // trigger SMS
        }
    }
}