package com.dart69.plannerokapp.core.presentation

import com.faltenreich.skeletonlayout.SkeletonLayout

var SkeletonLayout.isSkeletonVisible: Boolean
    get() = isShown
    set(isVisible) {
        if (isVisible) showSkeleton() else showOriginal()
    }