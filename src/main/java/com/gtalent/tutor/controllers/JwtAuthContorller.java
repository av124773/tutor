package com.gtalent.tutor.controllers;

import com.gtalent.tutor.requests.LoginRequest;
import com.gtalent.tutor.requests.RegisterRequest;
import com.gtalent.tutor.responses.AuthResponse;
import com.gtalent.tutor.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@CrossOrigin("*")
@Tag(name = "JWT驗證", description = "提供使用者登入註冊")
public class JwtAuthContorller {

    @Autowired
    private AuthService authService;

    @Operation(summary = "jwt註冊用戶",  description = "1.username不得重複。\n\n2.密碼必須8個字元以上。\n\n3.必須提供用戶角色，並附上ROLE_前綴字串(ex: ROLE_USER)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "401", description = "資料格式不正確"),
        @ApiResponse(responseCode = "403", description = "權限不足")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "jwt登入用戶",  description = "返回Token")
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }
}
