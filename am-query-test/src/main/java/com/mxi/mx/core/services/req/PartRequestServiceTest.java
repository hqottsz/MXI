package com.mxi.mx.core.services.req;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.stask.status.StatusService;
import com.mxi.mx.core.services.stask.taskpart.MxPartRequirementService;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.evt.EvtStage;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.req.ReqPart;


public class PartRequestServiceTest {

   private static final String DELIVERY_STATUS_WAS_UPDATED = "Delivery status was updated. ETA: ";
   private static final String NOTE = ". Note: ";
   private static final String DELIVERY_STATUS_ETA_WAS_UPDATED =
         "Delivery status ETA was updated: ";
   private static final String DELIVERY_STATUS_NOTE_WAS_ADDED = "Delivery status note was added: ";
   private static final String DELIVERY_STATUS_ETA_WAS_CHANGED_TO_UNKNOWN =
         "Delivery status ETA was changed to unknown.";
   private static final RefEventStatusKey OPEN_STATUS = RefEventStatusKey.PROPEN;
   private static final RefEventStatusKey AVAIL_STATUS = RefEventStatusKey.PRAVAIL;
   private static final RefEventStatusKey CANCEL_STATUS = RefEventStatusKey.PRCANCEL;
   private static final Date NEW_DELIVERY_ETA = new GregorianCalendar( 2009, 1, 10 ).getTime();
   private static final String NEW_NOTE = "NEW_NOTE";
   private static final String PR_CANCEL_NOTE = "CANCEL THE PART REQUEST";
   private static final String INVENTORY_WAS_UNRESERVED = "Inventory was unreserved by the system.";
   private static final String PR_CANCEL_NOTE_ON_TASK =
         "The part requirement for part group DEFAULT_PART_GROUP was updated to cancel the part request.";
   private static final String TASK_CANCEL_NOTE =
         "Canceled because task 'Task [T40S0001DNJ]' was canceled.";
   private static final String PART_REQUIREMENT_REMOVE_NOTE =
         "This part request was cancelled as a result of the installed part being removed from task 'Task [T40S0001DNJ]'.";

