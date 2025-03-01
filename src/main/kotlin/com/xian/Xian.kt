package com.xian

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import com.google.gson.Gson
import com.xian.SmartContract
import com.xian.Transaction

class Xian(private val nodeUrl: String, private val wallet: Wallet) {
    val smartContracts = SmartContract(nodeUrl, wallet)
    val transactions = Transaction(nodeUrl, wallet)
    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json".toMediaType()

    fun getBalance(address: String): Long {
        return runBlocking {
            val request = Request.Builder()
                .url("$nodeUrl/balance?address=$address")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: throw Exception("Empty response")
            gson.fromJson(responseBody, BalanceResponse::class.java).balance
        }
    }

    fun send(amount: Long, toAddress: String): TransactionResult {
        return runBlocking {
            val payload = mapOf(
                "from" to wallet.publicKey.toString(Charsets.UTF_8),
                "to" to toAddress,
                "amount" to amount
            )
            
            val request = Request.Builder()
                .url("$nodeUrl/send")
                .post(gson.toJson(payload).toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: throw Exception("Empty response")
            gson.fromJson(responseBody, TransactionResult::class.java)
        }
    }

    private data class BalanceResponse(val balance: Long)
    data class TransactionResult(val success: Boolean, val hash: String)
}