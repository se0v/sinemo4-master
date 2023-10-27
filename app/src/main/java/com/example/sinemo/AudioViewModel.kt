package com.example.sinemo

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.*

class AudioViewModel : ViewModel() {
    private val currentTimeMillis = System.currentTimeMillis()
    private val date = Date(currentTimeMillis)
    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy ")
    private val formattedDate: String = dateFormat.format(date)
    var recordList = mutableStateListOf(
    DataRecord(
    heading = "recording$numRec",
    subtext = formattedDate,
    audioPath = output
    )
    )
    fun addRecord(record: DataRecord) {
        recordList.add(record)
    }
}