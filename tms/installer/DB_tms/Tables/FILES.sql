Prompt drop TABLE FILES;
ALTER TABLE FILES
 DROP PRIMARY KEY CASCADE
/
DROP TABLE FILES CASCADE CONSTRAINTS PURGE
/

Prompt Table FILES;
--
-- FILES  (Table) 
--
CREATE TABLE FILES
(
  ID         NUMBER(10)                         NOT NULL,
  OWNER      NUMBER(10)                         NOT NULL,
  PROJECT    NUMBER(10)                         NOT NULL,
  TASK       NUMBER(10)                         NOT NULL,
  NAME       VARCHAR2(255 BYTE)                 NOT NULL,
  DAY        DATE                               NOT NULL,
  LENGTH     NUMBER(15)                         NOT NULL,
  TYPE       VARCHAR2(60 BYTE)                  NOT NULL,
  COMMENTS   VARCHAR2(155 BYTE)                 NOT NULL,
  UPLOAD     DATE                               NOT NULL,
  PUBLISHED  CHAR(1 BYTE)                       NOT NULL,
  STATUS     NUMBER(10)                         NOT NULL,
  TOPIC      NUMBER(10)
)
/


