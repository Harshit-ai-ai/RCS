package com.example.rcslocationtracking.monitor

import android.content.Context
import android.location.Location

import com.example.rcslocationtracking.hospital.HospitalManager
import com.example.rcslocationtracking.police.PoliceManager
import com.example.rcslocationtracking.relative.RelativeManager

object IsolationRiskManager {

    fun calculateIsolationRisk(
        context: Context,
        lat: Double,
        lon: Double
    ): Int {

        var risk = 0

        risk += policeRisk(
            context,
            lat,
            lon
        )

        risk += hospitalRisk(
            context,
            lat,
            lon
        )

        risk += relativeRisk(
            context,
            lat,
            lon
        )

        return risk.coerceAtMost(30)
    }

    private fun policeRisk(
        context: Context,
        lat: Double,
        lon: Double
    ): Int {

        val nearest =
            PoliceManager.getNearest(
                context,
                lat,
                lon
            ) ?: return 15

        val distanceKm =
            nearest.distance / 1000f

        return when {

            distanceKm < 1 ->
                0

            distanceKm < 3 ->
                3

            distanceKm < 5 ->
                8

            distanceKm < 10 ->
                12

            else ->
                15
        }
    }

    private fun hospitalRisk(
        context: Context,
        lat: Double,
        lon: Double
    ): Int {

        val nearest =
            HospitalManager.getNearest(
                context,
                lat,
                lon
            ) ?: return 10

        val distanceKm =
            nearest.distance / 1000f

        return when {

            distanceKm < 1 ->
                0

            distanceKm < 3 ->
                2

            distanceKm < 5 ->
                5

            distanceKm < 10 ->
                8

            else ->
                10
        }
    }

    private fun relativeRisk(
        context: Context,
        lat: Double,
        lon: Double
    ): Int {

        val relatives =
            RelativeManager
                .getAllLocations(
                    context
                )

        if (
            relatives.isEmpty()
        ) {
            return 5
        }

        var nearest =
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
                result[0] < nearest
            ) {
                nearest =
                    result[0]
            }
        }

        val distanceKm =
            nearest / 1000f

        return when {

            distanceKm < 1 ->
                0

            distanceKm < 3 ->
                1

            distanceKm < 5 ->
                3

            distanceKm < 10 ->
                5

            else ->
                8
        }
    }
}