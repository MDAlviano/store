package com.alvian.esemkastore.repository

import com.alvian.esemkastore.http.HttpHandler
import com.alvian.esemkastore.model.CheckoutService
import com.alvian.esemkastore.model.Item
import org.json.JSONArray

object ItemRepository {

    fun checkout(endpoint: String): String {
        return ""
    }

    fun getItems(endpoint: String): List<Item> {
        val items = mutableListOf<Item>()
        try {
            val response = HttpHandler.getRequest(endpoint)
            if (response.isNotEmpty()) {
                val jsonArray = JSONArray(response)
                parseItemsFromJson(items, jsonArray)
            } else {
                return emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return items
    }

    fun getServices(endpoint: String): List<CheckoutService> {
        val services = mutableListOf<CheckoutService>()
        try {
            val response = HttpHandler.getRequest(endpoint)
            if (response.isNotEmpty()) {
                val jsonArray = JSONArray(response)
                parseServicesFromJson(services, jsonArray)
            } else {
                return emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return services
    }

    private fun parseServicesFromJson(services: MutableList<CheckoutService>, jsonArray: JSONArray) {
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            try {
                services.add(
                    CheckoutService(
                        id = jsonObject.getInt("id"),
                        name = jsonObject.getString("name"),
                        duration = jsonObject.getInt("duration"),
                        price = jsonObject.getLong("price"),
                        transaction = jsonObject.getJSONArray("transaction")
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseItemsFromJson(items: MutableList<Item>, jsonArray: JSONArray) {
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            try {
                items.add(
                    Item(
                        id = jsonObject.getInt("id"),
                        name = jsonObject.getString("name"),
                        description = jsonObject.getString("description"),
                        price = jsonObject.getLong("price"),
                        stock = jsonObject.getInt("stock")
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}