package com.dart69.plannerokapp.core

import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import com.dart69.mvvm.strings.StringResource
import com.dart69.plannerokapp.R
import com.faltenreich.skeletonlayout.SkeletonLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10

var SkeletonLayout.isSkeletonVisible: Boolean
    get() = isShown
    set(isVisible) {
        if (isVisible) showSkeleton() else showOriginal()
    }

val Int.length: Int
    get() = when (this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }

fun Char.isHyphenOrUnderscore(): Boolean = this in "_-"

fun Char.isEnglishLetter(): Boolean = this in 'a'..'z' || this in 'A'..'Z'

fun StringResource.orNull(): StringResource? = if(this is StringResource.Empty) null else this

fun TextView.setVisibleText(@StringRes resId: Int?) {
    val text = resId ?: R.string.unknown
    setText(text)
}

fun TextView.setVisibleText(text: String?) {
    text?.let(this::setText) ?: setText(R.string.unknown)
}

fun Long.toDateString(): String {
    val format = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
    return format.format(Date(this))
}

fun EditText.addListener(listener: (String) -> Unit) {
    addTextChangedListener {
        it?.toString()?.let(listener::invoke)
    }
}

fun String.extension() =
    substring(lastIndexOf(".") + 1);