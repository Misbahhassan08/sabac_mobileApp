package com.example.carapp.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.carapp.Apis.TestApi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class InspectionViewModel : ViewModel() {

    fun submitSlots(dateWithSlotList: List<DateWithSlots>) {
        val jsonBody = createJsonBody(dateWithSlotList)
        postSlotsToApi(jsonBody)
    }

    private fun createJsonBody(dateWithSlotList: List<DateWithSlots>): String {
        val jsonArray = JSONArray()
        for (dateSlot in dateWithSlotList) {
            val jsonObject = JSONObject().apply {
                put("date", dateSlot.date)
                put("slots", JSONArray(dateSlot.timeSlots))
            }
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    private fun postSlotsToApi(jsonBody: String) {
        val client = OkHttpClient()

        val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(TestApi.add_availability)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API_ERROR", "Failed to post slots: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e("API_ERROR", "Unexpected response: ${response.code}")
                    } else {
                        Log.d("API_SUCCESS", "Slots posted successfully: ${response.body?.string()}")
                    }
                }
            }
        })
    }
}
