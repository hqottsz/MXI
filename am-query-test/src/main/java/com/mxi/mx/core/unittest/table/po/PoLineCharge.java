
package com.mxi.mx.core.unittest.table.po;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.ChargeKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.procurement.ChargeTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the PO_LINE_CHARGE table.
 *
 * @author $Author: rabson $
 * @version $Revision: 1.2 $
 */
public class PoLineCharge {

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "CHARGE_ID", "CHARGE_AMOUNT" };


   /**
    * Initializes the class.
    *
    * @param aPurchaseOrder
    *           primary key of the table.
    */
   public PoLineCharge(PurchaseOrderKey aPurchaseOrder) {

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "po_line_charge",
            aPurchaseOrder.getPKWhereArg() );
   }


   /**
    * Initializes the class.
    *
    * @param aPurchaseOrderLine
    *           primary key of the purchase order line.
    */
   public PoLineCharge(PurchaseOrderLineKey aPurchaseOrderLine) {

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "po_line_charge",
            aPurchaseOrderLine.getPKWhereArg() );
   }


   /**
    * Asserts that the actual price for the charge is as expected.
    *
    * @param aChargeKey
    *           the charge
    * @param aExpectedPrice
    *           the expected price of the charge
    */
   public void assertCharge( ChargeKey aChargeKey, BigDecimal aExpectedPrice ) {

      // Determine if the charge exists against the po line
      if ( iActual.getRowCount() > 0 ) {

         // Filter the dataset to show only those rows with the specified charge
         iActual.setFilter( "dsFmtUuid( charge_id ) == \"" + aChargeKey.toString() + "\"" );

         // Assert that the value of all the price values is equal to that given
         while ( iActual.next() ) {
            MxAssert.assertEquals( "charge_amount", aExpectedPrice,
                  iActual.getBigDecimal( "charge_amount" ) );
         }
      } else {
         MxAssert.fail( "The " + ChargeTable.findByPrimaryKey( aChargeKey ).getChargeCode()
               + " charge is not levied against the purchase order line." );
      }

      // Reset the dataset
      iActual.setFilter( null );
   }


   /**
    * Asserts that the charges assigned to a PO are the same as the given list and that the
    * charge_amount is 0.
    *
    * @param aCharges
    *           The list of charges to compare.
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertCharges( Collection<ChargeKey> aCharges ) throws Exception {

      // Construct a unique list of charges from the dataset
      Collection<ChargeKey> lActualCharges = null;
      iActual.beforeFirst();
      if ( iActual.hasNext() ) {
         lActualCharges = new HashSet<ChargeKey>();
         while ( iActual.next() ) {

            // Assert that the charge amount is 0
            MxAssert.assertEquals( "charge_amount", 0.0, iActual.getDouble( "charge_amount" ) );

            // Get the next charge type
            lActualCharges.add( new ChargeKey( iActual.getUuid( "charge_id" ) ) );
         }
      }

      // Compare the 2 lists
      if ( !MxCoreUtils.isEqual( aCharges, lActualCharges ) ) {
         MxAssert.fail( "The charges do not match." );
      }
   }


   /**
    * Verifies that there are no charges for the po line.
    */
   public void assertNoCharge() {

      // Reset the result set
      if ( iActual.getRowCount() > 0 ) {
         MxAssert.fail( "This line has charges." );
      }
   }
}
