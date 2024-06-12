package com.app.widget.ui

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class AppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Box(modifier = GlanceModifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {

                Text(text = "Hello", style =  TextStyle(fontSize = 24.sp))
            }
        }
    }
}

