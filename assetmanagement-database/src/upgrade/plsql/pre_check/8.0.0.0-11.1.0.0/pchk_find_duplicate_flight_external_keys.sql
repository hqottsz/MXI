WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_find_duplicate_flight_external_keys
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

-----------------------------------------------------------------------------------------------
-- Purpose: Inform user about the existance of duplicate Flight External Key's against active Flights
-- 
-----------------------------------------------------------------------------------------------

SELECT
   '&pchk_severity' AS "SEV",
   ext_key AS Fl_External_Key,
   total AS Duplicate_Ext_Keys
FROM
    (SELECT COUNT(1) total, ext_key 
	 FROM fl_leg 
	 WHERE EXT_KEY IS NOT NULL AND 
	       hist_bool=0 
	GROUP BY fl_leg.ext_key)  
WHERE
    total > 1;

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