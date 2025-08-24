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

ALTER TABLE users ADD (
    password_expired NUMBER(1) DEFAULT 0 CHECK (password_expired IN (0,1))
);


INSERT INTO roles (id, name)
VALUES (roles_seq.NEXTVAL, 'ROLE_ADMIN');
INSERT INTO roles (id, name)
VALUES (roles_seq.NEXTVAL, 'ROLE_EMPLOYEE');
INSERT INTO roles (id, name)
VALUES (roles_seq.NEXTVAL, 'ROLE_CONTRACTOR');


--=========Scheduler Job====================
CREATE
OR REPLACE PROCEDURE expire_old_passwords(p_days NUMBER) IS
BEGIN
UPDATE users
SET password_expired = 1
WHERE password_changed_at IS NOT NULL
  AND password_changed_at < (SYSDATE - p_days)
  AND password_expired = 0;

COMMIT;
END expire_old_passwords;
/

BEGIN
  DBMS_SCHEDULER.create_job
(
    job_name        => 'EXPIRE_PASSWORDS_JOB',
    job_type        => 'STORED_PROCEDURE',
    job_action      => 'EXPIRE_OLD_PASSWORDS',
    number_of_arguments => 1,
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=DAILY;BYHOUR=2;BYMINUTE=0;BYSECOND=0',
    enabled         => FALSE,
    comments        => 'Expire user passwords older than N days'
  );

  -- set argument value (90 days)
  DBMS_SCHEDULER.set_job_argument_value
(
    job_name      => 'EXPIRE_PASSWORDS_JOB',
    argument_position => 1,
    argument_value    => '90'
  );

  -- enable job
  DBMS_SCHEDULER.enable
('EXPIRE_PASSWORDS_JOB');
END;

SELECT job_name,enabled,state,last_start_date,last_run_duration,next_run_date,failure_count
FROM dba_scheduler_jobs WHERE  job_name = 'EXPIRE_PASSWORDS_JOB';

SELECT log_id,job_name,status,actual_start_date,run_duration,error#,additional_info
FROM dba_scheduler_job_run_details WHERE job_name = 'EXPIRE_PASSWORDS_JOB' ORDER BY log_id DESC;

SELECT job_name, session_id, slave_os_process_id, running_instance
FROM dba_scheduler_running_jobs;

BEGIN
  DBMS_SCHEDULER.run_job('EXPIRE_PASSWORDS_JOB', use_current_session => FALSE);
END;

--=====================================
