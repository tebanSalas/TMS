-- Function: tms.getprojectinfo(numeric)

-- DROP FUNCTION tms.getprojectinfo(numeric);

CREATE OR REPLACE FUNCTION tms.getprojectinfo(IN projectid numeric, OUT totaltasks numeric, OUT totalended numeric, OUT totalnotassigned numeric, OUT totalsuspended numeric, OUT totalrejected numeric, OUT totallate numeric)
  RETURNS record AS
$BODY$
DECLARE
  tmp numeric ( 10 );
BEGIN
-- Todas las tareas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid AND status IN( 1, 2, 3, 4, 12 );
  totaltasks := tmp;
-- Todas las terminadas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid AND status = 1;
  totalended := tmp;
-- Tareas no asignadas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid AND status IN( 2, 3, 4, 12 ) AND assigned_to = 0;
  totalnotassigned := tmp;
-- Tareas suspendidas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid AND status = 4;
  totalsuspended := tmp;
  totalsuspended := 0;
-- Tareas rechazadas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid AND status = 12;
  totalrejected := tmp;
  totalrejected := 0;
-- Tareas atrazadas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid AND status IN( 2, 3, 4, 12 ) AND(    due_date IS NULL
                                                              OR due_date < CURRENT_DATE );
  totallate := tmp;
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tms.getprojectinfo(numeric) OWNER TO tms;
