package com.crypto.interview.wallet.global

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crypto.interview.wallet.data.db.dao.CoinBalanceDao
import com.crypto.interview.wallet.data.realtime.RealtimeRateTransport
import com.crypto.interview.wallet.data.realtime.model.BalanceRate
import com.crypto.interview.wallet.common.utils.watchForever
import com.crypto.interview.wallet.common.utils.Removable

class CoinBalanceProvider(
    private val walletId: LiveData<String>,
    private val balanceDao: CoinBalanceDao,
    private val realtimeRateTransport: RealtimeRateTransport,
    private val fitCurrency: LiveData<String>,
) {

    private val balancePriceManager = BalancePriceManager(
        balanceDao = balanceDao,
        realtimeRateTransport = realtimeRateTransport,
        fitCurrency = fitCurrency,
        onBalanceRatesChanged = { balanceRates ->
            onBalanceRatesChanged(balanceRates)
        }
    )
    private val _balanceRates: MutableLiveData<List<BalanceRate>> = MutableLiveData()

    val balanceRates: LiveData<List<BalanceRate>> get() = _balanceRates

    private var walletIdRemovable: Removable? = null
    private val _totalAssetValue: MutableLiveData<Double> = MutableLiveData()
    val totalAssetValue: LiveData<Double> get() = _totalAssetValue

    init {
        walletIdRemovable = walletId.watchForever { walletId ->
            onWalletChanged(walletId)
        }
    }

    private fun onWalletChanged(walletId: String) {
        balancePriceManager.startListening(walletId)
    }

    private fun onBalanceRatesChanged(balanceRates: List<BalanceRate>) {
        // 更新UI
        _balanceRates.value = balanceRates
        var totalValue = 0.0; //总资产价值
        for (balanceRate in balanceRates) {
            totalValue += balanceRate.currencyValue
        }
        _totalAssetValue.value = totalValue
    }


    fun cleanup() {
        balancePriceManager.cleanup()
        walletIdRemovable?.removeObserver()
    }

}