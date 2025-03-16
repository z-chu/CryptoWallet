package com.crypto.interview.wallet.data.realtime.model

data class BalanceRequest(
    val walletId: String,
    val coinId: String,
)
