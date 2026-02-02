-- Table: client
CREATE TABLE client
(
    id        nvarchar(200)  NOT NULL,
    id_client nvarchar(225)  NOT NULL,
    secret    nvarchar(200)  NULL,
    CONSTRAINT client_pk PRIMARY KEY (id)
);

-- Table: client_scope
CREATE TABLE client_scope
(
    client_id nvarchar(200)  NOT NULL,
    scope_id  int NOT NULL,
    CONSTRAINT client_scope_pk PRIMARY KEY (client_id, scope_id)
);

-- Table: client_auth_methods
CREATE TABLE client_auth_methods
(
    client_id   nvarchar(200) NOT NULL,
    auth_method varchar(50) NOT NULL,
    CONSTRAINT client_auth_methods_pk PRIMARY KEY (client_id, auth_method),
    CONSTRAINT client_auth_methods_client_fk FOREIGN KEY (client_id)
        REFERENCES client (id)
);

-- Table: client_grant_types
CREATE TABLE client_grant_types
(
    client_id  nvarchar(50) NOT NULL,
    grant_type varchar(50) NOT NULL,
    CONSTRAINT client_grant_types_pk PRIMARY KEY (client_id, grant_type),
    CONSTRAINT client_grant_types_client_fk FOREIGN KEY (client_id)
        REFERENCES client (id)
);

-- Table: redirect_uri
CREATE TABLE redirect_uri
(
    id        int NOT NULL AUTO_INCREMENT,
    name      nvarchar(200)  NOT NULL,
    client_id nvarchar(200)  NOT NULL,
    CONSTRAINT redirect_uri_pk PRIMARY KEY (id)
);

-- Table: scope
CREATE TABLE scope
(
    id   int NOT NULL AUTO_INCREMENT,
    name nvarchar(50)  NOT NULL,
    CONSTRAINT scope_pk PRIMARY KEY (id)
);

-- Table: user_
CREATE TABLE user_
(
    id       int NOT NULL AUTO_INCREMENT,
    username nvarchar(45)  NOT NULL UNIQUE,
    password nvarchar(200)  NOT NULL,
    role     NVARCHAR(50) NOT NULL,
    CONSTRAINT user__pk PRIMARY KEY (id)
);


-- foreign keys

-- Reference: client_scope_client (table: client_scope)
ALTER TABLE client_scope
    ADD CONSTRAINT client_scope_client FOREIGN KEY client_scope_client (client_id)
    REFERENCES client (id);

-- Reference: client_scope_scope (table: client_scope)
ALTER TABLE client_scope
    ADD CONSTRAINT client_scope_scope FOREIGN KEY client_scope_scope (scope_id)
    REFERENCES scope (id);

-- Reference: redirect_uri_client (table: redirect_uri)
ALTER TABLE redirect_uri
    ADD CONSTRAINT redirect_uri_client FOREIGN KEY redirect_uri_client (client_id)
    REFERENCES client (id);