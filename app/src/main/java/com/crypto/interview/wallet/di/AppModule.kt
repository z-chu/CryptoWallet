package com.crypto.interview.wallet.di

import com.crypto.interview.wallet.data.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        AppPreferences(androidContext())
    }
}