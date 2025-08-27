package com.gtalent.tutor.controllers;

import com.gtalent.tutor.models.User;
import com.gtalent.tutor.repositories.UserRepository;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.responses.GetUserResponse;
import com.gtalent.tutor.responses.UpdateUserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin("*")
@Tag(name = "使用者", description = "使用者v2控制器，提供使用者資訊新增、刪除、更新、查詢功能。")
public class UserV2Controller {
    private final UserRepository userRepository;

    public UserV2Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "取得所有使用者資訊", description = "回傳所有使用者資訊清單")
    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().map(GetUserResponse::new).toList());
    }

    @Operation(summary = "取得指定使用者資訊", description = "回傳指定使用者的資訊")
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

    @Operation(summary = "更新指定使用者資訊", description = "更新使用者資訊需要 ROLE_USER 權限。", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "401", description = "資料格式不正確"),
        @ApiResponse(responseCode = "403", description = "權限不符"),
    })
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

    @Operation(summary = "新增使用者資訊", description = "新增使用者資訊需要 ROLE_USER 權限。", security = @SecurityRequirement(name = "bearerAuth"))
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

    @Operation(summary = "刪除指定使用者資訊", description = "刪除使用者需要 ROLE_ADMIN 權限。", security = @SecurityRequirement(name = "bearerAuth"))
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
