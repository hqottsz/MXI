--liquibase formatted sql


--changeSet 0ref_inv_class:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INV_CLASS"
** 0-Level
** DATE: 12/10/04 TIME: 00:00:00
*********************************************/
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, serial_bool, tracked_bool, traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ACFT', 0, 29, 'Aircraft', 'Aircraft', 1, 1, 0, 0, 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_inv_class:2 stripComments:false
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  serial_bool, tracked_bool,traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ASSY', 0, 3, 'Assembly', 'Assembly',  1, 1, 0, 0, 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_inv_class:3 stripComments:false
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, serial_bool, tracked_bool, traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TRK', 0, 2, 'Tracked Item', 'Tracked Item',  1, 1, 0, 1, 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_inv_class:4 stripComments:false
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, serial_bool, tracked_bool, traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SER', 0, 2, 'Serialized', 'Serialized', 1, 0, 0, 1, 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_inv_class:5 stripComments:false
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, serial_bool, tracked_bool, traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'BATCH', 0, 2, 'Batch', 'Batch', 0, 0, 1, 0, 0, '10-DEC-04', '10-DEC-04', 0, 'MXI');

--changeSet 0ref_inv_class:6 stripComments:false
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, serial_bool, tracked_bool, traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SYS', 0, 2, 'System', 'System', 0, 0, 0, 0, 0, '10-DEC-04', '10-DEC-04', 0, 'MXI');

--changeSet 0ref_inv_class:7 stripComments:false
insert into ref_inv_class (inv_class_db_id, inv_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, serial_bool, tracked_bool, traceable_bin_bool, receipt_insp_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'KIT', 0, 2, 'Kit', 'Maintenance Kit', 1, 0, 0, 1, 0, '11-DEC-07', '11-DEC-07', 0, 'MXI');

--changeSet 0ref_inv_class:8 stripComments:false
insert into REF_INV_CLASS (INV_CLASS_DB_ID, INV_CLASS_CD, BITMAP_DB_ID, BITMAP_TAG, TRACKED_BOOL, TRACEABLE_BIN_BOOL, SERIAL_BOOL, DESC_SDESC, DESC_LDESC, RECEIPT_INSP_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 1, 0, 1, 'N/A', 'N/A', 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');