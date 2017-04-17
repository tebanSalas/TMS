-- Function: tms.getrealtimeminutes(character varying, character varying, character varying, character varying)

-- DROP FUNCTION tms.getrealtimeminutes(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION tms.getrealtimeminutes(starttime character varying, endtime character varying, ampmstart character varying, ampmend character varying)
  RETURNS numeric AS
$BODY$
DECLARE
  auxminute numeric ;
BEGIN
 SELECT      date_part('MINUTES', TO_TIMESTAMP(TO_CHAR(TIMESTAMP 'now', 'YYYY-MM-DD') || endtime, 'YYYY-MM-DDHH:MI ' || ampmend )-
   TO_TIMESTAMP(TO_CHAR(TIMESTAMP 'now', 'YYYY-MM-DD') || starttime, 'YYYY-MM-DDHH:MI ' || ampmstart )) 
  INTO   auxminute
  ;
  RETURN auxminute;
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tms.getrealtimeminutes(character varying, character varying, character varying, character varying) OWNER TO tms;
