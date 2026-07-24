package co.taskflow.ecommerce.payload;

public record FieldErrorResponse(
        String field,
        String reason
) {}
