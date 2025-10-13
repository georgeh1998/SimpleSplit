package com.github.georgeh1998.simplesplit

import android.app.Application
import com.github.georgeh1998.simplesplit.di.initKoin

class SSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
