package com.example.rcslocationtracking.monitor

import android.content.Context
import android.location.Location
import java.util.Calendar

object PatternAnalyzer {

    fun calculatePatternRisk(

        context: Context,

        currentLat: Double,

        currentLon: Double

    ): Int {

        val history =

            LocationHistoryManager
                .getLocations(
                    context
                )

        if (
            history.size < 10
        ) {
            return 0
        }

        var nearest =
            Float.MAX_VALUE

        history.forEach {

            val result =
                FloatArray(1)

            Location.distanceBetween(

                currentLat,
                currentLon,

                it.first,
                it.second,

                result
            )

            if (
                result[0] < nearest
            ) {

                nearest =
                    result[0]
            }
        }

        val hour =

            Calendar
                .getInstance()
                .get(
                    Calendar.HOUR_OF_DAY
                )

        return when {

            nearest > 10000 &&
                    hour in 0..5 ->
                40

            nearest > 5000 &&
                    hour in 0..5 ->
                30

            nearest > 10000 ->
                20

            nearest > 5000 ->
                10

            else ->
                0
        }
    }
}