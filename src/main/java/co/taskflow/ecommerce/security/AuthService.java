package co.taskflow.ecommerce.security;

import co.taskflow.ecommerce.dto.request.AuthRequest;
import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.dto.request.ResetPasswordRequest;
import co.taskflow.ecommerce.dto.response.AuthResponse;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.entity.User;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);

    UserResponse me();

    User authenticate();

    UserResponse resetPassword(ResetPasswordRequest request);
}
