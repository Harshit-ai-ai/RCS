package com.example.rcslocationtracking.police

import android.content.Context
import android.location.Location

data class PolicePlace(

    val name: String,

    val lat: Double,

    val lon: Double,

    var distance: Float = 0f
)

object PoliceManager {

    private val stations = listOf(

        PolicePlace("Police Station Sadar", 28.429162, 77.037512),
        PolicePlace("Sadar Police Station", 28.46406, 77.036067),
        PolicePlace("Sushant Lok Police Station", 28.462993, 77.072817),
        PolicePlace("Sector 18 Police Station", 28.486364, 77.066095),
        PolicePlace("Police Station Bhondsi", 28.344578, 77.068134),
        PolicePlace("Sector 40 Police Station", 28.454482, 77.050454),
        PolicePlace("Women Police Station East", 28.463756, 77.030445),
        PolicePlace("Police Station DLF Phase 1", 28.476691, 77.094659),
        PolicePlace("Police Station DLF Phase 2", 28.494916, 77.089157),
        PolicePlace("Police Station DLF Phase 3", 28.499083, 77.09162),
        PolicePlace("Police Station Palam Vihar", 28.510876, 77.042931),
        PolicePlace("Police Station Udyog Vihar", 28.503102, 77.083514),
        PolicePlace("Police Station Sector 29", 28.46731, 77.068257),
        PolicePlace("Police Station Civil Lines", 28.470521, 77.028561),
        PolicePlace("Police Station New Colony", 28.466322, 77.012398),
        PolicePlace("Police Station Sector 10A", 28.455413, 76.996481),
        PolicePlace("Police Station Sector 17", 28.480027, 77.044157),
        PolicePlace("Police Station IMT Manesar", 28.351824, 76.941102),
        PolicePlace("Police Station Bilaspur", 28.318804, 76.993735),
        PolicePlace("Police Station Farrukhnagar", 28.446992, 76.824151),
        PolicePlace("Police Station Sohna", 28.24795, 77.065614),
        PolicePlace("Police Station Pataudi", 28.325214, 76.778414),
        PolicePlace("Police Station Badshahpur", 28.394557, 77.046928),
        PolicePlace("Police Station Kherki Daula", 28.420771, 76.985611),
        PolicePlace("Police Station Sector 65", 28.399228, 77.063492),
        PolicePlace("Women Police Station West", 28.455778, 77.015319),
        PolicePlace("Cyber Crime Police Station Gurgaon", 28.461947, 77.029638),
        PolicePlace("Shivaji Nagar Police Station", 28.453864399999997, 77.0206266)
    )

    fun getNearest(

        context: Context,

        lat: Double,

        lon: Double

    ): PolicePlace? {

        var nearest:
                PolicePlace? = null

        var best =
            Float.MAX_VALUE

        stations.forEach {

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
                result[0] < best
            ) {

                best =
                    result[0]

                it.distance =
                    result[0]

                nearest = it
            }
        }

        return nearest
    }
}