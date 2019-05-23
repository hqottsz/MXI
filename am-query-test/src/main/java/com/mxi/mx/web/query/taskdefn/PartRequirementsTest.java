
package com.mxi.mx.web.query.taskdefn;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.TaskPartListKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Query test for the Part Requirements Query
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartRequirementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testPartRequirements() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", 8000000 );
      lArgs.add( "aTaskId", 21598 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      lDs.next();
      assertRow( lDs, new TaskPartListKey( "8000000:21598:1996" ), "1.0", false, false, false, null,
            new PartGroupKey( "8000001:1335" ), "636 (roller bearing)", null, null, null, null,
            null, null, null, null, null, null, true, new RefReqActionKey( "0:NOREQ" ), false );

      lDs.next();
      assertRow( lDs, new TaskPartListKey( "8000000:21598:2072" ), "1.0", false, false, false, null,
            new PartGroupKey( "8000001:1336" ), "637 (roller bearing)", null, null, null, null,
            null, null, null, null, null, null, false, new RefReqActionKey( "0:NOREQ" ), true );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), PartRequirementsTest.class,
            PartRequirementsTestData.getDataFile() );
   }


   /**
    * assert the data set
    *
    * @param aDs
    *           the data set
    * @param aTaskPartListKey
    *           Task Part List Key
    * @param aReqQt
    *           Req Qt
    * @param aInstallBool
    *           Install Bool
    * @param aRemoveBool
    *           Remove Bool
    * @param aRequestBool
    *           Request Bool
    * @param aPartProviderTypecd
    *           Part Provider Type Cd
    * @param aPartGroupKey
    *           Bom Part Key
    * @param aBomPart
    *           Bom Part
    * @param aEqpPoscd
    *           Eqp Pos Cd
    * @param aRemoveReasonKey
    *           Remove Reason Key
    * @param aRemoveReason
    *           Remove Reason
    * @param aReqPriorityKey
    *           Req Priority Key
    * @param aReqPriority
    *           Req Priority
    * @param aPartProviderType
    *           Part Provider Type
    * @param aPartKey
    *           Part Key
    * @param aPartNoOem
    *           Part No Oem
    * @param aQtyUnitCd
    *           Qty Unit Cd
    * @param aDecimalPlacesQt
    *           Decimal Places Qt
    * @param aTrackedBool
    *           Tracked Bool
    * @param aReqActionKey
    *           Kit Part Bool
    * @param aKitGroupBool
    *           DOCUMENT ME!
    */
   private void assertRow( DataSet aDs, TaskPartListKey aTaskPartListKey, String aReqQt,
         boolean aInstallBool, boolean aRemoveBool, boolean aRequestBool,
         String aPartProviderTypecd, PartGroupKey aPartGroupKey, String aBomPart, String aEqpPoscd,
         RefRemoveReasonKey aRemoveReasonKey, String aRemoveReason,
         RefReqPriorityKey aReqPriorityKey, String aReqPriority, String aPartProviderType,
         PartNoKey aPartKey, String aPartNoOem, String aQtyUnitCd, String aDecimalPlacesQt,
         boolean aTrackedBool, RefReqActionKey aReqActionKey, boolean aKitGroupBool ) {
      MxAssert.assertEquals( "task_part_list_key", aTaskPartListKey,
            aDs.getString( "task_part_list_key" ) );
      MxAssert.assertEquals( "req_qt", aReqQt, aDs.getString( "req_qt" ) );
      MxAssert.assertEquals( "install_bool", aInstallBool, aDs.getBoolean( "install_bool" ) );
      MxAssert.assertEquals( "remove_bool", aRemoveBool, aDs.getBoolean( "remove_bool" ) );
      MxAssert.assertEquals( "part_provider_type_cd", aPartProviderTypecd,
            aDs.getString( "part_provider_type_cd" ) );
      MxAssert.assertEquals( "bom_part_key", aPartGroupKey, aDs.getString( "bom_part_key" ) );
      MxAssert.assertEquals( "bom_part", aBomPart, aDs.getString( "bom_part" ) );
      MxAssert.assertEquals( "eqp_pos_cd", aEqpPoscd, aDs.getString( "eqp_pos_cd" ) );
      MxAssert.assertEquals( "remove_reason_key", aRemoveReasonKey,
            aDs.getString( "remove_reason_key" ) );
      MxAssert.assertEquals( "remove_reason", aRemoveReason, aDs.getString( "remove_reason" ) );
      MxAssert.assertEquals( "req_priority_key", aReqPriorityKey,
            aDs.getString( "req_priority_key" ) );
      MxAssert.assertEquals( "req_priority", aReqPriority, aDs.getString( "req_priority" ) );
      MxAssert.assertEquals( "part_provider_type", aPartProviderType,
            aDs.getString( "part_provider_type" ) );
      MxAssert.assertEquals( "part_key", aPartKey, aDs.getString( "part_key" ) );
      MxAssert.assertEquals( "part_no_oem", aPartNoOem, aDs.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "qty_unit_cd", aQtyUnitCd, aDs.getString( "qty_unit_cd" ) );
      MxAssert.assertEquals( "decimal_places_qt", aDecimalPlacesQt,
            aDs.getString( "decimal_places_qt" ) );
      MxAssert.assertEquals( "tracked_bool", aTrackedBool, aDs.getBoolean( "tracked_bool" ) );

      MxAssert.assertEquals( "req_action_key", aReqActionKey, aDs.getString( "req_action_key" ) );
   }
}
