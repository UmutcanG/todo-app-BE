package com.example.todoapp.todo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title cannot be blank")
    @Size(max = 75, message = "Title must be less than 75 characters")
    private String title;

    private boolean completed;
}
