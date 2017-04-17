-- 
-- Non Foreign Key Constraints for Table TOPICS 
-- 
Prompt Non-Foreign Key Constraints on Table TOPICS;
ALTER TABLE TOPICS ADD (
  CONSTRAINT PK_TOPICS
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
