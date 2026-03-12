# Login History Model

The `Login History` class is an entity to monitorate the user access when it authenticate on the system. It map to the table `login_history` table and saves both the time the user acessed the system and their ip, when it performs an sucessfull authentication on system.

---

## Overview

- **Package:** `com.tom.security.hash.security.model`
- **Extends:** `Auditable`
- **Table Name:** `login_history` (Includes an index for `loginTime` column for fast lookups)

## Database Fields

Column that are mapped to the database:

| Field | Type | Constraints | Description |
| :--- | :--- | :---: | :--- |
| ``id`` | ``UUID`` | Primary Key, Auto-generated | Unique identifier for the login history record. |
| ``user`` | ``User`` | ``Foreign Key (Lazy)`` | The user associated with this login attempt. |
| ``loginTime`` | ``ZonedDateTime`` | ``Not Null, Indexed, Immutable`` | The exact timestamp when the login occurred. |
| ``ipAddress`` | ``String`` | ``Not Null, Immutable`` | The network IP address of the client device |

## Relationships

The system architecture utilizes standard JPA relationship mapping to maintain data integrity and performance.

#### User ↔ LoginHistory (One-to-Many)

* **Type: @ManyToOne** (from the perspective of LoginHistory).

* **Fetch Strategy: LAZY**. This ensures that when you query a login record, the system doesn't unnecessarily load the entire User object unless explicitly accessed, optimizing memory usage.

* **Join Column: user_id**. This links the history record to the primary key of the users table.
