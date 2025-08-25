package com.gtalent.tutor.controllers;

import com.gtalent.tutor.models.User;
import com.gtalent.tutor.repositories.UserRepository;
import com.gtalent.tutor.requests.CreateUserRequest;
import com.gtalent.tutor.requests.LoginRequest;
import com.gtalent.tutor.requests.RegisterRequest;
import com.gtalent.tutor.responses.GetUserResponse;
import com.gtalent.tutor.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/session")
@CrossOrigin("*")
public class SessionAuthController {
    private final UserService userService;
    private final UserRepository userRepository; // 偷懶作法

    @Autowired
    public SessionAuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<User> user = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> user = userRepository.findById(userId);

        return ResponseEntity.ok(user.get());
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request, HttpSession session) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userRepository.save(user);

        session.setAttribute("userId", user.getId());
        return ResponseEntity.ok(user);
    }

}
