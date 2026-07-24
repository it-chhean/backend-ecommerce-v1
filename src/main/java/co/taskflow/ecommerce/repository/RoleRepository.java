package co.taskflow.ecommerce.repository;

import co.taskflow.ecommerce.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Short> {
    Optional<Role> findByName(String name);
}
