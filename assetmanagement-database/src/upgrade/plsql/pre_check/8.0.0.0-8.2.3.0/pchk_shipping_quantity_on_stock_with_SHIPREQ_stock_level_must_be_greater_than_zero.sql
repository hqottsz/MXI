WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_shipping_quantity_on_stock_with_SHIPREQ_stock_level_must_be_greater_than_zero
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

SELECT
   '&pchk_severity' AS "SEV",
   eqp_stock_no.stock_no_db_id, 
   eqp_stock_no.stock_no_id, 
   eqp_stock_no.stock_no_cd AS "STOCK_CODE",
   eqp_stock_no.stock_no_name AS "STOCK_NAME",
   eqp_stock_no.ship_qt AS "SHIPPING_QUANTITY" 
FROM  
   eqp_stock_no
WHERE 
   (
      eqp_stock_no.ship_qt IS NULL OR 
      eqp_stock_no.ship_qt <= 0
   )
   AND EXISTS 
   ( 
      SELECT 1 
      FROM 
         inv_loc_stock 
      WHERE 
         inv_loc_stock.stock_no_db_id  = eqp_stock_no.stock_no_db_id AND 
         inv_loc_stock.stock_no_id     = eqp_stock_no.stock_no_id 
         AND 
         inv_loc_stock.stock_low_actn_db_id  = 0 AND
         inv_loc_stock.stock_low_actn_cd     = 'SHIPREQ'
   );
 
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
