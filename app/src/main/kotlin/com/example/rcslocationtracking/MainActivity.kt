package com.example.rcslocationtracking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.rcslocationtracking.ride.RideManager
import com.example.rcslocationtracking.ride.RideMonitorWorker
import com.example.rcslocationtracking.relative.EmergencyResolver
import com.example.rcslocationtracking.police.NearestPolice
import com.example.rcslocationtracking.hospital.NearestHospital
import com.google.android.gms.location.LocationServices
import android.telephony.SmsManager
import android.widget.ArrayAdapter

import java.util.concurrent.TimeUnit

import com.example.rcslocationtracking.monitor.HomeMonitorWorker
import com.example.rcslocationtracking.monitor.HomeLocationManager
import android.widget.LinearLayout
import android.widget.EditText

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.rcslocationtracking.contacts.TrustedContactsManager
import com.example.rcslocationtracking.service.LocationService
import com.example.rcslocationtracking.relative.RelativeManager

class MainActivity : AppCompatActivity() {

    private lateinit var guideButton: Button
    private lateinit var statusText: TextView
    private lateinit var startButton: Button
    private lateinit var addContactButton: Button
    private lateinit var syncFavoritesButton: Button
    private lateinit var trustedContactsList: android.widget.ListView
    private lateinit var sosButton: Button
    private lateinit var mapButton: Button
    private lateinit var rideAccessButton: Button
    private lateinit var setHomeButton: Button
    private lateinit var startRideButton: Button
    private lateinit var addRelativeButton: Button
    private lateinit var addRelativeMapButton: Button

    private val REQ_SMS = 101
    private val REQ_LOCATION = 102
    private val REQ_CONTACT = 103
    private val PICK_CONTACT = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        intent?.let {

            if (
                it.action ==
                Intent.ACTION_SEND
            ) {

                val sharedText =
                    it.getStringExtra(
                        Intent.EXTRA_TEXT
                    )

                if (
                    sharedText != null
                ) {

                    RideManager.setCabDetected(
                        this,
                        true
                    )

                    handleRideShare(
                        sharedText
                    )

                    Toast.makeText(

                        this,

                        "Cab detected",

                        Toast.LENGTH_LONG

                    ).show()
                }
            }
        }

        statusText = findViewById(R.id.statusText)


        startButton =
            findViewById(R.id.startButton)

        addContactButton =
            findViewById(R.id.addContactButton)

        sosButton =
            findViewById(R.id.sosButton)

        mapButton =
            findViewById(R.id.mapButton)

        updateStatus()

        addRelativeButton =
            findViewById(
                R.id.addRelativeButton
            )

        addRelativeButton.setOnClickListener {

            showRelativeDialog()
        }

        startButton.setOnClickListener {
            startProtectionFlow()
        }

        addContactButton.setOnClickListener {
            addTrustedContact()
        }

        rideAccessButton =
            findViewById(
                R.id.rideAccessButton
            )

