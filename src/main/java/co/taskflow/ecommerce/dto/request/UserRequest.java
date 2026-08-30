package co.taskflow.ecommerce.dto.request;

import java.util.List;

public record UserRequest(
     String username,
     String email,
     String password,
     String refreshToken,
     String token,
     List<String>roles
) { }