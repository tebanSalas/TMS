-- 
-- Non Foreign Key Constraints for Table CALENDAR 
-- 
Prompt Non-Foreign Key Constraints on Table CALENDAR;
ALTER TABLE CALENDAR ADD (
  CONSTRAINT PK_CALENDAR
 PRIMARY KEY
 (ID,ID_ACCOUNT))
/
