package com.eric.ksheethelper.Base

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

//        Logger.isLoggerEnable = DEBUG

//        TimezoneUtils.table = getGen1TimezoneTable(this)
//
//        TimezoneUtils.gen2TimezoneTable = getGen2TimezoneTable(this)

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(
                viewModelModule,
                coordinator
//                apiModule
            ))
        }
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}