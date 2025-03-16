package com.crypto.interview.wallet.di

import CryptoServiceMockImpl
import WalletDataRepository
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.crypto.interview.wallet.data.AppPreferences
import com.crypto.interview.wallet.data.db.WalletDatabase
import com.crypto.interview.wallet.data.network.CryptoDataSource
import com.crypto.interview.wallet.data.network.CryptoService
import com.crypto.interview.wallet.data.realtime.RealtimeBalanceTransport
import com.crypto.interview.wallet.data.realtime.RealtimeRateTransport
import com.crypto.interview.wallet.global.CoinAssetProvider
import com.crypto.interview.wallet.global.CoinBalanceProvider
import com.crypto.interview.wallet.global.WalletProvider
import com.crypto.interview.wallet.ui.wallet.WalletViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        AppPreferences(androidContext())
    }

    single<CryptoService> {
        CryptoServiceMockImpl(androidContext())
    }

    single {
        CryptoDataSource(get())
    }


    single {
        Room
            .databaseBuilder(androidContext(), WalletDatabase::class.java, "WalletDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        RealtimeRateTransport(get<WalletDatabase>().currencyRateDao())
    }

    single {
        RealtimeBalanceTransport(get<WalletDatabase>().coinBalanceDao())
    }

    single {
        WalletDataRepository(
            get(),
            get<WalletDatabase>().coinDao(),
            get<WalletDatabase>().coinBalanceDao(),
            get<WalletDatabase>().currencyRateDao(),
            get(),
            get()
        )
    }

    single{
        WalletProvider()
    }

    single {
        CoinBalanceProvider(
            get<WalletProvider>().currentWalletId,
            get<WalletDatabase>().coinBalanceDao(),
            get(),
            MutableLiveData("USD")
        )
    }

    single {
        CoinAssetProvider(
            get<CoinBalanceProvider>().balanceRates,
            get<WalletDatabase>().coinDao(),
        )
    }

    viewModel {
        WalletViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }


}