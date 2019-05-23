--liquibase formatted sql


--changeSet 0inv_loc_org:1 stripComments:false
-- Assigning 0-level locations to the ADMIN organization.
/********************************************
** INSERT SCRIPT FOR TABLE "INV_LOC"
** DATE: 21/03/2005 TIME: 11:53:54
*********************************************/
insert into INV_LOC_ORG (LOC_DB_ID, LOC_ID, ORG_DB_ID, ORG_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 1000, 0, 1, 0,'20-DEC-01', '20-DEC-01', 0, 'MXI');

--changeSet 0inv_loc_org:2 stripComments:false
insert into INV_LOC_ORG (LOC_DB_ID, LOC_ID, ORG_DB_ID, ORG_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 1001, 0, 1, 0,'20-DEC-01', '20-DEC-01', 0, 'MXI');

--changeSet 0inv_loc_org:3 stripComments:false
insert into INV_LOC_ORG (LOC_DB_ID, LOC_ID, ORG_DB_ID, ORG_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 1002, 0, 1, 0,'20-DEC-01', '20-DEC-01', 0, 'MXI');

--changeSet 0inv_loc_org:4 stripComments:false
insert into INV_LOC_ORG (LOC_DB_ID, LOC_ID, ORG_DB_ID, ORG_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 1003, 0, 1, 0,'20-DEC-01', '20-DEC-01', 0, 'MXI');