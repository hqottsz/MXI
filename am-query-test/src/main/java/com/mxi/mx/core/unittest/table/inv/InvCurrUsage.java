
package com.mxi.mx.core.unittest.table.inv;

import javax.ejb.EJBException;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>inv_curr_usage</code> table.
 *
 * @author slr
 * @created March 21, 2002
 */
public class InvCurrUsage {

   private final com.mxi.mx.core.table.inv.InvCurrUsage iTable;


   /**
    * Initializes the class.
    *
    * @param aUsageParm
    *           Description of Parameter.
    */
   public InvCurrUsage(UsageParmKey aUsageParm) {
      iTable = com.mxi.mx.core.table.inv.InvCurrUsage.findByPrimaryKey( aUsageParm );
   }


   /**
    * Asserts that the row with <code>inv_no_db_id:inv_no_id</code> = <code>aInventory</code> and
    * <code>data_type_db_id:data_type_cd</code> = <code>aDataType</code> exists in the table.
    *
    * @param aInventory
    *           inventory.
    * @param aDataType
    *           data type.
    */
   public static void assertExist( InventoryKey aInventory, DataTypeKey aDataType ) {
      new InvCurrUsage( new UsageParmKey( aInventory, aDataType ) ).assertExists();
   }


   /**
    * Asserts that the row with <code>inv_no_db_id:inv_no_id</code> = <code>aInventory</code> and
    * <code>data_type_db_id:data_type_cd</code> = <code>aDataType</code> does not exist in the
    * table.
    *
    * @param aInventory
    *           inventory.
    * @param aDataType
    *           data type.
    */
   public static void assertNotExist( InventoryKey aInventory, DataTypeKey aDataType ) {
      new InvCurrUsage( new UsageParmKey( aInventory, aDataType ) ).assertNotExists();
   }


   /**
    * Asserts that the <code>tsi_qt</code> column has the <code>aExpected</code> value.
    *
    * @param aUsageParm
    *           the usage parameter to test.
    * @param aExpected
    *           the expected value for the <code>tsi_qt</code> column.
    */
   public static void assertTsiQt( UsageParmKey aUsageParm, Double aExpected ) {

      // Lookup the INV_CURR_USAGE row
      InvCurrUsage lInvCurrUsage = new InvCurrUsage( aUsageParm );

      // Ensure the expected and actual values match
      MxAssert.assertEquals( "tsi_qt", aExpected, lInvCurrUsage.getTsiQt() );
   }


   /**
    * Asserts that the <code>tsn_qt</code> was set properly
    *
    * @param aUsageParm
    *           Description of Parameter.
    * @param aExpected
    *           Expected value for tsn_qt
    */
   public static void assertTsnQt( UsageParmKey aUsageParm, Double aExpected ) {
      InvCurrUsage lInvCurrUsage = new InvCurrUsage( aUsageParm );

      MxAssert.assertEquals( "tsn_qt", aExpected, lInvCurrUsage.getTsnQt() );
   }


   /**
    * Asserts that the <code>tso_qt</code> was set properly
    *
    * @param aUsageParm
    *           key for usage param
    * @param aExpected
    *           Expected value for tso_qt
    */
   public static void assertTsoQt( UsageParmKey aUsageParm, Double aExpected ) {
      InvCurrUsage lInvCurrUsage = new InvCurrUsage( aUsageParm );

      MxAssert.assertEquals( "tso_qt", aExpected, lInvCurrUsage.getTsoQt() );
   }


   /**
    * Asserts all the usage values at once.
    *
    * @param aUsageParm
    *           The key
    * @param aTSN
    *           The TSN
    * @param aTSO
    *           The TSO
    * @param aTSI
    *           The TSI
    */
   public static void assertUsage( UsageParmKey aUsageParm, Double aTSN, Double aTSO,
         Double aTSI ) {
      InvCurrUsage lInvCurrUsage = new InvCurrUsage( aUsageParm );

      MxAssert.assertEquals( "tsn_qt", aTSN, lInvCurrUsage.getTsnQt() );
      MxAssert.assertEquals( "tso_qt", aTSO, lInvCurrUsage.getTsoQt() );
      MxAssert.assertEquals( "tsi_qt", aTSI, lInvCurrUsage.getTsiQt() );
   }


   /**
    * Asserts that the row with <code>inv_no_db_id:inv_no_id and <code>
    * data_type_db_id:data_type_cd</code> = has expected number of count.
    *
    * @param aInvKey
    *           The inventory
    * @param aExpected
    *           Expected row count to assert
    */
   public static void assertUsageCount( InventoryKey aInvKey, int aExpected ) {
      DataSetArgument lWhereClause;

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "inv_no_db_id", aInvKey.getDbId() );
      lWhereClause.add( "inv_no_id", aInvKey.getId() );

      QuerySet lResult = QuerySetFactory.getInstance().executeQuery( new String[] { "1" },
            "inv_curr_usage", lWhereClause );

