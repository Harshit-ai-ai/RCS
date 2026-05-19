package com.example.rcslocationtracking.ride

import android.content.Context

object RideManager {

    private const val PREF =
        "ride"

    private const val ACTIVE =
        "ride_active"

    private const val DEST_LAT =
        "dest_lat"

    private const val DEST_LON =
        "dest_lon"

    private const val START_TIME =
        "start_time"

    private const val CAB_ACTIVE =
        "cab_active"


    fun setCabDetected(

        context: Context,

        value: Boolean

    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .edit()

            .putBoolean(
                "cab_detected",
                value
            )

            .apply()
    }

    fun cabDetected(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .getBoolean(
                "cab_detected",
                false
            )
    }

    fun isCabDetected(
        context: Context
    ): Boolean {

        return context

            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .getBoolean(
                CAB_ACTIVE,
                false
            )
    }

    fun startRide(

        context: Context,

        lat: Double,

        lon: Double

    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .edit()

            .putBoolean(
                ACTIVE,
                true
            )

            .putFloat(
                DEST_LAT,
                lat.toFloat()
            )

            .putFloat(
                DEST_LON,
                lon.toFloat()
            )

            .putLong(
                START_TIME,

                System.currentTimeMillis()
            )

            .apply()
    }

    fun stopRide(
        context: Context
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .edit()

            .putBoolean(
                ACTIVE,
                false
            )

            .apply()
    }

    fun active(
        context: Context
    ): Boolean {

        return context

            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .getBoolean(
                ACTIVE,
                false
            )
    }

    fun getDestination(

        context: Context

    ): Pair<Double, Double> {

        val prefs =

            context
                .getSharedPreferences(
                    PREF,
                    Context.MODE_PRIVATE
                )

        val lat =

            prefs.getFloat(
                DEST_LAT,
                0f
            )

        val lon =

            prefs.getFloat(
                DEST_LON,
                0f
            )

        return Pair(
            lat.toDouble(),
            lon.toDouble()
        )
    }

    fun getStartTime(

        context: Context

    ): Long {

        return context

            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

            .getLong(

                "ride_start",

                0

            )
    }
}