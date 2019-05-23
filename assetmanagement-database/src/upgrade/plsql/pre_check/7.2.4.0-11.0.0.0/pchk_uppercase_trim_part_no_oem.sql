WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_uppercase_trim_part_no_oem
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
-- Pre-validation script for upgrade to 8.3 in OPER-17884
----------------------------------------------------------------------------------------------
----Identification of existing records that will cause Natural Key duplicates during upgrade to EQP_PART_NO

-- A Part Number is a duplicate if following attributes match
--	  - part_no_oem, manufact_cd combination
-------------------------------------------------------------------------------
SELECT
   '&pchk_severity' AS "SEV",
	part_no_db_id,
	part_no_id,
   '[' || part_no_oem || ']'  AS part_no_oem,
   '[' || manufact_cd || ']'  AS manufact_cd,
   converted_value            AS trimmed_uppercase_code,
	part_no_sdesc
FROM
(
   SELECT
      part_no_db_id,
      part_no_id,
      manufact_cd,
      part_no_oem,
      part_no_sdesc,
      TRIM(UPPER(part_no_oem)) AS converted_value,
      COUNT(*) OVER
      (
         PARTITION BY
            UPPER(TRIM(part_no_oem)),
            UPPER(TRIM(manufact_cd))
      )  AS dup_count
   FROM
      eqp_part_no
)
WHERE
   dup_count > 1
   AND
   -- Excluded cases where duplicates were caused solely my Manfacturer Code
  UPPER(TRIM(manufact_cd)) NOT IN
   (
      SELECT
         UPPER(TRIM(manufact_cd))
      FROM
         eqp_manufact
      GROUP BY
         UPPER(TRIM(manufact_cd))
      HAVING
         COUNT(*) > 1
   )
ORDER BY
   converted_value,
   UPPER(TRIM(manufact_cd)),
   part_no_oem,
   manufact_cd,
   part_no_db_id,
   part_no_id
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