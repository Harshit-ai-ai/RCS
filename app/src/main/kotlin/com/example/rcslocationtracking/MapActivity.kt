package com.example.rcslocationtracking

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity

import com.example.rcslocationtracking.monitor.HomeLocationManager
import com.example.rcslocationtracking.relative.RelativeManager
import com.google.android.gms.location.LocationServices

import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    private var pickRelative = false

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(

            applicationContext,

            getSharedPreferences(
                "osm",
                MODE_PRIVATE
            )
        )

        setContentView(
            R.layout.activity_map
        )

        pickRelative =

            intent.getBooleanExtra(
                "pick_relative",
                false
            )

        mapView =
            findViewById(
                R.id.map
            )

        mapView.setMultiTouchControls(
            true
        )

        mapView.controller.setZoom(
            12.0
        )

        // -------- USER MARKER --------

        val fused =

            LocationServices
                .getFusedLocationProviderClient(
                    this
                )

        fused.lastLocation
            .addOnSuccessListener {

                val location =
                    it ?: return@addOnSuccessListener

                val point =

                    GeoPoint(

                        location.latitude,

                        location.longitude

                    )

                val userMarker =
                    Marker(mapView)

                userMarker.position =
                    point

                userMarker.title =
                    "You"

                mapView.overlays.add(
                    userMarker
                )

                mapView.controller
                    .setCenter(
                        point
                    )

                mapView.invalidate()
            }

        // -------- HOME MARKER --------

        val home =

            HomeLocationManager
                .getHome(this)

        home?.let {

            val marker =
                Marker(mapView)

            marker.position =

                GeoPoint(
                    it.first,
                    it.second
                )

            marker.title =
                "Home"

            mapView.overlays.add(
                marker
            )
        }

        // -------- MAP TAP RELATIVE --------

        mapView.overlays.add(

            object : Overlay() {

                override fun onSingleTapConfirmed(

                    e: android.view.MotionEvent,

                    map: MapView

                ): Boolean {

                    if (
                        !pickRelative
                    ) {
                        return false
                    }

                    val point =

                        map.projection
                            .fromPixels(

                                e.x.toInt(),

                                e.y.toInt()

                            ) as GeoPoint

                    val input =
                        EditText(
                            this@MapActivity
                        )

                    AlertDialog
                        .Builder(
                            this@MapActivity
                        )

                        .setTitle(
                            "Relative Name"
                        )

                        .setView(
                            input
                        )

                        .setPositiveButton(
                            "Save"
                        ) { _, _ ->

                            RelativeManager
                                .saveRelativeLocation(

                                    this@MapActivity,

                                    input.text
                                        .toString(),

                                    point.latitude,

                                    point.longitude

                                )
                        }

                        .show()

                    return true
                }
            }
        )
    }

    override fun onResume() {

        super.onResume()

        mapView.onResume()
    }

    override fun onPause() {

        super.onPause()

        mapView.onPause()
    }

    override fun onDestroy() {

        super.onDestroy()

        mapView.onDetach()
    }
}