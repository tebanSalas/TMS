-- 
-- Non Foreign Key Constraints for Table COSTS 
-- 
Prompt Non-Foreign Key Constraints on Table COSTS;
ALTER TABLE COSTS ADD (
  CONSTRAINT PK_COSTS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
