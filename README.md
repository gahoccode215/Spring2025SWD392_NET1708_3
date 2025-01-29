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
5. [Project Rules](#project-rules)
6. [Commit Message Guidelines](#commit-message-guidelines)
   - [Type](#type)
   - [Optional Scope](#optional-scope)
   - [Description](#description)



---

## Introduction
- A course project to help the company has a system to managing sale products easily.
- Actors: Manager, Staff, Guest, Customer.

---

## Features
- Highlight the main functionalities and features.
  - RESTful API for CRUD operations
  - Authentication and role-based authorization
  - Integration with third-party services (e.g., payment gateways, cloud storage)

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
1. **Clone the Repository**  
   Open your terminal and run the following command:  
   git clone https://github.com/gahoccode215/Spring2025SWD392_NET1708_3.git
2. **Build the Project**
   mvn clean install
   
...(More)

---


## Project Rules
1. **Branch Naming Convention**:
   - Use descriptive branch names based on the type of work:
     - Feature: `feature/<short-description>`
     - Bug Fix: `fix/<short-description>`
     - Documentation: `docs/<short-description>`
   - Examples:
     ```text
     feature/add-user-login
     fix/user-authentication-error
     docs/update-readme
     ```

2. **Pull Requests**:
   - Always create a new branch for features or fixes.
   - Submit a pull request (PR) for review before merging into the main branch.
   - Ensure all PRs include:
     - A clear description of the changes.
     - Relevant issue numbers, if applicable (e.g., `Closes #123`).
   - Assign at least one reviewer and address all comments before merging.

3. **Code Reviews**:
   - Every PR must be reviewed and approved by at least one team member.
   - Focus on readability, functionality, and adherence to coding standards.
   - Reviewers should suggest improvements but also acknowledge good practices.

4. **Testing**:
   - Write unit tests for all new features and bug fixes
   - Ensure all tests pass before submitting a PR.
   - Maintain a minimum test coverage of 80%.
5. **Documentation**:
   - Update relevant documentation (e.g., README, API docs) for any significant changes.
   - Ensure comments are included for complex code sections.


---


## Commit Message Guidelines
`<type>(optional scope): <description>`
Example: `feat(pre-event): add speakers section`
 
### Type
 
Available types are:
 
- feat → Changes about addition or removal of a feature. Ex: `feat: add table on landing page`, `feat: remove table from landing page`
- fix → Bug fixing, followed by the bug. Ex: `fix: illustration overflows in mobile view`
- docs → Update documentation (README.md)
- style → Updating style, and not changing any logic in the code (reorder imports, fix whitespace, remove comments)
- chore → Installing new dependencies, or bumping deps
- refactor → Changes in code, same output, but different approach
- ci → Update github workflows, husky
- test → Update testing suite, cypress files
- revert → when reverting commits
- perf → Fixing something regarding performance (deriving state, using memo, callback)
- vercel → Blank commit to trigger vercel deployment. Ex: `vercel: trigger deployment`
 
### Optional Scope
 
Labels per page Ex: `feat(pre-event): add date label`
 
\*If there is no scope needed, you don't need to write it

### Description
 
Description must fully explain what is being done.
 
Add BREAKING CHANGE in the description if there is a significant change.
 
**If there are multiple changes, then commit one by one**
 
- After colon, there are a single space Ex: `feat: add something`
- When using `fix` type, state the issue Ex: `fix: file size limiter not working`
- Use imperative, and present tense: "change" not "changed" or "changes"
- Don't use capitals in front of the sentence
- Don't add full stop (.) at the end of the sentence