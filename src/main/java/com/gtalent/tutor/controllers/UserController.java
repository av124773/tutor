package com.gtalent.tutor.controllers;

import com.gtalent.tutor.models.User;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.requests.UpdateUserRequest;
import com.gtalent.tutor.responses.CreateUserResponse;
import com.gtalent.tutor.responses.GetUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> mockUser = new HashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger();
    public UserController() {
        mockUser.put(1, new User(1, "admin", "admin@gmail.com"));
        mockUser.put(2, new User(2, "user1", "user1@gmail.com"));
        mockUser.put(3, new User(3, "user2", "user2@gmail.com"));
        atomicInteger.set(4);
    }

    @GetMapping()
    public ResponseEntity<List<GetUserResponse>> getAllUsers(){
        List<User> userList = new ArrayList<>(mockUser.values());
        List<GetUserResponse> responses = new ArrayList<>();
        for (User user : userList) {
            GetUserResponse response = new GetUserResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            responses.add(response);
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id) {
        User user = mockUser.get(id);
        // response 404 if user no found
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        GetUserResponse response = new GetUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserRequest> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        User user = mockUser.get(id);
        // response 404 if user no found
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setUsername(request.getUsername());

        UpdateUserRequest response = new UpdateUserRequest(user.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        int newId = atomicInteger.getAndIncrement();
        User newUser = new User(newId, request.getUsername(), request.getEmail());
        mockUser.put(newUser.getId(), newUser);
        CreateUserResponse response = new CreateUserResponse(newUser.getUsername());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable int id) {
        User user = mockUser.get(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        mockUser.remove(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetUserResponse>> searchUser(@RequestParam String keyword){
        List<GetUserResponse> results = mockUser.values()
                .stream() // lambda 起手式
                .filter(user ->
                user.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                .map(GetUserResponse::new)
//                .map(user -> this.toGetUserResponse(user))
                .toList();

        return ResponseEntity.ok(results);

    }

    @GetMapping("/normal")
    public ResponseEntity<List<GetUserResponse>> getNormalUser() {
        List<GetUserResponse> results = mockUser.values()
                .stream()
                .filter(user ->
                        !user.getUsername().toLowerCase().contains("admin"))
//                .filter(user ->
//                        user.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                .map(GetUserResponse::new)
                .toList();

        return ResponseEntity.ok(results);
    }

    private GetUserResponse toGetUserResponse (User user) {
        return new GetUserResponse(user.getId(), user.getUsername());
    }
}
