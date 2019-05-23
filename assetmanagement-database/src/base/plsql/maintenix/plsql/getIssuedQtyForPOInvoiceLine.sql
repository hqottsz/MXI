--liquibase formatted sql


--changeSet getIssuedQtyForPOInvoiceLine:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getIssuedQtyForPOInvoiceLine
* Arguments:     an_POInvoiceDbId, an_POInvoiceId, an_POInvoiceLineId - pk for the PO Invoice Line
*           
* Description:   This function will return total issued quantity for all PO Lines Mapped to the specified
*                PO Invoice Line 
*
*
* Orig.Coder:    lsoh
* Recent Coder:  
* Recent Date:   2007-09-28
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getIssuedQtyForPOInvoiceLine
(
    an_POInvoiceDbId NUMBER,
    an_POInvoiceId NUMBER,
    an_POInvoiceLineId NUMBER
)  RETURN NUMBER
IS
   lTotalIssuedQt NUMBER;

   -- Define a cursor to retrieve the sum of csgn owed qt
   CURSOR lCurIssuedQt IS
      SELECT
           sum(req_part.csgn_owed_qt) AS issued_qt
      FROM
         req_part,
         inv_inv,
         po_invoice,
         po_invoice_line
     WHERE
          -- get po invoice
          po_invoice.po_invoice_db_id = an_POInvoiceDbId AND
          po_invoice.po_invoice_id    = an_POInvoiceId
          AND
          -- get part request with the same vendor
          req_part.csgn_vendor_db_id = po_invoice.vendor_db_id AND
          req_part.csgn_vendor_id    = po_invoice.vendor_id    AND
          req_part.rstat_cd	     = 0
          AND
          -- get reserved inventory
          inv_inv.inv_no_db_id = req_part.inv_no_db_id AND
          inv_inv.inv_no_id    = req_part.inv_no_id
          AND
          inv_inv.rstat_cd = 0
          AND
          -- get invoice line
          po_invoice_line.po_invoice_db_id = an_POInvoiceDbId AND
          po_invoice_line.po_invoice_id    = an_POInvoiceId AND
          po_invoice_line.po_invoice_line_id = an_POInvoiceLineId
          AND
          -- match reserved part with invoice line part
          po_invoice_line.part_no_db_id = inv_inv.part_no_db_id AND
          po_invoice_line.part_no_id    = inv_inv.part_no_id
      GROUP BY
            po_invoice_line.po_invoice_db_id,
            po_invoice_line.po_invoice_id,
            po_invoice_line.po_invoice_line_id;

      lRecIssuedQt lCurIssuedQt%ROWTYPE;
BEGIN

     -- Get sum of issued qt from cursor
     OPEN lCurIssuedQt;
     FETCH lCurIssuedQt INTO lRecIssuedQt;

       -- If no record return, return 0
       IF NOT lCurIssuedQt%FOUND THEN
          CLOSE lCurIssuedQt;
          RETURN 0;
       ELSE
           lTotalIssuedQt := lRecIssuedQt.issued_qt;
       END IF;

     CLOSE lCurIssuedQt;

     RETURN lTotalIssuedQt;

END getIssuedQtyForPOInvoiceLine;
/