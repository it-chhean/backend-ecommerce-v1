package co.taskflow.ecommerce.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private String refreshToken;
    private String token;
    private List<String> roles;
    private Instant createdAt;
    private Instant updatedAt;

}
