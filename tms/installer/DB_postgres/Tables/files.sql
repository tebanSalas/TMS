-- Table: tms.files

-- DROP TABLE tms.files;

CREATE TABLE tms.files
(
  id integer NOT NULL,
  "owner" integer NOT NULL,
  project integer NOT NULL,
  task integer NOT NULL,
  "name" character varying(255) NOT NULL,
  "day" timestamp without time zone NOT NULL,
  length bigint NOT NULL,
  "type" character varying(100) NOT NULL,
  comments character varying(155) NOT NULL,
  upload timestamp without time zone NOT NULL,
  published character(1) NOT NULL,
  status integer NOT NULL,
  topic integer,
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT files_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_files FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_files_project FOREIGN KEY (project, id_account)
      REFERENCES tms.projects (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.files OWNER TO tms;

-- Index: tms.fk_files_projects

-- DROP INDEX tms.fk_files_projects;

CREATE INDEX fk_files_projects
  ON tms.files
  USING btree
  (project, id_account);

