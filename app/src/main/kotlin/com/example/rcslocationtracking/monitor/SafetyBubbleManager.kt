package com.example.rcslocationtracking.monitor

import android.content.Context
import android.os.BatteryManager
import java.util.Calendar

object SafetyBubbleManager {

    const val SAFE = "SAFE"
    const val ALERT = "ALERT"
    const val ELEVATED = "ELEVATED"
    const val HIGH = "HIGH"
    const val CRITICAL = "CRITICAL"

    fun calculateRisk(
        context: Context,
        distanceFromHomeMeters: Float,
        currentLat: Double,
        currentLon: Double
    ): Int {

        var score = 0

        score += calculateDistanceRisk(
            distanceFromHomeMeters
        )

        score += calculateTimeRisk()

        score += calculateBatteryRisk(
            context
        )

        score += calculateAwayDurationRisk(
            context
        )

        score +=
            IsolationRiskManager
                .calculateIsolationRisk(
                    context,
                    currentLat,
                    currentLon
                )

        score +=

            PatternAnalyzer
                .calculatePatternRisk(

                    context,

                    currentLat,

                    currentLon
                )

        return score.coerceIn(
            0,
            100
        )
    }

    fun getSafetyLevel(
        score: Int
    ): String {

        return when {

            score <= 20 ->
                SAFE

            score <= 40 ->
                ALERT

            score <= 60 ->
                ELEVATED

            score <= 80 ->
                HIGH

            else ->
                CRITICAL
        }
    }

    private fun calculateDistanceRisk(
        distanceMeters: Float
    ): Int {

        return when {

            distanceMeters > 50000 ->
                30

            distanceMeters > 10000 ->
                20

            distanceMeters > 2000 ->
                10

            else ->
                0
        }
    }

    private fun calculateTimeRisk(): Int {

        val hour = Calendar
            .getInstance()
            .get(Calendar.HOUR_OF_DAY)

        return when {

            hour in 0..4 ->
                35

            hour in 5..6 ->
                20

            hour in 21..23 ->
                15

            else ->
                0
        }
    }

    private fun calculateBatteryRisk(
        context: Context
    ): Int {

        val bm =
            context.getSystemService(
                Context.BATTERY_SERVICE
            ) as BatteryManager

        val battery =
            bm.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CAPACITY
            )

        return when {

            battery <= 5 ->
                25

            battery <= 10 ->
                20

            battery <= 20 ->
                10

            else ->
                0
        }
    }

    private fun calculateAwayDurationRisk(
        context: Context
    ): Int {

        val prefs =
            context.getSharedPreferences(
                "home_monitor",
                Context.MODE_PRIVATE
            )

        val awayStart =
            prefs.getLong(
                "away_start_time",
                0L
            )

        if (
            awayStart == 0L
        ) {
            return 0
        }

        val elapsedHours =

            (
                    System.currentTimeMillis()
                            - awayStart
                    ) /
                    (1000 * 60 * 60)

        val allowedHours =
            HomeLocationManager
                .getAwayTimeHours(
                    context
                )

        return when {

            elapsedHours >=
                    allowedHours * 2L ->
                30

            elapsedHours >=
                    allowedHours.toLong() ->
                20

            elapsedHours >=
                    allowedHours / 2L ->
                10

            else ->
                0
        }
    }
}