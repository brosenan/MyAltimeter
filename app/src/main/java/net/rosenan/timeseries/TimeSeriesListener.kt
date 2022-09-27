package net.rosenan.timeseries

interface TimeSeriesListener<T> {
    fun onEvent(source: TimeSeriesProvider<T>, value: T, timestampMillis: Long)
}
