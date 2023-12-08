package com.example.sinemo.screens

import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.sinemo.BuildConfig
import com.example.sinemo.DataRecord
import com.example.sinemo.MyAccessibilityService
import com.example.sinemo.R
import com.example.sinemo.audioViewModel
import java.io.File

@Composable
fun SoundGraph(recordList: List<Float>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(75.dp)) {
        val width = size.width
        val height = size.height / 2  // Половина высоты для отображения вверх и вниз

        drawLine(
            color = Color.White,
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 1f
        )

        if (recordList.isNotEmpty()) {
            val stepSize = width / recordList.size

            recordList.forEachIndexed { index, amplitude ->
                val x = index * stepSize

                // Отображаем амплитуды вверх и вниз
                val yUp = height - amplitude * height
                val yDown = height + amplitude * height

                // Рисуем линии для амплитуды
                drawLine(
                    color = Color.White,
                    start = Offset(x, height),
                    end = Offset(x, yUp),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(x, height),
                    end = Offset(x, yDown),
                    strokeWidth = 1f
                )
            }
        }
    }
}


@Composable
fun AudioPlayerWithGraph(
    audioPath: String,
    modifier: Modifier = Modifier
) {
    // Используем ваш код SoundGraph, предполагая, что у вас есть данные звука для отображения
    val soundData = listOf(0.1f, 0.3f, 0.5f, 0.2f, 0.4f, 0.3f, 0.5f, 0.2f, 0.4f, 0.3f, 0.5f, 0.2f, 0.4f, 0.3f, 0.5f, 0.2f, 0.4f, 0.3f, 0.5f, 0.2f, 0.4f)

    // Добавляем элементы управления для обрезки
    var startCutIndex by remember { mutableStateOf(0) }
    var endCutIndex by remember { mutableStateOf(soundData.size) }

    Column(
        modifier = modifier
    ) {
        SoundGraph(recordList = soundData.subList(startCutIndex, endCutIndex))

        // Добавляем элементы управления для обрезки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Start: $startCutIndex")
            Text("End: $endCutIndex")
        }
    }
}

@Composable
fun LazyListScreen(
) {

    val context = LocalContext.current
    val dataSet = audioViewModel.recordList

    var showDialog by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<DataRecord?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Records") }
            )
        }
    ) {
        var isEmpty by remember { mutableStateOf(dataSet.isEmpty()) }
        LaunchedEffect(dataSet) {
            isEmpty = dataSet.isEmpty()
        }
        if (isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF1F2022)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No records")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF1F2022)),
                state = rememberLazyListState()
            ) {
                items(
                    items = dataSet
                ) { record ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(color = Color(0xFF282828), shape = RoundedCornerShape(corner = CornerSize(12.dp)))
                            .padding(vertical = 4.dp)
                    ){
                    Row {
                        AudioPlayer(
                            audioPath = record.audioPath,
                            modifier = Modifier.size(48.dp)
                        )
                        //AudioPlayerWithGraph(audioPath = record.audioPath, modifier = Modifier.size(48.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = record.heading,
                                style = MaterialTheme.typography.h5
                            )
                            Text(
                                text = record.subtext
                            )
                        }

                        IconButton(onClick = {
                            val myAccessibilityService = MyAccessibilityService()
                            myAccessibilityService.stopRecording(audioViewModel)
                            try {
                                val file = File(record.audioPath)
                                if (file.exists()) {
                                    val uri = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        file
                                    )
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    intent.type = "audio/ogg"
                                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.setPackage("org.telegram.messenger")
                                    context.startActivity(intent)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.telega_logo),
                            contentDescription = null,
                            Modifier
                                .padding(top = 10.dp, end = 10.dp).size(30.dp)
                        )
                        IconButton(onClick = {
                            showDialog = true
                            recordToDelete = record
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                Modifier
                                    .padding(top = 6.dp, end = 5.dp)
                            )
                        }
                    }
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    // Close dialog if cancel
                                    showDialog = false
                                    recordToDelete = null
                                },
                                title = { Text("Delete Record") },
                                text = { Text("Are you sure you want to delete this record?") },
                                shape = RoundedCornerShape(corner = CornerSize(24.dp)),
                                backgroundColor = Color(0xFF1F2022),
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            // Confirm delete
                                            audioViewModel.deleteRecord(recordToDelete!!)
                                            showDialog = false
                                            recordToDelete = null
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1F2022)),
                                        shape = CircleShape
                                    ) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            // Cancel delete
                                            showDialog = false
                                            recordToDelete = null
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1F2022)),
                                        shape = CircleShape
                                    ) {
                                        Text("No")
                                    }
                                }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 60.dp, end = 16.dp, bottom = 4.dp)
                                .background(color = Color(0xFF1F2022), shape = RoundedCornerShape(corner = CornerSize(12.dp)))
                                .height(100.dp) // Задайте желаемую высоту
                        ){
                            AudioPlayerWithGraph(audioPath = record.audioPath, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AudioPlayer(
    audioPath: String,
    modifier: Modifier = Modifier
) {
    val mediaPlayer = remember {
        if (File(audioPath).exists()) {
            MediaPlayer().apply {
                setDataSource(audioPath)
                prepare()
            }
        } else {
            null
        }
    }

    val isPlaying by remember { mutableStateOf(mediaPlayer?.isPlaying ?: false) }

    DisposableEffect(mediaPlayer) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    IconButton(
        onClick = {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                } else {
                    it.start()
                }
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Place
            else Icons.Default.PlayArrow,
            contentDescription = null
        )
    }
}
