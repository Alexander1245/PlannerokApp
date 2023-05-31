package com.dart69.plannerokapp.presentation.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.faltenreich.skeletonlayout.SkeletonLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectOnStarted(lifecycleOwner: LifecycleOwner, collector: FlowCollector<T>) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect(collector)
        }
    }
}

var SkeletonLayout.isSkeletonVisible: Boolean
    get() = isShown
    set(isVisible) {
        if(isVisible) showSkeleton() else showOriginal()
    }