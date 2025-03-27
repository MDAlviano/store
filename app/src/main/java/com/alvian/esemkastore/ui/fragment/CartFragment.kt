package com.alvian.esemkastore.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.CartItem
import com.alvian.esemkastore.model.CheckoutService
import com.alvian.esemkastore.repository.ItemRepository
import com.alvian.esemkastore.ui.adapter.CartItemAdapter
import com.alvian.esemkastore.ui.viewmodel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class CartFragment : Fragment() {

    private lateinit var spServices: Spinner

    private val services = mutableListOf<CheckoutService>()
    private var selectedServiceId: Int? = null

    private val cartItems = mutableListOf<CartItem>()

    private lateinit var rvCartItems: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                view.findViewById<LinearLayout>(R.id.layoutEmpty).visibility = View.VISIBLE
                view.findViewById<LinearLayout>(R.id.layoutNotEmpty).visibility = View.GONE
            } else {
                view.findViewById<LinearLayout>(R.id.layoutEmpty).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.layoutNotEmpty).visibility = View.VISIBLE

                rvCartItems = view.findViewById(R.id.rvCheckoutedItems)

                setupSpinner()
                loadServices()

                setupRvItems()

                cartItems.clear()
                cartItems.addAll(items)
                rvCartItems.adapter?.notifyDataSetChanged()

                val totalPrice = items.sumOf { it.totalPrice }
                val item = items.map { cartItem ->
                    Pair(cartItem.item.id, cartItem.quantity)
                }

                val selectedService = services.find { it.id == selectedServiceId }
                val duration = selectedService?.duration ?: 0

                val localDate = LocalDate.now()
                val orderDate = localDate.toString()
                val acceptanceDate = localDate.plusDays(duration.toLong()).toString()

                view.findViewById<TextView>(R.id.tvCartTotalPrice).text = "Total Price : Rp. $totalPrice,00"

                view.findViewById<Button>(R.id.bCheckout).setOnClickListener {
                    try {
                        doCheckout(
                            userId = 1,
                            serviceId = selectedServiceId!!,
                            totalPrice = totalPrice,
                            orderDate = orderDate,
                            acceptanceDate = acceptanceDate,
                            details = item
                        )
                        cartViewModel.checkout()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }

    }

    private fun doCheckout(
        userId: Int,
        serviceId: Int,
        totalPrice: Long,
        orderDate: String,
        acceptanceDate: String,
        details: List<Pair<Int, Int>>
    ) {
        thread {
            try {
                val response = ItemRepository.checkout(
                    userId,
                    serviceId,
                    totalPrice,
                    orderDate,
                    acceptanceDate,
                    details
                )
                Toast.makeText(requireContext(), "Success checkout items", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Log.e("doCheckout", e.message.toString())
            }
        }
    }

    private fun setupRvItems() {
        val ciAdapter = CartItemAdapter(cartItems)
        rvCartItems.apply {
            adapter = ciAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadServices() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ItemRepository.getServices("api/Checkout/Service")
            try {
                if (response.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        services.clear()
                        services.addAll(response)
                        setupSpinner()
                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.e("checkout services", response.toString())
                }
            }
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            services.map { it.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spServices.adapter = adapter
        spServices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedServiceId = services[p2].id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

}