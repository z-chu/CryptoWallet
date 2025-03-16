package com.crypto.interview.wallet.ui.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import com.crypto.interview.wallet.R
import com.crypto.interview.wallet.common.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : BaseFragment(){

    override val layoutId: Int
        get() = R.layout.fragment_wallet

    val viewModel:WalletViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateCoins()
        viewModel.fetchBalance()
        viewModel.liveCoinAsset.observe(viewLifecycleOwner){
            Log.d("WalletFragment", "liveCoinAsset: $it")
        }
        viewModel.totalAssetValue.observe(viewLifecycleOwner){
            Log.d("WalletFragment", "totalAssetValue: $it")

        }

    }



}