-- 
-- Non Foreign Key Constraints for Table SCHEDULES 
-- 
Prompt Non-Foreign Key Constraints on Table SCHEDULES;
ALTER TABLE SCHEDULES ADD (
  CONSTRAINT PK_SCHEDULES
 PRIMARY KEY
 (HOURID, USERID, DAY,ID_ACCOUNT))
/
