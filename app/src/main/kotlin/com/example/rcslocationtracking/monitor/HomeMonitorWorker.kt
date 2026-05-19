package com.example.rcslocationtracking.monitor

import android.content.Context
import android.location.Location
import android.telephony.SmsManager

import androidx.work.Worker
import androidx.work.WorkerParameters

import com.example.rcslocationtracking.contacts.TrustedContactsManager
import com.google.android.gms.location.LocationServices

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

                if (
                    distance > 200
                ) {

                    alertContacts(
                        loc.latitude,
                        loc.longitude
                    )
                }
            }

        return Result.success()
    }

    private fun alertContacts(
        lat: Double,
        lon: Double
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
User away from home for long duration

Location:

https://maps.google.com/?q=$lat,$lon
                    """.trimIndent(),

                    null,

                    null
                )
        }
    }
}