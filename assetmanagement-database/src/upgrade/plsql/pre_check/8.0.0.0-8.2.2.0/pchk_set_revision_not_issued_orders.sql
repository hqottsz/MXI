WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_set_revision_not_issued_orders
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
    '&pchk_severity' AS SEV,
    po_event.event_sdesc AS "ORDER NUMBER",
    po_status.user_status_cd AS "ORDER STATUS",
    po_header.po_type_cd AS "ORDER TYPE",
    po_header.issued_dt AS "ISSUE DATE",
    po_header.po_revision_no AS "ISSUE NUMBER"
FROM 
   po_header
   -- join to determine po number
   INNER JOIN evt_event po_event ON
      po_event.event_db_id = po_header.po_db_id AND
      po_event.event_id    = po_header.po_id
   -- join to determine the order status
   INNER JOIN ref_event_status po_status ON
      po_status.event_status_db_id = po_event.event_status_db_id AND
      po_status.event_status_cd    = po_event.event_status_cd
WHERE   
   po_header.po_revision_no = 1 
   AND 
   po_header.issued_dt IS NULL
   AND
   po_header.rstat_cd = 0;
   
 
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
