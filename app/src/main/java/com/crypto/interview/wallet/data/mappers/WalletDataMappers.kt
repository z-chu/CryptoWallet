import com.crypto.interview.wallet.data.db.entry.CoinEntry
import com.crypto.interview.wallet.data.db.entry.CoinBalanceEntry
import com.crypto.interview.wallet.data.db.entry.CurrencyRateEntry

fun CurrencyInfo.toCoinEntry(): CoinEntry {
    return CoinEntry(
        id = coin_id,
        name = name,
        symbol = symbol,
        imageUrl = colorful_image_url,
    )
}

fun WalletBalance.toCoinBalanceEntry(walletId: String): CoinBalanceEntry {
    return CoinBalanceEntry(
        walletId = walletId,
        coinId = currency,
        balance = amount,
        updateTime = System.currentTimeMillis()
    )
}

fun CurrencyTier.toCurrencyRateEntry(): CurrencyRateEntry {
    return CurrencyRateEntry(
        from = from_currency,
        to = to_currency,
        rate = rates.first().rate.toDouble(),
        updateTime = System.currentTimeMillis()
    )
}