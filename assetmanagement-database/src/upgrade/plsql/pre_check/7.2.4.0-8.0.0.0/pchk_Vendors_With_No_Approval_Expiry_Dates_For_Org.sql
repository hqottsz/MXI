WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Vendors_With_No_Approval_Expiry_Dates_For_Org
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
   '&pchk_severity' AS "SEV",
   org_vendor.vendor_db_id,
   org_vendor.vendor_id,
   org_vendor.vendor_cd, 
   org_vendor.vendor_name, 
   org_org.org_db_id,
   org_org.org_id,
   org_org.org_cd, 
   org_org.org_sdesc
FROM
   org_org_vendor
   INNER JOIN org_vendor ON 
      org_org_vendor.vendor_db_id = org_vendor.vendor_db_id AND
      org_org_vendor.vendor_id = org_vendor.vendor_id
   INNER JOIN org_org ON
      org_org_vendor.org_db_id = org_org.org_db_id AND
      org_org_vendor.org_id = org_org.org_id
WHERE
   org_org_vendor.vendor_status_db_id = 0 AND
   org_org_vendor.vendor_status_cd = 'APPROVED' AND
   org_org_vendor.approval_expiry_dt IS NULL;

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
