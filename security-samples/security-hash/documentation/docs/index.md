# Security Hash

### 🛠️ Development Setup

This project uses mise-en-place (mise) to manage runtimes and automate development tasks.

#### 1. Prerequisites

Ensure you have the following either installed:

    Mise: Installation Guide

    Docker: For containerized testing/deployment.

#### 2. Environment Configuration

The project relies on environment variables for security settings. Copy the sample file to get started:
```bash
cp .env.sample .env
```

## 🚀 Running the Project

You can interact with the project using the built-in mise tasks.
Using the Command Line (Recommended)
Task	Command	Description
Build	mise run build	Cleans and packages the JAR (skips tests).
Run	mise run run	Starts the Spring Boot application locally.
Docker	mise run docker_run	Spins up the full stack via Docker Compose.
Docs	mise run documentation	Serves this documentation using Zensical.
Using an IDE (Spring Tool Suite / IntelliJ)

If you prefer a GUI-based workflow:

1. Import: Import as a "Maven Project."

2. Environment: You must point the IDE to your .env file or manually add the variables from .env.sample to your Run Configuration.

3. Main Class: Run SecurityHashApplication.java.

| Task | Command | Description |
| :--- | :--- | :--- |
| **Build** | `mise run build` | Cleans and packages the JAR (skips tests). |
| **Run** | `mise run run` | Starts the Spring Boot application locally. |
| **Run** | `mise run run [port]` | Starts the Spring Boot application on Default port `8000`. Example: `mise run run 8000` |
| **Docker** | `mise run docker_run` | Spins up the full stack via Docker Compose. |
| **Docker** | `mise run docker_stop` | Stop the full stack via Docker Compose. |
| **Docs** | `mise run documentation` | Serves this documentation using Zensical. |
