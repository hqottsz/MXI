--liquibase formatted sql


--changeSet 0ref_work_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WORK_TYPE"
** 0-Level
** DATE: 02/03/03 TIME: 11:17:07
*********************************************/
insert into ref_work_type(work_type_db_id, work_type_cd, work_type_ord, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TURN', 10, 'Turn Check', 'Turn Check',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_work_type:2 stripComments:false
insert into ref_work_type(work_type_db_id, work_type_cd, work_type_ord, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SERVICE', 10, 'Service Check', 'Service Check(Minimum Capability)',  0, '17-FEB-12', '17-FEB-12', 100, 'MXI');