package com.xiong.appbase.extension

import android.os.Handler
import android.os.Looper

/**
 * Created by iiMedia on 2018/5/22.
 */
fun postDelayedToUI(runnable: () -> Unit, delay: Long) {
    val mainHandler = Handler(Looper.getMainLooper())
    mainHandler.postDelayed(runnable, delay)
}

fun postToUI(runnable: () -> Unit) {
    val mainHandler = Handler(Looper.getMainLooper())
    mainHandler.post(runnable)
}