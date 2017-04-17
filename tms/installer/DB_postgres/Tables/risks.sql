-- Table: tms.risks

-- DROP TABLE tms.risks;

CREATE TABLE tms.risks
(
  id integer NOT NULL,
  description character varying(255) NOT NULL,
  probability integer,
  impact integer,
  todoaction text,
  planb character varying(255),
  task integer,
  project integer,
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT risks_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_risks FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_risks_project FOREIGN KEY (project, id_account)
      REFERENCES tms.projects (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_risks_tasks FOREIGN KEY (task, id_account)
      REFERENCES tms.tasks (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.risks OWNER TO tms;

-- Index: tms.idx_risks1

-- DROP INDEX tms.idx_risks1;

CREATE INDEX idx_risks1
  ON tms.risks
  USING btree
  (project);

