# xian-kt

Kotlin SDK for interacting with the Xian blockchain network. This library provides tools for wallet management, transaction handling, and smart contract interactions.

## Table of Contents
- [Installation](#installation)
- [Features](#features)
- [Usage Guide](#usage-guide)
  - [Wallet Management](#wallet-management)
  - [HD Wallet Operations](#hd-wallet-operations)
  - [Blockchain Interactions](#blockchain-interactions)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/your-repo/xian-kt.git
cd xian-kt
```

2. Build the project:
```bash
./gradlew build
```

3. Run the example:
```bash
./gradlew run
```

## Features

- Basic wallet creation and management using Ed25519 cryptography
- BIP39 mnemonic seed generation and recovery (24 words)
- BIP32/SLIP-0010 compliant hierarchical deterministic wallets
- Transaction creation and broadcasting
- Balance queries and token transfers

## Usage Guide

### Wallet Management

```kotlin
import com.xian.Wallet

// Create new wallet with random seed
val wallet = Wallet()
println("Public Key: ${wallet.publicKey.toHexString()}")
println("Private Key: ${wallet.privateKey.toHexString()}")

// Create from existing private key
val existingWallet = Wallet(privateKeyBytes)
```

### HD Wallet Operations

```kotlin
import com.xian.HDWallet

// Create new HD wallet
val hdWallet = HDWallet.generateNew()

// Derive wallet at specific path
val derivedWallet = hdWallet.getWallet(listOf(44, 0, 0, 0, 0))
println("Derived Wallet Public Key: ${derivedWallet.publicKey.toHexString()}")
```

### Blockchain Interactions

```kotlin
import com.xian.Xian

// Initialize client
val wallet = Wallet()
val xian = Xian("http://localhost:52260", wallet)

// Check balance
val balance = xian.getBalance(wallet.publicKey.toHexString())
println("Wallet balance: $balance")

// Send tokens
val result = xian.send(1000, "recipient_address")
println("Transaction result: ${if (result.success) "Success" else "Failed"}")
```