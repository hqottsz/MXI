--liquibase formatted sql


--changeSet tax_charge_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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