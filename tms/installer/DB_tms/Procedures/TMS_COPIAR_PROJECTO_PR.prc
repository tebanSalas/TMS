Prompt drop Procedure TMS_COPIAR_PROJECTO_PR;
DROP PROCEDURE TMS_COPIAR_PROJECTO_PR
/

Prompt Procedure TMS_COPIAR_PROJECTO_PR;
--
-- TMS_COPIAR_PROJECTO_PR  (Procedure) 
--
--  Dependencies: 
--   STANDARD (Package)
--   SYS_STUB_FOR_PURITY_ANALYSIS (Package)
--   COSTS (Table)
--   PROJECTS (Table)
--   TASKS (Table)
--   TEAMS (Table)
--
CREATE OR REPLACE /* Formatted on 2007/09/19 15:38 (Formatter Plus v4.8.8) */
PROCEDURE tms_copiar_projecto_pr(
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
     ( ID, projects, members, published, authorized )
    VALUES( max_team_v, max_project_v, i.members, i.published, i.authorized );
  END LOOP;
  FOR j IN tareas_c LOOP
    SELECT MAX( ID ) + 1
    INTO   max_task_v
    FROM tasks;
    INSERT INTO tasks
     ( ID, project, priority, status, MESSAGE, owner, assigned_to, NAME, description, start_date
      ,due_date, real_due_date, estimated_time, actual_time, comments, completion, created
      ,modified, assigned, published, COLLECT, send_quotation_date, reply_quotation_date
      ,reply_quotation_member, topic, tolerance, fare, predecessor, predecessor_required, severity
      ,type_task, spread_fix )
    VALUES( max_task_v, max_project_v, j.priority, j.status, j.MESSAGE, j.owner, j.assigned_to
      , j.NAME || id_p, j.description, j.start_date, j.due_date, j.real_due_date, j.estimated_time
      ,j.actual_time, j.comments, j.completion, j.created, j.modified, j.assigned, j.published
      ,j.COLLECT, j.send_quotation_date, j.reply_quotation_date, j.reply_quotation_member, j.topic
      ,j.tolerance, j.fare, j.predecessor, j.predecessor_required, j.severity, j.type_task
      ,j.spread_fix );
  END LOOP;
  FOR k IN costo_c LOOP
    SELECT MAX( ID ) + 1
    INTO   max_cost_v
    FROM costs;
    INSERT INTO costs
     ( ID, project, description, units, standard_cost, real_cost, tasks )
    VALUES( max_cost_v, max_project_v, k.description, k.units, k.standard_cost, k.real_cost
      ,k.tasks );
  END LOOP;
END;
/

SHOW ERRORS;


