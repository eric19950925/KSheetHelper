package com.eric.ksheethelper.Ext

import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import com.eric.ksheethelper.BuildConfig.DEBUG
import com.jakewharton.rxbinding3.view.clicks
import java.util.concurrent.TimeUnit

fun View.listenClick(onClick: (View) -> Unit) {
    clicks()
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe({
            performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)

            onClick(this)
        }, {
            it.log()
        })
}

private fun Throwable.log() {
    if (!DEBUG){
        return
    }
    val customTag = "0210"
    with(Throwable().stackTrace[1]) {
        Log.e(customTag, "====================================================================================================")
        Log.e(customTag, "⎜From    : $methodName ($fileName:$lineNumber)")
        Log.e(customTag, if (message != null) {
            "⎜Message : $message"
        } else {
            "⎜Action  : $methodName"
        })
        Log.e(customTag, "====================================================================================================")
    }
}
