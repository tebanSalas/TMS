Prompt drop TABLE CALENDAR;
ALTER TABLE CALENDAR
 DROP PRIMARY KEY CASCADE
/
DROP TABLE CALENDAR CASCADE CONSTRAINTS PURGE
/

Prompt Table CALENDAR;
--
-- CALENDAR  (Table) 
--
CREATE TABLE CALENDAR
(
  ID           NUMBER(10)                       NOT NULL,
  MEMBER       NUMBER(10)                       NOT NULL,
  SUBJECT      VARCHAR2(255 BYTE)               NOT NULL,
  DESCRIPTION  CLOB,
  DAY          DATE                             NOT NULL,
  HOUR_START   NUMBER(10)                       NOT NULL,
  HOUR_END     NUMBER(10)                       NOT NULL,
  MIN_START    NUMBER(10)                       NOT NULL,
  MIN_END      NUMBER(10)                       NOT NULL,
  TASK         NUMBER(10),
  GUESTS       VARCHAR2(150 BYTE)
)
/


