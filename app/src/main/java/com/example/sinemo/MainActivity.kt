package com.example.sinemo
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.sinemo.navigation.AppNavigation
import com.example.sinemo.navigation.AppScreen
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

            /*val startRecBroadcastReceiver: StartRecBroadcastReceiver?
            //register a receiver to tell the MainActivity when a notification has been received
            startRecBroadcastReceiver = StartRecBroadcastReceiver()
            val intentFilter = IntentFilter()
            intentFilter.addAction("com.example.sinemo")
            registerReceiver(startRecBroadcastReceiver, intentFilter)*/

            val navController = rememberNavController()
            val screens = AppScreen.getAll()
            var selectedScreen by remember {
                mutableStateOf(screens.first())
            }

            SinemoTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("SinEmo")
                            }
                        )
                    },
                    content = {
                        AppNavigation(navController)
                    },
                    bottomBar = {
                        BottomNavigation {
                            screens.forEach { screen ->
                                BottomNavigationItem(
                                    selected = selectedScreen == screen,
                                    onClick = {
                                        selectedScreen = screen
                                        navController.navigate(screen.route)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text(text = stringResource(screen.nameResource))
                                    },
                                    alwaysShowLabel = false

                                )
                            }
                        }
                    }
                )
            }
        }
    }
}











