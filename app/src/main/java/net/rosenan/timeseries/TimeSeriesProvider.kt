package net.rosenan.timeseries

typealias TimeSeriesListener<T> = (source: TimeSeriesProvider<T>, value: T, timestampMillis: Long) -> Unit

open class TimeSeriesProvider<T> {
    fun addTimeSeriesListener(listener: TimeSeriesListener<T>) {
        listeners.add(listener)
    }

    var listeners = ArrayList<TimeSeriesListener<T>>()

    protected fun broadcast(value: T, timestampMillis: Long) {
        for (listener in listeners) {
            listener(this, value, timestampMillis)
        }
    }

    fun <Y> map(f: (T) -> Y): TimeSeriesProvider<Y> {
        val m = TimeSeriesProvider<Y>()
        addTimeSeriesListener { _, value, timestampMillis ->
            m.broadcast(
                f(value),
                timestampMillis
            )
        }
        return m
    }
}
