package com.example.rcslocationtracking.mesh

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class SOSScannerService : Service() {

    override fun onCreate() {
        super.onCreate()

        val scanner =
            BluetoothAdapter
                .getDefaultAdapter()
                ?.bluetoothLeScanner

        scanner?.startScan(
            callback
        )
    }

    private val callback =
        object : ScanCallback() {

            override fun onScanResult(
                callbackType: Int,
                result: ScanResult
            ) {

                val record =
                    result.scanRecord
                        ?: return

                val bytes =
                    record.serviceData
                        .values
                        .firstOrNull()
                        ?: return

                val msg =
                    String(bytes)

                if (
                    msg.startsWith(
                        "SOS:"
                    )
                ) {

                    Toast.makeText(
                        applicationContext,
                        "Nearby SOS detected",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onBind(
        intent: Intent?
    ): IBinder? = null
}