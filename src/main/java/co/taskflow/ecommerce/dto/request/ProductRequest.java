package co.taskflow.ecommerce.dto.request;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        String desc,
        BigDecimal price,
        Integer productQty,
        String image,
        boolean status
) {}
