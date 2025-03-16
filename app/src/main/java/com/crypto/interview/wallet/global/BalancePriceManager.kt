package com.crypto.interview.wallet.global

import androidx.lifecycle.LiveData
import com.crypto.interview.wallet.data.db.dao.CoinBalanceDao
import com.crypto.interview.wallet.data.db.entry.CoinBalanceEntry
import com.crypto.interview.wallet.data.db.entry.CurrencyRateEntry
import com.crypto.interview.wallet.data.realtime.RealtimeRateTransport
import com.crypto.interview.wallet.data.realtime.model.BalanceRate
import com.crypto.interview.wallet.data.realtime.model.RateRequest
import kotlinx.coroutines.Job
import com.crypto.interview.wallet.common.utils.Removable
import com.crypto.interview.wallet.common.utils.watchForever

///1.监听用户的代币持仓变化
///2.为每个代币订阅实时价格更新
///3.使用批处理机制优化价格更新频率,发出 BalanceRate 变化
class BalancePriceManager(
    private val balanceDao: CoinBalanceDao,
    private val realtimeRateTransport: RealtimeRateTransport,
    private val fitCurrency: LiveData<String>,
    private val onBalanceRatesChanged: (List<BalanceRate>) -> Unit
) {
    private var balanceRates: List<BalanceRate> = ArrayList()

    private val rateSubscriptions = HashMap<RateRequest, Removable>()
    private var coinBalancesRemovable: Removable? = null

    private val pendingUpdates =
        HashMap<RateRequest, Pair<CoinBalanceEntry, CurrencyRateEntry>>()
    private var batchUpdateJob: Job? = null
    private val batchDelay = 160L // 约10帧的时间

    //  Map 来存储价格索引
    private val balanceIndexMap = HashMap<String, Int>()

    // 添加 Handler
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private val processBatchRunnable = Runnable { processBatchUpdates() }

    fun startListening(walletId: String) {
        cleanup()
        val liveCoinBalances = balanceDao.liveCoinBalances(walletId)
        // 观察余额变化
        coinBalancesRemovable =liveCoinBalances.watchForever { balances ->
            val fitCurrency = fitCurrency.value
            if(fitCurrency!=null) {
                handleBalancesUpdate(balances, fitCurrency)
            }
        }

        // 观察法币单位变化
        fitCurrency.observeForever { currency ->
            val coinBalances = liveCoinBalances.value
            if(coinBalances!=null){
                handleBalancesUpdate(coinBalances, currency)
            }
        }
    }

    private fun handleBalancesUpdate(balances: List<CoinBalanceEntry>, fitCurrency: String) {
        batchUpdateJob?.cancel()
        pendingUpdates.clear()
        balanceIndexMap.clear()

        if (balances.isEmpty()) {
            for (rateSubscription in rateSubscriptions) {
                rateSubscription.value.removeObserver();
            }
            rateSubscriptions.clear()
            balanceRates = listOf()
            onBalanceRatesChanged(balanceRates)
            return
        }
        val prevBalanceRates = balanceRates;
        val newRates = ArrayList<BalanceRate>()
        balances.forEachIndexed { index, balance ->
            balanceIndexMap[balance.coinId] = index
            newRates.add(BalanceRate(balance, null))
        }


        val currentRequests = HashSet<RateRequest>()
        balances.forEachIndexed { index, balance ->
            val request = RateRequest(balance.coinId, fitCurrency)
            currentRequests.add(request)

            if (rateSubscriptions.containsKey(request)) {
                val rate = prevBalanceRates.firstOrNull {
                    it.coinBalance.coinId == balance.coinId
                }
                newRates[index] = BalanceRate(balance, rate?.rate);
            } else {
                newRates[index] = BalanceRate(balance, null);
            }
            rateSubscriptions[request]?.removeObserver()
            rateSubscriptions[request] =
                realtimeRateTransport.subscribeRate(request).watchForever { rate ->
                    schedulePriceUpdate(balance, rate, request)
                }
        }



        balanceRates = newRates
        onBalanceRatesChanged(balanceRates)

        rateSubscriptions.keys
            .filter { it !in currentRequests }
            .forEach { request ->
                rateSubscriptions[request]?.removeObserver();
                rateSubscriptions.remove(request)
            }
    }

    private fun schedulePriceUpdate(
        balance: CoinBalanceEntry,
        rate: CurrencyRateEntry,
        request: RateRequest
    ) {
        pendingUpdates[request] = balance to rate
        handler.removeCallbacks(processBatchRunnable)
        handler.postDelayed(processBatchRunnable, batchDelay)
    }

    private fun processBatchUpdates() {
        if (pendingUpdates.isEmpty()) return

        var hasUpdates = false
        val currentRates = balanceRates
        var updatedRates: MutableList<BalanceRate>? = null

        for ((_, pair) in pendingUpdates) {
            val (balance, rate) = pair
            val index = balanceIndexMap[balance.coinId]
            
            if (index != null) {
                val currentRate = currentRates[index].rate
                if (currentRate != rate) {
                    if (updatedRates == null) {
                        updatedRates = ArrayList(currentRates)
                    }
                    updatedRates[index] = BalanceRate(balance, rate)
                    hasUpdates = true
                }
            }
        }

        if (hasUpdates) {
            balanceRates = updatedRates!!
            onBalanceRatesChanged(balanceRates)
        }

        pendingUpdates.clear()
    }

    private fun clearSubscriptions() {
        coinBalancesRemovable?.removeObserver()
        for (subscription in rateSubscriptions.values) {
            subscription.removeObserver()
        }
        rateSubscriptions.clear()
    }

    fun cleanup() {
        handler.removeCallbacks(processBatchRunnable)
        pendingUpdates.clear()
        clearSubscriptions()
    }
}
