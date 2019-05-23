package com.mxi.mx.core.query.bsync;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.creation.CreationService;
import com.mxi.mx.core.services.stask.status.StatusService;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.db.view.baselinetask.BaselineTestCase;


/**
 * Tests the hasACTVDependentTask functionality.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class HasACTVDependentTaskTest extends BaselineTestCase {

   private MaintenanceProgram iMaintPrgm = new MaintenanceProgram();

   private TaskDefinition iReq1 = new TaskDefinition( COMPONENT_CONFIG_SLOT );
   private TaskDefinition iReq2 = new TaskDefinition( COMPONENT_CONFIG_SLOT );
   private TaskDefinition iReq3 = new TaskDefinition( COMPONENT_CONFIG_SLOT );
   private TaskDefinition iReq4 = new TaskDefinition( COMPONENT_CONFIG_SLOT );
   private TaskDefinition iReq5 = new TaskDefinition( COMPONENT_CONFIG_SLOT );

   private HumanResourceKey iHr = new HumanResourceKey( 0, 3 );

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Matches false
    *
    * @return the matcher
    */
   public Matcher<Boolean> doesNotExist() {
      return is( equalTo( false ) );
   }


   /**
    * Matches true
    *
    * @return the matcher
    */
   public Matcher<Boolean> exists() {
      return is( equalTo( true ) );
   }


   /**
    * Initializes a task
    *
    * @param aTaskDefinition
    *           the task to initialize
    *
    * @return The created task
    *
    * @throws Exception
    *            an error occurred
    */
   public TaskKey initializeTask( TaskDefinition aTaskDefinition ) throws Exception {
      return new CreationService().createTaskFromDefinition( null, INV_ACFT_CARRIER,
            aTaskDefinition.getLatestTaskRevision(), null, false, true, false, new Date() );
   }


   /**
    * Terminates a task
    *
    * @param aTaskDefinition
    *           the task to terminate
    *
    *
    */
   public void terminateTask( TaskKey aTaskDefinition ) throws Exception {
      new StatusService( aTaskDefinition ).terminate( iHr, null, null, null,
            new String[] { RefEventStatusKey.ACTV.getCd() }, false, false );
   }


   /**
    * Makes the task definition on-condition
    *
    * @param aTaskRevision
    *           the task revision
    */
   public void makeOnCondition( TaskTaskKey aTaskRevision ) {
      TaskTaskTable lTable = TaskTaskTable.findByPrimaryKey( aTaskRevision );
      lTable.setOnConditionBool( true );
      lTable.update();
   }


   /**
    * Ensure that the dependency check is recursive
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testChainDependencies() throws Exception {
      iReq3.revise();
      iReq3.activate();

      iReq2.revise();
      makeDependentOn( iReq2, iReq3 );
      iReq2.activate();

      iReq1.revise();
      makeNonUnique( iReq1 );
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq3 ), exists() );
   }


   /**
    * Ensure that the dependency check is recursive (post create)
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPostChainDependencies() throws Exception {

      iReq1.revise();
      iReq1.activate();

      iReq2.revise();
      makePostDependentOn( iReq1, iReq2 );
      iReq2.activate();

      iReq3.revise();
      makePostDependentOn( iReq2, iReq3 );
      iReq3.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq3 ), exists() );
   }


   /**
    * Test a scenario where multiple CRT links point into the same task. If REQ1 is initialized,
    * then REQ5 should NOT be blocked by following the dependency tree from REQ5 to REQ3. Because
    * both REQ2 and REQ4 point inbound into REQ3 There should be no dependency relation between REQ1
    * and REQ5 as the chain will be broken at REQ3
    * (REQ1)------>(REQ2)------>(REQ3)<------(REQ4)<----(REQ5)
    *
    * As of
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testMultipleChainDependencies() throws Exception {

      // REQ4
      iReq4.revise();
      iReq4.activate();

      // REQ5 -> CRT -> REQ4
      iReq5.revise();
      makeDependentOn( iReq5, iReq4 );
      iReq5.activate();

      // REQ3
      iReq3.revise();
      iReq3.activate();

      // REQ4 -> CRT -> REQ3
      iReq4.revise();
      makeDependentOn( iReq4, iReq3 );
      iReq4.activate();

      // REQ2 -> CRT -> REQ3
      iReq2.revise();
      makeDependentOn( iReq2, iReq3 );
      iReq2.activate();

      // REQ1 -> CRT -> REQ2
      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      // Create instance of REQ1
      initializeTask( iReq1 );

      // Check that REQ5 can be created
      assertThat( dependenciesFor( iReq5 ), doesNotExist() );
   }


   /**
    * Test a scenario where multiple CRT links point into the same task. If REQ5 is initialized,
    * then REQ5 should be blocked by following the dependency tree from REQ4 to REQ3, then changing
    * directions and continuing to follow REQ3 in the other direction back to REQ1
    * (REQ1)------>(REQ2)------>(REQ3)<------(REQ4)<----(REQ5)
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testMultipleChainDependenciesBackwards() throws Exception {

      // REQ4
      iReq4.revise();
      iReq4.activate();

      // REQ5 -> CRT -> REQ4
      iReq5.revise();
      makeDependentOn( iReq5, iReq4 );
      iReq5.activate();

      // REQ3
      iReq3.revise();
      iReq3.activate();

      // REQ4 -> CRT -> REQ3
      iReq4.revise();
      makeDependentOn( iReq4, iReq3 );
      iReq4.activate();

      // REQ2 -> CRT -> REQ3
      iReq2.revise();
      makeDependentOn( iReq2, iReq3 );
      iReq2.activate();

      // REQ1 -> CRT -> REQ2
      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      // Create instance of REQ5
      initializeTask( iReq5 );

      // Check that REQ1 can be created
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /**
    * Test a scenario where multiple POSTCRT links point into the same task. If REQ1 is initialized,
    * then REQ5 should be blocked by following the dependency tree from REQ5 to REQ3, then changing
    * directions and continuing to follow REQ3 in the other direction back to REQ1
    * (REQ1)------>(REQ2)------>(REQ3)<------(REQ4)<----(REQ5)
    * ....<--POSTCRT-|<---POSTCRT-|-POSTCRT---->|-POSTCRT-->
    *
    * @throws Exception
    *            an error occurred
    */
   @Ignore
   @Test
   public void testPostMultipleChainDependencies() throws Exception {

      // must be constructed from the tail ends inward
      iReq5.revise();
      iReq5.activate();

      iReq1.revise();
      iReq1.activate();

      iReq2.revise();
      // REQ1 Creates REQ2 (link formed from REQ2)
      makePostDependentOn( iReq1, iReq2 );
      iReq2.activate();

      iReq4.revise();
      // REQ5 Creates REQ4 (link formed from REQ4)
      makePostDependentOn( iReq5, iReq4 );
      iReq4.activate();

      iReq3.revise();
      // REQ2 Creates REQ3 (link formed from 3)
      makePostDependentOn( iReq2, iReq3, 2 );
      // REQ4 Creates REQ3 (link formed from 3
      makePostDependentOn( iReq4, iReq3, 1 );
      iReq3.activate();

      // Create instance of REQ5
      initializeTask( iReq1 );

      // Check that REQ1 can not be created
      assertThat( dependenciesFor( iReq5 ), exists() );
   }


   /**
    * Test a scenario where multiple POSTCRT links point into the same task. If REQ1 is initialized,
    * then REQ5 should be blocked by following the dependency tree from REQ1 to REQ3, then changing
    * directions and continuing to follow REQ3 in the other direction back to REQ5
    * (REQ1)------>(REQ2)------>(REQ3)<------(REQ4)<----(REQ5)
    * ....<--POSTCRT-|<---POSTCRT-|-POSTCRT---->|-POSTCRT-->
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Ignore
   @Test
   public void testPostMultipleChainDependenciesBackwards() throws Exception {

      // must be constructed from the tail ends inward
      iReq5.revise();
      iReq5.activate();

      iReq1.revise();
      iReq1.activate();

      iReq2.revise();
      // REQ1 Creates REQ2 (link formed from REQ2)
      makePostDependentOn( iReq1, iReq2 );
      iReq2.activate();

      iReq4.revise();
      // REQ5 Creates REQ4 (link formed from REQ4)
      makePostDependentOn( iReq5, iReq4 );
      iReq4.activate();

      iReq3.revise();
      // REQ2 Creates REQ3 (link formed from 3)
      makePostDependentOn( iReq2, iReq3, 2 );
      // REQ4 Creates REQ3 (link formed from 3
      makePostDependentOn( iReq4, iReq3, 1 );
      iReq3.activate();

      // Create instance of REQ5
      initializeTask( iReq5 );

      // Check that REQ1 can not be created
      assertThat( dependenciesFor( iReq1 ), exists() );
   }


   /**
    * Test a scenario where multiple single POSTCRT links point into the same task
    *
    * A single, non-recurring EO on completion creates multiple recurring tasks via POSTCRT
    *
    * @throws Exception
    *            an error occurred
    */
   @Ignore
   @Test
   public void testMultiplePostCRTLinksIntoSameTask() throws Exception {

      iReq5.revise();
      iReq5.activate();

      iReq2.revise();
      // REQ5 Creates REQ2 (link formed from REQ2)
      makePostDependentOn( iReq5, iReq2 );
      iReq2.activate();

      iReq3.revise();
      // REQ5 Creates REQ3 (link formed from REQ3)
      makePostDependentOn( iReq5, iReq3 );
      iReq3.activate();

      iReq4.revise();
      // REQ5 Creates REQ4 (link formed from REQ4)
      makePostDependentOn( iReq5, iReq4 );
      iReq4.activate();

      // Create instance of REQ5
      initializeTask( iReq5 );

      // Check that REQ1 through REQ3 can not be created
      assertThat( dependenciesFor( iReq1 ), exists() );
      assertThat( dependenciesFor( iReq2 ), exists() );
      assertThat( dependenciesFor( iReq3 ), exists() );
   }


   /**
    * Test a scenario where multiple single POSTCRT links point into the same task
    *
    * A single, non-recurring EO on completion creates multiple recurring tasks via POSTCRT
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testMultiplePostCRTLinksIntoSameTaskBackwards() throws Exception {

      iReq5.revise();
      iReq5.activate();

      iReq2.revise();
      // REQ5 Creates REQ2 (link formed from REQ2)
      makePostDependentOn( iReq5, iReq2 );
      iReq2.activate();

      iReq3.revise();
      // REQ5 Creates REQ3 (link formed from REQ3)
      makePostDependentOn( iReq5, iReq3 );
      iReq3.activate();

      iReq4.revise();
      // REQ5 Creates REQ4 (link formed from REQ4)
      makePostDependentOn( iReq5, iReq4 );
      iReq4.activate();

      // Create instance of REQ5
      initializeTask( iReq2 );

      // Check that REQ5 can not be created
      assertThat( dependenciesFor( iReq5 ), exists() );
   }


   /**
    * Test a scenario where multiple POSTCRT links and Multiple CRT links point into the same task.
    * If REQ5 is initialized, then REQ1 should be blocked by following the dependency tree from REQ1
    * to REQ3, then changing directions and continuing to follow REQ3 in the other direction back to
    * (REQ1)------>(REQ2)------>(REQ3)<------(REQ4)<----(REQ5)..................
    * ....<--POSTCRT-|<---POSTCRT-|.............................................
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPostChainAndCrtChainDependencies() throws Exception {

      iReq5.revise();
      iReq5.activate();

      iReq4.revise();
      makePostDependentOn( iReq5, iReq4 );
      iReq4.activate();

      iReq3.revise();
      makePostDependentOn( iReq4, iReq3 );
      iReq3.activate();

      iReq2.revise();
      makeDependentOn( iReq2, iReq3 );
      iReq2.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      // Create instance of REQ5
      initializeTask( iReq5 );

      // Check that REQ1 can be created
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /**
    * Test a scenario where multiple POSTCRT links and Multiple CRT links point into the same task.
    * If REQ1 is initialized, then REQ5 should be blocked by following the dependency tree from REQ1
    * to REQ3, then changing directions and continuing to follow REQ3 in the other direction back to
    * (REQ1)------>(REQ2)------>(REQ3)<------(REQ4)<----(REQ5)..................
    * ....<--POSTCRT-|<---POSTCRT-|.............................................
    *
    * @throws Exception
    *            an error occurred
    */
   @Ignore
   @Test
   public void testPostChainAndCrtChainDependenciesBackwards() throws Exception {

      iReq5.revise();
      iReq5.activate();

      iReq4.revise();
      makePostDependentOn( iReq5, iReq4 );
      iReq4.activate();

      iReq3.revise();
      makePostDependentOn( iReq4, iReq3 );
      iReq3.activate();

      iReq2.revise();
      makeDependentOn( iReq2, iReq3 );
      iReq2.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      // Create instance of REQ1
      initializeTask( iReq1 );

      // Check that REQ5 can not be created
      assertThat( dependenciesFor( iReq5 ), exists() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for non-unique tasks (Specifically the tail end node)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithNonUnique() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeNonUnique( iReq3 );
      makeDependentOn( iReq1, iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();

      iReq3.activate();
      // Note: The activation of REQ3 automatically removes the non-unique status
      // for the purposes of this test, we will flip it back
      makeNonUnique( iReq3 );

      // Create instance of REQ1
      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), exists() );
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Because REQ is not unique, there should be no dependency
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for non-unique tasks (Specifically the tail end node in reverse)
    *
    * @throws Exception
    */
   @Ignore
   @Test
   public void testCrtChainWithNonUniqueBackwards() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeNonUnique( iReq3 );
      makeDependentOn( iReq1, iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();
      // Activating removes the non-unique status, set it back
      makeNonUnique( iReq3 );

      // Create instance of REQ3
      initializeTask( iReq3 );

      // REQ3 is not unique, so it should not have a dependency even once created
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );
      // REQ2 can not be blocked by it's link to REQ3 because REQ3 is not unique
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Because REQ is not unique, there should be no dependency
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for non-unique tasks (Specifically the middle node while
    * traveling forwards)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithNonUniqueMiddle() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeDependentOn( iReq1, iReq2 );
      makeNonUnique( iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      makeNonUnique( iReq2 );
      iReq3.activate();

      // Create instance of REQ1
      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), exists() );

      // Because REQ is not unique, there should be no dependency
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for non-unique tasks (Specifically the middle node while
    * traveling backwards)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithNonUniqueMiddleBackwards() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeDependentOn( iReq1, iReq2 );
      makeNonUnique( iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      // Note that activation removes the non-unique status, setting it back for the purposes of
      // testing
      makeNonUnique( iReq2 );
      iReq3.activate();

      // Create instance of REQ3
      initializeTask( iReq3 );

      assertThat( dependenciesFor( iReq3 ), exists() );
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Because REQ2 is not unique, there should be no dependency for REQ1 because the chain ends
      // at REQ2 since it is not unique
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for on condition tasks (Specifically the tail node in the forward
    * direction)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithOnCondition() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeOnCondition( iReq3 );
      makeDependentOn( iReq1, iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      // Create instance of REQ1
      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), exists() );
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Because REQ is on condition, there should be no dependency
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for on condition tasks (Specifically the tail node in reverse
    * direction)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithOnConditionBackwards() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeOnCondition( iReq1 );
      makeDependentOn( iReq1, iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      // Create instance of REQ3
      initializeTask( iReq3 );

      assertThat( dependenciesFor( iReq3 ), exists() );
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Because REQ is on condition, there should be no dependency
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for on condition tasks (Specifically the middle node in reverse
    * direction)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithOnConditionMiddleBackwards() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeDependentOn( iReq1, iReq2 );
      makeOnCondition( iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      // Create instance of REQ3
      initializeTask( iReq3 );

      assertThat( dependenciesFor( iReq3 ), exists() );
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Because REQ is on condition, there should be no dependency
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /***
    *
    * This test will ensure that through each node in the recursion, the decision to follow
    * dependencies is re-evaluated for on condition tasks (Specifically the middle node in forward
    * direction)
    *
    * @throws Exception
    */
   @Test
   public void testCrtChainWithOnConditionMiddle() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();
      makeDependentOn( iReq1, iReq2 );
      makeOnCondition( iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      // Create instance of REQ1
      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), exists() );
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Because REQ is on condition, there should be no dependency
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );
   }


   /**
    * Tests the duplicate task exception on post-crt links<br>
    * <br>
    * REQ1 POSTCRT<- REQ2 POSTCRT<- REQ3 ->CRT REQ5<br>
    * REQ4 POSTCRT<- REQ3 <br>
    * <br>
    * REQ1 is initialized and when system initializes REQ4, a dependency should be found
    *
    * Test is ignored at the moment as this functionality/scenario is being discussed
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Ignore
   @Test
   public void testCrtandPostCrtLinksParallel() throws Exception {
      // REQ1 - No links from it
      iReq1.revise();
      iReq1.activate();

      // REQ4 - No links from it
      iReq4.revise();
      iReq4.activate();

      // REQ5 - No links from it
      iReq5.revise();
      iReq5.activate();

      iReq2.revise();
      // REQ1 -> CRT -> REQ2
      // |<- POSTCRT ----|
      makePostDependentOn( iReq1, iReq2 );
      iReq2.activate();

      iReq3.revise();
      // REQ2 -> CRT -> REQ3
      // |<- POSTCRT ----|
      makePostDependentOn( iReq2, iReq3, 1 );

      // REQ4 -> CRT -> REQ3
      // |<- POSTCRT ----|
      makePostDependentOn( iReq4, iReq3, 2 );

      // REQ3 -> CRT -> REQ5
      makeDependentOn( iReq3, iReq5, 3 );
      iReq3.activate();

      // Create instance of REQ1
      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), exists() );
      assertThat( dependenciesFor( iReq2 ), exists() );
      assertThat( dependenciesFor( iReq3 ), exists() );
      assertThat( dependenciesFor( iReq5 ), doesNotExist() );
      // Check that REQ4 can not be created
      assertThat( dependenciesFor( iReq4 ), exists() );
   }


   /**
    * Ensure that the dependency check ignores not-on-condition tasks
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testChainDependenciesOnCondition() throws Exception {
      iReq3.revise();
      makeOnCondition( iReq3 );
      iReq3.activate();

      iReq2.revise();
      makeOnCondition( iReq2 );
      makeDependentOn( iReq2, iReq3 );
      iReq2.activate();

      iReq1.revise();
      makeNonUnique( iReq1 );
      makeOnCondition( iReq1 );
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq3 ), doesNotExist() );
   }


   /**
    * Ensure that a severed dependency link does not prevent the old follow-on task from being
    * created.
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testLatestApprovedTaskDefinition_NewDependency() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeNonUnique( iReq1 );
      iReq1.activate();

      iMaintPrgm.create();
      iMaintPrgm.assign( iReq1 );
      iMaintPrgm.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * Ensure that a severed dependency link does not prevent the old follow-on task from being
    * created.
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testLatestApprovedTaskDefinition_SeveredDependency() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeNonUnique( iReq1 );
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      iMaintPrgm.create();
      iMaintPrgm.assign( iReq1 );
      iMaintPrgm.activate();

      iReq1.revise();
      removeDepedentOn( iReq1, iReq2 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * Ensure that only initialized tasks are considered dependent
    *
    * @throws Exception
    */
   @Test
   public void testNonInitializedTasksAreNotDependencies() throws Exception {
      iReq1.revise();
      iReq1.activate();

      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /**
    * Ensure that only unique tasks are considered dependent
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testNonUniqueHasNoActiveDependencies() throws Exception {
      iReq1.revise();
      makeOnCondition( iReq1 );
      makeNonUnique( iReq1 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /**
    * Ensure that the task considers itself as an active dependency
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testSameTaskDefinitionIsActiveDepedency() throws Exception {
      iReq1.revise();
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), exists() );
   }


   /**
    * Ensure that maintenance programs are considered when determining the active task definition
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testSeveredDependencyNoLongerConsideredDependent() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeNonUnique( iReq1 );
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();
      iReq1.revise();
      removeDepedentOn( iReq1, iReq2 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * Ensure that only dependents are considered. Given Req1 creates Req2 and Req3, and Req3 creates
    * Req1, but no actual Req1 exists, and an actual Req3 exists, there should be an active
    * dependent of Req2.
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testThatCyclicalDependencyIsDetected() throws Exception {
      iReq1.revise();
      iReq2.revise();
      iReq3.revise();

      iReq2.activate();
      makeDependentOn( iReq3, iReq1 ); // loop back to req1
      iReq3.activate();

      makeDependentOn( iReq1, iReq2, 1 );
      makeDependentOn( iReq1, iReq3, 2 );
      iReq1.activate();

      TaskKey lTask = initializeTask( iReq3 );

      assertThat( lTask, is( notNullValue( TaskKey.class ) ) );

      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * Ensure that only dependents are considered. Given Req1 creates Req2 and Req3, but no actual
    * Req1 exists, and an actual Req3 exists, there should be no active dependents of Req2.
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testThatDependenciesDoNotCrossSubTrees() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq3.revise();
      iReq3.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2, 1 );
      makeDependentOn( iReq1, iReq3, 2 );
      iReq1.activate();

      TaskKey lTask = initializeTask( iReq3 );

      assertThat( lTask, is( notNullValue( TaskKey.class ) ) );

      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * Ensure that only unique tasks are considered
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testUniqueHasActiveDependencies() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeNonUnique( iReq1 );
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * Ensure that only not-on-condition tasks are considered dependent
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testUniqueOnConditionHasNoActiveDependencies() throws Exception {
      iReq1.revise();
      makeOnCondition( iReq1 );
      iReq1.activate();

      initializeTask( iReq1 );

      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
   }


   /**
    * Tests below this Section are from http://rdwiki/display/ES/Baseline+Sync+Task+Dependency+Rules
    * See OPER-27159 for more information
    */

   /**
    *
    * Test from task A's point of view
    *
    * @throws Exception
    */
   private void executeScenario01A() throws Exception {
      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );
      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      terminateTask( lActiveTask );

   }


   /**
    * A (not recurring) CRT-> B (not recurring) CRT-> C (recurring)
    *
    * This is a task chain that is typically set up to get around how mx will not let you have
    * different intervals etc after the initial interval.
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario01() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq3 );

      makeDependentOn( iReq1, iReq2 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );
      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );
      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );
      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );
      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );
      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * A (not recurring) >POSTCRT-- B (not recurring) >POSTCRT-- C (recurring)
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario01() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq3 );

      makePostDependentOn( iReq1, iReq2 );
      makePostDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );
      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );
      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );
      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );
      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );
      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

   }


   /**
    * A (recurring) CRT-> B (recurring) CRT-> C (recurring)
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario02() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makeDependentOn( iReq1, iReq2, 2 );
      makeDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * A (recurring) >POSTCRT---> B (recurring) >POSTCRT---> C (recurring)
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario02() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makePostDependentOn( iReq1, iReq2, 2 );
      makePostDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * A (not recurring) CRT-> C (recurring) <br>
    * B (not recurring) CRT-> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario03() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq3 );

      makeDependentOn( iReq1, iReq3 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) >POSTCRT---> C (recurring) <br>
    * B (not recurring) >POSTCRT---> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario03() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq3 );

      makePostDependentOn( iReq1, iReq3, 2 );
      makePostDependentOn( iReq2, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (recurring) CRT-> C (recurring) <br>
    * B (recurring) CRT-> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario04() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makeDependentOn( iReq1, iReq3, 2 );
      makeDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (recurring) >POSTCRT---> C (recurring) <br>
    * B (recurring) >POSTCRT---> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario04() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makePostDependentOn( iReq1, iReq3, 2 );
      makePostDependentOn( iReq2, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * Test from task A's point of view
    *
    * @throws Exception
    *            an error occurred
    */
   private void executeScenario05A() throws Exception {

   }


   /**
    * A (not recurring) CRT-> B (not recurring) <br>
    * A (not recurring) CRT-> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario05() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeDependentOn( iReq1, iReq2, 1 );
      makeDependentOn( iReq1, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) >POSTCRT---> B (not recurring) <br>
    * A (not recurring) >POSTCRT---> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario05() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makePostDependentOn( iReq1, iReq2, 1 );
      makePostDependentOn( iReq1, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * A (not recurring) CRT-> B (recurring) <br>
    * A (not recurring) CRT-> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario06() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makeDependentOn( iReq1, iReq2, 1 );
      makeDependentOn( iReq1, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   @Test
   public void testPOSTCRTscenario06() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makePostDependentOn( iReq1, iReq2, 2 );
      makePostDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * A (recurring) CRT-> B (recurring) <br>
    * A (recurring) CRT-> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario07() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makeDependentOn( iReq1, iReq2, 2 );
      makeDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) >POSTCRT---> B (recurring) <br>
    * A (not recurring) >POSTCRT---> C (recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario07() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );
      makeRecurring( iReq3 );

      makePostDependentOn( iReq1, iReq2, 2 );
      makePostDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

   }


   /**
    * A (recurring) CRT-> C (not recurring) <br>
    * B (recurring) CRT-> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario08() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );

      makeDependentOn( iReq1, iReq3, 2 );
      makeDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

   }


   /**
    * A (recurring) >POSTCRT---> C (not recurring) <br>
    * B (recurring) >POSTCRT---> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario08() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );

      makePostDependentOn( iReq1, iReq3, 1 );
      makePostDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) CRT-> C (not recurring) <br>
    * B (not recurring) CRT-> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario09() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeDependentOn( iReq1, iReq3 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) >POSTCRT---> C (not recurring) <br>
    * B (not recurring) >POSTCRT---> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario09() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makePostDependentOn( iReq1, iReq3, 1 );
      makePostDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) CRT-> B (recurring) <br>
    * A (not recurring) CRT-> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario10() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq2 );

      makeDependentOn( iReq1, iReq2, 1 );
      makeDependentOn( iReq1, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * A (not recurring) >POSTCRT---> B (recurring) <br>
    * A (not recurring) >POSTCRT---> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario10() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq2 );

      makePostDependentOn( iReq1, iReq2, 2 );
      makePostDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * A (recurring) >POSTCRT---> B (not recurring) <br>
    * A (recurring) >POSTCRT---> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario11() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );

      makeDependentOn( iReq1, iReq2, 2 );
      makeDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

   }


   /**
    * A (recurring) CRT-> B (not recurring) <br>
    * A (recurring) CRT-> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario11() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );

      makePostDependentOn( iReq1, iReq2, 2 );
      makePostDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

   }


   /**
    * A (recurring) CRT-> B ( recurring) <br>
    * A (recurring) CRT-> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario12() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );

      makeDependentOn( iReq1, iReq2, 2 );
      makeDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

   }


   /**
    * A (recurring) >POSTCRT---> B ( recurring) <br>
    * A (recurring) >POSTCRT---> C (not recurring) <br>
    *
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario12() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeRecurring( iReq1 );
      makeRecurring( iReq2 );

      makePostDependentOn( iReq1, iReq2, 2 );
      makePostDependentOn( iReq1, iReq3, 3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is blocked
      assertThat( dependenciesFor( iReq2 ), exists() );
   }


   /**
    * With a previous revision history of<br>
    * A (not recurring) CRT-> C (not recurring) <br>
    * B (not recurring) CRT-> C (not recurring) <br>
    * Then a revision such that there is no longer any CRT link from B to C<br>
    * <br>
    * A multi link inbound setup no longer qualifies and regular blocking should be done<br>
    * between A and C
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testCRTscenario13() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeDependentOn( iReq1, iReq3 );
      makeDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      // Revise and remove the B -> C link
      iReq2.revise();
      removeDepedentOn( iReq2, iReq3 );
      iReq2.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

   }


   /**
    * With a previous revision history of<br>
    * A (not recurring) >POSTCRT---> C (not recurring) <br>
    * B (not recurring) >POSTCRT---> C (not recurring) <br>
    * Then a revision such that there is no longer any POSTCRT link from B to C<br>
    * <br>
    * A multi link inbound setup no longer qualifies and regular blocking should be done<br>
    * between A and C
    *
    * @throws Exception
    *            an error occurred
    */
   @Test
   public void testPOSTCRTscenario13() throws Exception {
      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makePostDependentOn( iReq1, iReq3, 1 );
      makePostDependentOn( iReq2, iReq3, 2 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      // Revise and remove the B -> C link
      iReq3.revise();
      removeDepedentOn( iReq3, iReq2, 2 );
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is not blocked
      assertThat( dependenciesFor( iReq3 ), doesNotExist() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is blocked
      assertThat( dependenciesFor( iReq1 ), exists() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * Combine a CRT link inbound with a POSTCRT link inbound<br>
    * A (not recurring) -------CRT-> C (not recurring) <br>
    * B (not recurring) >POSTCRT---> C (not recurring) <br>
    * <br>
    * The Multi link detection should sum these to a link count of 2<br>
    * and unlock the blocking behavior for A and B
    *
    * @throws Exception
    *            an error occurred
    */

   @Test
   public void testMultiLinkTypeInbound() throws Exception {

      // A
      iReq1.revise();
      // B
      iReq2.revise();
      // C
      iReq3.revise();

      makeDependentOn( iReq1, iReq3 );
      makePostDependentOn( iReq2, iReq3 );

      iReq1.activate();
      iReq2.activate();
      iReq3.activate();

      TaskKey lActiveTask = null;

      // A is ACTV
      lActiveTask = initializeTask( iReq1 );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // B is ACTV
      lActiveTask = initializeTask( iReq2 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that C is blocked
      assertThat( dependenciesFor( iReq3 ), exists() );

      // Reset
      terminateTask( lActiveTask );

      // C is ACTV
      initializeTask( iReq3 );

      // Validate that A is not blocked
      assertThat( dependenciesFor( iReq1 ), doesNotExist() );

      // Validate that B is not blocked
      assertThat( dependenciesFor( iReq2 ), doesNotExist() );
   }


   /**
    * Returns TRUE if there is a dependency
    *
    * @param aTaskDefinition
    *           the task
    *
    * @return TRUE if they have a dependency
    */
   private boolean dependenciesFor( TaskDefinition aTaskDefinition ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskDefinition.getLatestTaskRevision(), "aTaskDbId", "aTaskId" );
      lArgs.add( INV_ACFT_CARRIER, "aInvNoDbId", "aInvNoId" );

      // Copy data from vw_h_baseline_task to the global temporary table
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView", lArgs );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.bsync.HasACTVDependentTasks", lArgs );

      return ( !( lQs.next() && lQs.getBoolean( "no_actv_task" ) ) );
   }


   /**
    * Creates a dependency on maintenance program
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the task definition that this depends on
    */
   private void makeDependentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition ) {
      makeDependentOn( aTaskDefinition, aDependentOnTaskDefinition, 1 );
   }


   /**
    * Creates a dependency on maintenance program in reverse order
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the task definition that this depends on
    */
   private void makePostDependentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition ) {
      makePostDependentOn( aTaskDefinition, aDependentOnTaskDefinition, 1 );
   }


   /**
    * Creates a dependency on maintenance program
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the task definition that this depends on
    * @param aDepId
    *           the id of the dependency
    */
   private void makeDependentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition, int aDepId ) {
      TaskTaskDepTable lTaskDepTable = TaskTaskDepTable
            .create( new TaskTaskDepKey( aTaskDefinition.getLatestTaskRevision(), aDepId ) );
      lTaskDepTable.setDepTaskDefn( aDependentOnTaskDefinition.getTaskDefn() );
      lTaskDepTable.setTaskDepAction( RefTaskDepActionKey.CRT );
      lTaskDepTable.insert();
   }


   /**
    * Creates a dependency on maintenance program in reverse order
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the task definition that this depends on
    * @param aDepId
    *           the id of the dependency
    */
   private void makePostDependentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition, int aDepId ) {
      TaskTaskDepTable lTaskDepTable = TaskTaskDepTable.create(
            new TaskTaskDepKey( aDependentOnTaskDefinition.getLatestTaskRevision(), aDepId ) );
      lTaskDepTable.setDepTaskDefn( aTaskDefinition.getTaskDefn() );
      lTaskDepTable.setTaskDepAction( RefTaskDepActionKey.POSTCRT );
      lTaskDepTable.insert();
   }


   /**
    * Sets the task to be unique
    *
    * @param aTaskDefinition
    *           the task definition
    */
   private void makeNonUnique( TaskDefinition aTaskDefinition ) {
      TaskTaskTable lTable =
            TaskTaskTable.findByPrimaryKey( aTaskDefinition.getLatestTaskRevision() );
      lTable.setUniqueBool( false );
      lTable.update();
   }


   /**
    * Sets the task to be on-condition
    *
    * @param aTaskDefinition
    *           the task definition
    */
   private void makeOnCondition( TaskDefinition aTaskDefinition ) {
      TaskTaskTable lTable =
            TaskTaskTable.findByPrimaryKey( aTaskDefinition.getLatestTaskRevision() );
      lTable.setOnConditionBool( true );
      lTable.update();
   }


   /**
    * Sets the task to be recurring
    *
    * @param aTaskDefinition
    *           the task definition
    */
   private void makeRecurring( TaskDefinition aTaskDefinition ) {
      TaskTaskTable lTable =
            TaskTaskTable.findByPrimaryKey( aTaskDefinition.getLatestTaskRevision() );
      lTable.setRecurringTaskBool( true );
      lTable.update();
      // Create a self referencing CRT link
      makeDependentOn( aTaskDefinition, aTaskDefinition );
   }


   /**
    * Removes task dependency
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the dependent task definition
    *
    * @throws Exception
    */
   private void removeDepedentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition ) throws Exception {
      removeDepedentOn( aTaskDefinition, aDependentOnTaskDefinition, 1 );
   }


   /**
    * Removes task dependency
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the dependent task definition
    * @param aDepId
    *           the id of the dependency
    *
    * @throws Exception
    */
   private void removeDepedentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition, int aDepId ) throws Exception {
      TaskDefnService.removeLinkedTask(
            new TaskTaskDepKey( aTaskDefinition.getLatestTaskRevision(), aDepId ) );
   }
}
