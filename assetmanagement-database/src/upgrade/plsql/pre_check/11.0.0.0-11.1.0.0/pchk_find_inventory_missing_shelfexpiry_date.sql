WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_find_inventory_missing_shelfexpiry_date
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

-----------------------------------------------------------------------------------------------
-- Purpose: Inform user about existing uninstalled inventories that are missing shelf expiry date 
-- but the associated part definition has shelf life defined
-----------------------------------------------------------------------------------------------

SELECT
   '&pchk_severity' AS "SEV",
   inv_inv.inv_no_db_id || ':' || inv_inv.inv_no_id AS inventory_key,
   eqp_part_no.part_no_oem AS part_number,
   eqp_part_no.shelf_life_qt || ' ' || ref_eng_unit.desc_sdesc as part_shelf_life,
   inv_inv.serial_no_oem AS inventory_serial_number,
   inv_inv.barcode_sdesc AS inventory_barcode,
   inv_inv.inv_cond_cd AS inventory_condition
FROM 
   eqp_part_no
   INNER JOIN inv_inv ON 
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id = eqp_part_no.part_no_id
   INNER JOIN ref_eng_unit ON
      ref_eng_unit.eng_unit_db_id = eqp_part_no.shelf_life_unit_db_id AND
      ref_eng_unit.eng_unit_cd    = eqp_part_no.shelf_life_unit_cd
WHERE
   inv_inv.shelf_expiry_dt IS NULL AND
   --inventory not installed 
   inv_inv.nh_inv_no_db_id IS NULL AND
   --part has shelf life
   eqp_part_no.shelf_life_qt IS NOT NULL AND
   eqp_part_no.shelf_life_unit_cd IS NOT NULL;

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