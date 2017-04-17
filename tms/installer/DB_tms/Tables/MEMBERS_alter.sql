ALTER TABLE MEMBERS
 ADD (TIME_ZONE            VARCHAR2(45 BYTE),
      ID_ACCOUNT           NUMBER(10)               DEFAULT 2                     NOT NULL,
      EXPIRED_DATE         DATE,
      RENEW_DATE           DATE,
      SALE_COST            NUMBER(10,2),
      IND_EXEC_REPORT      CHAR(1 BYTE)             DEFAULT 'N')
	  

ALTER TABLE members MODIFY (cost DEFAULT 0)

/