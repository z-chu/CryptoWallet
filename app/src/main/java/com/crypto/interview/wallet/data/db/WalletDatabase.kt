package com.crypto.interview.wallet.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crypto.interview.wallet.data.db.dao.CoinBalanceDao
import com.crypto.interview.wallet.data.db.dao.CurrencyRateDao
import com.crypto.interview.wallet.data.db.dao.CoinDao
import com.crypto.interview.wallet.data.db.entry.CoinBalanceEntry
import com.crypto.interview.wallet.data.db.entry.CoinEntry
import com.crypto.interview.wallet.data.db.entry.CurrencyRateEntry

@Database(
    entities = [
        CoinEntry::class,
        CoinBalanceEntry::class,
        CurrencyRateEntry::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters
abstract class WalletDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    abstract fun coinBalanceDao(): CoinBalanceDao

    abstract fun currencyRateDao(): CurrencyRateDao
}