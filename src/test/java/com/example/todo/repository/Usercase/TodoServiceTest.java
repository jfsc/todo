package com.example.todo.repository.Usercase;

import com.example.todo.domain.Todo;
import com.example.todo.repository.InMemoryTodoRepository;
import com.example.todo.usecase.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    // Simula o repositório, monitorando chamadas, mas não executando o código real dele.
    @Mock
    private InMemoryTodoRepository repository;

    // Injeta o mock do repository na instância de TodoService
    @InjectMocks
    private TodoService service;

    // --- Testes de Leitura (Read/Find/List) ---

    @Test
    void shouldFindExistingTodo() {
        UUID id = UUID.randomUUID();
        Todo existing = new Todo(id, "Existing", "Desc", false);

        // Define o comportamento: quando findById for chamado com 'id', retorne 'existing'
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        Optional<Todo> result = service.find(id);

        assertTrue(result.isPresent());
        assertEquals("Existing", result.get().getTitle());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenTodoNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        // Define o comportamento: quando findById for chamado, retorne Optional.empty()
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Todo> result = service.find(nonExistentId);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    void shouldListAllTodos() {
        Todo todo1 = new Todo(UUID.randomUUID(), "T1", "D1", false);
        Todo todo2 = new Todo(UUID.randomUUID(), "T2", "D2", true);
        List<Todo> mockList = Arrays.asList(todo1, todo2);

        // Define o comportamento: quando findAll for chamado, retorne a lista mockada
        when(repository.findAll()).thenReturn(mockList);

        List<Todo> result = service.list();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    // --- Teste de Criação (Create) ---

    @Test
    void shouldCreateTodoAndReturnSavedInstance() {
        Todo newTodo = new Todo(null, "New", "Desc", false);
        Todo savedTodo = new Todo(UUID.randomUUID(), "New", "Desc", false);

        // Define o comportamento: quando save for chamado com qualquer Todo, retorne o savedTodo
        when(repository.save(any(Todo.class))).thenReturn(savedTodo);

        Todo created = service.create(newTodo);

        assertNotNull(created.getId());
        assertEquals("New", created.getTitle());
        verify(repository, times(1)).save(newTodo);
    }

    // --- Testes de Atualização (Update) ---

    @Test
    void shouldUpdateExistingTodoSuccessfully() {
        UUID id = UUID.randomUUID();
        // 1. O item existente que o repositório irá "encontrar"
        Todo existing = new Todo(id, "Old Title", "Old Desc", false);
        // 2. O objeto com os novos dados
        Todo updateData = new Todo(null, "New Title", "New Desc", true);

        // Mock 1: Encontrar o item existente
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        // Mock 2: Simular o salvamento (retornamos o 'existing' porque o service o modifica)
        when(repository.save(existing)).thenReturn(existing);

        Todo updated = service.update(id, updateData);

        // Verifica se a lógica de atualização do Service foi aplicada ao objeto 'existing'
        assertEquals("New Title", updated.getTitle());
        assertTrue(updated.isDone());
        assertEquals(id, updated.getId()); // O ID deve ser mantido

        // Cobre a linha 'return repo.save(existing);'
        verify(repository, times(1)).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenTodoNotFoundOnUpdate() {
        UUID nonExistentId = UUID.randomUUID();
        Todo updateData = new Todo(null, "Title", "Desc", false);

        // Mock: Repositório não encontra
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Cobre a branch .orElseThrow(() -> new RuntimeException("Not found"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(nonExistentId, updateData);
        });

        assertEquals("Not found", exception.getMessage());
        verify(repository, never()).save(any(Todo.class)); // O save nunca deve ser chamado
    }

    // --- Testes de Exclusão (Delete) ---

    @Test
    void shouldDeleteExistingTodoSuccessfully() {
        UUID existingId = UUID.randomUUID();
        Todo existing = new Todo(existingId, "Test", "Desc", false);

        // Mock: Repositório encontra, permitindo a exclusão
        when(repository.findById(existingId)).thenReturn(Optional.of(existing));

        service.delete(existingId);

        // Verifica se o findById foi chamado para checar a existência
        verify(repository, times(1)).findById(existingId);
        // Verifica se o deleteById foi chamado para remover
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    void shouldThrowExceptionWhenTodoNotFoundOnDelete() {
        UUID nonExistentId = UUID.randomUUID();

        // Mock: Repositório não encontra, acionando a exceção
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Cobre a branch .orElseThrow(...) dentro do delete
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.delete(nonExistentId);
        });

        assertEquals("Not found", exception.getMessage());
        // Garante que o método de exclusão não foi chamado, pois o item não foi encontrado
        verify(repository, never()).deleteById(any(UUID.class));
    }
}