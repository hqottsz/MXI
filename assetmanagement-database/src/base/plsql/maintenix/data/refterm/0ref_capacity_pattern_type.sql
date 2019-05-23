--liquibase formatted sql


--changeSet 0ref_capacity_pattern_type:1 stripComments:false
/*****************************************************
** INSERT SCRIPT FOR TABLE "REF_CAPACITY_PATTERN_TYPE"
** 0-Level
** DATE: 02-JAN-10 TIME: 00:00:00
******************************************************/
insert into ref_capacity_pattern_type (capacity_pattern_type_db_id, capacity_pattern_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'WEEKLY', '7 Day Pattern', 'This is a weekly pattern of shifts', 0, '02-JAN-10', '02-JAN-10', 0, 'MXI');

--changeSet 0ref_capacity_pattern_type:2 stripComments:false
insert into ref_capacity_pattern_type (capacity_pattern_type_db_id, capacity_pattern_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'DAILY', 'Daily Pattern', 'This is a one day pattern of shifts', 0, '02-JAN-10', '02-JAN-10', 0, 'MXI');