CREATE
USER pharmacy_mgmt IDENTIFIED BY "dudkrish1A";

GRANT CREATE
SESSION TO pharmacy_mgmt;

GRANT DBA TO pharmacy_mgmt;

-- Sequences for primary keys
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE roles_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- ROLES
CREATE TABLE roles
(
    id   NUMBER PRIMARY KEY,
    name VARCHAR2(50) UNIQUE NOT NULL
);

-- USERS
CREATE TABLE users
(
    id                  NUMBER PRIMARY KEY,
    username            VARCHAR2(100) UNIQUE NOT NULL,
    password            VARCHAR2(255) NOT NULL,
    enabled             NUMBER(1) DEFAULT 1 CHECK (enabled IN (0,1)),

    failed_attempts     NUMBER    DEFAULT 0 NOT NULL,
    lock_until          TIMESTAMP NULL,
    password_changed_at TIMESTAMP DEFAULT SYSDATE
);

-- USER_ROLES (many-to-many)
CREATE TABLE user_roles
(
    user_id NUMBER NOT NULL,
    role_id NUMBER NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT user_roles_pk PRIMARY KEY (user_id, role_id)
);


INSERT INTO roles (id, name)
VALUES (roles_seq.NEXTVAL, 'ROLE_ADMIN');
INSERT INTO roles (id, name)
VALUES (roles_seq.NEXTVAL, 'ROLE_EMPLOYEE');
INSERT INTO roles (id, name)
VALUES (roles_seq.NEXTVAL, 'ROLE_CONTRACTOR');
