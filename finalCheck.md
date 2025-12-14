## Review
First, Well done!!!!!. All good. However, you could review Portuguese entries in the future:
```java
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    // Simula o repositório, monitorando chamadas, mas não executando o código real dele.
    @Mock
    private InMemoryTodoRepository repository;

    // Injeta o mock do repository na instância de TodoService
    @InjectMocks
    private TodoService service;

// --- Testes de Leitura (Read/Find/List) ---
```



