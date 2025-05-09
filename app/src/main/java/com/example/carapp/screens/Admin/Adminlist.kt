package com.example.carapp.screens.Admin

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.getToken
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

class AdminListViewModel : ViewModel() {
    private val _admins = mutableStateListOf<Admin>()
    val admins: List<Admin> = _admins

    private val client = OkHttpClient()

    fun fetchAdmins(context: Context) {
        val token = getToken(context)
        val request = Request.Builder()
            .url(TestApi.Get_Admin_list)
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val bodyString = response.body?.string()
                    Log.d("AdminFetch", "Full JSON: $bodyString")

                    bodyString?.let {
                        val adminList = parseAdminsFromJson(it)
                        withContext(Dispatchers.Main) {
                            _admins.clear()
                            _admins.addAll(adminList)
                        }
                    }
                } else {
                    Log.e("AdminFetch", "Failed with code: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("AdminFetch", "Exception: ${e.message}")
            }
        }
    }

    fun parseAdminsFromJson(json: String): List<Admin> {
        val adminList = mutableListOf<Admin>()
        val rootObject = JSONObject(json)
        val adminsArray = rootObject.getJSONArray("admins")

        for (i in 0 until adminsArray.length()) {
            val item = adminsArray.getJSONObject(i)
            val admin = Admin(
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
            adminList.add(admin)
        }

        return adminList
    }

    fun updateAdmin(
        context: Context,
        admin: Admin,
        onResponse: (Boolean) -> Unit = {} // default value
    ) {
        val client = OkHttpClient()
        val token = getToken(context)
        val json = """
    {
        "username": "${admin.username}",
        "email": "${admin.email}",
        "phone_number": "${admin.phone_number}",
        "first_name": "${admin.first_name}",
        "last_name": "${admin.last_name}"
    }
     """.trimIndent()
        Log.d("DealerAPI", "Posting JSON data: $json")

        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${TestApi.Get_admin_update}${admin.id}/")
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
                    fetchAdmins(context)
                    Log.e("DealerAPI", "API error: ${response.code}")
                }
                onResponse(response.isSuccessful)
            }
        })
    }


    fun deleteAdminById(
        dealerId: Int, context: Context
    ) {
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
                    fetchAdmins(context)
                } else {
                    // Handle error
                }
            }
        })
    }

}
data class Admin(
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

