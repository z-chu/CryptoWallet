package com.crypto.interview.wallet.data.network

import CurrencyInfo
import CurrencyTier
import WalletBalance

class CryptoDataSource(private val cryptoService: CryptoService) {

    suspend fun fetchCurrencies(): List<CurrencyInfo> = cryptoService.fetchCurrencies()

    suspend fun fetchWalletBalances(): List<WalletBalance> = cryptoService.fetchWalletBalances()

    suspend fun fetchLiveRates(): List<CurrencyTier> = cryptoService.fetchLiveRates()
}
