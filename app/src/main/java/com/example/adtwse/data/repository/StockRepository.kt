package com.example.adtwse.data.repository

import com.example.adtwse.network.RetrofitClient
import com.example.adtwse.network.StockDayAllResponse
import com.example.adtwse.network.StockDayAvgResponse
import com.example.adtwse.network.StockEvaluationResponse

class StockRepository {

    suspend fun fetchStockEvaluations(): List<StockEvaluationResponse> {
        return RetrofitClient.instance.getStockEvaluation()
    }

    suspend fun fetchStockDayAvgAlls() : List<StockDayAvgResponse> {
        return RetrofitClient.instance.getStockDayAvgAll()
    }

    suspend fun fetchStockDayAlls() : List<StockDayAllResponse> {
        return RetrofitClient.instance.getStockDayAll()
    }
}