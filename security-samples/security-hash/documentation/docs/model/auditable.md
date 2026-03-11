# 🕵️ Auditing & Metadata

The project implements automated auditing for all security entities via the Auditable base class. This ensures full traceability for compliance (GDPR/SOC2).
Automatic Tracking

Every entity that extends Auditable automatically captures the following metadata:

---

## Overview

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

#### Auditated tables on the system

| Field | Description | Type |
| :--- | :--- | :--- |
| ``createdAt`` | Timestamp when the record was first hashed/saved. | ``ZonedDateTime``
| ``updatedAt``	| Timestamp of the last modification.	| ``ZonedDateTime`` |
| ``createdBy``	| The user/service that initiated the record.	| ``String`` |
| ``lastModifiedBy`` | The last user to update the record. | ``String`` |
| ``version``	| Optimistic locking version to prevent data collisions. | ``Long`` |


#### How to create a new entity and enable auditation

###### 🛠️ Implementation Example

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

The line: 

```Java
  @Version
	@Column(name = "version", nullable = false)
	private Long version = 0L;
```

Is to control the version of the provided table on the database.
