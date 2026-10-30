# MEDORA – Smart Telemedicine Platform

A microservices-based telemedicine platform designed to support virtual healthcare consultations by connecting patients and healthcare practitioners through digital services.

---

## 1. Project Overview

**MEDORA – Smart Telemedicine Platform** is a multi-vendor telemedicine and virtual consultation platform developed as part of the course **SOA Programming and Microservices (PS047)**.

The platform aims to simplify digital healthcare by providing separate services for authentication, patient management, practitioner management, appointment scheduling, and prescription management.

The system follows a **microservices architecture**, where each backend service handles a specific business responsibility and maintains its own database. An API Gateway provides a common entry point for client requests, while a service discovery server supports service registration and discovery.

### Main Objectives

- Provide a centralized platform for patients and healthcare practitioners.
- Support user registration and login.
- Manage patient information.
- Manage practitioner profiles and consultation details.
- Schedule and manage virtual consultation appointments.
- Create and retrieve prescriptions associated with appointments.
- Organize backend functionality into independent microservices.
- Support communication between services through APIs.

---

## 2. Key Features

### Authentication and User Management
- User registration.
- User login.
- Role-based user information.
- Authentication service for managing user access.

### Patient Management
- Create patient profiles.
- View all registered patients.
- Retrieve individual patient details.
- Update patient information.
- Delete patient records.

### Practitioner Management
- Register practitioner details.
- Manage practitioner profiles.
- Store specialization and consultation fees.
- Track practitioner availability.
- Retrieve, update, and delete practitioner records.

### Appointment Management
- Schedule appointments between patients and practitioners.
- Store appointment date and time.
- Maintain appointment status.
- Retrieve and manage appointment records.

### Prescription Management
- Create prescriptions for appointments.
- Store medication details and instructions.
- Retrieve prescription records.
- Delete prescriptions when required.

---

## 3. Technology Stack

| Component | Technology |
|---|---|
| Backend | Java 21 |
| Backend Framework | Spring Boot 3.3.4 |
| Architecture | Microservices |
| Service Communication | REST APIs |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Netflix Eureka |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| API Testing | Postman |
| IDE | Spring Tool Suite (STS) |
| Version Control | Git and GitHub |
| Frontend | MEDORA frontend application |

*The frontend framework and library versions should be documented here once confirmed from the frontend configuration.*

---

## 4. System Architecture

MEDORA follows a microservices architecture.

The frontend communicates with the backend through the API Gateway. The gateway routes requests to the appropriate microservice. Eureka supports service registration and discovery.

### Architecture Overview

```text
                  MEDORA FRONTEND
                         |
                         |
                    API GATEWAY
                    Port: 8080
                         |
          +--------------+--------------+
          |              |              |
       AUTH           PATIENT       PRACTITIONER
      SERVICE         SERVICE        SERVICE
      8081             8082           8083
          |              |              |
       Auth DB        Patient DB    Practitioner DB

          +--------------+--------------+
                         |
                  APPOINTMENT SERVICE
                       8084
                         |
                  Appointment DB

                         |
                 PRESCRIPTION SERVICE
                       8085
                         |
                  Prescription DB

                EUREKA DISCOVERY SERVER
                       8761
```

The diagram represents the logical service organization. Actual request routing and service-to-service communication depend on the configured application properties and implementation.

### Microservices and Responsibilities

| Service | Port | Responsibility |
|---|---:|---|
| Auth Service | 8081 | Registration and login |
| Patient Service | 8082 | Patient profile management |
| Practitioner Service | 8083 | Practitioner profile management |
| Appointment Service | 8084 | Appointment management |
| Prescription Service | 8085 | Prescription management |
| API Gateway | 8080 | Central request routing |
| Discovery Server | 8761 | Service registration and discovery |

---

## 5. Backend Microservices

### 5.1 Auth Service

Responsible for user registration and login.

Main operations:

- Register a new user.
- Authenticate an existing user.
- Store user account information and role.

### 5.2 Patient Service

Responsible for managing patient information.

Patient details include:

- Patient name
- Email
- Phone number
- Age
- Gender

### 5.3 Practitioner Service

Responsible for managing healthcare practitioner information.

Practitioner details include:

- Practitioner name
- Specialization
- Consultation fee
- Availability status

### 5.4 Appointment Service

Responsible for managing consultation appointments.

Appointment details include:

- Patient ID
- Practitioner ID
- Appointment date and time
- Appointment status
- Consultation reason
- Creation timestamp

### 5.5 Prescription Service

Responsible for managing prescriptions associated with appointments.

Prescription details include:

- Appointment ID
- Medication details
- Instructions
- Issued date

### 5.6 API Gateway

The API Gateway acts as the single entry point for backend API requests.

It routes incoming requests to the appropriate microservice.

### 5.7 Discovery Server

The Discovery Server uses Netflix Eureka to support service registration and discovery.

Services can register themselves with Eureka and discover other registered services.

