--liquibase formatted sql


--changeSet QC-4992:1 stripComments:false
DELETE FROM ref_dept_type
WHERE dept_type_db_id = 0 
AND dept_type_cd = 'ENDSTRT';

--changeSet QC-4992:2 stripComments:false
DELETE FROM ref_dept_type
WHERE dept_type_db_id = 0 
AND dept_type_cd = 'STRTSTRT';