package com.example.todoapp.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodo(Long id) {
        return todoRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found id"));
    }

    public String deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found id"));
        todoRepository.delete(todo);
        return "todo deleted";
    }

    public Todo updatedTodo(Long id, Todo todo) {
        Todo todo1 =todoRepository.findById(id).orElseThrow(()-> new RuntimeException("not found id"));
        todo1.setTitle(todo.getTitle());
        todo1.setCompleted(todo.isCompleted());
        return todoRepository.save(todo1);
    }
}
