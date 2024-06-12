package com.app.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder


class WidgetService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}