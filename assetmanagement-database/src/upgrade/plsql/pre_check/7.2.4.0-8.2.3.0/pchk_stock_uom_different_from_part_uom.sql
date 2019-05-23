WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_stock_uom_different_from_part_uom
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

COLUMN STOCK_INV_CLASS FORMAT A15
COLUMN STOCK_QTY_UNIT FORMAT A14
COLUMN PART_INV_CLASS FORMAT A14
COLUMN PART_QTY_UNIT FORMAT A13
COLUMN PART_STATUS FORMAT A11
   
SELECT
   '&pchk_severity' AS sev,
   eqp_stock_no.stock_no_db_id, 
   eqp_stock_no.stock_no_id, 
   eqp_stock_no.stock_no_cd   AS stock_cd,
   eqp_stock_no.stock_no_name AS stock_name,
   eqp_stock_no.inv_class_cd  AS stock_inv_class,
   eqp_stock_no.qty_unit_cd   AS stock_qty_unit,
   eqp_part_no.part_no_db_id,
   eqp_part_no.part_no_id,
   eqp_part_no.part_no_oem    AS part_oem,
   eqp_part_no.part_no_sdesc  AS part_desc,
   eqp_part_no.manufact_cd,
   eqp_part_no.inv_class_cd   AS part_inv_class,
   eqp_part_no.qty_unit_cd    AS part_qty_unit,
   eqp_part_no.part_status_cd AS part_status
FROM  
   eqp_stock_no
INNER JOIN eqp_part_no ON 
  eqp_part_no.stock_no_db_id = eqp_stock_no.stock_no_db_id AND 
  eqp_part_no.stock_no_id = eqp_stock_no.stock_no_id
WHERE 
  (eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd) NOT IN 
  ((eqp_stock_no.qty_unit_db_id, eqp_stock_no.qty_unit_cd)); 
 
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
