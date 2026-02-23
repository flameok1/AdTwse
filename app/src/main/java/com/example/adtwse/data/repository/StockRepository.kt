package com.example.adtwse.data.repository

import com.example.adtwse.network.RetrofitClient
import com.example.adtwse.network.StockEvaluationResponse

class StockRepository {

    suspend fun fetchStocks(): List<StockEvaluationResponse> {
        return RetrofitClient.instance.getStockEvaluation()
    }


}