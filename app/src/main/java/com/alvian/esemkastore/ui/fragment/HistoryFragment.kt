package com.alvian.esemkastore.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alvian.esemkastore.R
import com.alvian.esemkastore.model.CheckoutHistory
import com.alvian.esemkastore.repository.ItemRepository
import com.alvian.esemkastore.ui.adapter.CheckoutHistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var rvHistory: RecyclerView

    private val histories = mutableListOf<CheckoutHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = view.findViewById(R.id.rvHistory)

        setupRv()
        loadHistories()

    }

    private fun loadHistories() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ItemRepository.getCheckoutHistory("api/History/Transaction/1")
            try {
                if (response.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        histories.addAll(response)
                        rvHistory.adapter?.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.e("loadHistories", "error ${e.message}")
                }
            }
        }
    }

    private fun setupRv() {
        val historyAdapter = CheckoutHistoryAdapter(histories)
        rvHistory.apply {
            adapter = historyAdapter
            setHasFixedSize(true)
        }
    }

}