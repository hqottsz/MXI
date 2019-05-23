--liquibase formatted sql


--changeSet 0ref_cfg_slot_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_CFG_SLOT_STATUS"
** 0-Level
** DATE: 10-OCT-2007 TIME: 00:00:00
*********************************************/
insert into ref_cfg_slot_status ( CFG_SLOT_STATUS_DB_ID, CFG_SLOT_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'ACTV', 'ACTIVE', 'The config slot is listed as active', 0, '10-OCT-07', '10-OCT-07', 0, 'MXI');

--changeSet 0ref_cfg_slot_status:2 stripComments:false
insert into ref_cfg_slot_status ( CFG_SLOT_STATUS_DB_ID, CFG_SLOT_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'OBSOLETE', 'OBSOLETE', 'The config slot is listed as obsolete', 0, '10-OCT-07', '10-OCT-07', 0, 'MXI');