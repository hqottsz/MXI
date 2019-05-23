
package com.mxi.mx.web.query.inventory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.qrx.QuerySetKeyColumnPredicate;
import com.mxi.am.db.qrx.QuerySetRowSelector;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.inventory.SubAssembliesByInventoryTest.qrx
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SubAssembliesByInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SubAssembliesByInventoryTest.class );
   }


   /**
    * Tests the retrieval of sub assemblies by inventory.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testSubAssembliesByInventory() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aInvNoDbId", 4650 );
      lArgs.add( "aInvNoId", 370873 );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      Assert.assertEquals( "Number of retrieved rows", 3, lResult.getRowCount() );

      // Verify the contents of the row returned.
      InventoryKey lFirstRowKey = new InventoryKey( 4650, 373445 );
      QuerySetRowSelector.select( lResult,
            new QuerySetKeyColumnPredicate( lFirstRowKey, "inv_key" ) );
      testRow( lResult, "8000002", "APU G20", "8000002:APU G20", "0", "APU", "4650", "373445",
            "4650:373445", "49-00-APU", "G20 757/767 APU (PN: 3800298-1-5, SN: XXX)" );

      InventoryKey lSecondRowKey = new InventoryKey( 4650, 373504 );
      QuerySetRowSelector.select( lResult,
            new QuerySetKeyColumnPredicate( lSecondRowKey, "inv_key" ) );
      testRow( lResult, "5001", "CFM56", "5001:CFM56", "0", "ENG", "4650", "373504", "4650:373504",
            "CFM56-3C (1.LT)", "(1.LT) CFM56-3C (PN: CFM56-3C, SN: XXX)" );

      InventoryKey lThirdRowKey = new InventoryKey( 4650, 394972 );
      QuerySetRowSelector.select( lResult,
            new QuerySetKeyColumnPredicate( lThirdRowKey, "inv_key" ) );
      testRow( lResult, "5001", "CFM56", "5001:CFM56", "0", "ENG", "4650", "394972", "4650:394972",
            "CFM56-3C (2.RT)", "(2.RT) CFM56-3C (PN: CFM56-3C, SN: XXX)" );
   }


   /**
    * Assert the Row Coloumns.
    *
    * @param aDataSet
    *           the DataSet
    * @param aOrigAssmblDbId
    *           the OrigAssmblDbId
    * @param aOrigAssmblCd
    *           the OrigAssmblCd
    * @param aAssmblKey
    *           the AssmblKey
    * @param aAssmblClassDbId
    *           the AssmblClassDbId
    * @param aAssmblClassCd
    *           the AssmblClassCd
    * @param aInvNoDbId
    *           the InvNoDbId
    * @param aInvNoId
    *           the InvNoId
    * @param aInvKey
    *           the InvKey
    * @param aConfigPosSdes
    *           the ConfigPosSdes
    * @param aInvSdesc
    *           the InvSdesc
    */
   private void testRow( DataSet aDataSet, String aOrigAssmblDbId, String aOrigAssmblCd,
         String aAssmblKey, String aAssmblClassDbId, String aAssmblClassCd, String aInvNoDbId,
         String aInvNoId, String aInvKey, String aConfigPosSdes, String aInvSdesc ) {
      MxAssert.assertEquals( "orig_assmbl_db_id", aOrigAssmblDbId,
            aDataSet.getString( "orig_assmbl_db_id" ) );
      MxAssert.assertEquals( "orig_assmbl_cd", aOrigAssmblCd,
            aDataSet.getString( "orig_assmbl_cd" ) );
      MxAssert.assertEquals( "asmb_key", aAssmblKey, aDataSet.getString( "asmb_key" ) );
      MxAssert.assertEquals( "assmbl_class_db_id", aAssmblClassDbId,
            aDataSet.getString( "assmbl_class_db_id" ) );
      MxAssert.assertEquals( "assmbl_class_cd", aAssmblClassCd,
            aDataSet.getString( "assmbl_class_cd" ) );
      MxAssert.assertEquals( "inv_no_db_id", aInvNoDbId, aDataSet.getString( "inv_no_db_id" ) );
      MxAssert.assertEquals( "inv_no_id", aInvNoId, aDataSet.getString( "inv_no_id" ) );
      MxAssert.assertEquals( "inv_key", aInvKey, aDataSet.getString( "inv_key" ) );
      MxAssert.assertEquals( "config_pos_sdesc", aConfigPosSdes,
            aDataSet.getString( "config_pos_sdesc" ) );
      MxAssert.assertEquals( "inv_no_sdesc", aInvSdesc, aDataSet.getString( "inv_no_sdesc" ) );
   }
}
