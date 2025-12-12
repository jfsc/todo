# Testes do Pacote - `config`

Este documento detalha os testes de unidade para o pacote `com.example.todo.config`, que contém a lógica para processamento de configurações customizadas da aplicação.

---

## 🔬 `TomlEnvironmentPostProcessorTest.java`

Esta suíte de testes valida o comportamento da classe `TomlEnvironmentPostProcessor`. Para permitir o teste de unidade e isolar o teste do sistema de arquivos, a classe de produção foi levemente refatorada, extraindo a criação do `Resource` para um novo método `protected`.

O teste então sobrescreve este método para injetar um `Resource` mockado, permitindo a simulação de diferentes cenários.

---

## ✅ Cenários de Teste Cobertos

Abaixo está a lista de cenários de teste implementados:

*   **`shouldLoadTomlAndAddPropertySource`**: Garante que, quando um arquivo de configuração TOML válido existe, ele é lido e suas propriedades são adicionadas corretamente ao ambiente do Spring.
*   **`shouldDoNothingWhenResourceDoesNotExist`**: Verifica se o processador não executa nenhuma ação (e não lança erro) quando o arquivo de configuração TOML não é encontrado.
*   **`shouldHandleIOExceptionGracefully`**: Assegura que, se ocorrer um erro de I/O ao ler o arquivo, a exceção é capturada e tratada adequadamente, sem interromper a inicialização da aplicação.
