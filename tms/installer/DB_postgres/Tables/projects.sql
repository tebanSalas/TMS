-- Table: tms.projects

-- DROP TABLE tms.projects;

CREATE TABLE tms.projects
(
  id integer NOT NULL,
  organization integer NOT NULL,
  "owner" integer NOT NULL,
  priority integer NOT NULL,
  status integer NOT NULL,
  "name" character varying(155) NOT NULL,
  description text NOT NULL,
  created timestamp without time zone NOT NULL,
  modified timestamp without time zone NOT NULL,
  published character(1) NOT NULL,
  upload_max character varying(155) NOT NULL,
  published_assigned character(1) NOT NULL,
  published_endtask character(1) NOT NULL,
  start_date timestamp without time zone,
  end_date timestamp without time zone,
  id_account integer NOT NULL DEFAULT 1,
  project_id integer,
  sec_projects character varying(100),
  send_email character(1) DEFAULT '2'::bpchar,
  autom_notification  character(1) DEFAULT '1',	
  CONSTRAINT projects_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_projects FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_projects_org FOREIGN KEY (organization, id_account)
      REFERENCES tms.organizations (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.projects OWNER TO tms;

-- Index: tms.idx_projects1

-- DROP INDEX tms.idx_projects1;

CREATE INDEX idx_projects1
  ON tms.projects
  USING btree
  (owner);

