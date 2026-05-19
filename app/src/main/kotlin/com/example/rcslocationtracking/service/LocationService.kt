package com.example.rcslocationtracking.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.telephony.SmsManager

import androidx.core.app.NotificationCompat

import com.google.android.gms.location.*

import com.example.rcslocationtracking.police.PoliceManager
import com.example.rcslocationtracking.hospital.HospitalManager
import com.example.rcslocationtracking.relative.RelativeManager

class LocationService : Service() {

    private lateinit var fusedClient:
            FusedLocationProviderClient

    override fun onCreate() {

        super.onCreate()

        fusedClient =
            LocationServices
                .getFusedLocationProviderClient(
                    this
                )

        startForeground(
            1,
            notification()
        )
    }

    override fun onStartCommand(

        intent: Intent?,

        flags: Int,

        startId: Int

    ): Int {

        val sender =
            intent
                ?.getStringExtra(
                    "sender"
                )

        if (sender == null) {

            stopSelf()

            return START_NOT_STICKY
        }

        val request =

            LocationRequest.Builder(

                Priority.PRIORITY_HIGH_ACCURACY,

                5000

            )

                .setMaxUpdates(1)

                .build()

        fusedClient.requestLocationUpdates(

            request,

            object : LocationCallback() {

                override fun onLocationResult(
                    result: LocationResult
                ) {

                    try {

                        val loc =
                            result.lastLocation
                                ?: return

                        val lat =
                            loc.latitude

                        val lon =
                            loc.longitude

                        val nearestPolice =
                            PoliceManager.getNearest(
                                applicationContext,
                                lat,
                                lon
                            )

                        val nearestHospital =
                            HospitalManager.getNearest(
                                applicationContext,
                                lat,
                                lon
                            )

                        val nearestRelative =
                            RelativeManager.getNearest(
                                applicationContext,
                                lat,
                                lon
                            )

                        val policeText =
                            nearestPolice?.name
                                ?: "Not found"

                        val hospitalText =
                            nearestHospital?.name
                                ?: "Not found"

                        val relativeText =
                            nearestRelative?.name
                                ?: "No relative"

                        val message = """
🚨 SOS ALERT 🚨

https://maps.google.com/?q=$lat,$lon

Police:
$policeText

Hospital:
$hospitalText

Relative:
$relativeText
""".trimIndent()

                        val sms =
                            SmsManager.getDefault()

                        val parts =
                            sms.divideMessage(
                                message
                            )

                        sms.sendMultipartTextMessage(

                            sender,

                            null,

                            parts,

                            null,

                            null
                        )

                    }

                    catch (e: Exception) {

                        e.printStackTrace()
                    }

                    stopSelf()
                }
            },

            Looper.getMainLooper()

        )

        return START_NOT_STICKY
    }

    private fun notification():
            Notification {

        val channelId =
            "location_channel"

        if (

            android.os.Build.VERSION.SDK_INT
            >= 26

        ) {

            val channel =

                NotificationChannel(

                    channelId,

                    "Location Service",

                    NotificationManager
                        .IMPORTANCE_LOW

                )

            getSystemService(

                NotificationManager::class.java

            )

                .createNotificationChannel(
                    channel
                )
        }

        return NotificationCompat.Builder(

            this,

            channelId

        )

            .setContentTitle(
                "Family Safety"
            )

            .setContentText(
                "Fetching location"
            )

            .setSmallIcon(
                android.R.drawable
                    .ic_menu_mylocation
            )

            .build()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {

        return null
    }
}