# Testes do Pacote - `usecase`

Este documento detalha os testes de unidade para o pacote `com.example.todo.usecase`, que contém a lógica de negócio principal da aplicação, orquestrada pela classe `TodoService`.

---

## 🔬 `TodoServiceTest.java`

Esta suíte de testes valida o comportamento da classe `TodoService`. Utilizando o Mockito, o `InMemoryTodoRepository` é mockado (simulado) para isolar o serviço, garantindo que estamos testando apenas a lógica de negócio do `TodoService`, e não a camada de persistência.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`shouldCreateTodo`**: Verifica se o método `create` chama o método `save` do repositório.
*   **`shouldFindTodoById`**: Garante que o método `find` retorna corretamente um `Todo` quando ele existe no repositório.
*   **`shouldListAllTodos`**: Assegura que o método `list` retorna a lista completa de `Todo`s do repositório.
*   **`shouldUpdateTodo`**: Testa a lógica de atualização, garantindo que um `Todo` existente é encontrado, modificado e salvo.
*   **`shouldThrowExceptionWhenUpdatingNonExistentTodo`**: Confirma que uma `RuntimeException` é lançada ao tentar atualizar um `Todo` que não existe.
*   **`shouldDeleteTodo`**: Verifica se o método `delete` invoca corretamente o método `deleteById` do repositório.
