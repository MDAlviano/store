package com.alvian.esemkastore.ui.adapter

import android.app.Activity
import android.graphics.BitmapFactory
import com.alvian.esemkastore.model.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvian.esemkastore.R
import java.net.HttpURLConnection
import java.net.URL

class ItemAdapter(private val items: List<Item>, private val onClick: (Item) -> Unit): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.imageItem)
        val name = view.findViewById<TextView>(R.id.tvItemName)
        val desc = view.findViewById<TextView>(R.id.tvItemDesc)
        val price = view.findViewById<TextView>(R.id.tvItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.desc.text = item.description
        holder.price.text = "Rp${item.price}.00"

        holder.itemView.setOnClickListener { onClick(item) }

        val imageUrl = "http://10.0.2.2:5000/api/Home/Item/Photo/${item.id}"

        Thread {
            val url = URL(imageUrl)
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