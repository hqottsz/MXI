
package com.mxi.mx.core.services.transfer;

import static com.mxi.mx.core.key.RefEventStatusKey.IXCMPLT;
import static com.mxi.mx.core.key.RefEventStatusKey.IXINTR;
import static com.mxi.mx.core.key.RefEventStatusKey.LXCMPLT;
import static com.mxi.mx.core.key.RefEventStatusKey.LXPEND;
import static com.mxi.mx.core.key.RefXferTypeKey.STKTRN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.facade.req.PartRequestFacade;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocPrefMapKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefXferTypeKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.CoreLicenseStub.FeatureSet;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.inventory.exception.InventoryInTransitException;
import com.mxi.mx.core.services.inventory.exception.InventoryWithinKitInTransitException;
import com.mxi.mx.core.services.inventory.pick.PickedItem;
import com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils;
import com.mxi.mx.core.services.req.IssueInventoryService;
import com.mxi.mx.core.services.req.PartRequestTO;
import com.mxi.mx.core.services.req.PickIssueTO;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocPrefMapTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNo;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.req.ReqPart;


/**
 * This class tests the transfer services.
 *
 * @author Libin Cai
 * @created December 07, 2015
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TransferServiceTest {

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int USERID_TESTUSER = 999;
   private static final String USERNAME_TESTUSER = "testuser";

   private static final String AIRPORT_A = "AIRPORT_A";
   private static final String AIRPORT_B = "AIRPORT_B";

   private static final String DOCK_LOC_CD_A = "DOCK_LOC_A";
   private static final String DOCK_LOC_CD_B = "DOCK_LOC_B";

   private static final String EXPENSE_ACC_CD = "EXPENSE";
   private static final String PREFSRVSTORE = "PREFSRVSTORE";
   private static final String SECOND_PREFSRVSTORE = "SECOND_PREFSRVSTORE";
   private static final String PREFSRVSTORE_BIN = "PREFSRVSTORE_BIN";
   private static final String NON_PREFSRVSTORE = "NON_PREFSRVSTORE";
   private static final String LINE = "LINE";
   private static final String DOCK = "DOCK";

   private static final ManufacturerKey MANUFACTURER = new ManufacturerKey( 0, "MXI" );
   private static final String PART_NO_OEM = "PART_NO_OEM";
   private static final BigDecimal PART_AUP = BigDecimal.ONE;
   private static final BigDecimal PART_TOTAL_QTY = BigDecimal.valueOf( 3 );
   private static final String INV_SN = "INV_SN";
   private static final String BATCH_INV_SN = "BATCH_INV_SN";
   protected static final RefEventStatusKey PRAVAIL = RefEventStatusKey.PRAVAIL;
   protected static final RefInvCondKey RFI = RefInvCondKey.RFI;
   private static final double BIN_QTY = 10.0;

   // Data required by transfer service logic but is not applicable to the tests.
   private static final String NA_LOCATION_CODE = "FROM_LOC";
   private static final String NA_TRANSFER_TO_LOCATION_CODE = "TO_LOC";
   private static final BigDecimal NA_BATCH_QUANTITY = BigDecimal.TEN;

   private LocationKey iAirport;

   // A supply location
   protected LocationKey iAirportLocation;
   // A location under the supply location
   protected LocationKey iLineLocation;
   protected LocationKey iPreferredStoreLocation;
   protected LocationKey iSecondPreferredStoreLocation;
   protected LocationKey iPreferredStoreBinLocation;
   protected LocationKey iIgnoredDockLocation;
   protected LocationKey iNonPreferredStoreLocation;
   public static OwnerKey iLocalOwner;
   protected PartNoKey iPartA;

   private HumanResourceKey iHr;
   private HumanResourceKey iReceivedBy;
   private HumanResourceKey iAuthorizedBy;
   private EvtEventDao iEvtEventDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * This case is testing the total quantity and total value for an expected turned in part (track
    * consumable) while its alternative part is issued. For example: A part request is created to
    * remove a part (original part) from an aircraft, but install its alternative part with
    * different AUP. After the turn in is completed, the total quantity of the original part should
    * be increated by one, and its total value should be increased by its AUP x 1, instead of the
    * alternative part's AUP x 1.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testExpectedTurnInWithAlternativePart() throws Exception {

      final String ALT_PART_NO_OEM = "ALT_PART_NO_OEM";
      final String ALT_INV_SN = "ALT_INV_SN";

      // Data setup

      // create a part group for the two parts
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( "TESTGROUP" ).build();

      PartNoKey lTrkConsumOrigPart = createTrackPart( lPartGroup, PART_NO_OEM );
      PartNoKey lTrkConsumAltePart = createTrackPart( lPartGroup, ALT_PART_NO_OEM );

      OwnerKey lOwner = new OwnerDomainBuilder().isDefault().build();

      // create DOCK location for turning in
      LocationKey lDockLoc = createDock( DOCK_LOC_CD_A, iAirport );

      // create USSTG location for expected turn in transfer
      createUSSTGLocation( iAirport );

      InventoryKey lOrigPartInv =
            new InventoryBuilder().withPartNo( lTrkConsumOrigPart ).withClass( RefInvClassKey.TRK )
                  .withSerialNo( INV_SN ).withCondition( RefInvCondKey.REPREQ )
                  .atLocation( iAirport ).withOwner( lOwner ).isIssued().build();

      // create inventory for alterative part
      InventoryKey lAltePartInv = new InventoryBuilder().withPartNo( lTrkConsumAltePart )
            .withClass( RefInvClassKey.TRK ).withSerialNo( ALT_INV_SN ).withCondition( RFI )
            .atLocation( lDockLoc ).withOwner( lOwner ).withBinQt( 1.0 ).build();

      // Create an adhoc open part request for the alterative part

      PartRequestTO lPartRequestTO = new PartRequestTO();
      lPartRequestTO.setRequestPriority( RefReqPriorityKey.NORMAL, "" );
      lPartRequestTO.setRequestedBy( USERNAME_TESTUSER, "" );
      lPartRequestTO.setRequestBomPart( lPartGroup, "" );
      lPartRequestTO.setPartNo( lTrkConsumAltePart );
      lPartRequestTO.setNeededQty( 1.0, "" );
      lPartRequestTO.setWhereNeeded( DOCK_LOC_CD_A );
      lPartRequestTO.setIssueToAccount( EXPENSE_ACC_CD );

      PartRequestKey lPartRequestKey =
            PartRequestFacade.createPartRequest( lPartRequestTO, false, false )[0];

      // Issue the inventory with the alternative part

      PickIssueTO lPickIssueTO = new PickIssueTO( lPartRequestKey );
      lPickIssueTO.setReceivedBy( iHr, false, "" );
      lPickIssueTO.addPickedItem( new PickedItem( ALT_PART_NO_OEM, MANUFACTURER.getCd(), ALT_INV_SN,
            RFI.getCd(), 1.0, lDockLoc, null ) );

      // Issue picked item. When issuing, if the to-be-issued inventory hasn't been reserved to
      // the given part request, system will reserve it first.
      IssueInventoryService.issueInventory( lPickIssueTO, iHr );

      // assert the alternative inventory is issued
      new InvInv( lAltePartInv ).assertIssuedBoolean( true );

      // Turn in the inventory with the original part

      turnIn( lOrigPartInv, lDockLoc );

      // Assert the results

      BigDecimal lExpectedTotalQty = PART_TOTAL_QTY.add( BigDecimal.ONE );

      EqpPartNo lEqpPartNo = new EqpPartNo( lTrkConsumOrigPart );
      lEqpPartNo.assertTotalQt( lExpectedTotalQty );
      lEqpPartNo.assertTotalValue( lExpectedTotalQty.multiply( PART_AUP ) );
   }


   /**
    * This case is testing the total quantity for a part (track consumable) while turning in the
    * inventory (issued) of the part to a location different than where it is currently located in
    * Maintenix. For example: If a Cape Air aircraft breaks down in an outstation they will fly a
    * mechanic there with the parts that they require and complete the maintenance. They will then
    * take the broken component with them on the return flights. So the component was removed from
    * the aircraft in Location B, but the inventory will be turned in at the home base, location A.
    * If you turn in an inventory at a location different than where it is currently located in
    * Maintenix, it should cause the total quantity to increment by just 1 rather than 2.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTurnInTrackIssuedInvToDifferentLoc() throws Exception {

      turnInInvToDifferentLocAndAssert( true, PART_TOTAL_QTY.add( BigDecimal.ONE ) );
   }


   /**
    * This case is testing the total quantity for a part (track consumable) while turning in the
    * inventory (not issued) of the part to a location different than where it is currently located
    * in Maintenix. For example: If there is an urgent need of a component, but they don't have any
    * avaliable in Location A. They will fly a mechanic to Location B to take a component with them
    * on the return flights. So the inventory (not issued yet) of the component will be turned in at
    * the home base, location A. If you turn in a not issued inventory at a location different than
    * where it is currently located in Maintenix, it should not change the total quantity of the
    * part.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTurnInTrackNotIssuedInvToDifferentLoc() throws Exception {

      turnInInvToDifferentLocAndAssert( false, PART_TOTAL_QTY );
   }


   /**
    * test to make sure the serialized inventory transfer from non-preferred to preferred location
    * breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromNonPreferredToPreferredLocationBreaksReservation()
         throws Exception {

      // create inventory that is reserved at non-preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iNonPreferredStoreLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer to preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PROPEN, since the reservation got broken
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure the batch inventory transfer from non-preferred to preferred location breaks
    * reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromNonPreferredToPreferredLocationBreaksReservation()
         throws Exception {

      // create inventory that is reserved at non-preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iNonPreferredStoreLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer to preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status is PROPEN, since the reservation got broken
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure the serialized inventory transfer from non-preferred to ignored location
    * breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromNonPreferredToIgnoredLocationBreaksReservation()
         throws Exception {

      // create inventory at a non-preferrred location that is reserved
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iNonPreferredStoreLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to ignored dock location
      TransferKey lTransfer = createTransfer( lInventory, DOCK, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PROPEN, since the reservation should be broken when
      // inventory is transferred to ignored location
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure the batch inventory transfer from non-preferred to ignored location breaks
    * reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromNonPreferredToIgnoredLocationBreaksReservation()
         throws Exception {

      // create inventory at non-preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iNonPreferredStoreLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to ignored dock location
      TransferKey lTransfer = createTransfer( lInventory, DOCK, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );

   }


   /**
    * test to make sure the inventory transfer within the same preferred store does not break
    * reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferWithinPreferredLocationKeepsReservation() throws Exception {

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iPreferredStoreBinLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to the bin under the same srvstore location
      TransferKey lTransfer = createTransfer( lInventory, PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PRAVAIL, since the reservation is not broken
      assertPartRequestHasStatus( lPartRequestAtLine, RefEventStatusKey.PRAVAIL );
   }


   /**
    * test to make sure the batch inventory transfer within the same preferred store does not break
    * reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferWithinPreferredLocationKeepsReservation() throws Exception {

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iPreferredStoreBinLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to the bin under the same srvstore location
      TransferKey lTransfer = createTransfer( lInventory, PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PRAVAIL, since the reservation is not broken
      assertPartRequestHasStatus( lPartRequestAtLine, RefEventStatusKey.PRAVAIL );
   }


   /**
    * test to make sure that serialized inventory transfer from preferred location to
    * non-preferred/regular location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromPreferredToNonPreferredLocationBreaksReservation()
         throws Exception {

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iPreferredStoreBinLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to non-preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, NON_PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that batch inventory transfer from preferred location to
    * non-preferred/regular location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromPreferredToNonPreferredLocationBreaksReservation()
         throws Exception {

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iPreferredStoreBinLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to non-preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, NON_PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that serialized inventory transfer from preferred location to another
    * preferred location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromPreferredToAnotherPreferredLocationBreaksReservation()
         throws Exception {

      // add a second preferred srvstore to the line maintenance location
      addSecondPreferredLocation();

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iPreferredStoreBinLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to another preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, SECOND_PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that batch inventory transfer from preferred location to another preferred
    * location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromPreferredToAnotherPreferredLocationBreaksReservation()
         throws Exception {

      // add a second preferred srvstore to the line maintenance location
      addSecondPreferredLocation();

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iPreferredStoreBinLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to another preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, SECOND_PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that serialized inventory transfer from preferred location to ignored
    * location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromPreferredToIgnoredLocationBreaksReservation() throws Exception {

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iPreferredStoreBinLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to ignored location
      TransferKey lTransfer = createTransfer( lInventory, DOCK, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that batch inventory transfer from preferred location to ignored location
    * breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromPreferredToIgnoredLocationBreaksReservation() throws Exception {

      // create inventory at bin location under preferred store
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iPreferredStoreBinLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to to ignored location
      TransferKey lTransfer = createTransfer( lInventory, DOCK, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status is open
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that serialized inventory transfer from non-preferred location to
    * non-preferred location does not break reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromNonPreferredToNonPreferredLocationKeepsReservation()
         throws Exception {

      // create inventory at a non-preferrred location that is reserved
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iNonPreferredStoreLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer for the inventory to another non-preferred location
      TransferKey lTransfer = createTransfer( lInventory, LINE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PRAVAIL, since the reservation is not broken
      assertPartRequestHasStatus( lPartRequestAtLine, RefEventStatusKey.PRAVAIL );
   }


   /**
    * test to make sure that serialized inventory transfer from ignored location to preferred
    * location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromIgnoredToPreferredLocationBreaksReservation() throws Exception {

      // create inventory that is reserved at non-preferred location
      InventoryKey lInventory = createInventory( RefInvClassKey.SER, iIgnoredDockLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer to preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PROPEN, since the reservation got broken
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that batch inventory transfer from ignored location to preferred location
    * breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromIgnoredToPreferredLocationBreaksReservation() throws Exception {

      // create inventory that is reserved at non-preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iIgnoredDockLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer to preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status is PROPEN, since the reservation got broken
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that serialized inventory transfer from ignored location to non-preferred
    * location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromIgnoredToNonPreferredLocationBreaksReservation()
         throws Exception {
      // set the location ignored for auto-reservation
      InvLocTable lInvLocTable = InvLocTable.findByPrimaryKey( iPreferredStoreLocation );
      lInvLocTable.setNoAutoRsrvBool( true );

      // create inventory that is reserved at ignored location
      InventoryKey lInventory = createInventory( RefInvClassKey.SER, iPreferredStoreLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer to non-preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, NON_PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status is PROPEN, since the reservation got broken
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * test to make sure that batch inventory transfer from ignored location to non-preferred
    * location breaks reservation
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromIgnoredToNonPreferredLocationBreaksReservation()
         throws Exception {

      // set the location ignored for auto-reservation
      InvLocTable lInvLocTable = InvLocTable.findByPrimaryKey( iPreferredStoreLocation );
      lInvLocTable.setNoAutoRsrvBool( true );

      // create inventory that is reserved at ignored location but preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iPreferredStoreBinLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );

      // create transfer to non-preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, NON_PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status is PROPEN, since the reservation got broken
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /**
    * Test to make sure that locked reservations are not broken due to transfer
    *
    * @throws Exception
    */
   @Test
   public void testSERTransferFromPreferredToNonPreferredLocationKeepsLockReservation()
         throws Exception {

      // create inventory that is reserved at preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, iPreferredStoreBinLocation, 1.0 );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, 1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( lPartRequestAtLine );
      lReqPartTable.setLockReserveBool( true );
      lReqPartTable.update();

      // create transfer to non-preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, NON_PREFSRVSTORE, 1.0 );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, false, true, false, null );

      // check that part-request status
      assertPartRequestHasStatus( lPartRequestAtLine, RefEventStatusKey.PRAVAIL );
   }


   /**
    * Test to make sure that locked reservations are not broken due to transfer
    *
    * @throws Exception
    */
   @Test
   public void testBatchTransferFromPreferredToNonPreferredLocationKeepsLockReservation()
         throws Exception {

      // create inventory that is reserved at preferred location
      InventoryKey lInventory =
            createInventory( RefInvClassKey.BATCH, iPreferredStoreBinLocation, BIN_QTY );

      // create part-request needed in 5 days from now
      Date lWhenNeeded = DateUtils.addDays( new Date(), 5 );
      PartRequestKey lPartRequestAtLine = CreatePartRequestUtils.createPartRequest( iPartA,
            iLineLocation, BIN_QTY, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( lPartRequestAtLine );
      lReqPartTable.setLockReserveBool( true );
      lReqPartTable.update();

      // create transfer to non-preferred srvstore location
      TransferKey lTransfer = createTransfer( lInventory, NON_PREFSRVSTORE, BIN_QTY );

      // Complete the transfer
      lInventory = TransferService.complete( lTransfer, null, iHr, true, false, false, null );

      // check that part-request status
      assertPartRequestHasStatus( lPartRequestAtLine, RefEventStatusKey.PRAVAIL );
   }


   /**
    * Test to make sure that a new batch inventory will be created when turning in unused batch
    * inventory , and the new inventory has the same Manufacture Date, Received Date, and Shelf
    * Expiry Date as the original batch inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTurnInUnusedBatchInv() throws Exception {

      // 1 GIVEN

      // create batch part
      PartNoKey lBatchPart = createBatchPart();

      // create partially issued batch inventory with BIN_QTY > 0
      InventoryKey lPartiallyIssuedBatchInv = createBatchInv( lBatchPart, 1.0 );

      // create DOCK location for turning in
      LocationKey lDockLoc = createDock( DOCK_LOC_CD_A, iAirport );

      // create SRVSTG location for expected turn in transfer
      createSRVSTGLocation( iAirport );

      // create Turn In TO
      TurnInTO lTurnInTO = new TurnInTO();
      lTurnInTO.setInventory( null );
      lTurnInTO.setPartNo( PART_NO_OEM, "" );
      lTurnInTO.setSerialNo( BATCH_INV_SN, "" );
      lTurnInTO.setManufacturer( MANUFACTURER.getCd(), "" );
      lTurnInTO.setTurnInQuantity( 1.0, "" );
      lTurnInTO.setCondition( RefInvCondKey.RFI, "" );
      lTurnInTO.setLocation( lDockLoc, "" );
      lTurnInTO.setCreditAccount( EXPENSE_ACC_CD, "" );

      // 2 WHEN
      new TransferService().turnIn( lTurnInTO, iHr );

      // 3 THEN

      QuerySet lDs = findBatchInventoriesByPartNoAndBatchNo( lBatchPart, BATCH_INV_SN );
      // assert one new inventory is created in addtion to the existing one
      assertTrue( lDs.getRowCount() == 2 );

      // get the newly created inventory add
      InventoryKey lNewlyCreatedInvKey = getNewlyCreatedInv( lPartiallyIssuedBatchInv, lDs );

      assertTrue( lNewlyCreatedInvKey != null );

      InvInvTable lOriginalInvTable = InvInvTable.findByPrimaryKey( lPartiallyIssuedBatchInv );
      InvInvTable lNewInvTable = InvInvTable.findByPrimaryKey( lNewlyCreatedInvKey );
      // assert manufacture date, received date, and expiry date are well copied
      assertEquals( lNewInvTable.getManufactDt(), lOriginalInvTable.getManufactDt() );
      assertEquals( lNewInvTable.getReceivedDt(), lOriginalInvTable.getReceivedDt() );
      assertEquals( lNewInvTable.getShelfExpiryDt(), lOriginalInvTable.getShelfExpiryDt() );
   }


   @Test( expected = InventoryInTransitException.class )
   public void completingTransferPreventedWhenStockTransferAndComponentIsInTransit()
         throws Exception {

      // Given a component that is in a shipment that is in-transit.
      InventoryKey engine = Domain.createEngine( eng -> {
         // Dependency of the complete transfer logic.
         eng.setLocation( Domain.createLocation() );
      } );
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXINTR );
         shipmnt.addShipmentLine( line -> {
            line.inventory( engine );
         } );
      } );

      // Given a pending stock transfer for the component.
      TransferKey transfer = Domain.createTransfer( xfer -> {
         xfer.setStatus( LXPEND );
         xfer.setType( STKTRN );
         xfer.setInventory( engine );
         // Dependency of the complete transfer logic.
         xfer.setFromLocation( Domain.createLocation() );
         xfer.setToLocation( Domain.createLocation() );
      } );

      // When the transfer is attempted to be completed.
      // Note: the last 4 arguments are hard coded in TransferBean.complete()'s call to the method.
      TransferService.complete( transfer, iReceivedBy, iAuthorizedBy, true, true, false, null );

      // Then the transfer is prevented and an exception is thrown.
      // (refer to test "expected" exception)

   }


   @Test
   public void completingTransferIsSuccessfulWhenNonStockTransferAndComponentIsInTransit()
         throws Exception {

      // Given a component that is in a shipment that is in-transit.
      InventoryKey engine = Domain.createEngine( eng -> {
         // Dependency of the complete transfer logic.
         eng.setLocation( Domain.createLocation() );
      } );
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXINTR );
         shipmnt.addShipmentLine( line -> {
            line.inventory( engine );
         } );
      } );

      // Given a pending non-stock transfer for the component.
      RefXferTypeKey nonStockTransfer = RefXferTypeKey.TURNIN;
      TransferKey transfer = Domain.createTransfer( xfer -> {
         xfer.setStatus( LXPEND );
         xfer.setType( nonStockTransfer );
         xfer.setInventory( engine );
         // Dependency of the complete transfer logic.
         xfer.setFromLocation( Domain.createLocation() );
         xfer.setToLocation( Domain.createLocation() );
      } );

      // When the transfer is attempted to be completed.
      // Note: the last 4 arguments are hard coded in TransferBean.complete()'s call to the method.
      TransferService.complete( transfer, iReceivedBy, iAuthorizedBy, true, true, false, null );

      // Then the transfer is successfully completed.
      assertThat( "Unexpected transfer status",
            iEvtEventDao.findByPrimaryKey( transfer.getEventKey() ).getEventStatus(),
            is( LXCMPLT ) );
   }


   @Test
   public void completingTransferIsSuccessfulWhenInventoryIsNotInTransit() throws Exception {

      // Given a component that was in a completed shipment (not in-transit).
      InventoryKey engine = Domain.createEngine( eng -> {
         // Dependency of the complete transfer logic.
         eng.setLocation( Domain.createLocation() );
      } );
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXCMPLT );
         shipmnt.addShipmentLine( line -> {
            line.inventory( engine );
         } );
      } );

      // Given a pending transfer for the component.
      TransferKey transfer = Domain.createTransfer( xfer -> {
         xfer.setStatus( LXPEND );
         xfer.setInventory( engine );
         // Dependency of the complete transfer logic.
         xfer.setFromLocation( Domain.createLocation() );
         xfer.setToLocation( Domain.createLocation() );
      } );

      // When the transfer is attempted to be completed.
      // Note: the last 4 arguments are hard coded in TransferBean.complete()'s call to the method.
      TransferService.complete( transfer, iReceivedBy, iAuthorizedBy, true, true, false, null );

      // Then the transfer is successfully completed.
      assertThat( "Unexpected transfer status",
            iEvtEventDao.findByPrimaryKey( transfer.getEventKey() ).getEventStatus(),
            is( LXCMPLT ) );
   }


   /**
    * Verify that creating a transfer that represents the removal of an inventory from a kit is
    * prevented when the kit is within an in-transfer shipment.
    */
   @Test( expected = InventoryWithinKitInTransitException.class )
   public void creatingTransferOfInventoryOutOfKitIsPreventedWhenKitWithinInTransferShipment()
         throws Exception {

      // Given an inventory within a kit.
      InventoryKey trk = Domain.createTrackedInventory( inv -> {
         // Dependency of the create transfer logic.
         inv.setLocation( Domain.createLocation( loc -> {
            loc.setCode( NA_LOCATION_CODE );
         } ) );
         // The trigger TIBR_UOM_INVXFER requires the inventory to have a part number and that part
         // number to have an associated quantity unit.
         inv.setPartNumber( Domain.createPart( part -> {
            part.setQtyUnitKey( RefQtyUnitKey.EA );
         } ) );

      } );
      InventoryKey kit = Domain.createKitInventory( kt -> {
         kt.addKitContentInventory( trk );
      } );

      // Given a kit that was in a completed shipment (not in-transit).
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXINTR );
         shipmnt.addShipmentLine( line -> {
            line.inventory( kit );
         } );
      } );

      // Given a location to transfer the inventory to after it is removed from the kit.
      Domain.createLocation( loc -> {
         // Dependency of the create transfer logic.
         loc.setCode( NA_TRANSFER_TO_LOCATION_CODE );
      } );

      // When a transfer is attempted to be created for the inventory.
      // (i.e. when the inventory is attempted to be removed from the kit)
      CreateTransferTO createTransferTO = new CreateTransferTO();
      createTransferTO.setHrKey( iAuthorizedBy );
      createTransferTO.setInventory( trk );
      createTransferTO.setKitInv( kit );
      createTransferTO.setRemoveInvFromKit( true );
      createTransferTO.setTransferToCd( NA_TRANSFER_TO_LOCATION_CODE );
      createTransferTO.setQuantity( 1.0 );
      TransferService.create( createTransferTO );

      // Then a InventoryWithinKitInTransitException is thrown.
      // (refer to expected of Test annotation)
   }


   /**
    * Verify that creating a transfer that represents the removal of an inventory from a kit is
    * successful when the kit is not within an in-transfer shipment.
    */
   @Test
   public void creatingTransferOfInventoryOutOfKitIsSuccessfulWhenKitNotWithinInTransferShipment()
         throws Exception {

      // Given an inventory within a kit.
      InventoryKey trk = Domain.createTrackedInventory( inv -> {
         // Dependency of the create transfer logic.
         inv.setLocation( Domain.createLocation( loc -> {
            loc.setCode( NA_LOCATION_CODE );
         } ) );
         // The trigger TIBR_UOM_INVXFER requires the inventory to have a part number and that part
         // number to have an associated quantity unit.
         inv.setPartNumber( Domain.createPart( part -> {
            part.setQtyUnitKey( RefQtyUnitKey.EA );
         } ) );

      } );
      InventoryKey kit = Domain.createKitInventory( kt -> {
         kt.addKitContentInventory( trk );
      } );

      // Given a kit that was in a completed shipment (not in-transit).
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXCMPLT );
         shipmnt.addShipmentLine( line -> {
            line.inventory( kit );
         } );
      } );

      // Given a location to transfer the inventory to after it is removed from the kit.
      Domain.createLocation( loc -> {
         // Dependency of the create transfer logic.
         loc.setCode( NA_TRANSFER_TO_LOCATION_CODE );
      } );

      // When a transfer is attempted to be created for the inventory.
      // (i.e. when the inventory is attempted to be removed from the kit)
      CreateTransferTO createTransferTO = new CreateTransferTO();
      createTransferTO.setHrKey( iAuthorizedBy );
      createTransferTO.setInventory( trk );
      createTransferTO.setKitInv( kit );
      createTransferTO.setRemoveInvFromKit( true );
      createTransferTO.setTransferToCd( NA_TRANSFER_TO_LOCATION_CODE );
      createTransferTO.setQuantity( 1.0 );
      TransferKey transfer = TransferService.create( createTransferTO );

      // Then the transfer is successfully created.
      assertTrue( "Transfer key is not valid, thus transfer not successfully created.",
            transfer.isValid() );
   }


   @Test( expected = InventoryInTransitException.class )
   public void completingTransferPreventedWhenStockTransferAndBatchIsInTransit() throws Exception {

      // Given a batch inventory that is in a shipment that is in-transit.
      InventoryKey batch = Domain.createBatchInventory( inv -> {
         // Dependency of the complete transfer logic.
         inv.setBinQt( 1.0 );
         inv.setLocation( Domain.createLocation() );
      } );
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXINTR );
         shipmnt.addShipmentLine( line -> {
            line.inventory( batch );
         } );
      } );

      // Given a pending stock transfer for the component.
      TransferKey transfer = Domain.createTransfer( xfer -> {
         xfer.setInventory( batch );
         xfer.setStatus( LXPEND );
         xfer.setType( STKTRN );
         // Dependency of the complete transfer logic.
         xfer.setQuantity( 1 );
         xfer.setFromLocation( Domain.createLocation() );
         xfer.setToLocation( Domain.createLocation() );
      } );

      // When the transfer is attempted to be completed.
      // Note: the last 4 arguments are hard coded in TransferBean.complete()'s call to the method.
      TransferService.complete( transfer, iReceivedBy, iAuthorizedBy, true, true, false, null );

      // Then the transfer is prevented and an exception is thrown.
      // (refer to test "expected" exception)

   }


   @Test
   public void completingTransferIsSuccessfulWhenBatchIsNotInTransit() throws Exception {

      // Given a batch inventory that was in a completed shipment (not in-transit).
      InventoryKey batch = Domain.createBatchInventory( inv -> {
         // Dependency of the complete transfer logic.
         inv.setLocation( Domain.createLocation() );
         inv.setBinQt( NA_BATCH_QUANTITY );
      } );
      Domain.createShipment( shipmnt -> {
         shipmnt.setStatus( IXCMPLT );
         shipmnt.addShipmentLine( line -> {
            line.inventory( batch );
         } );
      } );

      // Given a pending transfer for the component.
      TransferKey transfer = Domain.createTransfer( xfer -> {
         xfer.setInventory( batch );
         // Dependency of the complete transfer logic.
         xfer.setQuantity( NA_BATCH_QUANTITY );
         xfer.setFromLocation( Domain.createLocation() );
         xfer.setToLocation( Domain.createLocation() );
      } );

      // When the transfer is attempted to be completed.
      // Note: the last 4 arguments are hard coded in TransferBean.complete()'s call to the method.
      TransferService.complete( transfer, iReceivedBy, iAuthorizedBy, true, true, false, null );

      // Then the transfer is successfully completed.
      assertThat( "Unexpected transfer status",
            iEvtEventDao.findByPrimaryKey( transfer.getEventKey() ).getEventStatus(),
            is( LXCMPLT ) );
   }


   /**
    *
    * Assert that the part request has the given status.
    *
    * @param aPartRequest
    *           the part request
    * @param aStatus
    *           the part request's status
    */
   public static void assertPartRequestHasStatus( PartRequestKey aPartRequest,
         RefEventStatusKey aStatus ) {
      new EvtEventUtil( aPartRequest ).assertEventStatus( aStatus );
   }


   /**
    *
    * Assert that the part request is open - has status PROPEN and has no reserved inventory.
    *
    * @param aPartRequest
    *           the part request
    */
   public static void assertPartRequestIsOpen( PartRequestKey aPartRequest ) {
      new EvtEventUtil( aPartRequest ).assertEventStatus( RefEventStatusKey.PROPEN );
      new ReqPart( aPartRequest ).assertInventoryKey( null );
   }


   @Before
   public void setUp() {
      MxCoreLicense.setValidator( new CoreLicenseStub( FeatureSet.NONE ) );
      UserParameters lUserSecuredResourceParameters =
            UserParameters.getInstance( USERID_TESTUSER, "SECURED_RESOURCE" );
      lUserSecuredResourceParameters.setBoolean( "ALLOW_TURN_IN_MOVE_INV", true );
      lUserSecuredResourceParameters.setBoolean( "ALLOW_TURN_IN_NOT_ISSUED_INV", true );

      iAirportLocation = Domain.createLocation( loc -> {
         loc.setCode( "AIRPORT" );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );

      // The preferred location
      iPreferredStoreLocation = Domain.createLocation( loc -> {
         loc.setCode( PREFSRVSTORE );
         loc.setType( RefLocTypeKey.SRVSTORE );
         loc.setParent( iAirportLocation );
         loc.setSupplyLocation( iAirportLocation );
      } );

      // The bin location under the preferred store location
      iPreferredStoreBinLocation = Domain.createLocation( loc -> {
         loc.setCode( PREFSRVSTORE_BIN );
         loc.setType( RefLocTypeKey.BIN );
         loc.setParent( iPreferredStoreLocation );
         loc.setSupplyLocation( iAirportLocation );
      } );

      // Line location
      iLineLocation = Domain.createLocation( loc -> {
         loc.setCode( LINE );
         loc.setType( RefLocTypeKey.LINE );
         loc.setParent( iAirportLocation );
         loc.setSupplyLocation( iAirportLocation );
         loc.addPreferredLocation( iPreferredStoreLocation );
      } );

      // Dock location
      iIgnoredDockLocation = Domain.createLocation( loc -> {
         loc.setCode( DOCK );
         loc.setType( RefLocTypeKey.DOCK );
         loc.setParent( iAirportLocation );
         loc.setSupplyLocation( iAirportLocation );
         loc.setIsPreventingAutoReservation( true );
      } );

      // A non-preferred warehouse location
      iNonPreferredStoreLocation = Domain.createLocation( loc -> {
         loc.setCode( NON_PREFSRVSTORE );
         loc.setType( RefLocTypeKey.SRVSTORE );
         loc.setParent( iAirportLocation );
         loc.setSupplyLocation( iAirportLocation );
      } );

      iLocalOwner = Domain.createOwner( owner -> {
         owner.setDefaultBool( true );
      } );

      iReceivedBy = Domain.createHumanResource();
      iAuthorizedBy = Domain.createHumanResource();
      iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );

   }


   @After
   public void tearDown() {
      MxCoreLicense.setValidator( null );
   }


   /**
    * Set up the testing data.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void loadData() throws Exception {
      // create a human resource
      iHr = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser( user -> {
            user.setUsername( USERNAME_TESTUSER );
            user.setUserId( USERID_TESTUSER );
         } ) );
      } );

      iAirport = createAirport( AIRPORT_A );

      // create account for turn in
      new AccountBuilder().withCode( EXPENSE_ACC_CD ).withType( RefAccountTypeKey.EXPENSE ).build();
   }


   /**
    * Create an airport.
    *
    * @param aCode
    *           the location code
    *
    * @return the airport location key
    */
   private LocationKey createAirport( String aCode ) {
      return Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setCode( aCode );
         loc.setIsSupplyLocation( true );
      } );
   }


   /**
    * Create a dock location.
    *
    * @param aLocCd
    *           the location code
    * @param aSupplyLoc
    *           the supply location
    *
    * @return the doc location
    */
   private LocationKey createDock( String aLocCd, LocationKey aSupplyLoc ) {
      return Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setCode( aLocCd );
         loc.setSupplyLocation( aSupplyLoc );
      } );
   }


   /**
    * Create a track part.
    *
    * @return the part no builder
    */
   private PartNoKey createTrackPart() {

      return createTrackPart( null, PART_NO_OEM );
   }


   /**
    * create batch part
    *
    * @return
    */
   private PartNoKey createBatchPart() {
      return new PartNoBuilder().withOemPartNo( PART_NO_OEM ).manufacturedBy( MANUFACTURER )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withAbcClass( RefAbcClassKey.D ).withUnitType( RefQtyUnitKey.EA )
            .withTotalQuantity( PART_TOTAL_QTY ).withAverageUnitPrice( PART_AUP )
            .withTotalValue( PART_TOTAL_QTY.multiply( PART_AUP ) ).build();
   }


   /**
    * Create a track part.
    *
    * @param aPartGroup
    *           the part group
    * @param aPartNoOem
    *           the part no oem
    *
    * @return the part no builder
    */
   private PartNoKey createTrackPart( PartGroupKey aPartGroup, String aPartNoOem ) {

      return new PartNoBuilder().withOemPartNo( aPartNoOem )
            .withShortDescription( "Consum Part " + aPartNoOem ).manufacturedBy( MANUFACTURER )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withInventoryClass( RefInvClassKey.TRK ).withRepairBool( true )
            .withAbcClass( RefAbcClassKey.D ).withUnitType( RefQtyUnitKey.EA )
            .withTotalQuantity( PART_TOTAL_QTY ).withAverageUnitPrice( PART_AUP )
            .withTotalValue( PART_TOTAL_QTY.multiply( PART_AUP ) ).isAlternateIn( aPartGroup )
            .build();
   }


   /**
    * Create a SRVSTG location.
    *
    * @param aSupplyLoc
    *           the supply location
    *
    * @return the SRVSTG location
    */
   private LocationKey createSRVSTGLocation( LocationKey aSupplyLoc ) {
      return Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.SRVSTG );
         loc.setSupplyLocation( aSupplyLoc );
      } );
   }


   /**
    * Create a USSTG location.
    *
    * @param aSupplyLoc
    *           the supply location
    *
    * @return the USSTG location
    */
   private LocationKey createUSSTGLocation( LocationKey aSupplyLoc ) {
      return Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.USSTG );
         loc.setSupplyLocation( aSupplyLoc );
      } );
   }


   /**
    * Turn in inventory.
    *
    * @param aInventory
    *           the inventory to turn in
    * @param aLocation
    *           the location to turn in
    *
    * @throws Exception
    *            if an error occurs
    */
   private void turnIn( InventoryKey aInventory, LocationKey aLocation ) throws Exception {

      TurnInTO lTurnInTO = new TurnInTO();
      lTurnInTO.setInventory( aInventory );
      lTurnInTO.setTurnInQuantity( 1.0, "" );
      lTurnInTO.setCondition( RefInvCondKey.REPREQ, "" );
      lTurnInTO.setLocation( aLocation, "" );
      lTurnInTO.setCreditAccount( EXPENSE_ACC_CD, "" );
      lTurnInTO.setSerialNo( INV_SN, "" );
      lTurnInTO.setPartNo( PART_NO_OEM, "" );
      lTurnInTO.setManufacturer( MANUFACTURER.getCd(), "" );

      new TransferService().turnIn( lTurnInTO, iHr );
   }


   /**
    * Turn in inventory to different location than its current location.
    *
    * @param aIssuedInv
    *           the inventory key
    * @param aExpectedTotalQty
    *           the expected total quantity
    *
    * @throws Exception
    *            if an error occurs
    */
   private void turnInInvToDifferentLocAndAssert( boolean aIssuedInv, BigDecimal aExpectedTotalQty )
         throws Exception {

      // Data setup

      PartNoKey lTrackPart = createTrackPart();

      InventoryBuilder lInvBuilder = new InventoryBuilder().withPartNo( lTrackPart )
            .withClass( RefInvClassKey.TRK ).withSerialNo( INV_SN )
            .withCondition( RefInvCondKey.REPREQ ).atLocation( iAirport )
            .withOwner( new OwnerDomainBuilder().isDefault().build() ).withBinQt( 1.0 );

      if ( aIssuedInv ) {
         lInvBuilder = lInvBuilder.isIssued();
      }

      InventoryKey lInvToTurnIn = lInvBuilder.build();

      // create a supply location for different DOCK and USSTG locations
      LocationKey lDiffAirport = createAirport( AIRPORT_B );

      // create a different DOCK location for expected turn in
      LocationKey lDiffDockLoc = createDock( DOCK_LOC_CD_B, lDiffAirport );

      // create USSTG location for expected turn in transfer
      createUSSTGLocation( lDiffAirport );

      // Turn in the inventory with the part to a different location

      turnIn( lInvToTurnIn, lDiffDockLoc );

      // Assert the results

      assertEquals( aExpectedTotalQty, new EqpPartNo( lTrackPart ).getTotalQt() );
   }


   /**
    * this method creates part and the inventory for the part for the given inventory class and at
    * given location
    *
    * @param aInventoryClass
    *           part inventory class
    * @param aLocation
    *           the inventory location
    * @param aQuantity
    *           quantity
    * @return inventory key
    */
   private InventoryKey createInventory( RefInvClassKey aInventoryClass, LocationKey aLocation,
         double aQuantity ) {
      // create part and inventory for the part
      iPartA = new PartNoBuilder().withOemPartNo( "PARTA" ).withInventoryClass( aInventoryClass )
            .withStatus( RefPartStatusKey.ACTV ).withDefaultPartGroup().build();

      InventoryBuilder lBuilder = new InventoryBuilder().withPartNo( iPartA )
            .withCondition( RefInvCondKey.RFI ).atLocation( aLocation ).withOwner( iLocalOwner )
            .withClass( aInventoryClass ).withBinQt( aQuantity );
      // if Serialized inventory then set the reserved boolean
      if ( RefInvClassKey.SER.equals( aInventoryClass ) ) {
         lBuilder.isReserved();
      }

      return lBuilder.build();
   }


   /**
    * create batch inventory
    *
    * @param lBatchPart
    * @param lBinQty
    * @return
    */
   private InventoryKey createBatchInv( PartNoKey lBatchPart, double lBinQty ) {
      return new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withPartNo( lBatchPart )
            .withSerialNo( BATCH_INV_SN ).withCondition( RefInvCondKey.RFI ).atLocation( iAirport )
            .withOwner( new OwnerDomainBuilder().isDefault().build() ).withBinQt( lBinQty ).build();
   }


   /**
    * This method adds a second preferred store to the Line maintenance location
    *
    */
   private void addSecondPreferredLocation() {
      // create second preferred location
      iSecondPreferredStoreLocation = Domain.createLocation( loc -> {
         loc.setCode( SECOND_PREFSRVSTORE );
         loc.setType( RefLocTypeKey.SRVSTORE );
         loc.setParent( iAirportLocation );
         loc.setSupplyLocation( iAirportLocation );
      } );

      // add the the second preferred store location for Line location
      InvLocPrefMapKey lInvLocPrefMapKey =
            new InvLocPrefMapKey( iLineLocation, iSecondPreferredStoreLocation );
      InvLocPrefMapTable lInvLocPrefMapTable = InvLocPrefMapTable.create( lInvLocPrefMapKey );
      lInvLocPrefMapTable.setPriorityOrder( 2 );
      lInvLocPrefMapTable.insert();
   }


   /**
    * Creates the transfer for given inventory to the given destination location
    *
    * @param aInventory
    *           the inventory key
    * @param aDestination
    *           the destination location
    * @param aQuantity
    *           the quantity to transfer
    * @return the transfer key
    * @throws Exception
    */
   private TransferKey createTransfer( InventoryKey aInventory, String aDestination,
         double aQuantity ) throws Exception {

      CreateTransferTO lTO = new CreateTransferTO();
      lTO.setTransferToCd( aDestination );
      lTO.setInventory( aInventory );
      lTO.setQuantity( aQuantity );
      // create transfer and return the transfer key
      return TransferService.create( lTO );
   }


   /**
    * find batch inventories given part no and batch no
    *
    * @param aBatchPart
    * @param aBatchNo
    * @return
    */
   private QuerySet findBatchInventoriesByPartNoAndBatchNo( PartNoKey aBatchPart,
         String aBatchNo ) {
      QuerySet lDs;
      // columns to SELECT
      String[] lCols = { "inv_no_db_id", "inv_no_id" };

      // where arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aBatchPart, "part_no_db_id", "part_no_id" );
      lArgs.add( "serial_no_oem", aBatchNo );
      lArgs.add( "inv_class_db_id", "0" );
      lArgs.add( "inv_class_cd", "BATCH" );

      // get the inventory row
      lDs = QuerySetFactory.getInstance().executeQuery( lCols, "inv_inv", lArgs );
      return lDs;
   }


   /**
    * get newly created inventory from the data set
    *
    * @param aOriginalBatchInv
    * @param aDs
    * @return
    */
   private InventoryKey getNewlyCreatedInv( InventoryKey aOriginalBatchInv, QuerySet aDs ) {

      InventoryKey lNewlyCreatedInvKey = null;
      while ( aDs.next() ) {
         InventoryKey lInvKey = aDs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );
         // exclude original batch inventory
         if ( !lInvKey.equals( aOriginalBatchInv ) ) {
            lNewlyCreatedInvKey = lInvKey;
         }
      }
      return lNewlyCreatedInvKey;
   }
}
