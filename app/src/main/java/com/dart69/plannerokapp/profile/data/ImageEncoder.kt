package com.dart69.plannerokapp.profile.data

import android.content.Context
import android.util.Base64
import androidx.core.net.toUri
import com.dart69.plannerokapp.core.extension
import dagger.hilt.android.qualifiers.ApplicationContext
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
            context.contentResolver.openInputStream(uri.toUri()).use {
                val bytes = it!!.readBytes()
                return BaseData(
                    base64 = Base64.encodeToString(bytes, Base64.DEFAULT),
                    name = "${UUID.randomUUID()}.${uri.extension()}",
                )
            }
        }
    }
}