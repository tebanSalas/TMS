-- Table: tms.logs

-- DROP TABLE tms.logs;

CREATE TABLE tms.logs
(
  id integer NOT NULL,
  "login" character varying(155) NOT NULL,
  "password" character varying(155) NOT NULL,
  ip character varying(155),
  session_id character varying(155),
  compt integer,
  last_visite timestamp without time zone,
  connected character varying(255),
  CONSTRAINT logs_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.logs OWNER TO tms;
