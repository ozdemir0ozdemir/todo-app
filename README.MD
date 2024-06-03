# To-Do App Backend

## Introduction
This is a simple RESTful API for a To-Do application backend. It provides endpoints for managing tasks such as creating, reading, updating, and deleting tasks.

## Technologies Used
- Spring Boot
- Spring Boot Data JPA
- Spring Boot Security
- JWT
- Lombok
- Jakarta Validation API
- H2 Database
- JUnit
- Mockito
- Thymeleaf

## API Endpoints

### 1. Create a New Task
- **URL:** `/tasks`
- **Method:** `POST`
- **Request Body:** `CreateNewTaskRequest`
- **Response Body:** `TaskResponse`
- **Description:** Creates a new task.

### 2. Get All Tasks
- **URL:** `/tasks`
- **Method:** `GET`
- **Request Parameters:**
    - `pageNumber` (optional, default: 0): The page number to retrieve.
    - `pageSize` (optional, default: 10): The number of tasks per page.
- **Response Body:** `TaskPageResponse`
- **Description:** Retrieves all tasks paginated.

### 3. Get Task by ID
- **URL:** `/tasks/{taskId}`
- **Method:** `GET`
- **Path Variable:** `taskId`: The ID of the task to retrieve.
- **Response Body:** `TaskResponse`
- **Description:** Retrieves a task by its ID.

### 4. Update Task by ID
- **URL:** `/tasks/{taskId}`
- **Method:** `PUT`
- **Path Variable:** `taskId`: The ID of the task to update.
- **Request Body:** `UpdateTaskRequest`
- **Response Body:** `TaskResponse`
- **Description:** Updates a task by its ID.

### 5. Update Task Completion by ID
- **URL:** `/tasks/{taskId}`
- **Method:** `PATCH`
- **Path Variable:** `taskId`: The ID of the task to update.
- **Request Body:** `UpdateTaskCompletionRequest`
- **Response Body:** `TaskResponse`
- **Description:** Updates the completion status of a task by its ID.

### 6. Delete Task by ID
- **URL:** `/tasks/{taskId}`
- **Method:** `DELETE`
- **Path Variable:** `taskId`: The ID of the task to delete.
- **Response Body:** `TaskResponse`
- **Description:** Deletes a task by its ID.

## Author
- Özdemir Özdemir

## Note
- The API follows RESTful conventions for resource naming and HTTP methods.
- Error handling and validation are implemented using Jakarta Validation API.
- Lombok is used for reducing boilerplate code.
