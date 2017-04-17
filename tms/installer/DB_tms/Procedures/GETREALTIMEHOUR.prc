Prompt drop Procedure getrealtimehour;
DROP PROCEDURE getrealtimehour
/

Prompt Procedure getrealtimehour;
--
-- getrealtimehour(Procedure) 
--
--
CREATE OR REPLACE PROCEDURE getrealtimehour(
  starttime     IN      VARCHAR2
 ,endtime       IN      VARCHAR2
 ,ampmstart     IN      VARCHAR2
 ,ampmend       IN      VARCHAR2
 ,realtimehour  OUT     NUMBER
) AS
  auxhour  NUMBER;
BEGIN
  SELECT FLOOR(    (   TO_DATE( TO_CHAR( SYSDATE, 'ddmmrr' ) || endtime, 'ddmmrrhh:mi ' || ampmend )
                     - TO_DATE( TO_CHAR( SYSDATE, 'ddmmrr' ) || starttime, 'ddmmrrhh:mi ' || ampmstart )
                   )
                * 24
              )
  INTO   auxhour
  FROM DUAL;
  realtimehour := auxhour;
END;
/

SHOW ERRORS;


