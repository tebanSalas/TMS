-- 
-- Non Foreign Key Constraints for Table TASKS 
-- 
Prompt Non-Foreign Key Constraints on Table TASKS;
ALTER TABLE TASKS ADD (
  CONSTRAINT PK_TASKS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
