package com.xmwsh.videolib.media;

import android.content.Context;
import android.content.SharedPreferences;

public final class MeasureScanSp {
    private static final String SCAN_NAME = "video_scan";
    private static final String SCAN = "scan";

    public static void setVideoScan(Context context,int scanType) {
        SharedPreferences sp = context.getSharedPreferences(SCAN_NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(SCAN,scanType).commit();
    }

    public static int getVideoScan(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SCAN_NAME,Context.MODE_PRIVATE);
        return sp.getInt(SCAN,0);
    }
}
