package com.example.highwaytransactions.ui.util

import androidx.compose.ui.Modifier
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val priceFormatter: DecimalFormat by lazy {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
    }
    DecimalFormat("#,###", symbols)
}

fun Int.toPriceString(): String = "${priceFormatter.format(this)} Ft"

fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier) =
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }