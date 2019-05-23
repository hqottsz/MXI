/********************************************
** INSERT SCRIPT FOR TABLE "REF_SD_TYPE"
** 10-Level
** DATE: 10/30/1997 TIME: 11:17:07
*********************************************/
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'HI', 0, 118, 'High', 'The failure is severe (ie. Flight incident)', 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'LOW', 0, 119, 'Low', 'The failure is not severe', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'MEL', 0, 1, 'MEL failure', 'The failure is MEL related', 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
