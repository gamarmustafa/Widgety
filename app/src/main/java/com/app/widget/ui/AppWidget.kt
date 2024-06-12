package com.app.widget.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
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
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            val prefs = currentState<Preferences>()
            val date = prefs[stringPreferencesKey("target_date")].orEmpty()
            var remainingTime by remember { mutableStateOf(calculateRemainingTime(date)) }

            LaunchedEffect(Unit) {
                while (true) {
                    remainingTime = calculateRemainingTime(date)
                    delay(1000)
                }
            }


            Box(
                modifier = GlanceModifier.fillMaxSize()
                    .background(GlanceTheme.colors.widgetBackground).padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${remainingTime.days} days \n${remainingTime.hours} hours \n${remainingTime.minutes} minutes\n${remainingTime. seconds} seconds remaining",
                    style = TextStyle(fontSize = 24.sp, color = GlanceTheme.colors.onSurface
                ))

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

        return TimeRemaining(days, hours, minutes,seconds)
    }

    data class TimeRemaining(val days: Long, val hours: Long, val minutes: Long,val seconds: Long)









    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CountdownWidget(date: String) {
        val currentDateTime = LocalDateTime.now()
        val targetDateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val duration = Duration.between(currentDateTime, targetDateTime)
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        Text(
            text = "Countdown: $days days, $hours hours, $minutes minutes",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
    }




}


