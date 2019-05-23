--liquibase formatted sql


--changeSet 0ref_part_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PART_STATUS"
** 0-Level
** DATE: 29-JUL-03
*********************************************/
insert into ref_part_status(part_status_db_id, part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'BUILD', 0, 65, 'Unapproved Part', 'The part has been created but not approved', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_part_status:2 stripComments:false
insert into ref_part_status(part_status_db_id, part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ACTV', 0, 66, 'Approved Part', 'The part has been approved and can be used for installations', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_part_status:3 stripComments:false
insert into ref_part_status(part_status_db_id, part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'OBSLT', 0, 115, 'Reject Part', 'The part is rejected and cannot be used for installations', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_part_status:4 stripComments:false
insert into REF_PART_STATUS (PART_STATUS_DB_ID, PART_STATUS_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 'N/A', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');