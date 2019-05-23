--liquibase formatted sql


--changeSet QC-4474:1 stripComments:false
UPDATE ref_task_class
  SET desc_sdesc = 'Open Panel JIC'
WHERE task_class_db_id=0 AND task_class_cd= 'OPENPANEL';

--changeSet QC-4474:2 stripComments:false
UPDATE ref_task_class
  SET desc_sdesc = 'Close Panel JIC'
WHERE task_class_db_id=0 AND task_class_cd= 'CLOSEPANEL';