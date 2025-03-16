data class LiveRatesResponse(
    val ok: Boolean,
    val warning: String,
    val tiers: List<CurrencyTier>
)

data class CurrencyTier(
    val from_currency: String,
    val to_currency: String,
    val rates: List<RateInfo>,
    val time_stamp: Long
)

data class RateInfo(
    val amount: String,
    val rate: String
)