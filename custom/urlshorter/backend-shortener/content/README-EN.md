<div align = "center">
<a>
<img src="logo.png" alt="Logo" width="80" height="80">
</a>
<br>
<p>ShortURL is an efficient and secure URL shortener built on Spring Boot that allows you to convert long links into short, easy-to-share URLs. The system supports JWT authentication, caching with Redis, persistence in PostgreSQL, and documentation via Swagger or Postman.</p>
</div>

> **[Leia esta documentação em português](README-PT.md)**

## 📖 **Index**

- [Overview](#-overview)
- [Prerequisites](#-prerequisites)
- [Technologies](#-technologies)
- [Swagger](#-swagger)
- [Postman](#-postman)
- [Environment Variables](#%EF%B8%8F-environment-variables)
- [Testing](#-testing)
- [License](#-license)
- [References](#references)

## 🔭 **Overview**

ShortURL allows users to shorten URLs and track access statistics. With support for JWT authentication, Redis caching, and PostgreSQL as the database, it is scalable and secure. The system also includes **Swagger** for API exploration and **Flyway** for database migration control.

**Main Features:**

- URL shortening.
- Authentication and authorization of `/actuator` via JWT.
- Redis support for performance improvement.
- Integration with PostgreSQL for persistence.
- API documentation via **Swagger** or **Postman**.
- Flexible configurations via **environment variables**.

---

## <img src="https://static-00.iconduck.com/assets.00/toolbox-emoji-512x505-gpgwist1.png" width="20" height="20" alt="Toolbox"> **Prerequisites**

- **JDK** (version **21** or later)
- **Maven** (version **3.8** or later)
- **Spring Boot** (version **3.4.2** or later)
- **PostgreSQL** (version **16** or later)
- **Redis** (Optional, for caching)

---

## 💻 **Technologies**

- **Spring Boot** (Main framework)
- **Spring Security** (Authentication and authorization via JWT)
- **Spring Data JPA** (Relational database persistence)
- **Spring Data Redis** (Cache for URL shortening)
- **MapStruct** (DTO and entity conversion)
- **JWT (JJWT)** (Authentication and security)
- **Flyway** (Database migration)
- **Swagger (SpringDoc OpenAPI)** (API documentation)
- **PostgreSQL** (Primary database)
- **H2 Database** (In-memory database for testing)
- **Redis** (Optional, used for cache and performance)
- **Prometheus** (Optional used for monitoring)

---

## 📜**Swagger**

The project provides API documentation via Swagger. To access it, re-enable Swagger in `application.yml` and in `SecurityConfig`, then start the system and go to:

🔗 **`http://localhost:8000/swagger-ui.html`**

---

## 🔗**Postman**

Test collection for **Postman** available:

[Postman Collection](https://www.postman.com/sam-goldman11/programs-of-mapple/collection/r2yhoqi/url-shortener)

---

## <img src="https://img.icons8.com/plasticine/100/java-coffee-cup-logo.png" alt="java-coffee-cup-logo" width="50" height="50" style="position: relative; top: 10px;">**Program Installation**

Configure the available files with environment variables detailed in `config.args` for deployment. If performing tests, use `testes.args` to set the appropriate parameters for a test environment or define attributes directly in the IDE with the latest version.

To start the program automatically with configured environment variables, use one of the available scripts:

- **`run.bat`** (For Windows)
- **`run.ps1`** (For Windows PowerShell)

These scripts ensure that all environment variables are correctly loaded before running the Java program.

---

## <img src="https://user-images.githubusercontent.com/11943860/46922575-7017cf80-cfe1-11e8-845a-0cd198fb546c.png" alt="java-coffee-cup-logo" width="30" height="30" style="position: relative; top: 5px;"> **Eclipse IDE**

The project includes configuration files for **Eclipse IDE**, which can be imported directly into **Run Configuration**. This facilitates automatic loading of environment variables and switching between different implementation types, such as:

- **Development Environment**
- **Testing Environment**
- **Production Deployment**

---

## ⚙️ **Environment Variables**

|**Description**|**Parameter**|**Default values**|
|---|---|---|
|`Server port`|`SERVER_PORT`|`8000`|
|`JWT Secret Key for /actuator authentication`|`SECRET-KEY`|`NONE`|
|`Password to access /actuator`|`SECURITY_PASSWORD`|`NONE`|
|`Database URL`|`DB_HOST`|`LOCALHOST`|
|`Database port`|`DB_PORT`|`5432`|
|`Database name`|`DB_DATABASE`|`example`|
|`Database user`|`DB_USER`|`NONE`|
|`Database password`|`DB_PASSWORD`|`NONE`|
|`Max connection lifetime`|`DB_MAXLIFETIME`|`300000`|
|`Idle timeout`|`DB_IDLE-TIMEOUT`|`180000`|
|`Clean expired URLs`|`EXPIRED_DATA`|`1200000`|
|`Redis user`|`REDIS_USERNAME`|`NONE`|
|`Redis host`|`REDIS_HOST`|`NONE`|
|`Redis password`|`REDIS_PASSWORD`|`NONE`|
|`Redis port`|`REDIS_PORT`|`NONE`|
|`Redis SSL`|`REDIS_SSL`|`NONE`|
|`Redis connection check interval`|`REDIS_CONNECTION`|`300000`|
|`Redis-to-Database sync interval`|`REDIS_SYNC`|`300000`|
|`Prometheus logging with Docker or AWS` (Experimental)|`PROMETHEUS_REGION`|`NONE`|
|`Prometheus remote write URL` (Experimental)|`REMOTE_WRITE_URL`|`NONE`|
|`Enable or disable Swagger`|`SWAGGER_ENABLE`|`false`|
|`Swagger URL`|`SWAGGER_URL`|`/docs`|

---

## **Testing**

To run tests, execute with specific parameters for the test environment using the following command:

> 🚨 Check the [prerequisites](#-prerequisites)

bash

CopyEdit

`mvn test`

---

## **Environment Variables for Testing**

|**Description**|**Parameter**|**Default values**|
|---|---|---|
|`Server port`|`SERVER_PORT`|`8000`|
|`JWT Secret Key for /actuator authentication`|`SECRET-KEY`|`NONE`|
|`Password to access /actuator`|`SECURITY_PASSWORD`|`NONE`|
|`Redis user`|`REDIS_USERNAME_TEST`|`NONE`|
|`Redis host`|`REDIS_HOST_TEST`|`NONE`|
|`Redis password`|`REDIS_PASSWORD_TEST`|`NONE`|
|`Redis port`|`REDIS_PORT_TEST`|`NONE`|
|`Redis SSL`|`REDIS_SSL_TEST`|`NONE`|

---

## 📄 **License**

This project is licensed under the **BSD 2-Clause License**. For more details, see the [LICENSE](LICENSE) file.

> 🔗 **[BSD 2-Clause License](https://opensource.org/license/bsd-2-clause)**

---

## 📌**References**

> Based on the original project by **[Zeeshaan Ahmad](https://github.com/zeeshaanahmad/url-shortener)**.