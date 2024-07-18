package com.example.todoapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public String addUser(UserInfo user) {
        UserInfo existingUser = userInfoRepository.findByMailAndName(user.getMail(), user.getName());
        if (existingUser == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userInfoRepository.save(user);
            return "User Created";
        } else {
            throw new RuntimeException("User with this name or email already exists...");
        }
    }
    public List<UserInfo> getAll() {
        List<UserInfo> userInfos;
        userInfos = userInfoRepository.findAll();
        return userInfos;
    }
    public void deleteUser(Long id) {
        UserInfo user = userInfoRepository.findById(id).orElseThrow(()-> new RuntimeException("not found"));
        userInfoRepository.delete(user);
    }
}