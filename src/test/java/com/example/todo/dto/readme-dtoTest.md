# Testes do Pacote - `dto`

Este documento detalha os testes de unidade para o pacote `com.example.todo.dto`, que contém os Objetos de Transferência de Dados (Data Transfer Objects) da aplicação.

---

## 🔬 `TodoDtoTest.java`

Esta suíte de testes valida o comportamento e a integridade dos DTOs `TodoRequest` e `TodoResponse`. O objetivo é garantir que a conversão de e para o modelo de domínio (`Todo`) funcione corretamente e que as regras de validação sejam aplicadas.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`shouldConvertTodoRequestToDomain`**: Garante que um objeto `TodoRequest` seja convertido corretamente para um objeto de domínio `Todo`.
*   **`shouldConvertDomainToTodoResponse`**: Assegura que um objeto de domínio `Todo` seja convertido com precisão para um objeto `TodoResponse`.
*   **`whenTodoRequestTitleIsBlank_thenValidationFails`**: Testa a regra de validação (`@NotBlank`) para garantir que um `TodoRequest` com título em branco seja inválido.
*   **`whenTodoRequestTitleIsNull_thenValidationFails`**: Testa a validação para garantir que um `TodoRequest` com título nulo seja considerado inválido.
*   **`whenTodoRequestTitleIsValid_thenValidationSucceeds`**: Confirma que um `TodoRequest` com um título válido passa na validação sem erros.

Com a implementação destes testes, a cobertura do pacote `dto` foi de **0% para 90%**.
