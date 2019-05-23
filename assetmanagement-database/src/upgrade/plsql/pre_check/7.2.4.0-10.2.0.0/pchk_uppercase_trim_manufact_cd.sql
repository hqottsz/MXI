WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_uppercase_trim_manufact_cd
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
 
--------------------------------------------------------------------------------------------
-- Pre-validation script for upgrade to 8.2-SP4 in OPER-15625
--------------------------------------------------------------------------------------------
-- Identify existing data records in table EQP_MANUFACT that will prevent adding Natural Key
-- check constraint CK_EQPMANUFACT_MANUFACTCD during upgrade to 8.2-SP4 because automatic
-- conversion of field EQP_MANUFACT.MANUFACT_CD to uppercase trimmed value will produce the
-- duplicate Natural Key records
--------------------------------------------------------------------------------------------

COLUMN MANUFACT_CD FORMAT A11
COLUMN UPPERCASE_TRIMMED_CODE FORMAT A22

SELECT
   '&pchk_severity' AS "SEV",
   manufact_db_id,
   '[' || manufact_cd || ']'  AS manufact_cd,
   converted_value            AS trimmed_uppercase_code,
   manufact_name
FROM
(
   SELECT
      manufact_db_id,
      manufact_cd,
      UPPER(TRIM(manufact_cd)) AS converted_value,
      manufact_name,
      COUNT(*) OVER
      (
         PARTITION BY
            UPPER(TRIM(manufact_cd))
      )  AS dup_count
   FROM
      eqp_manufact
)
WHERE
   dup_count > 1
ORDER BY
   converted_value,
   manufact_db_id,
   manufact_cd;

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