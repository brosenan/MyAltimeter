package net.rosenan.timeseries.avionics

import android.location.Location
import net.rosenan.timeseries.TimeSeriesProvider
import kotlin.math.ln

class Altitude(pressure: TimeSeriesProvider<FloatArray>, location: TimeSeriesProvider<Location>) :
    TimeSeriesProvider<Double>() {
    private var lastPressure = 1013.25
    private var seenPressure = false
    private var lastGpsAltitude = 0.0
    private var seenGpsAltitude = false
    private var basePressure = 0.0
    private var hasBasePressure = false

    companion object {
        const val RT_BY_MG = 8435.845382608
    }

    init {
        pressure.map { values -> values[0].toDouble() }
            .addTimeSeriesListener { _, pressure, timestampMillis ->
                lastPressure = pressure
                seenPressure = true
                if (hasBasePressure) {
                    broadcast(pressureAltitude(), timestampMillis)
                }
            }
        location.map { location -> location.altitude }
            .addTimeSeriesListener { _, altitude, timestampMillis ->
                lastGpsAltitude = altitude
                seenGpsAltitude = true
                if (seenPressure) {
                    basePressure = lastPressure
                    hasBasePressure = true
                }
                broadcast(lastGpsAltitude, timestampMillis)
            }
    }

    private fun pressureAltitude(): Double {
        return lastGpsAltitude - RT_BY_MG * ln(lastPressure / basePressure)
    }
}