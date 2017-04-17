CREATE TABLE TASK_STATUS_LOG 
(
ID        number(10) ,
ID_ACCOUNT     number(10) ,
TASK        number(10) ,
MEMBER        number(10) ,
STATUS        number(10) ,
CREATED        date,
CONSTRAINT pk_task_status_log PRIMARY KEY (id)
);
/