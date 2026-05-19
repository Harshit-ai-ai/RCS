package com.example.rcslocationtracking.relative

import android.content.Context
import android.location.Location

object EmergencyResolver {

    fun nearestRelative(

        context: Context,

        lat: Double,

        lon: Double

    ): String {

        val relatives =

            RelativeManager
                .getAllLocations(
                    context
                )

        if (
            relatives.isEmpty()
        ) {

            return "No relative"
        }

        var nearest =
            relatives[0]

        var best =
            Float.MAX_VALUE

        relatives.forEach {

            val result =
                FloatArray(1)

            Location.distanceBetween(

                lat,
                lon,

                it.lat,
                it.lon,

                result
            )

            if (
                result[0] <
                best
            ) {

                best =
                    result[0]

                nearest =
                    it
            }
        }

        return """
${nearest.name}

Distance:

${best.toInt()} m
        """.trimIndent()
    }
}