package com.gtalent.tutor.controllers;

import com.gtalent.tutor.models.User;
import com.gtalent.tutor.repositories.UserRepository;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.responses.GetUserResponse;
import com.gtalent.tutor.responses.UpdateUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin("*")
public class UserV2Controller {
    private final UserRepository userRepository;

    public UserV2Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().map(GetUserResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id ) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            GetUserResponse response = new GetUserResponse(user.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUserById(@PathVariable int id, @RequestBody CreateUserRequest request) {
        // 1. 找到user
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            // 2. 確定找到user之後
            User updatedUser = user.get();
            // 3. 將欲更新資料填充至對應user
            updatedUser.setUsername(request.getUsername());
            User saveUser = userRepository.save(updatedUser);

            UpdateUserResponse response = new UpdateUserResponse(saveUser.getUsername());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<GetUserResponse> createUsers(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        System.out.println("Before Save" + user);
        User savedUser = userRepository.save(user);
        GetUserResponse response = new GetUserResponse(savedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }
}
