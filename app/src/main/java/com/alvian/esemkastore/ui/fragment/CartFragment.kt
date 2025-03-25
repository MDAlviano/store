package com.alvian.esemkastore.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.CheckoutService
import com.alvian.esemkastore.repository.ItemRepository
import com.alvian.esemkastore.ui.viewmodel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var spServices: Spinner

    private val services = mutableListOf<CheckoutService>()
    private var selectedServiceId: Int? = null

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

                setupSpinner()
                loadServices()
            }
        }

    }

    private fun loadServices() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ItemRepository.getServices("api/Home/Item")
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
                    Log.e("home items", response.toString())
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
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

}