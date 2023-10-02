package com.example.sinemo.screens

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sinemo.audioViewModel
import java.io.File

@Composable
fun LazyListScreen(
) {

    val dataSet = audioViewModel.recordList

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(color = Color.DarkGray)
    ) {
        items(
            items = dataSet
        ) { record ->
            Row {
                AudioPlayer(
                    audioPath = record.audioPath,
                    modifier = Modifier.size(48.dp),
                )

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

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
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
            imageVector = if (mediaPlayer?.isPlaying == true) Icons.Default.Place
            else Icons.Default.PlayArrow,
            contentDescription = null
        )
    }
}