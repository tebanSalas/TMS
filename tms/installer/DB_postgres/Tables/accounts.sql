-- Table: tms.accounts

-- DROP TABLE tms.accounts;

CREATE TABLE tms.accounts
(
  id integer NOT NULL,
  "name" character varying(150),
  description character varying(150),
  address character varying(150),
  phone_1 character varying(25),
  phone_2 character varying(25),
  email character varying(155),
  account_logo character varying(255),
  user_fare numeric(10,2),
  account_email character varying(155),
  company character varying(150),
  creator character varying(150),
  website character varying(150),
  active character(1),
  shortname character varying(5),
  key_encriptation character varying(100),
  final_trial_date timestamp without time zone,
  main_url character varying(150),
  CONSTRAINT accounts_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.accounts OWNER TO tms;

-- Trigger: acc_bud on tms.accounts

-- DROP TRIGGER acc_bud ON tms.accounts;

CREATE TRIGGER acc_bud
  BEFORE UPDATE OR DELETE
  ON tms.accounts
  FOR EACH ROW
  EXECUTE PROCEDURE tms.pg_fct_acc_bud();

