-- Table: tms.tasks

-- DROP TABLE tms.tasks;

CREATE TABLE tms.tasks
(
  id integer NOT NULL,
  project integer NOT NULL,
  priority integer NOT NULL,
  status integer NOT NULL,
  message integer NOT NULL,
  "owner" integer NOT NULL,
  assigned_to integer NOT NULL,
  "name" character varying(155) NOT NULL,
  description text,
  start_date timestamp without time zone NOT NULL,
  due_date timestamp without time zone,
  real_due_date timestamp without time zone NOT NULL,
  estimated_time numeric(10,2) NOT NULL,
  actual_time numeric(10,2) NOT NULL,
  completion integer NOT NULL,
  created timestamp without time zone NOT NULL,
  modified timestamp without time zone NOT NULL,
  assigned timestamp without time zone NOT NULL,
  published character(1) NOT NULL,
  collect character(1) NOT NULL,
  send_quotation_date timestamp without time zone,
  reply_quotation_date timestamp without time zone,
  reply_quotation_member integer,
  tolerance integer NOT NULL,
  fare numeric(12,2) NOT NULL,
  predecessor integer,
  predecessor_required character(1),
  severity character(1),
  type_task integer,
  spread_fix character(1),
  comments text NOT NULL,
  topic integer NOT NULL,
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT tasks_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_tasks FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_tasks_project FOREIGN KEY (project, id_account)
      REFERENCES tms.projects (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.tasks OWNER TO tms;

-- Index: tms.idx_tasks1

-- DROP INDEX tms.idx_tasks1;

CREATE INDEX idx_tasks1
  ON tms.tasks
  USING btree
  (project, status);

-- Index: tms.idx_tasks_2

-- DROP INDEX tms.idx_tasks_2;

CREATE INDEX idx_tasks_2
  ON tms.tasks
  USING btree
  (assigned_to, status);

