--liquibase formatted sql


--changeSet 0ref_lrp_config_sev:1 stripComments:false
/***********************************************
** INSERT SCRIPT FOR TABLE "REF_LRP_CONFIG_SEV"
** DATE: 05/29/2008 TIME: 00:00:00
************************************************/
insert into REF_LRP_CONFIG_SEV(LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, PRIORITY_ORD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'IGNORE', 4, 'Ignore', 'Events that can be ignored', 'FFFFFF',0, '16-May-08', '16-May-08', 100, 'MXI');

--changeSet 0ref_lrp_config_sev:2 stripComments:false
insert into REF_LRP_CONFIG_SEV(LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, PRIORITY_ORD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'NONCRITC', 3, 'Non-critical', 'Events that are non-critical', 'FFFF00',0, '16-May-08', '16-May-08', 100, 'MXI');

--changeSet 0ref_lrp_config_sev:3 stripComments:false
insert into REF_LRP_CONFIG_SEV(LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, PRIORITY_ORD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'CRITICAL', 2, 'Critical', 'Events that are critical', 'FF0000',0, '16-May-08', '16-May-08', 100, 'MXI');

--changeSet 0ref_lrp_config_sev:4 stripComments:false
insert into REF_LRP_CONFIG_SEV(LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, PRIORITY_ORD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'HIGHLITE', 1, 'Highlight', 'Events that are highlighted', '5F9EA0',0, '10-DEC-09', '10-DEC-09', 100, 'MXI');