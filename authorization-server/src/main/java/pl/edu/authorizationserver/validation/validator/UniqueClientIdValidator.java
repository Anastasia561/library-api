package pl.edu.authorizationserver.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.authorizationserver.client.repository.ClientRepository;
import pl.edu.authorizationserver.validation.annotation.UniqueClientId;

public class UniqueClientIdValidator implements ConstraintValidator<UniqueClientId, String> {
    private final ClientRepository clientRepository;

    public UniqueClientIdValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean isValid(String clientId, ConstraintValidatorContext constraintValidatorContext) {
        return !clientRepository.existsByIdClient(clientId);
    }
}
