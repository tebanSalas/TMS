-- 
-- Non Foreign Key Constraints for Table POST 
-- 
Prompt Non-Foreign Key Constraints on Table POST;
ALTER TABLE POST ADD (
  CONSTRAINT PK_POST
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
