-- 
-- Non Foreign Key Constraints for Table RISKS 
-- 
Prompt Non-Foreign Key Constraints on Table RISKS;
ALTER TABLE RISKS ADD (
  CONSTRAINT PK_RISKS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
