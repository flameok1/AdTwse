package com.example.adtwse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adtwse.data.model.StockInfo
import com.example.adtwse.data.repository.StockRepository
import com.example.adtwse.network.StockEvaluationResponse
import com.example.adtwse.utils.safeToDouble
import com.example.adtwse.utils.safeToLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _stockList = MutableLiveData<List<StockInfo>>()
    val stockList: LiveData<List<StockInfo>> = _stockList

    private val _stocks = MutableLiveData<List<StockEvaluationResponse>>()
    val stocks: LiveData<List<StockEvaluationResponse>> = _stocks
    private val repository = StockRepository()

    private var isUpdating = false

    init {

    }

    fun loadStocks() {
        if (isUpdating) return

        isUpdating = true

        viewModelScope.launch {
            try {
                val evalList = repository.fetchStockEvaluations()
                val avgList = repository.fetchStockDayAvgAlls()
                val dayList = repository.fetchStockDayAlls()

                val masterMap = mutableMapOf<String, StockInfo>()

                // 處理基本價格與交易量 (STOCK_DAY_ALL)
                dayList.forEach { item ->
                    masterMap[item.Code] = StockInfo(
                        code = item.Code,
                        name = item.Name,
                        openingPrice = item.OpeningPrice.safeToDouble(),
                        highestPrice = item.HighestPrice.safeToDouble(),
                        lowestPrice = item.LowestPrice.safeToDouble(),
                        tradeVolume = item.TradeVolume.safeToLong(),
                        tradeValue = item.TradeValue.safeToLong(),
                        tradeNum = item.Transaction.safeToLong(),
                        change = item.Change.safeToDouble(),
                        closingPrice = 0.0,
                        monthlyAverage = 0.0,
                        peRatio = 0.0,
                        dividendYield = 0.0,
                        pbRatio = 0.0
                    )
                }

                avgList.forEach { item ->
                    masterMap[item.Code]?.let { existing ->
                        masterMap[item.Code] = existing.copy(
                            closingPrice = item.ClosingPrice.safeToDouble(),
                            monthlyAverage = item.MonthlyAveragePrice.safeToDouble()
                        )
                    }
                }

                evalList.forEach { item ->
                    masterMap[item.Code]?.let { existing ->

                        masterMap[item.Code] = existing.copy(
                            peRatio = item.PEratio.safeToDouble(),
                            dividendYield = item.DividendYield.safeToDouble(),
                            pbRatio = item.PBratio.safeToDouble()
                        )
                    }
                }

                withContext(Dispatchers.Main) {
                    _stockList.value = masterMap.values.toList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                isUpdating = false
            }

        }
    }

    private fun loadMockData() {
        val mock = listOf(
            StockInfo(
                code = "2330",
                name = "台積電",
                closingPrice = 600.00,
                monthlyAverage = 590.00,
                change = 10.00,
                openingPrice = 595.0,
                highestPrice = 605.0,
                lowestPrice = 590.0,
                tradeNum = 10,
                tradeVolume = 25000000,
                tradeValue = 100,
                peRatio = 28.5,
                dividendYield = 2.1,
                pbRatio = 5.2
            ),
            StockInfo(
                code = "2317",
                name = "鴻海",
                closingPrice = 100.00,
                monthlyAverage = 105.00,
                change = -2.00,
                openingPrice = 102.00,
                highestPrice = 103.00,
                lowestPrice = 99.00,
                tradeNum = 10,
                tradeVolume = 15000000,
                tradeValue = 100,
                peRatio = 10.2,
                dividendYield = 4.5,
                pbRatio = 1.1
            )
        )
        _stockList.value = mock
    }

    fun sortStocks(descending: Boolean) {
        val currentList = _stockList.value ?: return
        _stockList.value = if (descending) {
            currentList.sortedByDescending { it.code }
        } else {
            currentList.sortedBy { it.code }
        }
    }
}