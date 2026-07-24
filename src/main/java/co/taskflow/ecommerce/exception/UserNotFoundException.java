package co.taskflow.ecommerce.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String identifier;
    private final String value;

    public UserNotFoundException(String identifier, String value) {
        super(String.format("User with %s '%s' not found", identifier, value));
        this.identifier = identifier;
        this.value = value;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.identifier = null;
        this.value = null;
    }
}
