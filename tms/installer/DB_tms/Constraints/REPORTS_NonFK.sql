-- 
-- Non Foreign Key Constraints for Table REPORTS 
-- 
Prompt Non-Foreign Key Constraints on Table REPORTS;
ALTER TABLE REPORTS ADD (
  CONSTRAINT PK_REPORTS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
