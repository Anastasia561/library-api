# Library Management System

**Library management system** is a cloud-integrated, secure backend platform for managing digital books and documents. It enables users to upload books with cover images, download pdfs generate preview files via AWS Lambda, and enforce strict validation and access control using OAuth2 JWT tokens.

---

## Features

### User Roles
- **Non-registered user:** View book listing, browse books
- **Registered user:** Download full and preview book pdfs  
- **Librarian:** Upload book, manage metadata, view enhanced listing 

### Preview Generation
- AWS S3 triggers AWS Lambda
- Python Lambda extracts first 5 pages
- Preview accessible via signed URL

### Security
- OAuth2 + JWT token-based authentication
- Role-based endpoint authorization
- Secure password hashing  

---

## Technologies Used

- **Backend:** Java 21, Spring Boot, Spring Security, Spring MVC
- **Database:** MySQL (via AWS RDS)
- **Storage:** AWS S3 (book files & previews)
- **Lambda** Python 3.12 + PyPDF2 (for preview generation)
- **Security:** OAuth2 + JWT
- **Testing:** JUnit 5, Mockito, Testcontainers, LocalStack
- **Documentation:** Swagger UI (OpenAPI 3.0)
- **Build Tool:** Maven
- **Containerization:** Docker

---

## Performance & Security

- Passwords securely hashed  
- Role-based access control for all endpoints
- OAuth2 access tokens with expiration
- 95% of file operations and Lambda executions under 2s
  
 ---

 ## Cloud Architecture

 - **S3** for file storage
 - **Lambda** for preview creation
 - **VPC**-secured access to **RDS**
 - **IAM** roles for Lambda & EC2 communication
