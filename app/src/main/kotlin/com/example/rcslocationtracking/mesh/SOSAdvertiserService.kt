package com.example.rcslocationtracking.mesh

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Intent
import android.os.IBinder

class SOSAdvertiserService : Service() {

    private var advertiser: BluetoothLeAdvertiser? = null

    override fun onCreate() {
        super.onCreate()

        advertiser =
            BluetoothAdapter
                .getDefaultAdapter()
                ?.bluetoothLeAdvertiser
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        val lat =
            intent?.getDoubleExtra(
                "lat",
                0.0
            ) ?: 0.0

        val lon =
            intent?.getDoubleExtra(
                "lon",
                0.0
            ) ?: 0.0

        val msg =
            "SOS:$lat,$lon"

        val data =
            AdvertiseData.Builder()

                .addServiceData(
                    android.os.ParcelUuid(
                        java.util.UUID
                            .fromString(
                                "00001111-0000-1000-8000-00805F9B34FB"
                            )
                    ),

                    msg.toByteArray()
                )

                .build()

        val settings =
            AdvertiseSettings.Builder()

                .setAdvertiseMode(
                    AdvertiseSettings
                        .ADVERTISE_MODE_LOW_LATENCY
                )

                .setConnectable(false)

                .build()

        advertiser?.startAdvertising(
            settings,
            data,
            callback
        )

        return START_NOT_STICKY
    }

    private val callback =
        object : AdvertiseCallback() {}

    override fun onBind(
        intent: Intent?
    ): IBinder? = null
}