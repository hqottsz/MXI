--liquibase formatted sql


--changeSet DEV-1423:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE TAX_CHARGE_PKG IS
   ----------------------------------------------------------------------------
   -- Object Name : TAX_CHARGE_PKG
   -- Object Type : Package Header
   -- Date        : Feb 09, 2012
   -- Coder       : yvakulenko
   -- Recent Date :
   -- Recent Coder:
   -- Description :
   -- This package contains methods/functions for Tax & Charge Management
   ----------------------------------------------------------------------------
   -- Copyright © 2010-2012 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   /********************************************************************************
   *
   * Procedure: taxRateModified
   * Arguments:
   *            aTaxId       (TAX.TAX_ID%TYPE) -- TAX entity ID;
   *            aPrevTaxRate (TAX.TAX_RATE%TYPE) -- Tax rate value prior to the
   *                                                modification;
   *
   * Description: If the Rate Percentage attribute is modified on a Tax:
   *              - Look up all never-issued & non-historic orders that have the Tax
   *                 - Recalculate the po_line.line_price for all lines
   *              - Re-evaluate whether the Order needs reauthorization
   *              - Look up all OPEN Invoices that have the Tax
   *                 - Recalculate the po_invoice_line.line_price for all lines
   *              - For RFQs, do not automatically adjust the taxes
   *              - Look up all historic Orders, Invoices, RFQs that have the Tax
   *                 - If the Tax Rate on the entity is null, update it to match the Rate on the Tax Entity (value prior to the modification)
   *                   However, if the Rate on the Tax Entity is null as well, then set it to 0%
   *
   *
   * Orig.Coder:     Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:    Feb 09, 2012
   *********************************************************************************/
   PROCEDURE taxRateModified(
      aTaxId in TAX.TAX_ID%TYPE,
      aPrevTaxRate in TAX.TAX_RATE%TYPE
      );

   /********************************************************************************
   *
   * Procedure: orderIssued
   * Arguments:
   *            aPoDbId      (po_line_tax.po_db_id%TYPE) -- Order Db Id;
   *            aPoId        (po_line_tax.po_id%TYPE)    -- Order Id;
   *
   * Description: When an Order is issued, for all po_line_tax values with null
   *              as the tax_rate:
   *              - Update the po_line_tax.compound_bool based on the tax value
   *              - Set the po_line_tax.tax_rate to the tax.tax_rate
   *                If null on the tax, then set the order line tax to 0
   *
   * Orig.Coder:     Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:    Feb 13, 2012
   *********************************************************************************/
   PROCEDURE orderIssued(
      aPoDbId in po_line_tax.po_db_id%TYPE,
      aPoId in po_line_tax.po_id%TYPE
      );

   /********************************************************************************
   *
   * Function: isVendorApplicableForTax
   * Arguments:
   *            aVendorDbId AND aVendorId     given VENDOR PK to evaluate to
   *            aTaxId                        given TAX PK. 
   *
   * Description: Checks whether the given VENDOR is applicable to be assigned to 
   *              the given TAX.
   *               
   * Return:      1 (TRUE)      If the given tax is NON COMPOUND
   *                            OR
   *                            If the given tax is COMPOUND and the VENDOR was 
   *                            not assigned to any COMPOUND tax before. 
   *                             
   *              0 (FALSE)     Otherwise 
   *
   * Orig.Coder:     Yuriy Vakulenko
   * Recent Coder:
   * Recent Date:    Mar 19, 2012
   *********************************************************************************/
   FUNCTION isVendorApplicableForTax(
      aVendorDbId in TAX_VENDOR.VENDOR_DB_ID%TYPE,
      aVendorId in TAX_VENDOR.VENDOR_ID%TYPE,
      aTaxId in TAX_VENDOR.TAX_ID%TYPE
      ) RETURN NUMBER;

END TAX_CHARGE_PKG;
/

--changeSet DEV-1423:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY TAX_CHARGE_PKG IS

