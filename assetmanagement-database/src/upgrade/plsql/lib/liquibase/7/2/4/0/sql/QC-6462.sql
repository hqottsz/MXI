--liquibase formatted sql


--changeSet QC-6462:1 stripComments:false
/********************************************************************
 * Migrate the charge price to the charge amount within the 
 * procurement line charge tables.
 *
 * The charge amount will only be overwritten if it is null, 
 * thus preserving any previous value.
 ********************************************************************/
UPDATE 
   po_line_charge
SET
   charge_amount = charge_price
WHERE
   charge_amount IS NULL
;

--changeSet QC-6462:2 stripComments:false
UPDATE 
   po_invoice_line_charge
SET
   charge_amount = charge_price
WHERE
   charge_amount IS NULL
;

--changeSet QC-6462:3 stripComments:false
UPDATE 
   rfq_line_vendor_charge
SET
   charge_amount = charge_price
WHERE
   charge_amount IS NULL
; 