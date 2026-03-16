## Key Configuration Variables

#### Configuration Variables (.env)

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
