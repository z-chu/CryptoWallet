package com.crypto.interview.wallet.data.db.entry

import androidx.room.Entity;

@Entity(tableName = "currency_rate", primaryKeys = ["from", "to"])
data class CurrencyRateEntry(
    val from: String,
    val to: String,
    val rate: Double,
    val updateTime: Long
){
    val uniqueId: String
        get() = "$from-$to"
}