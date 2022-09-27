package net.rosenan.timeseries

open class TimeSeriesProvider<T> {
    fun addTimeSeriesListener(listener: TimeSeriesListener<T>) {
        listeners.add(listener)
    }

    var listeners = ArrayList<TimeSeriesListener<T>>()

    protected fun broadcast(value: T, timestampMillis: Long) {
        for (listener in listeners) {
            listener.onEvent(this, value, timestampMillis)
        }
    }
}
