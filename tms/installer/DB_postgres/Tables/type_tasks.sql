-- Table: tms.type_tasks

-- DROP TABLE tms.type_tasks;

CREATE TABLE tms.type_tasks
(
  id integer NOT NULL,
  description character varying(155) NOT NULL,
  prefix character varying(7),
  consecutive integer,
  use_prefix character(1),
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT type_tasks_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_type_tasks FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.type_tasks OWNER TO tms;
