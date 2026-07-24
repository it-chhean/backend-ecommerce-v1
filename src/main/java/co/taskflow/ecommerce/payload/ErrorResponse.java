package co.taskflow.ecommerce.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String status,
        int code,
        String message,
        Instant timestamp,
        Object errorDetails
) {
    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(
                status.getReasonPhrase(),
                status.value(),
                message,
                Instant.now(),
                null
        );
    }

    public static ErrorResponse of(HttpStatus status, String message, Object errorDetails) {
        return new ErrorResponse(
                status.getReasonPhrase(),
                status.value(),
                message,
                Instant.now(),
                errorDetails
        );
    }
}