WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Invalid_MRO_OPER_Hierarchy
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
-- Pre-validation script for QC-6336
----------------------------------------------------------------------------------------------
-- Author: sdevi
--
-- Purpose:
-- 1) Warn the user about invalid configurations for MRO/OPERATOR organizations that require manual intervention.
--   the MRO's and operators that are not directly below the ROOT organization are considered as invalid configurations.
--
-- WARNING: The following MROs/Operators are not under the ROOT organization, the upgrade will move these organizations under the ROOT organization.
----------------------------------------------------------------------------------------------
 
SELECT
  '&pchk_severity' AS "SEV",
  org_org.org_cd,
  org_org.org_sdesc,
  org_org.org_type_cd
FROM 
  org_org
WHERE
  --find all MRO and OPERATORs which are not under the Root organization
  org_org.org_type_db_id = 0 AND
  org_org.org_type_cd IN ('MRO', 'OPERATOR')
  AND EXISTS 
  (
    SELECT 1
    FROM 
   org_org parent_org
    WHERE
   parent_org.org_db_id = org_org.nh_org_db_id AND
   parent_org.org_id    = org_org.nh_org_id
   AND
   parent_org.nh_org_db_id IS NOT NULL
  )
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
