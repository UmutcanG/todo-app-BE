package com.example.todoapp.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping("/register")
    public String addUser(@Valid @RequestBody UserInfo user) {
        return userInfoService.addUser(user);
    }
    @GetMapping("/get-all")
    public List<UserInfo> getAll(){
        return userInfoService.getAll();
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userInfoService.deleteUser(id);
        return "deleted";
    }
}
