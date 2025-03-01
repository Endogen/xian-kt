package com.xian

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import java.security.SecureRandom
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.generators.HKDFBytesGenerator
import org.bouncycastle.crypto.params.HKDFParameters

class HDWallet(private val seed: ByteArray) {
    private val masterKey: ByteArray
    
    init {
        val hkdf = HKDFBytesGenerator(SHA512Digest())
        hkdf.init(HKDFParameters.defaultParameters(seed))
        masterKey = ByteArray(64)
        hkdf.generateBytes(masterKey, 0, masterKey.size)
    }

    fun getWallet(path: List<Int>): Wallet {
        var currentKey = masterKey
        for (index in path) {
            val hkdf = HKDFBytesGenerator(SHA512Digest())
            hkdf.init(HKDFParameters.skipExtractParameters(currentKey, index.toString().toByteArray()))
            currentKey = ByteArray(64)
            hkdf.generateBytes(currentKey, 0, currentKey.size)
        }
        return Wallet(currentKey.copyOfRange(0, 32))
    }

    companion object {
        fun generateNew(): HDWallet {
            val random = SecureRandom()
            val seed = ByteArray(64)
            random.nextBytes(seed)
            return HDWallet(seed)
        }
    }
}