package co.taskflow.ecommerce.service;

import co.taskflow.ecommerce.dto.request.ProductRequest;
import co.taskflow.ecommerce.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProduct(Long id);
    List<ProductResponse> getProducts();
    Page<ProductResponse> getProducts(Pageable pageable);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
