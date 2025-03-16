package com.crypto.interview.wallet.data.db.entry

import androidx.room.Entity

@Entity(tableName = "coin_balance", primaryKeys = ["walletId", "coinId"])
data class CoinBalanceEntry(
        val walletId: String,
        val coinId: String,
        val balance: Double,
        val updateTime: Long
)
