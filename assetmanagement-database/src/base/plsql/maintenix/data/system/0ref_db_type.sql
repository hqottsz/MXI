--liquibase formatted sql


--changeSet 0ref_db_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_DB_TYPE"
** DATE: 07/19/2005 TIME: 11:53:54
*********************************************/
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('SYS', 0, 'System','A database identifier used to represent internal, Maintenix data',TO_DATE ('05/13/1998 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('05/13/1998 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0ref_db_type:2 stripComments:false
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('CUST', 0, 'Customer','A database identifier used to represent a customer database',TO_DATE ('05/13/1998 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('05/13/1998 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0ref_db_type:3 stripComments:false
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('MASTER', 0, 'Master','A database identifier used to represent a Master database.',TO_DATE ('07/19/2005 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('07/19/2005 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0ref_db_type:4 stripComments:false
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('REPLICA', 0, 'Replica','A database identifier used to represent a replicated database',TO_DATE ('07/19/2005 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('07/19/2005 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0ref_db_type:5 stripComments:false
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('STANDARD', 0, 'Standard','A database identifier used to represent a Standard non-master or replicateed database',TO_DATE ('07/19/2005 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('07/19/2005 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0ref_db_type:6 stripComments:false
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('OPER', 0, 'Operational','A database identifier used to represent an operational database.',TO_DATE ('01/28/2009 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('01/28/2009 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MXI');

--changeSet 0ref_db_type:7 stripComments:false
INSERT into REF_DB_TYPE (DB_TYPE_CD, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('CONSOL', 0, 'Consolidated','A database identifier used to represent a consolidated database.',TO_DATE ('01/28/2009 11:52:14', 'MM/DD/YYYY HH24:MI:SS'),TO_DATE ('01/28/2009 11:52:14', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MXI');