---

## 6. Database Design

MEDORA uses PostgreSQL.

Each business microservice maintains its own database to support service independence.

| Microservice | Database |
|---|---|
| Auth Service | `medora_auth_db` |
| Patient Service | `medora_patient_db` |
| Practitioner Service | `medora_practitioner_db` |
| Appointment Service | `medora_appointment_db` |
| Prescription Service | `medora_prescription_db` |

The API Gateway and Discovery Server do not require separate business databases.

### Create Databases

Open PostgreSQL Query Tool and execute:

```sql
CREATE DATABASE medora_auth_db;
CREATE DATABASE medora_patient_db;
CREATE DATABASE medora_practitioner_db;
CREATE DATABASE medora_appointment_db;
CREATE DATABASE medora_prescription_db;
```

If a database already exists, skip its creation.

### Database Configuration

Configure the database connection separately in each microservice's `application.properties`.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medora_patient_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Replace the database name for each service.

Set `DB_USERNAME` and `DB_PASSWORD` in your local environment. Do not commit actual database passwords to GitHub.

Hibernate can create or update tables based on the JPA entities when the service starts, provided the database connection is configured correctly.

---

## 7. API Endpoints

The following endpoints are exposed through the API Gateway at:

`http://localhost:8080`

### Authentication APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a user |
| POST | `/auth/login` | Login a user |

### Patient APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/patients` | Create a patient |
| GET | `/api/patients` | Get all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| PUT | `/api/patients/{id}` | Update patient |
| DELETE | `/api/patients/{id}` | Delete patient |

### Practitioner APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/practitioners` | Create practitioner |
| GET | `/api/practitioners` | Get all practitioners |
| GET | `/api/practitioners/{id}` | Get practitioner by ID |
| PUT | `/api/practitioners/{id}` | Update practitioner |
| DELETE | `/api/practitioners/{id}` | Delete practitioner |

### Appointment APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/appointments` | Create appointment |
| GET | `/api/appointments` | Get all appointments |
| GET | `/api/appointments/{id}` | Get appointment by ID |
| PUT | `/api/appointments/{id}` | Update appointment |
| DELETE | `/api/appointments/{id}` | Delete appointment |

### Prescription APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/prescriptions` | Create prescription |
| GET | `/api/prescriptions` | Get all prescriptions |
| GET | `/api/prescriptions/{id}` | Get prescription by ID |
| DELETE | `/api/prescriptions/{id}` | Delete prescription |

*Note: Endpoint availability and authorization requirements should be confirmed against the current controller and security configuration.*

---

## 8. Sample API Requests

### Register User

**POST** `/auth/register`

```json
{
  "username": "john",
  "password": "pass123",
  "role": "PATIENT"
}
```

### Login User

**POST** `/auth/login`

```json
{
  "username": "john",
  "password": "pass123"
}
```

### Create Patient

