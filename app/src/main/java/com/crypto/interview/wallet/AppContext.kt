package com.crypto.interview.wallet

import android.app.Application
import android.content.Context
import com.crypto.interview.wallet.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AppContext : Application() {


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppContext)
            modules(
                appModule,
            )
        }

    }


    companion object {
        lateinit var context: AppContext
            private set
    }


}

