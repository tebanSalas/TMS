Prompt drop TABLE TASKS;
ALTER TABLE TASKS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE TASKS CASCADE CONSTRAINTS PURGE
/

Prompt Table TASKS;
--
-- TASKS  (Table) 
--
CREATE TABLE TASKS
(
  ID                      NUMBER(10)            NOT NULL,
  PROJECT                 NUMBER(10)            NOT NULL,
  PRIORITY                NUMBER(10)            NOT NULL,
  STATUS                  NUMBER(10)            NOT NULL,
  MESSAGE                 NUMBER(10)            NOT NULL,
  OWNER                   NUMBER(10)            NOT NULL,
  ASSIGNED_TO             NUMBER(10)            NOT NULL,
  NAME                    VARCHAR2(155 BYTE)    NOT NULL,
  DESCRIPTION             CLOB,
  START_DATE              DATE                  NOT NULL,
  DUE_DATE                DATE,
  REAL_DUE_DATE           DATE                  NOT NULL,
  ESTIMATED_TIME          NUMBER(10,2)          NOT NULL,
  ACTUAL_TIME             NUMBER(10,2)          NOT NULL,
  COMMENTS                VARCHAR2(3000 BYTE)   NOT NULL,
  COMPLETION              NUMBER(10)            NOT NULL,
  CREATED                 DATE                  NOT NULL,
  MODIFIED                DATE                  NOT NULL,
  ASSIGNED                DATE                  NOT NULL,
  PUBLISHED               CHAR(1 BYTE)          NOT NULL,
  COLLECT                 CHAR(1 BYTE)          NOT NULL,
  SEND_QUOTATION_DATE     DATE,
  REPLY_QUOTATION_DATE    DATE,
  REPLY_QUOTATION_MEMBER  NUMBER(10),
  TOPIC                   CHAR(1 BYTE)          NOT NULL,
  TOLERANCE               NUMBER(10)            NOT NULL,
  FARE                    NUMBER(12,2)          NOT NULL,
  PREDECESSOR             NUMBER(10),
  PREDECESSOR_REQUIRED    CHAR(1 BYTE),
  SEVERITY                CHAR(1 BYTE),
  TYPE_TASK               NUMBER(10),
  SPREAD_FIX              CHAR(1 BYTE)
)
/


