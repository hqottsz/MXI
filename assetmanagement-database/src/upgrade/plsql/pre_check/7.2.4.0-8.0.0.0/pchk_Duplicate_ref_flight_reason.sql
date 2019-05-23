WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Duplicate_ref_flight_reason
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
 
----------------------------------------------------------------------------------------------
-- Pre-validation script for 8.0 - Event DBModel Development 
----------------------------------------------------------------------------------------------
----Identification of existing records that will cause Natural Key duplicates during upgrade to REF_FLIGHT_REASON

-- A flight leg status log is a duplicate if all the following attributes match
--   - DB ID  
--   - Display Code

--legacy key (ref_event_reason)
--   - event_reason_db_id
--   - event_reason_cd
-------------------------------------------------------------------------------

SELECT * FROM 
(
  SELECT 
         '&pchk_severity' AS "SEV",
         ref_event_reason.event_reason_db_id,
         ref_event_reason.event_reason_cd,
         ref_event_reason.user_reason_cd,
         COUNT(*) over (
                        PARTITION BY
                          ref_event_reason.event_reason_db_id,
                          ref_event_reason.user_reason_cd
                       ) AS nk_occurences                       
  FROM
      ref_event_reason 
  WHERE
      ref_event_reason.event_type_cd = 'FL'    
) 
WHERE nk_occurences > 1;  

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
