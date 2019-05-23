/********************************************
** INSERT SCRIPT FOR TABLE "REF_LRP_PRIORITY"
** 10-Level
** DATE: 03-JUN-08 TIME: 00:00:00
*********************************************/
insert into ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd, priority_ord, default_bool, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values  (10, 'HIGH', 3, 0, 'High', 'High',  0, '03-JUN-2008', '03-JUN-2008', 100, 'MXI');

insert into ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd, priority_ord, default_bool, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values  (10, 'MED', 2, 1, 'Medium', 'Medium',  0, '03-JUN-2008', '03-JUN-2008', 100, 'MXI');

insert into ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd, priority_ord, default_bool, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values  (10, 'LOW', 1, 0, 'Low', 'Low',  0, '03-JUN-2008', '03-JUN-2008', 100, 'MXI');
