-- Table: tms.config_send_report

-- DROP TABLE tms.config_send_report;

CREATE TABLE tms.config_send_report
(
  id_account integer NOT NULL,
  id_master_report integer NOT NULL,
  members integer NOT NULL,
  periodicity character(1),
  notification integer,
  format character(1),
  CONSTRAINT config_send_report_pkey PRIMARY KEY (id_account, members, id_master_report),
  CONSTRAINT fk_account FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_master_report FOREIGN KEY (id_master_report)
      REFERENCES tms.master_report (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_member FOREIGN KEY (members, id_account)
      REFERENCES tms.members (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.config_send_report OWNER TO tms;
