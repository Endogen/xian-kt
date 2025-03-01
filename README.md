# Xian Kotlin SDK

[![Kotlin Version](https://img.shields.io/badge/kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Kotlin implementation of the Xian blockchain SDK, providing tools for wallet management, transaction handling, and smart contract interactions.

## Features

- 🛡️ Secure wallet management using Ed25519 cryptography
- 🔗 Hierarchical Deterministic (HD) wallet support
- 📜 Smart contract deployment and interaction
- 💸 Token transfer functionality
- 🔍 Blockchain state queries
- 🧪 Transaction simulation

## Smart Contract Features

### Contract Deployment
Deploy smart contracts to the Xian blockchain with versioning and metadata support.

### Contract Interaction
Call smart contract methods with typed arguments and return values.

### State Management
Query and monitor smart contract state variables and methods.

### Contract Validation
Basic validation of contract code before deployment.

## Installation

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("com.xian:xian-kt:1.0.0")
}
```

### Maven
```xml
<dependency>
    <groupId>com.xian</groupId>
    <artifactId>xian-kt</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### Wallet Management
```kotlin
import com.xian.Wallet

val wallet = Wallet()
println("Public Key: ${wallet.publicKey.toHexString()}")
println("Private Key: ${wallet.privateKey.toHexString()}")
```

### Smart Contract Example
```kotlin
import com.xian.Xian

// Initialize client
val xian = Xian("http://localhost:52260", wallet)

// Example contract code
val contractCode = """
    @export
    def greet(name: str):
        return f"Hello, {name}!"
        
    @export
    def store_value(key: str, value: any):
        storage[key] = value
        return True
        
    @export
    def get_value(key: str):
        return storage.get(key)
""".trimIndent()

// Deploy contract
val deployed = xian.smartContracts.deployContract("example", contractCode)
println("Contract deployed: $deployed")

// Call contract methods
val greeting = xian.smartContracts.callContract("example", "greet", 
    mapOf("name" to "World"))
println("Greeting: $greeting")

// Store and retrieve values
xian.smartContracts.callContract("example", "store_value",
    mapOf("key" to "test", "value" to 42))

val storedValue = xian.smartContracts.callContract("example", "get_value",
    mapOf("key" to "test"))
println("Stored value: $storedValue")

// Get contract state
val state = xian.smartContracts.getContractState("example")
println("Contract state: $state")
```

## API Reference

### SmartContract Class

#### Methods
- `deployContract(name: String, code: String): Boolean`
- `callContract(contractName: String, method: String, args: Map<String, Any>): Any?`
- `getContractState(contractName: String): ContractState?`
- `validateContract(code: String): Boolean`

#### Data Classes
- `Contract`: Represents a deployed contract
- `ContractState`: Represents the current state of a contract

## Documentation

Full documentation is available at [xian-kt-docs](https://github.com/Endogen/xian-kt/wiki)

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

[![Kotlin Version](https://img.shields.io/badge/kotlin-1.9.22-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Kotlin implementation of the Xian blockchain SDK, providing tools for wallet management, transaction handling, and smart contract interactions.

## Features

- 🛡️ Secure wallet management using Ed25519 cryptography
- 🔗 Hierarchical Deterministic (HD) wallet support
- 📜 Smart contract deployment and interaction
- 💸 Token transfer functionality
- 🔍 Blockchain state queries
- 🧪 Transaction simulation

## Installation

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("com.xian:xian-kt:1.0.0")
}
```

### Maven
```xml
<dependency>
    <groupId>com.xian</groupId>
    <artifactId>xian-kt</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### Create a Wallet
```kotlin
import com.xian.Wallet

val wallet = Wallet()
println("Public Key: ${wallet.publicKey.toHexString()}")
println("Private Key: ${wallet.privateKey.toHexString()}")
```

### HD Wallet Example
```kotlin
import com.xian.HDWallet

val hdWallet = HDWallet.generateNew()
val derivedWallet = hdWallet.getWallet(listOf(44, 0, 0, 0, 0))
println("Derived Wallet Public Key: ${derivedWallet.publicKey.toHexString()}")
```

### Blockchain Interaction
```kotlin
import com.xian.Xian

val xian = Xian("http://localhost:52260", wallet)
val balance = xian.getBalance(wallet.publicKey.toHexString())
println("Wallet Balance: $balance")

// Smart Contract Example
val contractCode = """
    @export
    def greet(name: str):
        return f"Hello, {name}!"
""".trimIndent()

// Deploy contract
val deployed = xian.smartContracts.deployContract("greeting", contractCode)
println("Contract deployed: $deployed")

// Call contract
val result = xian.smartContracts.callContract("greeting", "greet", mapOf("name" to "World"))
println("Contract result: $result")

// Get contract state
val state = xian.smartContracts.getContractState("greeting")
println("Contract state: $state")
```

## Documentation

Full documentation is available at [xian-kt-docs](https://github.com/Endogen/xian-kt/wiki)

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


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