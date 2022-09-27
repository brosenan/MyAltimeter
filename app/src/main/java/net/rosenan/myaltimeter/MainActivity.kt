package net.rosenan.myaltimeter

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import net.rosenan.myaltimeter.databinding.ActivityMainBinding
import net.rosenan.timeseries.avionics.Altitude
import net.rosenan.timeseries.bindTextView
import net.rosenan.timeseries.sensors.LocationTimeSeries
import net.rosenan.timeseries.sensors.SensorTimeSeries
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pressure: SensorTimeSeries
    private lateinit var location: LocationTimeSeries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pressure = SensorTimeSeries(this, Sensor.TYPE_PRESSURE, SensorManager.SENSOR_DELAY_NORMAL)
        val verticalVelocityView = findViewById<TextView>(R.id.verticalVelocity)
        bindTextView(pressure.map { values -> "${values[0]} hPa" }, verticalVelocityView)

        location = LocationTimeSeries(this, 30000)
        val altitudeView = findViewById<TextView>(R.id.altitude)
        bindTextView(Altitude(pressure, location).map { altitude -> round(altitude).toInt() }
            .map { altitude -> "${altitude}m" }, altitudeView)

        val timeView = findViewById<TextView>(R.id.timeView)
        bindTextView(
            pressure.map { Calendar.getInstance().time }
                .map { date -> SimpleDateFormat("HH:mm").format(date) },
            timeView
        )
    }

    override fun onResume() {
        super.onResume()
        pressure.start()
        location.start()
    }

    override fun onPause() {
        super.onPause()
        pressure.stop()
        location.stop()
    }
}