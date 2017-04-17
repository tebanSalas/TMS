Prompt drop Procedure GETPROJECTINFO;
DROP PROCEDURE GETPROJECTINFO
/

Prompt Procedure GETPROJECTINFO;
--
-- GETPROJECTINFO  (Procedure) 
--
--  Dependencies: 
--   STANDARD (Package)
--   SYS_STUB_FOR_PURITY_ANALYSIS (Package)
--   TASKS (Table)
--
CREATE OR REPLACE /* Formatted on 2007/09/19 15:38 (Formatter Plus v4.8.8) */
PROCEDURE getprojectinfo(
  projectid         IN      NUMBER
 ,totaltasks        OUT     NUMBER
 ,totalended        OUT     NUMBER
 ,totalnotassigned  OUT     NUMBER
 ,totalsuspended    OUT     NUMBER
 ,totalrejected     OUT     NUMBER
 ,totallate         OUT     NUMBER
) AS
  tmp  NUMBER( 10 );
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

  /*

  select count(*) into tmp from tasks

  where project = projectId

  and status = 4;

  totalSuspended := tmp;

  */
  totalsuspended := 0;
-- Tareas rechazadas

  /*

  select count(*) into tmp from tasks

  where project = projectId

  and status = 12;

  totalRejected := tmp;

  */
  totalrejected := 0;
-- Tareas atrazadas
  SELECT COUNT( * )
  INTO   tmp
  FROM tasks
  WHERE project = projectid
    AND status IN( 2, 3, 4, 12 )
    AND (    due_date IS NULL
          OR due_date < CURRENT_DATE );
  totallate := tmp;
END;
/

SHOW ERRORS;


