--liquibase formatted sql


--changeSet 0ref_fail_sev:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_SEV"
** 0-Level
** DATE: 02/05/03 TIME: 16:56:27
*********************************************/
insert into ref_fail_sev(fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, fail_sev_ord, sev_type_db_id, sev_type_cd,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UNKNOWN', 0, 148,  'Unknown', 'Unknown', 20,  0, 'UNKNOWN',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fail_sev:2 stripComments:false
insert into ref_fail_sev(fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, fail_sev_ord, sev_type_db_id, sev_type_cd,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'AOG', 0, 151,  'Aircraft is on Ground', 'The fault is severe enough to ground the aircraft', 90,  0, 'AOG',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fail_sev:3 stripComments:false
insert into ref_fail_sev(fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, fail_sev_ord, sev_type_db_id, sev_type_cd,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MINOR', 0, 150,  'Minor', 'The fault has no operational impact', 25,  0, 'MINOR',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fail_sev:4 stripComments:false
insert into REF_FAIL_SEV (FAIL_SEV_DB_ID, FAIL_SEV_CD, BITMAP_DB_ID, BITMAP_TAG, SEV_TYPE_DB_ID, SEV_TYPE_CD, DESC_SDESC, DESC_LDESC, FAIL_SEV_ORD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 0, 'BLKOUT', 'N/A', 'N/A', 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_fail_sev:5 stripComments:false
-- Electronic Logbook
insert into ref_fail_sev (fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag, sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, fail_sev_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'CDL-WND', 0, 150, 0, 'MINOR', 'CDL - With No Deadline', 'Configuration Deviation List, with no deadline.', 22, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev:6 stripComments:false
insert into ref_fail_sev (fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag, sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, fail_sev_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'MINR-WD', 0, 150, 0, 'MEL', 'MINOR - With Deadline', 'The fault has no operational impact, with a deadline.', 20, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev:7 stripComments:false
insert into ref_fail_sev (fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag, sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, fail_sev_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'MINR-WND', 0, 150, 0, 'MINOR', 'MINOR - With No Deadline', 'The fault has no operational impact, with no deadline', 20, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev:8 stripComments:false
insert into ref_fail_sev (fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag, sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, fail_sev_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'CDL', 0, 150, 0, 'MEL', 'Configuration Deviation List', 'Configuration Deviation List', 22, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev:9 stripComments:false
insert into ref_fail_sev (fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag, sev_type_db_id, sev_type_cd, desc_sdesc, desc_ldesc, fail_sev_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'MEL', 0, 149, 0, 'MEL', 'MEL failure', 'The failure is MEL related.', 30, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');