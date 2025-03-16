package com.crypto.interview.wallet.data.network

import CurrenciesResponse
import CurrencyInfo
import WalletBalance
import CurrencyTier

interface CryptoService {

    suspend fun fetchCurrencies(): List<CurrencyInfo>

    suspend fun fetchWalletBalances(): List<WalletBalance>

    suspend fun fetchLiveRates(): List<CurrencyTier>

}
