-- 
-- Non Foreign Key Constraints for Table TEAMS 
-- 
Prompt Non-Foreign Key Constraints on Table TEAMS;
ALTER TABLE TEAMS ADD (
  CONSTRAINT PK_TEAMS
 PRIMARY KEY
 (ID_ACCOUNT,ID))
/
