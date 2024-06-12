package com.app.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.widget.ui.theme.WidgetyTheme
import java.time.LocalDateTime
import java.util.Calendar
import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.app.widget.ui.AppWidget

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WidgetyTheme {
                DatePickerContent{

                }
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(onDateSelected: (LocalDateTime) -> Unit) {
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

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {

        Button(onClick = {
            datePickerDialog.show()
        }) {
            Text("Pick a Date")
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerContent(finishActivity:() ->Unit) {
    val context = LocalContext.current
    var date by remember { mutableStateOf(LocalDateTime.now()) }

    Column {
        DatePickerDialog(onDateSelected = { selectedDate ->
            date = selectedDate
            val intent = Intent(context, WidgetReceiver::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra("target_date", date.toString())
            context.sendBroadcast(intent)
            finishActivity()
        })

//            val sharedPreferences = context.getSharedPreferences("countdown_prefs", Context.MODE_PRIVATE)
//            with(sharedPreferences.edit()) {
//                putString("target_date", date.toString())
//                apply()
//            }
            // Update the widget

    }


}
