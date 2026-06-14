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
    ): Pair<Double, Double> {

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

    fun saveAwayTimeHours(
        context: Context,
        hours: Int
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .putInt(
                "away_hours",
                hours
            )
            .apply()
    }

    fun getAwayTimeHours(
        context: Context
    ): Int {

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .getInt(
                "away_hours",
                3
            )
    }

    fun saveAwayStartTime(
        context: Context,
        time: Long
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .putLong(
                "away_start_time",
                time
            )
            .apply()
    }

    fun getAwayStartTime(
        context: Context
    ): Long {

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .getLong(
                "away_start_time",
                0L
            )
    }

    fun saveSafetyScore(
        context: Context,
        score: Int
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .putInt(
                "safety_score",
                score
            )
            .apply()
    }

    fun getSafetyScore(
        context: Context
    ): Int {

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .getInt(
                "safety_score",
                0
            )
    }

    fun saveSafetyLevel(
        context: Context,
        level: String
    ) {

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                "safety_level",
                level
            )
            .apply()
    }

    fun getSafetyLevel(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .getString(
                "safety_level",
                "SAFE"
            ) ?: "SAFE"
    }
}