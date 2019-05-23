
package com.mxi.mx.core.unittest.table.evt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>evt_inv</code> table. It is a slightly different
 * from the rest of table test classes, as columns in the table would be either all or only primary
 * key filled in.
 *
 * @author asmolko
 * @created March 18, 2002
 */
public class EvtInv {

   /** Columns in the table. */

   private static final String[] COLUMNS = { "event_db_id", "event_id", "inv_no_db_id", "inv_no_id",
         "nh_inv_no_db_id", "nh_inv_no_id", "assmbl_inv_no_db_id", "assmbl_inv_no_id",
         "h_inv_no_db_id", "h_inv_no_id", "assmbl_db_id", "assmbl_cd", "assmbl_bom_id",
         "assmbl_pos_id", "part_no_db_id", "part_no_id", "bom_part_db_id", "bom_part_id",
         "main_inv_bool", "event_inv_id", "bin_qt" };

   /** Results of the query. */
   private DataSet iActual;


   /**
    * Initializes the class.
    *
    * @param aEvent
    *           primary key of the table.
    * @param aInventory
    *           inventory key
    */
   public EvtInv(EventKey aEvent, InventoryKey aInventory) {
      DataSetArgument lWhereClause;

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "event_db_id", aEvent.getDbId() );
      lWhereClause.add( "event_id", aEvent.getId() );
      lWhereClause.add( "inv_no_db_id", aInventory.getDbId() );
      lWhereClause.add( "inv_no_id", aInventory.getId() );
      iActual = MxDataAccess.getInstance().executeQuery( COLUMNS, "evt_inv", lWhereClause );
   }


   /**
    * Returns latest event for an inventory
    *
    * @param aInventory
    *           inventory primary key
    *
    * @return first returned inventory.
    */
   public static EventKey getLatestEventForInv( InventoryKey aInventory ) {
      List<EventKey> lEvents = getLatestEventsForInv( aInventory, 1 );
      if ( lEvents.isEmpty() ) {
         return null;
      } else {
         return lEvents.get( 0 );
      }
   }


   /**
    * Returns latest event for in inventory
    *
    * @param aInventory
    *           inventory primary key
    * @param aCount
    *           the maximum number of rows to return
    *
    * @return first returned inventory.
    */
   public static List<EventKey> getLatestEventsForInv( InventoryKey aInventory, int aCount ) {

      // cols to select
      String[] lCols = { "event_db_id", "event_id" };

      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( aInventory, "inv_no_db_id", "inv_no_id" );

      // execute query
      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( lCols, "evt_inv", lWhereClause );
      lDataSet.setSort( "dsInteger( event_id )", false ); // sort descending

      int lRemaining = aCount;

      List<EventKey> lEvents = new ArrayList<EventKey>( lDataSet.getRowCount() );
      while ( lDataSet.next() && ( lRemaining > 0 ) ) {
         lEvents.add( lDataSet.getKey( EventKey.class, "event_db_id", "event_id" ) );
         lRemaining--;
      }

      return lEvents;
   }


   /**
    * Returns an array of events corresponding to the inventory.
    *
    * @param aInventory
    *           invwentory primary key
    *
    * @return first returned inventory.
    */
   public static EventKey[] getEventsForInventory( InventoryKey aInventory ) {

      // cols to select
      String[] lCols = { "event_db_id", "event_id", "creation_dt" };

      // EventKey
      EventKey[] lEvent;

      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( "inv_no_db_id", aInventory.getDbId() );
      lWhereClause.add( "inv_no_id", aInventory.getId() );

      // execute query
      DataSet lDataSet = MxDataAccess.getInstance().executeQuery( lCols, "evt_inv", lWhereClause );
      lDataSet.setSort( new String[] { "dsInteger(event_id)" }, new boolean[] { true } );
      lEvent = new EventKey[lDataSet.getRowCount()];

      int lCount = 0;
      while ( lDataSet.next() ) {

         lEvent[lCount] =
               new EventKey( lDataSet.getInt( "event_db_id" ), lDataSet.getInt( "event_id" ) );
         lCount++;
      }

      return lEvent;
   }


   /**
    * Asserts that the row for the event/inventory pair is in the table.
    */
   public void assertExist() {
      if ( !iActual.hasNext() ) {
         Assert.fail(
               "The evt_inv table does not have the row for the specified event/inventory." );
      }
   }


   /**
    * Asserts that the <code>h_inv_no_db_id:h_inv_no_id</code> key and the argument are equal.
    *
    * @param aHInventoryKey
    *           Highest inventory to assert against actual data.
    */
   public void assertHInventory( InventoryKey aHInventoryKey ) {

      // Compare the keys
      MxAssert.assertEquals( "HInventory", aHInventoryKey, new InventoryKey(
            iActual.getIntAt( 1, "h_inv_no_db_id" ), iActual.getIntAt( 1, "h_inv_no_id" ) ) );
   }


   /**
    * Asserts that the <code>nh_inv_no_db_id:nh_inv_no_id</code> key and the argument are equal.
    *
    * @param aNhInventoryKey
    *           Next highest inventory to assert against actual data.
    */
   public void assertNhInventory( InventoryKey aNhInventoryKey ) {

      // Compare the keys
      if ( aNhInventoryKey == null ) {
         MxAssert.assertEquals( "NhInventory", "0:0",
               new InventoryKey( iActual.getIntAt( 1, "nh_inv_no_db_id" ),
                     iActual.getIntAt( 1, "nh_inv_no_id" ) ).toString() );
      } else {
         MxAssert.assertEquals( "NhInventory", aNhInventoryKey, new InventoryKey(
               iActual.getIntAt( 1, "nh_inv_no_db_id" ), iActual.getIntAt( 1, "nh_inv_no_id" ) ) );
      }
   }
}
