# Nexus.kt 🛡️ | The Secure AI Agent Bridge

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Ktor](https://img.shields.io/badge/Ktor-3.0-purple.svg?style=flat&logo=ktor)](https://ktor.io)
[![Auth0](https://img.shields.io/badge/Auth0-Identity-orange.svg?style=flat&logo=auth0)](https://auth0.com)

**Nexus.kt** is a high-performance, asynchronous middleware designed to bridge the gap between **Sovereign Local AI Agents** and real-world actions. 

Built for the **"Authorized to Act: AI Agents with Auth0"** hackathon (Devpost/Okta).

---

## ⚠️ The Problem: Credential Exposure in AI
As local LLMs and autonomous agents gain the ability to perform actions (booking flights, accessing private data), we face a critical security flaw: **The "Over-Privileged Agent"**.
* Giving a local agent plain-text API keys or passwords creates a massive attack surface.
* If the agent's environment is compromised, the user's entire digital identity is exposed.

## ✅ The Solution: Identity Delegation Layer
Nexus.kt implements a **Delegated Identity Pattern**. Instead of sharing master secrets, the user delegates specific, time-bound authority to the Bridge.

1. **User Authentication:** The user logs in via **Auth0** (OIDC), granting Nexus.kt permission to act.
2. **Token Vaulting:** Nexus.kt securely manages and rotates scoped **JWT Tokens** in a private vault using **MongoDB Atlas**.
3. **Scoped Execution:** The Bridge executes agent requests using **Minimal Necessary Permissions** (Principle of Least Privilege).

> **"In the era of autonomous agents, Identity is the new Perimeter."**

---

## 🛠️ Tech Stack
- **Kotlin 2.0:** Leveraging Structured Concurrency and Coroutines.
- **Ktor 3.0:** Asynchronous, non-blocking microservices architecture.
- **Auth0 (Okta):** Handling M2M (Machine-to-Machine) and Authorization Code flows.
- **MongoDB Atlas:** Asynchronous persistence layer via the official Kotlin driver.

---

## 🚀 Roadmap & Progress
- [x] **Sprint 1: The Vault Logic.** M2M Handshake with Auth0 & Async MongoDB connection.
- [x] **Sprint 2: User Gateway.** Implementing OIDC Login for end-users.
- [x] **Sprint 3: Scoped Execution.** Validation engine for agent requests.

---

## 🛠️ Building & Running
To run the server locally:

| Task | Description |
| :--- | :--- |
| `./gradlew run` | Run the server (Default: http://0.0.0.0:8080) |
| `./gradlew build` | Build the project |
| `./gradlew test` | Run the test suite |
| `./gradlew buildImage` | Build the Docker image |

---
*Created with ❤️ for the Auth0 AI Agents Hackathon.*
