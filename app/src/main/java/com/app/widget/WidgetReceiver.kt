package com.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.app.widget.ui.AppWidget
import com.app.widget.ui.dateKey
import com.app.widget.ui.widgetStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = AppWidget()


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show()
        Log.e("ZZupdate", "onupdate")
    }

//    override fun onReceive(context: Context, intent: Intent) {
//        super.onReceive(context, intent)
//        val date = intent.getStringExtra("target_date").orEmpty()
//        Toast.makeText(context, "Date: $date", Toast.LENGTH_SHORT).show()
//        GlobalScope.launch {
//
//            GlanceAppWidgetManager(context).getGlanceIds(AppWidget::class.java).forEach {
//                updateAppWidgetState(context, it) {
//                    it.toMutablePreferences().apply { set(dateKey, date) }
//                }
//            }
//            context.widgetStore.updateData {
//                it.toMutablePreferences().apply { set(dateKey, date) }
//            }
//            glanceAppWidget.updateAll(context)
//        }
//    }
}