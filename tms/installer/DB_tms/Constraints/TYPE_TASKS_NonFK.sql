-- 
-- Non Foreign Key Constraints for Table TYPE_TASKS 
-- 
Prompt Non-Foreign Key Constraints on Table TYPE_TASKS;
ALTER TABLE TYPE_TASKS ADD (
  CONSTRAINT PK_TYPE_TASKS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
