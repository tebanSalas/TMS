-- 
-- Non Foreign Key Constraints for Table FILES 
-- 
Prompt Non-Foreign Key Constraints on Table FILES;
ALTER TABLE FILES ADD (
  CONSTRAINT PK_FILES
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
