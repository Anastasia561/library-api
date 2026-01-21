-- Insert into scope
INSERT INTO scope (name)
VALUES ('openid');

-- Insert into user_
INSERT INTO user_ (username, password, role)
VALUES ('user1', '$2a$10$fnglpT/GYWhGAF1tpDAMkuKJtPPk8b7ogsrVCfR7OVGF5Ssp9mxTm', 'USER');
-- pass 111

-- Insert into client
INSERT INTO client (id, id_client, secret)
VALUES ('test_id', 'test-client', '$2a$10$F43k.qzUyg.mdW8NXeRArOvetbf1vgyWaG.WjjMyTRGKlZv8levZa');
-- secret secret

-- Insert into redirect_uri
INSERT INTO redirect_uri (name, client_id)
VALUES ('https://springone.io/test', 'test_id');

-- Insert into client_auth_methods
INSERT INTO client_auth_methods (client_id, auth_method)
VALUES ('test_id', 'CLIENT_SECRET_BASIC');

-- Insert into client_grant_types
INSERT INTO client_grant_types (client_id, grant_type)
VALUES ('test_id', 'AUTHORIZATION_CODE');

-- Insert into client_scope
INSERT INTO client_scope (client_id, scope_id)
VALUES ('test_id', 1);
