package com.alvian.esemkastore.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.CheckoutHistory

class CheckoutHistoryAdapter(private val histories: List<CheckoutHistory>) :
    RecyclerView.Adapter<CheckoutHistoryAdapter.CheckoutHistoryViewHolder>() {

    class CheckoutHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderDate = view.findViewById<TextView>(R.id.tvOrderDate)
        val rvDetails = view.findViewById<RecyclerView>(R.id.rvCheckoutDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_history, parent, false)
        return CheckoutHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: CheckoutHistoryViewHolder, position: Int) {
        val history = histories[position]
        holder.orderDate.text = history.orderDate

        // setup child adapter
        val childAdapter = CheckoutDetailAdapter(history.details)
        holder.rvDetails.apply {
            adapter = childAdapter
            setHasFixedSize(true)
        }

    }

}