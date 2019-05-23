WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_set_shipping_quantity_on_SHIPREQ_stock_levels
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
   inv_loc_stock.stock_no_db_id,
   inv_loc_stock.stock_no_id,
   inv_loc_stock.loc_db_id,
   inv_loc_stock.loc_id,
   inv_loc_stock.owner_db_id,
   inv_loc_stock.owner_id,
   eqp_stock_no.stock_no_cd AS "STOCK_CODE",
   eqp_stock_no.stock_no_name AS "STOCK_NAME",
   inv_loc.loc_cd AS "SUPPLY_LOCATION_CODE",
   inv_owner.owner_cd AS "OWNER_CODE",
   eqp_stock_no.ship_qt AS "DEFAULT_STOCK_SHIPPING_QTY",
   supplier_loc.loc_cd AS "SUPPLIER_LOCATION_CODE"
FROM 
   inv_loc_stock 
INNER JOIN eqp_stock_no ON 
   eqp_stock_no.stock_no_db_id = inv_loc_stock.stock_no_db_id AND 
   eqp_stock_no.stock_no_id    = inv_loc_stock.stock_no_id
INNER JOIN inv_loc ON 
   inv_loc.loc_db_id = inv_loc_stock.loc_db_id AND 
   inv_loc.loc_id    = inv_loc_stock.loc_id
INNER JOIN inv_owner ON 
   inv_owner.owner_db_id = inv_loc_stock.owner_db_id AND 
   inv_owner.owner_id    = inv_loc_stock.owner_id
INNER JOIN inv_loc supplier_loc ON 
   supplier_loc.loc_db_id  = inv_loc.hub_loc_db_id AND
   supplier_loc.loc_id     = inv_loc.hub_loc_id 
WHERE 
   inv_loc_stock.stock_low_actn_cd = 'SHIPREQ' AND 
   (
      inv_loc_stock.batch_size IS NULL OR 
      inv_loc_stock.batch_size <= 0
   )
ORDER BY 
   inv_loc_stock.stock_no_db_id, 
   inv_loc_stock.stock_no_id,
   inv_loc_stock.loc_db_id,
   inv_loc_stock.loc_id,
   inv_loc_stock.owner_db_id,
   inv_loc_stock.owner_id;
 
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
