package com.example.playlistmaker.data.impl.favorite.playlists

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.random.Random

class ImageSaver(private val context: Context) {

    private suspend fun copy(file: File, fileCopy: File) {
        withContext(Dispatchers.IO) {
            FileInputStream(file).use { inp ->
                FileOutputStream(fileCopy).use { out ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while ((inp.read(buf).also { len = it }) > 0) {
                        out.write(buf, 0, len)
                    }

                    out.close()
                }

                inp.close()
            }
        }
    }

    private suspend fun copy(uri: Uri, fileCopy: File) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inp ->
                FileOutputStream(fileCopy).use { out ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while ((inp.read(buf).also { len = it }) > 0) {
                        out.write(buf, 0, len)
                    }

                    out.flush()
                    out.close()
                }

                inp.close()
            }
        }
    }

    suspend fun saveCover(uri: Uri, fileName: String) {

        val file = File(context.getDir(PLAYLIST_COVERS, MODE_PRIVATE), fileName)

        copy(uri, file)
    }

    suspend fun getCoverUri(fileName: String): Uri {
        val file = File(context.getDir(PLAYLIST_COVERS, MODE_PRIVATE), fileName)

        return Uri.fromFile(file)
    }

    private fun createTmpFile(ext: String = "jpg"): File {
        val rnd = Random.nextInt(1, 100)
        val fileName = "tmp_$rnd.$ext"
        val file = File(context.cacheDir, fileName)
        file.createNewFile()
        return file
    }

    suspend fun saveToTmpStorage(imageFile: File): Uri {
        val file = createTmpFile()
        copy(imageFile, file)
        return Uri.fromFile(file)
    }

    suspend fun saveToTmpStorage(imageUri: Uri): Uri {
        val file = createTmpFile()
        withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(file)
            BitmapFactory.decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }
        return Uri.fromFile(file)
    }

    suspend fun saveToTmpStorage(bitmap: Bitmap): Uri {

        val file = createTmpFile("jpg")
        withContext(Dispatchers.IO) {
            val bas = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bas)

            val out = FileOutputStream(file)
            out.write(bas.toByteArray())
            out.flush()
            out.close()
        }
        return Uri.fromFile(file)
    }

    companion object {
        const val PLAYLIST_COVERS = "playlist_covers"
    }
}