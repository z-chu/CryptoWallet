package com.crypto.interview.wallet.data.realtime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crypto.interview.wallet.data.db.dao.CoinBalanceDao
import com.crypto.interview.wallet.data.db.entry.CoinBalanceEntry
import com.crypto.interview.wallet.data.realtime.model.BalanceRequest
import java.util.HashMap
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RealtimeBalanceTransport(private val balanceDao: CoinBalanceDao) {

    private val balanceSubjects = HashMap<BalanceRequest, MutableLiveData<CoinBalanceEntry>>()

    fun dispatchWalletBalances(coinBalances: List<CoinBalanceEntry>) {
        val balancesMap =
                coinBalances
                        .associateBy { coinBalance ->
                            BalanceRequest(
                                    walletId = coinBalance.walletId,
                                    coinId = coinBalance.coinId
                            )
                        }
                        .toMutableMap()

        for (entry in balanceSubjects.entries) {
            if (balancesMap.isEmpty()) {
                break
            }

            val subject = entry.value
            val currentBalance = subject.value
            val newBalance = balancesMap.remove(entry.key)

            if (newBalance != null) {
                if (isNewer(newBalance, currentBalance)) {
                    subject.postValue(newBalance)
                }
            }
        }

        if (balancesMap.isNotEmpty()) {
            balancesMap.forEach { (key, value) ->
                balanceSubjects.getOrPut(key) { MutableLiveData() }.postValue(value)
            }
        }
    }

    fun subscribeBalance(request: BalanceRequest): LiveData<CoinBalanceEntry> {
        return balanceSubjects.getOrPut(request) {
            MutableLiveData<CoinBalanceEntry>().apply { loadBalance(request) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadBalance(request: BalanceRequest) {
        GlobalScope.launch {
            val coinBalance = balanceDao.getCoinBalance(request.walletId, request.coinId)
            if (coinBalance != null) {
                balanceSubjects[request]?.value = coinBalance
            } else {
                balanceSubjects[request]?.value = createDefaultBalance(request)
            }
        }
    }

    private fun createDefaultBalance(request: BalanceRequest): CoinBalanceEntry {
        return CoinBalanceEntry(
                walletId = request.walletId,
                coinId = request.coinId,
                balance = 0.0,
                updateTime = System.currentTimeMillis()
        )
    }

    private fun isNewer(newBalance: CoinBalanceEntry, currentBalance: CoinBalanceEntry?): Boolean {
        if (currentBalance == null) return true
        return newBalance.updateTime > currentBalance.updateTime
    }

    fun cleanUnusedSubjects() {
        val iterator = balanceSubjects.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (!entry.value.hasActiveObservers()) {
                iterator.remove()
            }
        }
    }

    fun cleanAllSubjects() {
        balanceSubjects.clear()
    }
}
