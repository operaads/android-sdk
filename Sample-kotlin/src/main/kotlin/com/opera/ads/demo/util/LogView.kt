package com.opera.ads.demo.util

import android.text.TextUtils
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class LogView(
    private var logView: TextView,
    private var scrollView: ScrollView,
) {
    private val formatter = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM) as SimpleDateFormat
    private var bufferedLog = ""

    init {
        if (!TextUtils.isEmpty(bufferedLog)) {
            logView.text = bufferedLog
            bufferedLog = ""
        }
    }

    fun print(message: String) {
        val time = formatter.format(Calendar.getInstance().getTime())
        val msg = "($time) $message\n"
        logView.append(msg)
        scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
    }
}
