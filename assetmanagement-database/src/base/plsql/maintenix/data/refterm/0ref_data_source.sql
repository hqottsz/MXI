--liquibase formatted sql


--changeSet 0ref_data_source:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_DATA_SOURCE"
** 0-Level
** DATE: 09/30/1998 TIME: 16:09:03
*********************************************/
insert into ref_data_source(data_source_db_id, data_source_cd, data_source_type_db_id, data_source_type_cd, bitmap_db_id, bitmap_tag,  desc_sdesc,  desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MXFL', 0, 'FLT', 0, 26, 'Maintenix Flight Log', 'Data collection performed through the Maintenix Flight Log module',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');