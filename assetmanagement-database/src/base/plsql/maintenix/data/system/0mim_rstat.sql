--liquibase formatted sql


--changeSet 0mim_rstat:1 stripComments:false
/****************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE "MIM_RSTAT"
*****************************************************/
INSERT into MIM_RSTAT (RSTAT_CD, RSTAT_SDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'Active', TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0mim_rstat:2 stripComments:false
INSERT into MIM_RSTAT (RSTAT_CD, RSTAT_SDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (1, 'Carbon Copy', TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0mim_rstat:3 stripComments:false
INSERT into MIM_RSTAT (RSTAT_CD, RSTAT_SDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (2, 'Soft Delete', TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');

--changeSet 0mim_rstat:4 stripComments:false
INSERT into MIM_RSTAT (RSTAT_CD, RSTAT_SDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (3, 'Blacked Out', TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');