package pl.edu.authorizationserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.edu.authorizationserver.validation.annotation.UniqueUsername;

public class UserRegisterDto {
    @UniqueUsername
    @NotEmpty(message = "username can not be empty")
    @Size(min = 3, max = 45)
    private String username;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/~`]).{8,}$",
            message = "Password must be at least 8 characters long and contain an uppercase letter, lowercase letter, digit, and special character"
    )
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
