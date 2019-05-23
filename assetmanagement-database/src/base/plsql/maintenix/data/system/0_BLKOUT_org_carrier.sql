--liquibase formatted sql


--changeSet 0_BLKOUT_org_carrier:1 stripComments:false
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE ORG_CARRIER FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into ORG_CARRIER (CARRIER_DB_ID, CARRIER_ID, SUPPLY_CHAIN_DB_ID, SUPPLY_CHAIN_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1000, 0, 'DEFAULT', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');