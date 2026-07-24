package co.taskflow.ecommerce.security.impl;

import co.taskflow.ecommerce.dto.request.AuthRequest;
import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.dto.response.AuthResponse;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.entity.Role;
import co.taskflow.ecommerce.entity.User;
import co.taskflow.ecommerce.exception.ConflictException;
import co.taskflow.ecommerce.exception.UserAlreadyExistsException;
import co.taskflow.ecommerce.exception.UserNotFoundException;
import co.taskflow.ecommerce.jwt.JwtService;
import co.taskflow.ecommerce.mapper.AuthMapper;
import co.taskflow.ecommerce.repository.RoleRepository;
import co.taskflow.ecommerce.repository.UserRepository;
import co.taskflow.ecommerce.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exist or already registered");
        }

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default role USER is not found"));


        return null;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        /*
         1. Delegate credential checking to spring security throws
            BadCredentialsException if email/password don't match.
         */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        /*
         2. Credential are valid -- load the user to build claims/token
         */
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found after authentication"));

        /*
         3. Update login metadata
         */
        user.setLastLoginAt(Instant.now());
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        /*
         4. Generate Jwt
         */
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
        );

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail(), jwtService.getExpiration());
    }

}
