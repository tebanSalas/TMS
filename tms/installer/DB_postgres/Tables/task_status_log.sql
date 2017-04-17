-- Table: tms.task_status_log

-- DROP TABLE tms.task_status_log;

CREATE TABLE tms.TASK_STATUS_LOG 
(
ID        integer ,
ID_ACCOUNT     integer ,
TASK        integer ,
MEMBER        integer ,
STATUS        integer ,
CREATED        timestamp,
CONSTRAINT pk_task_status_log PRIMARY KEY (id)
);