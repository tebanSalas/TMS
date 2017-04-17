-- Function: tms.isconflict(numeric, character varying, numeric, character varying, character varying)

-- DROP FUNCTION tms.isconflict(numeric, character varying, numeric, character varying, character varying);

CREATE OR REPLACE FUNCTION tms.isconflict(userid_p numeric, day_p character varying, accountid_p numeric, hourini_p character varying, hourend_p character varying)
  RETURNS numeric AS
$BODY$
DECLARE
  conflict numeric ;
BEGIN
SELECT count(s.hourid) 
FROM schedules s 
WHERE s.userid = userid_p AND 
s.DAY = day_p AND 
s.id_account = accountid_p AND 
(TO_TIMESTAMP(day_p || hourini_p,'YYYY-MM-DDHH:MI am' ) 
 BETWEEN TO_TIMESTAMP( s.DAY || s.hour_start  ,'YYYY-MM-DDHH:MI am') 
 AND( TO_TIMESTAMP( s.DAY || s.hour_end  ,'YYYY-MM-DDHH:MI am') - interval '1 second') 
 OR ( TO_TIMESTAMP( day_p || hourend_p, 'YYYY-MM-DDHH:MI am' ) - interval '1 second' )
  
BETWEEN TO_TIMESTAMP( s.DAY || s.hour_start, 'YYYY-MM-DDHH:MI am' )  AND
(TO_TIMESTAMP( s.DAY || s.hour_end, 'YYYY-MM-DDHH:MI am' )  - interval '1 second'  )  OR 
TO_TIMESTAMP( s.DAY || s.hour_start, 'YYYY-MM-DDHH:MI am' ) BETWEEN TO_TIMESTAMP( day_p || hourini_p ,'YYYY-MM-DDHH:MI am') AND
(TO_TIMESTAMP( day_p || hourend_p,'YYYY-MM-DDHH:MI am') - interval '1 second'  ))

 
  INTO   conflict
  ;
  RETURN conflict;
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tms.isconflict(numeric, character varying, numeric, character varying, character varying) OWNER TO tms;
