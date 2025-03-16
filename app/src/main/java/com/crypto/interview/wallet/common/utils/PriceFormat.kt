package com.crypto.interview.wallet.common.utils

import java.text.DecimalFormat
import kotlin.math.abs

private val formatter = DecimalFormat().apply {
    maximumFractionDigits = 4
    minimumFractionDigits = 0
}

private val fillZerosFormatter = DecimalFormat().apply {
    maximumFractionDigits = 4
    minimumFractionDigits = 4
}

private val smallNumChars = arrayOf("₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉")
/**
 * Double 类型的价格格式化扩展函数
 */
fun Double.formatPrice(fillZeros: Boolean = false): String {


    if (this == 0.0) return "0"
    if (this == 1.0) return "1"
    
    if (abs(this) >= 1.0) {
        return if (fillZeros) {
            fillZerosFormatter.format(this)
        } else {
            formatter.format(this)
        }
    }

    // 转换为非科学计数法的字符串
    val nonScientificString = String.format("%.20f", this).trimEnd('0')
    val decimalIndex = nonScientificString.indexOf('.')
    
    if (decimalIndex <= 0) {
        return if (fillZeros) {
            fillZerosFormatter.format(this)
        } else {
            formatter.format(this)
        }
    }

    // 获取小数部分
    val decimalPart = nonScientificString.substring(decimalIndex + 1)
    // 找到第一个非0数字的位置
    val indexOfFirstNonZero = decimalPart.indexOfFirst { it != '0' }
    
    if (indexOfFirstNonZero < 4) {
        // 如果小数点后面的0不足4个，使用常规格式化
        val decimalFormat = DecimalFormat().apply {
            maximumFractionDigits = indexOfFirstNonZero + 4
            minimumFractionDigits = if (fillZeros) indexOfFirstNonZero + 4 else 0
        }
        return decimalFormat.format(this)
    } else {
        // 如果小数点后面的0超过4个，使用特殊格式：0.0ₙ{后4位}
        val endIndex = (indexOfFirstNonZero + 4).coerceAtMost(decimalPart.length)
        var truncatedDecimal = decimalPart
            .substring(indexOfFirstNonZero, endIndex)
            .trimEnd('0')
            
        // 如果需要补0且截取的数字不足4位
        if (fillZeros && truncatedDecimal.length < 4) {
            truncatedDecimal = truncatedDecimal.padEnd(4, '0')
        }

        // 将0的个数转换为下标数字
        val subscriptZeros = indexOfFirstNonZero.toString()
            .map { digit -> smallNumChars[digit.digitToInt()] }
            .joinToString("")

        return "0.0$subscriptZeros$truncatedDecimal"
    }
}

