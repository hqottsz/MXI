--liquibase formatted sql


--changeSet 0ref_fault_log_type:1 stripComments:false
-- Electronic Logbook
/********************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE "REF_FAULT_LOG_TYPE"
********************************************************/
insert into ref_fault_log_type (fault_log_type_db_id, fault_log_type_cd, name_sdesc, desc_ldesc, user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TECH', 'Technical Logbook', 'Electronic system providing functionality equivalent to an aircraft''s paper-based technical logbook, and integrated to ground based systems.  ', 'TECH', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fault_log_type:2 stripComments:false
insert into ref_fault_log_type (fault_log_type_db_id, fault_log_type_cd, name_sdesc, desc_ldesc, user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CABIN', 'Cabin Logbook', 'Electronic system providing functionality equivalent to an aircraft''s paper-based technical logbook, and integrated to ground based systems.  ', 'CABIN', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fault_log_type:3 stripComments:false
insert into ref_fault_log_type (fault_log_type_db_id, fault_log_type_cd, name_sdesc, desc_ldesc, user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IFE', 'IFE Logbook', 'Electronic Logbook that manages in-flight entertainment faults and issues.', 'IFE', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fault_log_type:4 stripComments:false
insert into ref_fault_log_type (fault_log_type_db_id, fault_log_type_cd, name_sdesc, desc_ldesc, user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TECHCAB', 'Technical and Cabin Logbook', 'Electronic system providing functionality equivalent to an aircraft''s paper-based technical logbook, and integrated to ground based systems.', 'TECHCAB', 0, TO_DATE('2018-10-03', 'YYYY-MM-DD'), TO_DATE('2018-10-03', 'YYYY-MM-DD'), 0, 'MXI');