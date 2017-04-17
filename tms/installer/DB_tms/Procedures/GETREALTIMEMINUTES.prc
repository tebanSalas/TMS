Prompt drop Procedure getrealtimeminutes;
DROP PROCEDURE getrealtimeminutes
/

Prompt Procedure getrealtimeminutes;
--
-- getrealtimeminutes(Procedure) 
--
--
CREATE OR REPLACE PROCEDURE getrealtimeminutes(
  starttime       IN      VARCHAR2
 ,endtime         IN      VARCHAR2
 ,ampmstart       IN      VARCHAR2
 ,ampmend         IN      VARCHAR2
 ,realtimeminute  OUT     NUMBER
) AS
  auxminute  NUMBER;
BEGIN
  SELECT   (    (    (   TO_DATE( TO_CHAR( SYSDATE, 'ddmmrr' ) || endtime, 'ddmmrrhh:mi ' || ampmend )
                       - TO_DATE( TO_CHAR( SYSDATE, 'ddmmrr' ) || starttime, 'ddmmrrhh:mi ' || ampmstart )
                     )
                  * 24
                )
             - FLOOR(    (   TO_DATE( TO_CHAR( SYSDATE, 'ddmmrr' ) || endtime, 'ddmmrrhh:mi ' || ampmend )
                           - TO_DATE( TO_CHAR( SYSDATE, 'ddmmrr' ) || starttime, 'ddmmrrhh:mi ' || ampmstart )
                         )
                      * 24
                    )
           )
         * 60
  INTO   auxminute
  FROM DUAL;
  realtimeminute := ROUND( auxminute );
END;
/


SHOW ERRORS;


