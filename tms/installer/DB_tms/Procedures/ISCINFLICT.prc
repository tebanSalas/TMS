Prompt drop Procedure isconflict ;
DROP PROCEDURE isconflict 
/

Prompt Procedure isconflict ;
--
-- isconflict (Procedure) 
--
--
CREATE OR REPLACE PROCEDURE isconflict (
   userid_p      IN       NUMBER,
   day_p         IN       VARCHAR2,
   accountid_p   IN       NUMBER,
   hourini_p     IN       VARCHAR2,
   hourend_p     IN       VARCHAR2,
   conflict_p    OUT      NUMBER
)
AS
   conflict_v   NUMBER;
BEGIN
   SELECT count(s.hourid)
     INTO conflict_v
     FROM schedules s
    WHERE s.userid = userid_p
      AND s.DAY = day_p
      AND s.id_account = accountid_p
      AND (TO_DATE (day_p || hourini_p, 'RRRR-MM-DDHH:MI am')
                 BETWEEN TO_DATE (s.DAY || s.hour_start, 'RRRR-MM-DDHH:MI am')
                     AND (  TO_DATE (s.DAY || s.hour_end,
                                     'RRRR-MM-DDHH:MI am')
                          - (1 / (24 * 60 * 60))
                         )
           OR (  TO_DATE (DAY || hourend_p, 'RRRR-MM-DDHH:MI am')
               - (1 / (24 * 60 * 60))
              ) BETWEEN TO_DATE (s.DAY || s.hour_start, 'RRRR-MM-DDHH:MI am')
                    AND (  TO_DATE (s.DAY || s.hour_end, 'RRRR-MM-DDHH:MI am')
                         - (1 / (24 * 60 * 60))
                        )
           OR TO_DATE (s.DAY || s.hour_start, 'RRRR-MM-DDHH:MI am')
                 BETWEEN TO_DATE (day_p || hourini_p, 'RRRR-MM-DDHH:MI am')
                     AND (  TO_DATE (day_p || hourend_p ,
                                     'RRRR-MM-DDHH:MI am'
                                    )
                          - (1 / (24 * 60 * 60))
                         )
          );
   conflict_p := conflict_v;    
 /* empty */
EXCEPTION
     WHEN NO_DATA_FOUND THEN
       conflict_p :=0;
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;

END;
/


SHOW ERRORS;


