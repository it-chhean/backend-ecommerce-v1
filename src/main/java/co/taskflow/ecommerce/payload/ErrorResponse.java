package co.taskflow.ecommerce.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        String timestamp,
        List<String> details 
) {
    public static ErrorResponse of(
            HttpStatus status,
            String error,
            String message,
            String path,
    ) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                Instant.now().toString(),
                null
        );
    }
    public static ErrorResponse of(
            HttpStatus status, 
            String error, 
            String message,
            String path,
            List<String> details
    ){
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                Instant.now().toString(),
                details
        );
    }
}
