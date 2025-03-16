package com.crypto.interview.wallet.ui.wallet

import CoinAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.crypto.interview.wallet.R
import com.crypto.interview.wallet.common.base.BaseFragment
import com.crypto.interview.wallet.databinding.FragmentWalletBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : BaseFragment(){

    override val layoutId: Int
        get() = R.layout.fragment_wallet

    private val viewModel:WalletViewModel by viewModel()
    private lateinit var binding: FragmentWalletBinding
    private val coinAdapter = CoinAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWalletBinding.bind(view)
        
        setupRecyclerView()
        setupObservers()
        
        viewModel.updateCoins()
        viewModel.fetchBalance()
    }

    private fun setupRecyclerView() {
        binding.rvCoins.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinAdapter
        }
    }

    private fun setupObservers() {
        viewModel.liveCoinAsset.observe(viewLifecycleOwner) { coins ->
            coinAdapter.updateCoins(coins)
            Log.d("TAG", "setupObservers: ")
        }
        
        viewModel.totalAssetValue.observe(viewLifecycleOwner) { total ->
            binding.tvTotalAsset.text = String.format("%.2f USD", total)
            Log.d("TAG", "setupObservers: ")
        }
    }

}