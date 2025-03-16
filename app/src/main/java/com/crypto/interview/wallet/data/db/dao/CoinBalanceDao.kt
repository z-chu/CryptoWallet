package com.crypto.interview.wallet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crypto.interview.wallet.data.db.entry.CoinBalanceEntry

@Dao
interface CoinBalanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertCoinBalances(coinBalanceEntries: List<CoinBalanceEntry>)

    @Query("SELECT * FROM coin_balance WHERE walletId = :walletId AND coinId = :coinId")
     suspend fun getCoinBalance(walletId: String, coinId: String): CoinBalanceEntry?

    @Query("SELECT * FROM coin_balance WHERE walletId = :walletId")
      fun liveCoinBalances(walletId: String): LiveData<List<CoinBalanceEntry>>

}