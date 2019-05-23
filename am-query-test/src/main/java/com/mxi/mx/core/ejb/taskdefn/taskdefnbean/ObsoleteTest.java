package com.mxi.mx.core.ejb.taskdefn.taskdefnbean;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.CANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefEventStatusKey.FORECAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Block;
import com.mxi.am.domain.BlockChainDefinition;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.services.work.WorkItemGeneratorExecuteImmediateFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.taskdefn.TaskDefnBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.bsync.synchronization.logic.InventorySynchronizer;
import com.mxi.mx.core.services.bsync.synchronization.model.InventoryTask;
import com.mxi.mx.core.services.bsync.synchronization.model.InventoryTask.InventoryTaskBuilder;
import com.mxi.mx.core.services.taskdefn.transferobject.TaskDefnRevTO;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 *
 * Automated functional tests to verify the behaviour of {@linkplain TaskDefnBean#obsolete}
 *
 */
@RunWith( Enclosed.class )
public class ObsoleteTest {

   /**
    *
    * This inner class verifies {@linkplain TaskDefnBean#obsolete} when obsoleting of a block chain
    * for a very specific scenario.
    *
    * In a nutshell, a recurring block chain has one recurring req starting it the first block and
    * another recurring req starting in the second block. The details of the scenario are as
    * follows:
    *
    * <pre>
    * Baseline setup...
    *   Given a requirement definition with the following attributes:
    *       - recurring
    *       - not on-condition
    *       - has a scheduling rule (and minimum forecast range)
    *       - has an assigned job card definition
    *       - activated
    *   Given another requirement definition with the same attributes as the first.
    *   Given a block chain definition with the following attributes:
    *       - recurring
    *       - not on-condition
    *       - has a scheduling rule (and minimum forecast range)
    *       - 2 blocks
    *       - first block assigned the first requirement definition
    *       - second block assigned the second requirement definition
    *       - superseded
    *   Given an in-revision version of the block chain definition.
    *
    * Actuals setup...
    *   Given a block chain with two block tasks (first block active, second block forecast)
    *   Given a requirement task with the following attributes:
    *       - based on the first requirement definition
    *       - is a sub-task of the first block task
    *       - has an assigned job card task
    *       - active
    *   Given another requirement task with the following attributes:
    *       - based on the first requirement definition
    *       - is a sub-task of the SECOND block task
    *       - has an assigned job card task
    *       - forecasted
    *   Given another requirement task with the following attributes:
    *       - based on the second requirement definition
    *       - is a sub-task of the SECOND block task
    *       - has an assigned job card task
    *       - active
    *
    *
    * And now for some pretty ascii art...
    *
    *   block chain
    *     block1 (actv) ---> block2 (forecast)
    *       |                  |
    *       +-> req1 (actv)    +-> req1 (forecast)
    *             |            |      |
    *             +-> jic1     |      +-> jic1
    *                          |
    *                          +-> req2 (actv)
    *                                |
    *                                +-> jic2
    * </pre>
    */
   public static class ObsoleteBlockChainWhenDiffReqInDiffBlocks {

      private static final BigDecimal MINIMUM_FORECAST_RANGE = new BigDecimal( 100 );
      private static final int REVISION_1 = 1;
      private static final int REVISION_2 = 2;

      private static final String REQ_1_CODE = "REQ1";
      private static final BigDecimal REQ_1_CDY_INTERVAL = new BigDecimal( 10 );

      private static final String REQ_2_CODE = "REQ2";
      private static final BigDecimal REQ_2_CDY_INTERVAL = new BigDecimal( 20 );

      private static final String BLOCK_CHAIN_NAME = "block chain";
      private static final int BLOCK_1 = 1;
      private static final int BLOCK_2 = 2;
      private static final String BLOCK_1_CODE = "BLOCK1";
      private static final String BLOCK_1_NAME = BLOCK_1_CODE;
      private static final String BLOCK_2_CODE = "BLOCK2";
      private static final String BLOCK_2_NAME = BLOCK_2_CODE;

      private static final int INTERVAL_ONE = 1;
      private static final int INTERVAL_TWO = 2;
      private static final int STARTING_BLOCK_ONE = 1;
      private static final int STARTING_BLOCK_TWO = 2;

      private HumanResourceKey iAuthorizingHr;
      private int iAuthorizingHrId;

      private InventoryKey iAircraft;
      private TaskKey iActvReq1;
      private TaskKey iActvJic1;
      private TaskKey iForecastReq1;
      private TaskKey iForecastJic1;
      private TaskKey iActvReq2;
      private TaskKey iActvJic2;
      private Map<Integer, TaskTaskKey> iInRevisionBlockChain;
      private TaskKey iActiveBlock;
      private TaskKey iForecastBlock;
      private TaskKey iCommittedWorkPackage;

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();

      @Rule
      public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


      /**
       *
       * Verifies that the active block task is cancelled.
       *
       */
      @Test
      public void itCancelsActvBlockWhenBlockChainIsObsoleted() throws Exception {

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the active block task is canceled.
         assertEquals( CANCEL, getStatus( iActiveBlock ) );
      }


      /**
       *
       * Verify that the forecast block task is deleted.
       *
       */
      @Test
      public void itDeletesForecastBlockWhenBlockChainIsObsoleted() throws Exception {

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the forecast block task is deleted.
         assertFalse( iForecastBlock.isValid() );
      }


      /**
       *
       * Verify that the active requirement and jic that were previously in the first block are
       * still active but the requirement has no parent task.
       *
       */
      @Test
      public void itOrphansReqInFirstBlockWhenBlockChainIsObsoleted() throws Exception {

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the active requirement and jic that were previously in the first block are still
         // active but the requirement has no parent task.
         assertEquals( ACTV, getStatus( iActvReq1 ) );
         assertEquals( null, getParent( iActvReq1 ) );
         assertEquals( ACTV, getStatus( iActvJic1 ) );
         assertEquals( iActvReq1, getParent( iActvJic1 ) );
      }


      /**
       *
       * Verify that the active requirement and jic that were previously in the second block are
       * still active but the requirement has no parent task.
       *
       */
      @Test
      public void itOrphansReqInSecondBlockWhenBlockChainIsObsoleted() throws Exception {

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the active requirement and jic that were previously in the second block are still
         // active but the requirement has no parent task.
         assertEquals( ACTV, getStatus( iActvReq2 ) );
         assertEquals( null, getParent( iActvReq2 ) );
         assertEquals( ACTV, getStatus( iActvJic2 ) );
         assertEquals( iActvReq2, getParent( iActvJic2 ) );
      }


      /**
       *
       * In addition to the scenario setup; the first active block in the block chain is assigned to
       * a committed work package.
       *
       * Verify that the active block task is remains active and assigned to the work package.
       *
       */
      @Test
      public void itDoesNotChangeActvBlockWhenBlockChainIsObsoletedAndActvBlockInWp()
            throws Exception {

         // Given the first active block is assigned to a committed work package.
         assignActiveBlockToCommittedWorkPackage();

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the active block task is remains active and assigned to the work package.
         assertEquals( ACTV, getStatus( iActiveBlock ) );
         assertEquals( iCommittedWorkPackage, getParent( iActiveBlock ) );
      }


      /**
       *
       * In addition to the scenario setup; the first active block in the block chain is assigned to
       * a committed work package.
       *
       * Verify that the forecast block task is deleted.
       *
       */
      @Test
      public void itDeletesForecastBlockWhenBlockChainIsObsoletedAndActvBlockInWp()
            throws Exception {

         // Given the first active block is assigned to a committed work package.
         assignActiveBlockToCommittedWorkPackage();

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the forecast block task is deleted.
         assertFalse( iForecastBlock.isValid() );
      }


      /**
       *
       * In addition to the scenario setup; the first active block in the block chain is assigned to
       * a committed work package.
       *
       * Verify that the active requirement in the first block remains active and assigned to that
       * block. Its JIC remains active and assigned.
       *
       */
      @Test
      public void itDoesNotChangeActvReqInActvBlockWhenBlockChainIsObsoletedAndActvBlockInWp()
            throws Exception {

         // Given the first active block is assigned to a committed work package.
         assignActiveBlockToCommittedWorkPackage();

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the active requirement in the first block remains active and assigned to that
         // block. As well, the jic remains active and assigned.
         assertEquals( ACTV, getStatus( iActvReq1 ) );
         assertEquals( iActiveBlock, getParent( iActvReq1 ) );
         assertEquals( ACTV, getStatus( iActvJic1 ) );
         assertEquals( iActvReq1, getParent( iActvJic1 ) );
      }


      /**
       *
       * In addition to the scenario setup; the first active block in the block chain is assigned to
       * a committed work package.
       *
       * Verify that the active requirement in the second block remains active but no longer has a
       * parent task. Its JIC remains active and assigned.
       *
       */
      @Test
      public void itOrphansReqInSecondBlockWhenBlockChainIsObsoletedAndActvBlockInWp()
            throws Exception {

         // Given the first active block is assigned to a committed work package.
         assignActiveBlockToCommittedWorkPackage();

         // When the block chain definition is obsoleted.
         obsoleteBlockChain();

         // Then the active requirement in the second block remains active but no longer has a
         // parent
         // task. Its JIC remains active and assigned.
         assertEquals( ACTV, getStatus( iActvReq2 ) );
         assertEquals( null, getParent( iActvReq2 ) );
         assertEquals( ACTV, getStatus( iActvJic2 ) );
         assertEquals( iActvReq2, getParent( iActvJic2 ) );
      }


      @Before
      public void setup() {

         iAuthorizingHr = new HumanResourceDomainBuilder().build();
         iAuthorizingHrId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();

         // Canceling a task requires the ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP user parameter to be
         // set for the authorizing user.
         UserParametersFake lUserParmsFake = new UserParametersFake( iAuthorizingHrId, "LOGIC" );
         lUserParmsFake.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
         UserParameters.setInstance( iAuthorizingHrId, "LOGIC", lUserParmsFake );

         // Arrange the data required for all the tests.
         setupBlockChainWithReqInFirstBlockAndDifferentReqInSecondBlock();

      }


      @After
      public void teardown() {
         UserParameters.setInstance( iAuthorizingHrId, "LOGIC", null );
      }


      /**
       * Arrangement of data required to setup a recurring block chain with a recurring requirement
       * starting in the first block and another recurring requirement starting in the second block.
       * All the requirements have job cards. The block chain is currently in-revision.
       *
       */
      private void setupBlockChainWithReqInFirstBlockAndDifferentReqInSecondBlock() {

         //
         // Set up the baseline.
         //

         // Given an aircraft assembly.
         final AssemblyKey lAcftAssy = Domain.createAircraftAssembly();

         // Given an activated recurring requirement with a scheduling rule and an assigned job
         // card.
         final ConfigSlotKey lAcftRootConfigSlot =
               EqpAssmblBom.findByPrimaryKey( new ConfigSlotKey( lAcftAssy, 0 ) ).getPk();

         final TaskTaskKey lJicDefn1 = Domain.createJobCardDefinition();

         final TaskTaskKey lReqDefn1 = Domain
               .createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

                  @Override
                  public void configure( RequirementDefinition aReqDefn ) {
                     aReqDefn.setCode( REQ_1_CODE );
                     aReqDefn.againstConfigurationSlot( lAcftRootConfigSlot );
                     aReqDefn.setOnCondition( false );
                     aReqDefn.setRecurring( true );
                     aReqDefn.setFleetApproval( true );
                     aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                     aReqDefn.setMinimumForecastRange( MINIMUM_FORECAST_RANGE );
                     aReqDefn.addRecurringSchedulingRule( CDY, REQ_1_CDY_INTERVAL );
                     aReqDefn.addJobCardDefinition( lJicDefn1 );
                  }
               } );

         // Given another activated recurring requirement with a scheduling rule (same parameter but
         // different interval) and an assigned different job card.
         final TaskTaskKey lJicDefn2 = Domain.createJobCardDefinition();

         final TaskTaskKey lReqDefn2 = Domain
               .createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

                  @Override
                  public void configure( RequirementDefinition aReqDefn ) {
                     aReqDefn.setCode( REQ_2_CODE );
                     aReqDefn.againstConfigurationSlot( lAcftRootConfigSlot );
                     aReqDefn.setOnCondition( false );
                     aReqDefn.setRecurring( true );
                     aReqDefn.setFleetApproval( true );
                     aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                     aReqDefn.setMinimumForecastRange( MINIMUM_FORECAST_RANGE );
                     aReqDefn.addRecurringSchedulingRule( CDY, REQ_2_CDY_INTERVAL );
                     aReqDefn.addJobCardDefinition( lJicDefn2 );
                  }
               } );

         // Given an superseded block chain with 2 blocks, with a scheduling rule matching the first
         // requirement. The first requirement is assigned to the first block and the second
         // requirement is assigned to the second block.
         final Map<Integer, TaskTaskKey> lSupersededBlockChain =
               Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

                  @Override
                  public void configure( BlockChainDefinition aBlockChainDefn ) {
                     aBlockChainDefn.setName( BLOCK_CHAIN_NAME );
                     aBlockChainDefn.setConfigurationSlot( lAcftRootConfigSlot );
                     aBlockChainDefn.setOnCondition( false );
                     aBlockChainDefn.setRecurring( true );
                     aBlockChainDefn.setMinimumForecastRange( MINIMUM_FORECAST_RANGE );
                     aBlockChainDefn.addRecurringSchedulingRule( CDY, REQ_1_CDY_INTERVAL );

                     aBlockChainDefn.addBlock( BLOCK_1, BLOCK_1_CODE, BLOCK_1_NAME );
                     aBlockChainDefn.addBlock( BLOCK_2, BLOCK_2_CODE, BLOCK_2_NAME );

                     aBlockChainDefn.addRequirement( lReqDefn1, STARTING_BLOCK_ONE, INTERVAL_ONE );
                     aBlockChainDefn.addRequirement( lReqDefn2, STARTING_BLOCK_TWO, INTERVAL_TWO );

                     // First revision that is activated.
                     aBlockChainDefn.setRevisionNumber( REVISION_1 );
                     aBlockChainDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
                  }
               } );

         // Given an in-revision version of the same block chain.
         iInRevisionBlockChain =
               Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

                  @Override
                  public void configure( BlockChainDefinition aBlockChainDefn ) {

                     aBlockChainDefn.setName( BLOCK_CHAIN_NAME );
                     aBlockChainDefn.setConfigurationSlot( lAcftRootConfigSlot );
                     aBlockChainDefn.setOnCondition( false );
                     aBlockChainDefn.setRecurring( true );
                     aBlockChainDefn.setMinimumForecastRange( MINIMUM_FORECAST_RANGE );
                     aBlockChainDefn.addRecurringSchedulingRule( CDY, REQ_1_CDY_INTERVAL );

                     // Ensure the blocks are provided a "previous" block from the superseded block
                     // chain.
                     aBlockChainDefn.addBlock( BLOCK_1, BLOCK_1_CODE, BLOCK_1_NAME,
                           lSupersededBlockChain.get( BLOCK_1 ) );
                     aBlockChainDefn.addBlock( BLOCK_2, BLOCK_2_CODE, BLOCK_2_NAME,
                           lSupersededBlockChain.get( BLOCK_2 ) );

                     aBlockChainDefn.addRequirement( lReqDefn1, STARTING_BLOCK_ONE, INTERVAL_ONE );
                     aBlockChainDefn.addRequirement( lReqDefn2, STARTING_BLOCK_TWO, INTERVAL_TWO );

                     // Second revision that is in-revision.
                     aBlockChainDefn.setRevisionNumber( REVISION_2 );
                     aBlockChainDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  }
               } );

         //
         // Set up the actuals.
         //

         // Given an aircraft based on the aircraft assembly.
         iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aAircraft ) {
               aAircraft.setAssembly( lAcftAssy );
               aAircraft.allowSynchronization();
            }
         } );

         // Given an active JIC and active requirement task based on the first requirement/jic.
         // definitions
         iActvJic1 = Domain.createJobCard( new DomainConfiguration<JobCard>() {

            @Override
            public void configure( JobCard aJobCard ) {
               aJobCard.setDefinition( lJicDefn1 );
            }
         } );

         iActvReq1 = Domain.createRequirement( new DomainConfiguration<Requirement>() {

            @Override
            public void configure( Requirement aReq ) {
               aReq.setDefinition( lReqDefn1 );
               aReq.setInventory( iAircraft );
               aReq.setStatus( RefEventStatusKey.ACTV );
               aReq.addJobCard( iActvJic1 );
            }
         } );

         // Given a forecast JIC and forecast requirement task based on the first requirement/jic.
         iForecastJic1 = Domain.createJobCard( new DomainConfiguration<JobCard>() {

            @Override
            public void configure( JobCard aJobCard ) {
               aJobCard.setDefinition( lJicDefn1 );
               aJobCard.setStatus( RefEventStatusKey.FORECAST );
            }
         } );

         iForecastReq1 = Domain.createRequirement( new DomainConfiguration<Requirement>() {

            @Override
            public void configure( Requirement aReq ) {
               aReq.setDefinition( lReqDefn1 );
               aReq.setInventory( iAircraft );
               aReq.setStatus( RefEventStatusKey.FORECAST );
               aReq.addJobCard( iForecastJic1 );
            }
         } );

         // Given an active JIC and active requirement task based on the second requirement/jic.
         // definitions
         iActvJic2 = Domain.createJobCard( new DomainConfiguration<JobCard>() {

            @Override
            public void configure( JobCard aJobCard ) {
               aJobCard.setDefinition( lJicDefn1 );
            }
         } );

         iActvReq2 = Domain.createRequirement( new DomainConfiguration<Requirement>() {

            @Override
            public void configure( Requirement aReq ) {
               aReq.setDefinition( lReqDefn2 );
               aReq.setInventory( iAircraft );
               aReq.setStatus( RefEventStatusKey.ACTV );
               aReq.addJobCard( iActvJic2 );
            }
         } );

         // Given an active first block of the block chain with the active first requirement
         // assigned
         // to it.
         iActiveBlock = Domain.createBlock( new DomainConfiguration<Block>() {

            @Override
            public void configure( Block aBlock ) {
               aBlock.setDefinition( lSupersededBlockChain.get( BLOCK_1 ) );
               aBlock.setInventory( iAircraft );
               aBlock.setStatus( ACTV );
               aBlock.addRequirement( iActvReq1 );
            }
         } );

         // Given a forecast second block of the block chain with the forecast first requirement
         // assigned
         // to it and with the active second requirement assigned to it.
         iForecastBlock = Domain.createBlock( new DomainConfiguration<Block>() {

            @Override
            public void configure( Block aBlock ) {
               aBlock.setDefinition( lSupersededBlockChain.get( BLOCK_2 ) );
               aBlock.setInventory( iAircraft );
               aBlock.setStatus( RefEventStatusKey.FORECAST );
               aBlock.setPreviousBlock( iActiveBlock );
               aBlock.addRequirement( iForecastReq1 );
               aBlock.addRequirement( iActvReq2 );
            };
         } );

         // Ensure the status and parents of the tasks are as expected.
         assertEquals( ACTV, getStatus( iActvReq1 ) );
         assertEquals( iActiveBlock, getParent( iActvReq1 ) );
         assertEquals( ACTV, getStatus( iActvJic1 ) );
         assertEquals( iActvReq1, getParent( iActvJic1 ) );

         assertEquals( FORECAST, getStatus( iForecastReq1 ) );
         assertEquals( iForecastBlock, getParent( iForecastReq1 ) );
         assertEquals( FORECAST, getStatus( iForecastJic1 ) );
         assertEquals( iForecastReq1, getParent( iForecastJic1 ) );

         assertEquals( ACTV, getStatus( iActvReq2 ) );
         assertEquals( iForecastBlock, getParent( iActvReq2 ) );
         assertEquals( ACTV, getStatus( iActvJic2 ) );
         assertEquals( iActvReq2, getParent( iActvJic2 ) );

         assertEquals( ACTV, getStatus( iActiveBlock ) );
         assertEquals( FORECAST, getStatus( iForecastBlock ) );
      }


      /**
       *
       * Create a committed work package and assign the first active block to it.
       *
       */
      private void assignActiveBlockToCommittedWorkPackage() {

         iCommittedWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

            @Override
            public void configure( WorkPackage aWp ) {
               aWp.setAircraft( iAircraft );
               aWp.setStatus( COMMIT );
               aWp.addTask( iActiveBlock );
            }
         } );

         // Ensure the active block is assigned to the work package.
         assertEquals( iCommittedWorkPackage, getParent( iActiveBlock ) );

      }


      /**
       * Obsoletes the in-revision block chain.
       *
       */
      private void obsoleteBlockChain() throws Exception {

         // Pick one of the block tasks in the in-revision block chain (obsoleting one will obsolete
         // all in the chain).
         new TaskDefnBean().obsolete( iInRevisionBlockChain.get( 1 ), new TaskDefnRevTO(),
               iAuthorizingHr );

         // Run baseline sync in order to manage the various tasks as a result of the block chain
         // being
         // obsoleted.
         InventoryTask lInvSyncTask =
               new InventoryTaskBuilder().inventory( iAircraft ).hInventory( iAircraft ).build();

         WorkItemGeneratorExecuteImmediateFake lWorkItemGenFake =
               new WorkItemGeneratorExecuteImmediateFake( iAuthorizingHr );
         InventorySynchronizer lInvSynchronizer = new InventorySynchronizer( lWorkItemGenFake );

         lInvSynchronizer.processInventory( Arrays.asList( lInvSyncTask ) );
      }


      private RefEventStatusKey getStatus( TaskKey aTask ) {
         return EvtEventTable.findByPrimaryKey( aTask ).getEventStatus();
      }


      private TaskKey getParent( TaskKey aTask ) {
         EventKey lParentEvent = EvtEventTable.findByPrimaryKey( aTask ).getNhEvent();
         return ( lParentEvent == null ) ? null : new TaskKey( lParentEvent );
      }
   }
}
