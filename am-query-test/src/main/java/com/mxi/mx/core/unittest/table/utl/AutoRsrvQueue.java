
package com.mxi.mx.core.unittest.table.utl;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AutoReservationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>auto_rsrv_queue</code> table.
 *
 * @author cjb
 * @created July 22, 2005
 */
public class AutoRsrvQueue {

   /** Table name. */
   private static final String TABLE_NAME = "auto_rsrv_queue";

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "*" };


   /**
    * Initializes the class.
    *
    * @param aAutoRsrv
    *           the autoreservation request pk.
    */
   public AutoRsrvQueue(AutoReservationKey aAutoRsrv) {

      // set arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAutoRsrv, new String[] { "auto_rsrv_db_id", "auto_rsrv_id" } );

      // Initialize the row
      iActual = MxDataAccess.getInstance().executeQuery( iCols, TABLE_NAME, lArgs );
   }


   /**
    * DOCUMENT ME!
    *
    * @param aPartNo
    *           DOCUMENT ME!
    * @param aSupplyLocation
    *           DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static AutoRsrvQueue getAutoRsrvQueueContains( PartNoKey aPartNo,
         LocationKey aSupplyLocation ) {

      // Columns to retrieve
      String[] lCols = { "auto_rsrv_db_id", "auto_rsrv_id" };

      // Build the arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNo, new String[] { "part_no_db_id", "part_no_id" } );
      lArgs.add( aSupplyLocation, new String[] { "sup_loc_db_id", "sup_loc_id" } );

      // Execute the query
      DataSet lDs = MxDataAccess.getInstance().executeQuery( lCols, TABLE_NAME, lArgs );
      if ( lDs.first() ) {

         return new AutoRsrvQueue( new AutoReservationKey( lDs.getInt( "auto_rsrv_db_id" ),
               lDs.getInt( "auto_rsrv_id" ) ) );
      }

      return null;
   }


   /**
    * Asserts that the expected and actual values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertBomPart( PartGroupKey aExpected ) {

      // The actual pk and values
      PartGroupKey lActual = null;
      Integer lActualDbId = iActual.getIntegerAt( 1, "bom_part_db_id" );
      Integer lActualId = iActual.getIntegerAt( 1, "bom_part_id" );

      // Build the actual pk if not null
      if ( lActualDbId != null ) {
         lActual = new PartGroupKey( lActualDbId.intValue(), lActualId.intValue() );
      }

      // Assert the expected and actual match
      MxAssert.assertEquals( "BomPart", aExpected, lActual );
   }


   /**
    * Asserts that the expected and actual values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertExecuteBool( boolean aExpected ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "ExecuteBool", aExpected, iActual.getBooleanAt( 1, "execute_bool" ) );
   }


   /**
    * Asserts that the expected and actual values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertFailedBool( boolean aExpected ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "FailedBool", aExpected, iActual.getBooleanAt( 1, "failed_bool" ) );
   }


   /**
    * Asserts the row does not exist.
    */
   public void assertNotExists() {
      MxAssert.assertFalse( "Not Exists", iActual.first() );
   }


   /**
    * Asserts that the expected and actual values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertPartNo( PartNoKey aExpected ) {

      // The actual pk and values
      PartNoKey lActual = null;
      Integer lActualDbId = iActual.getIntegerAt( 1, "part_no_db_id" );
      Integer lActualId = iActual.getIntegerAt( 1, "part_no_id" );

      // Build the actual pk if not null
      if ( lActualDbId != null ) {
         lActual = new PartNoKey( lActualDbId.intValue(), lActualId.intValue() );
      }

      // Assert the expected and actual match
      MxAssert.assertEquals( "PartNo", aExpected, lActual );
   }


   /**
    * Asserts that the expected and actual result values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertResult( String aExpected ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "Result", aExpected, iActual.getStringAt( 1, "result" ) );
   }


   /**
    * Asserts that the expected and actual values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertSupLoc( LocationKey aExpected ) {

      // The actual pk and values
      LocationKey lActual = null;
      Integer lActualDbId = iActual.getIntegerAt( 1, "sup_loc_db_id" );
      Integer lActualId = iActual.getIntegerAt( 1, "sup_loc_id" );

      // Build the actual pk if not null
      if ( lActualDbId != null ) {
         lActual = new LocationKey( lActualDbId.intValue(), lActualId.intValue() );
      }

      // Assert the expected and actual match
      MxAssert.assertEquals( "SupLoc", aExpected, lActual );
   }


   /**
    * Asserts that the expected and actual values match.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertTryCount( Integer aExpected ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "TryCount", aExpected, iActual.getIntegerAt( 1, "try_count" ) );
   }


   /**
    * Returns the value of the execute bool property.
    *
    * @return the value of the execute bool property.
    */
   public boolean getExecuteBool() {
      return iActual.getBooleanAt( 1, "execute_bool" );
   }
}
