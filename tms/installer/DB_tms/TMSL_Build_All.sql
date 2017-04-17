--
-- Drop and Create Script 
--   Database Version   : 10.2.0.1.0 
--   TOAD Version       : 9.0.1.8 
--   DB Connect String  : DEMOS.SOCASE.CR.COM 
--   Schema             : TMSTRIAL 
--   Script Created by  : TMSTRIAL 
--   Script Created at  : 19/09/2007 15:38:28 
--   Physical Location  :  
--   Notes              :  
--

-- Object Counts: 
--   Indexes: 25        Columns: 29         
--   Procedures: 2      Lines of Code: 148 
--   Tables: 18         Columns: 193        Constraints: 18     



-- "Set define off" turns off substitution variables. 
Set define off; 

@./Tables/ACCOUNTS.sql;
@./Tables/ASSIGNMENTS.sql;
@./Tables/ASSIGNMENTS_ALTER.sql;
@./Tables/CALENDAR.sql;
@./Tables/CALENDAR_ALTER.sql;
@./Tables/CONFIG_SEND_REPORT.sql;
@./Tables/ORGANIZATIONS.sql;
@./Tables/ORGANIZATIONS_ALTER.sql;
@./Tables/COSTS.sql;
@./Tables/COSTS_ALTER.sql;
@./Tables/COUNTRIES.sql;
@./Tables/FILES.sql;
@./Tables/LOGS.sql;
@./Tables/MASTER_REPORT.sql;
@./Tables/MEMBERS.sql;
@./Tables/MEMBERS_ALTER.sql;
@./Tables/POSTS_ALTER.sql;
@./Tables/POST.sql;
@./Tables/PREDEFINED_MESSAGES.sql;
@./Tables/PROJECTS.sql;
@./Tables/PROJECT_ALTER.sql;
@./Tables/REPORTS.sql;
@./Tables/REPORTS_ALTER.sql;
@./Tables/RISKS.sql;
@./Tables/SCHEDULES.sql;
@./Tables/SCHEDULES_ALTER.sql;
@./Tables/TASKS.sql;
@./Tables/TASKS_ALTER.sql;
@./Tables/TEAMS.sql;
@./Tables/TEAMS_ALTER.sql;
@./Tables/TOPICS.sql;
@./Tables/TOPICS_ALTER.sql;
@./Tables/TYPE_TASKS.sql;
@./Tables/TYPE_TASKS_ALTER.sql;
@./Indexes/PK_ACCOUNTS.sql;
@./Indexes/PK_CONFIG_SEND_REPORT.sql;
@./Indexes/PK_TYPE_TASKS.sql;
@./Indexes/IDX_TOPICS1.sql;
@./Indexes/PK_TOPICS.sql;
@./Indexes/IDX_TEAMS1.sql;
@./Indexes/PK_TEAMS.sql;
@./Indexes/IDX_TASKS_2.sql;
@./Indexes/IDX_TASKS1.sql;
@./Indexes/PK_TASKS.sql;
@./Indexes/PK_SCHEDULES.sql;
@./Indexes/IDX_RISKS1.sql;
@./Indexes/PK_RISKS.sql;
@./Indexes/PK_REPORTS.sql;
@./Indexes/IDX_PROJECTS1.sql;
@./Indexes/PK_PROJECTS.sql;
@./Indexes/PK_PREDEFINED_MESSAGES.sql;
@./Indexes/PK_POST.sql;
@./Indexes/PK_ORGANIZATIONS.sql;
@./Indexes/PK_MEMBERS.sql;
@./Indexes/PK_MASTER_REPORT.sql;
@./Indexes/PK_LOGS.sql;
@./Indexes/PK_FILES.sql;
@./Indexes/PK_COUNTRIES.sql;
@./Indexes/PK_COSTS.sql;
@./Indexes/PK_CALENDAR.sql;
@./Indexes/IDX_ASSIGMENTS1.sql;
@./Indexes/PK_ASSIGNMENTS.sql;
@./Procedures/GETPROJECTINFO.prc;
@./Procedures/TMS_COPIAR_PROJECTO_PR.prc;
@./Procedures/GETPROJECTINFO.prc;
@./Procedures/GETREALTIMEHOUR.prc;
@./Procedures/GETREALTIMEMINUTES.prc;
@./Procedures/ISCONFLICT.prc;
@./Constraints/ACCOUNTS_NonFK.sql;
@./Constraints/ASSIGNMENTS_FK.sql;
@./Constraints/ASSIGNMENTS_NonFK.sql;
@./Constraints/CALENDAR_FK.sql;
@./Constraints/CALENDAR_NonFK.sql;
@./Constraints/CONFIG_SEND_REPORT_NonFK.sql;
@./Constraints/COSTS_NonFK.sql;
@./Constraints/COSTS_FK.sql;
@./Constraints/ORGANIZATIONS_NonFK.sql;
@./Constraints/COUNTRIES_NonFK.sql;
@./Constraints/COUNTRIES_FK.sql;
@./Constraints/FILES_NonFK.sql;
@./Constraints/FILES_FK.sql;
@./Constraints/LOGS_NonFK.sql;
@./Constraints/LOGS_FK.sql;
@./Constraints/MASTER_REPORTS_FK.sql;
@./Constraints/MASTER_REPORTS_NonFK.sql;
@./Constraints/MEMBERS_NonFK.sql;
@./Constraints/MEMBERS_FK.sql;
@./Constraints/POST_NonFK.sql;
@./Constraints/POST_FK.sql;
@./Constraints/PREDEFINED_MESSAGES_NonFK.sql;
@./Constraints/PROJECTS_NonFK.sql;
@./Constraints/PROJECTS_FK.sql;
@./Constraints/REPORTS_NonFK.sql;
@./Constraints/REPORTS_FK.sql;
@./Constraints/RISKS_NonFK.sql;
@./Constraints/RISKS_FK.sql;
@./Constraints/SCHEDULES_NonFK.sql;
@./Constraints/SCHEDULES_FK.sql;
@./Constraints/TASKS_NonFK.sql;
@./Constraints/TASKS_FK.sql;
@./Constraints/TEAM_LOCK_NonFK.sql;
@./Constraints/TEAMS_NonFK.sql;
@./Constraints/TOPICS_NonFK.sql;
@./Constraints/TOPICS_FK.sql;
@./Constraints/TYPE_TASKS_NonFK.sql;
