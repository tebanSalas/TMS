ALTER TABLE PROJECTS
 ADD (ID_ACCOUNT           NUMBER(10)  )
/

update PROJECTS set id_account =2;


ALTER TABLE PROJECTS
MODIFY(ID_ACCOUNT NOT NULL);

ALTER TABLE PROJECTS
 ADD (PROJECT_ID  NUMBER(10));

update projects
set project_id = 0;

ALTER TABLE PROJECTS
 ADD (SEC_PROJECTS  VARCHAR2(100))
/
update projects
set sec_projects = concat('-',id)
where project_id = 0
/
update projects
set sec_projects = concat(sec_projects,'-')
where project_id = 0
/
