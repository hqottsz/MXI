--liquibase formatted sql


--changeSet 0ref_labour_stage:1 stripComments:false
/****************************************************
** INSERT SCRIPT FOR TABLE "REF_LABOUR_STAGE"
** 0-Level
*****************************************************/
insert into ref_labour_stage(labour_stage_db_id, labour_stage_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ACTV', 0, 103, 'Active', 'Active', 0, '03-SEP-02', '03-SEP-02', 100, 'MXI');

--changeSet 0ref_labour_stage:2 stripComments:false
insert into ref_labour_stage(labour_stage_db_id, labour_stage_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'IN WORK', 0, 103, 'In Work', 'In Work', 0, '03-SEP-02', '03-SEP-02', 100, 'MXI');

--changeSet 0ref_labour_stage:3 stripComments:false
insert into ref_labour_stage(labour_stage_db_id, labour_stage_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'COMPLETE', 0, 103, 'Complete', 'Complete', 0, '03-SEP-02', '03-SEP-02', 100, 'MXI');

--changeSet 0ref_labour_stage:4 stripComments:false
insert into ref_labour_stage(labour_stage_db_id, labour_stage_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CANCEL', 0, 103, 'Cancel', 'Cancel', 0, '04-MAY-06', '04-MAY-06', 100, 'MXI');