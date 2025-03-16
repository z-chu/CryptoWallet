package com.crypto.interview.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.crypto.interview.wallet.data.db.entry.CoinEntry

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoin(coinEntry: CoinEntry)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoins(coinEntries: List<CoinEntry>)

    @Update
    suspend fun updateCoin(coinEntry: CoinEntry)


    @Query("SELECT * FROM coin WHERE id = :id")
    suspend fun getCoinById(id: String): CoinEntry?

    @Query("DELETE FROM coin")
    suspend fun deleteAllCoins()


}