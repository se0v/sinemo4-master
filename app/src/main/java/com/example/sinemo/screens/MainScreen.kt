package com.example.sinemo.screens

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SinEmo")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background((Color(0xFF1F2022)))
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 57.dp, end = 57.dp, top = 25.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("lazyList") },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                modifier = Modifier.size(180.dp)
            ) {
                Text(text = "Last Record", color = Color.White)
            }
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "For use this app, you must provide the following accesses to:",
                style = TextStyle(color = Color.White, fontSize = 16.sp),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    val intentAccess = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intentAccess)
                },
                shape = RoundedCornerShape(corner = CornerSize(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF282828)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                Text(text = "Accessibility ", color = Color.White)
                Icon(imageVector = Icons.Default.Star, contentDescription = null)
            }
            Button(
                onClick = {
                    val intentNotify = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    context.startActivity(intentNotify)
                },
                shape = RoundedCornerShape(corner = CornerSize(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF282828)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Notifications ", color = Color.White)
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
            }
            Button(
                onClick = {
                    val intentFiles = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    context.startActivity(intentFiles)
                },
                shape = RoundedCornerShape(corner = CornerSize(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF282828)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            ) {
                Text(text = "All Files ", color = Color.White)
                Icon(imageVector = Icons.Default.List, contentDescription = null)
            }
        }
    }}
