
package com.mxi.mx.core.services.part.classchanger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.EqpKitPartGroupKey;
import com.mxi.mx.core.key.EqpPartNoLogKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefLogActionKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.CoreLicenseStub.FeatureSet;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.part.CannotChangeSerialToBatchWhenBorrowOrderException;
import com.mxi.mx.core.services.part.KitUtils;
import com.mxi.mx.core.services.part.PartHasPartRequestsInAwaitingIssueException;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;
import com.mxi.mx.core.services.part.classchange.SerializedToBatchChanger;
import com.mxi.mx.core.services.part.classchange.TrackedToSerializedChanger;
import com.mxi.mx.core.table.eqp.EqpKitPartGroupTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNo;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNoLog;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;


/**
 * This tests the SerializedToBatchChanger class
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SerializedToBATCHChangerTest {

   private static final String WING_FLAP = "wing_flap";
   private static final BigDecimal PART_TOTAL_QT = new BigDecimal( 2 );
   private AssemblyKey iAcftAssembly;
   private ConfigSlotKey iAcftConfigSlot;
   private ConfigSlotPositionKey iAcftConfigSlotPos;
   private InventoryKey iAircraft;
   private HumanResourceKey iHR;
   private LocationKey iLocation;
   private PartNoKey iTRKPart;
   private PartNoKey iSERPart;
   private ConfigSlotKey iTrkConfigSlot;
   private ConfigSlotPositionKey iTrkConfigSlotPos;
   private InventoryKey iTrkInventory;
   private PartGroupKey iTrkPartGroup;
   private LocationKey iDockLocation;
   private final RefQtyUnitKey BOX = new RefQtyUnitKey( 10, "BOX" );
   public static final double CONVERSION_FACTOR = 5.0;
   private final BigDecimal PART_VALUE = new BigDecimal( 10 );
   private final Double MAX_QT = 10.00;
   private final Double MIN_QT = 1.00;
   private final Double EXPECTED_MAX_QT = 50.00;
   private final Double EXPECTED_MIN_QT = 5.00;
   private final Double INITIAL_QTY = 10.00;
   private final double EXPECTED_QTY = 50.00;
   private EqpPartNoTable iEqpPartNo;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUser = new OperateAsUserRule( 99999, "currentuser" );


   @Before
   public void setUp() {
      MxCoreLicense.setValidator( new CoreLicenseStub( FeatureSet.NONE ) );

      Domain.createHumanResource( hr -> {
         hr.setUser( new UserKey( 99999 ) );
      } );
   }


   @After
   public void tearDown() {
      MxCoreLicense.setValidator( null );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      // build an aircraft
      iAcftAssembly = new AssemblyBuilder( "AAC01" ).build();

      iAcftConfigSlot = new ConfigSlotBuilder( "ACS01" ).withClass( RefBOMClassKey.ROOT )
            .withRootAssembly( iAcftAssembly ).build();

      iAcftConfigSlotPos = new ConfigSlotPositionKey( iAcftConfigSlot, 1 );

      iAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .withDescription( "Aircraft Inventory Test" )
            .withConfigSlotPosition( iAcftConfigSlotPos ).withOriginalAssembly( iAcftAssembly )
            .withSerialNo( "SN001" ).build();

      // build a config slot on aircraft to attach a inventory
      iTrkConfigSlot = new ConfigSlotBuilder( "TRKCSCD" ).withClass( RefBOMClassKey.TRK )
            .withRootAssembly( iAcftAssembly ).build();

      iTrkConfigSlotPos = new ConfigSlotPositionBuilder().withConfigSlot( iTrkConfigSlot ).build();

      // build a TRK part
      iTRKPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withShortDescription( "Part A" )
            .withAbcClass( RefAbcClassKey.A ).withTotalQuantity( PART_TOTAL_QT ).build();

      // build a serial part
      iSERPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withShortDescription( "SER" ).withAbcClass( RefAbcClassKey.A ).build();

      // build a part group
      iTrkPartGroup = new PartGroupDomainBuilder( "PARTGC" ).withConfigSlot( iTrkConfigSlot )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( iTRKPart ).build();

      // build a location
      iLocation = Domain.createLocation( aSupplyLocation -> {
         aSupplyLocation.setCode( "supplyloc" );
         aSupplyLocation.setType( RefLocTypeKey.AIRPORT );
         aSupplyLocation.setIsSupplyLocation( true );
      } );

      // create inventory based on the given part, and attach it to aircraft
      iTrkInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withConfigSlotPosition( iTrkConfigSlotPos ).withPartGroup( iTrkPartGroup )
            .withPartNo( iTRKPart ).withOriginalAssembly( iAcftAssembly )
            .withAssemblyInventory( iAircraft ).withParentInventory( iAircraft )
            .atLocation( iLocation ).build();

      // create a dock location
      iDockLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.DOCK );
         aLocationConfiguration.setIsSupplyLocation( true );
         aLocationConfiguration.setCode( "Dock" );
      } );
   }


   /**
    * This scenario tests the conversion of a part from TRK to SER class and then, again from SER to
    * a BATCH class. The aircraft inventory is locked (ARCHIVE)thus, all the sub-inventories are
    * locked.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testChangePartClassFromSERToBATCHWhenAircraftIsLocked()
         throws MxException, TriggerException {

      // archive aircraft inventory
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iAircraft );
      lInvInvTable.setLockedBool( true );
      lInvInvTable.setInvCond( RefInvCondKey.ARCHIVE );
      lInvInvTable.update();

      // assert aircraft is locked
      assertTrue( InvInvTable.findByPrimaryKey( iAircraft ).isLocked() );

      // archive the inventory, since aircraft is locked
      lInvInvTable = InvInvTable.findByPrimaryKey( iTrkInventory );
      lInvInvTable.setLockedBool( true );
      lInvInvTable.setInvCond( RefInvCondKey.ARCHIVE );
      lInvInvTable.update();

      // assert inventory is locked
      assertTrue( InvInvTable.findByPrimaryKey( iTrkInventory ).isLocked() );

      // call service class to change part to SER
      new TrackedToSerializedChanger( iTRKPart, false, iHR ).change();

      // assert that part class changed to SER
      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( iTRKPart );
      assertTrue( iEqpPartNo.getInvClass().equals( RefInvClassKey.SER ) );

      // call service class to change part to BATCH
      new SerializedToBatchChanger( iTRKPart, iHR, BOX, CONVERSION_FACTOR ).change();

      // assert that part class changed to BATCH
      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( iTRKPart );
      assertTrue( iEqpPartNo.getInvClass().equals( RefInvClassKey.BATCH ) );
      assertEquals( iEqpPartNo.getQtyUnit(), BOX );
   }


   /**
    * This scenario tests the conversion of a part from TRK to SER class and then, again from SER to
    * a BATCH class. The aircraft inventory is not locked.
    *
    * @throws MxException
    * @throws TriggerException
    */

   public void testChangePartClassFromSERToBATCHWhenAircraftNotLocked()
         throws MxException, TriggerException {

      // assert aircraft is not locked
      assertFalse( InvInvTable.findByPrimaryKey( iAircraft ).isLocked() );

      // assert inventory is not locked
      assertFalse( InvInvTable.findByPrimaryKey( iTrkInventory ).isLocked() );

      // call service class to change part to SER
      new TrackedToSerializedChanger( iTRKPart, false, iHR ).change();

      // assert that part class changed to SER
      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( iTRKPart );
      assertTrue( iEqpPartNo.getInvClass().equals( RefInvClassKey.SER ) );

      // call service class to change part to BATCH
      new SerializedToBatchChanger( iTRKPart, iHR, null, CONVERSION_FACTOR ).change();

      // assert that part class changed to BATCH
      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( iTRKPart );
      assertTrue( iEqpPartNo.getInvClass().equals( RefInvClassKey.BATCH ) );
   }


   /**
    * This scenario tests the conversion of a part from SER to BATCH class when the part is
    * associated with a part request with awaiting issue status.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testPartHasPartRequestsInAwaitingIssueException()
         throws MxException, TriggerException {

      // assert serialize part is properly created
      EqpPartNo lEqpPartNo = new EqpPartNo( iSERPart );
      lEqpPartNo.assertInventoryClass( RefInvClassKey.SER.getCd() );

      /** create a part request **/

      // create a part requirement
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( new TaskKey( 1, 1 ) )
            .withInstallPart( iSERPart ).withInstallQuantity( 1.0 ).build();

      // create a part request with the part requirement
      PartRequestKey lPartRequestKey = new PartRequestBuilder()
            .forPartRequirement( new TaskInstPartKey( lTaskPartKey, 1 ) )
            .withStatus( RefEventStatusKey.PRAWAITISSUE ).withType( RefReqTypeKey.TASK ).build();

      EvtEventUtil lEvtEvent = new EvtEventUtil( lPartRequestKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRAWAITISSUE );

      // call service class to change part to BATCH
      try {
         new SerializedToBatchChanger( iSERPart, iHR, null, CONVERSION_FACTOR ).change();

         fail( "Expected PartHasPartRequestsInAwaitingIssueException." );
      } catch ( PartHasPartRequestsInAwaitingIssueException lE ) {

         // assert part is still serialize
         lEqpPartNo = new EqpPartNo( iSERPart );
         lEqpPartNo.assertInventoryClass( RefInvClassKey.SER.getCd() );
      }
   }


   /**
    * This scenario tests the conversion of a part from SER to BATCH class when the part has an open
    * borrow order.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testCannotChangeSerialToBatchWhenBorrowOrderException()
         throws MxException, TriggerException {

      // assert serialize part is properly created
      EqpPartNo lEqpPartNo = new EqpPartNo( iSERPart );
      lEqpPartNo.assertInventoryClass( RefInvClassKey.SER.getCd() );

      /** create a borrow order **/
      // build borrow vendor
      VendorKey lVendor = new VendorBuilder().withCode( "TESTVENDOR" )
            .atLocation( Domain.createLocation( aVendorLocation -> {
               aVendorLocation.setCode( "vendorloc" );
               aVendorLocation.setType( RefLocTypeKey.VENDOR );
            } ) ).build();

      // build borrow order with status OPEN
      PurchaseOrderKey lOrder = new OrderBuilder().withVendor( lVendor ).withIssueDate( new Date() )
            .shippingTo( iLocation ).withStatus( RefEventStatusKey.POOPEN )
            .withOrderType( RefPoTypeKey.BORROW ).build();

      // add in borrow order line
      new OrderLineBuilder( lOrder ).withLineType( RefPoLineTypeKey.BORROW ).forPart( iSERPart )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withOrderQuantity( BigDecimal.ONE ).promisedBy( new Date() ).build();

      // call service class to change part to BATCH
      try {
         new SerializedToBatchChanger( iSERPart, iHR, null, CONVERSION_FACTOR ).change();

         fail( "Expected CannotChangeSerialToBatchWhenBorrowOrderException." );
      } catch ( CannotChangeSerialToBatchWhenBorrowOrderException lE ) {

         // assert part is still serialize
         lEqpPartNo = new EqpPartNo( iSERPart );
         lEqpPartNo.assertInventoryClass( RefInvClassKey.SER.getCd() );
      }
   }


   /**
    * GIVEN a serialized part WHEN the inventory class of the part is changed from SER to BATCH THEN
    * tests the audit message log for changing the unit of measure code.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testAuditLogforUoMChange() throws MxException, TriggerException {

      // create a serialize part
      iSERPart = createSerialPart( PART_TOTAL_QT, RefQtyUnitKey.EA );

      // fetch the original unit of measure of the part.
      String lOrigQtyUnitCd = getUomOfPart( iSERPart );

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      // fetch the new unit of measure of the part.
      String lNewQtyUnitCd = getUomOfPart( iSERPart );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iSERPart.getPKWhereArg() );

      QuerySet lDs = QuerySetFactory.getInstance().executeQueryTable( "EQP_PART_NO_LOG", lArgs );

      if ( lDs.last() ) {
         EqpPartNoLog lEqpPartNoLogTable = new EqpPartNoLog( lDs.getKey( EqpPartNoLogKey.class,
               new String[] { "PART_NO_DB_ID", "PART_NO_ID", "PART_NO_LOG_ID" } ) );

         String lAuditMessage = MessageFormat.format( i18n.get( "core.msg.CHANGE_PART_NO_UOM" ),
               new Object[] { lOrigQtyUnitCd, lNewQtyUnitCd, CONVERSION_FACTOR, lNewQtyUnitCd } );

         lEqpPartNoLogTable.assertExist();
         lEqpPartNoLogTable.assertSystemNote( lAuditMessage );
         lEqpPartNoLogTable.assertLogAction( RefLogActionKey.CHANGEINVCLASS );
         lEqpPartNoLogTable.assertLogDate( new Date() );
         lEqpPartNoLogTable.assertLogReason( null );
         lEqpPartNoLogTable.assertUserNote( null );
      }
   }


   /**
    * GIVEN a serialized inventory and a shipment for the inventory WHEN the inventory class of the
    * part is changed from SER to BATCH THEN tests the audit log information is updated for the
    * original shipment.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testAuditLogOfShipmentForUoMChange() throws MxException, TriggerException {

      iSERPart = createSerialPart( PART_TOTAL_QT, RefQtyUnitKey.EA );

      // fetch the original unit of measure of the part.
      String lOrigQtyUnitCd = getUomOfPart( iSERPart );

      // create a shipment for the serialized inventory
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iDockLocation ).build();

      // create a shipment line for the shipment
      new ShipmentLineBuilder( lShipment ).forPart( iSERPart ).build();

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      // fetch the new unit of measure of the part.
      String lNewQtyUnitCd = getUomOfPart( iSERPart );

      // create history note after changing UoM
      String lMessage = i18n.get( "core.msg.SHIPMENT_LINE_UOM_CHANGE", iEqpPartNo.getPartNoOEM(),
            lOrigQtyUnitCd, lNewQtyUnitCd, CONVERSION_FACTOR, lNewQtyUnitCd );

      EventKey lEventKey = new EventKey( lShipment.getDbId(), lShipment.getId() );

      // fetch stage note from event stage table
      StageKey lStageKey = new StageKey( lEventKey, 1 );
      EvtStageTable lEvtStage = EvtStageTable.findByPrimaryKey( lStageKey );

      // assert entered actual history note and expected history note are same
      assertEquals( lMessage, lEvtStage.getStageNote() );

   }


   @Test
   public void testBinLevelUpdateSerializedToBatch() throws MxException, TriggerException {

      // create a serialize part
      iSERPart = createSerialPart( PART_VALUE, RefQtyUnitKey.EA );

      LocationKey lLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.BIN );
      } );
      // Create the bin level
      BinLevelTO lBinLevelTO = new BinLevelTO();
      lBinLevelTO.setMaxQt( MAX_QT, "Max" );
      lBinLevelTO.setMinQt( MIN_QT, "Min" );
      new BinLevelService().add( iSERPart, lLocationKey, lBinLevelTO );

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iSERPart.getPKWhereArg() );
      lArgs.add( lLocationKey.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "INV_LOC_BIN", lArgs );
      while ( lQs.next() ) {
         assertTrue( "Max quantity updated ", EXPECTED_MAX_QT == lQs.getDouble( "max_qt" ) );
         assertTrue( "Min quantity updated ", EXPECTED_MIN_QT == lQs.getDouble( "min_qt" ) );
      }
   }


   @Test
   public void testNonHistoricShipmentPreserveUnitOfMeasure()
         throws MxException, TriggerException, SQLException {

      // create a serialize part
      iSERPart = createSerialPart( PART_TOTAL_QT, RefQtyUnitKey.EA );

      ShipmentKey lShipmentKey = Domain.createShipment( aShipment -> {
         aShipment.setHistorical( true );
      } );
      ShipmentLineKey lShipmentLineKey = Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.part( iSERPart );
         aShipmentLine.shipmentKey( lShipmentKey );
      } );

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lShipmentLineKey.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE", lArgs );
      while ( lQs.next() ) {
         assertTrue( "Unit of measure not updated",
               RefQtyUnitKey.EA.getCd().equals( lQs.getString( "qty_unit_cd" ) ) );
      }
   }


   @Test
   public void testNonHistoricShipmentNotPreserveUnitOfMeasureSerializedTobatch()
         throws MxException, TriggerException, SQLException {

      // create a serialize part
      iSERPart = createSerialPart( new BigDecimal( 5 ), RefQtyUnitKey.EA );

      ShipmentKey lShipmentKey = Domain.createShipment( aShipment -> {
         aShipment.setHistorical( false );
      } );
      ShipmentLineKey lShipmentLineKey = Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.part( iSERPart );
         aShipmentLine.shipmentKey( lShipmentKey );
      } );

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lShipmentLineKey.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE", lArgs );
      while ( lQs.next() ) {
         assertTrue( "Unit of measure not updated",
               getUomOfPart( iSERPart ).equals( lQs.getString( "qty_unit_cd" ) ) );
      }
   }


   @Test
   public void testUpdateStockRequests() throws MxException, TriggerException {

      // create a serialize part
      iSERPart = createSerialPart( new BigDecimal( 5 ), BOX );

      PartRequestKey lPartRequest = Domain.createPartRequest( aPartRequest -> {
         aPartRequest.type( RefReqTypeKey.STOCK );
         aPartRequest.purchasePart( iSERPart );
         aPartRequest.requestedQuantity( INITIAL_QTY );
      } );

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      assertEquals( EXPECTED_QTY, ReqPartTable.findByPrimaryKey( lPartRequest ).getReqQt(), 0 );
   }


   /**
    * GIVEN a serialized part WHEN the inventory class of the part is changed from SER to BATCH THEN
    * tests the history note for changing the unit of measure code for relevant part requests.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testPartRequestHistoryNoteforUoMChange() throws MxException, TriggerException {

      // create a serialize part
      iSERPart = createSerialPart( PART_TOTAL_QT, RefQtyUnitKey.EA );

      // create ADHOC part request
      PartRequestKey iPartReqKeyAdhoc = Domain.createPartRequest( aPartReqKey -> {
         aPartReqKey.purchasePart( iSERPart );
         aPartReqKey.type( RefReqTypeKey.ADHOC );
         aPartReqKey.status( RefEventStatusKey.PRCOMPLETE );
      } );

      // create STOCK part request
      PartRequestKey iPartReqKeyStock = Domain.createPartRequest( aPartReqKey -> {
         aPartReqKey.purchasePart( iSERPart );
         aPartReqKey.type( RefReqTypeKey.ADHOC );
         aPartReqKey.status( RefEventStatusKey.PRCOMPLETE );
      } );

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      // get ADHOC part request note
      EventKey lEventKeyAdhoc =
            new EventKey( iPartReqKeyAdhoc.getDbId(), iPartReqKeyAdhoc.getId() );
      StageKey lStageKeyAdhoc = new StageKey( lEventKeyAdhoc, 1 );
      EvtStageTable lEvtStageAdhoc = EvtStageTable.findByPrimaryKey( lStageKeyAdhoc );
      String lStageNoteAdhoc = lEvtStageAdhoc.getStageNote();

      // get STOCK part request note
      EventKey lEventKeyStock =
            new EventKey( iPartReqKeyStock.getDbId(), iPartReqKeyStock.getId() );
      StageKey lStageKeyStock = new StageKey( lEventKeyStock, 1 );
      EvtStageTable lEvtStageStock = EvtStageTable.findByPrimaryKey( lStageKeyStock );
      String lStageNoteStock = lEvtStageAdhoc.getStageNote();

      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( iSERPart );

      String lHistory = i18n.get( "core.msg.CHANGE_PART_REQ_UOM", iEqpPartNo.getPartNoOEM(),
            RefQtyUnitKey.EA.getCd(), BOX.getCd(), CONVERSION_FACTOR, BOX.getCd() );

      // assert that history note added ADHOC part request added
      assertTrue( lHistory.equals( lStageNoteAdhoc ) );

      // assert that history note for STOCK part request added
      assertTrue( lHistory.equals( lStageNoteStock ) );
   }


   @Test
   public void testUpdateKitQuantity() throws MxException, TriggerException {
      PartNoKey aKitPart = Domain.createPart( lKitPart -> {
         lKitPart.setInventoryClass( RefInvClassKey.KIT );
      } );

      iSERPart = createSerialPart( PART_TOTAL_QT, RefQtyUnitKey.EA );

      PartGroupKey lSerPartGroupKey = Domain.createPartGroup( apartGroup -> {
         apartGroup.addPart( iSERPart );
      } );
      new KitBuilder( aKitPart ).withContent( iSERPart, lSerPartGroupKey )
            .withKitQuantity( INITIAL_QTY ).build();

      // call service class to change part to BATCH.
      new SerializedToBatchChanger( iSERPart, iHR, BOX, CONVERSION_FACTOR ).change();

      QuerySet lQs = KitUtils.getKitPartGroupByPartNo( iSERPart );
      while ( lQs.next() ) {
         Double lActualQuantity = EqpKitPartGroupTable
               .findByPrimaryKey( new EqpKitPartGroupKey( lQs.getInt( "eqp_kit_part_group_db_id" ),
                     lQs.getInt( "eqp_kit_part_group_id" ) ) )
               .getKitQty();
         assertEquals( EXPECTED_QTY, lActualQuantity, 0 );
      }

   }


   /**
    * Create a serialized part
    *
    * @param aPartTotalQt
    *           the total quantity
    * @param aQtyUnit
    *           part's quantity unit type
    * @return primary key of an event
    */
   private PartNoKey createSerialPart( BigDecimal aPartTotalQt, RefQtyUnitKey aQtyUnit ) {
      return new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).withOemPartNo( WING_FLAP )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( aQtyUnit )
            .withTotalQuantity( aPartTotalQt ).build();
   }


   /**
    * Fetch unit of measure of the part
    *
    * @param lPartNo
    *           primary key of an event
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   private String getUomOfPart( PartNoKey lPartNo ) throws MxException, TriggerException {
      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( lPartNo );
      String QtyUnitCd = iEqpPartNo.getQtyUnitCd();
      return QtyUnitCd;
   }

}
