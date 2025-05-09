package com.example.carapp.screens.Admin


import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.getToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class InspectorListViewModel : ViewModel() {
    private val _inspectors = mutableStateListOf<Inspector>()
    val inspectors: List<Inspector> = _inspectors

    private val client = OkHttpClient()

    fun fetchInspectors(context: Context) {
        val token = getToken(context)
        val request = Request.Builder()
            .url(TestApi.Get_Inspector_list)
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val bodyString = response.body?.string()
                    Log.d("InspectorFetch", "Full JSON: $bodyString")

                    bodyString?.let {
                        val inspectorList = parseInspectorsFromJson(it)
                        withContext(Dispatchers.Main) {
                            _inspectors.clear()
                            _inspectors.addAll(inspectorList)
                        }
                    }
                } else {
                    Log.e("InspectorFetch", "Failed with code: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("InspectorFetch", "Exception: ${e.message}")
            }
        }
    }

    fun parseInspectorsFromJson(json: String): List<Inspector> {
        val inspectorList = mutableListOf<Inspector>()
        val rootObject = JSONObject(json)
        val inspectorsArray = rootObject.getJSONArray("inspectors")

        for (i in 0 until inspectorsArray.length()) {
            val item = inspectorsArray.getJSONObject(i)
            val inspector = Inspector(
                id = item.getInt("id"),
                username = item.getString("username"),
                first_name = item.getString("first_name"),
                last_name = item.getString("last_name"),
                email = item.getString("email"),
                role = item.getString("role"),
                phone_number = item.getString("phone_number"),
                adress = item.getString("adress"),
                image = if (item.isNull("image")) null else item.getString("image")
            )
            inspectorList.add(inspector)
        }

        return inspectorList
    }

    fun updateInspector(
        context: Context,
        inspector: Inspector,
        onResponse: (Boolean) -> Unit = {} // default value
    ) {
        val client = OkHttpClient()
        val token = getToken(context)
        val json = """
    {
        "username": "${inspector.username}",
        "email": "${inspector.email}",
        "phone_number": "${inspector.phone_number}",
        "first_name": "${inspector.first_name}",
        "last_name": "${inspector.last_name}"
    }
     """.trimIndent()
        Log.d("DealerAPI", "Posting JSON data: $json")

        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${TestApi.Get_inspector_update}${inspector.id}/")
            .put(requestBody)
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DealerAPI", "API call failed: ${e.message}")
                onResponse(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    fetchInspectors(context)
                    Log.e("DealerAPI", "API error: ${response.code}")
                }
                onResponse(response.isSuccessful)
            }
        })
    }


    fun deleteInspectorById(dealerId: Int, context: Context) {
        val client = OkHttpClient()
        val token = getToken(context)

        val jsonBody = JSONObject().apply {
            put("id", dealerId)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(TestApi.delete_user)
            .delete(requestBody)
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle failure, maybe show a toast
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    fetchInspectors(context)
                } else {
                    // Handle error
                }
            }
        })
    }

}
data class Inspector(
    val id: Int,
    val username: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val role: String,
    val phone_number: String,
    val adress: String,
    val image: String?
)
