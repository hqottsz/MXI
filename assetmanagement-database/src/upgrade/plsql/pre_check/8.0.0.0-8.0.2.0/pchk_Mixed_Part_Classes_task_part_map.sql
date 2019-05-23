WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_Mixed_Part_Classes_task_part_map
DEFINE pchk_severity=WARNING

SET VERIFY OFF 
SET SQLPROMPT "_date _user> "
SET TIME ON

SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON
SET FEEDBACK ON
SET HEADING ON
SET PAGESIZE 50000
SET LINESIZE 32767
SET TRIMSPOOL ON
SET SQLBLANKLINES ON
SET DEFINE ON
SET CONCAT OFF
SET MARKUP HTML OFF

SET ECHO OFF
PROMPT ***
PROMPT *** Opening file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to &pchk_file_name.html
PROMPT ***
SET ECHO ON
SPOOL OFF

SET ECHO OFF
SET TERMOUT OFF
SET MARKUP -
   HTML ON -
   HEAD " -
   <STYLE type='text/css'> -
      BODY {font-family:verdana;font-size:11px; font-weight:bold;} -
      TABLE {border-collapse:collapse; font-size:11px; width:100normal;} -
      TH {background-color:#4682B4; color:#fff; height:30px; font-weight:bold;} -
   </STYLE> " -
   BODY "" -
   TABLE "border=1 bordercolor=black" -
   SPOOL ON -
   ENTMAP ON -
   PREFORMAT OFF 

SPOOL &pchk_file_name.html

----------------------------------------------------------------------------------------------
-- Pre-Check script as per MX-28028  
----------------------------------------------------------------------------------------------
-- This script lists all the tasks definition that has mixed assigned part
-- BATCH and NON-BATCH (SER, TRK, KIT).

-- **IMPORTANT**
-- 1) Any BATCH part has to be un-assigned prior to edit/update the task definition to avoid
--    scheduling data corruption.
-- 2) Should you revisit these tasks definition to ensure that any scheduling data has
--    not been corrupted.
----------------------------------------------------------------------------------------------

SELECT
   '&pchk_severity' AS sev,
   task_task.task_cd,
   task_task.task_db_id,
   task_task.task_id
FROM 
   task_task
   INNER JOIN  
   -- select task definition domain by category
   (  SELECT 
         task_part_map.task_db_id,
         task_part_map.task_id,
         inv_class_category.class_cd
      FROM 
         task_part_map
         INNER JOIN eqp_part_no ON
            task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
            task_part_map.part_no_id    = eqp_part_no.part_no_id
         INNER JOIN
         -- select tasks definition that has more than one assigned part 
         (  SELECT 
               task_task.task_cd,
               task_task.task_db_id,
               task_task.task_id
            FROM
               task_task
               INNER JOIN task_part_map ON
                  task_task.task_db_id = task_part_map.task_db_id AND
                  task_task.task_id    = task_part_map.task_id
               INNER JOIN eqp_part_no ON
                  task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
                  task_part_map.part_no_id    = eqp_part_no.part_no_id
            GROUP BY 
               task_task.task_db_id,
               task_task.task_id,
               task_task.task_cd
            HAVING
               COUNT(*) > 1
         ) task_def_list_domain ON
            task_part_map.task_db_id = task_def_list_domain.task_db_id AND
            task_part_map.task_id = task_def_list_domain.task_id
         INNER JOIN
         --group inv class cd under BATCH and NON-BATCH categories
         (  SELECT 
               ref_inv_class.inv_class_cd,
               CASE
                  WHEN 
                     ref_inv_class.inv_class_cd IN ('SER', 'TRK', 'KIT')
                  THEN
                     'NON-BATCH'
                  ELSE
                    'BATCH'
               END  AS class_cd
            FROM
               ref_inv_class
            WHERE
               ref_inv_class.inv_class_cd IN ('SER', 'TRK', 'KIT', 'BATCH')
         ) inv_class_category ON
            eqp_part_no.inv_class_cd = inv_class_category.inv_class_cd
      GROUP BY 
         task_part_map.task_db_id,
         task_part_map.task_id,
         inv_class_category.class_cd
   ) task_def_domain_by_category ON
      task_task.task_db_id = task_def_domain_by_category.task_db_id AND
      task_task.task_id    = task_def_domain_by_category.task_id
GROUP BY 
   task_task.task_db_id,
   task_task.task_id,
   task_task.task_cd
HAVING
   COUNT(*) > 1 
;
 
SPOOL OFF
SET MARKUP HTML OFF
SET TERMOUT ON

SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON

SET ECHO OFF
PROMPT ***
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON

SPOOL OFF

UNDEFINE pchk_file_name
UNDEFINE pchk_severity
