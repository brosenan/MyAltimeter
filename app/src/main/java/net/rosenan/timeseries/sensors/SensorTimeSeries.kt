package net.rosenan.timeseries.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import net.rosenan.timeseries.TimeSeriesProvider

class SensorTimeSeries(context: Context, sensorType: Int, private val sensorDelay: Int) :
    TimeSeriesProvider<FloatArray>(), SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(sensorType)

    fun start() {
        sensorManager.registerListener(this, sensor, sensorDelay)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(ev: SensorEvent?) {
        if (ev == null) return
        Log.v("BR", "Got sensor reading: ${ev.values[0]}")

        broadcast(ev.values, ev.timestamp / 1000000)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Ignore.
    }
}