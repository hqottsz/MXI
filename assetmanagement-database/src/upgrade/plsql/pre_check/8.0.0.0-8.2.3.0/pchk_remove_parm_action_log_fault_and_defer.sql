WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_remove_parm_action_log_fault_and_defer
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

-- roles WITH permission ACTION_LOG_FAULT_AND_DEFER, but WITHOUT  permission ACTION_DEFER_MEL_TASK or ACTION_DEFER_MINOR_TASK
SELECT
   '&pchk_severity'                    AS sev,
   utl_role.role_cd                    AS role_cd,
   utl_action_role_parm.parm_name      AS parm_name,
   COUNT(*)                            AS affected_users
FROM
   utl_action_role_parm
   INNER JOIN utl_role ON
      utl_role.role_id = utl_action_role_parm.role_id
   INNER JOIN utl_user_role ON
      utl_user_role.role_id = utl_action_role_parm.role_id
WHERE
   utl_action_role_parm.parm_name = 'ACTION_LOG_FAULT_AND_DEFER' AND
   UPPER(utl_action_role_parm.parm_value) = 'TRUE' AND
   utl_action_role_parm.role_id NOT IN
   (
      SELECT 
         role_id 
      FROM 
         utl_action_role_parm
      WHERE
         (parm_name = 'ACTION_DEFER_MEL_TASK' OR
         parm_name = 'ACTION_DEFER_MINOR_TASK') AND
         UPPER(parm_value) = 'TRUE' 
   ) 
GROUP BY 
   utl_action_role_parm.parm_name,
   utl_role.role_cd
ORDER BY
   utl_role.role_cd;

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
