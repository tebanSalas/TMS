Prompt drop TABLE TOPICS;
ALTER TABLE TOPICS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE TOPICS CASCADE CONSTRAINTS PURGE
/

Prompt Table TOPICS;
--
-- TOPICS  (Table) 
--
CREATE TABLE TOPICS
(
  ID         NUMBER(10)                         NOT NULL,
  PROJECT    NUMBER(10)                         NOT NULL,
  OWNER      NUMBER(10)                         NOT NULL,
  SUBJECT    VARCHAR2(600 BYTE)                 NOT NULL,
  STATUS     CHAR(1 BYTE)                       NOT NULL,
  LAST_POST  DATE                               NOT NULL,
  POSTS      NUMBER(10)                         NOT NULL,
  PUBLISHED  CHAR(1 BYTE)                       NOT NULL,
  TASKS      NUMBER(10)                         NOT NULL,
  TOTASKS    CHAR(1 BYTE)                       NOT NULL,
  NOTIFIED   CHAR(1 BYTE)                       DEFAULT '0'	
)
/


