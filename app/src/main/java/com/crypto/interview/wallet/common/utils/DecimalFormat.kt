package com.crypto.interview.wallet.common.utils

import java.text.DecimalFormat



/**
 * Double 的扩展函数，用于格式化数字并可选添加符号
 * @param decimals 保留的小数位数
 * @param symbol 可选的符号（单位）
 * @return 格式化后的字符串
 */
fun Double.formatWithDecimals(decimals: Int, symbol: String? = null): String {
    if (this == 0.0) {
        return symbol?.let { "0 $it" } ?: "0"
    }
    
    val pattern = buildString {
        append("#,##0")
        if (decimals > 0) {
            append(".")
            repeat(decimals) { append("#") }
        }
    }
    
    val formatter = DecimalFormat(pattern)
    val formatted = formatter.format(this)
    
    return symbol?.let { "$formatted $it" } ?: formatted
}