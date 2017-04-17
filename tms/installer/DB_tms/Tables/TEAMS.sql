Prompt drop TABLE TEAMS;
ALTER TABLE TEAMS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE TEAMS CASCADE CONSTRAINTS PURGE
/

Prompt Table TEAMS;
--
-- TEAMS  (Table) 
--
CREATE TABLE TEAMS
(
  ID          NUMBER(10)                        NOT NULL,
  PROJECTS    NUMBER(10)                        NOT NULL,
  MEMBERS     NUMBER(10)                        NOT NULL,
  PUBLISHED   CHAR(1 BYTE)                      NOT NULL,
  AUTHORIZED  CHAR(1 BYTE)                      NOT NULL
)
/