   private final PartRequestService iPartRequestService = new PartRequestService();
   private static HumanResourceKey sHr;
   private PartRequestKey iPartRequest;
   private PartRequestKey iPartRequestToCancel;
   private TaskKey iTask;
   private CancelPartRequestTO iCancelPartRequestTO;
   private LocationKey iSupplyLoc;
   private PartNoKey iPartNo;
   private InventoryKey iReservedInv;
   private TaskPartKey iTaskPartKey;
   private PartGroupKey iPartGroup;

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sConnection.getConnection(), PartRequestServiceTest.class,
            "PartRequestServiceTest.xml" );

      sHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );

      final int userId = OrgHr.findByPrimaryKey( sHr ).getUserId();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( sHr ) );
      UserParametersStub lUserParametersStub = new UserParametersStub( userId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_REMOVE_HISTORIC_PART_REQUIREMENT", true );
      UserParameters.setInstance( userId, "SECURED_RESOURCE", lUserParametersStub );
   }


   @Before
   public void setUpData() {
      iPartRequest = new PartRequestBuilder().withStatus( OPEN_STATUS )
            .withQuantityUnit( RefQtyUnitKey.EA ).build();
   }


   /**
    *
    * setup test data for canceling part request
    *
    * @param aRefInvClassKey:
    *           SER or BATCH
    * @param aBinQt:
    *           Quantity for BATCH ( N/A for SER)
    * @throws StringTooLongException
    */
   public void setUpDataForCancelPartRequest( RefInvClassKey aRefInvClassKey, double aBinQt )
         throws StringTooLongException {

      // setup task, part no, default part group, part requirement, task installed part, reserved
      // inventory
      iSupplyLoc = new LocationDomainBuilder().build();

      iTask = new TaskBuilder().withName( "Task" ).withBarcode( "T40S0001DNJ" )
            .withTaskClass( RefTaskClassKey.ADHOC ).build();

      setReservedInv( aRefInvClassKey, aBinQt );

      iTaskPartKey = new PartRequirementDomainBuilder( iTask ).withInstallPart( iPartNo )
            .forPartGroup( iPartGroup ).withRequestAction( RefReqActionKey.REQ ).build();

      TaskInstPartKey lTaskInstPart = new TaskInstPartKey( iTaskPartKey, 1 );

      DataSetArgument lArgs = lTaskInstPart.getPKWhereArg();
      lArgs.add( iPartNo.getPKWhereArg() );
      MxDataAccess.getInstance().executeInsert( "sched_inst_part", lArgs );

      // setup part request for part requirement with reserved SER inventory
      iPartRequestToCancel = new PartRequestBuilder().forPartRequirement( lTaskInstPart )
            .withReservedInventory( iReservedInv ).withStatus( AVAIL_STATUS )
            .withQuantityUnit( RefQtyUnitKey.EA ).build();

      // setup CancelPartRequestTO
      iCancelPartRequestTO = new CancelPartRequestTO();
      iCancelPartRequestTO.setHumanResource( sHr );
      iCancelPartRequestTO.setNotes( PR_CANCEL_NOTE );
   }


   /**
    * Setup reserved inventory for SER or BATCH
    *
    * @param aRefInvClassKey
    * @param aBinQt
    */
   private void setReservedInv( RefInvClassKey aRefInvClassKey, double aBinQt ) {
      PartNoBuilder lPartNoBuilder =
            new PartNoBuilder().withInventoryClass( aRefInvClassKey ).withDefaultPartGroup();

      iPartNo = lPartNoBuilder.build();
      iPartGroup = lPartNoBuilder.getDefaultPartGroup();
      iReservedInv = new InventoryBuilder().withPartNo( iPartNo ).withBinQt( aBinQt )
            .withCondition( RefInvCondKey.RFI ).atLocation( iSupplyLoc ).isReserved()
            .withReserveQt( aBinQt ).build();
   }


   /**
    * Make sure the history note is correctly inserted when both delivery ETA and note are updated.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHistoryNoteWhenBothDeliveryETAAndNoteAreUpdated() throws Exception {
      iPartRequestService.updateDeliveryStatus( new PartRequestKey[] { iPartRequest },
            NEW_DELIVERY_ETA, NEW_NOTE, false );

      EvtStage lEvtStage = new EvtStage( iPartRequest.getEventKey() );

      assertCommonProperties( lEvtStage );
      lEvtStage.assertStageNote(
            DELIVERY_STATUS_WAS_UPDATED + "10-FEB-2009 00:00 EST" + NOTE + NEW_NOTE );
   }


   /**
    * Make sure the history note is correctly inserted when only delivery ETA is updated.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHistoryNoteWhenOnlyDeliveryETAIsUpdated() throws Exception {
      iPartRequestService.updateDeliveryStatus( new PartRequestKey[] { iPartRequest },
            NEW_DELIVERY_ETA, null, false );

      EvtStage lEvtStage = new EvtStage( iPartRequest.getEventKey() );

      assertCommonProperties( lEvtStage );
      lEvtStage.assertStageNote( DELIVERY_STATUS_ETA_WAS_UPDATED + "10-FEB-2009 00:00 EST" );
   }


   /**
    * Make sure the history note is correctly inserted when only delivery ETA is updated.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHistoryNoteWhenOnlyNoteIsUpdated() throws Exception {
      iPartRequestService.updateDeliveryStatus( new PartRequestKey[] { iPartRequest }, null,
            NEW_NOTE, false );

      EvtStage lEvtStage = new EvtStage( iPartRequest.getEventKey() );

      assertCommonProperties( lEvtStage );
      lEvtStage.assertStageNote( DELIVERY_STATUS_NOTE_WAS_ADDED + NEW_NOTE );
   }


   /**
    * Make sure the history note is correctly inserted when only delivery ETA is cleared.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHistoryNoteWhenOnlyDeliveryETAIsCleared() throws Exception {
      iPartRequestService.updateDeliveryStatus( new PartRequestKey[] { iPartRequest }, null, null,
            true );

      EvtStage lEvtStage = new EvtStage( iPartRequest.getEventKey() );

      assertCommonProperties( lEvtStage );
      lEvtStage.assertStageNote( DELIVERY_STATUS_ETA_WAS_CHANGED_TO_UNKNOWN );
   }


   /**
    * Make sure the history note is correctly inserted when delivery ETA is cleared and note is
    * updated.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHistoryNoteWhenDeliveryETAIsClearedAndNoteIsUpdated() throws Exception {
      iPartRequestService.updateDeliveryStatus( new PartRequestKey[] { iPartRequest }, null,
            NEW_NOTE, true );

      EvtStage lEvtStage = new EvtStage( iPartRequest.getEventKey() );

      assertCommonProperties( lEvtStage );
      lEvtStage.assertStageNote( DELIVERY_STATUS_ETA_WAS_CHANGED_TO_UNKNOWN + " "
            + DELIVERY_STATUS_NOTE_WAS_ADDED + NEW_NOTE );
   }


   @Test
   public void createPartRequests_fromPartRequirement_reqPriorityKeepTheSame() throws Exception {
      List<PartRequestKey> partRequests =
            iPartRequestService.createPartRequests( new TaskKey( 4650, 1 ), sHr, null, false );

      assertEquals( 1, partRequests.size() );

      ReqPartTable lPartRequestTable = ReqPartTable.findByPrimaryKey( partRequests.get( 0 ) );

      assertEquals( RefReqPriorityKey.AOG, lPartRequestTable.getReqPriority() );
   }


   @Test
   public void createPartRequests_fromPartRequirement_withKIT() throws Exception {
      List<PartRequestKey> partRequests =
            iPartRequestService.createPartRequests( new TaskKey( 4650, 10 ), sHr, null, false );

      assertEquals( 1, partRequests.size() );

      ReqPartTable reqPartTable = ReqPartTable.findByPrimaryKey( partRequests.get( 0 ) );

      assertEquals( new PartNoKey( 4650, 10 ), reqPartTable.getPoPartNo() );
   }


   /**
    *
    * Make sure that after canceling the SER part request (1) the SER inventory is unreserved, (2)
    * the part request is in CANCEL status, and (3) the history notes are correctly created for the
    * part request and the related task
    *
    * @throws Exception
    */
   @Test
   public void testCancelPartRequestForSerInvShouldAddHistoryNotes() throws Exception {

      // GIVEN
      setUpDataForCancelPartRequest( RefInvClassKey.SER, 1.0 );

      // WHEN
      boolean lIsHistoryNoteNeededForTask = true;
      iPartRequestService.cancelPartRequest( iPartRequestToCancel, iCancelPartRequestTO, true, true,
            lIsHistoryNoteNeededForTask );

      // THEN
      assertCancelingPartRequestIsProperlyDone( PR_CANCEL_NOTE );

      assertHistoryNoteIsAddedForTask();
   }


   /**
    *
    * Make sure that after canceling the BATCH part request (1) the BATCH inventory is unreserved,
    * (2) the part request is in CANCEL status, and (3) the history notes are correctly created for
    * the part request and the related task
    *
    * @throws Exception
    */
   @Test
   public void testCancelPartRequestForBatchInvShouldAddHistoryNotes() throws Exception {

      // GIVEN
      setUpDataForCancelPartRequest( RefInvClassKey.BATCH, 3.0 );

      // WHEN
      boolean lIsHistoryNoteNeededForTask = true;
      iPartRequestService.cancelPartRequest( iPartRequestToCancel, iCancelPartRequestTO, true, true,
            lIsHistoryNoteNeededForTask );

      // THEN
      assertCancelingPartRequestIsProperlyDone( PR_CANCEL_NOTE );

      assertHistoryNoteIsAddedForTask();
   }


   /**
    *
    * Make sure that cancel task will cancel it's part request: (1) the inventory is unreserved, (2)
    * the part request is in CANCEL status, and (3) the history notes are correctly created for the
    * part request
    *
    * @throws Exception
    */
   @Test
   public void testCancelTaskShouldCancelPartRequest() throws Exception {

      // GIVEN
      setUpDataForCancelPartRequest( RefInvClassKey.SER, 1.0 );

      // WHEN
      new StatusService( iTask ).cancel( sHr, null, null, null, null, false, false );

      // THEN
      assertCancelingPartRequestIsProperlyDone( TASK_CANCEL_NOTE );
   }


   /**
    *
    * Make sure that remove part requirement will cancel part request: (1) the inventory is
    * unreserved, (2) the part request is in CANCEL status, and (3) the history notes are correctly
    * created for the part request
    *
    * @throws Exception
    */
   @Test
   public void testRemovePartRequirementShouldCancelPartRequest() throws Exception {

      // GIVEN
      setUpDataForCancelPartRequest( RefInvClassKey.SER, 1.0 );

      // WHEN
      new MxPartRequirementService().removePart( iTaskPartKey, false, sHr );

      // THEN
      assertCancelingPartRequestIsProperlyDone( PART_REQUIREMENT_REMOVE_NOTE );
   }


   /**
    * Assert the common properties.
    *
    * @param aEvtStage
    *           the event stage test table
    */
   private void assertCommonProperties( EvtStage aEvtStage ) {
      aEvtStage.assertCount( 1 );
      aEvtStage.assertHr( sHr );
      aEvtStage.assertSystemBool( 1 );
      aEvtStage.assertEventStatus( OPEN_STATUS.getCd() );
   }


   /**
    * assert that part request is cancelled properly
    *
    */
   private void assertCancelingPartRequestIsProperlyDone( String aNote ) {
      // reserved inventory is released
      InvInv lInvInv = new InvInv( iReservedInv );
      lInvInv.assertReservedBool( false );
      lInvInv.assertReservedQuantity( 0.0 );

      ReqPart lReqPart = new ReqPart( iPartRequestToCancel );
      lReqPart.assertInventoryKey( null );

      // PR cancellation will first unreserve the inventory, then set the PR status to CANCEL with
      // history note added to PR history
      EvtStage lPartReqStatus = new EvtStage( iPartRequestToCancel.getEventKey() );

      lPartReqStatus.assertCount( 2 );

      lPartReqStatus.assertEventStatus( 1, OPEN_STATUS.getCd() );
      lPartReqStatus.assertStageNote( 1, INVENTORY_WAS_UNRESERVED );

      lPartReqStatus.assertEventStatus( 2, CANCEL_STATUS.getCd() );
      lPartReqStatus.assertStageNote( 2, aNote );
   }


   /**
    * assert that history note is added to task history
    *
    */
   private void assertHistoryNoteIsAddedForTask() {
      EvtStage lTaskStatus = new EvtStage( iTask.getEventKey() );
      lTaskStatus.assertStageNote( PR_CANCEL_NOTE_ON_TASK );
   }
}
