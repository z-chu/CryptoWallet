package com.crypto.interview.wallet.data.model

import com.crypto.interview.wallet.data.db.entry.CoinEntry
import com.crypto.interview.wallet.data.realtime.model.BalanceRate

data class CoinAsset(
  val coinEntry: CoinEntry,
  val balanceRate: BalanceRate
)