package co.taskflow.ecommerce.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String name,
    String desc,
    BigDecimal price,
    Integer productQty,
    String image,
    boolean status, 
    LocalDateTime createdAt,
    LocalDateTime updatedAt 
) {}
