
package com.mxi.mx.core.unittest.stask.labour.autoissue;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.ScheduledInstallLabourBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.stask.workcapture.autoissue.BatchAutoIssueInventoryHandler;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.fnc.FncXactionLog;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.sched.SchedInstPartTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.req.ReqPart;


/**
 * Test the BatchAutoIssueInventoryHandler.process when the system is under auto-issue mode
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class BatchAutoIssueInventoryHandlerTest {

   private BatchAutoIssueInventoryHandler iBatchAutoIssueInventory;
   private InventoryKey iAcft;
   private InventoryKey iBATCHInventoryA;
   private InventoryKey iBATCHInventoryB;
   private PartNoKey iBATCHPartA;
   private PartNoKey iBATCHPartB;
   private HumanResourceKey iHr;
   private LocationKey iMaintenanceLocation;
   private OwnerKey iOwnerKey;
   private PartRequestKey iPartRequestKey;
   private LocationKey iSupplyLocation;
   private TaskKey iTask;
   private TaskPartKey iTaskPartKey;
   private TaskKey iWorkPackage;
   private TaskInstPartKey iTaskInstPartKey;
   private FncAccountKey iAcftAccount;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test when a batch part is entered as installed with no serial number
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchBlankSerialNumber() throws MxException, TriggerException {

      withBlankSerialNumber();

      withLocallyReservedPartRequest();

      // assert preconditions
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      InvInv lInvInvB = new InvInv( iBATCHInventoryB );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results Inventory A is not issued. Inventory B is not issued. Part request
       * is PRAWAITISSUE.
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvB = new InvInv( iBATCHInventoryB );

      lInvInvA.assertIssuedBoolean( false );
      lInvInvB.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // cleared part request
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRAWAITISSUE );
   }


   /**
    * This scenario tests installing a part that is the same as the one issued in the part request
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchInstalledAlreadyIssued() throws MxException, TriggerException {

      withIssuedPartRequest();

      // assert preconditions
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvA.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results Inventory A is not issued. Part request is PRISSUED.
       */
      lInvInvA = new InvInv( iBATCHInventoryA );

      // no issue transaction qty remains the same
      lInvInvA.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );

      // part request remains the same
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( iBATCHInventoryA );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRISSUED );
   }


   /**
    * This scenario tests installing a part that is not reserved
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchInstalledNotReserved() throws MxException, TriggerException {

      withOpenPartRequest();

      // assert preconditions
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      InvInv lInvInvB = new InvInv( iBATCHInventoryB );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results Inventory A is not issued. Inventory B is not issued. Part request
       * is PRAWAITISSUE.
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvB = new InvInv( iBATCHInventoryB );

      // no issue transaction qty remains the same
      lInvInvA.assertIssuedBoolean( false );
      lInvInvB.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // part request cleared up
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRAWAITISSUE );
   }


   /**
    * This scenario tests the case of installing a batch part that does not exist
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchInstallNotExists() throws MxException, TriggerException {

      withInexistentInventory();

      withLocallyReservedPartRequest();

      // assert preconditions
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      InvInv lInvInvB = new InvInv( iBATCHInventoryB );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results Inventory A is not issued. Inventory B is not issued. Part request
       * is PRAWAITISSUE.
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvB = new InvInv( iBATCHInventoryB );

      // no issue transaction qty remains the same
      lInvInvA.assertIssuedBoolean( false );
      lInvInvB.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // part request cleared up
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRAWAITISSUE );
   }


   /**
    * This scenario tests installing a part that is not reserved
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchLocallyReservedInstalledDifferent() throws MxException, TriggerException {

      withInstalledDifferentAsReserved();

      withLocallyReservedPartRequest();

      // assert preconditions
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      InvInv lInvInvB = new InvInv( iBATCHInventoryB );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results Inventory A is not issued. Inventory B is not issued. Part request
       * is PRAWAITISSUE.
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvB = new InvInv( iBATCHInventoryB );

      // no issue transaction then qty remains the same
      lInvInvA.assertIssuedBoolean( false );
      lInvInvB.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // part request cleared up
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRAWAITISSUE );
   }


   /**
    * This scenario tests installing the same reserved part
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchLocallyReservedInstalledSame() throws MxException, TriggerException {

      withInstalledSameAsReserved();

      withLocallyReservedPartRequest();

      // check pre-condition of batch
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvA.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results Record a financial issue type transaction PR is cancelled
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvA.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 9.0 );

      // part request is cleared up
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRCANCEL );
   }


   /**
    * This scenario tests installing a different a part than the reserved. In this case, neither
    * inventory is issued, and Part Request is PRAWAITISSUE
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchRemotellyReservedInstalledDifferent() throws MxException, TriggerException {

      withInstalledDifferentAsReserved();

      withRemotellyReservedPartRequest();

      // assert preconditions
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      InvInv lInvInvB = new InvInv( iBATCHInventoryB );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvB = new InvInv( iBATCHInventoryB );

      // Inventory is not issued and qty remains the same
      lInvInvA.assertIssuedBoolean( false );
      lInvInvB.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );
      lInvInvB.assertBinQuantity( 10.0 );

      // part request cleared up
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRAWAITISSUE );
   }


   /**
    * This scenario tests installing the reserved part In this case, the Part Request is CANCELLED
    * and an ISSUE type financial transaction is logged.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testBatchRemotellyReservedInstalledSame() throws MxException, TriggerException {

      withInstalledSameAsReserved();

      withRemotellyReservedPartRequest();

      // check pre-condition of batch
      InvInv lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvA.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 10.0 );

      // call test method
      iBatchAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      /*
       * Verify expected results
       */
      lInvInvA = new InvInv( iBATCHInventoryA );
      lInvInvA.assertIssuedBoolean( false );
      lInvInvA.assertBinQuantity( 9.0 );

      // part request is cleared up
      ReqPart lReqPart = new ReqPart( iPartRequestKey );
      lReqPart.assertInventoryKey( null );

      EvtEventUtil lEvtEvent = new EvtEventUtil( iPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRCANCEL );

      // Because iBATCHPartA financial type is CONSUMABLE, a financial issue type transaction is
      // recorded. event_id is null, but sched_id is populated with the originating task.
      FncXactionLogKey lXactionLogKey =
            AutoIssueInventoryHandlerTestHelper.getLatestTransactionByPart( iBATCHPartA, 3 );
      Assert.assertNotNull( "No transaction for Part A", lXactionLogKey );

      FncXactionLog lXactionLog = FncXactionLog.findByPrimaryKey( lXactionLogKey );
      Assert.assertNull( "No event expected for Auto-issued part A", lXactionLog.getEvent() );

      Assert.assertEquals( "Unexpected associated Task for transaction", iTask,
            lXactionLog.getTask() );
   }


   @Before
   public void loadData() throws Exception {
      iBatchAutoIssueInventory = new BatchAutoIssueInventoryHandler();

      iHr = new HumanResourceDomainBuilder().build();

      iSupplyLocation = new LocationDomainBuilder().build();
      iMaintenanceLocation = new LocationDomainBuilder().withSupplyLocation( iSupplyLocation )
            .withType( RefLocTypeKey.LINE ).build();

      iOwnerKey = new OwnerDomainBuilder().build();

      // create an aircraft with issue to account which is mandatory
      iAcft = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).build();

      iAcftAccount = new AccountBuilder().withCode( "ACFT_ACCOUNT" )
            .withType( RefAccountTypeKey.EXPENSE ).build();
      AircraftKey lAcftKey = new AircraftKey( iAcft );
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( lAcftKey );
      lInvAcReg.setIssueToAccount( iAcftAccount );

      iWorkPackage =
            new TaskBuilder().atLocation( iMaintenanceLocation ).onInventory( iAcft ).build();
      iTask = new TaskBuilder().withParentTask( iWorkPackage ).build();

      buildTestData();
   }


   /**
    * Set up batch parts and inventory
    */
   private void buildTestData() {
      iBATCHPartA = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withShortDescription( "BATCHA" ).withAbcClass( RefAbcClassKey.A )
            .withFinancialType( RefFinanceTypeKey.CONSUM )
            .withAverageUnitPrice( new BigDecimal( 1.0 ) )
            .withTotalQuantity( new BigDecimal( 1.0 ) ).withTotalValue( new BigDecimal( 1.0 ) )
            .build();

      iBATCHPartB = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withShortDescription( "BATCHB" ).withAbcClass( RefAbcClassKey.A )
            .withFinancialType( RefFinanceTypeKey.EXPENSE )
            .withAverageUnitPrice( new BigDecimal( 1.0 ) )
            .withTotalQuantity( new BigDecimal( 1.0 ) ).withTotalValue( new BigDecimal( 1.0 ) )
            .build();

      iBATCHInventoryA = new InventoryBuilder().withPartNo( iBATCHPartA ).withSerialNo( "batchA" )
            .withClass( RefInvClassKey.BATCH ).atLocation( iMaintenanceLocation )
            .withOwner( iOwnerKey ).withBinQt( 10.0 ).withReserveQt( 0.0 )
            .withCondition( RefInvCondKey.RFI ).build();

      iBATCHInventoryB = new InventoryBuilder().withPartNo( iBATCHPartB ).withSerialNo( "batchB" )
            .withClass( RefInvClassKey.BATCH ).atLocation( iMaintenanceLocation )
            .withOwner( iOwnerKey ).withBinQt( 10.0 ).withReserveQt( 0.0 )
            .withCondition( RefInvCondKey.RFI ).build();
   }


   /**
    * Batch install has blank serial number
    */
   private void withBlankSerialNumber() {

      withInstalledDifferentAsReserved();

      SchedInstPartTable lSchedInstPartTable =
            SchedInstPartTable.findByPrimaryKey( iTaskInstPartKey );
      lSchedInstPartTable.setInventory( null );
      lSchedInstPartTable.setSerialNoOem( null );
      lSchedInstPartTable.update();
   }


   /**
    * Batch install does not exists
    */
   private void withInexistentInventory() {

      withInstalledDifferentAsReserved();

      SchedInstPartTable lSchedInstPartTable =
            SchedInstPartTable.findByPrimaryKey( iTaskInstPartKey );
      lSchedInstPartTable.setInventory( null );
      lSchedInstPartTable.update();
   }


   /**
    * Installed part is different than reserved
    */
   private void withInstalledDifferentAsReserved() {

      // part requirement installed A
      iTaskPartKey = new PartRequirementDomainBuilder( iTask ).withInstallPart( iBATCHPartA )
            .withInstallQuantity( 1.0 ).withInstallSerialNumber( "batchA" )
            .withInstallInventory( iBATCHInventoryA ).build();

      iTaskInstPartKey = new TaskInstPartKey( iTaskPartKey, 1 );

      // part request reserved inventory B remotelly
      iPartRequestKey = new PartRequestBuilder().forPartRequirement( iTaskInstPartKey )
            .withType( RefReqTypeKey.TASK ).withReservedInventory( iBATCHInventoryB )
            .withRequestedQuantity( 1.0 ).isNeededAt( iMaintenanceLocation ).build();

      // create a labour on the part requirement
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      SchedLabourKey lSchedLabourKey = lSchedLabourTable.insert();

      new ScheduledInstallLabourBuilder().forPartRequirement( iTaskInstPartKey )
            .forLabour( lSchedLabourKey ).build();
   }


   /**
    * Installed part is what was reserved
    */
   private void withInstalledSameAsReserved() {

      // create a part requirement with install of inventory A
      iTaskPartKey = new PartRequirementDomainBuilder( iTask ).withInstallPart( iBATCHPartA )
            .withInstallQuantity( 1.0 ).withInstallInventory( iBATCHInventoryA )
            .withInstallSerialNumber( "batchA" ).build();

      iTaskInstPartKey = new TaskInstPartKey( iTaskPartKey, 1 );

      // create a part request with reserved inventory A (locally)
      iPartRequestKey = new PartRequestBuilder().forPartRequirement( iTaskInstPartKey )
            .withType( RefReqTypeKey.TASK ).withReservedInventory( iBATCHInventoryA )
            .withRequestedQuantity( 1.0 ).isNeededAt( iMaintenanceLocation ).build();

      // create a labour on the part requirement
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      SchedLabourKey lSchedLabourKey = lSchedLabourTable.insert();

      new ScheduledInstallLabourBuilder().forPartRequirement( iTaskInstPartKey )
            .forLabour( lSchedLabourKey ).build();
   }


   /**
    * Part Request is ISSUED
    */
   private void withIssuedPartRequest() {

      withInstalledSameAsReserved();

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRISSUED );
      lEvtEventTable.update();
   }


   /**
    * Part Request is PRAVAIL
    */
   private void withLocallyReservedPartRequest() {

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRAVAIL );
      lEvtEventTable.update();
   }


   /**
    * Part request is PROPEN
    */
   private void withOpenPartRequest() {

      withInstalledSameAsReserved();

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PROPEN );
      lEvtEventTable.update();

      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iPartRequestKey );
      lReqPartTable.setInventory( null );
      lReqPartTable.update();
   }


   /**
    * Part request is PRREMOTE
    */
   private void withRemotellyReservedPartRequest() {

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRREMOTE );
      lEvtEventTable.update();
   }

}
