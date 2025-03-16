data class CurrencyInfo(
    val coin_id: String,
    val name: String,
    val symbol: String,
    val token_decimal: Int,
    val contract_address: String,
    val withdrawal_eta: List<String>,
    val colorful_image_url: String,
    val gray_image_url: String,
    val has_deposit_address_tag: Boolean,
    val min_balance: Int,
    val blockchain_symbol: String,
    val trading_symbol: String,
    val code: String,
    val explorer: String,
    val is_erc20: Boolean,
    val gas_limit: Int,
    val token_decimal_value: String,
    val display_decimal: Int,
    val supports_legacy_address: Boolean,
    val deposit_address_tag_name: String,
    val deposit_address_tag_type: String,
    val num_confirmation_required: Int
)

data class CurrenciesResponse(
    val currencies: List<CurrencyInfo>
)