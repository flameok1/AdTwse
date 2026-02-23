package com.example.adtwse.network

import retrofit2.http.GET

interface TwseApiService {
    @GET("v1/exchangeReport/BWIBBU_ALL")
    suspend fun getStockEvaluation(): List<StockEvaluationResponse>
}


data class StockEvaluationResponse(
    val Code: String,         // 證券代號
    val Name: String,         // 證券名稱
    val PEratio: String,      // 本益比
    val DividendYield: String,// 殖利率(%)
    val PBratio: String       // 股價淨值比
)