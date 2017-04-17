-- Table: tms.members

-- DROP TABLE tms.members;

CREATE TABLE tms.members
(
  id integer NOT NULL,
  organization integer NOT NULL,
  "login" character varying(50) NOT NULL,
  "password" character varying(100) NOT NULL,
  "name" character varying(155) NOT NULL,
  title character varying(155),
  email_work character varying(155) NOT NULL,
  email_home character varying(155),
  phone_work character varying(25),
  phone_home character varying(25),
  mobile character varying(25),
  fax character varying(25),
  comments text,
  profile character(1) NOT NULL,
  created timestamp without time zone NOT NULL,
  logout_time integer NOT NULL,
  last_page character varying(255),
  quotation character(1),
  personal_title character varying(155),
  reports character(1) NOT NULL,
  "template" character varying(50),
  "cost" numeric(12,2) DEFAULT '0',
  ind_check_schedules character(1),
  ind_end_tasks character(1),
  ind_client_manager character(1),
  time_zone character varying(45),
  id_account integer NOT NULL DEFAULT 1,
  expired_date timestamp without time zone,
  renew_date timestamp without time zone,
  sale_cost numeric(10,2),
  ind_exec_report character(1) DEFAULT 'N'::bpchar,
  ind_chat_available character(1) DEFAULT '0'::bpchar,
  CONSTRAINT members_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_members FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_members_org FOREIGN KEY (organization, id_account)
      REFERENCES tms.organizations (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.members OWNER TO tms;
