package com.example.sinemo
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.sinemo.navigation.AppNavigation
import com.example.sinemo.ui.theme.SinemoTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "NewApi", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                val REQUEST_RECORD_AUDIO_PERMISSION = 1
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
            }

            val navController = rememberNavController()

            SinemoTheme {
                Scaffold(
                    content = {
                        AppNavigation(navController)
                    }
                )
            }
        }
    }
}