      Assert.assertEquals( "The inv_curr_usage table does not have the " + aExpected + " rows.",
            aExpected, lResult.getRowCount() );
   }


   /**
    * Returns the value of the data types property.
    *
    * @param aInventory
    *           DOCUMENT ME!
    *
    * @return the value of the data types property.
    */
   public static DataTypeKey[] getDataTypes( InventoryKey aInventory ) {

      // Local variables
      DataTypeKey[] lDataTypes = null;

      // Create query parameters
      String[] lCols = { "data_type_db_id", "data_type_id" };
      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( "inv_no_db_id", aInventory.getDbId() );
      lWhereClause.add( "inv_no_id", aInventory.getId() );

      // Execute query
      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( lCols, "inv_curr_usage", lWhereClause );

      // Obtain the count of datatypes
      int lCount = lDataSet.getRowCount();
      lDataTypes = new DataTypeKey[lCount];

      // Populate datatype array
      for ( int i = 0; i < lCount; i++ ) {
         lDataSet.next();
         lDataTypes[i] =
               new DataTypeKey( lDataSet.getInt( lCols[0] ), lDataSet.getInt( lCols[1] ) );
      }

      // Return the list of datatypes
      return lDataTypes;
   }


   /**
    * Return number of data types being tracked for inventory
    *
    * @param aInventory
    *           InventoryKey
    *
    * @return The number of datatypes mapped to inventory
    */
   public static int getNumberDataTypes( InventoryKey aInventory ) {
      DataSetArgument lWhereClause;
      String[] lCols = { "data_type_db_id", "data_type_id" };

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "inv_no_db_id", aInventory.getDbId() );
      lWhereClause.add( "inv_no_id", aInventory.getId() );

      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( lCols, "inv_curr_usage", lWhereClause );

      return lDataSet.getRowCount();
   }


   /**
    * Returns the Tsi property of the InvCurrUsage.
    *
    * @param aInventory
    *           the inventory Pk.
    * @param aDataType
    *           Key for datatype.
    *
    * @return The Tsi value.
    */
   public static double getTsi( InventoryKey aInventory, DataTypeKey aDataType ) {

      // Return the Tsi value
      return getTsiQt( new UsageParmKey( aInventory, aDataType ) );
   }


   /**
    * Returns the TsiQt property of the InvCurrUsage.
    *
    * @param aUsageParm
    *           Usage parameter to get the value for.
    *
    * @return the value.
    */
   public static double getTsiQt( UsageParmKey aUsageParm ) {

      return new InvCurrUsage( aUsageParm ).getTsiQt();
   }


   /**
    * Returns the Tsn property of the InvCurrUsage.
    *
    * @param aInventory
    *           Description of Parameter.
    * @param aDataType
    *           Key for datatype
    *
    * @return The Tsn value.
    */
   public static double getTsn( InventoryKey aInventory, DataTypeKey aDataType ) {
      UsageParmKey lUsageParm = new UsageParmKey( aInventory, aDataType );

      return getTsnQt( lUsageParm );
   }


   /**
    * Returns the TsnQt property of the InvCurrUsage.
    *
    * @param aUsageParm
    *           Usage parameter to get the value for.
    *
    * @return the value.
    */
   public static double getTsnQt( UsageParmKey aUsageParm ) {
      InvCurrUsage lInvCurrUsage = new InvCurrUsage( aUsageParm );

      return lInvCurrUsage.getTsnQt();
   }


   /**
    * Returns the Tso property of the InvCurrUsage.
    *
    * @param aInventory
    *           Inventory key used to located InvCurrUsage entity
    * @param aDataType
    *           Key for datatype
    *
    * @return The Tso value.
    */
   public static double getTso( InventoryKey aInventory, DataTypeKey aDataType ) {
      UsageParmKey lUsageParm = new UsageParmKey( aInventory, aDataType );

      return getTsoQt( lUsageParm );
   }


   /**
    * Returns the TsoQt property of the InvCurrUsage.
    *
    * @param aUsageParm
    *           Usage parameter to get the value for.
    *
    * @return the value.
    */
   public static double getTsoQt( UsageParmKey aUsageParm ) {
      InvCurrUsage lInvCurrUsage = new InvCurrUsage( aUsageParm );

      return lInvCurrUsage.getTsoQt();
   }


   /**
    * Assert that the inv_curr_usage row exists.
    */
   public void assertExists() {
      try {

         // this will throw an exception if no rows are returned
         iTable.getInvNoDbId();
      } catch ( EJBException e ) {
         Assert.fail( "The inv_curr_usage table does not have the " + iTable.getPk() + " row." );
      }
   }


   /**
    * Assert that the inv_curr_usage row exists.
    */
   public void assertNotExists() {
      try {

         // this will throw an exception if no rows are returned
         iTable.getInvNoDbId();

         Assert.fail( "The inv_curr_usage table has the " + iTable.getPk() + " row." );
      } catch ( EJBException e ) {
         // expected exception
      }
   }


   /**
    * Asserts that the current TSN value is the same as the given value.
    *
    * @param aTsn
    *           The time since new
    */
   public void assertTsnQt( Double aTsn ) {
      if ( aTsn != null ) {
         Assert.assertEquals( aTsn.doubleValue(), iTable.getTsnQt().doubleValue(), 0.001 );
      } else {
         Assert.assertNull( iTable.getTsnQt() );
      }
   }


   public Double getTsnQt() {
      return iTable.getTsnQt();
   }


   private Double getTsiQt() {
      return iTable.getTsiQt();
   }


   private Double getTsoQt() {
      return iTable.getTsoQt();
   }
}
