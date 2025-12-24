package com.example.calculator

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File

class LocationActivity : LocationListener, AppCompatActivity()  {

    val value: Int = 0
    val LOG_TAG: String = "LOCATION_ACTIVITY"

    private lateinit var bBackToMain: Button
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION= 100
    }
    data class LocationData(
        val latitude: Double,
        val longitude: Double,
        val altitude: Double,
        val time: Long
    )
    private lateinit var locationManager: LocationManager
    private lateinit var tvLat: TextView
    private lateinit var tvLon: TextView
    private lateinit var tvAppContext: TextView
    private lateinit var tvActivityContext: TextView
    private lateinit var tvAlt: TextView
    private lateinit var tvTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bBackToMain = findViewById<Button>(R.id.back_to_main)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        tvLat = findViewById(R.id.tv_lat) as TextView
        tvLon = findViewById(R.id.tv_lon) as TextView
        tvAppContext = findViewById(R.id.tv_appContext) as TextView
        tvActivityContext = findViewById(R.id.tv_activityContext) as TextView
        tvAlt = findViewById(R.id.tv_alt) as TextView
        tvTime = findViewById(R.id.tv_time) as TextView
        tvAppContext.setText(applicationContext.toString())
        tvActivityContext.setText(this.toString())
    }

    override fun onResume() {
        super.onResume()
        bBackToMain.setOnClickListener {
            val backToMain = Intent(this, MainActivity::class.java)
            startActivity(backToMain)
        }
        updateCurrentLocation()
    }

    private fun updateCurrentLocation(){
        if (!checkPermissions()) {
            requestPermissions()
            return
        }
        if (!isLocationEnabled()) {
            Toast.makeText(applicationContext, "Enable location in settings", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val MIN_TIME_MS: Long = 5000
        val MIN_DISTANCE_M: Float = 5f
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_MS,
                MIN_DISTANCE_M,
                this
            )
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                onLocationChanged(it)
            }
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_MS,
                MIN_DISTANCE_M,
                this
            )
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
                onLocationChanged(it)
            }
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
            !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "Location providers are unavailable.", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestPermissions() {
        Log.w(LOG_TAG, "requestPermissions()");
        val permissionsToRequest = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        ActivityCompat.requestPermissions(
            this,
            permissionsToRequest,
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean{
        if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            return true
        } else {
            return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                updateCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Denied by user", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isLocationEnabled(): Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onLocationChanged(location: Location) {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = dateFormat.format(Date(location.time))
        tvLat.setText("Latitude: ${location.latitude}")
        tvLon.setText("Longitude: ${location.longitude}")
        tvAlt.setText("Altitude: ${location.altitude} м")
        tvTime.setText("Time: $formattedTime")
        writeLocationToJsonFile(location)
    }

    private fun writeLocationToJsonFile(location: Location) {
        val data = LocationData(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            time = location.time
        )
        val jsonObject = JSONObject().apply {
            put("latitude", data.latitude)
            put("longitude", data.longitude)
            put("altitude", data.altitude)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            put("current_time_ms", data.time)
            put("current_time_formatted", dateFormat.format(Date(data.time)))
        }
        try {
            val fileName = "location_updates.json"
            val file = File(this.filesDir, fileName)
            FileWriter(file, true).use { writer ->
                writer.write(jsonObject.toString())
                writer.write("\n")
            }
            Log.i(LOG_TAG, "Location saved to file: ${file.absolutePath}")
            Toast.makeText(this, "Location data saved!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error writing location to file: ${e.message}")
            e.printStackTrace()
        }
    }
}

