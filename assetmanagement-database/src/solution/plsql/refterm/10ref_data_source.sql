/********************************************
** INSERT SCRIPT FOR TABLE "REF_DATA_SOURCE"
** 10-Level
** DATE: 09/30/1998 TIME: 16:09:03
*********************************************/
insert into ref_data_source(data_source_db_id, data_source_cd, data_source_type_db_id, data_source_type_cd, bitmap_db_id, bitmap_tag,  desc_sdesc,  desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'BULK', 0, 'CHK', 0, 79, 'Bulk Usage Entry', 'Bulk usage entry creates single usage entry',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_data_source(data_source_db_id, data_source_cd, data_source_type_db_id, data_source_type_cd, bitmap_db_id, bitmap_tag,  desc_sdesc,  desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'EDIT', 0, 'CHK', 0, 79, 'Allows edition of data collection', 'Edit data source is a method of manually changing usage entries.',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
