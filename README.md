# Skincare Products Sales System API

A system for managing the sale of skin care products of company

---

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
---

## Introduction
- A course project to help the company has a system to managing sale products easily.
- Actors: Manager, Staff, Guest, Customer.

---

## Features
- RESTful API for CRUD operations.
- Authentication and role-based authorization.
- Integration with third-party services (e.g., payment gateways, cloud storage).

---

## Technologies Used
- **Frameworks:** Spring Boot, Spring Security, Spring Data JPA
- **Database:** MySQL
- **Build Tool:** Maven
- **Containerization:** Docker
- **Testing Frameworks:** JUnit, Mockito
- **Documentation:** Swagger
- **Version Control:** GitHub

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 21
- Maven
- Database (MySQL)
- Docker

### Installation

1. **Maven build**
```bash
mvn clean package 
```
--- 
2. **Docker build**
```bash
docker build -t api-image . 
```
---
```bash
docker run -it -p 8080:8080 --name=api-container api-image
```
* Or:
```bash
docker-compose up -d api-service
```
---
* Checklog:
```bash
docker-compose logs -tf api-service
```

----
3. **Guide CI/CD:**
```bash
docker-compose up --build -d
```
--build: Build lại image nếu có thay đổi.

-d: Chạy dưới chế độ nền (detached mode).

---
```bash
docker ps
```
Chạy lệnh kiểm tra (Option)

---

```bash
docker logs -f "tên image"
```
Lệnh kiểm tra logs cho image (Option)

---

```bash
docker-compose down 
```
Lệnh dừng toàn bộ hệ thống (Option)

---

```bash
docker-compose down -v
```
Lệnh xóa container (Option)

---
```bash
docker run -p xxxx:xxxx --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag
```

Lệnh run image




