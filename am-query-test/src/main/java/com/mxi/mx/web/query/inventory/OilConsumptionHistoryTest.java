
package com.mxi.mx.web.query.inventory;

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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvOilStatusLogKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.inventory.OilConsumptionHistory.qrx
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OilConsumptionHistoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), OilConsumptionHistoryTest.class );
   }


   /**
    * Tests the retrieval of the oil consumption history.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testOilConsumptionHistory() throws Exception {

      int lInvNoDbId = 4650;
      int lInvNoId = 373504;

      /*
       * This no of days currently looks only for 10000 days; if the day exceeds 10000th day; then
       * either the lday need to be increased or the date field in the XML need to be updated other
       * wise the test case will fail
       */
      int lDays = 10000;
      DataSet lDataSet = this.execute( new InventoryKey( lInvNoDbId, lInvNoId ), lDays );

      MxAssert.assertEquals( "Number of retrieved rows", 7, lDataSet.getRowCount() );
      lDataSet.next();

      // user update status
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:8" ), "27-Nov-2009 03:31:12",
            "CAUTION", new HumanResourceKey( "4650:6000160" ), "ALLOW", "By User", "2.0",
            "rengasamy, sathish", null, null, null );

      // oil uptake task
      lDataSet.next();
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:6" ), "26-Nov-2009 03:31:12",
            "NORMAL", new HumanResourceKey( "4650:6000160" ), "THRESHOLD", null, "2.5", null,
            "B2(Thrust B2 Cycles)", "1", "T40S00017VA" );
      lDataSet.next();
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:6" ), "26-Nov-2009 03:31:12",
            "NORMAL", new HumanResourceKey( "4650:6000160" ), "THRESHOLD", null, "2.5", null,
            "C1(Thrust C1 Cycles)", "1", "T40S00017VA" );
      lDataSet.next();
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:6" ), "26-Nov-2009 03:31:12",
            "NORMAL", new HumanResourceKey( "4650:6000160" ), "THRESHOLD", null, "2.5", null,
            "EOT(Engine Operating Time)", "4", "T40S00017VA" );
      lDataSet.next();
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:6" ), "26-Nov-2009 03:31:12",
            "NORMAL", new HumanResourceKey( "4650:6000160" ), "THRESHOLD", null, "2.5", null,
            "TCSN(Total Engine Cycles)", "7360", "T40S00017VA" );
      lDataSet.next();
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:6" ), "26-Nov-2009 03:31:12",
            "NORMAL", new HumanResourceKey( "4650:6000160" ), "THRESHOLD", null, "2.5", null,
            "B1(Thrust B1 Cycles)", "1", "T40S00017VA" );
      lDataSet.next();
      testRow( lDataSet, new InvOilStatusLogKey( "4650:373504:6" ), "26-Nov-2009 03:31:12",
            "NORMAL", new HumanResourceKey( "4650:6000160" ), "THRESHOLD", null, "2.5", null,
            "ECYC(Equivalent Cycles)", "0.00012324", "T40S00017VA" );
   }


   /**
    * This method executes the query in OilConsumptionHistory.qrx
    *
    * @param aInventoryKey
    *           The inventory key.
    * @param aDays
    *           The data type key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( InventoryKey aInventoryKey, int aDays ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInventoryKey.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInventoryKey.getId() );
      lDataSetArgument.add( "aDayCount", aDays );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Assert the Row Coloumns.
    *
    * @param aDataSet
    *           the DataSet
    * @param aInvOilStatusLogKey
    *           the Inventorykey
    * @param aDate
    *           the date
    * @param aOilStatusCd
    *           the oil status cd
    * @param aHumanResourceKey
    *           the human resource key
    * @param aReason
    *           the reason
    * @param aNote
    *           the note
    * @param aConsumptionRate
    *           the consumption rate
    * @param aName
    *           the name
    * @param aUsageParm
    *           the usage parm
    * @param aTsnQt
    *           the tnsQt
    * @param aBarcode
    *           the barcode
    */
   private void testRow( DataSet aDataSet, InvOilStatusLogKey aInvOilStatusLogKey, String aDate,
         String aOilStatusCd, HumanResourceKey aHumanResourceKey, String aReason, String aNote,
         String aConsumptionRate, String aName, String aUsageParm, String aTsnQt,
         String aBarcode ) {
      MxAssert.assertEquals( "inv_oil_status_log_key", aInvOilStatusLogKey,
            aDataSet.getString( "inv_oil_status_log_key" ) );
      MxAssert.assertEquals( "status_change_date", aDate,
            aDataSet.getString( "status_change_date" ) );
      MxAssert.assertEquals( "oil_status_cd", aOilStatusCd, aDataSet.getString( "oil_status_cd" ) );
      MxAssert.assertEquals( "human_resource_key", aHumanResourceKey,
            aDataSet.getString( "human_resource_key" ) );
      MxAssert.assertEquals( "reason", aReason, aDataSet.getString( "reason" ) );
      MxAssert.assertEquals( "note", aNote, aDataSet.getString( "note" ) );
      MxAssert.assertEquals( "consumption_rate", aConsumptionRate,
            aDataSet.getDouble( "consumption_rate" ) );

      MxAssert.assertEquals( "name", aName, aDataSet.getString( "name" ) );
      MxAssert.assertEquals( "usage parm", aUsageParm, aDataSet.getString( "usage_parm" ) );
      MxAssert.assertEquals( "tsnQt", aTsnQt, aDataSet.getString( "tsn_qt" ) );
      MxAssert.assertEquals( "barcode", aBarcode, aDataSet.getString( "barcode" ) );
   }
}
