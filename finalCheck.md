First, test and bufixes checked. Well done!!!!!.  All good. However, you could review Portuguese entries in the future:
```java
#TodoRequestTest.java
void shouldValidateTitleNotBlank() {
    TodoRequest req = new TodoRequest();
    req.setTitle(""); // inválido (NotBlank)
   
```