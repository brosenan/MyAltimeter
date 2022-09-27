package net.rosenan.timeseries

import android.widget.TextView

fun bindTextView(provider: TimeSeriesProvider<String>, textView: TextView) {
    provider.addTimeSeriesListener { _, value, _ -> textView.text = value }
}
