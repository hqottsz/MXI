--liquibase formatted sql


--changeSet 0ref_assmbl_class:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_ASSMBL_CLASS"
** 0-Level
** DATE: 29-JUL-03 TIME: 00:00:00
*********************************************/
insert into ref_assmbl_class (assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ACFT', 0, 1, 'Aircraft', 'Aircraft assemblies may have Flights and Discrepancies',  0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_assmbl_class:2 stripComments:false
insert into ref_assmbl_class (assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'COMHW', 0, 19, 'Common Aircraft Hardware', 'This assembly is used to collect alternate part rules for common hardware.',  0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_assmbl_class:3 stripComments:false
insert into ref_assmbl_class (assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TSE', 0, 131, 'Tools and Service Equipment', 'This assembly represents all the tools and service equipment in the system.',  0, '17-JAN-06', '17-JAN-06', 0, 'MXI');

--changeSet 0ref_assmbl_class:4 stripComments:false
insert into ref_assmbl_class (assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values  (0, 'ENG', 0, 69, 'Engine', 'The assembly represents an engine.',  0, '23-MAR-01', '02-OCT-09', 0, 'MXI');

--changeSet 0ref_assmbl_class:5 stripComments:false
insert into ref_assmbl_class (assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values  (0, 'APU', 0, 130, 'Auxiliary Power Unit', 'The assembly represents an APU.',  0, '23-MAR-01', '02-OCT-09', 0, 'MXI');