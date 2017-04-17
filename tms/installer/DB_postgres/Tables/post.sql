-- Table: tms.post

-- DROP TABLE tms.post;

CREATE TABLE tms.post
(
  id integer NOT NULL,
  topic integer NOT NULL,
  member integer NOT NULL,
  created timestamp without time zone NOT NULL,
  message text NOT NULL,
  id_account integer NOT NULL DEFAULT 1,
  CONSTRAINT post_pkey PRIMARY KEY (id, id_account),
  CONSTRAINT fk_post FOREIGN KEY (id_account)
      REFERENCES tms.accounts (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_post_member FOREIGN KEY (member, id_account)
      REFERENCES tms.members (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
  CONSTRAINT fk_post_topic FOREIGN KEY (topic, id_account)
      REFERENCES tms.topics (id, id_account) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.post OWNER TO tms;
