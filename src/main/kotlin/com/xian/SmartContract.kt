package com.xian

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

class SmartContract(private val nodeUrl: String, private val wallet: Wallet) {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json".toMediaType()

    data class Contract(
        val name: String,
        val code: String,
        val owner: String,
        val timestamp: Long
    )

    data class ContractState(
        val contractName: String,
        val variables: Map<String, Any>,
        val methods: Map<String, String>
    )

    fun deployContract(name: String, code: String): Boolean {
        return try {
            val payload = mapOf(
                "name" to name,
                "code" to code,
                "owner" to wallet.publicKey.toHexString()
            )
            
            val request = Request.Builder()
                .url("$nodeUrl/contracts/deploy")
                .post(gson.toJson(payload).toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    fun callContract(
        contractName: String,
        method: String,
        args: Map<String, Any> = emptyMap()
    ): Any? {
        return try {
            val payload = mapOf(
                "contract" to contractName,
                "method" to method,
                "args" to args,
                "caller" to wallet.publicKey.toHexString()
            )
            
            val request = Request.Builder()
                .url("$nodeUrl/contracts/call")
                .post(gson.toJson(payload).toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.let { gson.fromJson(it, Any::class.java) }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getContractState(contractName: String): ContractState? {
        return try {
            val request = Request.Builder()
                .url("$nodeUrl/contracts/$contractName/state")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.let { gson.fromJson(it, ContractState::class.java) }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun validateContract(code: String): Boolean {
        // Basic validation logic
        return code.contains("@export") && 
               code.contains("def ") && 
               code.lines().size > 5
    }
}