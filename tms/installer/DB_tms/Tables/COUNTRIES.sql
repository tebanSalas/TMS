Prompt drop TABLE COUNTRIES;
ALTER TABLE COUNTRIES
 DROP PRIMARY KEY CASCADE
/
DROP TABLE COUNTRIES CASCADE CONSTRAINTS PURGE
/

Prompt Table COUNTRIES;
--
-- COUNTRIES  (Table) 
--
CREATE TABLE COUNTRIES
(
  COD_PAIS     NUMBER(10)                       NOT NULL,
  DESCRIPTION  VARCHAR2(155 BYTE)               NOT NULL
)
/


