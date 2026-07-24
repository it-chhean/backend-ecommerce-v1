package co.taskflow.ecommerce.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private String profileImage;
    private Instant lastLoginAt;
    private String lastLoginIp;
    private Integer failedLoginAttempts;
    private Instant lockedUtil;
    private boolean status = true;

}