/********************************************************************************
*
* Procedure: updateTaxRateForHistoric
* Arguments:
*            aTaxId       (TAX.TAX_ID%TYPE)   -- TAX entity ID;
*            aPrevTaxRate (TAX.TAX_RATE%TYPE) -- Tax percentage value prior to
*                                                the modification
*
* Description: Looks up all historic Orders, Invoices, RFQs that have the Tax
*                 - If the Tax Rate on the entity is null, update it to match
*                   the Rate on the Tax Entity (value prior to the modification)
*                   However, if the Rate on the Tax Entity is null as well,
*                   then set it to 0%
*
*
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Coder:
* Recent Date:    Feb 09, 2012
*
*********************************************************************************/
PROCEDURE updateTaxRateForHistoric(
   aTaxId in tax.tax_id%TYPE,
   aPrevTaxRate in tax.tax_rate%TYPE
) IS

lTaxRate tax.tax_rate%TYPE;

CURSOR HistoricPoLineTaxById IS
         SELECT
            po_line_tax.po_db_id,
            po_line_tax.po_id,
            po_line_tax.po_line_id,
            po_line_tax.tax_rate
         FROM
            po_line_tax
            INNER JOIN evt_event ON evt_event.event_db_id = po_line_tax.po_db_id AND
                                    evt_event.event_id    = po_line_tax.po_id
         WHERE
            po_line_tax.tax_id = aTaxId
            AND
            po_line_tax.tax_rate IS NULL
            AND
            evt_event.hist_bool=1;

CURSOR HistoricInvLineTaxById IS
         SELECT
            po_invoice_line_tax.po_invoice_db_id,
            po_invoice_line_tax.po_invoice_id,
            po_invoice_line_tax.po_invoice_line_id,
            po_invoice_line_tax.tax_rate
         FROM
            po_invoice_line_tax
            INNER JOIN evt_event ON evt_event.event_db_id = po_invoice_line_tax.po_invoice_db_id AND
                                    evt_event.event_id    = po_invoice_line_tax.po_invoice_id
         WHERE
            po_invoice_line_tax.tax_id = aTaxId
            AND
            po_invoice_line_tax.tax_rate IS NULL
            AND
            evt_event.hist_bool=1;

CURSOR HistoricRfqLineTaxById IS
         SELECT
            rfq_line_vendor_tax.rfq_db_id,
            rfq_line_vendor_tax.rfq_id,
            rfq_line_vendor_tax.rfq_line_id,
            rfq_line_vendor_tax.tax_rate
         FROM
            rfq_line_vendor_tax
            INNER JOIN evt_event ON evt_event.event_db_id = rfq_line_vendor_tax.rfq_db_id AND
                                    evt_event.event_id    = rfq_line_vendor_tax.rfq_id
         WHERE
            rfq_line_vendor_tax.tax_id = aTaxId
            AND
            rfq_line_vendor_tax.tax_rate IS NULL
            AND
            evt_event.hist_bool=1;

BEGIN
   -- if the previous Rate value of the Tax Entity is null...
   IF aPrevTaxRate IS NULL THEN
      lTaxRate := 0; -- set it to 0%
   ELSE
      lTaxRate := aPrevTaxRate; -- otherwise update to match to the previous Rate
   END IF;

   -- iterate trough all HISTORIC order line taxes that equal the given TAX
   FOR lineTaxes IN HistoricPoLineTaxById LOOP

      -- update the inventory line tax with new unit price
      UPDATE
         po_line_tax
      SET
         tax_rate=lTaxRate
      WHERE
         po_line_tax.po_db_id=lineTaxes.po_db_id AND
         po_line_tax.po_id = lineTaxes.po_id AND
         po_line_tax.po_line_id = lineTaxes.po_line_id;
   END LOOP;

   -- iterate trough all HISTORIC invoice line taxes that equal the given TAX
   FOR lineTaxes IN HistoricInvLineTaxById LOOP

      -- update the inventory line tax with new unit price
      UPDATE
         po_invoice_line_tax
      SET
         tax_rate=lTaxRate
      WHERE
         po_invoice_line_tax.po_invoice_db_id=lineTaxes.po_invoice_db_id AND
         po_invoice_line_tax.po_invoice_id = lineTaxes.po_invoice_id AND
         po_invoice_line_tax.po_invoice_line_id = lineTaxes.po_invoice_line_id;
   END LOOP;

   -- iterate trough all HISTORIC invoice line taxes that equal the given TAX
   FOR lineTaxes IN HistoricRfqLineTaxById LOOP

      -- update the inventory line tax with new unit price
      UPDATE
         rfq_line_vendor_tax
      SET
         tax_rate=lTaxRate
      WHERE
         rfq_line_vendor_tax.rfq_db_id=lineTaxes.rfq_db_id AND
         rfq_line_vendor_tax.rfq_id = lineTaxes.rfq_id AND
         rfq_line_vendor_tax.rfq_line_id = lineTaxes.rfq_line_id;
   END LOOP;
