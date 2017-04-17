Prompt drop TABLE POST;
ALTER TABLE POST
 DROP PRIMARY KEY CASCADE
/
DROP TABLE POST CASCADE CONSTRAINTS PURGE
/

Prompt Table POST;
--
-- POST  (Table) 
--
CREATE TABLE POST
(
  ID       NUMBER(10)                           NOT NULL,
  TOPIC    NUMBER(10)                           NOT NULL,
  MEMBER   NUMBER(10)                           NOT NULL,
  CREATED  DATE                                 NOT NULL,
  MESSAGE  CLOB                                 NOT NULL
)
/


