# Auditing & Metadata

The project implements automated auditing for all security entities via the Auditable base class. This ensures full traceability for compliance (GDPR/SOC2).
Automatic Tracking

---

## Overview

- **Package:** `com.tom.security.hash.global`
- **Type:** `abstract class`

Purpose: Standardizes audit metadata across all persistent entities.

## Database Fields

Every entity that extends Auditable automatically captures the following metadata:

| Field | Type | Constraints | Description |
| :--- | :--- | :---: | :--- |
| ``createdAt`` | ``ZonedDateTime`` | Not Null, Immutable | The exact timestamp when the record was first persisted. |
| ``updatedAt`` | ``ZonedDateTime`` | Not Null | The timestamp of the most recent update. |
| ``createdBy`` | ``String`` | Immutable | The username/ID of the actor who created the record. |
| ``lastModifiedBy`` | ``String`` | ``-`` | The username/ID of the actor who last updated the record. | 
| ``version`` | ``Long`` | Not Null | Used for Optimistic Locking to prevent concurrent write collisions. |

When the system starts to do functions, for an example: creation of user. It will trigger an createdAt function controlled by on the auditated table.

```Java
@EntityListeners(AuditingEntityListener.class) 
```
And on the system configurations. <br>
```Java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
public class AuditConfig {
    @Bean
    AuditorAware<String> auditorProvider() {
    	return new ApplicationAuditAware();
    }
	
	/* 
	* Date time provider is not enforced 
	* Only use it if you need extra configuration.
	*/
	
    @Bean
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}
```

#### How to create a new entity and enable auditation

###### Implementation Example

If you are creating a new entity (like a Student), simply extend the Auditable class:

```Java
@Entity
public class Student extends Auditable {
    @Id
    private Long id;
    private String name;
    // The audit fields are included automaticaly on the db you provide.
}
```

### Important information

!!! tip "The line:"

    ```Java
      @Version
      @Column(name = "version", nullable = false)
      private Long version = 0L;
    ```
    
    Is used to control the version of the provided table on the database.
