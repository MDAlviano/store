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
import com.alvian.esemkastore.model.CartItem
import java.net.HttpURLConnection
import java.net.URL

class CartItemAdapter(private val cartItems: List<CartItem>): RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    class CartItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.iCartItem)
        val name = view.findViewById<TextView>(R.id.tvCartItemName)
        val count = view.findViewById<TextView>(R.id.tvCartItemCount)
        val price = view.findViewById<TextView>(R.id.tvCartItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cartitem, parent, false)
        return CartItemViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.name.text = cartItem.item.name
        holder.count.text = "Count : ${cartItem.quantity}"
        holder.price.text = "Price: ${cartItem.totalPrice}"

        Thread {
            val url = URL("http://10.0.2.2:5000/api/Home/Item/Photo/${cartItem.item.id}")
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