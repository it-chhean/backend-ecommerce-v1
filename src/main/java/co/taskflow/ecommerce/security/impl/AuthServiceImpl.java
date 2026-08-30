package co.taskflow.ecommerce.security.impl;

import co.taskflow.ecommerce.dto.request.AuthRequest;
import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.dto.request.ResetPasswordRequest;
import co.taskflow.ecommerce.dto.response.AuthResponse;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.entity.Role;
import co.taskflow.ecommerce.entity.User;
import co.taskflow.ecommerce.exception.InvalidPasswordException;
import co.taskflow.ecommerce.exception.UserAlreadyExistsException;
import co.taskflow.ecommerce.exception.UserNotFoundException;
import co.taskflow.ecommerce.jwt.JwtService;
import co.taskflow.ecommerce.mapper.AuthMapper;
import co.taskflow.ecommerce.mapper.UserMapper;
import co.taskflow.ecommerce.repository.RoleRepository;
import co.taskflow.ecommerce.repository.UserRepository;
import co.taskflow.ecommerce.security.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default role USER is not found"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(defaultRole))
                .failedLoginAttempts(0)
                .build();

        User saved = userRepository.save(user);
        log.info("New user registered: {}", saved.getEmail());

        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException | DisabledException | LockedException ex) {
            throw new InvalidPasswordException("Invalid email or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (authentication.getPrincipal() instanceof User u)
                ? u
                : userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found after authentication"));

        user.setLastLoginAt(Instant.now());
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), jwtService.getExpiration());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse me() {
        User user = getCurrentUser();
        return userMapper.toResponse(user);
    }

    @Override
    public User authenticate() {
        return getCurrentUser();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // an AnonymousAuthenticationToken, not a null Authentication.
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }

        if (auth.getPrincipal() instanceof User user) {
            return user;
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + auth.getName()));
    }

    @Override
    @Transactional
    public UserResponse resetPassword(ResetPasswordRequest request) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new InvalidPasswordException("New password must differ from the old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User saved = userRepository.save(user);

        log.info("Password changed for user: {}", saved.getEmail());
        return userMapper.toResponse(saved);
    }
}
