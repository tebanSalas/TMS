-- Table: access_log

-- DROP TABLE access_log;

CREATE TABLE access_log
(
  id_registry integer NOT NULL,
  id_member integer NOT NULL,
  login_date timestamp without time zone NOT NULL,
  id_account integer NOT NULL,
  "login" character varying(30) NOT NULL,
  logout_date timestamp without time zone,
  portal character varying(30) NOT NULL,
  CONSTRAINT access_log_pkey PRIMARY KEY (id_registry)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE access_log OWNER TO tms;
