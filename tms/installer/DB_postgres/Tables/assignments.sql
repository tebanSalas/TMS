-- Table: tms.assignments

-- DROP TABLE tms.assignments;

CREATE TABLE tms.assignments
(
  id integer NOT NULL,
  task integer NOT NULL,
  "owner" integer NOT NULL,
  assigned_to integer NOT NULL,
  assigned timestamp without time zone NOT NULL,
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT assignments_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_assig_tasks FOREIGN KEY (task, id_account)
      REFERENCES tms.tasks (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_assigments FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.assignments OWNER TO tms;
