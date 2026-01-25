# Testes do Pacote - `exception`

Este documento detalha os testes de unidade para o pacote `com.example.todo.exception`, que contém classes para manipulação global de exceções na aplicação.

---

## 🔬 `GlobalExceptionHandlerTest.java`

Esta suíte de testes valida o comportamento do `GlobalExceptionHandler`, assegurando que ele captura e processa exceções de tempo de execução (`RuntimeException`) de maneira consistente, retornando respostas HTTP formatadas adequadamente.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`handleRuntimeShouldReturnNotFoundStatus`**: Verifica se o método `handleRuntime` do handler retorna o status HTTP `404 NOT FOUND` ao processar uma `RuntimeException`.
*   **`handleRuntimeShouldReturnCorrectBodyStructure`**: Garante que o corpo da resposta HTTP, em caso de exceção, tenha a estrutura esperada, incluindo campos como `type`, `title`, `status` e `timestamp`.
*   **`handleRuntimeShouldUseExceptionMessageAsTitle`**: Confirma que a mensagem da `RuntimeException` é utilizada corretamente como o "title" no corpo da resposta HTTP.
