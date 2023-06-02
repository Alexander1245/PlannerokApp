package com.dart69.plannerokapp.core

import com.dart69.mvvm.strings.StringResource
import com.faltenreich.skeletonlayout.SkeletonLayout
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