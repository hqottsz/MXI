
package com.mxi.mx.core.unittest.stask.labour.autoissue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.ScheduledInstallLabourBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefBOMClassKey;
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
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.plsql.delegates.InventoryProcedures;
import com.mxi.mx.core.services.inventory.config.AttachableInventoryService;
import com.mxi.mx.core.services.order.OrderLineUtility;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.UpdateDeadlineService;
import com.mxi.mx.core.services.stask.workcapture.autoissue.TrackedSerializedAutoIssueInventoryHandler;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.fnc.FncXactionLog;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;


/**
 * Test the TrackedSerializedAutoIssueInventoryHandler.process and
 * AttachableInventoryService.attachTrackedInventory when the system is under auto-issue mode This
 * test only inventory TRK part based
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TrackedSerializedAutoIssueInventoryHandlerTest {

   private static final int USER_ID = 999;

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "testuser" );

   private static final String DEFAULT_ACCOUNT = "Test";

   private AttachableInventoryService iEngineAAttachableInventoryService;
   private boolean iAutoIssueInventoryParameter;
   private Mockery iContext = new Mockery();
   private InventoryKey iEngineA;
   private InventoryKey iEngineB;
   private PartNoKey iEnginePart;
   private PartGroupKey iEnginePartGroup;
   private ConfigSlotPositionKey iEngineSubAssyPosition;
   private InventoryKey iEngineSystem;
   private HumanResourceKey iHr;
   private LocationKey iLocation;
   private LocationKey iMaintenanceLocation;
   private InventoryProcedures iMockInventoryProcedures;
   private SecurityIdentificationInterface iMockSecurityIdentification;
   private UpdateDeadlineService iMockUpdateDeadlineService;
   private OrderLineUtility iOrderLineUtilityMock;
   private PartRequestKey iPartRequestKey;
   private QueryAccessObject iQao = iContext.mock( QueryAccessObject.class );
   private LocationKey iSupplyLocation;
   private TaskKey iTask;
   private TaskPartKey iTaskPartKey;
   private TrackedSerializedAutoIssueInventoryHandler iTrackedSerializedAutoIssueInventory;
   private TaskKey iWorkPackage;
   private FncAccountKey iAcftAccount;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Sets up the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void loadData() throws Exception {
      iOrderLineUtilityMock = iContext.mock( OrderLineUtility.class );

      iMockUpdateDeadlineService = iContext.mock( UpdateDeadlineService.class );

      iMockSecurityIdentification = iContext.mock( SecurityIdentificationInterface.class );
      SecurityIdentificationUtils.setInstance( iMockSecurityIdentification );

      iMockInventoryProcedures = iContext.mock( InventoryProcedures.class );
      StoredProcedureCall.getInstance().setDelegate( iMockInventoryProcedures );

      iTrackedSerializedAutoIssueInventory = new TrackedSerializedAutoIssueInventoryHandler();

      buildTestData();

      // create the service class instance
      iEngineAAttachableInventoryService = new AttachableInventoryService( iEngineA,
            iOrderLineUtilityMock, iMockUpdateDeadlineService );

      iAutoIssueInventoryParameter = true;
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC ).setBoolean( "AUTO_ISSUE_INVENTORY",
            iAutoIssueInventoryParameter );
   }


   /**
    * If there is no reservation, and an inventory is installed, auto-issue that inventory
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testTRKInstalledNotReserved() throws MxException, TriggerException {

      withRFIInventoryCondition();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      assertNull( InvInvTable.findByPrimaryKey( iEngineA ).getNhInvNo() );

      // call auto issue class
      iTrackedSerializedAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      // Attach Engine A, which was reserved
      iEngineAAttachableInventoryService.attachTrackedInventory( iEngineSystem,
            iEngineSubAssyPosition, iEnginePartGroup, iTask, iHr, false, null, null, true, false,
            false, iAutoIssueInventoryParameter, false );

      assertPartRequestCancelled();

      assertInventoryInstalled();

      // assert part total quantity has decremented on one unit after installing
      assertPartTotalQuantity( lPartQtyPriorInstall.subtract( BigDecimal.ONE ) );

      assertAutoIssueTransactionRecorded();

   }


   /**
    * If an inventory is issued for the part requirement, its status will remain PRISSUED after
    * installation
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testTRKIssuedInstalledSame() throws MxException, TriggerException {

      withEngineAReserved();

      withAlreadyIssuedPartRequest();

      withRFIInventoryCondition();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      assertNull( InvInvTable.findByPrimaryKey( iEngineA ).getNhInvNo() );
      assertTrue( InvInvTable.findByPrimaryKey( iEngineA ).isIssuedBool() );

      // call auto issue class
      iTrackedSerializedAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      // call the attach service
      iEngineAAttachableInventoryService.attachTrackedInventory( iEngineSystem,
            iEngineSubAssyPosition, iEnginePartGroup, iTask, iHr, false, null, null, true, false,
            false, iAutoIssueInventoryParameter, false );

      // assert part request didn't change
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iPartRequestKey );
      assertEquals( iEngineA, lReqPartTable.getInventory() );

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      assertEquals( RefEventStatusKey.PRISSUED, lEvtEventTable.getEventStatus() );

      assertInventoryInstalled();

      // assert part total quantity remains the same
      assertPartTotalQuantity( lPartQtyPriorInstall );
   }


   /**
    * If one inventory in reserved but a different inventory is installed, auto-issue the installed
    * inventory. Note that this is different than the behaviour for BATCH inventory, where the
    * inventory is not auto-issued and the Part Request state remains PRAWAITISSUE (see
    * BatchAutoIssueInventoryHandlerTest.testBatchInstalledNotReserved())
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testTRKReservedInstalledDifferent() throws MxException, TriggerException {

      withEngineBReserved();

      withRFIInventoryCondition();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      assertNull( InvInvTable.findByPrimaryKey( iEngineA ).getNhInvNo() );

      // call auto issue class
      iTrackedSerializedAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      // call the attach service
      iEngineAAttachableInventoryService.attachTrackedInventory( iEngineSystem,
            iEngineSubAssyPosition, iEnginePartGroup, iTask, iHr, false, null, null, true, false,
            false, iAutoIssueInventoryParameter, false );

      // assert part request is cancelled and cleared up
      assertPartRequestCancelled();

      assertInventoryInstalled();

      // assert part total quantity has decremented on one unit after installing
      assertPartTotalQuantity( lPartQtyPriorInstall.subtract( BigDecimal.ONE ) );

      assertAutoIssueTransactionRecorded();
   }


   /**
    * INSPREQ Installed is the same as reserved.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testTRKReservedInstalledINSPREQSame() throws MxException, TriggerException {

      withEngineAReserved();

      withINSPREQInventoryCondition();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      assertNull( InvInvTable.findByPrimaryKey( iEngineA ).getNhInvNo() );

      // call auto issue class
      iTrackedSerializedAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      // call the attach service
      iEngineAAttachableInventoryService.attachTrackedInventory( iEngineSystem,
            iEngineSubAssyPosition, iEnginePartGroup, iTask, iHr, false, null, null, true, false,
            false, iAutoIssueInventoryParameter, false );

      // assert part request is cancelled and cleared up
      assertPartRequestCancelled();

      assertInventoryInstalled();

      // assert part total quantity has decremented on one unit after installing
      assertPartTotalQuantity( lPartQtyPriorInstall.subtract( BigDecimal.ONE ) );

      assertAutoIssueTransactionRecorded();
   }


   /**
    * REPREQ Installed is the same as reserved.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testTRKReservedInstalledREPREQSame() throws MxException, TriggerException {

      withEngineAReserved();

      withRemotellyReservedPartRequest();

      withREPREQInventoryCondition();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      assertNull( InvInvTable.findByPrimaryKey( iEngineA ).getNhInvNo() );

      // call auto issue class
      iTrackedSerializedAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      // call the attach service
      iEngineAAttachableInventoryService.attachTrackedInventory( iEngineSystem,
            iEngineSubAssyPosition, iEnginePartGroup, iTask, iHr, false, null, null, true, false,
            false, iAutoIssueInventoryParameter, false );

      assertPartRequestCancelled();

      assertInventoryInstalled();

      // assert part total quantity has decremented on one unit after installing
      assertPartTotalQuantity( lPartQtyPriorInstall.subtract( BigDecimal.ONE ) );

      assertAutoIssueTransactionRecorded();
   }


   /**
    * RFI Installed is the same as reserved.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testTRKReservedInstalledRFISame() throws MxException, TriggerException {

      withEngineAReserved();

      withRFIInventoryCondition();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );
      BigDecimal lPartQtyPriorInstall = lEqpPartNoTable.getTotalQt();

      // assert pre-conditions
      // total part quantity prior to perform the install
      assertEquals( lPartQtyPriorInstall, new BigDecimal( 2 ) );

      // assert inventory is now detached
      assertNull( InvInvTable.findByPrimaryKey( iEngineA ).getNhInvNo() );

      // call auto issue class
      iTrackedSerializedAutoIssueInventory.process( iPartRequestKey, iHr, iTask );

      // call the attach service
      iEngineAAttachableInventoryService.attachTrackedInventory( iEngineSystem,
            iEngineSubAssyPosition, iEnginePartGroup, iTask, iHr, false, null, null, true, false,
            false, iAutoIssueInventoryParameter, false );

      assertPartRequestCancelled();

      assertInventoryInstalled();

      // assert part total quantity has decremented on one unit after installing
      assertPartTotalQuantity( lPartQtyPriorInstall.subtract( BigDecimal.ONE ) );
   }


   /**
    * Sets up the test case data.
    */
   private void buildTestData() {

      // create a human resource
      iHr = new HumanResourceDomainBuilder().build();

      // create an inventory owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // create a location
      iLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).build();

      // create a part
      iEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withFinancialType( RefFinanceTypeKey.CONSUM )
            .withAverageUnitPrice( new BigDecimal( 11 ) ).withTotalQuantity( new BigDecimal( 2 ) )
            .withTotalValue( new BigDecimal( 22 ) ).withShortDescription( "Part A" ).build();

      // create the aircraft assembly
      AssemblyKey lAcftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey lAcftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( lAcftAssembly ).build();
      ConfigSlotPositionKey lAcftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lAcftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey lEngineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( lAcftAssembly )
                  .withParent( lAcftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey lEngineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( lEngineSystemSlot ).withParentPosition( lAcftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey lEngineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( lEngineSystemSlot ).withRootAssembly( lAcftAssembly ).build();

      iEngineSubAssyPosition = new ConfigSlotPositionBuilder().withConfigSlot( lEngineSubAssySlot )
            .withParentPosition( lEngineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      iEnginePartGroup = new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( lEngineSubAssySlot )
            .withPartNo( iEnginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the enigne assembly
      AssemblyKey lEngineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey lEngineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( lEngineAssembly ).build();
      ConfigSlotPositionKey lEnginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lEngineSlot ).build();

      // create the aircraft
      InventoryKey lAcft = new InventoryBuilder().withConfigSlotPosition( lAcftPosition )
            .withClass( RefInvClassKey.ACFT ).build();
      iAcftAccount = new AccountBuilder().withCode( DEFAULT_ACCOUNT )
            .withType( RefAccountTypeKey.EXPENSE ).build();
      AircraftKey lAcftKey = new AircraftKey( lAcft );
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( lAcftKey );
      lInvAcReg.setIssueToAccount( iAcftAccount );

      // create the engine system
      iEngineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( lAcft ).withAssemblyInventory( lAcft )
            .withConfigSlotPosition( lEngineSystemPosition ).build();

      // create the engine inventory
      iEngineA = new InventoryBuilder().withPartNo( iEnginePart )
            .withConfigSlotPosition( lEnginePosition ).withClass( RefInvClassKey.ASSY )
            .withOwner( lOwner ).atLocation( iLocation ).build();

      // create the engine inventory
      iEngineB = new InventoryBuilder().withPartNo( iEnginePart )
            .withConfigSlotPosition( lEnginePosition ).withClass( RefInvClassKey.ASSY )
            .withOwner( lOwner ).atLocation( iLocation ).build();

      iSupplyLocation = new LocationDomainBuilder().build();

      iMaintenanceLocation = new LocationDomainBuilder().withSupplyLocation( iSupplyLocation )
            .withType( RefLocTypeKey.LINE ).build();

      // create workpackage and task
      iWorkPackage =
            new TaskBuilder().atLocation( iMaintenanceLocation ).onInventory( lAcft ).build();

      iTask = new TaskBuilder().withParentTask( iWorkPackage ).build();

      // create a part requirement
      iTaskPartKey = new PartRequirementDomainBuilder( iTask ).withInstallPart( iEnginePart )
            .withInstallQuantity( 1.0 ).withInstallInventory( iEngineA )
            .withInstallSerialNumber( "trackA" ).build();

      // create a part request with reserved part
      iPartRequestKey =
            new PartRequestBuilder().forPartRequirement( new TaskInstPartKey( iTaskPartKey, 1 ) )
                  .withStatus( RefEventStatusKey.PRAVAIL ).withType( RefReqTypeKey.TASK )
                  .withReservedInventory( iEngineA ).withRequestedQuantity( 1.0 )
                  .isNeededAt( iMaintenanceLocation ).build();

      // create a labour on the part requirement
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      SchedLabourKey lSchedLabourKey = lSchedLabourTable.insert();

      new ScheduledInstallLabourBuilder()
            .forPartRequirement( new TaskInstPartKey( iTaskPartKey, 1 ) )
            .forLabour( lSchedLabourKey ).build();

      iContext.checking( new Expectations() {

         {
            allowing( iMockSecurityIdentification ).getCurrentUserId();
            will( returnValue( USER_ID ) );
            ignoring( iMockUpdateDeadlineService );
            ignoring( iMockInventoryProcedures );
         }
      } );

      iContext.assertIsSatisfied();
   }


   /**
    * Inventory has INSPREQ condition
    */
   private void withINSPREQInventoryCondition() {
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iEngineA );
      lInvInvTable.setInvCond( RefInvCondKey.INSPREQ );
      lInvInvTable.update();
   }


   /**
    * EngineA is reserved locally
    */
   private void withEngineAReserved() {

      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iPartRequestKey );
      lReqPartTable.setInventory( iEngineA );
      lReqPartTable.update();

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRAVAIL );
      lEvtEventTable.update();
   }


   /**
    * EngineB is reserved locally
    */
   private void withEngineBReserved() {

      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iPartRequestKey );
      lReqPartTable.setInventory( iEngineB );
      lReqPartTable.update();

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRAVAIL );
      lEvtEventTable.update();
   }


   /**
    * Part Request is ISSUED
    */
   private void withAlreadyIssuedPartRequest() {

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRISSUED );
      lEvtEventTable.update();

      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iEngineA );
      lInvInvTable.setIssuedBool( true );
      lInvInvTable.update();
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


   /**
    * Inventory has REPREQ condition
    */
   private void withREPREQInventoryCondition() {
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iEngineA );
      lInvInvTable.setInvCond( RefInvCondKey.REPREQ );
      lInvInvTable.update();
   }


   /**
    * Inventory has RFI condition
    */
   private void withRFIInventoryCondition() {
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iEngineA );
      lInvInvTable.setInvCond( RefInvCondKey.RFI );
      lInvInvTable.update();
   }


   // assert part request is cancelled and cleared up
   private void assertPartRequestCancelled() {
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iPartRequestKey );
      assertNull( lReqPartTable.getInventory() );

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( iPartRequestKey.getEventKey() );
      assertEquals( RefEventStatusKey.PRCANCEL, lEvtEventTable.getEventStatus() );
   }


   // Assert inventory is installed and issued_bool set to true
   private void assertInventoryInstalled() {

      assertNotNull( InvInvTable.findByPrimaryKey( iEngineA ).getHInvNo() );
      assertTrue( InvInvTable.findByPrimaryKey( iEngineA ).isIssuedBool() );

   }


   private void assertPartTotalQuantity( BigDecimal lExpectedQuantity ) {

      // assert part total quantity has decremented on one unit after installing
      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iEnginePart );

      BigDecimal lPartQtyAfterInstall = lEqpPartNoTable.getTotalQt();
      Assert.assertEquals( "Part total quanitity not as expected", lExpectedQuantity,
            lPartQtyAfterInstall );
   }


   private void assertAutoIssueTransactionRecorded() {

      // Because iEnginePart is financial class CONSUMABLE, a financial issue type transaction is
      // recorded. event_id is null, but sched_id is populated with the originating task.
      FncXactionLogKey lXactionLogKey =
            AutoIssueInventoryHandlerTestHelper.getLatestTransactionByPart( iEnginePart, 3 );
      Assert.assertNotNull( "No transaction for Engine Part", lXactionLogKey );

      FncXactionLog lXactionLog = FncXactionLog.findByPrimaryKey( lXactionLogKey );
      Assert.assertNull( "No event expected for Auto-issued inventory", lXactionLog.getEvent() );

      Assert.assertEquals( "Unexpected associated Task for transaction", iTask,
            lXactionLog.getTask() );
   }
}
