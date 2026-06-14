package com.example.rcslocationtracking.monitor

import android.location.Location
import android.telephony.SmsManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.content.Context
import com.example.rcslocationtracking.contacts.TrustedContactsManager
import com.google.android.gms.location.LocationServices
import com.example.rcslocationtracking.monitor.LocationHistoryManager

class HomeMonitorWorker(
    context: Context,
    params: WorkerParameters
) : Worker(
    context,
    params
) {

    override fun doWork(): Result {

        val home =
            HomeLocationManager
                .getHome(
                    applicationContext
                )

        val fused =
            LocationServices
                .getFusedLocationProviderClient(
                    applicationContext
                )

        fused.lastLocation
            .addOnSuccessListener {

                val loc =
                    it ?: return@addOnSuccessListener

                val result =
                    FloatArray(1)

                Location.distanceBetween(

                    home.first,
                    home.second,

                    loc.latitude,
                    loc.longitude,

                    result
                )

                val distance =
                    result[0]

                handleAwayTracking(
                    distance
                )

                LocationHistoryManager
                    .saveLocation(

                        applicationContext,

                        loc.latitude,

                        loc.longitude
                    )

                val score =
                    SafetyBubbleManager
                        .calculateRisk(

                            applicationContext,

                            distance,

                            loc.latitude,

                            loc.longitude
                        )

                val level =
                    SafetyBubbleManager
                        .getSafetyLevel(
                            score
                        )

                HomeLocationManager
                    .saveSafetyScore(
                        applicationContext,
                        score
                    )

                HomeLocationManager
                    .saveSafetyLevel(
                        applicationContext,
                        level
                    )

                if (
                    level ==
                    SafetyBubbleManager.HIGH
                    ||
                    level ==
                    SafetyBubbleManager.CRITICAL
                ) {

                    alertContacts(
                        loc.latitude,
                        loc.longitude,
                        level,
                        score
                    )
                }
            }

        return Result.success()
    }

    private fun handleAwayTracking(
        distance: Float
    ) {

        if (
            distance > 200
        ) {

            if (
                HomeLocationManager
                    .getAwayStartTime(
                        applicationContext
                    ) == 0L
            ) {

                HomeLocationManager
                    .saveAwayStartTime(
                        applicationContext,
                        System.currentTimeMillis()
                    )
            }
        } else {

            HomeLocationManager
                .saveAwayStartTime(
                    applicationContext,
                    0L
                )
        }
    }

    private fun alertContacts(
        lat: Double,
        lon: Double,
        level: String,
        score: Int
    ) {

        val contacts =
            TrustedContactsManager
                .getAll(
                    applicationContext
                )

        contacts.forEach {

            SmsManager
                .getDefault()

                .sendTextMessage(

                    it,

                    null,

                    """
RCS Predictive Safety Alert

Safety Level:
$level

Risk Score:
$score/100

Location:

https://maps.google.com/?q=$lat,$lon
                    """.trimIndent(),

                    null,

                    null
                )
        }
    }
}