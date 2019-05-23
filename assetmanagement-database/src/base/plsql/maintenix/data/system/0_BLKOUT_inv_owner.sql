--liquibase formatted sql


--changeSet 0_BLKOUT_inv_owner:1 stripComments:false
/******************************************
** 0-Level INSERT SCRIPT FOR INV_OWNER
*******************************************/
insert into INV_OWNER (OWNER_DB_ID, OWNER_ID, OWNER_CD, OWNER_NAME, LOCAL_BOOL, DEFAULT_BOOL, ORG_DB_ID, ORG_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1000, 'N/A', 'N/A', 1, 0, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0_BLKOUT_inv_owner:2 stripComments:false
insert into INV_OWNER (OWNER_DB_ID, OWNER_ID, OWNER_CD, OWNER_NAME, LOCAL_BOOL, DEFAULT_BOOL, ORG_DB_ID, ORG_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1001, 'N/A', 'N/A', 0, 0, null, null, 1, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');