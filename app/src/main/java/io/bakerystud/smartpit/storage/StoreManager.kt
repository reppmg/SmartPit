package io.bakerystud.smartpit.storage

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import io.bakerystud.smartpit.model.RecordWithLocation
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class StoreManager @Inject constructor(
    private val context: Context
) {
    fun saveLog(data: List<RecordWithLocation>) {
        val jsonLog = Gson().toJson(data)
        val simpleDateFormat = SimpleDateFormat("MM-dd_hh:mm:ss-S", Locale.US)
        val date = simpleDateFormat.format(Date())
        val fileName = "$date.json"
        saveFile(fileName, jsonLog)
    }

    private fun saveFile(fileName: String, text: String) {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val myDir = File(root, "ExifDemo")
        myDir.mkdirs()
        val logFile = File(myDir, fileName)
        logFile.createNewFile()
        logFile.writeText(text)
    }
}