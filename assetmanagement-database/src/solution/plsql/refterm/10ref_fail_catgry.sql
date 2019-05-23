/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_CATGRY"
** 10-Level
** DATE: 09/30/1998 TIME: 16:56:27
*********************************************/
insert into ref_fail_catgry(fail_catgry_db_id, fail_catgry_cd, bitmap_db_id, bitmap_tag, desc_sdesc,  desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'E-S', 0, 1,  'Evident-safety', 'A failure of this type will be evident to the crew and affect safety', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_fail_catgry(fail_catgry_db_id, fail_catgry_cd, bitmap_db_id, bitmap_tag, desc_sdesc,  desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'E-EO', 0, 1,  'Evident-economic-operational', 'A faiure of this type will be evident to the crew and will affect operations', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_fail_catgry(fail_catgry_db_id, fail_catgry_cd, bitmap_db_id, bitmap_tag, desc_sdesc,  desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'E-ENO', 0, 1,  'Evident-economic-non-operational', 'A failure of this type will be evident to the crew have economic consequence but no operational consequence', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_fail_catgry(fail_catgry_db_id, fail_catgry_cd, bitmap_db_id, bitmap_tag, desc_sdesc,  desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'H-S', 0, 1,  'Hidden-safety', 'A failure of this type will not be evident to the crew but affect safety.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_fail_catgry(fail_catgry_db_id, fail_catgry_cd, bitmap_db_id, bitmap_tag, desc_sdesc,  desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'H-E', 0, 1,  'Hidden-economic', 'A failure of this type will not be evident to the crew but will not affect safety', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
