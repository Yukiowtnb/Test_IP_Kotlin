package com.example.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class NetworkClient {

    private val client = OkHttpClient()

    suspend fun fetchIpAddress(url: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val json = JSONObject(responseBody.string())
                        val ipAddress = json.getString("myip")
                        Result.success(ipAddress)
                    } ?: Result.failure(Exception("Response body is null"))
                } else {
                    Result.failure(Exception("Failed to fetch IP address"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
