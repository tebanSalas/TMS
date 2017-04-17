Prompt drop TABLE PROJECTS;
ALTER TABLE PROJECTS
 DROP PRIMARY KEY CASCADE
/
DROP TABLE PROJECTS CASCADE CONSTRAINTS PURGE
/

Prompt Table PROJECTS;
--
-- PROJECTS  (Table) 
--
CREATE TABLE PROJECTS
(
  ID                  NUMBER(10)                NOT NULL,
  ORGANIZATION        NUMBER(10)                NOT NULL,
  OWNER               NUMBER(10)                NOT NULL,
  PRIORITY            NUMBER(10)                NOT NULL,
  STATUS              NUMBER(10)                NOT NULL,
  NAME                VARCHAR2(155 BYTE)        NOT NULL,
  DESCRIPTION         CLOB                      NOT NULL,
  CREATED             DATE                      NOT NULL,
  MODIFIED            DATE                      NOT NULL,
  PUBLISHED           CHAR(1 BYTE)              NOT NULL,
  UPLOAD_MAX          VARCHAR2(155 BYTE)        NOT NULL,
  PUBLISHED_ASSIGNED  CHAR(1 BYTE)              NOT NULL,
  PUBLISHED_ENDTASK   CHAR(1 BYTE)              NOT NULL,
  START_DATE          DATE,
  END_DATE            DATE,
  AUTOM_NOTIFICATION  CHAR(1 BYTE)		DEFAULT '1';			
)
/


