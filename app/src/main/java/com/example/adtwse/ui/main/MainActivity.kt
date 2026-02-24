package com.example.adtwse

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adtwse.data.model.StockInfo

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.displayList.observe(this) { stocks ->
            if (recyclerView.adapter == null) {
                recyclerView.adapter = StockAdapter(stocks)
            } else {
                (recyclerView.adapter as StockAdapter).updateData(stocks)
            }
        }

        viewModel.loadStocks()

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnSort = findViewById<Button>(R.id.btnSort)

        etSearch.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text.toString())
        }


        btnSort.setOnClickListener {
            viewModel.toggleSort()
            btnSort.text = viewModel.getSortText()
        }
    }
}