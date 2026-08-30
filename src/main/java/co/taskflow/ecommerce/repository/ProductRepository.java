package co.taskflow.ecommerce.repository;

import co.taskflow.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
    boolean existsById(Long id);
}
