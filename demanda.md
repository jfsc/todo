# Todo

- [] Objetivo: Ajustar as cobertura de teste para 80% ou mais
- [] Encontrar 2 bugs do projeto
    - E resolver os bugs encontrados
- [] Documentar quais sao os bugs encontrados
    - Como resolveu
    - descricao do bug

# Bugs encontrados

O parametro das requisicoes nao esta sendo recebida corretamente pelo o `controller` (referente ao `UUID id`)
Bug relacionado ao recebimento do ID nas funcoes

Ate entao os bugs estao se resolvendo adicionando mais um especificacao ao parametro, ou seja criando redundancia.
Com `...(@PathVariable**("id")** UUID id){...`, sendo adicionado *("id")* ao lado da anotacao.
