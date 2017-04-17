Prompt drop TABLE MEMBERS;
ALTER TABLE MEMBERS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE MEMBERS CASCADE CONSTRAINTS PURGE
/

Prompt Table MEMBERS;
--
-- MEMBERS  (Table) 
--
CREATE TABLE MEMBERS
(
  ID                   NUMBER(10)               NOT NULL,
  ORGANIZATION         NUMBER(10)               NOT NULL,
  LOGIN                VARCHAR2(50 BYTE)        NOT NULL,
  PASSWORD             VARCHAR2(100 BYTE)       NOT NULL,
  NAME                 VARCHAR2(155 BYTE)       NOT NULL,
  TITLE                VARCHAR2(155 BYTE),
  EMAIL_WORK           VARCHAR2(155 BYTE)       NOT NULL,
  EMAIL_HOME           VARCHAR2(155 BYTE),
  PHONE_WORK           VARCHAR2(25 BYTE),
  PHONE_HOME           VARCHAR2(25 BYTE),
  MOBILE               VARCHAR2(25 BYTE),
  FAX                  VARCHAR2(25 BYTE),
  COMMENTS             CLOB,
  PROFILE              CHAR(1 BYTE)             NOT NULL,
  CREATED              DATE                     NOT NULL,
  LOGOUT_TIME          NUMBER(10)               NOT NULL,
  LAST_PAGE            VARCHAR2(255 BYTE),
  QUOTATION            CHAR(1 BYTE),
  PERSONAL_TITLE       VARCHAR2(155 BYTE),
  REPORTS              CHAR(1 BYTE)             NOT NULL,
  TEMPLATE             VARCHAR2(50 BYTE),
  COST                 NUMBER(12,2),
  IND_CHECK_SCHEDULES  CHAR(1 BYTE),
  IND_END_TASKS        CHAR(1 BYTE),
  IND_CLIENT_MANAGER   CHAR(1 BYTE)
)
/

COMMENT ON COLUMN MEMBERS.IND_CLIENT_MANAGER IS 'Indica si el usuario es administrador de proyecto del cliente'

ALTER TABLE members MODIFY (cost DEFAULT 0)
/


