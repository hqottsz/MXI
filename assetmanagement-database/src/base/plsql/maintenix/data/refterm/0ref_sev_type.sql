--liquibase formatted sql


--changeSet 0ref_sev_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SEV_TYPE"
** 0-Level
** DATE: 06/28/05 TIME: 16:56:27
*********************************************/
insert into ref_sev_type(sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'UNKNOWN', 'Unknown', 'UNKNOWN', 0, '28-JUN-05', '28-JUN-05', 100, 'MXI');

--changeSet 0ref_sev_type:2 stripComments:false
insert into ref_sev_type(sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MEL', 'MEL Failure', 'The failure is MEL related', 0, '28-JUN-05', '28-JUN-05', 100, 'MXI');

--changeSet 0ref_sev_type:3 stripComments:false
insert into ref_sev_type(sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'AOG', 'Aircraft is on Ground', 'The fault is severe enough to ground the aircraft', 0, '28-JUN-05', '28-JUN-05', 100, 'MXI');

--changeSet 0ref_sev_type:4 stripComments:false
insert into ref_sev_type(sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MINOR', 'Minor', 'The fault has no operational impact', 0, '28-JUN-05', '28-JUN-05', 100, 'MXI');

--changeSet 0ref_sev_type:5 stripComments:false
insert into REF_SEV_TYPE (SEV_TYPE_DB_ID, SEV_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'N/A', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');