import android.content.Context
import com.crypto.interview.wallet.data.network.CryptoService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CryptoServiceMockImpl(private val context: Context) : CryptoService {
    private val gson=Gson()

    override suspend fun fetchCurrencies(): List<CurrencyInfo> =
            withContext(Dispatchers.IO) {
                val json =
                        context.assets.open("currencies.json").bufferedReader().use {
                            it.readText()
                        }
                val currencies =
                    gson.fromJson<CurrenciesResponse>(
                                        json,
                                        object : TypeToken<CurrenciesResponse>() {}.type
                                )
                currencies.currencies
            }

    override suspend fun fetchWalletBalances(): List<WalletBalance> =
            withContext(Dispatchers.IO) {
                val json =
                        context.assets.open("wallet-balance.json").bufferedReader().use {
                            it.readText()
                        }
                val walletBalances =
                    gson.fromJson<WalletBalanceResponse>(
                                        json,
                                        object : TypeToken<WalletBalanceResponse>() {}.type
                                )
                walletBalances.wallet
            }

    override suspend fun fetchLiveRates(): List<CurrencyTier> =
            withContext(Dispatchers.IO) {
                val json =
                        context.assets.open("live-rates.json").bufferedReader().use {
                            it.readText()
                        }
                val liveRates =
                    gson.fromJson<LiveRatesResponse>(
                                        json,
                                        object : TypeToken<LiveRatesResponse>() {}.type
                                )
                liveRates.tiers
            }
}
