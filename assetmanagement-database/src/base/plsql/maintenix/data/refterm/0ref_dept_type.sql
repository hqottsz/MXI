--liquibase formatted sql


--changeSet 0ref_dept_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_DEPT_TYPE"
** 0-Level
** DATE: 11-MAY-05 TIME: 16:56:27
*********************************************/
insert into ref_dept_type(dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ENG', 0, 53, 'Engineering', 'Engineering support to Maintenance', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_dept_type:2 stripComments:false
insert into ref_dept_type(dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MAINT', 0, 56, 'Maintenance', 'Maintenance', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_dept_type:3 stripComments:false
insert into ref_dept_type(dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SUPPLY', 0, 12, 'Supply', 'Provide suitable storage facilities for parts.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_dept_type:4 stripComments:false
insert into ref_dept_type(dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CREW', 0, 1, 'Crew', 'A Crew of Maintenance technicians.', 0, '15-JAN-02', '15-JAN-02', 100, 'MXI');