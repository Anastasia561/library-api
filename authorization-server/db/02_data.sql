-- Insert into scope
INSERT INTO scope (name)
VALUES ('openid'),
       ('read');

-- Insert into user_
INSERT INTO user_ (username, password, role)
VALUES ('user1', '$2a$10$fnglpT/GYWhGAF1tpDAMkuKJtPPk8b7ogsrVCfR7OVGF5Ssp9mxTm', 'USER'),
       ('user2', '$2a$10$fnglpT/GYWhGAF1tpDAMkuKJtPPk8b7ogsrVCfR7OVGF5Ssp9mxTm', 'LIBRARIAN');
-- pass 111

-- Insert into client
INSERT INTO client (id, id_client)
VALUES ('a7b9c8d0-1234-4e5f-9a6b-7c8d9e0f1a2b', 'swagger-ui');

-- Insert into redirect_uri
INSERT INTO redirect_uri (name, client_id)
VALUES ('http://localhost:8080/api/swagger-ui/oauth2-redirect.html', 'a7b9c8d0-1234-4e5f-9a6b-7c8d9e0f1a2b');


-- Insert into client_auth_methods
INSERT INTO client_auth_methods (client_id, auth_method)
VALUES ('a7b9c8d0-1234-4e5f-9a6b-7c8d9e0f1a2b', 'NONE');

-- Insert into client_grant_types
INSERT INTO client_grant_types (client_id, grant_type)
VALUES ('a7b9c8d0-1234-4e5f-9a6b-7c8d9e0f1a2b', 'AUTHORIZATION_CODE');

-- Insert into client_scope
INSERT INTO client_scope (client_id, scope_id)
VALUES ('a7b9c8d0-1234-4e5f-9a6b-7c8d9e0f1a2b', 1),
       ('a7b9c8d0-1234-4e5f-9a6b-7c8d9e0f1a2b', 2);
