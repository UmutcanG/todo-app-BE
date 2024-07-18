package com.example.todoapp.todo;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {
    private TodoService todoService;
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository = Mockito.mock(TodoRepository.class);
        todoService = new TodoService(todoRepository);
    }
    @Test
    void testCreateTodo() {

        Todo todo = new Todo();
        todo.setId(123L);
        todo.setTitle("title");
        todo.setCompleted(false);
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo result = todoService.createTodo(todo);

        Assert.assertEquals(todo.getId(), result.getId());
        Assert.assertEquals(todo.getTitle(), result.getTitle());
        Assert.assertEquals(todo.isCompleted(), result.isCompleted());
    }

    @Test
    void testGetTodos() {
        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("todo1");
        todo1.setCompleted(false);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("todo2");
        todo2.setCompleted(true);

        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        List<Todo> todos = todoService.getTodos();

        assertNotNull(todos);
        assertEquals(2, todos.size());

        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testGetTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");
        todo.setCompleted(false);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo foundTodo = todoService.getTodo(1L);

        assertNotNull(foundTodo);
        assertEquals(1L, foundTodo.getId());
        assertEquals("Test Todo", foundTodo.getTitle());
        assertFalse(foundTodo.isCompleted());

        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");
        todo.setCompleted(false);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        String result = todoService.deleteTodo(1L);

        assertEquals("todo deleted", result);

        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).delete(todo);
    }
    @Test
    void testUpdateTodo() {
        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setTitle("todo");
        existingTodo.setCompleted(false);

        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("u-todo");
        updatedTodo.setCompleted(true);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        Todo result = todoService.updatedTodo(1L, updatedTodo);

        assertNotNull(result);
        assertEquals("u-todo", result.getTitle());
        assertTrue(result.isCompleted());

        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    @Test
    void testUpdateTodoNotFound() {
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("todo");
        updatedTodo.setCompleted(true);

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            todoService.updatedTodo(1L, updatedTodo);
        });

        assertEquals("not found id", exception.getMessage());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(0)).save(any(Todo.class));
    }
}