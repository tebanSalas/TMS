-- Table: tms.master_report

-- DROP TABLE tms.master_report;

CREATE TABLE tms.master_report
(
  id integer NOT NULL,
  "name" character varying(155) NOT NULL,
  description text,
  CONSTRAINT master_report_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.master_report OWNER TO tms;
