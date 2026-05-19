package com.example.rcslocationtracking.monitor

import android.content.Context

object HomeLocationManager {

    private const val PREF = "home_monitor"

    fun saveHome(
        context: Context,
        lat: Double,
        lon: Double
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        prefs.edit()
            .putFloat(
                "lat",
                lat.toFloat()
            )
            .putFloat(
                "lon",
                lon.toFloat()
            )
            .apply()
    }

    fun getHome(
        context: Context
    ): Pair<Double,Double> {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        return Pair(

            prefs.getFloat(
                "lat",
                0f
            ).toDouble(),

            prefs.getFloat(
                "lon",
                0f
            ).toDouble()
        )
    }
}