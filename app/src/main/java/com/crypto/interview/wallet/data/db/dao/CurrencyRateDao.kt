package com.crypto.interview.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.crypto.interview.wallet.data.db.entry.CurrencyRateEntry

@Dao
interface CurrencyRateDao {
  
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertCurrencyRates(currencyRateEntries: List<CurrencyRateEntry>)

    @Query("SELECT * FROM currency_rate WHERE `from` = :from AND `to` = :to")
     suspend fun getCurrencyRate(from: String, to: String): CurrencyRateEntry?


}