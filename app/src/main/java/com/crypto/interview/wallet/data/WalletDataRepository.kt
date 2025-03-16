import com.crypto.interview.wallet.data.db.dao.CoinBalanceDao
import com.crypto.interview.wallet.data.db.dao.CoinDao
import com.crypto.interview.wallet.data.db.dao.CurrencyRateDao
import com.crypto.interview.wallet.data.network.CryptoDataSource
import com.crypto.interview.wallet.data.realtime.RealtimeRateTransport
import com.crypto.interview.wallet.data.realtime.RealtimeBalanceTransport

class WalletDataRepository(
    private val cryptoDataSource: CryptoDataSource,
    private val coinDao: CoinDao,
    private val coinBalanceDao: CoinBalanceDao,
    private val currencyRateDao: CurrencyRateDao,
    private val realtimeRateTransport: RealtimeRateTransport,
    private val realtimeBalanceTransport: RealtimeBalanceTransport
) {

    suspend fun fetchCurrencies(): List<CurrencyInfo> {
        val currencies = cryptoDataSource.fetchCurrencies();
        val coinEntities = currencies.map { it.toCoinEntry() }
        coinDao.insertCoins(coinEntities)

        return currencies;
    }

    suspend fun fetchWalletBalances(walletId: String): List<WalletBalance> {
        val walletBalances = cryptoDataSource.fetchWalletBalances();
        val coinBalanceEntities = walletBalances.map { it.toCoinBalanceEntry(walletId) }
        coinBalanceDao.insertCoinBalances(coinBalanceEntities)
        realtimeBalanceTransport.dispatchWalletBalances(coinBalanceEntities)

        return walletBalances;
    }

    suspend fun fetchLiveRates(): List<CurrencyTier> {
        val liveRates = cryptoDataSource.fetchLiveRates()
        val currencyRateEntities = liveRates.map { it.toCurrencyRateEntry() }
        currencyRateDao.insertCurrencyRates(currencyRateEntities)
        realtimeRateTransport.dispatchRates(currencyRateEntities)
        return liveRates
    }
}