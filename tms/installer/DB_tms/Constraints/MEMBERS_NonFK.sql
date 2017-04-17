-- 
-- Non Foreign Key Constraints for Table MEMBERS 
-- 
Prompt Non-Foreign Key Constraints on Table MEMBERS;
ALTER TABLE MEMBERS ADD (
  CONSTRAINT PK_MEMBERS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
