package net.rosenan.timeseries

class MapTimeSeries<X, Y>(private val map: (X) -> Y) : TimeSeriesListener<X>,
    TimeSeriesProvider<Y>() {
    override fun onEvent(source: TimeSeriesProvider<X>, value: X, timestampMillis: Long) {
        broadcast(map(value), timestampMillis)
    }
}
