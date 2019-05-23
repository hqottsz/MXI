WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_find_must_remove_tasks_from_acft_wp
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

--------------------------------------------------------------------------------------------
-- Purpose: Warning user about existing OFF_WING or OFF_PARENT tasks assigned to ON-AIRCRAFT 
-- work packages.
--------------------------------------------------------------------------------------------

SELECT
   '&pchk_severity' AS "SEV",
   sched_stask.sched_db_id || ':' || sched_stask.sched_id AS task_key,
   sched_stask.barcode_sdesc AS task_barcode,
   task_task.task_must_remove_cd,
   parent.sched_db_id || ':' || parent.sched_id AS work_package_key,
   parent.barcode_sdesc AS work_package_barcode,
   inv_inv.inv_class_cd AS work_package_inv_class
FROM
   sched_stask
   INNER JOIN task_task ON 
      task_task.task_db_id = sched_stask.task_db_id AND
      task_task.task_id = sched_stask.task_id
   INNER JOIN evt_event ON
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id = sched_stask.sched_id
   INNER JOIN sched_stask parent ON
      parent.sched_db_id = evt_event.h_event_db_id AND
      parent.sched_id = evt_event.h_event_id
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = parent.main_inv_no_db_id AND
      inv_inv.inv_no_id = parent.main_inv_no_id
WHERE
   task_task.task_must_remove_db_id = 0 AND
   (task_task.task_must_remove_cd = 'OFFWING' OR task_task.task_must_remove_cd = 'OFFPARENT')
   AND
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd = 'ACFT';

SET TERMOUT ON
SET MARKUP HTML OFF
SPOOL OFF

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