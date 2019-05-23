
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.ZoneKey;


/**
 * This class tests the query com.mxi.mx.web.query.task.TaskListForShiftPlanning.qrx
 *
 * @author twu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskListForShiftPlanningTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private ZoneKey[] iZonesList;
   private PanelKey iPanelKey;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            TaskListForShiftPlanningTest.class );
   }


   private static final TaskKey WORK_PACKAGE_KEY = new TaskKey( 0, 105360 );


   /**
    * Tests on with the filters on one zone selected.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testFilterOnSingleZoneSearch() throws Exception {

      // attach one zone to zone list
      iZonesList = new ZoneKey[1];
      iZonesList[0] = new ZoneKey( 0, 1 );
      DataSet lDataSet = execute( WORK_PACKAGE_KEY, iZonesList );

      // passed in one zone, expect 1 result
      assertEquals( 1, lDataSet.getTotalRowCount() );

      while ( lDataSet.next() ) {

         // expect the 1st task event's event_sdesc and the 1st zone's zone_cd
         assertEquals( "task1", lDataSet.getString( "event_sdesc" ) );
         assertEquals( "100", lDataSet.getString( "zones" ) );
      }
   }


   /**
    * Tests on with the filter on multiple zone selected.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testFilterOnMultipleZonesSearch() throws Exception {

      // attach 2 zones to zone list
      iZonesList = new ZoneKey[2];
      iZonesList[0] = new ZoneKey( 0, 1 );
      iZonesList[1] = new ZoneKey( 0, 2 );
      DataSet lDataSet = execute( WORK_PACKAGE_KEY, iZonesList );

      // passed in 2 zones, expect total 2 results
      assertEquals( 2, lDataSet.getTotalRowCount() );

      while ( lDataSet.next() ) {
         int lLineOrd = lDataSet.getInt( "sched_line_ord" );

         // based on wo_line_id in sched_wo_line to check corresponded results for zone and task
         if ( lLineOrd == 1 ) {
            assertEquals( "task1", lDataSet.getString( "event_sdesc" ) );
            assertEquals( "100", lDataSet.getString( "zones" ) );
         } else if ( lLineOrd == 2 ) {
            assertEquals( "task2", lDataSet.getString( "event_sdesc" ) );
            assertEquals( "200", lDataSet.getString( "zones" ) );
         }
      }
   }


   /**
    * Zone 3 is not associated with any task in the workscope. Filtering by Zone 3 should yield no
    * plan shift items.
    */
   @Test
   public void testFilterByUnrelatedZone() throws Exception {

      // attach 1 unrelated zone to zone list
      iZonesList = new ZoneKey[1];
      iZonesList[0] = new ZoneKey( 0, 3 );
      DataSet lDataSet = execute( WORK_PACKAGE_KEY, iZonesList );

      // passed in 1 unrelated zone, expect 0 result
      assertEquals( 0, lDataSet.getTotalRowCount() );
   }


   /**
    * Do not filtering on any zone. It should yield 2 plan shift items from 2 existing tasks.
    */
   @Test
   public void testFilterByNoZonesSelected() {

      // attach null zone to zone list
      iZonesList = null;
      DataSet lDataSet = execute( WORK_PACKAGE_KEY, iZonesList );

      // not passed any zone for filtering, expect 2 results from 2 existing tasks
      assertEquals( 2, lDataSet.getTotalRowCount() );
   }


   /**
    * This method executes the query in TaskListforShiftPlanning.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @param aZonesList
    *           the Array object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey, ZoneKey[] aZonesList ) {

      List<Integer> lZoneDbIds = new ArrayList<Integer>();
      List<Integer> lZoneIds = new ArrayList<Integer>();
      boolean lZoneFilterApplied = false;
      if ( aZonesList != null && aZonesList.length > 0 ) {
         for ( ZoneKey lZoneKey : iZonesList ) {
            lZoneDbIds.add( lZoneKey.getDbId() );
            lZoneIds.add( lZoneKey.getId() );
         }
         lZoneFilterApplied = true;
      }

      // set up input arguments in query TaskListforShiftPlanning.qrx
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aCheckDbId", aTaskKey.getDbId() );
      lArgs.add( "aCheckId", aTaskKey.getId() );
      lArgs.add( "aTaskName", ( String ) null );
      lArgs.add( "aStartDate", ( Date ) null );
      lArgs.add( "aTaskClass", ( String ) null );
      lArgs.add( "aMaterialReqStatusClass", ( String ) null );
      lArgs.add( "aOnlyUnassigned", false );
      lArgs.add( "aShowHistoric", false );
      lArgs.addIntegerArray( "aZoneDbIdArray", lZoneDbIds );
      lArgs.addIntegerArray( "aZoneIdArray", lZoneIds );
      lArgs.add( "aIsZoneFilterApplied", lZoneFilterApplied );
      lArgs.add( iPanelKey, new String[] { "aPanelDbId", "aPanelId" } );
      lArgs.add( "aWorkArea", ( String ) null );
      lArgs.add( "aPhaseId", ( String ) null );
      lArgs.add( "aLabourSkill", ( String ) null );

      // create where clause for line items that linked to work package
      String lWhereClause = "root_task_sched_wo_line.wo_sched_db_id = " + aTaskKey.getDbId()
            + " AND root_task_sched_wo_line.wo_sched_id = " + aTaskKey.getId();
      lArgs.addWhere( "WHERE_CLAUSE_CHECK_KEY", lWhereClause );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
