package com.crypto.interview.wallet.ui.wallet

import WalletDataRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.interview.wallet.data.model.CoinAsset
import com.crypto.interview.wallet.global.CoinAssetProvider
import com.crypto.interview.wallet.global.CoinBalanceProvider
import com.crypto.interview.wallet.global.WalletProvider
import kotlinx.coroutines.launch

class WalletViewModel(
    private val repository: WalletDataRepository,
    private val walletProvider: WalletProvider,
    private val coinAssetProvider: CoinAssetProvider,
    private val coinBalanceProvider: CoinBalanceProvider
) : ViewModel() {

    val liveCoinAsset: LiveData<List<CoinAsset>>
        get() = coinAssetProvider.coinAssets

    val totalAssetValue: LiveData<Double>
        get() = coinBalanceProvider.totalAssetValue

    fun updateCoins() {
        viewModelScope.launch {
            try {
                repository.fetchCurrencies()
            } catch (e: Exception) {
                Log.e("WalletViewModel", "Error fetching currencies", e)
            }
        }
    }


    fun fetchBalance() {
        viewModelScope.launch {
            val walletId = walletProvider.currentWalletId.value
            if (walletId != null) {
                try {
                    repository.fetchWalletBalances(walletId)
                    repository.fetchLiveRates()
                } catch (e: Exception) {
                    Log.e("WalletViewModel", "Error fetching currencies", e)
                }
            }
        }
    }
}