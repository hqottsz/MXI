WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_inv_has_shelf_life_expiry_date_but_part_has_no_shelf_life
DEFINE pchk_severity=INFO

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

COLUMN SHELF_EXPIRY_DATE FORMAT A17

SELECT
   '&pchk_severity' AS "SEV",
   eqp_part_no.part_no_db_id,
   eqp_part_no.part_no_id,
   eqp_part_no.part_no_oem,
   eqp_part_no.manufact_cd,
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   inv_inv.serial_no_oem,
   inv_inv.shelf_expiry_dt AS "SHELF_EXPIRY_DATE"
FROM  
   inv_inv
INNER JOIN eqp_part_no ON
   eqp_part_no.part_no_db_id  = inv_inv.part_no_db_id AND 
   eqp_part_no.part_no_id     = inv_inv.part_no_id
WHERE 
   eqp_part_no.shelf_life_unit_cd IS NULL AND 
   eqp_part_no.shelf_life_qt IS NULL 
   AND 
   inv_inv.shelf_expiry_dt IS NOT NULL
ORDER BY
   eqp_part_no.part_no_db_id,
   eqp_part_no.part_no_id,
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id;
 
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