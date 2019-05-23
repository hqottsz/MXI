WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_set_inventory_finance_status_cd_to_RCVD
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

COLUMN CONDITION FORMAT A9

SPOOL &pchk_file_name.html

SELECT
   '&pchk_severity' AS sev,
   eqp_part_no.part_no_oem AS "OEM PART NUMBER",
   inv_inv.serial_no_oem AS "SERIAL NO / BATCH NO",
   inv_inv.inv_cond_cd AS "CONDITION",
   ro_event.event_sdesc AS "ORDER",
   po_line.po_line_type_cd AS "TYPE",
   ro_event.event_status_cd AS "ORDER STATUS",
   ship_event.event_sdesc AS "SHIPMENT ID"
FROM
   inv_inv
   INNER JOIN po_header ON
      po_header.po_db_id = inv_inv.po_db_id AND
      po_header.po_id = inv_inv.po_id
   INNER JOIN ship_shipment ON
      ship_shipment.po_db_id = po_header.po_db_id AND
      ship_shipment.po_id = po_header.po_id
   INNER JOIN ship_shipment_line ON
      ship_shipment_line.shipment_db_id = ship_shipment.shipment_db_id AND
      ship_shipment_line.shipment_id = ship_shipment.shipment_id AND
      ship_shipment_line.inv_no_db_id = inv_inv.inv_no_db_id AND
      ship_shipment_line.inv_no_id = inv_inv.inv_no_id
   INNER JOIN evt_event ro_event ON
      ro_event.event_db_id = po_header.po_db_id AND
      ro_event.event_id = po_header.po_id
   INNER JOIN evt_event ship_event ON
      ship_event.event_db_id = ship_shipment.shipment_db_id AND
      ship_event.event_id = ship_shipment.shipment_id
   INNER JOIN po_line ON
      po_line.po_db_id = inv_inv.po_db_id AND
      po_line.po_id = inv_inv.po_id AND
      po_line.po_line_id = inv_inv.po_line_id
   INNER JOIN eqp_part_no ON
      eqp_part_no.part_no_db_id = po_line.part_no_db_id AND
      eqp_part_no.part_no_id = po_line.part_no_id
WHERE
   -- to get inbound shipments
   ship_shipment.shipment_type_db_id = 0 AND
   ship_shipment.shipment_type_cd = 'REPAIR'
   AND
   -- to get complete shipments
   ship_event.event_status_db_id = 0 AND
   ship_event.event_status_cd = 'IXCMPLT'
   AND
   -- to get active repair orders
   ro_event.hist_bool = 0
   AND
   -- to get those inventories where finance status code is 'INSP'.
   inv_inv.finance_status_cd = 'INSP'
   AND
   -- to get those inventories where condition is Inspection required
   inv_inv.inv_cond_db_id = 0 AND
   inv_inv.inv_cond_cd = 'INSPREQ';
 
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
