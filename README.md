# Library Management REST API

A Spring Boot backend application for managing books, members, and borrowing operations in a library.

## Technologies

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker Compose
- Swagger UI
- JUnit
- Mockito

## Features

### Books
- Create books with automatically generated IDs
- Update book information
- Search books by title
- Add physical copies
- Remove available copies safely
- View books sorted by ID
- Prevent deleting books with borrowing history

### Members
- Create members with automatically generated IDs
- Update member information
- Normalize emails to lowercase
- Prevent duplicate emails
- View members sorted by ID
- Prevent deleting members with borrowing history

### Borrowings
- Borrow available books
- Return borrowed books
- Prevent borrowing when no copies are available
- Prevent returning the same borrowing twice
- View all borrowings
- View active borrowings
- View borrowing history for a specific member

## Run the Database

Start PostgreSQL using Docker Compose:

```bash
docker compose up -d