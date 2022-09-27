package net.rosenan.timeseries

import java.util.*

class BoxCar(input: TimeSeriesProvider<Double>, durationMillis: Long) :
    TimeSeriesProvider<Double>() {
    private val history = LinkedList<Pair<Long, Double>>()
    private var sum = 0.0

    init {
        input.addTimeSeriesListener { _, value, timestampMillis ->
            while (!history.isEmpty() && history.elementAt(0).first < timestampMillis - durationMillis) {
                sum -= history.removeFirst().second
            }
            history.add(Pair(timestampMillis, value))
            sum += value

            broadcast(sum / history.size, timestampMillis - durationMillis / 2)
        }
    }
}