
package com.mxi.mx.core.unittest.table.po;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPriceTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for asserting data from the PO_LINE table.
 */
public class PoLine extends POLineTable {

   /**
    * Initializes the class.
    *
    * @param aPurchaseOrderLine
    *           primary key of the table.
    */
   public PoLine(PurchaseOrderLineKey aPurchaseOrderLine) {
      super( aPurchaseOrderLine );
   }


   /**
    * Returns the value of the shipment for external work order property.
    *
    * @param aTask
    *           the primary key of the external work order
    *
    * @return the value of the shipment for external work order property.
    */
   public static PurchaseOrderLineKey getPurchaseOrderLineForTask( TaskKey aTask ) {

      // Cols to select
      String[] lCols = { "po_db_id", "po_id", "po_line_id" };

      // Build the arguments
      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( "sched_db_id", aTask.getDbId() );
      lWhereClause.add( "sched_id", aTask.getId() );

      // Execute the query
      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( lCols, "po_line", lWhereClause );

      // Build the keys for return
      PurchaseOrderLineKey lPoLine = null;
      if ( lDataSet.next() ) {

         lPoLine = new PurchaseOrderLineKey( lDataSet.getInt( lCols[0] ),
               lDataSet.getInt( lCols[1] ), lDataSet.getInt( lCols[2] ) );
      }

      // Return the keys
      return lPoLine;
   }


   /**
    * Returns an array of po lines for the specified po.
    *
    * @param aPurchaseOrder
    *           purchase order primary key
    *
    * @return an array of po lines corresponding to the po
    */
   public static PurchaseOrderLineKey[] getPurchaseOrderLines( PurchaseOrderKey aPurchaseOrder ) {

      // Cols to select
      String[] lCols = { "po_db_id", "po_id", "po_line_id" };

      // Execute the query
      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( lCols, "po_line",
            aPurchaseOrder.getPKWhereArg() );

      // Build the keys for return
      PurchaseOrderLineKey[] lPOLines = new PurchaseOrderLineKey[lDataSet.getRowCount()];
      while ( lDataSet.next() ) {

         lPOLines[lDataSet.getRowNumber() - 1] =
               new PurchaseOrderLineKey( lDataSet.getInt( "po_db_id" ), lDataSet.getInt( "po_id" ),
                     lDataSet.getInt( "po_line_id" ) );
      }

      // Return the keys
      return lPOLines;
   }


   /**
    * Returns an array of po lines for the specified po. Lines are sorted by part number in
    * ascending order.
    *
    * @param aPurchaseOrder
    *           purchase order primary key
    *
    * @return an array of po lines corresponding to the po
    */
   public static PurchaseOrderLineKey[]
         getPurchaseOrderLinesSorted( PurchaseOrderKey aPurchaseOrder ) {

      // Cols to select
      String[] lCols = { "po_db_id", "po_id", "po_line_id", "part_no_id" };

      // Execute the query
      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( lCols, "po_line",
            aPurchaseOrder.getPKWhereArg() );

      // sort by part number in ascending order
      lDataSet.setSort( "dsString( part_no_id )", true );
      lDataSet.filterAndSort();

      // Build the keys for return
      PurchaseOrderLineKey[] lPOLines = new PurchaseOrderLineKey[lDataSet.getRowCount()];

      while ( lDataSet.next() ) {

         lPOLines[lDataSet.getRowNumber() - 1] =
               new PurchaseOrderLineKey( lDataSet.getInt( "po_db_id" ), lDataSet.getInt( "po_id" ),
                     lDataSet.getInt( "po_line_id" ) );
      }

      return lPOLines;
   }


   /**
    * Assert the account column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertAccount( FncAccountKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "account", aExpected, getAccount() );
   }


   /**
    * Assert the base_unit_price column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertBaseUnitPrice( BigDecimal aExpected ) {

      // Assert that the actual and expected base unit prices match
      MxAssert.assertEquals( "base_unit_price", aExpected, getBaseUnitPrice() );
   }


   /**
    * Assert the confirm_promised_by_bool column
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertConfirmPromisedByBool( boolean aExpected ) {
      MxAssert.assertEquals( "confirm_promise_by_bool", aExpected, isConfirmPromiseBy() );
   }


   /**
    * Asserts that the row does not exist in the table
    */
   public void assertDoesNotExist() {
      MxAssert.assertFalse( "The po_line table does have the row.", exists() );
   }


