package com.crypto.interview.wallet.data.realtime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crypto.interview.wallet.data.db.dao.CurrencyRateDao
import com.crypto.interview.wallet.data.db.entry.CurrencyRateEntry
import com.crypto.interview.wallet.data.realtime.model.RateRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.HashMap

class RealtimeRateTransport(private val rateDao: CurrencyRateDao) {

    private val rateSubjects = HashMap<RateRequest, MutableLiveData<CurrencyRateEntry>>()


    fun dispatchRates(rates: List<CurrencyRateEntry>) {
        val ratesMap = rates.associateBy { RateRequest(it.from, it.to) }.toMutableMap()

        // 更新现有的 subjects
        for (entry in rateSubjects.entries) {
            if (ratesMap.isEmpty()) {
                break
            }

            val subject = entry.value
            val currentRate = subject.value
            val newRate = ratesMap.remove(entry.key)

            if (newRate != null) {
                if (isNewer(newRate, currentRate)) {
                    subject.postValue(newRate)
                }
            }
        }
        // 添加新的汇率
        if (ratesMap.isNotEmpty()) {
            ratesMap.forEach { (key, value) ->
                rateSubjects.getOrPut(key) { MutableLiveData() }.postValue(value)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadRate(rateRequest: RateRequest) {
        // 从本地数据库加载汇率
        // 示例代码，需要根据实际情况调整
        GlobalScope.launch {
            val currencyRate = rateDao.getCurrencyRate(rateRequest.from, rateRequest.to)
            if (currencyRate != null) {
                rateSubjects[rateRequest]?.value = currencyRate;
            }
        }
    }

    fun subscribeRate(rateRequest: RateRequest): LiveData<CurrencyRateEntry> {
        val subject = rateSubjects.getOrPut(rateRequest) {
            MutableLiveData<CurrencyRateEntry>().apply {
                // 加载本地数据
                loadRate(rateRequest)
            }
        }
        return subject
    }


    private fun isNewer(newRate: CurrencyRateEntry, currentRate: CurrencyRateEntry?): Boolean {
        if (currentRate == null) return true
        return newRate.updateTime > currentRate.updateTime
    }

    fun cleanUnusedSubjects() {
        val iterator = rateSubjects.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (!entry.value.hasActiveObservers()) {
                iterator.remove()
            }
        }

    }

    fun cleanAllSubjects() {
        rateSubjects.clear()
    }
}