package co.taskflow.ecommerce.mapper;

import co.taskflow.ecommerce.dto.request.AuthRequest;
import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.dto.response.UserResponse;
import co.taskflow.ecommerce.entity.Role;
import co.taskflow.ecommerce.entity.User;
import co.taskflow.ecommerce.exception.ResourceNotFoundException;
import co.taskflow.ecommerce.repository.RoleRepository;
import co.taskflow.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
public class AuthMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    AuthMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
      this.passwordEncoder = passwordEncoder;
      this.roleRepository = roleRepository;
    }


    public User toLogin(AuthRequest request) {
        if (request == null) return null;
        return User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();
    }

    public User toRegister(RegisterRequest request) {
        if (request == null) return null;

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role USER not found!"));
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .profileImage(request.getProfileImage())
                .lastLoginAt(Instant.now())
                .lastLoginIp("172.20.10.2")
                .failedLoginAttempts(0)
                .lockedUtil(Instant.now())
                .status(true)
                .roles(Set.of(defaultRole))
                .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .profileImage(user.getProfileImage())
                .lastLoginAt(user.getLastLoginAt())
                .lastLoginIp(user.getLastLoginIp())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .lockedUtil(user.getLockedUtil())
                .status(user.isStatus())
                .build();
    }

}
