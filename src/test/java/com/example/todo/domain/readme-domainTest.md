# Testes do Pacote - `domain`

Este documento detalha os testes de unidade para o pacote `com.example.todo.domain`, que contém a classe `Todo` - o modelo de domínio principal da aplicação.

---

## 🔬 `TodoTest.java`

Esta suíte de testes valida a correta inicialização e manipulação dos atributos da entidade `Todo`. O objetivo é garantir que os construtores funcionem como esperado e que os métodos getters e setters acessem e modifiquem os dados de forma consistente.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`defaultConstructorShouldInitializeWithRandomIdAndDefaults`**: Verifica se o construtor padrão inicializa um `Todo` com um ID aleatório e valores padrão para os outros atributos.
*   **`constructorWithTitleAndDescriptionShouldInitializeCorrectly`**: Garante que o construtor que recebe `title` e `description` inicialize o `Todo` corretamente, mantendo os valores padrão para os demais campos.
*   **`fullConstructorShouldInitializeAllFields`**: Assegura que o construtor completo inicialize todos os campos de um `Todo` com os valores fornecidos.
*   **`constructorWithIdTitleAndDoneShouldInitializeCorrectly`**: Testa a inicialização de um `Todo` com `id`, `title` e `done`, verificando se a descrição é definida como vazia por padrão.
*   **`gettersAndSettersShouldWorkCorrectly`**: Confirma que todos os métodos `get` e `set` de `Todo` funcionam corretamente, permitindo a leitura e modificação dos atributos.
