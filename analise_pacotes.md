# Análise dos Pacotes do Projeto `todo`

Este documento descreve a responsabilidade de cada pacote na aplicação.

## `com.example.todo.config`

**Propósito:** Configuração da aplicação.
**Arquivos:** `TomlEnvironmentPostProcessor.java`
**Análise:** Este pacote é responsável por carregar configurações de um arquivo TOML (`app_config.toml`) e injetá-las no ambiente do Spring. Isso explica a dependência `toml4j` encontrada no `pom.xml`, permitindo que a aplicação seja configurada por arquivos TOML externos.

## `com.example.todo.controller`

**Propósito:** Camada de API REST.
**Arquivos:** `TodoController.java`
**Análise:** Define os endpoints HTTP para as operações CRUD (Criar, Ler, Atualizar, Deletar) relacionadas a `Todo` items. Ele recebe as requisições, utiliza o `TodoService` para executar a lógica de negócio e retorna as respostas ao cliente.

## `com.example.todo.domain`

**Propósito:** Modelo de domínio principal.
**Arquivos:** `Todo.java`
**Análise:** Contém a classe `Todo`, que é a entidade principal da aplicação. Ela modela um item de tarefa com seus atributos, como `id`, `title`, `description` e `done`.

## `com.example.todo.dto`

**Propósito:** Objetos de Transferência de Dados (Data Transfer Objects).
**Arquivos:** `TodoRequest.java`, `TodoResponse.java`
**Análise:** Estes objetos são usados para transportar dados entre o cliente e o servidor. `TodoRequest` carrega os dados para criar ou atualizar um `Todo` e inclui validações. `TodoResponse` formata os dados de um `Todo` que serão enviados de volta ao cliente.

## `com.example.todo.exception`

**Propósito:** Manipulação global de exceções.
**Arquivos:** `GlobalExceptionHandler.java`
**Análise:** Centraliza o tratamento de erros da aplicação. Atualmente, captura `RuntimeException`, formata uma mensagem de erro padronizada e retorna um status HTTP 404 (Not Found), garantindo que os erros sejam tratados de forma consistente.

## `com.example.todo.repository`

**Propósito:** Camada de persistência de dados.
**Arquivos:** `InMemoryTodoRepository.java`
**Análise:** Implementa a lógica de armazenamento de dados. Conforme a investigação inicial, utiliza um `ConcurrentHashMap` para guardar os `Todo` items em memória, servindo como uma base de dados temporária.

## `com.example.todo.usecase`

**Propósito:** Camada de lógica de negócio (serviços).
**Arquivos:** `TodoService.java`
**Análise:** Orquestra as operações da aplicação. Atua como um intermediário entre o `TodoController` e o `InMemoryTodoRepository`, implementando as regras de negócio para criar, buscar, atualizar e deletar `Todo` items.
