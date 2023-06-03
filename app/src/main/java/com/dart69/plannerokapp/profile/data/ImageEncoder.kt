package com.dart69.plannerokapp.profile.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import javax.inject.Inject

interface ImageEncoder {
    suspend fun toBase64(uri: String): String

    class Implementation @Inject constructor(
        private val quality: Int,
    ) : ImageEncoder {
        override suspend fun toBase64(uri: String): String {
            val stream = ByteArrayOutputStream()
            BitmapFactory.decodeFile(uri).also { bitmap ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            }
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
        }
    }
}