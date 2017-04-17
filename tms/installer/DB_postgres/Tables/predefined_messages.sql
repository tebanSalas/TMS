-- Table: tms.predefined_messages

-- DROP TABLE tms.predefined_messages;

CREATE TABLE tms.predefined_messages
(
  id integer NOT NULL,
  "text" text NOT NULL,
  "name" character varying(20),
  id_account integer DEFAULT 1,
  CONSTRAINT predefined_messages_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE)
TABLESPACE tms_data;
ALTER TABLE tms.predefined_messages OWNER TO tms;
