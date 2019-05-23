--liquibase formatted sql


--changeSet 0ref_fault_source:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAULT_SOURCE"
** 0-Level
** DATE: 03/04/03
*********************************************/
insert into ref_fault_source (fault_source_db_id, fault_source_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, spec2k_fault_source_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'PILOT', 0, 1,  'Pilot ', 'Pilot ', 'PL', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fault_source:2 stripComments:false
insert into ref_fault_source (fault_source_db_id, fault_source_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, spec2k_fault_source_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MECH', 0, 1,  'Mechanic', 'Mechanic', 'ML', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fault_source:3 stripComments:false
insert into ref_fault_source (fault_source_db_id, fault_source_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, spec2k_fault_source_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MESSAGE', 0, 1,  'Automated Message System', 'Automated Message System', 'PL', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fault_source:4 stripComments:false
-- ELA project
insert into ref_fault_source (fault_source_db_id, fault_source_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, spec2k_fault_source_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CABIN', 0, 1,  'Cabin Crew', 'Cabin Crew', 'CL', 0, '16-JUN-11', '16-JUN-11', 0, 'MXI');