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
- Highlight the main functionalities and features.
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
- (Optional) Docker

### Installation

1. **Build application**
```bash
mvn clean package 
```

2. **Build application**
- Maven statement
```bash
./mvnw spring-boot:run
```
- Jar statement
```bash
java -jar target/skincare-products-sales-system-api.jar
```

- Docker
```bash
docker build -t skincare-products-sales-system-api
docker run -d skincare-products-sales-system-api:latest skincare-products-sales-system-api
```

## Package application
```bash
docker build -t skincare-products-sales-system-api
```
---


