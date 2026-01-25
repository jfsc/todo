# Testes do Pacote - `controller`

Este documento detalha os testes de unidade para o pacote `com.example.todo.controller`, que contém os controladores REST da aplicação e expõe os endpoints da API.

---

## 🔬 `TodoControllerTest.java`

Esta suíte de testes utiliza o Spring `MockMvc` para simular requisições HTTP aos endpoints do `TodoController`, isolando a camada da web através de `MockBean` para o `TodoService`. O objetivo é verificar se o controlador responde corretamente às requisições, mapeia as URLs adequadamente e interage com o serviço de forma esperada.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`shouldFetchAllTodos`**: Verifica se o endpoint `GET /api/todos` retorna uma lista de todos os `Todo`s e o status HTTP `200 OK`.
*   **`shouldCreateNewTodo`**: Garante que o endpoint `POST /api/todos` cria um novo `Todo` com sucesso, retornando o status HTTP `201 Created`.
*   **`shouldGetTodoById`**: Assegura que o endpoint `GET /api/todos/{id}` retorna um `Todo` específico quando o ID é válido.
*   **`shouldReturnNotFoundForInvalidTodoId`**: Testa se o endpoint `GET /api/todos/{id}` retorna status HTTP `404 Not Found` quando o ID do `Todo` não existe.
*   **`shouldUpdateTodo`**: Confirma que o endpoint `PUT /api/todos/{id}` atualiza um `Todo` existente e retorna o status HTTP `200 OK`.
*   **`shouldDeleteTodo`**: Verifica se o endpoint `DELETE /api/todos/{id}` remove um `Todo` com sucesso, retornando o status HTTP `204 No Content`.
