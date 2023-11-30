package com.example.sinemo

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.*

class AudioViewModel : ViewModel() {
    @SuppressLint("SimpleDateFormat")
    var recordList = mutableStateListOf<DataRecord>()
    fun addRecord(record: DataRecord) {
        recordList.add(record)
    }
    fun deleteRecord(record: DataRecord) {
        recordList.remove(record)
    }
}