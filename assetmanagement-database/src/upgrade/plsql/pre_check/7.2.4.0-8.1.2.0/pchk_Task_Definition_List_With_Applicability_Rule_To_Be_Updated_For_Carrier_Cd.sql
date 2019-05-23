WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Task_Definition_List_With_Applicability_Rule_To_Be_Updated_For_Carrier_Cd
DEFINE pchk_severity=INFO
 
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

-- A task applicability rule get translated to raw sql expression and stored in the task_task.task_appl_sql_ldesc.
-- Specifically for applicability rules regarding operators (Aircraft Operator and Assembly Operator) they 
-- are based on the column org_carrier.carrier_cd  
-- Now the org_carrier.carrier_cd  has changed to be in sync with the corresponding org_org.org_cd. 
-- Example:
--    original = org_carrier.carrier_cd = 'AA-BBB'
--    new = org_carrier.carrier_cd = 'CCC', assuming that org_org.org_cd = 'CCC'
--
-- Below is a list of task definitions that might be updated by the MTX-973_1.sql migration script.
-- This list intends to be informative only.  
-- The following task definitions have applicability rules that involved operators (Aircraft Operator or Assembly Operator)
--
-- WARNING: The following list of task definitions has applicability rules based on operator and aircraft assembly
-- now the task_task.task_appl_sql_ldesc column which contains carrier_cd will be updated to have the carrier code value
-- in sync with the corresponding org_org_org_cd value.

SELECT DISTINCT 
  '&pchk_severity' AS "SEV",
  task_task.task_db_id, 
  task_task.task_id, 
  task_task.task_cd, 
  task_task.task_name, 
  task_task.task_appl_sql_ldesc
FROM 
  task_task
INNER JOIN org_org ON
  task_task.org_db_id = org_org.nh_org_db_id
  AND
  task_task.org_id = org_org.nh_org_id
INNER JOIN org_carrier ON
  org_org.org_db_id = org_carrier.org_db_id
  AND
  org_org.org_id = org_carrier.org_id
WHERE
  org_org.org_type_db_id = 0
  AND
  org_org.org_type_cd = 'OPERATOR'
  AND
  task_task.task_appl_sql_ldesc LIKE '%carrier_cd%'
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
