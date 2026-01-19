package pl.edu.authorizationserver.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.edu.authorizationserver.validation.annotation.UniqueUsername;

public record UserRegisterDto(
        @UniqueUsername
        @NotEmpty(message = "username can not be empty")
        @Size(min = 3, max = 45)
        String username,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/~`]).{8,}$",
                message = "Password must be at least 8 characters long and contain an uppercase letter, lowercase letter, digit, and special character"
        )
        String password
) {
}
