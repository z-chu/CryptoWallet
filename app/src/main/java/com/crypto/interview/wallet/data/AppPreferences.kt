package com.crypto.interview.wallet.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppPreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    @Keep//不加这个数据，混淆时会被编译器优化，导致不被强引用持有
    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (K_CURRENCY_PRICE_UNIT == key) {
                (currencyPriceUnitLiveData as MutableLiveData<String>).value = currencyPriceUnit
            }
        }

    init {
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    var currencyPriceUnit: String
        set(value) = preferences.edit().putString(K_CURRENCY_PRICE_UNIT, value).apply()
        get() = preferences.getString(K_CURRENCY_PRICE_UNIT, "USD")!!


    val currencyPriceUnitLiveData: LiveData<String> by lazy {
        val mutableLiveData = MutableLiveData<String>()
        mutableLiveData.value = currencyPriceUnit
        return@lazy mutableLiveData
    }

    companion object {
        private const val K_CURRENCY_PRICE_UNIT = "currencyPriceUnit"
    }
}