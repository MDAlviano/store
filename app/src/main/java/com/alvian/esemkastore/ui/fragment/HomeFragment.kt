package com.alvian.esemkastore.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.Item
import com.alvian.esemkastore.repository.ItemRepository
import com.alvian.esemkastore.ui.adapter.ItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val items = mutableListOf<Item>()
    private lateinit var rvItems: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        rvItems = view.findViewById(R.id.rvItems)

        val name = arguments?.getString("NAME")
        view.findViewById<TextView>(R.id.tvName).text = "Welcome $name"

        setupRv()
        loadItems()

        return view
    }

    private fun loadItems() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ItemRepository.getItems("api/Home/Item")
            try {
                if (response.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        items.clear()
                        items.addAll(response)
                        rvItems.adapter?.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.e("home items", response.toString())
                }
            }
        }
    }

    private fun setupRv() {
        val itemAdapter = ItemAdapter(items) {

        }
        rvItems.apply {
            adapter = itemAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

}