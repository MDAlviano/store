package com.alvian.esemkastore.repository

import com.alvian.esemkastore.http.HttpHandler
import com.alvian.esemkastore.model.CheckoutDetail
import com.alvian.esemkastore.model.CheckoutHistory
import com.alvian.esemkastore.model.CheckoutService
import com.alvian.esemkastore.model.Item
import org.json.JSONArray
import org.json.JSONObject

object ItemRepository {

    fun checkout(userId: Int, serviceId: Int, totalPrice: Long, orderDate: String, acceptanceDate: String, details: List<Pair<Int, Int>>): String {
        val json = JSONObject().apply {
            put("userId", userId)
            put("serviceId", serviceId)
            put("totalPrice", totalPrice)
            put("orderDate", orderDate)
            put("acceptanceDate", acceptanceDate)

            val detailsJSONArray = JSONArray()
            details.forEach { (itemId, count) ->
                val detailObject = JSONObject().apply {
                    put("itemId", itemId)
                    put("count", count)
                }
                detailsJSONArray.put(detailObject)
            }
            put("detail", detailsJSONArray)
        }

        return HttpHandler.postRequest("api/Checkout/Transaction", json)
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

    fun getCheckoutHistory(endpoint: String): List<CheckoutHistory> {
        val checkoutHistory = mutableListOf<CheckoutHistory>()
        try {
            val response = HttpHandler.getRequest(endpoint)
            if (response.isNotEmpty()) {
                val jsonArray = JSONArray(response)
                parseHistoryFromJson(checkoutHistory, jsonArray)
            } else {
                return emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return checkoutHistory
    }

    private fun parseHistoryFromJson(
        checkoutHistory: MutableList<CheckoutHistory>,
        jsonArray: JSONArray
    ) {
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            try {
                val detailArray = jsonObject.getJSONArray("detail")
                val details = mutableListOf<CheckoutDetail>()

                for (j in 0 until detailArray.length()) {
                    val detailObject = detailArray.getJSONObject(j)
                    val itemObject = detailObject.getJSONObject("item")

                    val item = Item(
                        id = itemObject.getInt("id"),
                        name = itemObject.getString("name"),
                        description = itemObject.getString("description"),
                        price = itemObject.getLong("price"),
                        stock = itemObject.getInt("stock")
                    )

                    val count = detailObject.getInt("count")

                    details.add(
                        CheckoutDetail(
                            item = item,
                            count = count
                        )
                    )
                }

                checkoutHistory.add(
                    CheckoutHistory(
                        totalPrice = jsonObject.getLong("totalPrice"),
                        orderDate = jsonObject.getString("orderDate"),
                        acceptanceDate = jsonObject.getString("acceptanceDate"),
                        details = details
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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