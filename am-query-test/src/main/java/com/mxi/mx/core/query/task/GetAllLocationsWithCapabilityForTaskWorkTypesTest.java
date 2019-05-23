
package com.mxi.mx.core.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InvLocWTCapabilityKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.inv.InvLocWTCapabilityTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sched.SchedWorkTypeTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query
 * com.mxi.mx.web.query.task.GetAllLocationsWithCapabilityForTaskWorkTypes.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAllLocationsWithCapabilityForTaskWorkTypesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final TaskKey SCHED_STACK = new TaskKey( 1, 1 );

   public static final PartNoKey PART_BATCH = new PartNoKey( 2, 2 );

   public static final InventoryKey INVENTORY = new InventoryKey( 3, 3 );

   public static final EventKey EVENT_KEY = new EventKey( 1, 1 );

   public static final EventKey H_EVENT_KEY = new EventKey( 4, 4 );

   public static final EventKey EVENT_LOC = new EventKey( 4, 4 );

   public static final LocationKey LOCATION = new LocationKey( 5, 5 );

   public static final LocationKey SUPPLY_LOCATION = new LocationKey( 6, 6 );

   public static final LocationKey TASK_LOCATION = new LocationKey( 7, 7 );

   public static final RefWorkTypeKey WORK_TYPE = new RefWorkTypeKey( 10, "FUEL" );

   public static final InvLocWTCapabilityKey INV_LOC_WT_CAPABILITY =
         new InvLocWTCapabilityKey( TASK_LOCATION, WORK_TYPE );

   public static final RefLocTypeKey LOC_TYPE_KEY = new RefLocTypeKey( 0, "SHOP" );


   /**
    * Tests the retrieval of all allocations with capability for task work types.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAllAllocations() throws Exception {

      DataSet lDataSet = this.execute( new TaskKey( 4650, 136984 ) );
      assertTrue( lDataSet.getRowCount() > 0 );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "loc_cd", "YVR/LINE", lDataSet.getString( "loc_cd" ) );
         MxAssert.assertEquals( "loc_name", "Line", lDataSet.getString( "loc_name" ) );
      }
   }


   /**
    * Tests that the query returns WorkScope location, ensure that the Location's capability for
    * "Any Part" assigned to workscope correctly.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testWorkscopeLocation() throws Exception {
      DataSet lDataSet = this.execute( SCHED_STACK );

      lDataSet.first();

      assertEquals( "ABQ/AK", lDataSet.getString( "loc_cd" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            if an error occurs
    */
   @BeforeClass
   public static void loadData() throws Exception {

      SchedStaskTable lSchedStackTable = SchedStaskTable.create( SCHED_STACK );
      lSchedStackTable.setHTaskKey( SCHED_STACK );
      lSchedStackTable.setMainInventory( INVENTORY );
      lSchedStackTable.insert();

      EvtEventTable lEvtEventTable = EvtEventTable.create( EVENT_KEY );
      lEvtEventTable.setHEvent( H_EVENT_KEY );
      lEvtEventTable.insert();

      EvtLocTable lEvtLocTable = EvtLocTable.create( EVENT_LOC );
      lEvtLocTable.setLocation( LOCATION );
      lEvtLocTable.insert();

      InvLocTable lWPInvLocTable = InvLocTable.create( LOCATION );
      lWPInvLocTable.setSupplyLoc( SUPPLY_LOCATION );
      lWPInvLocTable.insert();

      InvInvTable lInvInvTable = InvInvTable.create( INVENTORY );
      lInvInvTable.insert();

      SchedWorkTypeTable lSchedWorkTypeTable = SchedWorkTypeTable.create( SCHED_STACK, WORK_TYPE );
      lSchedWorkTypeTable.insert();

      InvLocTable lTaskLocation = InvLocTable.create( TASK_LOCATION );
      lTaskLocation.setSupplyLoc( SUPPLY_LOCATION );
      lTaskLocation.setLocCd( "ABQ/AK" );
      lTaskLocation.setLocType( LOC_TYPE_KEY );
      lTaskLocation.insert();

      InvLocWTCapabilityTable lInvLocWTCapabilityTable =
            InvLocWTCapabilityTable.create( INV_LOC_WT_CAPABILITY );
      lInvLocWTCapabilityTable.insert();

      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAllLocationsWithCapabilityForTaskWorkTypesTest.class );
   }


   /**
    * This method executes the query in GetAllLocationsWithCapabilityForTaskWorkTypes.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      String[] lLocationCodes = { "LINE", "TRACK", "SHOP" };

      // Serial Number
      lDataSetArgument.addWhereIn( "task_location.loc_type_cd", lLocationCodes );
      lDataSetArgument.add( "aSchedDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aSchedId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
