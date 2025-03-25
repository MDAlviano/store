package com.alvian.esemkastore.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.Item
import com.alvian.esemkastore.ui.activity.MainActivity
import com.alvian.esemkastore.ui.viewmodel.CartViewModel
import com.google.android.material.textfield.TextInputEditText
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class DetailItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_item, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        val data = arguments?.getParcelable<Item>("ITEM")

        view.findViewById<TextView>(R.id.tvDetailHeader).text = data?.name
        view.findViewById<TextView>(R.id.tvDetailName).text = data?.name
        view.findViewById<TextView>(R.id.tvDetailDesc).text = data?.description
        view.findViewById<TextView>(R.id.tvDetailHeader).text = "Rp. ${data?.price}"
        view.findViewById<TextView>(R.id.tvDetailStock).text = "Stock : ${data?.stock}"
        view.findViewById<TextView>(R.id.tvTotalPrice).text = "Total Price : Rp. ${data?.price}"

        view.findViewById<TextInputEditText>(R.id.etQuantity).setText("1")

        val quantity = view.findViewById<TextInputEditText>(R.id.etQuantity).text.toString()
        var currentQuantity = quantity.toInt()
        val currentTotalPrice = data?.price

        view.findViewById<Button>(R.id.bMinus).setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                currentTotalPrice?.times(currentQuantity)
                view.findViewById<TextView>(R.id.tvTotalPrice).text = "Total Price : Rp. $currentTotalPrice"
                view.findViewById<TextInputEditText>(R.id.etQuantity).setText(currentQuantity)
            } else {
                Toast.makeText(requireContext(), "Minimum quantity is 1", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.bPlus).setOnClickListener {
            currentQuantity++
            currentTotalPrice?.times(currentQuantity)
            view.findViewById<TextView>(R.id.tvTotalPrice).text = "Total Price : Rp. $currentTotalPrice"
            view.findViewById<TextInputEditText>(R.id.etQuantity).setText(currentQuantity)
        }

        view.findViewById<Button>(R.id.bAddToCart).setOnClickListener {
            data.let {
                cartViewModel.addToCart(it!!, currentQuantity, currentTotalPrice!!)
                MainActivity().updateCartCount(cartViewModel.getCartCount())
            }
        }

        thread {
            val url = URL("http://10.0.2.2:5000/api/Home/Item/Photo/${data?.id}")
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                requireActivity().runOnUiThread {
                    view.findViewById<ImageView>(R.id.iDetailItem).setImageBitmap(bitmap)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}