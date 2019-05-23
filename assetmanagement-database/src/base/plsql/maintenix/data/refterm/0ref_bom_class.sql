--liquibase formatted sql


--changeSet 0ref_bom_class:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_BOM_CLASS"
** DATE: 12/07/2004 TIME: 00:00:00
*********************************************/
insert into ref_bom_class (bom_class_db_id, bom_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ROOT', 0, 3, 'Root Bom Item', 'Top item in the assembly tree.',  0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_bom_class:2 stripComments:false
insert into ref_bom_class (bom_class_db_id, bom_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SYS', 0, 4, 'System', 'System Placeholder; may be used to identify less significant items that you may not track',  0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_bom_class:3 stripComments:false
insert into ref_bom_class (bom_class_db_id, bom_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TRK', 0, 2, 'Tracked BOM', 'Tracked BOM',  0, '13-JUN-02', '13-JUN-02', 0, 'MXI');

--changeSet 0ref_bom_class:4 stripComments:false
insert into ref_bom_class (bom_class_db_id, bom_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SUBASSY', 0, 3, 'Sub-assembly', 'Connector for an assembly.',  0, '23-MAR-01', '23-MAR-01', 0, 'MXI');