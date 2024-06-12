package com.app.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import com.app.widget.ui.AppWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = AppWidget()


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show()
        Log.e("ZZUPDATE", "HEREEEEEEEE")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val date = intent.getStringExtra("target_date")
        Toast.makeText(context, "Date: $date", Toast.LENGTH_SHORT).show()
        GlobalScope.launch {
            GlanceAppWidgetManager(context).getGlanceIds(AppWidget::class.java).forEach {
                updateAppWidgetState(context,it){
                    it[stringPreferencesKey("target_date")] = date.orEmpty()
                }
                glanceAppWidget.update(context, it)
            }
        }
    }
}