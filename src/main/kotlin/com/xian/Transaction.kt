package com.xian

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import com.google.gson.Gson
import java.security.SecureRandom

class Transaction(private val nodeUrl: String, private val wallet: Wallet) {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json".toMediaType()
    private val secureRandom = SecureRandom()

    data class TransactionPayload(
        val chainId: String,
        val contract: String,
        val function: String,
        val kwargs: Map<String, Any>,
        val nonce: Long,
        val sender: String,
        val stampsSupplied: Long
    )

    data class SimulationResult(
        val success: Boolean,
        val stampsUsed: Long,
        val result: Any?,
        val error: String?
    )

    fun createTransaction(
        contract: String,
        function: String,
        kwargs: Map<String, Any>
    ): TransactionPayload {
        return TransactionPayload(
            chainId = getChainId(),
            contract = contract,
            function = function,
            kwargs = kwargs,
            nonce = getNonce(),
            sender = wallet.publicKey.toHexString(),
            stampsSupplied = 0
        )
    }

    fun simulateTransaction(payload: TransactionPayload): SimulationResult {
        return try {
            val request = Request.Builder()
                .url("$nodeUrl/transactions/simulate")
                .post(gson.toJson(payload).toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.let { 
                    gson.fromJson(it, SimulationResult::class.java) 
                } ?: SimulationResult(false, 0, null, "Empty response")
            } else {
                SimulationResult(false, 0, null, "Request failed")
            }
        } catch (e: Exception) {
            SimulationResult(false, 0, null, e.message)
        }
    }

    fun broadcastTransaction(payload: TransactionPayload): Boolean {
        return try {
            // Sign the transaction
            val signedPayload = signTransaction(payload)
            
            val request = Request.Builder()
                .url("$nodeUrl/transactions")
                .post(gson.toJson(signedPayload).toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private fun getChainId(): String {
        val request = Request.Builder()
            .url("$nodeUrl/chain_id")
            .build()

        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            response.body?.string() ?: "mainnet"
        } else {
            "mainnet"
        }
    }

    private fun getNonce(): Long {
        val request = Request.Builder()
            .url("$nodeUrl/nonce?address=${wallet.publicKey.toHexString()}")
            .build()

        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            response.body?.string()?.toLongOrNull() ?: secureRandom.nextLong()
        } else {
            secureRandom.nextLong()
        }
    }

    private fun signTransaction(payload: TransactionPayload): Map<String, Any> {
        val signature = wallet.signMessage(gson.toJson(payload).toByteArray())
        return mapOf(
            "payload" to payload,
            "signature" to signature.toHexString()
        )
    }
}