# Testes do Repositório - `repository`

Este documento detalha os testes de unidade para a camada de persistência de dados da aplicação, especificamente para o `InMemoryTodoRepository`.

---

## 🔬 `InMemoryTodoRepositoryTest.java`

Esta é a suíte de testes para o `InMemoryTodoRepository`. O objetivo é validar todas as operações CRUD (Criar, Ler, Atualizar, Deletar) e garantir que o repositório se comporte como esperado em diversos cenários, incluindo casos de borda.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`shouldCreateTodo`**: Verifica se um novo `Todo` pode ser salvo e recuperado com sucesso.
*   **`shouldListTodos`**: Garante que a lista de todos os `Todo`s seja retornada corretamente.
*   **`shouldUpdateTodo`**: Testa a atualização de um `Todo` existente e confirma que as alterações foram persistidas.
*   **`shouldDeleteTodo`**: Assegura que um `Todo` pode ser removido com sucesso.
*   **`shouldReturnEmptyListWhenNoTodos`**: Confirma que uma lista vazia é retornada quando o repositório não contém nenhum `Todo`.
*   **`shouldReturnEmptyOptionalForNonExistentId`**: Verifica o comportamento ao buscar por um ID que não existe (deve retornar um `Optional` vazio).
*   **`shouldDoNothingWhenDeletingNonExistentId`**: Garante que nenhuma exceção ocorra ao tentar deletar um `Todo` com um ID que não existe.
*   **`shouldOverwriteTodoWithSameId`**: Testa se salvar um `Todo` com um ID já existente sobrescreve o registro original, mantendo a consistência dos dados.
