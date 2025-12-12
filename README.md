# Todo API

A minimal **Todo API** built with **Java 17**, **Spring Boot 3**, **OpenAPI 2.0**, **TOML config loader**, and structured under **Clean Architecture**.

---
## 📁 Project Structure

```
src/
 ├── main/
 │   ├── java/
 │   │   └── com/example/todo/     ← Root package
 │   │        ├── config/          ← TOML environment processor
 │   │        ├── controller/      ← REST interfaces
 │   │        ├── domain/          ← Business entities
 │   │        ├── dto/             ← Request/response models
 │   │        ├── exception/       ← Error handling
 │   │        ├── repository/      ← Repository implementation
 │   │        ├── usecase/         ← Application use cases
 │   │        └── TodoApplication  ← Spring Boot entrypoint
 │   └── resources/
 │        ├── confd_spring/        ← TOML configuration
 │        ├── META-INF/            ← Spring factories
 │        └── static/              ← Swagger UI & OpenAPI
 └── test/
      └── com/example/todo/        ← JUnit 5 tests
```
---
## How to Run

### **1. Clone the repository**

```bash
git clone https://github.com/jfsc/todo.git
cd todo
```

### **2. Build the project**

```bash
mvn clean package
```

### **3. Run the Spring Boot application**

```bash
mvn spring-boot:run
```

### **4. Access the API documentation**

Swagger UI:

```
http://localhost:8080/swagger/index.html
```
---

## Running Tests

### **Run all unit tests**

```bash
mvn test
```

### **Run with detailed output**

```bash
mvn -q -Dtest=*Test test
```

JUnit 5 tests include:

* Repository tests
* Service tests (optional)
* Controller tests via WebMvcTest (optional)

---

## Stack

* **Java 17**
* **Spring Boot 3**
* **OpenAPI 2.0 (Swagger)**
* **JUnit 5**
* **TOML config loader**
* **Maven**

---

## License

This project is licensed under the **MIT License**.

---

## Contributors

Eu fernandofrancafilho8@gmail.com estou commitando na branch feature_fernando_v2

Erros encontrados: problema no ID na parte do Controller, e tratamento de excessões na parte do GlobaExceptionHandler

Testes com acurácia acima de 80%, e boas práticas implementadas.

