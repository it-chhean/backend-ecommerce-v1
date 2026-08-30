package co.taskflow.ecommerce.service.impl;

import co.taskflow.ecommerce.dto.request.UserRequest;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.entity.RefreshToken;
import co.taskflow.ecommerce.entity.User;
import co.taskflow.ecommerce.exception.ConflictException;
import co.taskflow.ecommerce.jwt.JwtService;
import co.taskflow.ecommerce.mapper.UserMapper;
import co.taskflow.ecommerce.repository.UserRepository;
import co.taskflow.ecommerce.service.RefreshTokenService;
import co.taskflow.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DuplicateFormatFlagsException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional(readOnly = true)
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        log.info("User created: {}", saved);

        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.refreshToken(user);
        user.setRefreshToken(refreshToken);

        return toResponse(user, token);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = findByOrThrow(id);
        userMapper.applyToUserField(user , request);
        User saved = userRepository.save(user);
        log.info("User updated: {}", saved);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = findByOrThrow(id);
        log.info("User get: {}", user);
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        log.info("Users found: {}", users);
        return userMapper.toResponseList(users);
    }

    @Override
    public void updateStatus(Long id) {
        User user = findByOrThrow(id);
        user.setStatus(!user.isStatus());
        log.info("User status updated: {}", user);
        userRepository.save(user);
    }

    private User findByOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user , String token) {
        UserResponse response = userMapper.toResponse(user);
        response.setRefreshToken(user.getRefreshToken().getToken());
        response.setToken(token);
        return response;
    }
}
