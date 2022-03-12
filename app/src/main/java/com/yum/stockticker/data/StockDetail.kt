package com.yum.stockticker.data

data class StockDetail(
    val stockTicker: StockTicker,
    val allTimeHigh: String,
    val address: String,
    val imageUrl: String,
    val website: String
)
