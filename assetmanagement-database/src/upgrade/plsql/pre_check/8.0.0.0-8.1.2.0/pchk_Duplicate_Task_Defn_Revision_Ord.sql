WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_Duplicate_Task_Defn_Revision_Ord
DEFINE pchk_severity=ERROR

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

--
-- MX-28907 Pre-Check
--
-- This pre-check displays all rows in the task_task table that prevent the 
-- application of the uk_taskdefn_revisionord unique constraint.  
-- Manual intrevention is required in order to determine which of the duplicate rows 
-- need to be deleted.
--
-- The unique constraint is on the columns: task_defn_db_id, task_defn_id, revision_ord
--

SELECT
   '&pchk_severity' AS sev,
   dups.dup_group,
   dups.num_of_dups,
   task_task.task_db_id,
   task_task.task_id,
   task_task.task_cd,
   task_task.task_name,
   task_task.task_class_cd,
   task_task.task_subclass_cd,
   task_task.task_def_status_cd,
   task_task.task_originator_cd,
   task_task.assmbl_cd,
   eqp_assmbl_bom.assmbl_bom_cd
FROM
   task_task
   INNER JOIN
   (
      SELECT 
         task_defn_db_id,
         task_defn_id,
         revision_ord,
         COUNT(*) AS num_of_dups,
         ROW_NUMBER() OVER ( ORDER BY
                                task_defn_db_id,
                                task_defn_id,
                                revision_ord
         ) AS dup_group
      FROM
         task_task
      GROUP BY
         task_defn_db_id,
         task_defn_id,
         revision_ord
      HAVING
         COUNT(*) > 1
   ) dups ON
      dups.task_defn_db_id = task_task.task_defn_db_id AND 
      dups.task_defn_id    = task_task.task_defn_id AND 
      dups.revision_ord    = task_task.revision_ord
   INNER JOIN eqp_assmbl_bom ON
      eqp_assmbl_bom.assmbl_db_id  = task_task.assmbl_db_id AND
      eqp_assmbl_bom.assmbl_cd     = task_task.assmbl_cd AND 
      eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id
ORDER BY
  dups.dup_group,
  task_task.task_db_id,
  task_task.task_id
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
