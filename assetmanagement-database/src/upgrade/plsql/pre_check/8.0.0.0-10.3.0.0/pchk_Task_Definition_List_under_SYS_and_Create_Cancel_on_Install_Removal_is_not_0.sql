WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_Task_Definition_List_under_SYS_and_Create_Cancel_on_Install_Removal_is_not_0
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
-- Pre-validation script for upgrade to 8.2-SP5 in OPER-9639
--------------------------------------------------------------------------------------------
-- Purpose: Warning user about existing data records in task_task table which are under SYS
-- config slot and have value 1 for Create/Cancel on Install/Removal will be updated.
--
-- These records are candidates whose value of create_on_ac_inst_bool,
-- create_on_any_inst_bool, create_on_ac_rmvl_bool, create_on_any_rmvl_bool,
-- cancel_on_ac_inst_bool, cancel_on_any_inst_bool, cancel_on_ac_rmvl_bool and
-- cancel_on_any_rmvl_bool will be updated to be 0.
--------------------------------------------------------------------------------------------

SELECT
   '&pchk_severity' AS "SEV",
   task_task.task_db_id,
   task_task.task_id,
   task_task.task_cd,
   task_task.task_name,
   eqp_assmbl_bom.assmbl_bom_cd,
   eqp_assmbl_bom.assmbl_cd,
   eqp_assmbl_bom.assmbl_bom_name,
   task_task.create_on_ac_inst_bool,
   task_task.create_on_any_inst_bool,
   task_task.create_on_ac_rmvl_bool,
   task_task.create_on_any_rmvl_bool,
   task_task.cancel_on_ac_inst_bool,
   task_task.cancel_on_any_inst_bool,
   task_task.cancel_on_ac_rmvl_bool,
   task_task.cancel_on_any_rmvl_bool
FROM
   task_task
INNER JOIN eqp_assmbl_bom ON
   task_task.assmbl_db_id      = eqp_assmbl_bom.assmbl_db_id AND
   task_task.assmbl_cd         = eqp_assmbl_bom.assmbl_cd AND
   task_task.assmbl_bom_id     = eqp_assmbl_bom.assmbl_bom_id AND
   eqp_assmbl_bom.bom_class_db_id = 0 AND
   eqp_assmbl_bom.bom_class_cd = 'SYS' AND
   (
      task_task.create_on_ac_inst_bool  = 1 OR
      task_task.create_on_any_inst_bool = 1 OR
      task_task.create_on_ac_rmvl_bool  = 1 OR
      task_task.create_on_any_rmvl_bool = 1 OR
      task_task.cancel_on_ac_inst_bool  = 1 OR
      task_task.cancel_on_any_inst_bool = 1 OR
      task_task.cancel_on_ac_rmvl_bool  = 1 OR
      task_task.cancel_on_any_rmvl_bool = 1
   );

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