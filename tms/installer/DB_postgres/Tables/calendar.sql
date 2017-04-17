-- Table: tms.calendar

-- DROP TABLE tms.calendar;

CREATE TABLE tms.calendar
(
  id integer NOT NULL,
  member integer NOT NULL,
  subject character varying(255) NOT NULL,
  description text,
  "day" timestamp without time zone NOT NULL,
  hour_start integer NOT NULL,
  hour_end integer NOT NULL,
  min_start integer NOT NULL,
  min_end integer NOT NULL,
  task integer,
  guests character varying(150),
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT calendar_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_cal_members FOREIGN KEY (member, id_account)
      REFERENCES tms.members (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_calendar FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.calendar OWNER TO tms;
