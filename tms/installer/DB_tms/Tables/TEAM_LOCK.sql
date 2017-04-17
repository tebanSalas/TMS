DROP TABLE TEAM_LOCK CASCADE CONSTRAINTS;

CREATE TABLE TEAM_LOCK
(
  ID            NUMBER(10)                      NOT NULL,
  ACCOUNT_ID    NUMBER(10)                          NULL,
  ACCOUNT_NAME  VARCHAR2(155 BYTE)                  NULL,
  ORG_ID        NUMBER(10)                          NULL,
  ORG_NAME      VARCHAR2(155 BYTE)                  NULL,
  PROJECT_ID    NUMBER(10)                          NULL,
  PROJECT_NAME  VARCHAR2(155 BYTE)                  NULL,
  USER_ID       NUMBER(10)                          NULL,
  USER_NAME     VARCHAR2(155 BYTE)                  NULL,
  ACTION        CHAR(1 BYTE)                        NULL,
  ACTION_DATE   DATE                                NULL
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


