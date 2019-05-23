WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_remove_parm_hide_sched_labour_hours
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

SELECT
   '&pchk_severity' AS sev,
   scope,
   who_had_it,
   parm_value,
   affected_users_number
FROM
(
   SELECT
      'Config'         AS scope,
      'Global Default' AS who_had_it,
      CASE
         WHEN utl_config_parm.parm_name IS NULL
         THEN '[No record]'
         WHEN utl_config_parm.parm_value IS NULL
         THEN '[NULL]'
         ELSE utl_config_parm.parm_value
      END              AS parm_value,
      CASE
         WHEN utl_config_parm.parm_value IS NULL
         THEN 0
         WHEN UPPER(utl_config_parm.parm_value) = 'FALSE'
         THEN 0
         ELSE
            ( SELECT
                 COUNT(*)
              FROM
                 utl_user
            )
         END           AS affected_users_number
   FROM
      DUAL
      LEFT OUTER JOIN utl_config_parm ON
         utl_config_parm.parm_name = 'HIDE_SCHED_LABOUR_HOURS'
   UNION ALL
   SELECT
      'Role'                   AS scope,
      utl_role.role_cd         AS who_had_it,
      NVL(utl_role_parm.parm_value, '[NULL]') AS parm_value, 
      ( SELECT
           COUNT(*)
        FROM
           utl_user_role
        WHERE
           utl_user_role.role_id = utl_role_parm.role_id
      )                        AS affected_users_number
   FROM
      utl_role_parm
      INNER JOIN utl_role ON
         utl_role.role_id = utl_role_parm.role_id
   WHERE
      utl_role_parm.parm_name = 'HIDE_SCHED_LABOUR_HOURS'
   UNION ALL
   SELECT
      'User'                   AS scope,
      utl_user.username        AS who_had_it,
      NVL(utl_user_parm.parm_value, '[NULL]') AS parm_value,
      1                        AS affected_users_number
   FROM
      utl_user_parm
      INNER JOIN utl_user ON
         utl_user.user_id = utl_user_parm.user_id
   WHERE
      utl_user_parm.parm_name = 'HIDE_SCHED_LABOUR_HOURS'
)
ORDER BY
   DECODE(scope, 'Role', 1, 'User', 2, 3),
   who_had_it;
 
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
