package com.xian

fun main() {
    // Basic wallet example
    println("Creating new wallet...")
    val wallet = Wallet()
    println("Public Key: ${wallet.publicKey.toHexString()}")
    println("Private Key: ${wallet.privateKey.toHexString()}")

    // HD wallet example
    println("\nCreating HD wallet...")
    val hdWallet = HDWallet.generateNew()
    val derivedWallet = hdWallet.getWallet(listOf(44, 0, 0, 0, 0))
    println("Derived Wallet Public Key: ${derivedWallet.publicKey.toHexString()}")

    // Blockchain interaction example
    println("\nConnecting to Xian node...")
    val xian = Xian("http://localhost:52260", wallet)
    val balance = xian.getBalance(wallet.publicKey.toHexString())
    println("Wallet balance: $balance")

    // Send example
    println("\nSending tokens...")
    val result = xian.send(1000, "recipient_address")
    println("Transaction result: ${if (result.success) "Success" else "Failed"} (Hash: ${result.hash})")
}

fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}