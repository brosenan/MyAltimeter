package net.rosenan.timeseries

class Derivative(input: TimeSeriesProvider<Double>) : TimeSeriesProvider<Double>() {
    private var lastValue = 0.0
    private var hasValue = false
    private var lastTimestamMillis = 0L

    init {
        input.addTimeSeriesListener { _, value, timestampMillis ->
            if (hasValue) {
                val delta_t = (timestampMillis - lastTimestamMillis) / 1000.0
                val delta_v = value - lastValue
                broadcast(delta_v / delta_t, lastTimestamMillis + (delta_t * 500).toLong())
            }

            lastValue = value
            hasValue = true
            lastTimestamMillis = timestampMillis
        }
    }
}
