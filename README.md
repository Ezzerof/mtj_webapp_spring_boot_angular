# My Task Journal Web App

A full-stack web application built with **Spring Boot** and **Angular** that allows users to manage and track their tasks efficiently.

## Features

- **Task Management:** Create, update, and delete tasks through a user-friendly interface.
- **RESTful API:** A robust backend built with Spring Boot that provides a comprehensive REST API.
- **Responsive Frontend:** An interactive Angular-based UI designed for optimal user experience.
- **Modular Architecture:** Clean separation between backend and frontend for maintainability and scalability.

## Technologies Used

- **Backend:** Java, Spring Boot, Maven
- **Frontend:** Angular, TypeScript, HTML, CSS
- **Build Tools:** Maven (with Maven Wrapper) and Node.js with Angular CLI

## Prerequisites

- **Java Development Kit (JDK) 11** or later
- **Maven** (or use the provided Maven Wrapper)
- **Node.js and npm**
- **Angular CLI** (install via `npm install -g @angular/cli`)

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Ezzerof/mtj_webapp_spring_boot_angular.git
   cd mtj_webapp_spring_boot_angular
2. **Backend Setup:**
   - Navigate to the root directory (where the pom.xml is located).
   - Build the project using Maven:
      ```bash
      mvn clean install
      ```
   - Run the Spring Boot application:
     ```bash
     ./mvnw spring-boot:run
     ```
   - The backend server will start on: http://localhost:8080.
3. **Frontend Setup:**
   - Change directory to the Angular project folder:
     ```bash
     cd angular-my-task-journal
     ```
   - Install the required dependencies:
      ```bash
      npm install
      ```
   - Start the Angular development server:
     ```bash
     ng serve
     ```
   - The frontend will be accessible at http://localhost:4200.

## Usage
- Open your web browser and navigate to http://localhost:4200 to access the application.
- Use the intuitive interface to manage your tasks.
- All actions performed on the frontend communicate with the Spring Boot backend through RESTful endpoints.
