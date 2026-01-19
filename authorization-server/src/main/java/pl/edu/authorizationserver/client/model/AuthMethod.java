package pl.edu.authorizationserver.client.model;

public enum AuthMethod {
    CLIENT_SECRET_BASIC,
    CLIENT_SECRET_POST,
    NONE,
    CLIENT_SECRET_JWK,
    PRIVATE_KEY_JWT
}
