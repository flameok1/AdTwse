package com.example.adtwse.network

import retrofit2.http.GET

interface TwseApiService {
    @GET("v1/exchangeReport/BWIBBU_ALL")
    suspend fun getStockEvaluation(): List<StockEvaluationResponse>

    @GET("v1/exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgAll(): List<StockDayAvgResponse>

    @GET("v1/exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): List<StockDayAllResponse>
}

// 對應 BWIBBU_ALL
data class StockEvaluationResponse(
    val Code: String,         // 證券代號
    val Name: String,         // 證券名稱
    val PEratio: String,      // 本益比
    val DividendYield: String,// 殖利率(%)
    val PBratio: String       // 股價淨值比
)

// 對應 STOCK_DAY_AVG_ALL
data class StockDayAvgResponse(
    val Code: String,
    val Name: String,
    val ClosingPrice: String,  // 收盤價
    val MonthlyAveragePrice: String // 月平均價
)

// 對應 STOCK_DAY_ALL
data class StockDayAllResponse(
    val Code: String,
    val Name: String,
    val OpeningPrice: String, // 開盤價
    val HighestPrice: String, // 最高價
    val LowestPrice: String,  // 最低價
    val TradeVolume: String,  // 成交股數
    val TradeValue: String,   // 成交金額
    val Transaction: String,  // 成交筆數
    val Change: String        // 漲跌價差
)