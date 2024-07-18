package com.example.todoapp.todo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo();
        todo.setId(123L);
        todo.setTitle("title");
        todo.setCompleted(false);
    }

    @Test
    void testCreateTodo() throws Exception {
        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);

        mockMvc.perform(post("/todo")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(todo.getId().intValue())))
                .andExpect(jsonPath("$.title", is(todo.getTitle())))
                .andExpect(jsonPath("$.completed", is(todo.isCompleted())));
    }
    @Test
    void testGetAllTodos() throws Exception {
        List<Todo> todos = Arrays.asList(todo);
        when(todoService.getTodos()).thenReturn(todos);

        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(todos.size())))
                .andExpect(jsonPath("$[0].id", is(todo.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(todo.getTitle())))
                .andExpect(jsonPath("$[0].completed", is(todo.isCompleted())));
    }

    @Test
    void testGetTodo() throws Exception {
        when(todoService.getTodo(anyLong())).thenReturn(todo);

        mockMvc.perform(get("/todo/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(todo.getId().intValue())))
                .andExpect(jsonPath("$.title", is(todo.getTitle())))
                .andExpect(jsonPath("$.completed", is(todo.isCompleted())));
    }

    @Test
    void testDeleteTodo() throws Exception {
        when(todoService.deleteTodo(anyLong())).thenReturn("todo deleted");

        mockMvc.perform(delete("/todo/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("todo deleted"));
    }
    @Test
    void testUpdateTodo() throws Exception {
        Todo updatedTodo = new Todo();
        updatedTodo.setId(123L);
        updatedTodo.setTitle("title1");
        updatedTodo.setCompleted(true);

        when(todoService.updatedTodo(anyLong(), any(Todo.class))).thenReturn(updatedTodo);

        mockMvc.perform(put("/todo/{id}", updatedTodo.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedTodo.getId().intValue())))
                .andExpect(jsonPath("$.title", is(updatedTodo.getTitle())))
                .andExpect(jsonPath("$.completed", is(updatedTodo.isCompleted())));
    }

}