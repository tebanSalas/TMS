CREATE INDEX FK_MEMBERS_ORG ON MEMBERS
(ORGANIZATION, ID_ACCOUNT)
NOLOGGING
NOPARALLEL;

