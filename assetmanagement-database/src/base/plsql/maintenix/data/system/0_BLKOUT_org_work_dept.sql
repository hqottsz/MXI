--liquibase formatted sql


--changeSet 0_BLKOUT_org_work_dept:1 stripComments:false
/************************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE ORG_WORK_DEPT FOR BLACKOUT DATA ONLY**
*************************************************************************/
insert into ORG_WORK_DEPT (DEPT_DB_ID, DEPT_ID, DEPT_CD, DEPT_TYPE_DB_ID, DEPT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1000, 'N/A', 0, 'SUPPLY', 0, 1, 'N/A', 'N/A', 1, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');