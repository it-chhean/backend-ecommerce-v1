package co.taskflow.ecommerce.mapper;

import co.taskflow.ecommerce.dto.request.RegisterRequest;
import co.taskflow.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    User toEntity(RegisterRequest request){
        if (request == null) return null;
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
