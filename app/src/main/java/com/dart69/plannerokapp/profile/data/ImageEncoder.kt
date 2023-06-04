package com.dart69.plannerokapp.profile.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.net.toUri
import com.dart69.plannerokapp.core.extension
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

interface ImageEncoder {
    data class BaseData(
        val base64: String,
        val name: String,
    )

    fun toBase64(uri: String): BaseData

    class Implementation @Inject constructor(
        @ApplicationContext private val context: Context,
    ) : ImageEncoder {
        override fun toBase64(uri: String): BaseData {
            context.contentResolver.openInputStream(uri.toUri()).use { inputStream ->
                val outputStream = ByteArrayOutputStream()
                BitmapFactory.decodeStream(inputStream).also { bitmap ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION, outputStream)
                }
                val bytes = inputStream!!.readBytes()
                return BaseData(
                    base64 = Base64.encodeToString(bytes, Base64.DEFAULT),
                    name = "${UUID.randomUUID()}.${uri.extension()}",
                )
            }
        }

        private companion object {
            const val COMPRESSION = 65
        }
    }
}