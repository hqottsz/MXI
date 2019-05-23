--liquibase formatted sql


--changeSet 0ref_inv_oper:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INV_OPER"
** 0-Level
** DATE: 29-apr-05 TIME: 16:56:27
*********************************************/
insert into ref_inv_oper(inv_oper_db_id, inv_oper_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, oper_ord, avail_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'AWR', 0, 1, 'Awaiting Release', 'Awaiting Release', -1, 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_oper:2 stripComments:false
insert into ref_inv_oper(inv_oper_db_id, inv_oper_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, oper_ord, avail_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'AOG', 0, 31, 'Aircraft on Ground', 'The aircraft is unavailable due to repairs.', 80, 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_oper:3 stripComments:false
insert into ref_inv_oper(inv_oper_db_id, inv_oper_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, oper_ord, avail_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'INM', 0, 131, 'In Maintenance', 'The aircraft is unavailable due to scheduled maintenance', -1, 0,  0, '23-MAR-01', '30-MAR-06', 100, 'MXI');

--changeSet 0ref_inv_oper:4 stripComments:false
insert into ref_inv_oper(inv_oper_db_id, inv_oper_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, oper_ord, avail_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'NORM', 0, 1, 'Normal', 'Normal operational capability', 20, 1,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_oper:5 stripComments:false
insert into ref_inv_oper(inv_oper_db_id, inv_oper_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, oper_ord, avail_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'OPEN', 0, 155, 'Open Fault', 'This fault has an OPEN status.', 26, 1,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');