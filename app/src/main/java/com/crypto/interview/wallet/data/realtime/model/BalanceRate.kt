package com.crypto.interview.wallet.data.realtime.model

import com.crypto.interview.wallet.data.db.entry.CoinBalanceEntry
import com.crypto.interview.wallet.data.db.entry.CurrencyRateEntry
data class BalanceRate(
    val coinBalance: CoinBalanceEntry,
    val rate: CurrencyRateEntry?
){
    val currencyValue: Double
        get() {
            if (coinBalance.balance == 0.0) {
                return 0.0
            }
            
            val rateLocal = rate
            if (rateLocal == null) {
                return 0.0
            }
            
            return coinBalance.balance * rateLocal.rate
        }
  
}