-- Table: tms.topics

-- DROP TABLE tms.topics;

CREATE TABLE tms.topics
(
  id integer NOT NULL,
  project integer NOT NULL,
  "owner" integer NOT NULL,
  subject character varying(600) NOT NULL,
  status character(1) NOT NULL,
  last_post timestamp without time zone NOT NULL,
  posts integer NOT NULL,
  published character(1) NOT NULL,
  tasks integer NOT NULL,
  totasks character(1) NOT NULL,
  id_account integer NOT NULL DEFAULT 1,
  notified  character(1) DEFAULT '0',	
  CONSTRAINT topics_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_topics FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_topics_project FOREIGN KEY (project, id_account)
      REFERENCES tms.projects (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.topics OWNER TO tms;

-- Index: tms.idx_topics1

-- DROP INDEX tms.idx_topics1;

CREATE INDEX idx_topics1
  ON tms.topics
  USING btree
  (owner);

