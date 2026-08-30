package co.taskflow.ecommerce.mapper;

import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.dto.request.UserRequest;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.entity.Role;
import co.taskflow.ecommerce.entity.User;
import co.taskflow.ecommerce.exception.UserNotFoundException;
import co.taskflow.ecommerce.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User toEntity(RegisterRequest request) {
        User user = new User();
        applyToUserField(user,  request);
        return user;
    }

    public User toUser(UserRequest req) {
        if (req == null) return null;
        List<String> roles = req.roles().stream()
                .map(Role::getName)
                .toList();
        return User.builder()
                .username(req.username())
                .email(req.email())
                .password(req.password())
                .roles(roles)
                .build();
    }

    public UserResponse toResponse(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(roles)
                .refreshToken(user.getRefreshToken().getToken())
                .token(null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyToUserField(User user, RegisterRequest request) {
        Role defaultRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(true);
        user.setRoles(Set.of(defaultRole));
    }
}
