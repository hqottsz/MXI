--liquibase formatted sql


--changeSet 0_BLKOUT_org_authority:1 stripComments:false
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE ORG_AUTHORITY FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into ORG_AUTHORITY (AUTHORITY_DB_ID, AUTHORITY_ID, AUTHORITY_CD, AUTHORITY_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1001, 'N/A', 'N/A', 1, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');