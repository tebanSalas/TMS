-- Table: tms.countries

-- DROP TABLE tms.countries;

CREATE TABLE tms.countries
(
  cod_pais integer NOT NULL,
  description character varying(155) NOT NULL,
  CONSTRAINT countries_pkey PRIMARY KEY (cod_pais)
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.countries OWNER TO tms;
