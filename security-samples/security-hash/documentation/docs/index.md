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

## Key Configuration Variables

#### ⚙️ Configuration Variables (.env)

These variables should be defined in your .env file. The application uses these to configure the Spring Boot context and security filters.

| Category | Variable | Default Value	| Description |
| :--- | :--- | :--- | :--- |
| Server Port	| SECURITY_HASH	| 8000 | The server port the application listens on. |
| Authentication | ADMIN_USER |	admin |	Default administrator username. |
| Authentication | ADMIN_EMAIL	| admin@admin.com | Email for the admin account. |
| Authentication | ADMIN_PASSWORD | admin | Initial password (Remember to Change on Prod!). |
| Security Policy |	HTTPS_ONLY | false | If true, forces all traffic over SSL. |
| Security Policy | CSRF_ENABLED	| false	| Enables/Disables Cross-Site Request Forgery protection. | 
| Security Policy | ALLOWED_ORIGINS	| http://localhost:4200	| CORS policy: permitted frontend origins. |
| Security Policy | CORS_TIME	| 3600 | How long (seconds) CORS preflight results are cached. |
| Security Policy | Key & Token	| KEY_PATH | /app/keys |	Path where cryptographic keys are stored. |
| Security Policy | EXPIRED_KEY	| 15m	| Lifespan of an access token/key. | 
| Security Policy | REFRESH_KEY	| 7d	| Lifespan of a refresh token. |
| Security Policy | TOKEN_CLEANUP	| 24h	| Interval for purging expired tokens from DB. |
| Cache & Throttling | CACHE_ADDRESS | http://127.0.0.1:5701 |	Address for the Hazelcast/Redis cache layer. |
| Cache & Throttling | LOGIN_ATTEMPTS | 5 | Max failed logins before account/IP lockout. | 
| Cache & Throttling | LOGIN_CACHE_TIME | 15m	| How long a login lockout lasts. |
| Application |	PAGE_SIZE	| 20 | Default record count for paginated API responses. |

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
