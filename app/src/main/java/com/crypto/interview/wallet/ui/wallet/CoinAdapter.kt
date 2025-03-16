import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crypto.interview.wallet.common.utils.formatPrice
import com.crypto.interview.wallet.data.model.CoinAsset
import com.crypto.interview.wallet.databinding.ItemCoinAssetBinding
import com.crypto.interview.wallet.common.utils.formatWithDecimals

class CoinAdapter : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
    private var coins = listOf<CoinAsset>()

    class CoinViewHolder(val binding: ItemCoinAssetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinAssetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CoinViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]
        holder.binding.apply {
            tvCoinName.text = coin.coinEntry.name
            tvCoinAmount.text = coin.balanceRate.coinBalance.balance.formatWithDecimals(
                coin.coinEntry.decimal,
                symbol = coin.coinEntry.symbol
            )
            val rate = coin.balanceRate.rate?.rate
            if (rate != null) {
                tvUnitPrice.text = ("\$" + rate.formatPrice())
                tvCoinValue.text = "\$" + coin.balanceRate.currencyValue.formatPrice()
            } else {
                tvUnitPrice.text = "--"
                tvCoinValue.text = "--"
            }

            Glide.with(ivCoinIcon)
                .load(coin.coinEntry.imageUrl)
                .circleCrop()  // 可选：使图片呈现圆形
                .into(ivCoinIcon)
        }
    }

    override fun getItemCount() = coins.size

    fun updateCoins(newCoins: List<CoinAsset>) {
        coins = newCoins
        notifyDataSetChanged()
    }
} 