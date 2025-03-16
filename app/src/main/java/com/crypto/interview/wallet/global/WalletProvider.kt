package com.crypto.interview.wallet.global

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class WalletProvider  {

    // TODO: get from database , now just for test
    private val _currentWalletId = MutableLiveData("wallet_1")

    val currentWalletId: LiveData<String> get() = _currentWalletId
}