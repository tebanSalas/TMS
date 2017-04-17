-- Function: tms.getrealtimehour(character varying, character varying, character varying, character varying)

-- DROP FUNCTION tms.getrealtimehour(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION tms.getrealtimehour(starttime character varying, endtime character varying, ampmstart character varying, ampmend character varying)
  RETURNS numeric AS
$BODY$
DECLARE
  auxhour numeric ;
BEGIN

	  SELECT     date_part('HOURS',  (TO_TIMESTAMP(TO_CHAR(TIMESTAMP 'now', 'YYYY-MM-DD') || endtime, 'YYYY-MM-DDHH:MI ' || ampmend )-
  TO_TIMESTAMP(TO_CHAR(TIMESTAMP 'now', 'YYYY-MM-DD') || starttime, 'YYYY-MM-DDHH:MI ' || ampmstart ) ))  INTO   auxhour;

  RETURN auxhour;
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tms.getrealtimehour(character varying, character varying, character varying, character varying) OWNER TO tms;
