package com.github.georgeh1998.simplesplit.util

import android.util.Log as AndroidLog

actual object Log {
    actual fun i(
        tag: String,
        message: String,
    ) {
        AndroidLog.i(tag, message)
    }
}
