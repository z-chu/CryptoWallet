package com.crypto.interview.wallet.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crypto.interview.wallet.data.db.dao.CoinDao
import com.crypto.interview.wallet.data.db.entry.CoinEntry
import com.crypto.interview.wallet.data.model.CoinAsset
import com.crypto.interview.wallet.data.realtime.model.BalanceRate
import kotlinx.coroutines.launch
import com.crypto.interview.wallet.common.utils.Removable
import com.crypto.interview.wallet.common.utils.watchForever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CoinAssetProvider(
    private val balanceRates: LiveData<List<BalanceRate>>,
    private val coinDao: CoinDao,
)  {

    private val _coinAssets: MutableLiveData<List<CoinAsset>> = MutableLiveData()
    val coinAssets: LiveData<List<CoinAsset>>
        get() = _coinAssets
    private var balanceRatesRemovable: Removable? = null

    private val coinCache = mutableMapOf<String, CoinEntry>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    

    init {
        balanceRatesRemovable =
            balanceRates.watchForever { balanceRates -> onBalanceRatesChanged(balanceRates) }
    }

    private fun onBalanceRatesChanged(balanceRates: List<BalanceRate>) {
        scope.launch {
            val coinAssetsList = mutableListOf<CoinAsset>()

            for (balanceRate in balanceRates) {
                val cacheKey = balanceRate.coinBalance.coinId

                var coin = coinCache[cacheKey]
                if (coin == null) {
                    coin = coinDao.getCoinById(balanceRate.coinBalance.coinId)
                    if (coin != null) {
                        coinCache[cacheKey] = coin
                    }
                }
                coin?.let {
                    coinAssetsList.add(CoinAsset(it, balanceRate))
                }
            }

            // 按价值降序排列并更新 LiveData
            _coinAssets.value = coinAssetsList.sortedByDescending {
                it.balanceRate.currencyValue
            }
        }
    }

     fun cleanup() {
        balanceRatesRemovable?.removeObserver()
         job.cancel()
    }
}
