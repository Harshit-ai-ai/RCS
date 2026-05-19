package com.example.rcslocationtracking.hospital

object NearestHospital {

    fun getNearest(

        lat:Double,

        lon:Double

    ):String {

        return """
RML Hospital

Distance:
3 km
        """.trimIndent()
    }
}