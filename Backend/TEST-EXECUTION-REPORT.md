# MEDORA backend test execution report

## Scope

Tests were added for the auth, patient, practitioner, appointment, and prescription
services. API gateway and discovery server existing context tests were also executed.
Integration tests use in-memory H2 databases and do not connect to the configured
PostgreSQL databases.

## Commands

```text
Backend\auth-service\mvnw.cmd test
Backend\patient-service\mvnw.cmd test
Backend\practitioner-service\mvnw.cmd test
Backend\appointment-service\mvnw.cmd test
Backend\prescription-service\mvnw.cmd test
Backend\api-gateway\mvnw.cmd test
Backend\discovery-server\mvnw.cmd test
```

## Final result

| Module | Tests | Failures | Errors | Skipped |
|---|---:|---:|---:|---:|
| auth-service | 15 | 0 | 0 | 0 |
| patient-service | 15 | 0 | 0 | 0 |
| practitioner-service | 12 | 0 | 0 | 0 |
| appointment-service | 10 | 0 | 0 | 0 |
| prescription-service | 10 | 0 | 0 | 0 |
| api-gateway | 1 | 0 | 0 | 0 |
| discovery-server | 1 | 0 | 0 | 0 |
| **Total** | **64** | **0** | **0** | **0** |

## Observations

- The auth service generates JWTs, but its current `SecurityConfig` does not
  register a JWT authentication filter. A generated JWT therefore does not
  authenticate a protected request; the security test records the current 403
  behavior.
- The entities do not currently declare Bean Validation constraints, so missing
  JSON fields are not rejected by validation. Tests only assert malformed JSON
  rejection where that behavior is implemented by Spring MVC.
- Existing application context tests may log failed Eureka connection attempts
  when no discovery server is running; the test suites still completed successfully.
