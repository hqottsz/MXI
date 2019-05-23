WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_OrderLines_MoreThan_99
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
-- Pre-validation script for Quantas CRs part 3(section 3.3.3 of design doc)
----------------------------------------------------------------------------------------------
-- Author: sdevi
--
-- Purpose:
-- 1)For each Order that has more than 99 po_lines, notify the user of the offending Order Barcode.
-------------------------------------------------------------------------------

SELECT   
   '&pchk_severity' AS "SEV",
   evt_event.event_sdesc as barcode,
   COUNT(po_line.po_id) AS order_lines
FROM 
   po_header
   INNER JOIN evt_event ON evt_event.event_db_id = po_header.po_db_id AND
                     evt_event.event_id    = po_header.po_id
   INNER JOIN po_line ON po_line.po_db_id = po_header.po_db_id AND
                  po_line.po_id    = po_header.po_id
GROUP BY 
   evt_event.event_sdesc                   
HAVING 
   COUNT(po_line.po_id) > 99;

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
