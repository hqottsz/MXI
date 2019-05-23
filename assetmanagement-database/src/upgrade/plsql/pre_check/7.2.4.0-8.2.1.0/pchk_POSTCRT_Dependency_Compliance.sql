WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_POSTCRT_Dependency_Compliance
DEFINE pchk_table_name=pchk_pcrt_dep_compliance
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

BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/

CREATE TABLE pchk_pcrt_dep_compliance
AS
SELECT
   task_task.task_name,
   task_task.task_cd AS o_task_name,
   CAST(NULL AS VARCHAR2(4000)) AS error_message
FROM
   task_task
WHERE
   rownum = 0
;

--------------------------------------------------------------------------------------------
-- Pre-validation script for OPER-3372
--------------------------------------------------------------------------------------------
--
-- Purpose: Warn the user about invalid task dependencies.
--   A task is invalid if they do not belong to the same config slot or the config slots do
--   not share the same usage measurements. System configuration slots share the same class
--   type as Root (ASSY/ACFT) configuration slots.
--------------------------------------------------------------------------------------------

DECLARE
   CURSOR lcur_GetInvalidTaskDependency IS
      SELECT DISTINCT
         task_task.task_name o_task_name,
         dep_task_task.task_name
      FROM
         task_task
         INNER JOIN eqp_assmbl_bom config_slot ON
            config_slot.assmbl_db_id = task_task.assmbl_db_id AND
            config_slot.assmbl_cd = task_task.assmbl_cd AND
            config_slot.assmbl_bom_id = task_task.assmbl_bom_id
         INNER JOIN task_task_dep ON
            task_task_dep.task_db_id = task_task.task_db_id AND
            task_task_dep.task_id = task_task.task_id
         INNER JOIN task_task dep_task_task ON
            dep_task_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
            dep_task_task.task_defn_id = task_task_dep.dep_task_defn_id
         INNER JOIN eqp_assmbl_bom dep_config_slot ON
            dep_config_slot.assmbl_db_id = dep_task_task.assmbl_db_id AND
            dep_config_slot.assmbl_cd = dep_task_task.assmbl_cd AND
            dep_config_slot.assmbl_bom_id = dep_task_task.assmbl_bom_id
      WHERE
         task_task_dep.task_dep_action_db_id = 0 AND
         task_task_dep.task_dep_action_cd = 'POSTCRT'
         AND
         NOT(
            task_task.assmbl_db_id = dep_task_task.assmbl_db_id AND
            task_task.assmbl_cd = dep_task_task.assmbl_cd AND
            task_task.assmbl_bom_id = dep_task_task.assmbl_bom_id
         )
         AND
         NOT(
            config_slot.bom_class_db_id = 0 AND
            config_slot.bom_class_cd IN ('ROOT', 'SYS')
            AND
            dep_config_slot.bom_class_db_id = 0 AND
            dep_config_slot.bom_class_cd IN ('ROOT', 'SYS')
         )
      ;
      
   lrec lcur_GetInvalidTaskDependency%ROWTYPE;
   
   -- output variables
   ln_error_message  VARCHAR2(4000);
   
BEGIN
   FOR lrec IN lcur_GetInvalidTaskDependency 
   LOOP
      ln_error_message := lrec.task_name || ' cannot depend on ' || lrec.o_task_name;
      
      INSERT INTO
         pchk_pcrt_dep_compliance
      VALUES
         (
            lrec.o_task_name,
            lrec.task_name,
            ln_error_message
         )
      ;
   END LOOP;
END;
/

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

SELECT
   '&pchk_severity' AS "SEV",
   error_message
FROM
   pchk_pcrt_dep_compliance
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
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
SET ECHO OFF
PROMPT ***
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
UNDEFINE pchk_file_name
UNDEFINE pchk_table_name
UNDEFINE pchk_severity
