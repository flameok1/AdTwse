package com.example.adtwse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adtwse.data.model.StockInfo
import com.example.adtwse.data.repository.StockRepository
import com.example.adtwse.network.StockEvaluationResponse
import com.example.adtwse.utils.safeToDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _stockList = MutableLiveData<List<StockInfo>>()
    val stockList: LiveData<List<StockInfo>> = _stockList

    private val _stocks = MutableLiveData<List<StockEvaluationResponse>>()
    val stocks: LiveData<List<StockEvaluationResponse>> = _stocks
    private val repository = StockRepository()

    init {

    }

    fun loadStocks() {
        viewModelScope.launch {
            try {
                val response = repository.fetchStocks()


                val currentMap = _stockList.value?.associateBy { it.code }?.toMutableMap() ?: mutableMapOf()

                response.forEach { res ->
                    val code = res.Code
                    val existingStock = currentMap[code]

                    if (existingStock == null) {
                        // no data
                        currentMap[code] = StockInfo(
                            code = code,
                            name = res.Name,
                            peRatio = res.PEratio.safeToDouble(),
                            dividendYield = res.DividendYield.safeToDouble(),
                            pbRatio = res.PBratio.safeToDouble(),
                            closingPrice = 0.0, // 預設值
                            monthlyAverage = 0.0,
                            change = 0.0,
                            openingPrice = 0.0,
                            highestPrice = 0.0,
                            lowestPrice = 0.0,
                            tradeNum = 0,
                            tradeVolume = 0,
                            tradeValue = 0
                        )
                    } else {
                        // modify
                        currentMap[code] = existingStock.copy(
                            peRatio = res.PEratio.safeToDouble(),
                            dividendYield = res.DividendYield.safeToDouble(),
                            pbRatio = res.PBratio.safeToDouble()
                        )
                    }
                }

                withContext(Dispatchers.Main) {
                    _stockList.value = currentMap.values.toList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
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