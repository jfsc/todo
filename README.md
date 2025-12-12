# Todo API - Projeto todo-openapi-spring3

Uma API **Todo** minimalista construída com **Java 17**, **Spring Boot 3**, **OpenAPI 2.0**, **TOML config loader**, e estruturada sob os princípios da **Clean Architecture**.

---
## 📁 Estrutura do Projeto

```
src/
 ├── main/
 │   ├── java/
 │   │   └── com/example/todo/     ← Pacote Raiz da Aplicação
 │   │        ├── config/          ← Processador de ambiente TOML
 │   │        ├── controller/      ← Interfaces REST (Endpoints da API)
 │   │        ├── domain/          ← Entidades de Negócio (Modelo de Dados)
 │   │        ├── dto/             ← Modelos de Requisição/Resposta (Data Transfer Objects)
 │   │        ├── exception/       ← Tratamento de Exceções
 │   │        ├── repository/      ← Implementação do Repositório (armazenamento em memória)
 │   │        ├── usecase/         ← Casos de Uso da Aplicação (Lógica de Negócio)
 │   │        └── TodoApplication  ← Ponto de entrada da aplicação Spring Boot
 │   └── resources/
 │        ├── confd_spring/        ← Configuração TOML
 │        ├── META-INF/            ← Fábricas Spring
 │        └── static/              ← Interface do Swagger UI e Definição OpenAPI
 └── test/
      └── com/example/todo/        ← Testes JUnit 5 (incluindo testes de repositório)
```
---
## Tecnologias Utilizadas

*   **Java 17**
*   **Spring Boot 3**
*   **OpenAPI 2.0 (Swagger)**
*   **JUnit 5**
*   **TOML config loader**
*   **Maven**

---

## Como Rodar o Projeto

### **1. Clonar o Repositório**

```bash
git clone <URL_DO_REPOSITORIO>
cd todo
```
*(Se você já está dentro da pasta `todo`, este passo pode ser ignorado ou adaptado.)*

### **2. Construir o Projeto**

Navegue até a pasta `todo` (onde o `pom.xml` está localizado) e execute o comando Maven para construir o projeto e baixar as dependências:

```bash
mvn clean package
```

### **3. Executar a Aplicação Spring Boot**

Após a construção, você pode iniciar a aplicação com o seguinte comando:

```bash
mvn spring-boot:run
```
A aplicação estará disponível em `http://localhost:8080` por padrão.

### **4. Acessar a Documentação da API (Swagger UI)**

Com a aplicação em execução, você pode acessar a documentação interativa da API (Swagger UI) através do seu navegador:

```
http://localhost:8080/swagger/index.html
```

---

## Endpoints da API

A API `Todo` oferece os seguintes endpoints:

### `GET /api/todos`

*   **Descrição:** Retorna uma lista com todas as tarefas existentes.
*   **Resposta (Exemplo):**
    ```json
    [
        {
            "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
            "title": "Comprar Leite",
            "description": "Leite integral e desnatado",
            "done": false,
            "createdAt": "2023-10-27T10:00:00Z",
            "updatedAt": "2023-10-27T10:00:00Z"
        }
    ]
    ```

### `POST /api/todos`

*   **Descrição:** Cria uma nova tarefa.
*   **Corpo da Requisição (TodoRequest - Exemplo):**
    ```json
    {
        "title": "Pagar Contas",
        "description": "Contas de água, luz e internet",
        "done": false
    }
    ```
*   **Resposta (TodoResponse - Exemplo - Status 201 Created):**
    ```json
    {
        "id": "b1c2d3e4-f5a6-7890-1234-567890abcdef",
        "title": "Pagar Contas",
        "description": "Contas de água, luz e internet",
        "done": false,
        "createdAt": "2023-10-27T10:30:00Z",
        "updatedAt": "2023-10-27T10:30:00Z"
    }
    ```

### `GET /api/todos/{id}`

*   **Descrição:** Retorna uma tarefa específica pelo seu ID.
*   **Parâmetro de Path:** `{id}` (UUID da tarefa)
*   **Resposta (TodoResponse - Exemplo):**
    ```json
    {
        "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
        "title": "Comprar Leite",
        "description": "Leite integral e desnatado",
        "done": false,
        "createdAt": "2023-10-27T10:00:00Z",
        "updatedAt": "2023-10-27T10:00:00Z"
    }
    ```
*   **Erro (Status 404 Not Found):** Se o ID não for encontrado, retornará um erro.

### `PUT /api/todos/{id}`

*   **Descrição:** Atualiza uma tarefa existente pelo seu ID.
*   **Parâmetro de Path:** `{id}` (UUID da tarefa)
*   **Corpo da Requisição (TodoRequest - Exemplo):**
    ```json
    {
        "title": "Pagar Contas Vencidas",
        "description": "Revisar todas as contas",
        "done": true
    }
    ```
*   **Resposta (TodoResponse - Exemplo):**
    ```json
    {
        "id": "b1c2d3e4-f5a6-7890-1234-567890abcdef",
        "title": "Pagar Contas Vencidas",
        "description": "Revisar todas as contas",
        "done": true,
        "createdAt": "2023-10-27T10:30:00Z",
        "updatedAt": "2023-10-27T11:00:00Z"
    }
    ```

### `DELETE /api/todos/{id}`

*   **Descrição:** Deleta uma tarefa existente pelo seu ID.
*   **Parâmetro de Path:** `{id}` (UUID da tarefa)
*   **Resposta:** Status 204 No Content em caso de sucesso.

---

## Rodando Testes

Para informações detalhadas sobre como rodar os testes e a explicação de cada teste unitário implementado, consulte o arquivo `README_TESTES.md` dentro desta mesma pasta:

[**Acesse a documentação de testes do projeto `todo` aqui.**](./README_TESTES.md)

---

## Licença

Este projeto está licenciado sob a **MIT License**.

---

## Contribuidores

*   gigliarly.gonzaga@outlook.com
*   aldocandeia000@gmail.com
*   jonnattanfarias@gmail.com
*   giordanniformiga103@gmail.com
*   cauanynunes00@gmail.com
*   gabrielseixas1@gmail.com
*   hbezerradev@gmail.com
*   larry_diego@hotmail.com
*   brunacsnn@gmail.com
*   samuel.marcos.smh@gmail.com
*   viniciusleal952@gmail.com
*   leonardoag1506@gmail.com