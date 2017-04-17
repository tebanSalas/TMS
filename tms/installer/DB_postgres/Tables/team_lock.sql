-- Table: tms.team_lock

-- DROP TABLE tms.team_lock;

CREATE TABLE tms.team_lock
(
  id integer NOT NULL,
  account_id integer NOT NULL,
  account_name character varying(155),
  org_id integer,
  org_name character varying(155),
  project_id integer,
  project_name character varying(155),
  user_id integer,
  user_name character varying(155),
  "action" character(1),
  action_date timestamp without time zone,
  CONSTRAINT team_lock_pkey PRIMARY KEY (id, account_id)
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.team_lock OWNER TO tms;
