Prompt drop TABLE TYPE_TASKS;
ALTER TABLE TYPE_TASKS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE TYPE_TASKS CASCADE CONSTRAINTS PURGE
/

Prompt Table TYPE_TASKS;
--
-- TYPE_TASKS  (Table) 
--
CREATE TABLE TYPE_TASKS
(
  ID           NUMBER(10)                       NOT NULL,
  DESCRIPTION  VARCHAR2(155 BYTE)               NOT NULL,
  PREFIX       VARCHAR2(7 BYTE),
  CONSECUTIVE  NUMBER(5),
  USE_PREFIX   CHAR(1 BYTE)
)
/


