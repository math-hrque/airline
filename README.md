# Airline

_Airline Project: A microservices-based airline management system_

## Introduction
**Airline** is an airline management system built on a microservices architecture, designed for scalability, security, and an efficient user experience. The system manages customers, employees, bookings, flights, and mileage, leveraging modern technologies and best practices in software development.

- **Frontend**: Built with Angular, using AuthGuard for route protection.  
- **Backend**: Composed of independent microservices developed with Java and Spring Boot, integrated via an API Gateway (Node.js) using API Composition to aggregate and expose data from various microservices.  
- **Messaging and Orchestration**: RabbitMQ is used for asynchronous communication and implementing the Saga pattern to manage distributed transactions.  
- **Data Storage**:
  - **MongoDB**: Stores authentication-related data, such as user credentials (customers and employees), enabling fast reads and writes.  
  - **PostgreSQL**: Each microservice has its own database (schema-per-service). A transactional database (CUD - normalized) manages operations related to bookings and flights, while a read-optimized database (R - denormalized) is used to enhance query performance, following the CQRS (Command Query Responsibility Segregation) pattern.  
  - **Redis**: Utilized for caching and managing compensatory states during Saga execution.  
- **Database Management**: Versioning of transactional databases is handled using Flyway.  
- **Design Pattern**: The architecture is based on the MVC (Model-View-Controller) pattern and supports CQRS.  
- **Security**: Implements secure authentication with user passwords hashed using Bcrypt.  
- **API Best Practices**: Consistent HTTP status codes and DTOs (Data Transfer Objects) for efficient communication between services.  

The system supports two user profiles, **customers** and **employees**, with detailed functionality tailored to each, prioritizing security, efficiency, and ease of use.

---

## Requirements

- **Docker**

---

## How to Run

### Clone the Repository

1. Clone this repository:

    ```bash
    git clone https://github.com/math-hrque/airline.git
    ```
2. Open a terminal and navigate to the directory where the project files are located.

### Run the Project

1. Using the Automated Shell Script:

   1.1. Grant execute permission to the script located in the project's root directory:
    
    ```bash
    chmod +x start.sh
    ```

   1.2. Run the script to build the images and start the services:
    
    ```bash
    ./start.sh
    ```

2. Using Docker Compose Directly:

   2.1. In the project's root directory, execute the following command to build the images and start the services:
    
    ```bash
    docker-compose up --build -d
    ```

---

## Access the Application

- **Frontend (Angular)**: Access the UI at [http://localhost:4200](http://localhost:4200).
- **API Gateway**: The gateway is available at [http://localhost:3015](http://localhost:3015).
- **Microservices**: Individual services are running on the following ports:
  - ms-auth: `8080`
  - ms-cliente: `8081`
  - ms-funcionario: `8082`
  - ms-reserva: `8083`
  - ms-voos: `8084`
  - Saga Orchestrator: `8085`

---

## Pre-Configured Users

To simplify testing, the system includes the following pre-configured users:

- **Customer**:  
  - Email: `leandro@gmail.com`  
  - Password: `pass`  

- **Employee**:  
  - Email: `matheus@gmail.com`  
  - Password: `pass`  

You can use these credentials to explore the system without creating new accounts.

---

## Email Configuration for Account Creation

The system sends a randomly generated 4-digit password to the user's email when they create an account. To enable this feature, you must configure the email settings in the `application.properties` file of the **ms-auth**. 

### Steps to Configure:

1. Open the `application.properties` file in the **ms-auth** directory.
2. Replace the placeholders in the email configuration section with your email credentials:

    ```properties
    spring.mail.username=your-email@gmail.com  # Replace with your email
    spring.mail.password=your-email-password   # Replace with your email password
    ```

4. Ensure that your email account allows less secure app access (or configure an app password if using Gmail with 2FA). Refer to your email provider's documentation for details.

5. Save the changes and restart the **ms-auth** to apply the new configuration.

---

## Access Supporting Services

- **RabbitMQ Management Dashboard**:  
  Access RabbitMQ's web UI at [http://localhost:15672](http://localhost:15672).  
  - **Default Credentials**:
    - Username: `rabbitmq`
    - Password: `rabbitmq`

- **MongoDB**:  
  MongoDB is running on port `27017`. You can connect using a MongoDB client like **MongoDB Compass** or the command line.  
  - **Connection String**:

    ```bash
    mongodb://mongo:mongo@localhost:27017/usuario?authSource=admin
    ```
  - **Default Credentials**:
    - Username: `mongo`
    - Password: `mongo`

- **PostgreSQL**:  
  PostgreSQL is running on port `5432`. You can connect using a PostgreSQL client like **pgAdmin** or the command line.  
  - **Connection Details**:
    - Host: `localhost`
    - Port: `5432`
    - Username: `postgres`
    - Password: `postgres`

---

## Additional Notes

- Logs: To check service logs, use:
  ```bash
  docker-compose logs -f
  ```

- Stop Services: To stop all running services, execute:
  ```bash
  docker-compose down
  ```

- Rebuild and Restart: If needed, rebuild and restart services:
  ```bash
  docker-compose up --build -d
  ```
