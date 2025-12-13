## Review
First, Well done!!!!!. All good. However, you could review Portuguese entries in the future:
```java
src/main/java/com/example/todo/controller/TodoController.java
  //corrigido bug aqui <----- look at this
    @GetMapping("/{id}")
    ....
   
```
also 

```java
Todo todo = new Todo(UUID.randomUUID(), "Test", "teste de criacao", false);  <----- look at this
```