END updateTaxRateForHistoric;

/********************************************************************************
* Inherits doc (see the header)
*********************************************************************************/
PROCEDURE taxRateModified(
   aTaxId in TAX.TAX_ID%TYPE,
   aPrevTaxRate in TAX.TAX_RATE%TYPE
) IS
BEGIN
  updateTaxRateForHistoric(aTaxId, aPrevTaxRate);
END taxRateModified;

/********************************************************************************
* Inherits doc (see the header)
*********************************************************************************/
PROCEDURE orderIssued(
   aPoDbId in po_line_tax.po_db_id%TYPE,
   aPoId in po_line_tax.po_id%TYPE
) IS
BEGIN
   UPDATE  (
      SELECT
         po_line_tax.tax_rate AS line_rate,
         po_line_tax.compound_bool AS line_compound,
         DECODE(tax.tax_rate, NULL, 0, tax.tax_rate) AS entity_rate,
         tax.compound_bool AS entity_compound
      FROM
         po_line_tax
         INNER JOIN tax ON tax.tax_id = po_line_tax.tax_id
      WHERE
         po_line_tax.po_db_id = aPoDbId AND
         po_line_tax.po_id = aPoId
         AND
         (po_line_tax.tax_rate IS NULL
          OR
          po_line_tax.compound_bool IS NULL)
      ) lines
      SET
         lines.line_compound=lines.entity_compound, lines.line_rate=lines.entity_rate;
END;

/********************************************************************************
* Inherits doc (see the header)
*********************************************************************************/
FUNCTION isVendorApplicableForTax(
   aVendorDbId in TAX_VENDOR.VENDOR_DB_ID%TYPE,
   aVendorId in TAX_VENDOR.VENDOR_ID%TYPE,
   aTaxId in TAX_VENDOR.TAX_ID%TYPE
   ) RETURN NUMBER IS
lRes INTEGER;
lCompoundBool INTEGER;
BEGIN
   SELECT
      tax.compound_bool
   INTO
      lCompoundBool
   FROM
      tax
   WHERE
      tax.tax_id=aTaxId;
      
   IF lCompoundBool = 0 THEN
      RETURN 1;
   END IF;
   
   SELECT
      CASE COUNT(tax.tax_id) WHEN 0 THEN 1 ELSE 0 END
   INTO
      lRes
   FROM
      tax_vendor
      INNER JOIN tax ON tax.tax_id = tax_vendor.tax_id
   WHERE
      tax_vendor.vendor_db_id=aVendorDbId AND
      tax_vendor.vendor_id=aVendorId
      AND
      tax.compound_bool=1;

   RETURN lRes;
END isVendorApplicableForTax;
END TAX_CHARGE_PKG;
/

--changeSet DEV-1423:3 stripComments:false
-- Conditional insert
INSERT INTO
 utl_work_item_type
(
  name, worker_class, work_manager, enabled, utl_id
)
SELECT 'Tax_Rate_Modified', 'com.mxi.mx.core.worker.procurement.TaxRateModifiedWorker', 'wm/Maintenix-DefaultWorkManager', 1, 0
FROM
  dual
WHERE
  NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'Tax_Rate_Modified' );