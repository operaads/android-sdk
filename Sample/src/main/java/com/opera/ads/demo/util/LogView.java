package com.opera.ads.demo.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogView {
    @Nullable
    private TextView mLogView;
    @Nullable
    private ScrollView mScrollView;
    @NonNull
    private final SimpleDateFormat formatter =
            (SimpleDateFormat) SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM);
    @NonNull
    private String mBufferedLog = "";

    public LogView() {
    }

    public void setUp(@NonNull TextView logView, @NonNull ScrollView scroller) {
        mLogView = logView;
        mScrollView = scroller;
        if (!TextUtils.isEmpty(mBufferedLog)) {
            mLogView.setText(mBufferedLog);
            mBufferedLog = "";
        }
    }

    public void print(@NonNull String message) {
        final String time = formatter.format(Calendar.getInstance().getTime());
        final String msg = "(" + time + ") " + message + "\n";
        if (mLogView == null) {
            mBufferedLog += msg;
            return;
        }
        mLogView.append(msg);
        if (mScrollView != null) mScrollView.post(() -> mScrollView.fullScroll(View.FOCUS_DOWN));
    }
}
