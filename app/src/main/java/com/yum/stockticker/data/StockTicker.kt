package com.yum.stockticker.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class StockTicker(
    val id: String = "APPL",
    val name: String = "Apple",
    val price: Double = 12.98,
    val companyType: List<String> = listOf("Tech")
)

fun String.toStockTickers(): ArrayList<StockTicker>? {
    runCatching {
        val type: Type = object : TypeToken<ArrayList<StockTicker?>?>() {}.type
        Gson().fromJson<ArrayList<StockTicker>>(this, type)
    }.onSuccess {
        return it?.let { tickers ->
            return tickers
        }
    }.onFailure {
        print(it)
    }
    println("Error Parsing Tickers list!")
    return null
}
