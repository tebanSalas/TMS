Prompt drop TABLE ASSIGNMENTS;
ALTER TABLE ASSIGNMENTS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE ASSIGNMENTS CASCADE CONSTRAINTS PURGE
/

Prompt Table ASSIGNMENTS;
--
-- ASSIGNMENTS  (Table) 
--
CREATE TABLE ASSIGNMENTS
(
  ID           NUMBER(10)                       NOT NULL,
  TASK         NUMBER(10)                       NOT NULL,
  OWNER        NUMBER(10)                       NOT NULL,
  ASSIGNED_TO  NUMBER(10)                       NOT NULL,
  ASSIGNED     DATE                             NOT NULL
)
/


