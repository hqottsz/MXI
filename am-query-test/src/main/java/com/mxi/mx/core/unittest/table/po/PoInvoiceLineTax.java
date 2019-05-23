
package com.mxi.mx.core.unittest.table.po;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.TaxKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.procurement.TaxTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the PO_INVOICE_LINE_TAX table.
 *
 * @author $Author: rabson $
 * @version $Revision: 1.1 $
 */
public class PoInvoiceLineTax {

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "TAX_ID", "TAX_RATE" };


   /**
    * Initializes the class.
    *
    * @param aPurchaseInvoiceLine
    *           foreign key of the purchase order line
    */
   public PoInvoiceLineTax(PurchaseInvoiceLineKey aPurchaseInvoiceLine) {

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "po_invoice_line_tax",
            aPurchaseInvoiceLine.getPKWhereArg() );
   }


   /**
    * Initializes the class.
    *
    * @param aPurchaseInvoice
    *           primary key of the table.
    */
   public PoInvoiceLineTax(PurchaseInvoiceKey aPurchaseInvoice) {

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "po_invoice_line_tax",
            aPurchaseInvoice.getPKWhereArg() );
   }


   /**
    * Verifies that there are no taxes for the po invoiceline.
    */
   public void assertNoTax() {

      // Reset the result set
      if ( iActual.getRowCount() > 0 ) {
         MxAssert.fail( "This line has taxes." );
      }
   }


   /**
    * Asserts that the actual rate for the tax is as expected.
    *
    * @param aTaxKey
    *           the tax
    * @param aExpectedRate
    *           the expected rate of the tax
    */
   public void assertTax( TaxKey aTaxKey, BigDecimal aExpectedRate ) {

      // Determine if the tax exists against the po line
      if ( iActual.getRowCount() > 0 ) {

         // Filter the dataset to show only those rows with the specified tax
         iActual.setFilter( "dsFmtUuid( tax_id ) == \"" + aTaxKey.toString() + "\"" );

         // Assert that the value of all the rate values is equal to that given
         while ( iActual.next() ) {
            MxAssert.assertEquals( "tax_rate", aExpectedRate.doubleValue(),
                  iActual.getBigDecimal( "tax_rate" ).doubleValue(), 0.0001 );
         }
      } else {
         TaxTable lTaxRow = TaxTable.findByPrimaryKey( aTaxKey );
         MxAssert.fail( "The " + lTaxRow.getTaxCode()
               + " tax is not levied against the purchase order line." );
      }
   }


   /**
    * Asserts that the taxes assigned to this Purchase Invoice are the same as the given list and
    * that the tax_rate is 0.
    *
    * @param aTaxes
    *           The list of taxes to compare.
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertTaxes( Collection<TaxKey> aTaxes ) throws Exception {

      // Construct a unique list of taxes from the dataset
      Collection<TaxKey> lActualTaxes = null;
      iActual.beforeFirst();
      if ( iActual.hasNext() ) {
         lActualTaxes = new HashSet<TaxKey>();
         while ( iActual.next() ) {

            // Assert that the tax rate is 0
            MxAssert.assertEquals( "tax_rate", 0.0, iActual.getDouble( "tax_rate" ) );

            // Get the next tax
            lActualTaxes.add( new TaxKey( iActual.getUuid( "tax_id" ) ) );
         }
      }

      // Compare the 2 lists
      if ( !MxCoreUtils.isEqual( aTaxes, lActualTaxes ) ) {
         MxAssert.fail( "The taxes do not match." );
      }
   }
}
