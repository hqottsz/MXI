
package com.mxi.mx.web.query.location.stationcapacity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.ToolRequirements
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ToolRequirementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the query
    *
    * <ol>
    * <li>Assign three tasks with different tool requirements to a check</li>
    * <li>Test that each row returns the correct data</li>
    * <li>Test that only the correct data is returned</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      ToolRequirementsData.assignTaskToCheck(
            ToolRequirementsData.Task.Tool.TASK_2_WITH_UNAVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // execute the query
      DataSet lResult = execute( StationCapacityData.Location.YOW, StationCapacityData.DAY_DT_1,
            StationCapacityData.User.HR_1 );

      assertTrue( "Row 1", lResult.next() );

      assertEquals( "Row 1: aircraft key", StationCapacityData.Aircraft.AC_1.toString(),
            lResult.getString( "aircraft_key" ) );
      assertEquals( "Row 1: aircraft name", StationCapacityData.Aircraft.AC_1_DESCRIPTION,
            lResult.getString( "inv_no_sdesc" ) );
      assertEquals( "Row 1: check key",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1.toString(),
            lResult.getString( "check_key" ) );
      assertEquals( "Row 1: check name",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_SDESC,
            lResult.getString( "check_sdesc" ) );
      assertEquals( "Row 1: root task key",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT.toString(),
            lResult.getString( "root_task_key" ) );
      assertEquals( "Row 1: root task name",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT_SDESC,
            lResult.getString( "root_task_sdesc" ) );
      assertEquals( "Row 1: part group key", StationCapacityData.Tool.TOOL_PART_GROUP_1.toString(),
            lResult.getString( "part_group_key" ) );
      assertEquals( "Row 1: part group decription",
            StationCapacityData.Tool.TOOL_PART_GROUP_1_CODE + " ("
                  + StationCapacityData.Tool.TOOL_PART_GROUP_1_NAME + ")",
            lResult.getString( "part_group_sdesc" ) );
      assertEquals( "Row 1: class mode code", "REQ", lResult.getString( "class_mode_cd" ) );
      assertEquals( "Row 1: local availability", "1*1", lResult.getString( "avail_local" ) );
      assertTrue( "Row 1: has authority", lResult.getBoolean( "has_authority" ) );

      assertTrue( "Row 2", lResult.next() );

      assertEquals( "Row 2: aircraft key", StationCapacityData.Aircraft.AC_1.toString(),
            lResult.getString( "aircraft_key" ) );
      assertEquals( "Row 2: aircraft name", StationCapacityData.Aircraft.AC_1_DESCRIPTION,
            lResult.getString( "inv_no_sdesc" ) );
      assertEquals( "Row 2: check key",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1.toString(),
            lResult.getString( "check_key" ) );
      assertEquals( "Row 2: check name",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_SDESC,
            lResult.getString( "check_sdesc" ) );
      assertEquals( "Row 2: root task key",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT.toString(),
            lResult.getString( "root_task_key" ) );
      assertEquals( "Row 2: root task name",
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT_SDESC,
            lResult.getString( "root_task_sdesc" ) );
      assertEquals( "Row 2: part group key", StationCapacityData.Tool.TOOL_PART_GROUP_2.toString(),
            lResult.getString( "part_group_key" ) );
      assertEquals( "Row 2: part group decription",
            StationCapacityData.Tool.TOOL_PART_GROUP_2_CODE + " ("
                  + StationCapacityData.Tool.TOOL_PART_GROUP_2_NAME + ")",
            lResult.getString( "part_group_sdesc" ) );
      assertEquals( "Row 2: class mode code", "REQ", lResult.getString( "class_mode_cd" ) );
      assertEquals( "Row 2: local availability", "0*0", lResult.getString( "avail_local" ) );
      assertTrue( "Row 2: has authority", lResult.getBoolean( "has_authority" ) );

      assertTrue( "Row 3", lResult.next() );

      assertEquals( "Row 3: part group key", ToolRequirementsData.Tool.TOOL_PART_GROUP_3.toString(),
            lResult.getString( "part_group_key" ) );

      assertEquals( "Row 3: part group decription",
            ToolRequirementsData.Tool.TOOL_PART_GROUP_3_CODE + " ("
                  + ToolRequirementsData.Tool.TOOL_PART_GROUP_3_NAME + ")",
            lResult.getString( "part_group_sdesc" ) );

      assertEquals( "Row 3: local availability", "1*2", lResult.getString( "avail_local" ) );

      assertFalse( lResult.next() );

      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1 );

      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1 );

      StationCapacityData.unassignTaskFromCheck(
            ToolRequirementsData.Task.Tool.TASK_2_WITH_UNAVAILABLE_TOOL_ON_DAY_1 );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ToolRequirementsTest.class );
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), ToolRequirementsTest.class,
            new StationCapacityData().getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @param aLocation
    *           location
    * @param aDate
    *           date
    * @param aHumanResource
    *           the hr
    *
    * @return the result
    */
   private DataSet execute( LocationKey aLocation, Date aDate, HumanResourceKey aHumanResource ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, new String[] { "aSupplyLocDbId", "aSupplyLocId" } );
      lArgs.add( "aDate", aDate );
      lArgs.add( aHumanResource, new String[] { "aHrDbId", "aHrId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
