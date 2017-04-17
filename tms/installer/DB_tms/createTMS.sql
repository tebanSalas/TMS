/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     7/31/2007 11:50:14 AM                        */
/*==============================================================*/


drop index IDX_ASSIGMENTS1;

drop index IDX_PROJECTS1;

drop index IDX_RISKS1;

drop index IDX_TASKS1;

drop index IDX_TASKS_2;

drop index IDX_TASKS_3;

drop index IDX_TEAMS1;

drop index IDX_TOPICS1;

drop table ASSIGNMENTS cascade constraints;

drop table CALENDAR cascade constraints;

drop table COSTS cascade constraints;

drop table COUNTRIES cascade constraints;

drop table FILES cascade constraints;

drop table LOGS cascade constraints;

drop table MEMBERS cascade constraints;

drop table ORGANIZATIONS cascade constraints;

drop table POST cascade constraints;

drop table PREDEFINED_MESSAGES cascade constraints;

drop table PROJECTS cascade constraints;

drop table REPORTS cascade constraints;

drop table RISKS cascade constraints;

drop table SCHEDULES cascade constraints;

drop table TASKS cascade constraints;

drop table TEAMS cascade constraints;

drop table TOPICS cascade constraints;

drop table TYPE_TASKS cascade constraints;

/*==============================================================*/
/* Table: ASSIGNMENTS                                           */
/*==============================================================*/
create table ASSIGNMENTS  (
   ID                   NUMBER(10)                      not null,
   TASK                 NUMBER(10)                      not null,
   OWNER                NUMBER(10)                      not null,
   ASSIGNED_TO          NUMBER(10)                      not null,
   ASSIGNED             DATE                            not null,
   constraint PK_ASSIGNMENTS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 128K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 192K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Index: IDX_ASSIGMENTS1                                       */
/*==============================================================*/
create index IDX_ASSIGMENTS1 on ASSIGNMENTS (
   TASK ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 128K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Table: CALENDAR                                              */
/*==============================================================*/
create table CALENDAR  (
   ID                   NUMBER(10)                      not null,
   MEMBER               NUMBER(10)                      not null,
   SUBJECT              VARCHAR2(255)                   not null,
   DESCRIPTION          CLOB,
   DAY                  DATE                            not null,
   HOUR_START           NUMBER(10)                      not null,
   HOUR_END             NUMBER(10)                      not null,
   MIN_START            NUMBER(10)                      not null,
   MIN_END              NUMBER(10)                      not null,
   TASK                 NUMBER(10),
   GUESTS               VARCHAR2(150),
   constraint PK_CALENDAR primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 128K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 896K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( DESCRIPTION )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Table: COSTS                                                 */
/*==============================================================*/
create table COSTS  (
   ID                   NUMBER(10)                      not null,
   PROJECT              NUMBER(10)                      not null,
   DESCRIPTION          CLOB                            not null,
   UNITS                NUMBER(10)                      not null,
   STANDARD_COST        NUMBER(12,2)                    not null,
   REAL_COST            NUMBER(12,2)                    not null,
   TASKS                NUMBER(10)                      not null,
   constraint PK_COSTS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( DESCRIPTION )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Table: COUNTRIES                                             */
/*==============================================================*/
create table COUNTRIES  (
   COD_PAIS             NUMBER(10)                      not null,
   DESCRIPTION          VARCHAR2(155)                   not null,
   constraint PK_COUNTRIES primary key (COD_PAIS)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Table: FILES                                                 */
/*==============================================================*/
create table FILES  (
   ID                   NUMBER(10)                      not null,
   OWNER                NUMBER(10)                      not null,
   PROJECT              NUMBER(10)                      not null,
   TASK                 NUMBER(10)                      not null,
   NAME                 VARCHAR2(255)                   not null,
   DAY                  DATE                            not null,
   LENGTH               NUMBER(15)                      not null,
   TYPE                 VARCHAR2(60)                    not null,
   COMMENTS             VARCHAR2(155)                   not null,
   UPLOAD               DATE                            not null,
   PUBLISHED            CHAR                            not null,
   STATUS               NUMBER(10)                      not null,
   TOPIC                NUMBER(10),
   constraint PK_FILES primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 128K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 512K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Table: LOGS                                                  */
/*==============================================================*/
create table LOGS  (
   ID                   NUMBER(10)                      not null,
   LOGIN                VARCHAR2(155)                   not null,
   PASSWORD             VARCHAR2(155)                   not null,
   IP                   VARCHAR2(155),
   SESSION_ID           VARCHAR2(155),
   COMPT                NUMBER(10),
   LAST_VISITE          DATE,
   CONNECTED            VARCHAR2(255),
   constraint PK_LOGS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 192K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 1024K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Table: MEMBERS                                               */
/*==============================================================*/
create table MEMBERS  (
   ID                   NUMBER(10)                      not null,
   ORGANIZATION         NUMBER(10)                      not null,
   LOGIN                VARCHAR2(50)                    not null,
   PASSWORD             VARCHAR2(100)                   not null,
   NAME                 VARCHAR2(155)                   not null,
   TITLE                VARCHAR2(155),
   EMAIL_WORK           VARCHAR2(155)                   not null,
   EMAIL_HOME           VARCHAR2(155),
   PHONE_WORK           VARCHAR2(25),
   PHONE_HOME           VARCHAR2(25),
   MOBILE               VARCHAR2(25),
   FAX                  VARCHAR2(25),
   COMMENTS             CLOB,
   PROFILE              CHAR                            not null,
   CREATED              DATE                            not null,
   LOGOUT_TIME          NUMBER(10)                      not null,
   LAST_PAGE            VARCHAR2(255),
   QUOTATION            CHAR,
   PERSONAL_TITLE       VARCHAR2(155),
   REPORTS              CHAR                            not null,
   TEMPLATE             VARCHAR2(50),
   COST                 NUMBER(12,2),
   IND_CHECK_SCHEDULES  CHAR,
   IND_END_TASKS        CHAR,
   IND_CLIENT_MANAGER   CHAR,
   constraint PK_MEMBERS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( COMMENTS )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

comment on column MEMBERS.IND_CLIENT_MANAGER is
'Indica si el usuario es administrador de proyecto del cliente';

/*==============================================================*/
/* Table: ORGANIZATIONS                                         */
/*==============================================================*/
create table ORGANIZATIONS  (
   ID                   NUMBER(10)                      not null,
   COD_PAIS             NUMBER(10)                      not null,
   NAME                 VARCHAR2(150)                   not null,
   ADDRESS1             VARCHAR2(255)                   not null,
   ADDRESS2             VARCHAR2(255),
   ZIP_CODE             VARCHAR2(10),
   CITY                 VARCHAR2(155),
   PHONE                VARCHAR2(20)                    not null,
   URL                  VARCHAR2(255),
   EMAIL                VARCHAR2(155)                   not null,
   COMMENTS             CLOB,
   CREATED              DATE                            not null,
   EMAIL_COLLECT        VARCHAR2(155),
   constraint PK_ORGANIZATIONS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( COMMENTS )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Table: POST                                                  */
/*==============================================================*/
create table POST  (
   ID                   NUMBER(10)                      not null,
   TOPIC                NUMBER(10)                      not null,
   MEMBER               NUMBER(10)                      not null,
   CREATED              DATE                            not null,
   MESSAGE              CLOB                            not null,
   constraint PK_POST primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 256K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( MESSAGE )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Table: PREDEFINED_MESSAGES                                   */
/*==============================================================*/
create table PREDEFINED_MESSAGES  (
   ID                   NUMBER(10)                      not null,
   TEXT                 CLOB                            not null,
   NAME                 VARCHAR2(20),
   constraint PK_PREDEFINED_MESSAGES primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( TEXT )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Table: PROJECTS                                              */
/*==============================================================*/
create table PROJECTS  (
   ID                   NUMBER(10)                      not null,
   ORGANIZATION         NUMBER(10)                      not null,
   OWNER                NUMBER(10)                      not null,
   PRIORITY             NUMBER(10)                      not null,
   STATUS               NUMBER(10)                      not null,
   NAME                 VARCHAR2(155)                   not null,
   DESCRIPTION          CLOB                            not null,
   CREATED              DATE                            not null,
   MODIFIED             DATE                            not null,
   PUBLISHED            CHAR                            not null,
   UPLOAD_MAX           VARCHAR2(155)                   not null,
   PUBLISHED_ASSIGNED   CHAR                            not null,
   PUBLISHED_ENDTASK    CHAR                            not null,
   START_DATE           DATE,
   END_DATE             DATE,
   constraint PK_PROJECTS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( DESCRIPTION )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Index: IDX_PROJECTS1                                         */
/*==============================================================*/
create index IDX_PROJECTS1 on PROJECTS (
   OWNER ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Table: REPORTS                                               */
/*==============================================================*/
create table REPORTS  (
   ID                   NUMBER(10)                      not null,
   OWNER                NUMBER(10)                      not null,
   NAME                 VARCHAR2(155),
   PROJECTS             VARCHAR2(255),
   MEMBERS              VARCHAR2(255),
   PRIORITIES           VARCHAR2(255),
   STATUS               VARCHAR2(255),
   START_DATE_START     DATE,
   END_DATE_START       DATE,
   START_DATE_DUE       DATE,
   END_DATE_DUE         DATE,
   IND_RANGE_START_DATE VARCHAR2(10),
   IND_RANGE_DUE_DATE   VARCHAR2(10),
   CREATED              DATE                            not null,
   SPREADFIX            VARCHAR2(3),
   IND_RANGE_END_DATE   VARCHAR2(10),
   START_DATE_END       DATE,
   END_DATE_END         DATE,
   TYPETASKS            VARCHAR2(255),
   constraint PK_REPORTS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Table: RISKS                                                 */
/*==============================================================*/
create table RISKS  (
   ID                   NUMBER(10)                      not null,
   DESCRIPTION          VARCHAR2(255)                   not null,
   PROBABILITY          NUMBER(10),
   IMPACT               NUMBER(10),
   TODOACTION           CLOB,
   PLANB                VARCHAR2(255),
   TASK                 NUMBER(10),
   PROJECT              NUMBER(10),
   constraint PK_RISKS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
pctused 40
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL
logging
  lob
 ( TODOACTION )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         logging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Index: IDX_RISKS1                                            */
/*==============================================================*/
create index IDX_RISKS1 on RISKS (
   PROJECT ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Table: SCHEDULES                                             */
/*==============================================================*/
create table SCHEDULES  (
   HOURID               NUMBER(10)                      not null,
   USERID               NUMBER(10)                      not null,
   DAY                  VARCHAR2(20)                    not null,
   TASKID               NUMBER(10),
   COMMENTS             CLOB,
   LAST_UPDATE          DATE                            not null,
   HOUR_NAME            VARCHAR2(8),
   constraint PK_SCHEDULES primary key (HOURID, USERID, DAY)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 2048K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 4096K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( COMMENTS )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Table: TASKS                                                 */
/*==============================================================*/
create table TASKS  (
   ID                   NUMBER(10)                      not null,
   PROJECT              NUMBER(10)                      not null,
   PRIORITY             NUMBER(10)                      not null,
   STATUS               NUMBER(10)                      not null,
   MESSAGE              NUMBER(10)                      not null,
   OWNER                NUMBER(10)                      not null,
   ASSIGNED_TO          NUMBER(10)                      not null,
   NAME                 VARCHAR2(155)                   not null,
   DESCRIPTION          CLOB,
   START_DATE           DATE                            not null,
   DUE_DATE             DATE,
   REAL_DUE_DATE        DATE                            not null,
   ESTIMATED_TIME       NUMBER(10,2)                    not null,
   ACTUAL_TIME          NUMBER(10,2)                    not null,
   COMMENTS             VARCHAR2(3000)                  not null,
   COMPLETION           NUMBER(10)                      not null,
   CREATED              DATE                            not null,
   MODIFIED             DATE                            not null,
   ASSIGNED             DATE                            not null,
   PUBLISHED            CHAR                            not null,
   COLLECT              CHAR                            not null,
   SEND_QUOTATION_DATE  DATE,
   REPLY_QUOTATION_DATE DATE,
   REPLY_QUOTATION_MEMBER NUMBER(10),
   TOPIC                CHAR                            not null,
   TOLERANCE            NUMBER(10)                      not null,
   FARE                 NUMBER(12,2)                    not null,
   PREDECESSOR          NUMBER(10),
   PREDECESSOR_REQUIRED CHAR,
   SEVERITY             CHAR,
   TYPE_TASK            NUMBER(10),
   SPREAD_FIX           CHAR,
   constraint PK_TASKS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 128K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 3072K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
  lob
 ( DESCRIPTION )
    store as  (
        chunk 8192
        pctversion 10
         nocache
         nologging
         enable storage in row
    )
monitoring
  noparallel;

/*==============================================================*/
/* Index: IDX_TASKS1                                            */
/*==============================================================*/
create index IDX_TASKS1 on TASKS (
   PROJECT ASC,
   STATUS ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 128K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Index: IDX_TASKS_2                                           */
/*==============================================================*/
create index IDX_TASKS_2 on TASKS (
   ASSIGNED_TO ASC,
   STATUS ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 128K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Index: IDX_TASKS_3                                           */
/*==============================================================*/
create index IDX_TASKS_3 on TASKS (
   PROJECT ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 128K
    minextents 1
    maxextents unlimited
)
tablespace USERS;

/*==============================================================*/
/* Table: TEAMS                                                 */
/*==============================================================*/
create table TEAMS  (
   ID                   NUMBER(10)                      not null,
   PROJECTS             NUMBER(10)                      not null,
   MEMBERS              NUMBER(10)                      not null,
   PUBLISHED            CHAR                            not null,
   AUTHORIZED           CHAR                            not null,
   constraint PK_TEAMS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Index: IDX_TEAMS1                                            */
/*==============================================================*/
create index IDX_TEAMS1 on TEAMS (
   PROJECTS ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Table: TOPICS                                                */
/*==============================================================*/
create table TOPICS  (
   ID                   NUMBER(10)                      not null,
   PROJECT              NUMBER(10)                      not null,
   OWNER                NUMBER(10)                      not null,
   SUBJECT              VARCHAR2(600)                   not null,
   STATUS               CHAR                            not null,
   LAST_POST            DATE                            not null,
   POSTS                NUMBER(10)                      not null,
   PUBLISHED            CHAR                            not null,
   TASKS                NUMBER(10)                      not null,
   TOTASKS              CHAR                            not null,
   constraint PK_TOPICS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

/*==============================================================*/
/* Index: IDX_TOPICS1                                           */
/*==============================================================*/
create index IDX_TOPICS1 on TOPICS (
   OWNER ASC
)
pctfree 10
initrans 2
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL;

/*==============================================================*/
/* Table: TYPE_TASKS                                            */
/*==============================================================*/
create table TYPE_TASKS  (
   ID                   NUMBER(10)                      not null,
   DESCRIPTION          VARCHAR2(155)                   not null,
   PREFIX               VARCHAR2(7),
   CONSECUTIVE          NUMBER(5),
   USE_PREFIX           CHAR,
   constraint PK_TYPE_TASKS primary key (ID)
         using index
       pctfree 10
       initrans 2
       maxtrans 255
       tablespace TMSTRIAL
       storage
       (
           initial 64K
           minextents 1
           maxextents unlimited
           freelists 1
           freelist groups 1
       )
        logging
)
  pctfree 10
pctused 40
initrans 1
maxtrans 255
storage
(
    initial 64K
    minextents 1
    maxextents unlimited
    freelists 1
    freelist groups 1
)
tablespace TMSTRIAL
logging
monitoring
  noparallel;

