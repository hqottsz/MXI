--liquibase formatted sql


--changeSet OPER-68:1 stripComments:false
-- OPER-86
INSERT 
INTO 
   ref_task_class 
     (
       task_class_db_id,
       task_class_cd,
       bitmap_db_id,
       bitmap_tag,
       desc_sdesc,
       desc_ldesc,
       auto_complete_bool,
       unique_bool,
       workscope_bool,
       class_mode_cd,
       rstat_cd,
       nr_est_bool,
       creation_dt
     )
SELECT
   0,
   'AMP',
   0,
   1,
   'Aircraft Maintenance Program',
   'Aircraft Maintenance Program',
   0,
   0,
   0,
   'REF',
   0,
   0,
   sysdate
FROM 
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   ref_task_class
                WHERE
                   task_class_cd = 'AMP'
              );