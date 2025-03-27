# Event Driven Microservices

This project demonstrates an event-driven microservices architecture using Axon Framework and Spring Boot. It consists of multiple microservices, each responsible for handling specific domains like Customers, Cards, Loans, and Profiles. The services communicate asynchronously through events.

## Project Structure

### Microservices

1. **Customer Service**

   - Handles customer-related operations such as creation, updates, and deletions.
   - Processes events like `CustomerCreatedEvent`, `CustomerUpdatedEvent`, and `CustomerDeletedEvent`.

2. **Card Service**

   - Manages card-related operations such as creation, updates, and deletions.
   - Processes events like `CardCreatedEvent`, `CardUpdatedEvent`, and `CardDeletedEvent`.

3. **Loan Service**

   - Handles loan-related operations such as creation, updates, and deletions.
   - Processes events like `LoanCreatedEvent`, `LoanUpdatedEvent`, and `LoanDeletedEvent`.

4. **Profile Service**
   - Aggregates data from other services to provide a unified view of customer profiles.
   - Processes events like `CustomerDataChangedEvent`, `AccountDataChangedEvent`, `LoanDataChangeEvent`, and `CardDataChangeEvent`.

### Key Features

- **Event Handling**: Each service uses Axon Framework's `@EventHandler` to process domain events.
- **CQRS**: Command Query Responsibility Segregation is implemented to separate write and read operations.
- **Spring Boot**: Provides a robust and scalable foundation for the microservices.

## Technologies Used

- **Java**: Programming language for the microservices.
- **Spring Boot**: Framework for building microservices.
- **Axon Framework**: For event sourcing and CQRS.
- **Lombok**: Reduces boilerplate code.
- **BeanUtils**: Simplifies object property copying.

## How to Run

1. Clone the repository.
2. Navigate to each microservice directory and run:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Each service will start on its configured port.

## Event Flow

1. Events are published by one service and consumed by others.
2. For example:
   - A `CustomerCreatedEvent` is published by the Customer Service.
   - The Profile Service listens to this event and updates the profile data.

## Contribution

Feel free to fork the repository and submit pull requests for improvements or bug fixes.
