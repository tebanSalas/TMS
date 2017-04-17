-- Table: tms.schedules

-- DROP TABLE tms.schedules;

CREATE TABLE tms.schedules
(
  hourid integer NOT NULL,
  userid integer NOT NULL,
  "day" character varying(20) NOT NULL,
  id_account integer NOT NULL,
  taskid integer NOT NULL,
  comments text,
  last_update timestamp without time zone,
  hour_start character varying(8) NOT NULL,
  hour_end character varying(8) NOT NULL,
  realtime_hours integer NOT NULL,
  realtime_minutes integer NOT NULL,
  CONSTRAINT schedules_pkey PRIMARY KEY (hourid, userid, day, id_account),
  CONSTRAINT fk_schedules FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_schedules_member FOREIGN KEY (userid, id_account)
      REFERENCES tms.members (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_schedules_tasks FOREIGN KEY (taskid, id_account)
      REFERENCES tms.tasks (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.schedules OWNER TO tms;
