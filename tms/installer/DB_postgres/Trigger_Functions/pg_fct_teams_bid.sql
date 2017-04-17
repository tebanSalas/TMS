-- Function: tms.pg_fct_teams_bid()

-- DROP FUNCTION tms.pg_fct_teams_bid();

CREATE OR REPLACE FUNCTION tms.pg_fct_teams_bid()
  RETURNS trigger AS
$BODY$
DECLARE
   id_v            numeric;
   nomacc_v        varchar (150);
   nomorg_v        varchar (150);
   idorg_v         numeric;
   nomproy_v       varchar (155);
   nom_user_v      varchar (155);
   action_v        varchar (1);
   id_account_v    numeric;
   nom_account_v   varchar (155);
   members_v	   numeric;
   project_v	   numeric;
/******************************************************************************
   NAME:       TEAMS_BIU
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        05/12/2008             1. Created this trigger.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     TEAMS_BIU
      CURRENT_TIMESTAMP:         05/12/2008
      Date and Time:   05/12/2008, 11:26:50 a.m., and 05/12/2008 11:26:50 a.m.
      Username:         (set in TOAD Options, Proc Templates)
      Table Name:      TEAMS (set in the "New PL/SQL Object" dialog)
      Trigger Options:  (set in the "New PL/SQL Object" dialog)
******************************************************************************/
BEGIN
   --take the max id
   SELECT COUNT (ID) + 1
     INTO id_v
     FROM team_lock;
   
   IF (TG_OP = 'INSERT') THEN
      members_v := NEW.members;
      project_v := NEW.projects;
      action_v := 'I';
   END IF;

   IF (TG_OP = 'UPDATE') THEN
     members_v := NEW.members;
     project_v := NEW.projects;
     action_v := 'U';
   END IF;

   IF (TG_OP = 'DELETE') THEN
      members_v := OLD.members;
      project_v := OLD.projects;
      action_v := 'D';
   END IF;
      
   --take the name of the account
   --  select name from account in to nomAcc_v where id = new.account;
   --take the name of the project
   SELECT NAME, ORGANIZATION
     INTO nomproy_v, idorg_v
     FROM projects p
    WHERE p.ID =  project_v;
		
   --take the name of the organization
   SELECT NAME
     INTO nomorg_v
     FROM organizations
    WHERE ID = idorg_v;

   --take the name of the user
   SELECT NAME, id_account
     INTO nom_user_v, id_account_v
     FROM members
    WHERE ID = members_v;

   --take the name of the account
   SELECT NAME
     INTO nom_account_v
     FROM accounts
    WHERE ID = id_account_v;

   INSERT INTO team_lock
        VALUES (id_v, id_account_v, nom_account_v, idorg_v, nomorg_v,
                 project_v, nomproy_v,  members_v, nom_user_v, action_v,
                CURRENT_TIMESTAMP);
   return null;
EXCEPTION
   WHEN raise_exception THEN
      -- Consider logging the error and then re-raise
      RAISE EXCEPTION 'Error %', SQLERRM;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tms.pg_fct_teams_bid() OWNER TO tms;