        rideAccessButton.setOnClickListener {

            startActivity(
                Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
                )
            )
        }

        addRelativeMapButton =
            findViewById(
                R.id.addRelativeMapButton
            )

        addRelativeMapButton.setOnClickListener {

            val intent =
                Intent(
                    this,
                    MapActivity::class.java
                )

            intent.putExtra(
                "pick_relative",
                true
            )

            startActivity(intent)
        }

        mapButton.setOnClickListener {

            Toast.makeText(
                this,
                "Opening map...",
                Toast.LENGTH_SHORT
            ).show()

            try {

                val intent =
                    Intent(
                        this,
                        MapActivity::class.java
                    )

                startActivity(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    this,
                    "Map failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                e.printStackTrace()
            }
        }


        setHomeButton =
            findViewById(
                R.id.setHomeButton
            )

        setHomeButton.setOnClickListener {

            saveCurrentHome()
        }

        guideButton=
            findViewById(
                R.id.guideButton
            )
        guideButton.setOnClickListener{
            showGuide()
        }

        startRideButton =findViewById(R.id.startRideButton)
        startRideButton =
            findViewById(
                R.id.startRideButton
            )

        startRideButton.setOnClickListener {

            if (

                !RideManager.cabDetected(
                    this
                )

            ) {

                Toast.makeText(

                    this,

                    "No cab booking detected",

                    Toast.LENGTH_LONG

                ).show()

                return@setOnClickListener
            }

            RideManager.startRide(

                this,

                28.6139,

                77.2090

            )

            Toast.makeText(

                this,

                "Ride monitoring started",

                Toast.LENGTH_LONG

            ).show()
        }

        val rideWork =

            PeriodicWorkRequestBuilder<RideMonitorWorker>(

                15,
                TimeUnit.MINUTES

            ).build()

        WorkManager

            .getInstance(this)

            .enqueueUniquePeriodicWork(

                "ride_monitor",

                ExistingPeriodicWorkPolicy.KEEP,

                rideWork

            )

        val workRequest =

            PeriodicWorkRequestBuilder<HomeMonitorWorker>(

                1,
                TimeUnit.HOURS

            ).build()

        WorkManager
            .getInstance(this)

            .enqueueUniquePeriodicWork(

                "home_check",

                ExistingPeriodicWorkPolicy.KEEP,

                workRequest

            )


        ActivityCompat.requestPermissions(
            this,

            arrayOf(

                Manifest.permission.BLUETOOTH_SCAN,

                Manifest.permission.BLUETOOTH_ADVERTISE,

                Manifest.permission.BLUETOOTH_CONNECT

            ),

            500
        )

        startService(
            Intent(
                this,
                com.example
                    .rcslocationtracking
                    .mesh
                    .SOSScannerService::class.java
            )
        )


        trustedContactsList =
            findViewById(
                R.id.trustedContactsList
            )

        updateTrustedContactsView()

        syncFavoritesButton =
            findViewById(
                R.id.syncFavoritesButton
            )

        syncFavoritesButton.setOnClickListener {

            syncFavoriteContacts()
        }

        sosButton.setOnClickListener {

            emergencySOS()

            val intent =
                Intent(
                    this,
                    com.example
                        .rcslocationtracking
                        .mesh
                        .SOSAdvertiserService::class.java
                )

            intent.putExtra(
                "lat",
                28.6139
            )

            intent.putExtra(
                "lon",
                77.2090
            )

            startService(
                intent
            )
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    showExitWarning()
                }
            }
        )
    }

    private fun updateTrustedContactsView() {

        val contacts =
            TrustedContactsManager
                .getAll(this)
                .toMutableList()

        val displayList =
            mutableListOf<String>()

        val numberMap =
            mutableMapOf<String, String>()

        contacts.forEach { number ->

            var name = "Unknown"

            val cursor =
                contentResolver.query(

                    ContactsContract
                        .CommonDataKinds
                        .Phone
                        .CONTENT_URI,

                    arrayOf(
                        ContactsContract
                            .CommonDataKinds
                            .Phone
                            .DISPLAY_NAME
                    ),

                    "${ContactsContract.CommonDataKinds.Phone.NUMBER} LIKE ?",

                    arrayOf("%$number%"),

                    null
                )

            cursor?.use {

                if (it.moveToFirst()) {

                    name =
                        it.getString(0)
                }
            }

            val display =

                "$name\n$number"

            displayList.add(display)

            numberMap[display] =
                number
        }

        val adapter = object :

            android.widget.ArrayAdapter<String>(

                this,

                android.R.layout.simple_list_item_1,

                displayList

            ) {

            override fun getView(

                position: Int,

                convertView: android.view.View?,

                parent: android.view.ViewGroup

            ): android.view.View {

                val view = super.getView(
                    position,
                    convertView,
                    parent
                )

                val text = view.findViewById<TextView>(
                    android.R.id.text1
                )

                text.setTextColor(
                    android.graphics.Color.BLACK
                )

                return view
            }
        }

        trustedContactsList.adapter =
            adapter

        trustedContactsList
            .setOnItemLongClickListener {

                    _,
                    _,
                    position,
                    _ ->

                val selected =
                    displayList[position]

                val number =
                    numberMap[selected]
                        ?: return@setOnItemLongClickListener true

                AlertDialog.Builder(
                    this
                )

                    .setTitle(
                        "Remove trusted contact?"
                    )

                    .setMessage(
                        selected
                    )

                    .setPositiveButton(
                        "Remove"
                    ) { _, _ ->

                        TrustedContactsManager
                            .removeNumber(
                                this,
                                number
                            )

                        updateTrustedContactsView()

                        Toast.makeText(

                            this,

                            "Removed",

                            Toast.LENGTH_SHORT

                        ).show()
                    }

                    .setNegativeButton(
                        "Cancel",
                        null
                    )

                    .show()

                true
            }
    }



    private fun showRelativeDialog() {

        val layout =
            LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        val nameBox =
            EditText(this)

        nameBox.hint =
            "Relative name"

        val addressBox =
            EditText(this)

        addressBox.hint =
            "Address"

        layout.addView(
            nameBox
        )

        layout.addView(
            addressBox
        )

        AlertDialog.Builder(this)

            .setTitle(
                "Add Relative"
            )

            .setView(
                layout
            )

            .setPositiveButton(
                "Save"
            ) { dialog, which ->

                RelativeManager.saveRelative(

                    this,

                    nameBox.text.toString(),

                    addressBox.text.toString()

                )
            }

            .show()
    }

    private fun saveCurrentHome() {

        val fused =
            com.google.android.gms.location
                .LocationServices
                .getFusedLocationProviderClient(
                    this
                )

        if (

            ContextCompat.checkSelfPermission(

                this,

                Manifest.permission.ACCESS_FINE_LOCATION

            )

            != PackageManager.PERMISSION_GRANTED

        ) {

            return

        }

        fused.lastLocation
            .addOnSuccessListener {

                it ?: return@addOnSuccessListener

                HomeLocationManager
                    .saveHome(

                        this,

                        it.latitude,

                        it.longitude

                    )

                Toast.makeText(

                    this,

                    "Home saved",

                    Toast.LENGTH_LONG

                ).show()
            }
    }

    private fun showGuide() {

        AlertDialog.Builder(this)

            .setTitle(
                "RCS Safety Setup"
            )

            .setMessage(

                """
1. Enable SMS permission

2. Enable Location permission

3. Allow Background Location

4. Disable Battery Optimization

5. Add trusted contacts

6. Keep GPS ON

7. Test LOC SMS

8. Test SOS button

Internet is NOT required
GPS + SIM required
            """.trimIndent()
            )

            .setPositiveButton(
                "OK",
                null
            )

            .show()
    }

    private fun handleRideShare(
        text: String
    ) {

        val contacts =
            TrustedContactsManager
                .getAll(this)

        if (
            contacts.isEmpty()
        ) {

            Toast.makeText(
                this,
                "No trusted contacts",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        contacts.forEach {

            sendRideSMS(
                it,
                text
            )
        }

        Toast.makeText(
            this,
            "Ride shared with trusted contacts",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun sendRideSMS(
        number: String,
        rideInfo: String
    ) {

        val sms =
            android.telephony
                .SmsManager
                .getDefault()

        val message =

            """
RCS Ride Alert

Ride details:

$rideInfo
        """.trimIndent()

        sms.sendTextMessage(
            number,
            null,
            message,
            null,
            null
        )
    }

    private fun syncFavoriteContacts() {

        if (

            ContextCompat.checkSelfPermission(

                this,

                Manifest.permission.READ_CONTACTS

            )

            != PackageManager.PERMISSION_GRANTED

        ) {

            ActivityCompat.requestPermissions(

                this,

                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ),

                REQ_CONTACT

            )

            return
        }

        val contactsCursor = contentResolver.query(

            ContactsContract.Contacts.CONTENT_URI,

            arrayOf(

                ContactsContract.Contacts._ID,

                ContactsContract.Contacts.DISPLAY_NAME,

                ContactsContract.Contacts.STARRED

            ),

            null,

            null,

            null

        )

        var count = 0

        contactsCursor?.use { cursor ->

            while (

                cursor.moveToNext()

            ) {

                val starred = cursor.getInt(2)

                if (

                    starred != 1

                ) continue

                val id =
                    cursor.getString(0)

                val phoneCursor = contentResolver.query(

                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,

                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),

                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID}=?",

                    arrayOf(id),

                    null

                )

                phoneCursor?.use {

                    while (

                        it.moveToNext()

                    ) {

                        val number =
                            it.getString(0)

                        TrustedContactsManager
                            .addNumber(
                                this,
                                number
                            )

                        count++
                    }
                }
            }
        }

        updateTrustedContactsView()

        Toast.makeText(

            this,

            "Imported $count favourites",

            Toast.LENGTH_LONG

        ).show()
    }

    private fun emergencySOS() {

        if (

            ContextCompat.checkSelfPermission(

                this,

                Manifest.permission.SEND_SMS

            )

            != PackageManager.PERMISSION_GRANTED

        ) {

            ActivityCompat.requestPermissions(

                this,

                arrayOf(

                    Manifest.permission.SEND_SMS,

                    Manifest.permission.RECEIVE_SMS,

                    Manifest.permission.ACCESS_FINE_LOCATION

                ),

                100
            )

            return
        }

        val contacts =

            TrustedContactsManager
                .getAll(this)

        if (
            contacts.isEmpty()
        ) {

            Toast.makeText(

                this,

                "No trusted contacts",

                Toast.LENGTH_LONG

            ).show()

            return
        }

        val fused =

            LocationServices
                .getFusedLocationProviderClient(
                    this
                )

        fused.lastLocation
            .addOnSuccessListener {

                val loc = it

                if (
                    loc == null
                ) {

                    Toast.makeText(

                        this,

                        "Location unavailable",

                        Toast.LENGTH_LONG

                    ).show()

                    return@addOnSuccessListener
                }

                val lat =
                    loc.latitude

                val lon =
                    loc.longitude

                val relative =

                    EmergencyResolver
                        .nearestRelative(

                            this,

                            lat,

                            lon
                        )

                val police =

                    NearestPolice
                        .getNearest(
                            lat,
                            lon
                        )

                val hospital =

                    NearestHospital
                        .getNearest(
                            lat,
                            lon
                        )

                val message =

                    """
SOS ALERT

Location:

https://maps.google.com/?q=$lat,$lon

Nearest Relative:

$relative

Nearest Police:

$police

Nearest Hospital:

$hospital
""".trimIndent()

                contacts.forEach {

                    Toast.makeText(

                        this,

                        "Sending",

                        Toast.LENGTH_SHORT

                    ).show()

                    val intent =

                        Intent(
                            this,
                            LocationService::class.java
                        )

                    intent.putExtra(
                        "sender",
                        it
                    )

                    intent.putExtra(
                        "extra_message",
                        message
                    )

                    startForegroundService(
                        intent
                    )
                }

                Toast.makeText(

                    this,

                    "Enhanced SOS sent",

                    Toast.LENGTH_LONG

                ).show()
            }
    }

    private fun addTrustedContact() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ),
                REQ_CONTACT
            )

        } else {

            pickContact()
        }
    }

    private fun pickContact() {

        val intent =
            Intent(
                Intent.ACTION_PICK
            )

        intent.type =
            ContactsContract
                .CommonDataKinds
                .Phone
                .CONTENT_TYPE

        startActivityForResult(
            intent,
            PICK_CONTACT
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == PICK_CONTACT &&
            resultCode == RESULT_OK
        ) {

            val uri =
                data?.data ?: return

            val cursor =
                contentResolver.query(
                    uri,
                    arrayOf(
                        ContactsContract
                            .CommonDataKinds
                            .Phone
                            .NUMBER
                    ),
                    null,
                    null,
                    null
                )

            cursor?.use {

                if (it.moveToFirst()) {

                    val number =
                        it.getString(0)

                    TrustedContactsManager
                        .addNumber(
                            this,
                            number
                        )

                    updateTrustedContactsView()

                    Toast.makeText(
                        this,
                        "Trusted contact added",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun startProtectionFlow() {

        Toast.makeText(
            this,
            "Protection ACTIVE",
            Toast.LENGTH_LONG
        ).show()

        requestIgnoreBatteryOptimizations()

        updateStatus()
    }

    private fun requestIgnoreBatteryOptimizations() {

        val pm =
            getSystemService(
                POWER_SERVICE
            ) as PowerManager

        if (
            !pm.isIgnoringBatteryOptimizations(
                packageName
            )
        ) {

            startActivity(
                Intent(
                    Settings
                        .ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Uri.parse(
                        "package:$packageName"
                    )
                )
            )
        }
    }

    private fun updateStatus() {

        val sms =
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECEIVE_SMS
                )
                ==
                PackageManager.PERMISSION_GRANTED
            )

                "✔ SMS"

            else

                "✖ SMS"

        val loc =
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                ==
                PackageManager.PERMISSION_GRANTED
            )

                "✔ Location"

            else

                "✖ Location"

        statusText.text =
            "$sms | $loc"
    }

    private fun showExitWarning() {

        AlertDialog.Builder(
            this
        )

            .setTitle(
                "Exit?"
            )

            .setMessage(
                "Protection may stop"
            )

            .setPositiveButton(
                "Exit"
            ) { _, _ -> finish() }

            .setNegativeButton(
                "Keep Running",
                null
            )

            .show()
    }
}