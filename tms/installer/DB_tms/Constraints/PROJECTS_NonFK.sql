-- 
-- Non Foreign Key Constraints for Table PROJECTS 
-- 
Prompt Non-Foreign Key Constraints on Table PROJECTS;
ALTER TABLE PROJECTS ADD (
  CONSTRAINT PK_PROJECTS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
