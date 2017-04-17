-- 
-- Non Foreign Key Constraints for Table ASSIGNMENTS 
-- 
Prompt Non-Foreign Key Constraints on Table ASSIGNMENTS;
ALTER TABLE ASSIGNMENTS ADD (
  CONSTRAINT PK_ASSIGNMENTS
 PRIMARY KEY
 (ID, ID_ACCOUNT))
/
