-- 
-- Non Foreign Key Constraints for Table ORGANIZATIONS 
-- 
Prompt Non-Foreign Key Constraints on Table ORGANIZATIONS;
ALTER TABLE ORGANIZATIONS ADD (
  CONSTRAINT PK_ORGANIZATIONS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
