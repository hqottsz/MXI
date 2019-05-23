WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Vendor_Code_Spec2000_Compliance
DEFINE pchk_table_name=pchk_vend_code_spec2000_comp
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
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
----------------------------------------------------------------------------------------------
-- Pre-validation script for Quantas CRs (section 3.3.3 of concept)
----------------------------------------------------------------------------------------------
-- Author: Yuriy Vakulenko
--
-- Purpose:
-- 1) Warn the user about vendor code SPEC2000 compliance (if any)
-------------------------------------------------------------------------------
 
CREATE TABLE pchk_vend_code_spec2000_comp
AS
SELECT 
   vendor_cd,
   CAST(NULL AS VARCHAR2(500)) AS spec2000_message
FROM
   org_vendor
WHERE 
   rownum = 0
;

DECLARE
   SPEC2000_COMPLIANT_LEN CONSTANT NUMBER := 5;
   ln_NonCompliantVendorNum NUMBER;
   ls_ConfigParmValue utl_config_parm.parm_value%TYPE:='FALSE';
   ls_Message STRING(500);
   
   CURSOR lcur_GetConfigParm IS
      SELECT
         UPPER(t.parm_value)
      INTO
         ls_ConfigParmValue
      FROM
         utl_config_parm t
      WHERE
         t.parm_name='SPEC2000_VENDOR_CD_ENABLE';
      
BEGIN
   SELECT count(*) INTO ln_NonCompliantVendorNum FROM org_vendor t WHERE length(t.vendor_cd) > SPEC2000_COMPLIANT_LEN;
   IF (ln_NonCompliantVendorNum>0) THEN -- We have non compliant codes
      -- Retrieving the config parameter value
      OPEN lcur_GetConfigParm;
      FETCH lcur_GetConfigParm INTO ls_ConfigParmValue;
      CLOSE lcur_GetConfigParm;
   
      -- Selecting an appropriate message
      IF (ls_ConfigParmValue = 'TRUE') THEN
         ls_Message := 'All vendor codes must not exceed 5 characters in length. This wendor does not comply:';
      ELSE
         ls_Message := 'Currently, Maintenix Vendors are not setup to be SPEC2000 compliant.'|| CHR(10) || 
             'This optional feature can be enabled using the ''SPEC2000_VENDOR_CD_ENABLE'' config parameter.'|| CHR(10) || CHR(10) || 
             'If this feature is enabled, all vendor codes must not exceed 5 characters in length. This vendor would not comply:';
      END IF;

      -- Display the list of the violating vendor codes with the message
      FOR rec_OrgVendor IN (SELECT * FROM org_vendor t  WHERE length(t.vendor_cd) > SPEC2000_COMPLIANT_LEN) LOOP
         
         INSERT INTO
            pchk_vend_code_spec2000_comp
         VALUES
            (
               rec_OrgVendor.vendor_cd,
               ls_Message
            )
         ;
         
      END LOOP;
   END IF;

END;
/
  
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
   vendor_cd,
   spec2000_message
FROM
   pchk_vend_code_spec2000_comp
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
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
SET ECHO OFF
PROMPT ***
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
UNDEFINE pchk_file_name
UNDEFINE pchk_table_name
UNDEFINE pchk_severity
