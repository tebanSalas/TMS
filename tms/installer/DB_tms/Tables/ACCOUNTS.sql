DROP TABLE ACCOUNTS CASCADE CONSTRAINTS;

CREATE TABLE ACCOUNTS
(
  ID                NUMBER(10)                  NOT NULL,
  NAME              VARCHAR2(150 BYTE)              NULL,
  DESCRIPTION       VARCHAR2(150 BYTE)              NULL,
  ADDRESS           VARCHAR2(150 BYTE)              NULL,
  PHONE_1           VARCHAR2(25 BYTE)               NULL,
  PHONE_2           VARCHAR2(25 BYTE)               NULL,
  EMAIL             VARCHAR2(155 BYTE)              NULL,
  ACCOUNT_LOGO      VARCHAR2(255 BYTE)              NULL,
  USER_FARE         NUMBER(10,2)                    NULL,
  ACCOUNT_EMAIL     VARCHAR2(155 BYTE)              NULL,
  COMPANY           VARCHAR2(150 BYTE)              NULL,
  CREATOR           VARCHAR2(150 BYTE)              NULL,
  WEBSITE           VARCHAR2(150 BYTE)              NULL,
  ACTIVE            CHAR(1 BYTE)                    NULL,
  SHORTNAME         VARCHAR2(5 BYTE)                NULL,
  KEY_ENCRIPTATION  VARCHAR2(100 BYTE)              NULL,
  FINAL_TRIAL_DATE  DATE							NULL,
  MAIN_URL			VARCHAR2(150 BYTE)              NULL
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