**POST** `/api/patients`

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210",
  "age": 25,
  "gender": "Male"
}
```

### Create Practitioner

**POST** `/api/practitioners`

```json
{
  "name": "Dr. Sarah Smith",
  "specialization": "Cardiology",
  "consultationFee": 500.0,
  "availabilityStatus": "AVAILABLE"
}
```

### Create Appointment

**POST** `/api/appointments`

```json
{
  "patientId": "1",
  "practitionerId": "1",
  "appointmentTime": "2026-11-01T10:30:00",
  "status": "REQUESTED",
  "reason": "General consultation"
}
```

### Create Prescription

**POST** `/api/prescriptions`

```json
{
  "appointmentId": 1,
  "medicationDetails": "Paracetamol 500mg",
  "instructions": "Take after meals",
  "issuedDate": "2026-11-01T11:00:00"
}
```

Use actual IDs returned by the APIs rather than assuming that records will always have ID `1`.

---

## 9. Installation and Setup

### Prerequisites

Install the following:

- Java Development Kit (JDK) 21
- Spring Tool Suite (STS)
- PostgreSQL
- pgAdmin
- Maven (or use the included Maven Wrapper)
- Git
- Postman

### Step 1: Clone the Repository

```bash
git clone https://github.com/BottaNavya/MEDORA-Smart-Telemedicine-Platform.git
```

Move into the repository:

```bash
cd MEDORA-Smart-Telemedicine-Platform
```

### Step 2: Create PostgreSQL Databases

Create the five databases listed in the Database Design section.

### Step 3: Configure Database Credentials

Configure the PostgreSQL username and password for each backend service.

Ensure that the database names and ports match the local PostgreSQL configuration.

### Step 4: Import Backend Projects into STS

1. Open Spring Tool Suite.
2. Select **File → Import**.
3. Choose **Maven → Existing Maven Projects**.
4. Browse to the cloned repository folder.
5. Select the seven backend Maven projects.
6. Click **Finish**.
7. Right-click the projects and select **Maven → Update Project**.

Do not import the frontend folder as a Maven project unless it has its own Maven configuration.

### Step 5: Start the Services

Start the services in the following order:

1. Discovery Server
2. Auth Service
3. Patient Service
4. Practitioner Service
5. Appointment Service
6. Prescription Service
7. API Gateway

Wait for each service to finish starting before testing the gateway.

### Step 6: Verify Service Availability

| Component | Local URL |
|---|---|
| API Gateway | http://localhost:8080 |
| Auth Service | http://localhost:8081 |
| Patient Service | http://localhost:8082 |
| Practitioner Service | http://localhost:8083 |
| Appointment Service | http://localhost:8084 |
| Prescription Service | http://localhost:8085 |
| Eureka Dashboard | http://localhost:8761 |

The Eureka dashboard can be used to check registered services.

---

## 10. Testing

Postman can be used to test the REST APIs.

Recommended testing sequence:

1. Register a user.
2. Log in with the registered credentials.
3. Create a patient.
4. Retrieve the patient record.
5. Create a practitioner.
6. Retrieve the practitioner record.
7. Create an appointment using valid patient and practitioner IDs.
8. Retrieve the appointment.
9. Create a prescription using a valid appointment ID.
10. Retrieve the prescription.

### Maven Tests

From an individual service directory, run:

```bash
mvn test
```

On Windows, when using the Maven Wrapper:

```cmd
mvnw.cmd test
```

To package a service without executing tests:

```cmd
mvnw.cmd -DskipTests package
```

Tests that interact with PostgreSQL require valid local database credentials and a running database.

Refer to `TEST-EXECUTION-REPORT.md` in the repository root for the recorded backend test execution details.

---

## 11. Repository Structure

The repository is organized into frontend and backend components.

```text
MEDORA-Smart-Telemedicine-Platform/
│
├── auth-service/
│
├── patient-service/
│
├── practitioner-service/
│
├── appointment-service/
│
├── prescription-service/
│
├── api-gateway/
│
├── discovery-server/
│
├── medora-frontend/
│
├── TEST-EXECUTION-REPORT.md
│
├── .gitignore
│
└── README.md
```

Each backend microservice is an independent Maven/Spring Boot project with its own source code and configuration.

---

## 12. Error Handling and Troubleshooting

### PostgreSQL Authentication Failed

Check:

- PostgreSQL is running.
- Database names are correct.
- Username and password are correct.
- The application configuration points to the correct database.

### Service Not Registered in Eureka

Check:

- Discovery Server is running.
- The service's Eureka client configuration is correct.
- The service can reach the Discovery Server.

### Maven Dependencies Not Downloading

In STS:

1. Right-click the project.
2. Select Maven → Update Project.
3. Enable Force Update of Snapshots/Releases.
4. Click OK.

### Port Already in Use

Check whether another application is using the service port. Stop the conflicting process or configure a different port.

### API Returns 404

Verify:

- The API Gateway is running.
- The correct route is configured.
- The target microservice is running.
- The requested endpoint and HTTP method are correct.

---

## 13. Security Considerations

Security is important because MEDORA handles healthcare-related information.

The project includes an authentication service and has JWT-related implementation. However, token generation alone does not establish complete JWT-based request authentication.

Before production deployment, the following should be implemented and verified:

- Secure password hashing using BCrypt or an equivalent password encoder.
- JWT validation through a configured authentication filter or equivalent security mechanism.
- Authorization based on user roles.
- Input validation for incoming API requests.
- Centralized and consistent exception handling.
- Secure storage of database credentials and secrets.
- Protection of patient and practitioner information.
- HTTPS for deployed environments.

Sample credentials used for development and testing must never be treated as production credentials.

---

## 14. Future Enhancements

Potential future improvements include:

- Complete JWT-based authentication and authorization.
- Role-specific dashboards for patients and practitioners.
- Video consultation integration.
- Online consultation payment integration.
- Email and appointment notifications.
- Practitioner search and filtering.
- Appointment availability management.
- Prescription PDF generation.
- API documentation using Swagger/OpenAPI.
- Docker-based deployment.
- Cloud deployment and monitoring.
- Improved logging and centralized exception handling.

---

## 15. Project Information

**Project Name:** MEDORA – Smart Telemedicine Platform

**Course:** SOA Programming and Microservices

**Course Code:** PS047

**Project Type:** Multi-Vendor Telemedicine and Virtual Consultation Engine

**Architecture:** Microservices Architecture

**Repository:** [MEDORA Smart Telemedicine Platform](https://github.com/BottaNavya/MEDORA-Smart-Telemedicine-Platform)

---

## 16. Conclusion

MEDORA demonstrates the use of service-oriented architecture and microservices in a healthcare application.

By separating authentication, patient, practitioner, appointment, and prescription management into independent services, the platform provides a modular foundation for developing and extending telemedicine functionality.

The project demonstrates REST API development, Spring Boot, PostgreSQL integration, service discovery, API Gateway routing, and independent microservice organization.
