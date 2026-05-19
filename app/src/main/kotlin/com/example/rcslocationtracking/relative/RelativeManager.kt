package com.example.rcslocationtracking.relative

import android.content.Context

object RelativeManager {

    private const val PREF =
        "relatives"

    fun saveRelativeLocation(

        context: Context,

        name: String,

        lat: Double,

        lon: Double

    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        prefs.edit()

            .putString(
                name,
                "$lat,$lon"
            )

            .apply()
    }

    fun saveRelative(

        context: Context,

        name: String,

        address: String

    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        prefs.edit()

            .putString(
                name,
                address
            )

            .apply()
    }

    fun getAll(
        context: Context
    ): Map<String, *> {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        return prefs.all
    }

    fun remove(

        context: Context,

        name: String

    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        prefs.edit()

            .remove(
                name
            )

            .apply()
    }

    fun getNearest(

        context: Context,

        lat: Double,

        lon: Double

    ): RelativeData? {

        val prefs = context.getSharedPreferences(

            PREF,

            Context.MODE_PRIVATE

        )

        val all = prefs.all

        if (
            all.isEmpty()
        ) {

            return null
        }

        val firstEntry =

            all.entries.first()

        return RelativeData(

            firstEntry.key,

            firstEntry.value.toString()

        )
    }

    fun getAllLocations(
        context: Context
    ): List<RelativeLocation> {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        val list =
            mutableListOf<
                    RelativeLocation
                    >()

        prefs.all.forEach {

            val key =
                it.key

            val value =
                it.value.toString()

            val parts =
                value.split(",")

            if (
                parts.size >= 2
            ) {

                try {

                    list.add(

                        RelativeLocation(

                            key,

                            parts[0].toDouble(),

                            parts[1].toDouble()

                        )
                    )

                } catch (_:Exception) {

                }
            }
        }

        return list
    }

    data class RelativeData(

        val name: String,

        val address: String

    )
}