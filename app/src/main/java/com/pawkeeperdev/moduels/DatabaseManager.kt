package com.pawkeeperdev.moduels
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.room.Room
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.dataBase.UserDataBase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DatabaseManager(private val context: Context) {

    @SuppressLint("SdCardPath")
    private val databaseName = "userData"

    fun handleReceivedDatabase(inputStream: InputStream) {
        val databasePath = context.getDatabasePath(databaseName)
        FileOutputStream(databasePath).use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        Room.databaseBuilder(
            context,
            UserDataBase::class.java,
            databaseName
        ).build()
    }

    fun shareDatabaseFile() {
        val databasePath = context.getDatabasePath(databaseName)
        val fileUri = FileProvider.getUriForFile(
            context,
            "com.demowork.roomdatademo.fileprovider",
            databasePath
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/octet-stream"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share database file"))

        /*val databasePath = context.getDatabasePath(databaseName)
        val renamedFile = renameFileToDb(databasePath, "renamed_database.db")

        if (renamedFile != null) {
            val fileUri = FileProvider.getUriForFile(
                context,
                "com.demowork.roomdatademo.fileprovider",
                renamedFile
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant URI permissions
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share database file"))
        } else {
            // Handle the error case where the file couldn't be renamed
        }*/

    }



    private fun renameFileToDb(originalFile: File, newFileName: String): File? {
        val newFile = File(originalFile.parentFile, newFileName)
        return try {
            if (originalFile.renameTo(newFile)) {
                newFile
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    private fun saveDatabaseFile(newLocation: File): Boolean {
        val databasePath = context.getDatabasePath(databaseName)
        return try {
            val inputFile = FileInputStream(databasePath)
            val outputFile = FileOutputStream(newLocation)
            inputFile.copyTo(outputFile)
            inputFile.close()
            outputFile.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun saveDatabase() {
        val newLocation = File(context.getExternalFilesDir(null), "saved_database.db")
        if (saveDatabaseFile(newLocation)) {
            CommonUtils.addLogE("DatabaseManager","file saved :$newLocation")
        } else {
            CommonUtils.addLogE("DatabaseManager","Error!!!!!!!")
        }
    }


}
