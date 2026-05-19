package com.example.rcslocationtracking.hospital

import android.content.Context
import android.location.Location

data class HospitalPlace(

    val name: String,

    val lat: Double,

    val lon: Double,

    var distance: Float = 0f
)

object HospitalManager {

    private val hospitals = listOf(

        HospitalPlace(
            "Ahmed Hospital Multi Speciality",
            28.4560817,
            77.0400155
        ),

        HospitalPlace(
            "Anand Hospital",
            28.600817,
            77.34522
        ),

        HospitalPlace(
            "Anand Medical Centre",
            28.426307,
            77.0947149
        ),

        HospitalPlace(
            "Anirud Hospital",
            28.3255049,
            76.778212
        ),

        HospitalPlace(
            "Aryan Hospital Private Limited",
            28.4669699,
            77.0209947
        ),

        HospitalPlace(
            "Bhatnagar MaternityandNursing Home",
            46.5394992,
            -102.8705785
        ),

        HospitalPlace(
            "Bindal Hospital And Maternity Centre",
            28.4669699,
            77.0209947
        ),

        HospitalPlace(
            "Chirag Hospital Pvt. Ltd",
            14.4224398,
            77.7145229
        ),

        HospitalPlace(
            "Chiranjiv Hospital and Maternity Home / Chiranjivi Child Hospital / Chiranjeev Hospital",
            28.3467326,
            76.7596221
        ),

        HospitalPlace(
            "Dayal Eye Centre",
            28.4517387,
            77.0421171
        ),

        HospitalPlace(
            "Dharam Raj Hospital",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Dr. Ajay S. Gupta",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Dr. Ashok Jain",
            28.3752154,
            76.9120529
        ),

        HospitalPlace(
            "Dr. R.N Yadav Eye Care Centre",
            28.6007027,
            77.1043255
        ),

        HospitalPlace(
            "Florence Nursing Home",
            33.6716554,
            73.074954
        ),

        HospitalPlace(
            "Geeta Nursing Home / Geeta Surgical and Ultrasound Centre",
            13.0536607,
            80.1983149
        ),

        HospitalPlace(
            "Goel Nursing Home",
            32.1778915,
            74.4842273
        ),

        HospitalPlace(
            "Gupta Hospital",
            28.4625522,
            77.0283798
        ),

        HospitalPlace(
            "Nursing Home",
            28.4573024,
            77.0361078
        ),

        HospitalPlace(
            "Jain Hospital",
            27.6542858,
            76.8846014
        ),

        HospitalPlace(
            "Jain Sant Phool Chand Ji Charitable Hospital",
            28.6496575,
            77.3379937
        ),

        HospitalPlace(
            "Kanshi Ram Medical Services",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Khanna Diagnostic Centre and Path Lab",
            29.3905173,
            76.9604353
        ),

        HospitalPlace(
            "Kharbanda Maternity and Nursing Home",
            28.4683613,
            77.0255377
        ),

        HospitalPlace(
            "Kumar Medical Park",
            28.3808658,
            77.3142692
        ),

        HospitalPlace(
            "Lotus Hospital Gurgoan",
            28.4693018,
            77.0341722
        ),

        HospitalPlace(
            "Mangalam Hospital and Heart Centre",
            28.455072,
            77.014676
        ),

        HospitalPlace(
            "Manju Hospital",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Marwah Clinic",
            28.4650453,
            77.1007117
        ),

        HospitalPlace(
            "Mathur Medical And Child Care Centre",
            28.455354,
            77.032304
        ),

        HospitalPlace(
            "Max Hospital",
            28.4753855,
            77.0756742
        ),

        HospitalPlace(
            "Medanta The Medicity Global Health Pvt Ltd",
            28.4357613,
            77.0406147
        ),

        HospitalPlace(
            "Mission Charitable Hospital",
            28.3754567,
            76.9066829
        ),

        HospitalPlace(
            "Nagpal Nursing Home",
            28.4707417,
            77.0463719
        ),

        HospitalPlace(
            "Nangia Hospital Ent and Maternity",
            28.4539065,
            77.020619
        ),

        HospitalPlace(
            "Narula Hospital",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Ob-Gyn Center",
            28.458306,
            77.035796
        ),

        HospitalPlace(
            "Orbit Hospital",
            28.4532055,
            77.0480158
        ),

        HospitalPlace(
            "Parashar Hospital",
            28.4563129,
            77.022695
        ),

        HospitalPlace(
            "Park Hospital",
            28.5120259,
            77.03193
        ),

        HospitalPlace(
            "Prime Multispeciality Hospital",
            28.4514004,
            77.0168634
        ),

        HospitalPlace(
            "Rajesh Hospital",
            28.354726,
            76.9375455
        ),

        HospitalPlace(
            "Rajiv Memorial Eye Infirmary",
            28.4625522,
            77.0283798
        ),

        HospitalPlace(
            "Rama Hospital Nursing Home / Rama Krishna Hospital",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Sai Dharamraj Hospital",
            28.3950645,
            77.046534
        ),

        HospitalPlace(
            "Sangwan Hospital",
            28.4472778,
            76.9946535
        ),

        HospitalPlace(
            "Satyam Hospital",
            28.4853382,
            77.0186621
        ),

        HospitalPlace(
            "Sethi Hospital",
            28.4567795,
            77.0219311
        ),

        HospitalPlace(
            "Shalu Paediatric and Trauma Hospital",
            28.473376,
            77.0335963
        ),

        HospitalPlace(
            "Shanta Hospital",
            28.4669699,
            77.0209947
        ),

        HospitalPlace(
            "Sheetla Hospital And Eye Institute Private Ltd",
            28.4683613,
            77.0255377
        ),

        HospitalPlace(
            "Shiromani Sai Heart and Trauma Centre",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Shiva Hospital",
            28.4757428,
            77.0380271
        ),

        HospitalPlace(
            "Shivam Hospital",
            28.4587807,
            77.0524887
        ),

        HospitalPlace(
            "Shree Tirupati Balaji Hospital",
            28.5093045,
            77.0306288
        ),

        HospitalPlace(
            "Sidhesh Hospital",
            28.4775758,
            77.0820378
        ),

        HospitalPlace(
            "Sita Hospital",
            29.1383854,
            77.0272501
        ),

        HospitalPlace(
            "Sneh Hospital",
            28.7055148,
            77.176282
        ),

        HospitalPlace(
            "Surgi Center Clinic",
            28.4930297,
            77.0866765
        ),

        HospitalPlace(
            "Taneja Hospital",
            28.466283,
            77.013852
        ),

        HospitalPlace(
            "The Cradle Hospital",
            28.4707417,
            77.0463719
        ),

        HospitalPlace(
            "Tirath Ram Hospitals Pvt Ltd",
            28.4570115,
            77.0202295
        ),

        HospitalPlace(
            "Vatsalya Medical Centre",
            28.4709658,
            77.0176977
        ),

        HospitalPlace(
            "Verma E.N.T. Hospital",
            28.4625522,
            77.0283798
        ),

        HospitalPlace(
            "Yashroop Medical Centre",
            28.3688093,
            76.9268541
        ),

        HospitalPlace(
            "Alchemist Hospitals Ltd",
            28.4482933,
            77.0955136
        ),

        HospitalPlace(
            "Alps Hospital Limited",
            28.5138999,
            77.0812691
        ),

        HospitalPlace(
            "Paras Hospital",
            28.6353011,
            77.2248008
        ),

        HospitalPlace(
            "Umkal Hospital And M. P. Heart Research Institute",
            28.4715182,
            77.0733208
        ),

        HospitalPlace(
            "World Laparoscopy Hospital",
            28.4936018,
            77.088325
        ),

        HospitalPlace(
            "Rockland Hospital Limited",
            28.3683154,
            76.9324697
        ),

        HospitalPlace(
            "Artemis Health Institute",
            28.4594965,
            77.0266383
        ),

        HospitalPlace(
            "Metro Hospital and Heart Institute, Gurgaon",
            28.5003715,
            77.0414187
        ),

        HospitalPlace(
            "Neelkanth Hospital",
            28.6139391,
            77.2090212
        ),

        HospitalPlace(
            "Kathuria Hospital",
            28.4348057,
            77.0032153
        ),

        HospitalPlace(
            "Arihant Hospital",
            28.4219753,
            77.0791496
        ),

        HospitalPlace(
            "Bhardwaj Hospital",
            28.492663,
            77.0060658
        ),

        HospitalPlace(
            "Malik Hospital",
            28.4663599,
            77.1003219
        ),

        HospitalPlace(
            "Sunrise Hospital",
            28.4437428,
            77.0111419
        )
    )

    fun getNearest(

        context: Context,

        lat: Double,

        lon: Double

    ): HospitalPlace? {

        var nearest:
                HospitalPlace? = null

        var best =
            Float.MAX_VALUE

        hospitals.forEach {

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