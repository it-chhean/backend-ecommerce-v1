package co.taskflow.ecommerce.controller;

import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserResponse> signin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

}
