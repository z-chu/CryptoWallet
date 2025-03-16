package com.crypto.interview.wallet.data.db.entry

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "coin"
)
data class CoinEntry(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val decimal: Int
)