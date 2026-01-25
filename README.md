# Todo API - Projeto todo-openapi-spring3

Uma API **Todo** minimalista construída com **Java 17**, **Spring Boot 3**, **OpenAPI 2.0** e um loader de configuração customizado para `TOML`. O projeto é estruturado sob os princípios da **Clean Architecture** e possui uma alta cobertura de testes (96%), garantindo robustez e manutenibilidade.

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
      └── com/example/todo/        ← Testes de Unidade e Integração (JUnit 5)
```

---

## ✨ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **OpenAPI 2.0 (Swagger)**
- **Maven**
- **JUnit 5, Mockito & JaCoCo** para testes e cobertura.
- **TOML** para configuração.

---

## 🚀 Como Rodar o Projeto

### 1. Pré-requisitos

- JDK 17 ou superior.
- Apache Maven 3.8 ou superior.

### 2. Construindo o Projeto

Navegue até a pasta `todo` (onde o `pom.xml` está localizado) e execute o comando Maven para construir o projeto e baixar as dependências:

```bash
mvn clean package
```

### 3. Executando a Aplicação

Após a construção, você pode iniciar a aplicação com o seguinte comando:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080` por padrão.

### 4. Acessando a Documentação da API (Swagger UI)

Com a aplicação em execução, você pode acessar a documentação interativa da API (Swagger UI) através do seu navegador:

[http://localhost:8080/swagger/index.html](http://localhost:8080/swagger/index.html)

---

## ✅ Qualidade e Cobertura de Testes

O projeto possui uma suíte de testes robusta que garante a qualidade e o comportamento esperado de cada camada da aplicação.

- **Cobertura de Testes:** **96%** (medido com JaCoCo).
- **Frameworks:** JUnit 5, Mockito, e Spring Boot Test.

### Executando os Testes e Gerando Relatórios

Para executar todos os testes e gerar os relatórios de cobertura, utilize o seguinte comando na pasta `todo`:

```bash
mvn test site
```

Após a execução, o relatório de cobertura detalhado estará disponível em:
`target/site/jacoco/index.html`

Para documentação específica sobre os testes de cada pacote, consulte os arquivos `readme-[pacote]Test.md` localizados dentro dos respectivos diretórios em `src/test/java/com/example/todo/`.

---

## Endpoints da API

A API `Todo` oferece os seguintes endpoints:

### `GET /api/todos`

- **Descrição:** Retorna uma lista com todas as tarefas existentes.
- **Resposta (Exemplo):**
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

- **Descrição:** Cria uma nova tarefa.
- **Corpo da Requisição (TodoRequest - Exemplo):**
  ```json
  {
    "title": "Pagar Contas",
    "description": "Contas de água, luz e internet",
    "done": false
  }
  ```
- **Resposta (TodoResponse - Exemplo - Status 201 Created):**
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

- **Descrição:** Retorna uma tarefa específica pelo seu ID.
- **Parâmetro de Path:** `{id}` (UUID da tarefa)
- **Resposta (TodoResponse - Exemplo):**
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
- **Erro (Status 404 Not Found):** Se o ID não for encontrado, retornará um erro.

### `PUT /api/todos/{id}`

- **Descrição:** Atualiza uma tarefa existente pelo seu ID.
- **Parâmetro de Path:** `{id}` (UUID da tarefa)
- **Corpo da Requisição (TodoRequest - Exemplo):**
  ```json
  {
    "title": "Pagar Contas Vencidas",
    "description": "Revisar todas as contas",
    "done": true
  }
  ```
- **Resposta (TodoResponse - Exemplo):**
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

- **Descrição:** Deleta uma tarefa existente pelo seu ID.
- **Parâmetro de Path:** `{id}` (UUID da tarefa)
- **Resposta:** Status 204 No Content em caso de sucesso.

---

## Licença

Este projeto está licenciado sob a **MIT License**.

---
