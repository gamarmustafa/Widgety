package com.app.widget.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val Context.widgetStore by preferencesDataStore(name = "widget_data_store")
val dateKey = stringPreferencesKey("target_date")

class AppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val store = context.widgetStore
        val initial = store.data.first()
        provideContent {

            val data by store.data.collectAsState(initial = initial)
            val newDate = data[dateKey].orEmpty()

            var remainingTime by remember { mutableStateOf(calculateRemainingTime(newDate)) }


            Log.e("zzzDate", newDate)


            LaunchedEffect(Unit) {
                while (true) {
                    remainingTime = calculateRemainingTime(newDate)
                    delay(1000)
                }
            }
            Box(
                modifier = GlanceModifier.fillMaxSize()
                    .background(GlanceTheme.colors.widgetBackground).padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                    Text(
                        text = "${remainingTime.days} days \n${remainingTime.hours} hours \n${remainingTime.minutes} minutes\n${remainingTime.seconds} seconds remaining",
                        style = TextStyle(
                            fontSize = 24.sp, color = GlanceTheme.colors.onSurface
                        )
                    )
            }
        }
    }


    private fun calculateRemainingTime(dateString: String): TimeRemaining {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val currentTime = Calendar.getInstance().time

        val difference = date.time - currentTime.time
        val minutes = difference / (1000 * 60) % 60
        val seconds = difference / 1000 % 60
        val hours = difference / (1000 * 60 * 60) % 24
        val days = difference / (1000 * 60 * 60 * 24)

        return TimeRemaining(days, hours, minutes, seconds)
    }

    data class TimeRemaining(val days: Long, val hours: Long, val minutes: Long, val seconds: Long)

}


