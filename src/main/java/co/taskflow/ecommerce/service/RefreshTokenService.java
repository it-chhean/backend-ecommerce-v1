package co.taskflow.ecommerce.service;

import co.taskflow.ecommerce.dto.response.RefreshTokenResponse;
import co.taskflow.ecommerce.entity.RefreshToken;
import co.taskflow.ecommerce.entity.User;

public interface RefreshTokenService {
    RefreshToken refreshToken(User user);
    RefreshTokenResponse verify(String token);
}
