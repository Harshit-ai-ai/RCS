package com.example.rcslocationtracking.monitor

import android.content.Context

object LocationHistoryManager {

    private const val PREF =
        "location_history"

    fun saveLocation(
        context: Context,
        lat: Double,
        lon: Double
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        val existing =
            prefs.getString(
                "locations",
                ""
            ) ?: ""

        val updated =

            if (
                existing.isEmpty()
            ) {
                "$lat,$lon"
            } else {
                "$existing;$lat,$lon"
            }

        val trimmed =

            updated
                .split(";")
                .takeLast(100)
                .joinToString(";")

        prefs.edit()
            .putString(
                "locations",
                trimmed
            )
            .apply()
    }

    fun getLocations(
        context: Context
    ): List<Pair<Double, Double>> {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        val raw =
            prefs.getString(
                "locations",
                ""
            ) ?: ""

        if (
            raw.isBlank()
        ) {
            return emptyList()
        }

        return raw.split(";")
            .mapNotNull {

                try {

                    val parts =
                        it.split(",")

                    Pair(
                        parts[0].toDouble(),
                        parts[1].toDouble()
                    )

                } catch (
                    _: Exception
                ) {
                    null
                }
            }
    }
}