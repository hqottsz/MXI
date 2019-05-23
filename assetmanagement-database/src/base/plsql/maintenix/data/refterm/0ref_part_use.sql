--liquibase formatted sql


--changeSet 0ref_part_use:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PART_USE"
** 0-Level
** DATE: 26-NOV-02 TIME: 16:09:03
*********************************************/
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'AE', 0, 16,  'Aeronautical Eqp.', 'Any components that are installed on an aircraft. These should all be in the IPC.',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_part_use:2 stripComments:false
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TOOLS', 0, 131,  'Tools', 'Equipment used to accomplish a maintenance task.  These can thus be assigned to Work Orders.',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');