package com.dart69.plannerokapp.core

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.dart69.mvvm.strings.StringResource
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
    isVisible = resId != null
    resId?.let(this::setText)
}

fun TextView.setVisibleText(text: String?) {
    isVisible = text != null
    text?.let(this::setText)
}

fun Long.toDateString(): String {
    val format = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
    return format.format(Date(this))
}