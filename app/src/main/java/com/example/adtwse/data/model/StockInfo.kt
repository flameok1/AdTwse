package com.example.adtwse.data.model

data class StockInfo(
    val code: String,
    val name: String,

    val openingPrice: Double,
    val closingPrice: Double,

    val highestPrice: Double,
    val lowestPrice: Double,

    val monthlyAverage: Double,
    val change: Double,

    val tradeNum: Long,      // 成交筆數
    val tradeVolume: Long,   // 成交股數
    val tradeValue: Long,    // 成交金額

    val peRatio: Double,       // 本益比
    val dividendYield: Double, // 殖利率
    val pbRatio: Double        // 股價淨值比
)
