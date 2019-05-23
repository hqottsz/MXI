
package com.mxi.mx.core.unittest.table.po;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.TaxKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.procurement.TaxTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the PO_LINE_TAX table.
 *
 * @author $Author: rabson $
 * @version $Revision: 1.2 $
 */
public class PoLineTax {

   /**
    * DOCUMENT_ME
    *
    * @author yvakulenko
    */
   static enum COLUMNS {
      TAX_ID, TAX_RATE
   }


   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { COLUMNS.TAX_ID.name(), COLUMNS.TAX_RATE.name() };


   /**
    * Initializes the class.
    *
    * @param aPurchaseOrder
    *           primary key of the table.
    */
   public PoLineTax(PurchaseOrderKey aPurchaseOrder) {

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "po_line_tax",
            aPurchaseOrder.getPKWhereArg() );
   }


   /**
    * Initializes the class.
    *
    * @param aPurchaseOrderLine
    *           primary key of the table.
    */
   public PoLineTax(PurchaseOrderLineKey aPurchaseOrderLine) {

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "po_line_tax",
            aPurchaseOrderLine.getPKWhereArg() );
   }


   /**
    * Verifies that there are no taxes for the po line.
    */
   public void assertNoTax() {

      // Reset the result set
      if ( iActual.getRowCount() > 0 ) {
         MxAssert.fail( "This line has taxes." );
      }
   }


   /**
    * Asserts that the actual price for the tax is as expected.
    *
    * @param aTaxKey
    *           the type of tax
    * @param aExpectedPrice
    *           the expected price of the tax
    */
   public void assertTax( TaxKey aTaxKey, BigDecimal aExpectedPrice ) {

      // Filter the dataset to show only those rows with the specified tax
      iActual.setFilter( "dsFmtUuid( tax_id ) == \"" + aTaxKey.toString() + "\"" );

      // Determine if the charge exists against the po line
      if ( !iActual.isEmpty() ) {

         // Assert that the value of all the price values is equal to that given
         iActual.beforeFirst();
         while ( iActual.next() ) {
            MxAssert.assertEquals( COLUMNS.TAX_RATE.name(), aExpectedPrice,
                  iActual.getBigDecimal( COLUMNS.TAX_RATE.name() ) );
         }
      } else {
         TaxTable lTaxRow = TaxTable.findByPrimaryKey( aTaxKey );
         MxAssert.fail( "The " + lTaxRow.getTaxCode()
               + " tax is not levied against the purchase order line." );
      }

      // Reset the dataset
      iActual.setFilter( null );
   }


   /**
    * Asserts that the taxes assigned to a PO are the same as the given list and that the tax_rate
    * is 0.
    *
    * @param aTaxes
    *           The list of taxes to compare.
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertTaxes( Collection<TaxKey> aTaxes ) throws Exception {

      // Construct a unique list of tax types from the dataset
      Collection<TaxKey> lActualTaxes = null;
      iActual.beforeFirst();
      if ( iActual.hasNext() ) {
         lActualTaxes = new HashSet<TaxKey>();
         while ( iActual.next() ) {

            // Assert that the tax price is 0
            MxAssert.assertEquals( COLUMNS.TAX_RATE.name(), 0.0,
                  iActual.getDouble( COLUMNS.TAX_RATE.name() ) );

            // Get the next tax type
            lActualTaxes.add( new TaxKey( iActual.getUuid( COLUMNS.TAX_ID.name() ) ) );
         }
      }

      // Compare the 2 lists
      if ( !MxCoreUtils.isEqual( aTaxes, lActualTaxes ) ) {
         MxAssert.fail( "The taxes do not match." );
      }
   }
}
