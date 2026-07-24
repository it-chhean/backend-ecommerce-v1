package co.taskflow.ecommerce.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(length = 50, nullable = false,  unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Override
    public @Nullable String getAuthority() {
        return "ROLE_" + name;
    }

}