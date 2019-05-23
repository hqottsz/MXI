--liquibase formatted sql


--changeSet DEV-210:1 stripComments:false
INSERT INTO
   ref_task_class
   (
      task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'OPEN', 0, 77,  'Open Panel Job Instruction Card', 'An open panel job card is used to specify instructions for opening a panel. Open panel job cards cannot be added to requirements.', 0, 1, 0, 'JIC', 0, TO_DATE('2010-01-26', 'YYYY-MM-DD'), TO_DATE('2010-01-26', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_task_class WHERE task_class_db_id = 0 and  task_class_cd = 'OPEN');    

--changeSet DEV-210:2 stripComments:false
INSERT INTO
   ref_task_class
   (
      task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'CLOSE', 0, 77,  'Close PanelJob Instruction Card', 'An close panel job card is used to specify instructions for closing a panel. Close panel job cards cannot be added to requirements.', 0, 1, 0, 'JIC', 0, TO_DATE('2010-01-26', 'YYYY-MM-DD'), TO_DATE('2010-01-26', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_task_class WHERE task_class_db_id = 0 and  task_class_cd = 'CLOSE');        