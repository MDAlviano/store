package com.alvian.esemkastore.ui.adapter

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.CheckoutDetail
import java.net.HttpURLConnection
import java.net.URL

class CheckoutDetailAdapter(private val historyDetails: List<CheckoutDetail>): RecyclerView.Adapter<CheckoutDetailAdapter.CheckoutDetailViewHolder>() {

    class CheckoutDetailViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.iCartItem)
        val name = view.findViewById<TextView>(R.id.tvCartItemName)
        val count = view.findViewById<TextView>(R.id.tvCartItemCount)
        val price = view.findViewById<TextView>(R.id.tvCartItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cartitem, parent, false)
        return CheckoutDetailViewHolder(view)
    }

    override fun getItemCount(): Int = historyDetails.size

    override fun onBindViewHolder(holder: CheckoutDetailViewHolder, position: Int) {
        val historyDetail = historyDetails[position]
        holder.name.text = historyDetail.item.name
        holder.count.text = "Count : ${historyDetail.count}"

        val price = historyDetail.item.price.times(historyDetail.count)
        holder.price.text = "Price: Rp. $price,00"

        Thread {
            val url = URL("http://10.0.2.2:5000/api/Home/Item/Photo/${historyDetail.item.id}")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                (holder.itemView.context as Activity).runOnUiThread {
                    holder.image.setImageBitmap(bitmap)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

}