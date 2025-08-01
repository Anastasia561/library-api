package pl.edu.authorizationserver.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.edu.authorizationserver.validation.validator.UniqueClientIdValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueClientIdValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueClientId {
    String message() default "Client id must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
