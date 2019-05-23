
package com.mxi.mx.core.services.stask.complete.completeservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.misc.TransactionHandler;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.TaskErrorHolder;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.service.message.notification.MessageType;


@RunWith( BlockJUnit4ClassRunner.class )
public class BatchCompleteAllTasksTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Object under test
   private CompleteService iCompleteService;

   private InventoryKey iAircraft;
   private HumanResourceKey iHr;
   private TransactionHandler iTxHandler;
   private TaskKey iWorkPackage;


   @Before
   public void setUp() {
      iAircraft = Domain.createAircraft();
      iWorkPackage = createWorkPackage();
      iCompleteService = new CompleteService( iWorkPackage );
      iTxHandler = new TransactionHandler( new SessionContextFake().getUserTransaction() );
      iHr = new HumanResourceDomainBuilder().build();
      UserParameters.setInstance( iHr.getId(), "LOGIC",
            new UserParametersFake( iHr.getId(), "LOGIC" ) );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );
   }


   @Test
   public void testBatchCompletion() throws Throwable {

      // Set up a work package with 2 JICs, 1 adhoc task and a deferred fault
      TaskKey lJIC1 = createTaskInWorkPackage( iWorkPackage, RefTaskClassKey.JIC, null );
      TaskKey lJIC2 = createTaskInWorkPackage( iWorkPackage, RefTaskClassKey.JIC, null );
      TaskKey lParentTask = createTaskInWorkPackage( iWorkPackage, RefTaskClassKey.ADHOC, null );
      TaskKey lSubtask =
            createSubTaskInWorkPackage( iWorkPackage, lParentTask, RefTaskClassKey.ADHOC );
      TaskKey lCorrectiveTask = createTaskInWorkPackage( iWorkPackage, RefTaskClassKey.CORR, null );
      FaultKey lDeferredFault =
            createFaultWithCorrectiveTask( lCorrectiveTask, RefEventStatusKey.CFDEFER );

      iCompleteService.batchCompleteAll( new Date(), iHr, iTxHandler );

      assertComplete( lJIC1 );
      assertComplete( lParentTask );
      assertComplete( lSubtask );
      assertComplete( lCorrectiveTask );
      assertComplete( lJIC2 );

      // The fault should now be certified
      assertEquals( RefEventStatusKey.CFCERT, EvtEventTable
            .findByPrimaryKey( new EventKey( lDeferredFault.toString() ) ).getEventStatus() );
   }


   @Test
   public void testBatchCompletion_taskWithPartReq_noSerialError() throws Throwable {
      // Set up a work package with 1 task
      PartNoKey lPartNo = createPart( RefInvClassKey.TRK );
      TaskKey lTaskWithPartReq =
            createTaskInWorkPackage( iWorkPackage, RefTaskClassKey.ADHOC, lPartNo );

      // Add a part requirement to the task
      createPartRequirementForTask( lTaskWithPartReq, lPartNo );

      List<TaskErrorHolder> lBatchCompleteErrors =
            iCompleteService.batchCompleteAll( new Date(), iHr, iTxHandler );

      assertTrue( !lBatchCompleteErrors.isEmpty() );
      assertNotComplete( lTaskWithPartReq );

   }


   @Test
   public void testBatchCompletion_taskWithPartReq_installedItemDoesNotExistError()
         throws Throwable {
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC.name() ).setString( "TOBE_INST_NOTFOUND",
            MessageType.ERROR.name() );

      // Set up a work package with 1 task
      PartNoKey lPartNo = createPart( RefInvClassKey.TRK );
      TaskKey lTaskWithPartReq =
            createTaskInWorkPackage( iWorkPackage, RefTaskClassKey.ADHOC, lPartNo );

      // Add a part requirement to the task
      createPartRequirementForTaskWithNonExistingInventory( lTaskWithPartReq, lPartNo );

      List<TaskErrorHolder> lBatchCompleteErrors =
            iCompleteService.batchCompleteAll( new Date(), iHr, iTxHandler );

      assertTrue( !lBatchCompleteErrors.isEmpty() );
      assertNotComplete( lTaskWithPartReq );

   }


   private void assertComplete( TaskKey aTask ) {
      assertEquals( RefEventStatusKey.COMPLETE,
            EvtEventTable.findByPrimaryKey( new EventKey( aTask.toString() ) ).getEventStatus() );
   }


   private void assertNotComplete( TaskKey aTask ) {
      assertNotEquals( RefEventStatusKey.COMPLETE,
            EvtEventTable.findByPrimaryKey( new EventKey( aTask.toString() ) ).getEventStatus() );
   }


   private PartNoKey createPart( final RefInvClassKey aInvClass ) {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( aInvClass );
            aPart.setPartStatus( RefPartStatusKey.ACTV );
            aPart.setShortDescription( "PART-1" );
         }
      } );
   }


   private TaskKey createWorkPackage() {
      return Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.setAircraft( iAircraft );
         }
      } );
   }


   private TaskKey createTaskInWorkPackage( TaskKey aWorkPackage, RefTaskClassKey aTaskClass,
         PartNoKey aPartNo ) {
      return new TaskBuilder().withParentTask( aWorkPackage ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV )
            .onInventory( new InventoryBuilder().withClass( RefInvClassKey.TRK )
                  .withParentInventory( iAircraft ).withPartNo( aPartNo ).build() )
            .build();
   }


   private TaskKey createSubTaskInWorkPackage( TaskKey aWorkPackage, TaskKey aParentTask,
         RefTaskClassKey aTaskClass ) {
      return new TaskBuilder().withParentTask( aParentTask ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV ).build();
   }


   private FaultKey createFaultWithCorrectiveTask( final TaskKey aCorrectiveTask,
         final RefEventStatusKey aStatus ) {

      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setInventory( iAircraft );
            aBuilder.setFoundOnDate( new Date() );
            aBuilder.setCorrectiveTask( aCorrectiveTask );
            aBuilder.setStatus( aStatus );
         }

      } );

      SchedStaskTable.findByPrimaryKey( aCorrectiveTask ).setFault( lFault );

      return lFault;
   }


   private void createPartRequirementForTask( TaskKey aTaskWithPartReq, PartNoKey aPartNo ) {
      final PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "PARTGROUP" )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( aPartNo ).build();

      new PartRequirementDomainBuilder( aTaskWithPartReq ).forPartGroup( lPartGroupKey )
            .withInstallQuantity( 1 ).withInstallPart( aPartNo ).build();
   }


   private void createPartRequirementForTaskWithNonExistingInventory( TaskKey aTaskWithPartReq,
         PartNoKey aPartNo ) {
      final PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "PARTGROUP" )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( aPartNo ).build();

      new PartRequirementDomainBuilder( aTaskWithPartReq ).forPartGroup( lPartGroupKey )
            .withInstallQuantity( 1 ).withInstallSerialNumber( "FAKE-123" )
            .withInstallPart( aPartNo ).withRemovalSerialNo( "FAKE-456" ).build();
   }

}
