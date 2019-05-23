--liquibase formatted sql


--changeSet 0ref_data_source_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_DATA_SOURCE_TYPE"
** DATE: 06/29/01 TIME: 16:07:35
*********************************************/
insert into ref_data_source_type (data_source_type_db_id, data_source_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, manual_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MSU',  0, 5, 'Memory Storage Unit', 'Memory Storage Unit for automatic data retrieval from a flight computer (ie. external data source)', 0,   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_data_source_type:2 stripComments:false
insert into ref_data_source_type (data_source_type_db_id, data_source_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, manual_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'FLT',  0, 26, 'Flight', 'Flight Record Sheet for manually entered data associated with aircraft flight', 1,   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_data_source_type:3 stripComments:false
insert into ref_data_source_type (data_source_type_db_id, data_source_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, manual_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CHK',  0, 6, 'Equipment Check', 'Manually collected usage parameters obtained during a check or inspection', 1,   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');