--liquibase formatted sql


--changeSet OPER-1611:1 stripComments:false
--
-- Add new 0-level task classes for AD and SB (class mode = reference document).
--
INSERT INTO
   ref_task_class
   (
      task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool,  auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT
      0, 'AD', 0, 1, 'Airworthiness Directive', 'An airworthiness directive is a mandatory order issued by an air transportation regulatory body advising all concerned of an unsafe condition on an aircraft that jeopardizes the safety of the flying public and requires immediate attention.', 0, 0, 0, 'REF', 0, 0, SYSDATE, SYSDATE, 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_task_class WHERE task_class_db_id = 0 AND task_class_cd = 'AD' );

--changeSet OPER-1611:2 stripComments:false
INSERT INTO
   ref_task_class
   (
      task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool,  auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT
      0, 'SB', 0, 1,  'Service Bulletin', 'A service bulletin is a manufacturer''s advisory bulletin that broadcasts alerts, warnings, and recommendations to their customers.', 0, 0, 0, 'REF', 0, 0, SYSDATE, SYSDATE, 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_task_class WHERE task_class_db_id = 0 AND task_class_cd = 'SB' );