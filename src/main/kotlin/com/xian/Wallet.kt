package com.xian

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import java.security.SecureRandom

class Wallet(privateKey: ByteArray? = null) {
    private val privateKeyParams: Ed25519PrivateKeyParameters
    val publicKeyParams: Ed25519PublicKeyParameters

    val publicKey: ByteArray
        get() = publicKeyParams.encoded

    val privateKey: ByteArray
        get() = privateKeyParams.encoded

    init {
        if (privateKey != null) {
            // Create from existing private key
            privateKeyParams = Ed25519PrivateKeyParameters(privateKey, 0)
            publicKeyParams = privateKeyParams.generatePublicKey()
        } else {
            // Generate new key pair
            val keyPairGenerator = Ed25519KeyPairGenerator()
            keyPairGenerator.init(Ed25519KeyGenerationParameters(SecureRandom()))
            val keyPair = keyPairGenerator.generateKeyPair()
            privateKeyParams = keyPair.private as Ed25519PrivateKeyParameters
            publicKeyParams = keyPair.public as Ed25519PublicKeyParameters
        }
    }

    fun signMessage(message: ByteArray): ByteArray {
        val signer = Ed25519Signer()
        signer.init(true, privateKeyParams)
        signer.update(message, 0, message.size)
        return signer.generateSignature()
    }

    fun verifyMessage(message: ByteArray, signature: ByteArray): Boolean {
        val verifier = Ed25519Signer()
        verifier.init(false, publicKeyParams)
        verifier.update(message, 0, message.size)
        return verifier.verifySignature(signature)
    }

    companion object {
        fun isValidKey(key: ByteArray): Boolean {
            return try {
                Ed25519PublicKeyParameters(key, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}