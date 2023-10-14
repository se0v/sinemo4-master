package com.example.sinemo.screens

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background((Color.DarkGray))
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(57.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(imageVector = Icons.Default.Warning,
            contentDescription = null,
            Modifier.size(150.dp)
        )
        Text(text = "For use this app, you must provide the following accesses:",
            style = TextStyle(color = Color.White)
        )
        Button(
            onClick = {
                val intentAccess = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intentAccess)
            },
            colors = ButtonDefaults
                .buttonColors(backgroundColor = Color.Black),
            modifier = Modifier.width(300.dp)) {
            Text(text = "Access to Accessibility Settings ")
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )
        }
        Button(
            onClick = {
                val intentNotify = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intentNotify)
            },
            colors = ButtonDefaults
                .buttonColors(backgroundColor = Color.Black),
            modifier = Modifier.width(300.dp)) {
            Text(text = "Access to Notifications ")
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null
            )
        }
        Button(
            onClick = {
                val intentFiles = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                context.startActivity(intentFiles)
            },
            colors = ButtonDefaults
                .buttonColors(backgroundColor = Color.Black),
            modifier = Modifier.width(300.dp)) {
            Text(text = "Access to All Files ")
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null
            )
        }
    }
}