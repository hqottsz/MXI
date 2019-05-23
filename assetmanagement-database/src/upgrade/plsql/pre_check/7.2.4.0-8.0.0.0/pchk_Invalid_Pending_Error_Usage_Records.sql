WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Invalid_Pending_Error_Usage_Records
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
-- Pre-validation script for 8.0 - Event DBModel Development 
----------------------------------------------------------------------------------------------
--
-- Purpose:
-- 1) Warn the user about UC/UR records that are invalid (due to their PENDING/ERROR status) that won't be upgraded and will be deleted

-- Key (evt_event):
-- event_db_id 
-- event_id 
-------------------------------------------------------------------------------

SELECT 
     '&pchk_severity' AS "SEV",
     evt_event.*
FROM 
     evt_event
WHERE 
     evt_event.event_type_db_id = 0 AND
     evt_event.event_status_db_id = 0 
     AND
     (
       (
          evt_event.event_type_cd = 'UR' AND
          evt_event.event_status_cd IN ('URPEND','URERROR')
       )
       OR
       (
          evt_event.event_type_cd = 'UC' AND
          evt_event.event_status_cd IN ('UCPEND','UCERROR')
       )     
     );

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
