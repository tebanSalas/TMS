-- Table: chat_session

-- DROP TABLE chat_session;

CREATE TABLE chat_session
(
  id_session numeric NOT NULL,
  start_date timestamp without time zone NOT NULL,
  end_date timestamp without time zone,
  CONSTRAINT chat_session_pkey PRIMARY KEY (id_session)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE chat_session OWNER TO tms;
