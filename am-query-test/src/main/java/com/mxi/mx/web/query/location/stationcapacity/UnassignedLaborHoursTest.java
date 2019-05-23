
package com.mxi.mx.web.query.location.stationcapacity;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.UnassignedLaborHours
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UnassignedLaborHoursTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** DOCUMENT ME! */
   public static final InventoryKey TASK_INVENTORY = new InventoryKey( 4650, 100 );

   /** DOCUMENT ME! */
   public static final TaskKey TASK = new TaskKey( 4650, 100 );


   /**
    * Tests the query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 1 );

      // There should be 3 rows
      MxAssert.assertEquals( "Number of retrieved rows", 3, lDs.getRowCount() );

      // sort the data
      lDs.setSort( "labour_skill_cd", true );

      // test the row
      lDs.next();
      testRow( lDs, TASK_INVENTORY, TASK, RefLabourSkillKey.ENG, 3.0 );

      // test the row
      lDs.next();
      testRow( lDs, TASK_INVENTORY, TASK, RefLabourSkillKey.LBR, 2.0 );

      // test the row
      lDs.next();
      testRow( lDs, TASK_INVENTORY, TASK, RefLabourSkillKey.PILOT, 1.0 );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), UnassignedLaborHoursTest.class,
            new StationCapacityData().getDataFile() );
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UnassignedLaborHoursTest.class );
   }


   /**
    * Execute the query.
    *
    * @param aAircraft
    *           aircraft
    * @param aDayCount
    *           day count
    *
    * @return the result
    */
   private DataSet execute( AircraftKey aAircraft, int aDayCount ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, new String[] { "aInvDbId", "aInvId" } );
      lArgs.add( "aDayCount", aDayCount );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aInventory
    *           the inventory
    * @param aTask
    *           the task
    * @param aLabourSkill
    *           the labour skill
    * @param aSchedHr
    *           the sched hr
    */
   private void testRow( DataSet aDs, InventoryKey aInventory, TaskKey aTask,
         RefLabourSkillKey aLabourSkill, Double aSchedHr ) {
      MxAssert.assertEquals( "inv_no_db_id", aInventory.getDbId(), aDs.getInt( "inv_no_db_id" ) );
      MxAssert.assertEquals( "inv_no_id", aInventory.getId(), aDs.getInt( "inv_no_id" ) );
      MxAssert.assertEquals( "event_db_id", aTask.getDbId(), aDs.getInt( "event_db_id" ) );
      MxAssert.assertEquals( "event_id", aTask.getId(), aDs.getInt( "event_id" ) );
      MxAssert.assertEquals( "labour_skill_db_id", aLabourSkill.getDbId(),
            aDs.getInteger( "labour_skill_db_id" ) );
      MxAssert.assertEquals( "labour_skill_cd", aLabourSkill.getCd(),
            aDs.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "sched_hr", aSchedHr, aDs.getDoubleObj( "sched_hr" ) );
   }
}
