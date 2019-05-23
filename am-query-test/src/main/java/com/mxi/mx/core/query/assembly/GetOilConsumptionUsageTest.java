
package com.mxi.mx.core.query.assembly;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.task.PreviousMeasurement.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOilConsumptionUsageTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetOilConsumptionUsageTest.class );
   }


   /**
    * Tests the retrieval of oil consumption usage values.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetOilConsumptionUsage() throws Exception {

      int lEventDbId = 4650;
      int lEventId = 134134;

      DataSet lDataSet = this.execute( new EventKey( lEventDbId, lEventId ) );

      MxAssert.assertEquals( "Number of retrieved rows", 3, lDataSet.getRowCount() );

      // sort the data to enforce consistent results
      lDataSet.setSort( "inv_no_id", true );

      lDataSet.next();

      testRow( lDataSet, "4650", "134133", "1", "4650", "3008", "4650", "EA", "8.0", "4650",
            "373445", "100", "3", "5.0" );

      lDataSet.next();

      testRow( lDataSet, "4650", "134133", "1", "4650", "3007", "4650", "EA", "8.0", "4650",
            "373504", "10", "25", "5.0" );

      lDataSet.next();

      testRow( lDataSet, "4650", "134133", "1", "4650", "3007", "4650", "EA", "8.0", "4650",
            "373781", "10", "25", "5.0" );
   }


   /**
    * Assert the dabase rows.
    *
    * @param aDataSet
    *           the data set.
    * @param aEventDbId
    *           the event db id.
    * @param aEventId
    *           the event id.
    * @param aEventInvId
    *           the event inventory id.
    * @param aUptakeDataTypeDbId
    *           the uptake data type db id.
    * @param aUptakeDataTypeId
    *           the uptake data type id.
    * @param aRecEngUnitDbId
    *           the recorded engineering unit db id.
    * @param aRecEngUnitCd
    *           the recording engineering unit code.
    * @param aParmQt
    *           the parm quantity.
    * @param aInvNoDbId
    *           the inv no db id.
    * @param aInvNoId
    *           the inv no id.
    * @param aTimeDataTypeDbId
    *           the time data type db id.
    * @param aTimeDataTypeId
    *           the time data type id.
    * @param aTsnQt
    *           the TSN quantity value.
    */
   public void testRow( DataSet aDataSet, String aEventDbId, String aEventId, String aEventInvId,
         String aUptakeDataTypeDbId, String aUptakeDataTypeId, String aRecEngUnitDbId,
         String aRecEngUnitCd, String aParmQt, String aInvNoDbId, String aInvNoId,
         String aTimeDataTypeDbId, String aTimeDataTypeId, String aTsnQt ) {

      MxAssert.assertEquals( "sub_event_db_id", aEventDbId,
            aDataSet.getString( "sub_event_db_id" ) );

      MxAssert.assertEquals( "sub_event_id", aEventId, aDataSet.getString( "sub_event_id" ) );

      MxAssert.assertEquals( "sub_event_inv_id", aEventInvId,
            aDataSet.getString( "sub_event_inv_id" ) );

      MxAssert.assertEquals( "uptake_data_type_db_id", aUptakeDataTypeDbId,
            aDataSet.getString( "uptake_data_type_db_id" ) );

      MxAssert.assertEquals( "uptake_data_type_id", aUptakeDataTypeId,
            aDataSet.getString( "uptake_data_type_id" ) );

      MxAssert.assertEquals( "uptake_rec_eng_unit_db_id", aRecEngUnitDbId,
            aDataSet.getString( "uptake_rec_eng_unit_db_id" ) );

      MxAssert.assertEquals( "uptake_rec_eng_unit_cd", aRecEngUnitCd,
            aDataSet.getString( "uptake_rec_eng_unit_cd" ) );

      MxAssert.assertEquals( "uptake_parm_qt", aParmQt, aDataSet.getString( "uptake_parm_qt" ) );

      MxAssert.assertEquals( "inv_no_db_id", aInvNoDbId, aDataSet.getString( "inv_no_db_id" ) );

      MxAssert.assertEquals( "inv_no_id", aInvNoId, aDataSet.getString( "inv_no_id" ) );

      MxAssert.assertEquals( "time_data_type_db_id", aTimeDataTypeDbId,
            aDataSet.getString( "time_data_type_db_id" ) );

      MxAssert.assertEquals( "time_data_type_id", aTimeDataTypeId,
            aDataSet.getString( "time_data_type_id" ) );

      MxAssert.assertEquals( "tsn_qt", aTsnQt, aDataSet.getString( "tsn_qt" ) );
   }


   /**
    * This method executes the query in GetOilConsumptionUsage.qrx
    *
    * @param aEventKey
    *           The event key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( EventKey aEventKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aEventDbId", aEventKey.getDbId() );
      lDataSetArgument.add( "aEventId", aEventKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
