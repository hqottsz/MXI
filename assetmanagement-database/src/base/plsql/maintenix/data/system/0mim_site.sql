--liquibase formatted sql


--changeSet 0mim_site:1 stripComments:false
/****************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE "MIM_SITE"
****************************************************/
INSERT into MIM_SITE (SITE_CD, RSTAT_CD, SITE_NAME, COMPANY_SDESC, BUS_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 0, 'MxI Technologies', 'MxI Technologies', NULL, TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE ('11/01/1997 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 'MxI');