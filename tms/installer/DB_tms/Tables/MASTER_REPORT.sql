DROP TABLE MASTER_REPORT CASCADE CONSTRAINTS;

CREATE TABLE MASTER_REPORT
(
  ID           NUMBER(10)                       NOT NULL,
  NAME         VARCHAR2(155 BYTE)               NOT NULL,
  DESCRIPTION  CLOB                                 NULL
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


