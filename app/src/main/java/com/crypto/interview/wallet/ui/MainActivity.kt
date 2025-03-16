package com.crypto.interview.wallet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crypto.interview.wallet.R
import com.crypto.interview.wallet.common.fragment.FragmentTabController
import com.crypto.interview.wallet.common.fragment.setupWithFragmentTabController
import com.crypto.interview.wallet.ui.defi.DefiFragment
import com.crypto.interview.wallet.ui.wallet.WalletFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentTabController: FragmentTabController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView =findViewById(R.id.nav_view)
        fragmentTabController = navView.setupWithFragmentTabController(
            R.id.fragment_container_view,
            supportFragmentManager,
            savedInstanceState,
            R.id.navigation_wallet,
        ) {
            when (it) {
                R.id.navigation_wallet -> {
                    WalletFragment()
                }
                R.id.navigation_defi -> {
                    DefiFragment()
                }
                else -> error("")
            }
        }
    }



}