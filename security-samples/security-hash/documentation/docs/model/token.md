# Token Model

The `Token` class is an entity that stores the refresh tokens used by the user to validate itself and fetch an Auth token on the system.

---

## Overview

- **Package:** `com.tom.security.hash.security.model`
- **Extends:** `Auditable` (Provides standard audit fields like creation/update timestamps)
- **Table Name:** `tokens` (Includes an index on the `email` column for fast lookups)

## Database Fields

Here are the column mapped to the database:

| Field | Type | Constraints | Description |
| :--- | :--- | :---: | :--- |
| `id` | `Long` | Primary Key, Auto-generated | Unique internal identifier for the token. |
| `token` | `String` | Unique, Not Null, Immutable | The actual token string (Length: 1024). |
| `tokenType` | `TokenType` | Not Null, Enum(String) | The classification of the token `BEARER`, the Default is ``BEARER``. |
| ``revoked``	| ``boolean``	| Not Null | Manual invalidation flag (used for logout). |
| ``expired``	| ``boolean``	| Not Null | Temporal invalidation flag. |
| ``user`` | ``User``	| Not Null, Foreign Key (Lazy) | The user account associated with this token. |

!!! tip "Observation on the Token Type" 
    Only using on the moment ``BEARER``, but if you need to implement others types, there is ``EMAIL_VERIFICATION`` and ``PASSWORD_RESET``, both can be used with an mail provider to send tokens for verifying the user mail or when needed to create a new password. Because an mail provider isn't implemented.

Not using UUID for the database field, because of the little performance gain for using Long instead.

## Relationships
#### Token ↔ User (Many-to-One)

* **Type: @ManyToOne**

* **Fetch Strategy: LAZY**

* Description: Multiple tokens can be associated with a single user (allowing for multiple concurrent sessions across different devices). The use of LAZY fetching prevents loading the full User object unless specifically required by the business logic.

## Business Logic

The entity includes helper methods to manage the security state of the token without exposing setter logic directly to the service layer for every field:

``isValid():`` Returns ``true`` only if the token is neither revoked nor expired.

``revoke():`` Sets the ``revoked`` status to true.

``expire():`` Sets the ``expired`` status to true.
