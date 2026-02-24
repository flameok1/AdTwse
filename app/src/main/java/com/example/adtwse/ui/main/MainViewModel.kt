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
    private val _stockAll = mutableListOf<StockInfo>()

    private val _displayList = MutableLiveData<List<StockInfo>>()


    val displayList: LiveData<List<StockInfo>> = _displayList

    private val repository = StockRepository()

    private var isUpdating = false

    private var currentQuery = ""
    private var sortType = 0 // 0: 預設, 1: 代碼升序, 2: 代碼降序

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
                    _stockAll.clear()
                    _stockAll.addAll(masterMap.values.toList())
                    applyFilterAndSort()
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
        _displayList.value = mock
    }

    fun setSearchQuery(query: String) {
        currentQuery = query
        applyFilterAndSort()
    }

    fun toggleSort() {
        sortType = (sortType + 1) % 3 // 在 0, 1, 2 之間切換
        applyFilterAndSort()
    }

    fun getSortText(): String = when(sortType) {
        1 -> "排序：代碼 ↑"
        2 -> "排序：代碼 ↓"
        else -> "排序：預設"
    }

    private fun applyFilterAndSort() {
        var result = _stockAll.filter {
            it.name.contains(currentQuery, ignoreCase = true) ||
                    it.code.contains(currentQuery)
        }

        result = when (sortType) {
            1 -> result.sortedBy { it.code }
            2 -> result.sortedByDescending { it.code }
            else -> result
        }

        _displayList.value = result
    }
}