-- Table: members_per_session

-- DROP TABLE members_per_session;

CREATE TABLE members_per_session
(
  date_ended timestamp without time zone,
  session_id numeric NOT NULL,
  member_id numeric NOT NULL,
  date_created timestamp without time zone,
  CONSTRAINT members_per_session_pkey PRIMARY KEY (session_id, member_id),
  CONSTRAINT fk_chat_session FOREIGN KEY (session_id)
      REFERENCES chat_session (id_session) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE members_per_session OWNER TO tms;
