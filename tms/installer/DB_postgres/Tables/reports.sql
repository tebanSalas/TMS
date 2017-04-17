-- Table: tms.reports

-- DROP TABLE tms.reports;

CREATE TABLE tms.reports
(
  id integer NOT NULL,
  "owner" integer NOT NULL,
  "name" character varying(155),
  projects character varying(255),
  members character varying(255),
  priorities character varying(255),
  status character varying(255),
  start_date_start timestamp without time zone,
  end_date_start timestamp without time zone,
  start_date_due timestamp without time zone,
  end_date_due timestamp without time zone,
  ind_range_start_date character varying(10),
  ind_range_due_date character varying(10),
  created timestamp without time zone NOT NULL,
  spreadfix character varying(3),
  ind_range_end_date character varying(10),
  start_date_end timestamp without time zone,
  end_date_end timestamp without time zone,
  typetasks character varying(255),
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT reports_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_reports FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.reports OWNER TO tms;
