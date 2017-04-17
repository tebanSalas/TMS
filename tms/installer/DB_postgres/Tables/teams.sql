-- Table: tms.teams

-- DROP TABLE tms.teams;

CREATE TABLE tms.teams
(
  id integer NOT NULL,
  projects integer NOT NULL,
  members integer NOT NULL,
  published character(1) NOT NULL,
  authorized character(1) NOT NULL,
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT teams_pkey PRIMARY KEY (id_account, id),
  CONSTRAINT fk_teams_members FOREIGN KEY (members, id_account)
      REFERENCES tms.members (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_teams_projects FOREIGN KEY (projects, id_account)
      REFERENCES tms.projects (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.teams OWNER TO tms;

-- Index: tms.idx_teams1

-- DROP INDEX tms.idx_teams1;

CREATE INDEX idx_teams1
  ON tms.teams
  USING btree
  (projects);

-- Index: tms.pk_teams

-- DROP INDEX tms.pk_teams;

CREATE UNIQUE INDEX pk_teams
  ON tms.teams
  USING btree
  (id);


-- Trigger: teams_bid on tms.teams

-- DROP TRIGGER teams_bid ON tms.teams;

CREATE TRIGGER teams_bid
  AFTER INSERT OR UPDATE OR DELETE
  ON tms.teams
  FOR EACH ROW
  EXECUTE PROCEDURE tms.pg_fct_teams_bid();

