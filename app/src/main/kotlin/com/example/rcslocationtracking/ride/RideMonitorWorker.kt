package com.example.rcslocationtracking.ride

import android.location.Location
import android.telephony.SmsManager
import android.Manifest

import androidx.work.Worker
import androidx.work.WorkerParameters

import com.example.rcslocationtracking.contacts.TrustedContactsManager
import com.google.android.gms.location.LocationServices
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

class RideMonitorWorker(

    context: android.content.Context,

    params: WorkerParameters

) : Worker(
    context,
    params
) {

    override fun doWork(): Result {

        if (

            !RideManager.cabDetected(
                applicationContext
            )

        ) {

            return Result.success()
        }

        if (

            !RideManager.active(
                applicationContext
            )

        ) {

            return Result.success()
        }

        // WAIT 5 MINUTES AFTER START

        val startTime =

            RideManager
                .getStartTime(
                    applicationContext
                )

        val rideAge =

            System.currentTimeMillis()

        -

        startTime

        if (

            rideAge

            <

            5 * 60 * 1000

        ) {

            return Result.success()
        }

        val dest =

            RideManager
                .getDestination(
                    applicationContext
                )

        val fused =

            LocationServices
                .getFusedLocationProviderClient(
                    applicationContext
                )
        if (

            ContextCompat.checkSelfPermission(

                applicationContext,

                Manifest.permission.ACCESS_FINE_LOCATION

            )

            != PackageManager.PERMISSION_GRANTED

        ) {

            return Result.success()
        }

        fused.lastLocation
            .addOnSuccessListener {

                val loc =
                    it ?: return@addOnSuccessListener

                checkDeviation(

                    loc.latitude,

                    loc.longitude,

                    dest.first,

                    dest.second

                )
            }

        return Result.success()
    }

    private fun checkDeviation(

        lat: Double,

        lon: Double,

        destLat: Double,

        destLon: Double

    ) {

        val result =
            FloatArray(1)

        Location.distanceBetween(

            lat,

            lon,

            destLat,

            destLon,

            result

        )

        val distance =
            result[0]

        // ALERT ONLY IF >5km

        if (

            distance > 5000

        ) {

            alertContacts(
                lat,
                lon
            )
        }
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
Ride deviation detected

Current location:

https://maps.google.com/?q=$lat,$lon
                    """.trimIndent(),

                    null,

                    null

                )
        }
    }
}