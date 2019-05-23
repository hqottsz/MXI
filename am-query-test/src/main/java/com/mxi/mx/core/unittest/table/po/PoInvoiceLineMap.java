
package com.mxi.mx.core.unittest.table.po;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the PO_INVOICE_LINE_MAP table.
 *
 * @author rabson
 */
public class PoInvoiceLineMap {

   /** Results of the query. */
   private DataSet iActual;


   /**
    * Initializes the class.
    *
    * @param aPOInvoiceLine
    *           the po invoice line
    * @param aPOLine
    *           the po line
    */
   public PoInvoiceLineMap(PurchaseInvoiceLineKey aPOInvoiceLine, PurchaseOrderLineKey aPOLine) {

      // Obtain actual value
      DataSetArgument lArgs = aPOInvoiceLine.getPKWhereArg();
      lArgs.add( aPOLine.getPKWhereArg() );
      iActual = MxDataAccess.getInstance().executeQuery( new String[] { "*" },
            "po_invoice_line_map", lArgs );
   }


   /**
    * Asserts that the row does not exist in table.
    */
   public void assertDoesNotExist() {
      if ( iActual.hasNext() ) {
         MxAssert.fail( "The po_invoice_line_map table has a row" );
      }
   }


   /**
    * Asserts that the row exists in table.
    */
   public void assertExists() {
      if ( !iActual.hasNext() ) {
         MxAssert.fail( "The po_invoice_line_map table does not have a row" );
      }
   }
}
