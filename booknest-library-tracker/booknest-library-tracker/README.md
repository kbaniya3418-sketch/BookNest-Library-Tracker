# BookNest - Library Tracker REST API

A Spring Boot REST API for managing library **students** and the **books they borrow**, stored in MongoDB.

**Tech stack:** Java 17, Spring Boot 3.3.5, Spring Data MongoDB, Bean Validation, Maven, MongoDB, Postman.

## Project structure

```
src/main/java/com/booknest/library
├── LibraryApplication.java
├── controller   StudentController, BorrowingController, HealthController
├── service      StudentService, BorrowingService
├── repository   StudentRepository, BorrowingRepository
├── entity       Student, Borrowing, Department (enum)
├── dto          StudentSummary
└── exception    ResourceNotFoundException, DuplicateResourceException, GlobalExceptionHandler
```

## How to run

1. Start MongoDB (default `mongodb://localhost:27017`).
2. Open the folder in IntelliJ (or VS Code) as a Maven project and let it load.
3. Run `LibraryApplication`. The API starts on **http://localhost:8083**.
4. Open MongoDB Compass and connect to `localhost:27017`. Database `booknestdb` is created on the first insert.
5. Optional: load demo data with `load("sample-data.js")` in mongosh.
6. In Postman, import `BookNest.postman_collection.json`. Run **Create Student** first, then **Borrow Book**. The ids are saved into collection variables automatically.

To change the port or database, edit `src/main/resources/application.properties`.

## Collections

**students**: `id`, `name`, `email` (unique), `age` (10-90), `department` (SCIENCE / ARTS / COMMERCE / ENGINEERING), `joinDate`, `active`

**borrowings**: `id`, `studentId` (reference to a student), `bookTitle`, `author`, `genre`, `borrowDate`, `dueDate`, `returned`, `returnDate`, `fine`

Business rules: due date defaults to 14 days after the borrow date. Returning a late book charges Rs. 5 per overdue day.

## Endpoints

| Method | URL                                  | Description                                    |
| ------ | ------------------------------------ | ---------------------------------------------- |
| GET    | /health                              | Check that the API is running                  |
| POST   | /api/students                        | Create student (201)                           |
| GET    | /api/students                        | List all students                              |
| GET    | /api/students/{id}                   | Get one student (404 if missing)               |
| PUT    | /api/students/{id}                   | Update student                                 |
| DELETE | /api/students/{id}                   | Delete student and their borrowings (204)      |
| GET    | /api/students/search?name=xyz        | Search by name, case-insensitive               |
| GET    | /api/students/department/{dept}      | Filter by department                           |
| GET    | /api/students/{id}/summary           | Total borrowed, returned, pending, total fine  |
| POST   | /api/borrowings                      | Borrow a book (student must exist)             |
| GET    | /api/borrowings                      | List all borrowings                            |
| GET    | /api/borrowings/{id}                 | Get one borrowing                              |
| PUT    | /api/borrowings/{id}                 | Update borrowing                               |
| PUT    | /api/borrowings/{id}/return          | Return a book and calculate the fine           |
| DELETE | /api/borrowings/{id}                 | Delete borrowing                               |
| GET    | /api/borrowings/student/{studentId}  | All borrowings of a student                    |
| GET    | /api/borrowings/genre/{genre}        | Filter by genre                                |
| GET    | /api/borrowings/overdue              | Books not returned and past the due date       |

## Error responses

| Situation                                                  | Status                      |
| ---------------------------------------------------------- | --------------------------- |
| Validation fails (blank name, bad email, age out of range) | 400 with per-field messages |
| Invalid JSON or unknown department name                    | 400                         |
| Student or borrowing id not found                          | 404                         |
| Duplicate email                                            | 409                         |

## Viva notes (short answers)

- **Why layered architecture?** The controller handles HTTP, the service holds business rules, and the repository talks to the database.
- **Why constructor injection instead of `@Autowired` fields?** It makes dependencies explicit and easier to test.
- **How are students and borrowings related?** MongoDB has no joins, so a borrowing stores `studentId`, and the service checks that the student exists before saving.
- **What does `@Valid` do?** It triggers the validation annotations on the entity. Failures become a clean 400 response via `GlobalExceptionHandler`.
- **What does `MongoRepository` give us?** Ready-made CRUD methods, plus derived queries like `findByNameContainingIgnoreCase` from the method name alone.
- **Why does deleting a student also delete borrowings?** To avoid orphan records pointing to a missing student.
