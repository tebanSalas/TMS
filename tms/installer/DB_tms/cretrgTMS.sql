/* Formatted on 2007/08/16 18:05 (Formatter Plus v4.8.8) */
/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     7/31/2007 11:50:39 AM                        */
/*==============================================================*/


DROP PROCEDURE getprojectinfo
/

CREATE OR REPLACE PROCEDURE getprojectinfo(
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

DROP PROCEDURE tms_copiar_projecto_pr
/

CREATE OR REPLACE PROCEDURE tms_copiar_projecto_pr(
  projecto_origen_p  NUMBER
 ,id_p               VARCHAR2
) IS
  CURSOR equipo_c IS
    SELECT members, published, authorized
    FROM teams
    WHERE projects = projecto_origen_p;

  CURSOR tareas_c IS
    SELECT priority, status, MESSAGE, owner, assigned_to, NAME, description, start_date, due_date
     ,real_due_date, estimated_time, actual_time, comments, completion, created, modified
     ,assigned, published, COLLECT, send_quotation_date, reply_quotation_date
     ,reply_quotation_member, topic, tolerance, fare, predecessor, predecessor_required, severity
     ,type_task, spread_fix
    FROM tasks
    WHERE project = projecto_origen_p;

  CURSOR costo_c IS
    SELECT description, units, standard_cost, real_cost, tasks
    FROM costs
    WHERE project = projecto_origen_p;

  max_project_v  NUMBER;
  max_team_v     NUMBER;
  max_task_v     NUMBER;
  max_cost_v     NUMBER;
BEGIN
  SELECT MAX( ID ) + 1
  INTO   max_project_v
  FROM projects;
  INSERT INTO projects
   ( ID, ORGANIZATION, owner, priority, status, NAME, description, created, modified, published
    ,upload_max, published_assigned, published_endtask, start_date, end_date )
    SELECT max_project_v, ORGANIZATION, owner, priority, status, NAME || id_p, description, created
     ,modified, published, upload_max, published_assigned, published_endtask, start_date, end_date
    FROM projects
    WHERE ID = projecto_origen_p;
  FOR i IN equipo_c LOOP
    SELECT MAX( ID ) + 1
    INTO   max_team_v
    FROM teams;
    INSERT INTO teams
           ( ID, projects, members, published, authorized
           )
    VALUES ( max_team_v, max_project_v, i.members, i.published, i.authorized
           );
  END LOOP;
  FOR j IN tareas_c LOOP
    SELECT MAX( ID ) + 1
    INTO   max_task_v
    FROM tasks;
    INSERT INTO tasks
           ( ID, project, priority, status, MESSAGE, owner, assigned_to
            ,NAME, description, start_date, due_date, real_due_date
            ,estimated_time, actual_time, comments, completion, created, modified
            ,assigned, published, COLLECT, send_quotation_date, reply_quotation_date
            ,reply_quotation_member, topic, tolerance, fare, predecessor
            ,predecessor_required, severity, type_task, spread_fix
           )
    VALUES ( max_task_v, max_project_v, j.priority, j.status, j.MESSAGE, j.owner, j.assigned_to
            , j.NAME || id_p, j.description, j.start_date, j.due_date, j.real_due_date
            ,j.estimated_time, j.actual_time, j.comments, j.completion, j.created, j.modified
            ,j.assigned, j.published, j.COLLECT, j.send_quotation_date, j.reply_quotation_date
            ,j.reply_quotation_member, j.topic, j.tolerance, j.fare, j.predecessor
            ,j.predecessor_required, j.severity, j.type_task, j.spread_fix
           );
  END LOOP;
  FOR k IN costo_c LOOP
    SELECT MAX( ID ) + 1
    INTO   max_cost_v
    FROM costs;
    INSERT INTO costs
           ( ID, project, description, units, standard_cost, real_cost
            ,tasks
           )
    VALUES ( max_cost_v, max_project_v, k.description, k.units, k.standard_cost, k.real_cost
            ,k.tasks
           );
  END LOOP;
END;
/
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
   SELECT s.hourid
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
