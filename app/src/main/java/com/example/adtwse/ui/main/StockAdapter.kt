package com.example.adtwse

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adtwse.data.model.StockInfo

class StockAdapter(private var stocks: List<StockInfo>) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvStockName)
        val tvCode: TextView = view.findViewById(R.id.tvStockCode)


        val tvOpen: TextView = view.findViewById(R.id.tvOpenValue)
        val tvClose: TextView = view.findViewById(R.id.tvCloseValue)


        val tvMax : TextView = view.findViewById(R.id.tvMaxValue)

        val tvMin : TextView = view.findViewById(R.id.tvMinValue)

        val tvAvg: TextView = view.findViewById(R.id.tvAvgValue)
        val tvChange: TextView = view.findViewById(R.id.tvChangeValue)


        var tvTradeNum : TextView = view.findViewById(R.id.tvTradeNumValue)

        var tvTradeTotal : TextView = view.findViewById(R.id.tvTradeTotalValue)

        var tvTradeSum : TextView = view.findViewById(R.id.tvTradeSumValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = stocks[position]
        holder.tvName.text = stock.name
        holder.tvCode.text = stock.code
        holder.tvOpen.text = stock.openingPrice.toString()
        holder.tvClose.text = stock.closingPrice.toString()

        holder.tvMax.text = stock.highestPrice.toString()
        holder.tvMin.text = stock.lowestPrice.toString()

        holder.tvAvg.text = stock.monthlyAverage.toString()
        holder.tvChange.text = stock.change.toString()

        holder.tvTradeNum.text = stock.tradeNum.toString()
        holder.tvTradeTotal.text = stock.tradeVolume.toString()
        holder.tvTradeSum.text = stock.tradeValue.toString()

        val close = stock.closingPrice ?: 0.0
        val avg = stock.monthlyAverage ?: 0.0
        holder.tvClose.setTextColor(if (close > avg) Color.RED else Color.GREEN)

        if (stock.change > 0) {
            holder.tvChange.setTextColor(Color.RED)
        } else if (stock.change < 0) {
            holder.tvChange.setTextColor(Color.GREEN)
        }


        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            // 建立對話框
            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("${stock.name} (${stock.code})") // 標題顯示名稱與代碼
                .setMessage(
                    "本益比：${stock.peRatio}\n" +
                            "殖利率：${stock.dividendYield}%\n" +
                            "股價淨值比：${stock.pbRatio}"
                ) // 顯示考題要求的內容
                .setPositiveButton("確定", null) // 提供關閉按鈕
                .show()
        }
    }

    fun updateData(newStocks: List<StockInfo>) {
        this.stocks = newStocks
        notifyDataSetChanged()
    }

    override fun getItemCount() = stocks.size
}