--liquibase formatted sql


--changeSet 0mim_db:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "MIM_DB"
** DATE: 30/05/2004 13:45:24
*********************************************/
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD, DB_NAME, DB_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, '0', 'SYS', 'MxI Maintenix Bus Logic', NULL,  0, TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');

--changeSet 0mim_db:2 stripComments:false
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD,DB_NAME, DB_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (10, '0', 'SYS','MxI Maintenix Common Baseline', NULL, 0,  TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');

--changeSet 0mim_db:3 stripComments:false
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD, DB_NAME, DB_LDESC,RSTAT_CD,  CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (20, '0', 'SYS', 'Commercial Standard', NULL, 0, TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');

--changeSet 0mim_db:4 stripComments:false
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD, DB_NAME, DB_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (30, '0', 'SYS', 'Defence Standard', NULL, 0, TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');

--changeSet 0mim_db:5 stripComments:false
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD, DB_NAME, DB_LDESC,RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (51, '0', 'SYS', 'MxI Custom Software Bus Logic','Values coded within software.  Do not change.', 0,  TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');

--changeSet 0mim_db:6 stripComments:false
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD, DB_NAME, DB_LDESC,RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (52, '0', 'SYS', 'MxI Custom Software Referential','Values are referential tied to other data in the baseline or an external system.  These should only be changed with help from an Mxi implementation specialist.', 0 , TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');

--changeSet 0mim_db:7 stripComments:false
INSERT into MIM_DB (DB_ID, SITE_CD, DB_TYPE_CD, DB_NAME, DB_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (53, '0', 'SYS', 'MxI Custom Software Suggested', 'Values are suggestions only and may be altered/deleted.',  0,  TO_DATE ('01-NOV-97 00:00:00', 'DD-MON-YY HH24:MI:SS'), TO_DATE ('14-OCT-98 09:49:58', 'DD-MON-YY HH24:MI:SS'), 0, 'MXI');