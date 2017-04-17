-- Table: tms.organizations

-- DROP TABLE tms.organizations;

CREATE TABLE tms.organizations
(
  id integer NOT NULL,
  cod_pais integer NOT NULL,
  "name" character varying(150) NOT NULL,
  address1 character varying(255) NOT NULL,
  address2 character varying(255),
  zip_code character varying(10),
  city character varying(155),
  phone character varying(20) NOT NULL,
  url character varying(255),
  email character varying(155) NOT NULL,
  comments text,
  created timestamp without time zone NOT NULL,
  email_collect character varying(155),
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT organizations_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_org_cod_pais FOREIGN KEY (cod_pais)
      REFERENCES tms.countries (cod_pais) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_organizations FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.organizations OWNER TO tms;
