@file:OptIn(DelicateCoroutinesApi::class)

package com.app.widget

import android.app.DatePickerDialog
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.AppWidgetId
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.app.widget.ui.dateKey
import com.app.widget.ui.theme.WidgetyTheme
import com.app.widget.ui.widgetStore
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar


class MainActivity : ComponentActivity() {

    private val widgetReceiver = WidgetReceiver()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val id = intent.getStringExtra("id")
        Log.e("ZZid", "$id")
        val glanceId = Gson().fromJson(id, AppWidgetId::class.java)
        createNewWidget()


        enableEdgeToEdge()
        setContent {
            WidgetyTheme {
                DatePickerContent(glanceId) {

                }
            }
        }
    }


    fun createNewWidget(): Intent {
        val host = AppWidgetHost(this, 1)
        val manager = AppWidgetManager.getInstance(this)

        val widgetId = host.allocateAppWidgetId()
        val widgetList = manager.installedProviders

        var provider: AppWidgetProviderInfo? = null
        for (info in widgetList) {

            if (info.provider.className == "com.app.widget.WidgetReceiver") {
                provider = info
                break
            }
        }
        val bindIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
        bindIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        bindIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider?.provider)
        return bindIntent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun DatePickerDialog(widgetId: GlanceId?, onDateSelected: (LocalDateTime) -> Unit) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
                onDateSelected(newDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            val createWidget =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        datePickerDialog.show()
                    } else {
                        Toast.makeText(this@MainActivity, "RESULT NOT OK", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            if (widgetId == null) {
                Button(onClick = {
                    val intent = createNewWidget()
                    createWidget.launch(intent)
                }) {
                    Text("Create a New Widget")
                }
            } else {
                Button(onClick = {
                    datePickerDialog.show()
                }) {
                    Text("Pick a New Date")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun DatePickerContent(widgetId: GlanceId?, finishActivity: () -> Unit) {
        val context = LocalContext.current
        var date by remember { mutableStateOf(LocalDateTime.now()) }

        Column {
            DatePickerDialog(widgetId, onDateSelected = { selectedDate ->
                date = selectedDate

                if (widgetId != null) {
                    GlobalScope.launch {
                        context.widgetStore.updateData {
                            it.toMutablePreferences().apply { set(stringPreferencesKey(widgetId.toString()), date.toString()) }
                        }
                        widgetReceiver.glanceAppWidget.update(context, widgetId)
                    }
                } else {
                    GlobalScope.launch {
                        context.widgetStore.updateData {
                            it.toMutablePreferences().apply { set(dateKey, date.toString()) }
                        }
                        widgetReceiver.glanceAppWidget.updateAll(context)
                    }
                }


//
//                val intent = Intent(context, WidgetReceiver::class.java)
//                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                intent.putExtra("target_date", date.toString())
//                context.sendBroadcast(intent)
//                finishActivity()
            })

        }

    }
}