   /**
    * Asserts that the row exists in the table
    */
   public void assertExist() {
      MxAssert.assertTrue( "The po_line table does not have the row.", exists() );
   }


   /**
    * asserts the po_line_ext_sdesc value
    *
    * @param aExpected
    *           the expected po_line_ext_sdesc value
    */
   public void assertExternalReference( String aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "po_line_ext_sdesc", aExpected, getExternalReference() );
   }


   /**
    * Assert the line_ldesc column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertLineLdesc( String aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "line_ldesc", aExpected, getLineLdesc() );
   }


   /**
    * Assert the line_no_ord column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertLineNoOrd( int aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "line_no_ord", aExpected, getLineNoOrd() );
   }


   /**
    * Assert the maint_pickup_bool column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertMaintenanceReceipt( boolean aExpected ) {
      MxAssert.assertEquals( "maint_pickup_bool", aExpected, isMaintPickupBool() );
   }


   /**
    * Assert the order_qt column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertOrderQt( BigDecimal aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "order_qt", aExpected, getOrderQt() );
   }


   /**
    * Assert the owner_db_id and owner_id column
    *
    * @param aExpected
    *           the expected owner key value.
    */
   public void assertOwner( OwnerKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "owner", aExpected, getOwner() );
   }


   /**
    * Assert the part no column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertPartNo( PartNoKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "part_no", aExpected, getPartNo() );
   }


   /**
    * Assert the po line type column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertPoLineType( RefPoLineTypeKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "po_line_type", aExpected, getPoLineType() );
   }


   /**
    * Assert the pre_insp_qt column
    *
    * @param aExpected
    *           The expected value
    */
   public void assertPreInspQt( BigDecimal aExpected ) {
      MxAssert.assertEquals( "pre_insp_qt", aExpected, getPreInspQt() );
   }


   /**
    * Assert the price type matches the expected type.
    *
    * @param aExpected
    *           The expected price type
    */
   public void assertPriceType( RefPriceTypeKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "price_type", aExpected, getPriceType() );
   }


   /**
    * Assert the promise_by_dt column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertPromiseBy( Date aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "promise_by_dt", aExpected, getPromisedBy() );
   }


   /**
    * Assert the qty_unit column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertQtyUnit( RefQtyUnitKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "qty_unit", aExpected, getQtyUnit() );
   }


   /**
    * Assert the received_qt column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertReceivedQt( BigDecimal aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "RECEIVED_QT", aExpected, getReceivedQt() );
   }


   /**
    * Assert the receiver_note column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertReceiverNote( String aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "receiver_note", aExpected, getReceiverNote() );
   }


   /**
    * Assert the repl_task_db_id and repl_task_id column
    *
    * @param aExpected
    *           the expected owner key value.
    */
   public void assertReplTask( TaskKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "repl_task", aExpected, getReplTask() );
   }


   /**
    * Assert the return by date column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertReturnByDate( Date aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "return_by_dt", aExpected, getReturnByDt() );
   }


   /**
    * Assert the sched column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertSched( TaskKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "sched", aExpected, getTask() );
   }


   /**
    * Assert the unit_price column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertUnitPrice( BigDecimal aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "unit_price", aExpected, getUnitPrice() );
   }


   /**
    * Assert the vendor_note column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertVendorNote( String aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "vendor_note", aExpected, getVendorNote() );
   }


   /**
    * Asserts the warranty_bool column.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertWarrantyBool( boolean aExpected ) {
      MxAssert.assertEquals( "warranty_bool", aExpected, isWarrantyBool() );
   }


   /**
    * Assert the xchg_shipment_db_id and xchg_shipment_id column
    *
    * @param aExpected
    *           the expected owner key value.
    */
   public void assertXchgShipment( ShipmentKey aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "xchg_shipment", aExpected, getXchgShipment() );
   }
}
