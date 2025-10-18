package com.github.georgeh1998.simplesplit.util

import platform.Foundation.NSLog

actual object Log {
    actual fun i(
        tag: String,
        message: String,
    ) {
        NSLog("[$tag] $message")
    }
}
