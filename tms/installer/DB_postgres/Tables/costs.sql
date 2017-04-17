-- Table: tms.costs

-- DROP TABLE tms.costs;

CREATE TABLE tms.costs
(
  id integer NOT NULL,
  project integer NOT NULL,
  description text NOT NULL,
  units integer NOT NULL,
  standard_cost numeric(12,2) NOT NULL,
  real_cost numeric(12,2) NOT NULL,
  tasks integer NOT NULL,
  id_account integer NOT NULL DEFAULT 1,
  chargeable character(1) NOT NULL,
  additional_costs_date timestamp without time zone NOT NULL, 
  
  CONSTRAINT costs_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_costs FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_costs_project FOREIGN KEY (project, id_account)
      REFERENCES tms.projects (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_costs_tasks FOREIGN KEY (tasks, id_account)
      REFERENCES tms.tasks (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.costs OWNER TO tms;
