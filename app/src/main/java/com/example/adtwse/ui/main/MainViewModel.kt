package com.example.adtwse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adtwse.data.model.StockInfo

class MainViewModel : ViewModel() {
    private val _stockList = MutableLiveData<List<StockInfo>>()
    val stockList: LiveData<List<StockInfo>> = _stockList

    init {
        loadMockData()
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