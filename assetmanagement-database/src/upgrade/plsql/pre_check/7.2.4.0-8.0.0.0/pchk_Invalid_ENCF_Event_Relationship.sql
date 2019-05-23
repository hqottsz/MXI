WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Invalid_ENCF_Event_Relationship
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
-- 1) Warn the user about invalid ENCF event relationships that will not be upgraded
-------------------------------------------------------------------------------
SELECT
  '&pchk_severity' AS "SEV",
  rel.event_db_id,
  rel.event_id,
  rel.event_rel_id,
  subj.event_sdesc,
  subj.event_type_cd,
  'ENCF',
  obj.event_sdesc,
  obj.event_type_cd
FROM
  evt_event_rel rel
  JOIN evt_event subj ON
       subj.event_db_id = rel.event_db_id AND
       subj.event_id    = rel.event_id       
  JOIN evt_event obj ON
       obj.event_db_id = rel.rel_event_db_id AND
       obj.event_id    = rel.rel_event_id 
WHERE
  rel.rel_type_db_id = 0 AND
  rel.rel_type_cd    = 'ENCF'
  AND
   (  
    obj.event_type_cd <> 'CF'
    OR
    subj.event_type_cd <> 'FL'
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
