package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.web.query.task.Workscope.qrx. The test data can be found in
 * com/mxi/mx/web/query/task/WorkscopeTest.xml.
 *
 *
 * There exists another test class {@link WorkscopeQueryTest} which uses domain entities/builders to
 * load test data . However, the tests in this class use an XML file. (Note, a test class cannot
 * intermix the test data loading techniques.)
 */
public final class WorkscopeTest {

   /**
    * This is the name of the placeholder in the com.mxi.mx.web.query.task.Workscope.qrx which will
    * be replaced with a dynamically produced where clause.
    */
   private static final String WHERE_WORK_PACKAGE = "WHERE_WORK_PACKAGE";
   private static final TaskKey WORK_PACKAGE_KEY = new TaskKey( 0, 105360 );

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), WorkscopeTest.class );
   }


   /**
    * Tests on with the filters only show IN WORK tasks and required labour skills.
    */
   @Test
   public void testGetFilteredInWorkResult() throws Exception {
      // set up input arguments in query Workscope.qrx, -999999 means null value
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         List<Integer> lZoneDbIdArray = new ArrayList<>();
         List<Integer> aZoneIdArray = new ArrayList<>();
         // create where clause string for filtering on required labour skill
         String[] lLabourSkills = new String[1];
         {
            lLabourSkills[0] = "ENG";
         }
         // create where clause string for filtering on ACTV and IN WORK tasks
         StringBuilder lActiveCheckStr = new StringBuilder();
         {
            lActiveCheckStr.append(
                  "( ( evt_event.hist_bool = 0 AND ( sched_labour.labour_stage_cd IN (\'IN WORK\',\'ACTV\') OR sched_labour.labour_db_id IS NULL ) ) OR EXISTS ( " );
            lActiveCheckStr.append(
                  "SELECT 1 FROM sched_labour WHERE sched_labour.sched_db_id = sched_stask.sched_db_id " );
            lActiveCheckStr.append( "AND sched_labour.sched_id = sched_stask.sched_id AND " );
            lActiveCheckStr.append( "sched_labour.labour_stage_cd = \'IN WORK\' AND " );
            lActiveCheckStr.append( "ref_event_status.user_status_cd = \'COMPLETE\' ) ) " );
         }

         lDataSetArgument.add( "aCheckDbId", WORK_PACKAGE_KEY.getDbId() );
         lDataSetArgument.add( "aCheckId", WORK_PACKAGE_KEY.getId() );
         lDataSetArgument.add( "aIsWorkTypeApplicable", 0 );
         lDataSetArgument.add( "aSubInvDbId", -999999 );
         lDataSetArgument.add( "aSubInvId", -999999 );
         lDataSetArgument.add( "aUnassignString", "UNASSIGN" );
         lDataSetArgument.addIntegerArray( "aZoneDbIdArray", lZoneDbIdArray );
         lDataSetArgument.addIntegerArray( "aZoneIdArray", aZoneIdArray );
         // filter not applied
         lDataSetArgument.add( "aIsZoneFilterApplied", 0 );
         lDataSetArgument.addWhereIn( WHERE_WORK_PACKAGE, "sched_labour.labour_skill_cd",
               lLabourSkills );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE, lActiveCheckStr.toString() );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE, "sched_wo_line.unassign_bool = 0" );
      }
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      while ( lDataSet.next() ) {

         // the 2nd JIC in WorkscopeTest.xml is expected showing
         assertEquals( "IN WORK", lDataSet.getString( "status_cd" ) );
         // task barcode
         assertEquals( "T999993", lDataSet.getString( "barcode_sdesc" ) );
      }
   }


   /**
    * Tests on with the filter only show ACTV and IN WORK tasks.
    */
   @Test
   public void testGetFilteredActvAndInWorkTaskResult() throws Exception {
      // set up input arguments in query Workscope.qrx, -999999 means null value
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         List<Integer> lZoneDbIdArray = new ArrayList<>();
         List<Integer> aZoneIdArray = new ArrayList<>();
         // create where clause string for filtering on ACTV and IN WORK tasks
         StringBuilder lActiveCheckStr = new StringBuilder();
         {
            lActiveCheckStr.append(
                  "( ( evt_event.hist_bool = 0 AND ( sched_labour.labour_stage_cd IN (\'IN WORK\',\'ACTV\') OR sched_labour.labour_db_id IS NULL ) ) OR EXISTS ( " );
            lActiveCheckStr.append(
                  "SELECT 1 FROM sched_labour WHERE sched_labour.sched_db_id = sched_stask.sched_db_id " );
            lActiveCheckStr.append( "AND sched_labour.sched_id = sched_stask.sched_id AND " );
            lActiveCheckStr.append( "sched_labour.labour_stage_cd = \'IN WORK\' AND " );
            lActiveCheckStr.append( "ref_event_status.user_status_cd = \'COMPLETE\' ) ) " );
         }

         lDataSetArgument.add( "aCheckDbId", WORK_PACKAGE_KEY.getDbId() );
         lDataSetArgument.add( "aCheckId", WORK_PACKAGE_KEY.getId() );
         lDataSetArgument.add( "aIsWorkTypeApplicable", 0 );
         lDataSetArgument.add( "aSubInvDbId", -999999 );
         lDataSetArgument.add( "aSubInvId", -999999 );
         lDataSetArgument.add( "aUnassignString", "UNASSIGN" );
         lDataSetArgument.addIntegerArray( "aZoneDbIdArray", lZoneDbIdArray );
         lDataSetArgument.addIntegerArray( "aZoneIdArray", aZoneIdArray );
         // filter not applied
         lDataSetArgument.add( "aIsZoneFilterApplied", 0 );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE, lActiveCheckStr.toString() );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE, "sched_wo_line.unassign_bool = 0" );
      }
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      while ( lDataSet.next() ) {

         int lLineNo = lDataSet.getInt( "line_no" );
         if ( lLineNo == 2 ) {
            assertEquals( "IN WORK", lDataSet.getString( "status_cd" ) );
            assertEquals( "T999993", lDataSet.getString( "barcode_sdesc" ) );

         } else if ( lLineNo == 1 ) {
            assertEquals( "ACTV", lDataSet.getString( "status_cd" ) );
            assertEquals( "T999992", lDataSet.getString( "barcode_sdesc" ) );

         }
      }
   }


   @Test
   public void testFilterByZoneNoZonesSelected() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         List<Integer> lZoneDbIdArray = new ArrayList<>();
         List<Integer> aZoneIdArray = new ArrayList<>();
         String lWhereClauseForcingAssignedWorkscopeItemsOnly = "sched_wo_line.unassign_bool = 0";

         lDataSetArgument.add( "aCheckDbId", WORK_PACKAGE_KEY.getDbId() );
         lDataSetArgument.add( "aCheckId", WORK_PACKAGE_KEY.getId() );
         lDataSetArgument.add( "aIsWorkTypeApplicable", 0 );
         lDataSetArgument.add( "aSubInvDbId", -999999 );
         lDataSetArgument.add( "aSubInvId", -999999 );
         lDataSetArgument.add( "aUnassignString", "UNASSIGN" );
         lDataSetArgument.addIntegerArray( "aZoneDbIdArray", lZoneDbIdArray );
         lDataSetArgument.addIntegerArray( "aZoneIdArray", aZoneIdArray );
         // filter not applied
         lDataSetArgument.add( "aIsZoneFilterApplied", 0 );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE,
               lWhereClauseForcingAssignedWorkscopeItemsOnly );
      }
      DataSet lFilteredWorkscopeDataSet =
            QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
      int lWorkscopeItemCount = lFilteredWorkscopeDataSet.getTotalRowCount();

      assertEquals( 2, lWorkscopeItemCount );
   }


   @Test
   public void testFilterByZoneSpecificZoneSelected() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         List<Integer> lZoneDbIdArray = new ArrayList<>();
         {
            lZoneDbIdArray.add( 0 );
         }
         List<Integer> aZoneIdArray = new ArrayList<>();
         {
            aZoneIdArray.add( 1 );
         }
         String lWhereClauseForcingAssignedWorkscopeItemsOnly = "sched_wo_line.unassign_bool = 0";

         lDataSetArgument.add( "aCheckDbId", WORK_PACKAGE_KEY.getDbId() );
         lDataSetArgument.add( "aCheckId", WORK_PACKAGE_KEY.getId() );
         lDataSetArgument.add( "aIsWorkTypeApplicable", 0 );
         lDataSetArgument.add( "aSubInvDbId", -999999 );
         lDataSetArgument.add( "aSubInvId", -999999 );
         lDataSetArgument.add( "aUnassignString", "UNASSIGN" );
         lDataSetArgument.addIntegerArray( "aZoneDbIdArray", lZoneDbIdArray );
         lDataSetArgument.addIntegerArray( "aZoneIdArray", aZoneIdArray );
         // filter applied
         lDataSetArgument.add( "aIsZoneFilterApplied", 1 );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE,
               lWhereClauseForcingAssignedWorkscopeItemsOnly );
      }
      DataSet lFilteredWorkscopeDataSet =
            QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
      int lWorkscopeItemCount = lFilteredWorkscopeDataSet.getTotalRowCount();

      assertEquals( 1, lWorkscopeItemCount );
      lFilteredWorkscopeDataSet.next();

      int lWoLineId = lFilteredWorkscopeDataSet.getInt( "line_no" );

      assertEquals( 1, lWoLineId );
   }


   /**
    * Zone 3 is not associated with any task in the workscope. Filtering by Zone 3 should yield no
    * workscope line items.
    */
   @Test
   public void testFilterByUnrelatedZone() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         List<Integer> lZoneDbIdArray = new ArrayList<>();
         {
            lZoneDbIdArray.add( 0 );
         }
         List<Integer> aZoneIdArray = new ArrayList<>();
         {
            aZoneIdArray.add( 3 );
         }
         String lWhereClauseForcingAssignedWorkscopeItemsOnly = "sched_wo_line.unassign_bool = 0";

         lDataSetArgument.add( "aCheckDbId", WORK_PACKAGE_KEY.getDbId() );
         lDataSetArgument.add( "aCheckId", WORK_PACKAGE_KEY.getId() );
         lDataSetArgument.add( "aIsWorkTypeApplicable", 0 );
         lDataSetArgument.add( "aSubInvDbId", -999999 );
         lDataSetArgument.add( "aSubInvId", -999999 );
         lDataSetArgument.add( "aUnassignString", "UNASSIGN" );
         lDataSetArgument.addIntegerArray( "aZoneDbIdArray", lZoneDbIdArray );
         lDataSetArgument.addIntegerArray( "aZoneIdArray", aZoneIdArray );
         // filter applied
         lDataSetArgument.add( "aIsZoneFilterApplied", 1 );
         lDataSetArgument.addWhere( WHERE_WORK_PACKAGE,
               lWhereClauseForcingAssignedWorkscopeItemsOnly );
      }
      DataSet lFilteredWorkscopeDataSet =
            QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
      int lWorkscopeItemCount = lFilteredWorkscopeDataSet.getTotalRowCount();

      assertEquals( 0, lWorkscopeItemCount );
   }
}
