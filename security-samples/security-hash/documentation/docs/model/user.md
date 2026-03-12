# User Model

The `User` class is the core entity representing an authenticated user in the system. It maps to the `users` database table and implements Spring Security's `UserDetails` interface to integrate seamlessly with the authentication flow.

---

## Overview

- **Package:** `com.tom.security.hash.security.model`
- **Extends:** `Auditable` (Provides standard audit fields like creation/update timestamps)
- **Implements:** `UserDetails` (Spring Security)
- **Table Name:** `users` (Includes an index on the `email` column for fast lookups)

!!! tip "Authentication"
    This system uses the user's **email** as the primary username for authentication (`getUsername()` returns the email).

## Database Fields

Here are the core columns mapped to the database:

| Field | Type | Constraints | Description |
| :--- | :--- | :---: | :--- |
| `id` | `UUID` | Primary Key, Auto-generated | Unique identifier for the user. |
| `nickname` | `String` | Unique, Not Null | The user's display name. |
| `email` | `String` | Unique, Not Null, Indexed | Used for login and communications. |
| `password` | `String` | Not Null | Hashed password. Ignored in JSON serialization. |
| `roles` | `Role` | Not Null, Enum (`STRING`) | Determines user authorities/permissions. |
| `accountNonLocked`| `boolean`| Not Null, Default `true` | Used by Spring Security to lock accounts. |
| `enabled` | `boolean`| Not Null, Default `true` | Used by Spring Security to disable accounts. |

## Relationships

#### The `User` entity acts as the parent for several other entities:

* **Tokens (`1-to-Many`):** A user can have multiple authentication tokens. Mapped by the `user` property in the `Token` class. Fetched lazily with a `@BatchSize(size = 20)` to optimize database queries.
* **Login Histories (`1-to-Many`):** Tracks the user's login attempts and metadata. Fetched lazily.

*(Note: Both relationships use `CascadeType.ALL` and `orphanRemoval = true`, meaning deleting a user will delete all their associated tokens and login histories).*

## Spring Security Implementation

Because this class implements `UserDetails`, it overrides several methods required by Spring Security:

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.getAuthorities();
}

@Override
public String getUsername() {
    return email; // Email acts as the username
}
```

!!! tip "Email"
    The system has been configured to use email as the primary identifier for user validation instead of a traditional ``username`` field.
    To ensure seamless integration with Spring Security and Lombok, the internal field was renamed to nickname. This prevents Lombok from automatically generating a getUsername() method that conflicts with our custom UserDetails implementation, where getUsername() is explicitly mapped to return the email field.
