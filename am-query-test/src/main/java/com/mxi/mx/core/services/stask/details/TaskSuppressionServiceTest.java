
package com.mxi.mx.core.services.stask.details;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskClassKey.FOLLOW;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.partgroup.AlternatePartService;
import com.mxi.mx.core.services.req.PartRequestService;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * This test class tests the {@link TaskSuppressionService} class.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskSuppressionServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String SUPPRESS_DUPLICATE_TASKS_PARAM = "SUPPRESS_DUPLICATE_TASKS";
   private static final String ALLOW_REPL_SUPPRESSION = "ALLOW_REPL_SUPPRESSION";

   private static final String POSITION_DESCRIPTION = "POSITION_DESCRIPTION";

   private static SchedStaskDao sSchedStaskDao;

   private TaskSuppressionService iTaskSuppressionService;


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   @Before
   public void setUp() throws Exception {
      sSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      iTaskSuppressionService = new TaskSuppressionService();
   }


   /**
    * In this case we are testing logic around suppression where a REPL task is being added to a
    * work package. It has a JIC with a part requirement on it. This same JIC is present on a REQ
    * already assigned to the work package. The REQ is actually on a sibling SYS slot to the REPL
    * and so position matching should not be enforced as both tasks must be REPL in order to compare
    * position for suppression.
    *
    * The part requirements are matching as they come from the same JIC. This logic was introduced
    * in OPER-1118. We should expect task suppression in this scenario.
    *
    * @throws Exception
    */
   @Test
   public void
         suppressDuplicateWorkLines_addingREPLIntoWPHavingREQWithDuplicateJIC_partReqSamePositions()
               throws Exception {

      boolean lSuppressDuplicateTasksParm =
            GlobalParameters.getInstance( "LOGIC" ).getBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM );
      GlobalParameters.getInstance( "LOGIC" ).setBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM, true );

      TaskKey REPL_TASK_WITH_DUPLICATE_JIC = new TaskKey( "4650:100" );

      List<TaskKey> lSuppressedTasks =
            iTaskSuppressionService.suppressDuplicateWorkLines( REPL_TASK_WITH_DUPLICATE_JIC );

      GlobalParameters.getInstance( "LOGIC" ).setBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM,
            lSuppressDuplicateTasksParm );

      assertEquals( 1, lSuppressedTasks.size() );
   }


   /**
    * In this case we are testing logic around suppression where a REPL task is being added to a
    * work package. It has a JIC with a part requirement on it. This same JIC is present on a REQ
    * already assigned to the work package. The REQ is actually on a sibling SYS slot to the REPL
    * and so position matching should not be enforced as both tasks must be REPL in order to compare
    * position for suppression.
    *
    * The part requirements are matching as they come from the same JIC. This logic was introduced
    * in OPER-1118. We should NOT expect task suppression in this scenario.
    *
    * @throws Exception
    */
   @Test
   public void
         suppressDuplicateWorkLines_addingREPLIntoWPHavingREQWithDuplicateJIC_partReqDiffPositions()
               throws Exception {

      boolean lSuppressDuplicateTasksParm =
            GlobalParameters.getInstance( "LOGIC" ).getBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM );
      GlobalParameters.getInstance( "LOGIC" ).setBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM, true );

      TaskKey REPL_TASK_WITH_DUPLICATE_JIC = new TaskKey( "4650:1002" );

      List<TaskKey> lSuppressedTasks =
            iTaskSuppressionService.suppressDuplicateWorkLines( REPL_TASK_WITH_DUPLICATE_JIC );

      GlobalParameters.getInstance( "LOGIC" ).setBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM,
            lSuppressDuplicateTasksParm );

      assertTrue( lSuppressedTasks.isEmpty() );
   }


   /**
    * In this case we are testing logic around suppression where two REPL tasks are being added to a
    * work package. Both their JICs based on the same task definition, but initialized on different
    * positions. To suppress one of the JIC, we use ALLOW_REPL_SUPPRESSION config parameter.
    *
    * The part requirements are matching as they come from the same JIC. This logic was introduced
    * in OPER-17925. We should expect task suppression in this scenario.
    *
    * @throws Exception
    */
   @Test
   public void
         suppressDuplicateWorkLines_addingREPLIntoWPHavingREQWithDuplicateJIC_partReqDiffPositions_allowReplSuppression()
               throws Exception {

      boolean lSuppressDuplicateTasksParm =
            GlobalParameters.getInstance( "LOGIC" ).getBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM );
      GlobalParameters.getInstance( "LOGIC" ).setBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM, true );

      boolean lAllowReplSuppressionParm =
            GlobalParameters.getInstance( "LOGIC" ).getBoolean( ALLOW_REPL_SUPPRESSION );
      GlobalParameters.getInstance( "LOGIC" ).setBoolean( ALLOW_REPL_SUPPRESSION, true );

      TaskKey REPL_TASK_WITH_DUPLICATE_JIC = new TaskKey( "4650:110" );

      List<TaskKey> lSuppressedTasks =
            iTaskSuppressionService.suppressDuplicateWorkLines( REPL_TASK_WITH_DUPLICATE_JIC );

      GlobalParameters.getInstance( "LOGIC" ).setBoolean( ALLOW_REPL_SUPPRESSION,
            lAllowReplSuppressionParm );
      GlobalParameters.getInstance( "LOGIC" ).setBoolean( SUPPRESS_DUPLICATE_TASKS_PARAM,
            lSuppressDuplicateTasksParm );

      assertEquals( 1, lSuppressedTasks.size() );
   }


   /**
    *
    * Promoting a duplicate job card with a requested part requirement generates a part request.
    *
    * <pre>
    * Given two duplicate job cards, each with a requested part requirement
    *  and two requirement tasks, one for each job card
    *  and an in-work work package with both requirement tasks assigned to it
    *  and one job card is suppressing the other
    *  When the suppressed job card is promoted
    *  Then a part request will be generated for the newly promoted job card
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void promoteDuplicateJobCard_withREQPartRequirement() throws Exception {

      final PartNoKey lRequestedPart = createSerializedPart();
      final PartGroupKey lPartGroup =
            AlternatePartService.getBomPartsFromPartNo( lRequestedPart )[0];

      // Given two duplicate job cards, each with a requested part requirement
      final TaskKey lFirstJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.addPartRequirement( lRequestedPart, lPartGroup, RefReqActionKey.REQ, 1.0 );
         }
      } );

      final TaskKey lSecondJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.addPartRequirement( lRequestedPart, lPartGroup, RefReqActionKey.REQ, 1.0 );
         }
      } );

      // Given two requirement tasks, one for each job card
      final TaskKey lFirstRequirementTask = createRequirementTask( lFirstJobCard );
      final TaskKey lSecondRequirementTask = createRequirementTask( lSecondJobCard );

      // Given an in-work work package for an aircraft with both requirement tasks assigned to it
      TaskKey lWorkPackage = createWorkPackage( lFirstRequirementTask, lSecondRequirementTask );

      suppressJobCard( lFirstJobCard, lSecondJobCard );

      iTaskSuppressionService.promoteDuplicateJobCard( lWorkPackage, lFirstJobCard, true );

      assertJobCardIsPromoted( lSecondJobCard );
      assertPartRequestsGenerated( lSecondJobCard, lRequestedPart );
   }


   /**
    *
    * Promoting a duplicate job card with a non-requested part requirement does not generate a part
    * request.
    *
    * <pre>
    * Given two duplicate job cards, each with a non-requested part requirement
    *  and two requirement tasks, one for each job card
    *  and an in-work work package with both requirement tasks assigned to it
    *  and one job card is suppressing the other
    *  When the suppressed job card is promoted
    *  Then a part request will NOT be generated for the newly promoted job card
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void promoteDuplicateJobCard_withNOREQPartRequirement() throws Exception {

      final PartNoKey lNonRequestedPart = createSerializedPart();

      // Given two duplicate job cards, each with the non-requested part requirement
      final TaskKey lFirstJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.addPartRequirement( lNonRequestedPart, null, RefReqActionKey.NOREQ, 1.0 );
         }
      } );

      final TaskKey lSecondJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.addPartRequirement( lNonRequestedPart, null, RefReqActionKey.NOREQ, 1.0 );
         }
      } );

      // Given two requirement tasks, one for each job card
      final TaskKey lFirstRequirementTask = createRequirementTask( lFirstJobCard );
      final TaskKey lSecondRequirementTask = createRequirementTask( lSecondJobCard );

      // Given an in-work work package for an aircraft with both requirement tasks assigned to it
      TaskKey lWorkPackage = createWorkPackage( lFirstRequirementTask, lSecondRequirementTask );

      suppressJobCard( lFirstJobCard, lSecondJobCard );

      iTaskSuppressionService.promoteDuplicateJobCard( lWorkPackage, lFirstJobCard, true );

      assertJobCardIsPromoted( lSecondJobCard );
      assertNoPartRequestsGenerated( lSecondJobCard );
   }


   /**
    *
    * Promoting a duplicate job card with two part requirements, one which is requested and one
    * which is not requested, generates a part request only for the requested part requirement.
    *
    * <pre>
    * Given two duplicate job cards, each with a requested and a non-requested part requirement
    *  and two requirement tasks, one for each job card
    *  and an in-work work package with both requirement tasks assigned to it
    *  and one job card is suppressing the other
    *  When the suppressed job card is promoted
    *  Then one part request will be generated for the requested part requirement on the newly promoted job card
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void promoteDuplicateJobCard_withREQAndNOREQPartRequirements() throws Exception {

      final PartNoKey lRequestedPart = createSerializedPart();
      final PartGroupKey lPartGroup =
            AlternatePartService.getBomPartsFromPartNo( lRequestedPart )[0];
      final PartNoKey lNonRequestedPart = createSerializedPart();

      // Given two duplicate job cards, each with a requested part requirement and a non-requested
      // part requirement
      final TaskKey lFirstJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.addPartRequirement( lRequestedPart, lPartGroup, RefReqActionKey.REQ, 1.0 );
            aJobCard.addPartRequirement( lNonRequestedPart, null, RefReqActionKey.NOREQ, 1.0 );
         }
      } );

      final TaskKey lSecondJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.addPartRequirement( lRequestedPart, lPartGroup, RefReqActionKey.REQ, 1.0 );
            aJobCard.addPartRequirement( lNonRequestedPart, null, RefReqActionKey.NOREQ, 1.0 );
         }
      } );
      // Given two requirement tasks, one for each job card
      final TaskKey lFirstRequirementTask = createRequirementTask( lFirstJobCard );
      final TaskKey lSecondRequirementTask = createRequirementTask( lSecondJobCard );

      // Given an in-work work package for an aircraft with both requirement tasks assigned to it
      TaskKey lWorkPackage = createWorkPackage( lFirstRequirementTask, lSecondRequirementTask );

      suppressJobCard( lFirstJobCard, lSecondJobCard );

      iTaskSuppressionService.promoteDuplicateJobCard( lWorkPackage, lFirstJobCard, true );

      assertJobCardIsPromoted( lSecondJobCard );
      assertPartRequestsGenerated( lSecondJobCard, lRequestedPart );
   }


   /**
    *
    * Verify that a work scope enabled task that is already in the work scope of a work package will
    * suppress a second duplicate work scope enabled task that is not in the work scope. The second
    * task must be a child of a REQ class requirement task (that is assigned to the work package).
    *
    * "Duplicated work scope tasks" are defined as non-historical tasks with the same definition and
    * the definition is work scope enabled.
    *
    * Exercises {@linkplain TaskSuppressionService#suppressDuplicateWorkLines(TaskKey)}
    */
   @Test
   public void duplicateJobCardSuppressedWhenChildOfReqRequirement() throws Exception {

      // Given two job card tasks against the same inventory and based on the same definition.
      // And the definition is work scope enabled.
      InventoryKey lInv = Domain.createAircraft();
      TaskTaskKey lJobCardDefn = Domain.createJobCardDefinition( aDefn -> {
         aDefn.setWorkScopeEnabled( true );
      } );
      TaskKey lJobCard1 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
      } );

      TaskKey lJobCard2 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
      } );

      // Given REQ class requirement tasks, each assigned one of the job card tasks.
      TaskKey lReq1 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( REQ );
         aReq.addJobCard( lJobCard1 );
      } );
      TaskKey lReq2 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( REQ );
         aReq.addJobCard( lJobCard2 );
      } );

      // Given a work package with all the requirement tasks assigned to it and all of their job
      // cards included in its work scope.
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq1 );
         aWp.addTask( lReq2 );
         aWp.addWorkScopeTask( lJobCard1 );
      } );

      // When the duplicate work lines of the work package are requested to be suppressed, given any
      // of the assigned requirements.
      List<TaskKey> lSuppressedTasks = iTaskSuppressionService.suppressDuplicateWorkLines( lReq1 );

      // Then the first job card task is suppressed.
      assertThat( "Unexpected suppressed tasks returned.", lSuppressedTasks.size(), is( 1 ) );
      assertThat( "Expected the second job card to be returned.", lSuppressedTasks,
            hasItem( lJobCard2 ) );

      // Then the first job card task has its duplicate-JIC field set to the second job card task.
      assertThat( "Unexpected duplicate-JIC of the second job card.",
            sSchedStaskDao.findByPrimaryKey( lJobCard2 ).getDupJicSchedKey(), is( lJobCard1 ) );
   }


   /**
    *
    * Verify that a work scope enabled task that is already in the work scope of a work package will
    * not suppress a second duplicate work scope enabled task when that second task is a child of a
    * FOLLOW class requirement task (that is assigned to the work package).
    *
    * "Duplicated work scope tasks" are defined as non-historical tasks with the same definition and
    * the definition is work scope enabled.
    *
    * Exercises {@linkplain TaskSuppressionService#suppressDuplicateWorkLines(TaskKey)}
    */
   @Test
   public void duplicateJobCardNotSuppressedWhenChildOfFollowRequirement() throws Exception {

      // Given two job card tasks against the same inventory and based on the same definition.
      // And the definition is work scope enabled.
      InventoryKey lInv = Domain.createAircraft();
      TaskTaskKey lJobCardDefn = Domain.createJobCardDefinition( aDefn -> {
         aDefn.setWorkScopeEnabled( true );
      } );
      TaskKey lJobCard1 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
      } );

      TaskKey lJobCard2 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
      } );

      // Given FOLLOW class requirement tasks, each assigned one of the job card tasks.
      TaskKey lReq1 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( FOLLOW );
         aReq.addJobCard( lJobCard1 );
      } );
      TaskKey lReq2 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( FOLLOW );
         aReq.addJobCard( lJobCard2 );
      } );

      // Given a work package with all the requirement tasks assigned to it and all of their job
      // cards included in its work scope.
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq1 );
         aWp.addTask( lReq2 );
         aWp.addWorkScopeTask( lJobCard1 );
      } );

      // When the duplicate work lines of the work package are requested to be suppressed, given any
      // of the assigned requirements.
      List<TaskKey> lSuppressedTasks = iTaskSuppressionService.suppressDuplicateWorkLines( lReq1 );

      // Then the second job card task is not suppressed.
      assertThat( "Unexpected suppressed tasks returned.", lSuppressedTasks.size(), is( 0 ) );
   }


   /**
    * Verify that neither of the 2 "duplicated work scope JICs" are suppressed when both JICs are
    * children of requirement tasks with a class of FOLLOW.
    *
    * "Duplicated work scope JICs" are defined as non-historical job card tasks with the same
    * definition and are assigned to the work scope of the same work package.
    *
    * Exercises {@linkplain TaskSuppressionService#suppressDuplicateJobCards(List)}
    */
   @Test
   public void jicsOfDuplicatedWorkScopeJicsAreNotSuppressedWhenChildrenOfTasksWithClassFollow()
         throws Exception {

      // Given two job card tasks against the same inventory and based on the same definition.
      // And the definition is work scope enabled.
      //
      // Notes:
      // - The inventory must allow synchronization (see
      // TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      // - The inventory must have a position description (see GetJobCardsFromSameTaskDefn.qrx).
      // - The JICs must be active (see GetJobCardsFromSameTaskDefn.qrx).
      InventoryKey lInv = Domain.createAircraft( aAcft -> {
         aAcft.setAllowSynchronization( true );
         aAcft.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskTaskKey lJobCardDefn = Domain.createJobCardDefinition( aDefn -> {
         aDefn.setWorkScopeEnabled( true );
      } );
      TaskKey lJobCard1 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
         aJic.setStatus( ACTV );
      } );
      TaskKey lJobCard2 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
         aJic.setStatus( ACTV );
      } );

      // Given FOLLOW class requirement tasks, each assigned one of the job card tasks.
      //
      // Note; the REQ definitions must be associated to the JIC definitions
      // (TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      TaskKey lReq1 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( FOLLOW );
         aReq.addJobCard( lJobCard1 );
         aReq.setDefinition( Domain.createRequirementDefinition(
               aReqDefn -> aReqDefn.addJobCardDefinition( lJobCardDefn ) ) );
      } );
      TaskKey lReq2 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( FOLLOW );
         aReq.addJobCard( lJobCard2 );
         aReq.setDefinition( Domain.createRequirementDefinition(
               aReqDefn -> aReqDefn.addJobCardDefinition( lJobCardDefn ) ) );
      } );

      // Given a work package with all the requirement tasks assigned to it and all of their job
      // cards included in its work scope.
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq1 );
         aWp.addTask( lReq2 );
         aWp.addWorkScopeTask( lJobCard1 );
         aWp.addWorkScopeTask( lJobCard2 );
      } );

      // When the duplicate work scope JICs of the work package are requested to be suppressed.
      iTaskSuppressionService.suppressDuplicateJobCards( Arrays.asList( lJobCard1, lJobCard2 ) );

      // Then the neither JIC is suppressed.
      assertNull( "Expected duplicate-JIC of the first job card to be null.",
            sSchedStaskDao.findByPrimaryKey( lJobCard1 ).getDupJicSchedKey() );
      assertNull( "Expected duplicate-JIC of the second job card to be null.",
            sSchedStaskDao.findByPrimaryKey( lJobCard2 ).getDupJicSchedKey() );
   }


   /**
    * Verify that neither of the 2 "duplicated work scope JICs" are suppressed when one JIC is a
    * child of a requirement task with class FOLLOW and one is a child of a requirement task with
    * class REQ.
    *
    * "Duplicated work scope JICs" are defined as non-historical job card tasks with the same
    * definition and are assigned to the work scope of the same work package.
    *
    * Exercises {@linkplain TaskSuppressionService#suppressDuplicateJobCards(List)}
    */
   @Test
   public void
         duplicatedWorkScopeJicsAreNotSuppressedWhenOneIsChildOfFollowReqAndOneIsChildOfReqReq()
               throws Exception {

      // Given two job card tasks against the same inventory and based on the same definition.
      // And the definition is work scope enabled.
      //
      // Notes:
      // - The inventory must allow synchronization (see
      // TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      // - The inventory must have a position description (see GetJobCardsFromSameTaskDefn.qrx).
      // - The JICs must be active (see GetJobCardsFromSameTaskDefn.qrx).
      InventoryKey lInv = Domain.createAircraft( aAcft -> {
         aAcft.setAllowSynchronization( true );
         aAcft.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskTaskKey lJobCardDefn = Domain.createJobCardDefinition( aDefn -> {
         aDefn.setWorkScopeEnabled( true );
      } );
      TaskKey lJobCard1 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
         aJic.setStatus( ACTV );
      } );
      TaskKey lJobCard2 = Domain.createJobCard( aJic -> {
         aJic.setDefinition( lJobCardDefn );
         aJic.setInventory( lInv );
         aJic.setStatus( ACTV );
      } );

      // Given one FOLLOW class and one REQ class requirement task, each assigned one of the job
      // card tasks.
      //
      // Note; the REQ definitions must be associated to the JIC definitions
      // (TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      TaskKey lReq1 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( FOLLOW );
         aReq.addJobCard( lJobCard1 );
         aReq.setDefinition( Domain.createRequirementDefinition(
               aReqDefn -> aReqDefn.addJobCardDefinition( lJobCardDefn ) ) );
      } );
      TaskKey lReq2 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( REQ );
         aReq.addJobCard( lJobCard2 );
         aReq.setDefinition( Domain.createRequirementDefinition(
               aReqDefn -> aReqDefn.addJobCardDefinition( lJobCardDefn ) ) );
      } );

      // Given a work package with all the requirement tasks assigned to it and all of their job
      // cards included in its work scope.
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq1 );
         aWp.addTask( lReq2 );
         aWp.addWorkScopeTask( lJobCard1 );
         aWp.addWorkScopeTask( lJobCard2 );
      } );

      // When the duplicate work scope JICs of the work package are requested to be suppressed.
      iTaskSuppressionService.suppressDuplicateJobCards( Arrays.asList( lJobCard1, lJobCard2 ) );

      // Then the neither JIC is suppressed.
      assertNull( "Expected duplicate-JIC of the first job card to be null.",
            sSchedStaskDao.findByPrimaryKey( lJobCard1 ).getDupJicSchedKey() );
      assertNull( "Expected duplicate-JIC of the second job card to be null.",
            sSchedStaskDao.findByPrimaryKey( lJobCard2 ).getDupJicSchedKey() );
   }


   /**
    * Assert one part request was generated for the given job card and part.
    *
    * @param aJobCard
    * @param aPart
    */
   private void assertPartRequestsGenerated( TaskKey aJobCard, PartNoKey aPart ) {
      PartRequestKey[] lPartRequests = PartRequestService.getPartRequestsForTask( aJobCard );
      assertEquals( 1, lPartRequests.length );
      assertEquals( aPart, ReqPartTable.findByPrimaryKey( lPartRequests[0] ).getSpecPartNo() );
   }


   /**
    * Assert no part requests were generated for the given job card.
    *
    * @param aJobCard
    */
   private void assertNoPartRequestsGenerated( TaskKey aJobCard ) {
      int lActualRequestsGenerated = PartRequestService.getPartRequestsForTask( aJobCard ).length;
      assertEquals( 0, lActualRequestsGenerated );
   }


   /**
    * Assert the specified job card is promoted (no longer suppressed).
    *
    * @param aJobCard
    */
   private void assertJobCardIsPromoted( TaskKey aJobCard ) {
      assertNull( sSchedStaskDao.findByPrimaryKey( aJobCard ).getDupJicSchedKey() );
   }


   /**
    * Create a serialized part number.
    *
    * @return
    */
   private PartNoKey createSerializedPart() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInDefaultPartGroup( true );
            aPart.setInventoryClass( RefInvClassKey.SER );
         }
      } );
   }


   /**
    * Create a requirement task with the given job card.
    *
    * @param aJobCard
    * @return the new requirement task key
    */
   private TaskKey createRequirementTask( final TaskKey aJobCard ) {
      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setTaskClass( RefTaskClassKey.REQ );
            aReq.addJobCard( aJobCard );
         }
      } );
   }


   /**
    *
    * Create a work package with the given requirement tasks.
    *
    * @param aFirstRequirementTask
    * @param aSecondRequirementTask
    * @return the new work package key
    */
   private TaskKey createWorkPackage( final TaskKey aFirstRequirementTask,
         final TaskKey aSecondRequirementTask ) {

      final InventoryKey lAircraft = Domain.createAircraft();

      return Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
            aWorkPackage.addTask( aFirstRequirementTask );
            aWorkPackage.addTask( aSecondRequirementTask );
         }
      } );

   }


   /**
    * Suppress one job card by the other job card.
    *
    * @param aSuppressingJobCard
    * @param aSuppressedJobCard
    */
   private void suppressJobCard( TaskKey aSuppressingJobCard, TaskKey aSuppressedJobCard ) {
      SchedStaskTable lSchedStask = sSchedStaskDao.findByPrimaryKey( aSuppressedJobCard );
      lSchedStask.setDupJicSchedKey( aSuppressingJobCard );
      sSchedStaskDao.update( lSchedStask );
   }

}
