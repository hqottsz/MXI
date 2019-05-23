package com.mxi.mx.core.services.shipment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.alert.AlertEngine;
import com.mxi.mx.common.alert.MxAlertEngine;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.alert.po.InventoryNotReInductedAlert;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This class tests the shipment line service. In the code, you may see the ShipmentService is
 * called instead of ShipmentLineSerice because any changes in the ShipmentLineService we need to
 * use ShipmentSerice as entry point to test.
 */
public final class ShipmentLineServiceTest {

   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;

   private static final String SERIAL_NO_OEM = "ABC";
   private static final String PART_NO_OEM = "DEF";
   private static final String PART_NO_ALT_ONE = "ALT_ONE";
   private static final String PART_NO_ALT_TWO = "ALT_TWO";
   protected static final String POSITION_CODE = "POSITION_CODE";
   protected static final String PART_GROUP = "PART_GROUP";
   private static final String CONFIG_SLOT_POS = "CONFIG_SLOT_POS";
   private static final String CONFIG_SLOT_CD = "SLOT_CD";
   private static final String ENG_ASSMBL_CD = "CFM380";
   private static final String PARAMETER_NAME = "MATCH_RECORDS_PG_MF_SN";
   private static final String PO_NUMBER = "P100300";
   private static final String INV_DESC = "INV_DESC";
   private static final String INVASSET_ACCOUNT = "INVASSET";

   private static final ManufacturerKey MANUFACTURER = new ManufacturerKey( 0, "MXI" );
   private static final String PART_SDESC = "PART_SHORT_DESCRIPTION";

   private LocationKey iDockLocation;
   private LocationKey iVendorLocation;
   private LocationKey iQuarLocation;
   private HumanResourceKey iHr;
   private OwnerKey iOwner;
   private ShipmentLineService iShipmentLineService;
   private AlertEngine iOrginalAlertEngine;
   private OrgKey iReceiptOrganization;
   private VendorKey iVendor;
   private FncAccountKey iAccount;

   private InvCndChgEventDao iInvCndChgEventDao;
   private InvCndChgEventTable iInvCndChgEventTable;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   private ShipmentKey iShipment;
   private List<ShipmentLineKey> iShipmentLines;

   // for test receiving shipment line for stock request
   private PartRequestKey iStockRequest;
   private ShipmentLineKey iShipmentLine;
   private ReceiveShipmentLineTO iLineTo;
   private LocationKey iSupplyLocation;
   private int iUserId;


   /**
    *
    * Test adding a part shipment line for a tracked part.
    *
    * @throws MxException
    */
   @Test
   public void testAddPartShipmentLine_TRK() throws Exception {

      double lQuantity = 3.0;
      PartNoKey lTrackPart = createPartBuilder( RefInvClassKey.TRK ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, null, null, null, null,
            lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one part shipment line was created per quantity (since the part is TRK)
      assertEquals( ( int ) lQuantity, iShipmentLines.size() );
      assertShipmentLines( lTrackPart, new Double( 1.0 ), null );

   }


   /**
    *
    * Test adding a part shipment line for a serialized part.
    *
    * @throws MxException
    */
   @Test
   public void testAddPartShipmentLine_SER() throws Exception {

      double lQuantity = 3.0;
      PartNoKey lSerializedPart = createPartBuilder( RefInvClassKey.SER ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, null, null, null, null,
            lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one part shipment line was created per quantity (since the part is SER)
      assertEquals( ( int ) lQuantity, iShipmentLines.size() );
      assertShipmentLines( lSerializedPart, new Double( 1.0 ), null );
   }


   /**
    *
    * Test adding a part shipment line for a batch part.
    *
    * @throws MxException
    */
   @Test
   public void testAddPartShipmentLine_BATCH() throws Exception {

      double lQuantity = 15.0;
      PartNoKey lBatchPart = createPartBuilder( RefInvClassKey.BATCH ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, null, null, null, null,
            lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that only one part shipment line was created (since part is BATCH)
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLines( lBatchPart, lQuantity, null );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing serialized inventory item, using
    * inventory key directly
    *
    * @throws Exception
    */
   @Test
   public void testAddExistingInventoryShipmentLineUsingInventoryKey_SER() throws Exception {

      Double lQuantity = 1.0;
      PartNoKey lSerializedPart =
            createPartBuilder( RefInvClassKey.SER ).manufacturedBy( MANUFACTURER ).build();
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, lSerializedPart, lQuantity ).build();
      iShipmentLineService.addShipmentLineWithInventory( iShipment, lInventory, lQuantity, iHr );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLines( lSerializedPart, lQuantity, lInventory );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing batch inventory item, using inventory
    * key directly
    *
    * @throws Exception
    */
   @Test
   public void testAddExistingInventoryShipmentLineUsingInventoryKey_BATCH() throws Exception {

      Double lQuantity = new Double( 5.0 );
      PartNoKey lBatchPart =
            createPartBuilder( RefInvClassKey.BATCH ).manufacturedBy( MANUFACTURER ).build();
      InventoryKey lInventory = createInventory( RefInvClassKey.BATCH, lBatchPart, lQuantity )
            .withOwner( iOwner ).build();
      iShipmentLineService.addShipmentLineWithInventory( iShipment, lInventory, lQuantity, iHr );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLines( lBatchPart, lQuantity, lInventory );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing tracked inventory item.
    *
    * @throws Exception
    */
   @Test
   public void testAddExistingInventoryShipmentLine_TRK() throws Exception {

      Double lQuantity = 1.0;
      PartNoKey lTrackedPart =
            createPartBuilder( RefInvClassKey.TRK ).manufacturedBy( MANUFACTURER ).build();
      InventoryKey lInventory =
            createInventory( RefInvClassKey.TRK, lTrackedPart, lQuantity ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, SERIAL_NO_OEM, null, null,
            RefInvCondKey.RFI.getCd(), lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLines( lTrackedPart, lQuantity, lInventory );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing serialized inventory item.
    *
    * @throws Exception
    */
   @Test
   public void testAddExistingInventoryShipmentLine_SER() throws Exception {

      Double lQuantity = 1.0;
      PartNoKey lSerializedPart =
            createPartBuilder( RefInvClassKey.SER ).manufacturedBy( MANUFACTURER ).build();
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, lSerializedPart, lQuantity ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, SERIAL_NO_OEM, null, null,
            RefInvCondKey.RFI.getCd(), lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLines( lSerializedPart, lQuantity, lInventory );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing batch inventory item.
    *
    * @throws Exception
    */
   @Test
   public void testAddExistingInventoryShipmentLine_BATCH() throws Exception {

      Double lQuantity = new Double( 5.0 );
      PartNoKey lBatchPart =
            createPartBuilder( RefInvClassKey.BATCH ).manufacturedBy( MANUFACTURER ).build();
      InventoryKey lInventory = createInventory( RefInvClassKey.BATCH, lBatchPart, lQuantity )
            .withOwner( iOwner ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, SERIAL_NO_OEM, null, null,
            RefInvCondKey.RFI.getCd(), lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLines( lBatchPart, lQuantity, lInventory );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing batch inventory item.
    *
    * @throws Exception
    */
   @Test
   public void testAddNewInventoryShipmentLine_SER() throws Exception {

      Double lQuantity = 1.0;
      PartNoKey lSerializedPart =
            createPartBuilder( RefInvClassKey.SER ).withShortDescription( PART_SDESC )
                  .withAverageUnitPrice( BigDecimal.ONE ).withTotalValue( BigDecimal.ONE )
                  .withTotalQuantity( BigDecimal.ONE ).manufacturedBy( MANUFACTURER ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, SERIAL_NO_OEM, null, null,
            RefInvCondKey.RFI.getCd(), lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLineWithNewInventory( lSerializedPart, lQuantity, RefInvClassKey.SER,
            RefInvCondKey.RFI );
   }


   /**
    *
    * Test adding an inventory shipment line for an existing batch inventory item.
    *
    * @throws Exception
    */
   @Test
   public void testAddNewInventoryShipmentLine_BATCH() throws Exception {

      Double lQuantity = 15.0;
      PartNoKey lBatchPart =
            createPartBuilder( RefInvClassKey.BATCH ).withShortDescription( PART_SDESC )
                  .withAverageUnitPrice( BigDecimal.ONE ).withTotalValue( BigDecimal.ONE )
                  .withTotalQuantity( BigDecimal.ONE ).manufacturedBy( MANUFACTURER ).build();
      iShipmentLineService.addShipmentLine( iShipment, PART_NO_OEM, SERIAL_NO_OEM, null, null,
            RefInvCondKey.RFI.getCd(), lQuantity, iHr, null );
      iShipmentLines = ShipmentLineService.getShipmentLine( iShipment );

      // assert that one inventory shipment line was created
      assertEquals( 1, iShipmentLines.size() );
      assertShipmentLineWithNewInventory( lBatchPart, lQuantity, RefInvClassKey.BATCH,
            RefInvCondKey.RFI );
   }


   /*
    * GIVEN an inventory with a received date, WHEN the inventory is sent out for repair and
    * received, THEN the inventory's received date should not be changed.
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testInvReceiveDtWhenReceiveInvFromRo() throws Exception {

      Double lQuantity = 1.0;
      Date invReceivedDt = DateUtils.parseDateTimeString( "10-JUL-2018 08:00" );

      PartNoKey lSerPart = createPartBuilder( RefInvClassKey.SER ).build();
      InventoryKey lInventory = createInventory( RefInvClassKey.SER, lSerPart, lQuantity )
            .receivedOn( invReceivedDt ).build();

      PurchaseOrderKey lRepairOrder =
            new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR ).build();
      PurchaseOrderLineKey lOrderLine =
            new OrderLineBuilder( lRepairOrder ).forPart( lSerPart ).build();

      iShipment = createShipmentFromVendorToDock();

      ReceiveShipmentLineTO lLineTo =
            receiveShipmentlineToSetup( lQuantity, lSerPart, lOrderLine, lInventory );
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );

      InvInvTable receivedInventory = InvInv.findByPrimaryKey( getInventoryFromShipment() );

      // assert inventory's received date is not changed
      assertEquals( receivedInventory.getReceivedDt(), invReceivedDt );
   }


   /**
    *
    * Test receiving a shipment with a part and serial number already existing in the system, where
    * the Manufactured date entered on the shipment is different from the part and the user does not
    * have permission to change manufactured date
    *
    * @throws Exception
    */
   @Test
   public void receiveNewPart_WithDirectMatchAndExistingManufacturedDateNoPermission()
         throws Exception {
      final PartNoKey lPartNo = createPartBuilder( PART_NO_ALT_ONE ).build();

      Calendar lManufacturedDate = Calendar.getInstance();

      // Preset the inventory manufactured date to a week ago
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, -7 );
      Date lInvManufacturedDate = lManufacturedDate.getTime();

      // Preset the Maufactured date to be used on the shipment as 4 days after the actual
      // inventories manufactured date
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, 4 );
      Date lShipRecievedManufacturedDate = lManufacturedDate.getTime();

      // create archived inventory
      InventoryKey lArchivedInventory =
            createArchivedInventory( lPartNo ).manufacturedOn( lInvManufacturedDate ).build();

      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lPartNo );
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      lLineTo.setManufacturedDate( lShipRecievedManufacturedDate );

      iShipmentLineService.receiveNewPart( iShipment, lLineTo, iDockLocation, RefInvCondKey.INSPREQ,
            null, 0, true, iHr, true, false );

      InvInv lRecievedInventory = new InvInv( getInventoryFromShipment() );

      SimpleDateFormat lDateFormatter = new SimpleDateFormat( "dd-MM-yyyy" );

      // Assert that the received inventory manufactured date didn't change as a result of receiving
      // it on the shipment
      assertEquals( "Inventory Manufactured Date changed when expecting it not too",
            lDateFormatter.format( lRecievedInventory.getManufactDt() ),
            lDateFormatter.format( lInvManufacturedDate ) );

   }


   /**
    *
    * Test receiving a shipment with a part and serial number already existing in the system, where
    * the Manufactured date entered on the shipment is different from the part and the user has
    * permission to change manufactured date
    *
    * @throws Exception
    */
   @Test
   public void receiveNewPart_WithDirectMatchAndExistingManufacturedDateWithPermission()
         throws Exception {
      final PartNoKey lPartNo = createPartBuilder( PART_NO_ALT_ONE ).build();

      UserParametersStub lUserParametersStub =
            new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            true );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", lUserParametersStub );

      Calendar lManufacturedDate = Calendar.getInstance();

      // Preset the inventory manufactured date to a week ago
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, -7 );
      Date lInvManufacturedDate = lManufacturedDate.getTime();

      // Preset the Maufactured date to be used on the shipment as 4 days after the actual
      // inventories manufactured date
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, 4 );
      Date lShipRecievedManufacturedDate = lManufacturedDate.getTime();

      // create archived inventory
      InventoryKey lArchivedInventory =
            createArchivedInventory( lPartNo ).manufacturedOn( lInvManufacturedDate ).build();

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( lPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iAccount );
         aPoLine.owner( iOwner );

      } );

      // Issue the EO to generate the inbound shipment
      OrderService iOrderService = new OrderService();
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine =
            ShipmentService.getShipmentLines( lInboundShipment )[0];

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( lShipRecievedManufacturedDate );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SERIAL_NO_OEM );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      ShipShipmentLineTable lShipShipmentLine = ShipShipmentLineTable
            .findByPrimaryKey( ShipmentLineService.getShipmentLine( lInboundShipment ).get( 0 ) );

      InvInv lRecievedInventory = new InvInv( lShipShipmentLine.getInventory() );

      SimpleDateFormat lDateFormatter = new SimpleDateFormat( "dd-MM-yyyy" );

      // Assert that the received inventory manufactured date didn't change as a result of receiving
      // it on the shipment
      assertEquals( "Inventory Manufactured Date did not change when expecting it too",
            lDateFormatter.format( lRecievedInventory.getManufactDt() ),
            lDateFormatter.format( lShipRecievedManufacturedDate ) );

   }


   /**
    *
    * Test receiving a shipment with a part and serial number already existing in the system, where
    * the Manufactured date entered on the shipment is different from the part and the user has
    * permission to change manufactured date
    *
    * @throws Exception
    */
   @Test
   public void
         receiveNewPart_WithDirectMatchAndExistingManufacturedDateWithPermissionAndEmptyShipmentManufacDate()
               throws Exception {
      final PartNoKey lPartNo = createPartBuilder( PART_NO_ALT_ONE ).build();

      UserParametersStub lUserParametersStub =
            new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            true );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", lUserParametersStub );

      Calendar lManufacturedDate = Calendar.getInstance();

      // Preset the inventory manufactured date to a week ago
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, -7 );
      Date lInvManufacturedDate = lManufacturedDate.getTime();

      // Preset the Maufactured date to be used on the shipment as 4 days after the actual
      // inventories manufactured date
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, 4 );
      Date lShipRecievedManufacturedDate = lManufacturedDate.getTime();

      // create archived inventory
      InventoryKey lArchivedInventory =
            createArchivedInventory( lPartNo ).manufacturedOn( lInvManufacturedDate ).build();

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( lPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iAccount );
         aPoLine.owner( iOwner );

      } );

      // Issue the EO to generate the inbound shipment
      OrderService iOrderService = new OrderService();
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine =
            ShipmentService.getShipmentLines( lInboundShipment )[0];

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( null );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SERIAL_NO_OEM );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      ShipShipmentLineTable lShipShipmentLine = ShipShipmentLineTable
            .findByPrimaryKey( ShipmentLineService.getShipmentLine( lInboundShipment ).get( 0 ) );

      InvInv lRecievedInventory = new InvInv( lShipShipmentLine.getInventory() );

      SimpleDateFormat lDateFormatter = new SimpleDateFormat( "dd-MM-yyyy" );

      // Assert that the received inventory manufactured date didn't change as a result of receiving
      // it on the shipment
      assertEquals( "Inventory Manufactured Date changed to null when not expecting it too",
            lDateFormatter.format( lRecievedInventory.getManufactDt() ),
            lDateFormatter.format( lInvManufacturedDate ) );

   }


   /**
    *
    * Test receiving a shipment with a part and serial number already existing in the system, where
    * the Manufactured date entered on the shipment is different from the part and the user has
    * permission to change manufactured date, and the part has a Task Definition against it.
    *
    * @throws Exception
    */
   @Test
   public void
         receiveNewPart_WithDirectMatchAndExistingManufactDtWithPermissionAndEmptyShipmentManufacDtAndPartTaskDefn()
               throws Exception {
      final PartNoKey lPartNo = createPartBuilder( PART_NO_ALT_ONE ).build();

      Domain.createRequirementDefinition(
            ( RequirementDefinition aRequirementDefinitionBuilder ) -> {

               aRequirementDefinitionBuilder.setExecutable( true );
               aRequirementDefinitionBuilder.setScheduledFromManufacturedDate();
               aRequirementDefinitionBuilder.addPartNo( lPartNo );

            } );

      UserParametersStub lUserParametersStub =
            new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            true );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", lUserParametersStub );

      Calendar lManufacturedDate = Calendar.getInstance();

      // Preset the inventory manufactured date to a week ago
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, -7 );
      Date lInvManufacturedDate = lManufacturedDate.getTime();

      // Preset the Maufactured date to be used on the shipment as 4 days after the actual
      // inventories manufactured date
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, 4 );
      Date lShipRecievedManufacturedDate = lManufacturedDate.getTime();

      // create archived inventory
      InventoryKey lArchivedInventory =
            createArchivedInventory( lPartNo ).manufacturedOn( lInvManufacturedDate ).build();

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( lPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iAccount );
         aPoLine.owner( iOwner );

      } );

      // Issue the EO to generate the inbound shipment
      OrderService iOrderService = new OrderService();
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine =
            ShipmentService.getShipmentLines( lInboundShipment )[0];

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( null );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SERIAL_NO_OEM );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      ShipShipmentLineTable lShipShipmentLine = ShipShipmentLineTable
            .findByPrimaryKey( ShipmentLineService.getShipmentLine( lInboundShipment ).get( 0 ) );

      InvInv lRecievedInventory = new InvInv( lShipShipmentLine.getInventory() );

      SimpleDateFormat lDateFormatter = new SimpleDateFormat( "dd-MM-yyyy" );

      // Assert that the received inventory manufactured date didn't change as a result of receiving
      // it on the shipment
      assertEquals( "Inventory Manufactured Date changed to null when not expecting it too",
            lDateFormatter.format( lRecievedInventory.getManufactDt() ),
            lDateFormatter.format( lInvManufacturedDate ) );

   }


   /**
    *
    * Test receiving a shipment with a part , manufacturer and serial number with an existing
    * archived inventory belonging to the same part and serial number
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testReceiveNewPartWithDirectMatch() throws Exception {
      final PartNoKey lPartNo = createPartBuilder( PART_NO_ALT_ONE ).build();

      // create archived inventory
      InventoryKey lArchivedInventory = createArchivedInventory( lPartNo ).build();

      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lPartNo );
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      iShipmentLineService.receiveNewPart( iShipment, lLineTo, iDockLocation, RefInvCondKey.INSPREQ,
            null, 0, true, iHr, true, false );

      InventoryKey lInventory = getInventoryFromShipment();

      // Assert that the received inventory and unarchived inventory is same
      assertEquals( "New inventory created where archived inventory expected", lArchivedInventory,
            lInventory );

      assertNoteAndStageReason( lInventory, i18n.get( "core.msg.INV_UNARCH_MDIRECT", PO_NUMBER ),
            RefStageReasonKey.ACDMCH );
   }


   /**
    *
    * Test receiving a shipment with a part , manufacturer and serial number with an existing
    * archived inventory belonging to an alternate part in the same part group
    *
    * @throws Exception
    */
   @Test
   public void testReceiveNewPartWithIndirectMatch() throws Exception {

      enableConfigParmForInvMatching();

      final PartNoKey lAlternatePart = createPartBuilder( PART_NO_ALT_ONE ).build();

      final PartNoKey lReceivingPart = createPartBuilder( PART_NO_OEM ).build();

      final AssemblyKey lEngineAssy = setupEngineAssemblyData( lAlternatePart, lReceivingPart );

      // retrieve above created part group
      PartGroupKey lPartGroup = EqpBomPart.getBomPartKey( lEngineAssy, PART_GROUP );

      // Retrieve above created configuration slot position
      ConfigSlotPositionKey lConfigSlotPosition =
            EqpAssmblPos.getBomItemPositionKey( lPartGroup, CONFIG_SLOT_POS );

      // create archived inventory
      InventoryKey lArchivedInventory = createArchivedInventory( lAlternatePart )
            .withPartGroup( lPartGroup ).withConfigSlotPosition( lConfigSlotPosition ).build();

      receiveNewPart( lReceivingPart );

      InventoryKey lInventory = getInventoryFromShipment();

      // assert that the received inventory and archived inventory is same
      assertEquals( "New inventory created where archived inventory expected", lArchivedInventory,
            lInventory );

      // assert that received inventory's part no is changed
      assertEquals( "Archived part number expected", lReceivingPart,
            InvInvTable.findByPrimaryKey( lInventory ).getPartNo() );

      assertNoteAndStageReason( lInventory,
            i18n.get( "core.msg.INV_UNARCH_INDMATCH", PO_NUMBER, PART_NO_ALT_ONE, PART_NO_OEM ),
            RefStageReasonKey.ACINDMCH );
   }


   /**
    *
    * Test receiving a shipment with a part , manufacturer and serial number with an existing active
    * on-hand inventory belonging to an alternate part in the same part group
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testReceiveNewPartWithActiveOnHandInventoryMatch() throws Exception {

      enableConfigParmForInvMatching();

      // create part group
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP ).build();

      final PartNoKey lAlternatePart =
            createPartBuilder( PART_NO_ALT_ONE ).isAlternateIn( lPartGroup ).build();

      final PartNoKey lReceivingPart =
            createPartBuilder( PART_NO_OEM ).isAlternateIn( lPartGroup ).build();

      // create active on-hand inventory
      @SuppressWarnings( "unused" )
      InventoryKey lActiveOnhandInventory =
            createInventory( RefInvClassKey.SER, lAlternatePart, 0.0 ).withOwner( iOwner ).build();

      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lReceivingPart );

      // set a serial number
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );

      InventoryKey lInventory = getInventoryFromShipment();

      // get the latest event keys for the inventory creation
      List<InvCndChgEventKey> lLatestEvents = getLatestEventsForInv( lInventory, 2 );

      // get the latest event out of the events
      InvCndChgEventKey lLatestEvent = lLatestEvents.get( ( lLatestEvents.size() - 1 ) );

      iInvCndChgEventTable = iInvCndChgEventDao.create( lLatestEvent );

      // assert one quarantine event is generated for the existing inventory
      assertOnlyOneQuarantineEvent( lInventory );

      // assert the note of the quarantine event
      assertEventLdesc( i18n.get( "core.msg.INV_MATCH_ACT_IND_ONHAND", PO_NUMBER, PART_NO_OEM,
            SERIAL_NO_OEM ) );

   }


   /**
    *
    * Test receiving a shipment with a part , manufacturer and serial number with an existing active
    * installed inventory belonging to an alternate part in the same part group
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testReceiveNewPartWithActiveInstalledInventoryMatch() throws Exception {

      enableUserConfigParm();

      enableConfigParmForInvMatching();

      // create an assembly
      AssemblyKey lAssemblyKey =
            new AssemblyBuilder( ENG_ASSMBL_CD ).withClass( RefAssmblClassKey.ENG ).build();

      InventoryKey lAircraft = new InventoryBuilder().withOriginalAssembly( lAssemblyKey )
            .withOwner( iOwner ).atLocation( iDockLocation ).build();

      // create a config slot
      ConfigSlotKey lConfigSlot = new ConfigSlotBuilder( CONFIG_SLOT_CD )
            .withRootAssembly( lAssemblyKey ).withClass( RefBOMClassKey.ROOT ).build();

      // create part group
      PartGroupKey lPartGroup =
            new PartGroupDomainBuilder( PART_GROUP ).withConfigSlot( lConfigSlot ).build();

      // create alternative part and recieve part
      final PartNoKey lAlternatePart =
            createPartBuilder( PART_NO_ALT_ONE ).isAlternateIn( lPartGroup ).build();

      final PartNoKey lReceivingPart =
            createPartBuilder( PART_NO_OEM ).isAlternateIn( lPartGroup ).build();

      // create active installed inventory
      @SuppressWarnings( "unused" )
      InventoryKey lActiveInstalledInventory =
            createInventory( RefInvClassKey.SER, lAlternatePart, 0.0 )
                  .withConfigSlotPosition( new ConfigSlotPositionKey( lConfigSlot, 1 ) )
                  .withHighestInventory( lAircraft ).withOriginalAssembly( lAssemblyKey )
                  .withOwner( iOwner ).build();

      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lReceivingPart );

      // set a serial number
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );

      InventoryKey lInventory = getInventoryFromShipment();

      // get the latest event keys for the inventory creation
      List<InvCndChgEventKey> lLatestEvents = getLatestEventsForInv( lInventory, 2 );

      // get the latest event out of the events
      InvCndChgEventKey lLatestEvent = lLatestEvents.get( ( lLatestEvents.size() - 1 ) );

      iInvCndChgEventTable = iInvCndChgEventDao.create( lLatestEvent );

      // assert the note of the quarantine event
      assertEventLdesc( i18n.get( "core.msg.INV_MATCH_ACT_IND_INSTALLED", PO_NUMBER, PART_NO_OEM,
            SERIAL_NO_OEM ) );
   }


   /**
    *
    * Test receiving a shipment with a part , manufacturer and serial number with an existing active
    * scrapped/condemned inventory belonging to an alternate part in the same part group
    *
    * @throws Exception
    */
   @Test
   public void testReceiveNewPartWithActiveScrappedOrCondemnedInventoryMatch() throws Exception {

      enableConfigParmForInvMatching();

      // create part group
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP ).build();

      final PartNoKey lAlternatePart =
            createPartBuilder( PART_NO_ALT_ONE ).isAlternateIn( lPartGroup ).build();

      final PartNoKey lReceivingPart =
            createPartBuilder( PART_NO_OEM ).isAlternateIn( lPartGroup ).build();

      // create active scrapped inventory
      @SuppressWarnings( "unused" )
      InventoryKey lActiveOnhandInventory =
            createInventory( RefInvClassKey.SER, lAlternatePart, 0.0 )
                  .withCondition( RefInvCondKey.SCRAP ).withOwner( iOwner ).build();

      receiveNewPart( lReceivingPart );

      InventoryKey lInventory = getInventoryFromShipment();

      // get the latest event keys for the inventory creation
      List<InvCndChgEventKey> lLatestEvents = getLatestEventsForInv( lInventory, 2 );

      // get the latest event out of the events
      InvCndChgEventKey lLatestEvent = lLatestEvents.get( ( lLatestEvents.size() - 1 ) );

      iInvCndChgEventTable = iInvCndChgEventDao.create( lLatestEvent );

      // assert one quarantine event is generated for the existing inventory
      assertOnlyOneQuarantineEvent( lInventory );

      // assert the note of the quarantine event
      assertEventLdesc(
            i18n.get( "core.msg.INV_MATCH_ACT_IND_SCCM", PO_NUMBER, PART_NO_OEM, SERIAL_NO_OEM ) );

   }


   /**
    *
    * Test receiving a shipment with a part , manufacturer and serial number with an existing active
    * multiple inventory belonging to an alternate part in the same part group
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testReceiveNewPartWithActiveMultipleInventoryMatch() throws Exception {

      // Enable the indirect matching rule
      enableConfigParmForInvMatching();

      // build receiving part
      PartNoKey lReceivingPart = createPartBuilder( PART_NO_OEM ).build();

      // create active inventories in two alternate parts
      String lActiveInventoryList =
            setupDataForFailedMatchWithMultipleActiveOrArchivedInventory( lReceivingPart, false );

      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lReceivingPart );

      // set a serial number
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );

      // retrieve the received inventory
      InventoryKey lReceivedInventory = getInventoryFromShipment();

      String lInventory = InvInvTable.findByPrimaryKey( lReceivedInventory ).getInvNoSdesc();

      // get the latest event keys for the inventory creation
      List<InvCndChgEventKey> lLatestEvents = getLatestEventsForInv( lReceivedInventory, 2 );

      // get the latest event out of the events
      InvCndChgEventKey lLatestEvent = lLatestEvents.get( ( lLatestEvents.size() - 1 ) );

      iInvCndChgEventTable = iInvCndChgEventDao.create( lLatestEvent );

      // assert one quarantine event is generated for the existing inventory
      assertOnlyOneQuarantineEvent( lReceivedInventory );

      // assert the note of the quarantine event
      assertEventLdesc( i18n.get( "core.msg.INV_ACTIVE_FLDMATCH", PO_NUMBER, lInventory,
            lActiveInventoryList ) );

   }


   /**
    *
    * Test one and only one quarantine event is generated when receiving batch as QUAR from purchase
    * order.
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testOneQuarEventWhenReceiveBatchQuarInvFromPo() throws Exception {
      PartNoKey lBatchPart = createPartBuilder( RefInvClassKey.BATCH ).build();
      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lBatchPart );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );
      InventoryKey lInventory = getInventoryFromShipment();

      // assert one and only one quarantine event is generated for this new inventory
      assertOnlyOneQuarantineEvent( lInventory );
      assertTwoMovements( lInventory );
   }


   /**
    *
    * Test one and only one quarantine event is generated when receiving serialized as QUAR from
    * purchase order.
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testOneQuarEventWhenReceiveSerializedQuarInvFromPo() throws Exception {

      PartNoKey lSerPart = createPartBuilder( RefInvClassKey.SER ).build();
      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( lSerPart );
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );
      InventoryKey lInventory = getInventoryFromShipment();

      // assert one and only one quarantine event is generated for this new inventory
      assertOnlyOneQuarantineEvent( lInventory );
      assertTwoMovements( lInventory );

   }


   /**
    *
    * Test one and only one quarantine event is generated when receiving serialized as QUAR from
    * repair order.
    *
    * @throws Exception
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testOneQuarEventWhenReceiveSerializedQuarInvFromRo() throws Exception {

      Double lQuantity = 1.0;
      PartNoKey lSerPart = createPartBuilder( RefInvClassKey.SER ).build();
      PurchaseOrderKey lRepairOrder =
            new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR ).build();
      PurchaseOrderLineKey lOrderLine =
            new OrderLineBuilder( lRepairOrder ).forPart( lSerPart ).build();
      InventoryKey lInventory = createInventory( RefInvClassKey.SER, lSerPart, lQuantity ).build();
      iShipment = createShipmentFromVendorToDock();
      ReceiveShipmentLineTO lLineTo =
            receiveShipmentlineToSetup( lQuantity, lSerPart, lOrderLine, lInventory );
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      // receive the shipment
      iShipmentLineService.receive( lLineTo, iDockLocation, new Date(), false, null, null, null,
            iHr );
      InventoryKey lNewInventory = getInventoryFromShipment();

      // assert one and only one quarantine event is generated for the original inventory
      assertEquals( lNewInventory, lInventory );
      assertOnlyOneQuarantineEvent( lNewInventory );
      assertTwoMovements( getInventoryFromShipment() );
   }


   @Test
   public void testPartQtyIndirectAchriveMatch() throws MxException, TriggerException {

      enableConfigParmForInvMatching();

      // Create existing part
      final PartNoKey lExistingPart =
            createPartBuilder( PART_NO_ALT_ONE ).withTotalQuantity( new BigDecimal( 0 ) ).build();

      // Create receiving part
      final PartNoKey lReceivingPart =
            createPartBuilder( PART_NO_OEM ).withTotalQuantity( new BigDecimal( 0 ) ).build();

      final AssemblyKey lEngineAssy = setupEngineAssemblyData( lExistingPart, lReceivingPart );

      // retrieve above created part group
      PartGroupKey lPartGroup = EqpBomPart.getBomPartKey( lEngineAssy, PART_GROUP );

      // Retrieve above created configuration slot position
      ConfigSlotPositionKey lConfigSlotPosition =
            EqpAssmblPos.getBomItemPositionKey( lPartGroup, CONFIG_SLOT_POS );

      // create archived inventory
      InventoryKey lArchivedInventory = createArchivedInventory( lExistingPart )
            .withPartGroup( lPartGroup ).withConfigSlotPosition( lConfigSlotPosition ).build();

      // Assert qty
      EqpPartNoTable lReceivingEqpPartNo = EqpPartNoTable.findByPrimaryKey( lReceivingPart );
      assertEquals( lReceivingEqpPartNo.getTotalQt(), BigDecimal.ZERO );

      EqpPartNoTable lExistingEqpPartNo = EqpPartNoTable.findByPrimaryKey( lExistingPart );
      assertEquals( lExistingEqpPartNo.getTotalQt(), BigDecimal.ZERO );

      // Receive shipment
      receiveNewPart( lReceivingPart );

      // Assert qty
      lReceivingEqpPartNo = EqpPartNoTable.findByPrimaryKey( lReceivingPart );
      assertEquals( lReceivingEqpPartNo.getTotalQt(), BigDecimal.ZERO );

      lExistingEqpPartNo = EqpPartNoTable.findByPrimaryKey( lExistingPart );
      assertEquals( lExistingEqpPartNo.getTotalQt(), BigDecimal.ZERO );
   }


   /**
    *
    * Test that the shipment line will be kept well for stock request after receiving the shipment
    * line with RFI inventory condition
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testReceiveShipmentLineRfiForStockRequest() throws Exception {

      // set up test data for stock request with remote reservation
      RefLocTypeKey lNeededLocationType = RefLocTypeKey.AIRPORT;
      RefInvCondKey lReceivedInvConditon = RefInvCondKey.RFI;
      setupDataForStockRequest( lNeededLocationType, lReceivedInvConditon );

      // call shipment line receive method
      iShipmentLineService.receive( iLineTo, iSupplyLocation, new Date(), false, null, null, null,
            iHr );

      // assert shipment line is well kept for stock request after receiving the inventory
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iStockRequest );
      assertEquals( iShipmentLine, lReqPartTable.getShipmentLine() );

   }


   /**
    *
    * Test that the shipment line will be kept well for stock request after receiving the shipment
    * line with QUARANTINE inventory condition
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testReceiveShipmentLineQuarForStockRequest() throws Exception {

      // set up test data for stock request with remote reservation
      RefLocTypeKey lNeededLocationType = RefLocTypeKey.QUAR;
      RefInvCondKey lReceivedInvConditon = RefInvCondKey.QUAR;
      setupDataForStockRequest( lNeededLocationType, lReceivedInvConditon );

      // call receive method
      iShipmentLineService.receive( iLineTo, iSupplyLocation, new Date(), false, null, null, null,
            iHr );

      // assert shipment line is well kept for stock request after receiving the inventory
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iStockRequest );
      assertEquals( iShipmentLine, lReqPartTable.getShipmentLine() );

   }


   /**
    *
    * Test that the shipment line will be kept well for stock request after receiving the shipment
    * line with INSPREQ inventory condition
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testReceiveShipmentLineInspreqForStockRequest() throws Exception {

      // set up test data for stock request with remote reservation
      RefLocTypeKey lNeededLocationType = RefLocTypeKey.AIRPORT;
      RefInvCondKey lReceivedInvConditon = RefInvCondKey.INSPREQ;
      setupDataForStockRequest( lNeededLocationType, lReceivedInvConditon );

      // call receive method
      iShipmentLineService.receive( iLineTo, iSupplyLocation, new Date(), false, null, null, null,
            iHr );

      // assert shipment line is well kept for stock request after receiving the inventory
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( iStockRequest );
      assertEquals( iShipmentLine, lReqPartTable.getShipmentLine() );

   }


   /**
    *
    * Test that new event note is created when there is multiple match (failed match) while shipment
    * receipt
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testFailedMatchEventNoteWhenReceiveShipment() throws Exception {

      // Enable the indirect matching rule
      enableConfigParmForInvMatching();

      // build receiving part
      PartNoKey lReceivingPart = createPartBuilder( PART_NO_OEM ).build();

      // create archived inventories in two alternate parts
      String lArchivedInventoryList =
            setupDataForFailedMatchWithMultipleActiveOrArchivedInventory( lReceivingPart, true );

      // receive shipment
      receiveNewPart( lReceivingPart );

      // retrieve the received inventory
      InventoryKey lReceivedInventory = getInventoryFromShipment();

      String lInventory = InvInvTable.findByPrimaryKey( lReceivedInventory ).getInvNoSdesc();

      assertNoteAndStageReason( lReceivedInventory, i18n.get( "core.msg.INV_UNARCH_FLDMATCH",
            PO_NUMBER, lInventory, lArchivedInventoryList ), RefStageReasonKey.ACFLDMCH );
   }


   /**
    *
    * Test the alert generated and send while shipment receipt multiple inventory match (failed
    * match)
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testFailedMatchEventNoteWhenReceiveShipmentAlert() throws Exception {

      Mockery iContext = new Mockery();
      final AlertEngine lMockAlertEngine = iContext.mock( AlertEngine.class );
      MxAlertEngine.setInstance( lMockAlertEngine );

      iContext.checking( new Expectations() {

         {
            one( lMockAlertEngine ).sendAlerts( with( any( InventoryNotReInductedAlert.class ) ) );
         }
      } );

      // Enable the indirect matching rule
      enableConfigParmForInvMatching();

      // build receiving part
      PartNoKey lReceivingPart = createPartBuilder( PART_NO_OEM ).build();

      // create archived inventories
      setupDataForFailedMatchWithMultipleActiveOrArchivedInventory( lReceivingPart, true );

      // receive shipment
      receiveNewPart( lReceivingPart );

      iContext.assertIsSatisfied();
   }


   public void setupDataForStockRequest( RefLocTypeKey aNeededLocationType,
         RefInvCondKey aInventoryConditon ) throws Exception {

      iSupplyLocation = new LocationDomainBuilder().withCode( "YYZ" )
            .withType( aNeededLocationType ).isSupplyLocation().build();

      LocationKey lRemoteLocation = new LocationDomainBuilder().withCode( "ATL" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      PartNoKey lSerPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withOemPartNo( PART_NO_OEM ).withStatus( RefPartStatusKey.ACTV )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).build();

      // a serialized inventory at remote location
      final double QUALITY = 1.0;
      InventoryKey lRemoteSerInventory = new InventoryBuilder().withClass( RefInvClassKey.SER )
            .withPartNo( lSerPart ).withSerialNo( SERIAL_NO_OEM ).withBinQt( QUALITY )
            .withCondition( RefInvCondKey.RFI ).atLocation( lRemoteLocation ).build();

      // shipment of type STOCK TRANSFER from remote location to needed location
      ShipmentKey lShipmentKey = new ShipmentDomainBuilder().fromLocation( lRemoteLocation )
            .toLocation( iSupplyLocation ).withType( RefShipmentTypeKey.STKTRN ) // Stock Transfer
            .withStatus( RefEventStatusKey.IXPEND ) // Pending
            .withHistoric( true ).build();

      // shipment line with remote inventory
      iShipmentLine = new ShipmentLineBuilder( lShipmentKey ).forPart( lSerPart )
            .withExpectedQuantity( QUALITY ).forInventory( lRemoteSerInventory )
            .withReceivedQuantity( QUALITY ).build();

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH )
            .withStockName( "test part 001" ).withStockCode( "test001" ).build();

      iStockRequest =
            new PartRequestBuilder().withType( RefReqTypeKey.STOCK ).withReqStockNo( lStockNo )
                  .forSpecifiedPart( lSerPart ).withReservedInventory( lRemoteSerInventory )
                  .withRequestedQuantity( QUALITY ).requiredBy( new Date() )
                  .isNeededAt( iSupplyLocation ).forShipmentLine( iShipmentLine ).build();

      // event built with part request builder
      EventKey lEventKey = iStockRequest.getEventKey();
      EvtEventTable lEvtEventTable = EvtEventTable.findByPrimaryKey( lEventKey );
      lEvtEventTable.setHistBool( false );
      // remote reservation as PRREMOTE
      lEvtEventTable.setEventStatus( RefEventStatusKey.PRREMOTE );
      lEvtEventTable.update();

      iLineTo = new ReceiveShipmentLineTO( iShipmentLine );
      iLineTo.setPartNo( lSerPart );
      iLineTo.setQuarantine( false );
      iLineTo.setReceivedQty( QUALITY );
      iLineTo.setAfterTheFactReceipt( false );
      iLineTo.setRouteCond( aInventoryConditon, "Error" );

   }


   @Before
   public void loadData() throws Exception {

      iOrginalAlertEngine = MxAlertEngine.getInstance();

      // create a human resource
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      iUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();
      UserParametersStub lUserParametersStub =
            new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            false );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", lUserParametersStub );

      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( INVASSET_ACCOUNT ).isDefault().build();

      // create default owner
      iOwner = new OwnerDomainBuilder().isDefault().build();

      // create a default receipt location
      iReceiptOrganization = new InvOwnerTable( iOwner ).getOrgKey();

      // create a vendor location
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR )
            .withCode( "TESTVENLOC" ).build();

      // Create a vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();

      // create a supply location
      LocationKey lSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( lSupplyLocation ).isDefaultDock( true ).build();

      // create a quarantine type location at the supply location
      iQuarLocation = new LocationDomainBuilder().withType( RefLocTypeKey.QUAR )
            .withSupplyLocation( lSupplyLocation ).build();

      // create shipment line service
      iShipmentLineService = new ShipmentLineService();

      // create shipment
      iShipment = createShipment();

      iInvCndChgEventDao = InjectorContainer.get().getInstance( InvCndChgEventDao.class );

   }


   @After
   public void tearDown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", null );
      MxAlertEngine.setInstance( iOrginalAlertEngine );
   }


   /**
    * Create a receiveshipmentlineto
    *
    * @param lReceivingPart
    *
    * @return
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    */
   private ReceiveShipmentLineTO createReceiveShipmentLineTO( PartNoKey aReceivingPart )
         throws MandatoryArgumentException, NegativeValueException {

      Double lQuantity = 1.0;
      PurchaseOrderKey lPurchaseOrder = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iVendorLocation ).withDescription( PO_NUMBER ).build();
      PurchaseOrderLineKey lOrderLine =
            new OrderLineBuilder( lPurchaseOrder ).forPart( aReceivingPart ).build();

      iShipment = new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
            .withStatus( RefEventStatusKey.IXPEND ).withHistoric( true ).withOrder( lPurchaseOrder )
            .build();

      ReceiveShipmentLineTO lLineTo =
            receiveShipmentlineToSetup( lQuantity, aReceivingPart, lOrderLine, null );
      return lLineTo;
   }


   /**
    * create a part builder
    *
    * @param aPartNo
    * @return
    */
   private PartNoBuilder createPartBuilder( String aPartNo ) {
      BigDecimal lUnitPrice = new BigDecimal( "10.00" );
      BigDecimal lValue = new BigDecimal( "5.00" );
      return new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).withOemPartNo( aPartNo )
            .withShortDescription( PART_SDESC ).withStatus( RefPartStatusKey.ACTV )
            .manufacturedBy( MANUFACTURER ).withAverageUnitPrice( lUnitPrice )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withTotalValue( lValue );
   }


   /**
    * Assert the shipment lines were created with the correct details.
    *
    * @param aShipmentLines
    *           the shipment line
    * @param aPart
    *           the part
    * @param aQuantity
    *           the shipment line expected quantity
    * @param aInventory
    *           the inventory
    */
   private void assertShipmentLines( PartNoKey aPart, Double aQuantity, InventoryKey aInventory ) {

      for ( ShipmentLineKey iShipmentLine : iShipmentLines ) {
         ShipShipmentLineTable lShipShipmentLine =
               ShipShipmentLineTable.findByPrimaryKey( iShipmentLine );
         assertEquals( "part no", aPart, lShipShipmentLine.getPartNo() );
         assertEquals( "expected qty", aQuantity, lShipShipmentLine.getExpectQt() );
         assertEquals( "shipment", iShipment, lShipShipmentLine.getShipment() );
         assertEquals( "inventory", aInventory, lShipShipmentLine.getInventory() );
         assertEquals( "po line", null, lShipShipmentLine.getPoLine() );
      }
   }


   /**
    *
    * Assert a shipment line was created with a newly created inventory item.
    *
    * @param aPart
    *           the part no key
    * @param aQuantity
    *           the expected quantity
    * @param aRefInvClass
    *           the inventory class
    * @param aRefInvCond
    *           the inventory condition
    */
   private void assertShipmentLineWithNewInventory( PartNoKey aPart, Double aQuantity,
         RefInvClassKey aRefInvClass, RefInvCondKey aRefInvCond ) {

      ShipShipmentLineTable lShipShipmentLine =
            ShipShipmentLineTable.findByPrimaryKey( iShipmentLines.get( 0 ) );
      assertEquals( "part no", aPart, lShipShipmentLine.getPartNo() );
      assertEquals( "expected qty", aQuantity, lShipShipmentLine.getExpectQt() );
      assertEquals( "shipment", iShipment, lShipShipmentLine.getShipment() );
      assertEquals( "po line", null, lShipShipmentLine.getPoLine() );

      // assert Inventory was created
      InventoryKey lInventory = lShipShipmentLine.getInventory();
      assertNotNull( lInventory );
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lInventory );
      assertEquals( "serial no oem", SERIAL_NO_OEM, lInvInv.getSerialNoOem() );
      assertEquals( "inventory class", aRefInvClass, lInvInv.getInvClass() );
      assertEquals( "inventory condition", aRefInvCond, lInvInv.getInvCond() );
      if ( RefInvClassKey.BATCH.equals( aRefInvClass ) ) {
         assertEquals( "bin quantity", aQuantity, lInvInv.getBinQt() );
      } else {
         assertEquals( "bin quantity", null, lInvInv.getBinQt() );
      }
   }


   /**
    * Assert one and only one quarantine event generated for the inventory
    *
    * @param aInventory
    *           the inventory key
    */
   private void assertOnlyOneQuarantineEvent( InventoryKey aInventory ) {

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      QuerySet lResults = QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.query.inventory.GetConditionChangeEventsForInventory", lWhereArgs );

      // assert one and only one AC type event generated for this inventory
      assertEquals( 1, lResults.getRowCount() );

      if ( lResults.next() ) {

         InvCndChgEventTable lInvCndChgEvent =
               iInvCndChgEventDao.findByPrimaryKey( new InvCndChgEventKey(
                     lResults.getInt( "event_db_id" ), lResults.getInt( "event_id" ) ) );

         // assert the AC type event is ACQUAR
         assertEquals( RefEventStatusKey.ACQUAR, lInvCndChgEvent.getEventStatus() );
      }
   }


   /**
    * Assert the movements from vendor to dock and dock to quarantine
    *
    * @param aInventory
    *           the inventory key
    */
   private void assertTwoMovements( InventoryKey aInventory ) {

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );
      lWhereArgs.add( "ahistorical", true );
      lWhereArgs.add( "aDayCount", 1 );

      QuerySet lResults = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.inventory.Movement", lWhereArgs );

      // For the repair order, more than 2 movements(events) are created as order is send out
      // for repair, then received and then moved from dock to quarantine. Therefore, just assert
      // the movements
      // from vendor to dock(when received) and dock to quarantine(when transfered).

      boolean lFoundMovementFromVendor = false;
      boolean lFoundMovementFromDock = false;

      while ( lResults.next() ) {

         LocationKey lFromLocation = lResults.getKey( LocationKey.class, "inv_loc_from_key" );
         LocationKey lToLocation = lResults.getKey( LocationKey.class, "inv_loc_to_key" );

         if ( iVendorLocation.equals( lFromLocation ) ) {

            assertEquals( iDockLocation, lToLocation );
            lFoundMovementFromVendor = true;

         } else if ( iDockLocation.equals( lFromLocation ) ) {

            assertEquals( iQuarLocation, lToLocation );
            lFoundMovementFromDock = true;
         }

      }

      Assert.assertTrue( lFoundMovementFromVendor );
      Assert.assertTrue( lFoundMovementFromDock );

   }


   @SuppressWarnings( "deprecation" )
   private void assertNoteAndStageReason( InventoryKey aInventory, String aNote,
         RefStageReasonKey aExpectedReason ) {

      InvCndChgEventKey lLatestEvent;

      // Two events are created for indirect match;
      // 1. when inventory is reinducted,
      // 2. when part number is changed. This asserts Reason and Note of the first event for
      // indirect matches. Only one event is created for the other types of matches.
      if ( aExpectedReason.equals( RefStageReasonKey.ACINDMCH ) ) {
         List<InvCndChgEventKey> lLatestEvents = getLatestEventsForInv( aInventory, 2 );
         lLatestEvent = lLatestEvents.get( ( lLatestEvents.size() - 1 ) );

      } else {
         lLatestEvent = getLatestEventForInv( aInventory );

      }

      iInvCndChgEventTable = iInvCndChgEventDao.create( lLatestEvent );

      assertEventLdesc( aNote );

      RefStageReasonKey lStageReasonKey = iInvCndChgEventTable.getStageReasonKey();
      assertEquals( "Stage reason keys do not match", aExpectedReason, lStageReasonKey );
   }


   /**
    * Create an inventory builder.
    *
    * @param aInventoryClass
    *           the inventory class
    * @param aPart
    *           the part
    * @param aBinQuantity
    *           the bin quantity
    *
    * @return InventoryBuilder the inventory builder
    */
   private InventoryBuilder createInventory( RefInvClassKey aInventoryClass, PartNoKey aPart,
         double aBinQuantity ) {
      return new InventoryBuilder().withClass( aInventoryClass ).withPartNo( aPart )
            .withSerialNo( SERIAL_NO_OEM ).withBinQt( aBinQuantity )
            .withCondition( RefInvCondKey.RFI ).atLocation( iDockLocation );
   }


   /**
    * Create a inventory builder with attributes for an archived inventory
    *
    * @param aPartNo
    * @return
    */
   private InventoryBuilder createArchivedInventory( final PartNoKey aPartNo ) {
      return new InventoryBuilder().withClass( RefInvClassKey.SER ).withPartNo( aPartNo )
            .withSerialNo( SERIAL_NO_OEM ).withCondition( RefInvCondKey.ARCHIVE )
            .atLocation( iVendorLocation ).withOwner( iOwner );
   }


   /**
    * Create a part builder with the given class.
    *
    * @param aInventoryClass
    *           the inventory class
    *
    * @return PartNoBuilder the part no builder
    */
   private PartNoBuilder createPartBuilder( RefInvClassKey aInventoryClass ) {
      return new PartNoBuilder().withInventoryClass( aInventoryClass ).withOemPartNo( PART_NO_OEM )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM );
   }


   /**
    * Create a shipment.
    *
    * @return ShipmentKey the shipment key
    */
   private ShipmentKey createShipment() {
      return new ShipmentDomainBuilder().fromLocation( iDockLocation ).toLocation( iVendorLocation )
            .withStatus( RefEventStatusKey.IXPEND ).withHistoric( true ).build();
   }


   /**
    * Create a shipment.
    *
    * @return ShipmentKey the shipment key
    */
   private ShipmentKey createShipmentFromVendorToDock() {
      return new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
            .withStatus( RefEventStatusKey.IXPEND ).withHistoric( true ).build();
   }


   /**
    * Create a shipment line.
    *
    * @param aShipmentKey
    *           the shipment
    * @param aPart
    *           the part
    * @param aOrderLine
    *           the order line
    * @param aInventory
    *           the inventory
    *
    * @return ShipmentLineKey the shipment line key
    */
   private ShipmentLineKey createShipmentLine( ShipmentKey aShipmentKey, PartNoKey aPart,
         PurchaseOrderLineKey aOrderLine, InventoryKey aInventory ) {
      return new ShipmentLineBuilder( aShipmentKey ).forPart( aPart ).withExpectedQuantity( 1.0 )
            .forOrderLine( aOrderLine ).withSerialNo( SERIAL_NO_OEM ).forInventory( aInventory )
            .withReceivedQuantity( 1.0 ).build();
   }


   /**
    * Prepare the ReceiveShipmentLineTO.
    *
    * @param aQuantity
    *           the received quantity
    * @param aPart
    *           the part no key
    * @param aOrderLine
    *           the order line key
    * @param aInventory
    *           the inventory key
    *
    * @return ReceiveShipmentLineTO the transfer object for receiving shipment line
    *
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    */
   private ReceiveShipmentLineTO receiveShipmentlineToSetup( Double aQuantity, PartNoKey aPart,
         PurchaseOrderLineKey aOrderLine, InventoryKey aInventory )
         throws MandatoryArgumentException, NegativeValueException {

      ReceiveShipmentLineTO lLineTo = new ReceiveShipmentLineTO(
            createShipmentLine( iShipment, aPart, aOrderLine, aInventory ) );

      lLineTo.setReceivedQty( aQuantity );
      lLineTo.setPartNo( aPart );
      lLineTo.setQuarantine( true );

      return lLineTo;
   }


   /**
    * Get the inventory from the shipment.
    *
    * @param aShipment
    *           the shipment key
    *
    * @return InventoryKey the inventory key
    */
   private InventoryKey getInventoryFromShipment() {

      ShipShipmentLineTable lShipShipmentLine = ShipShipmentLineTable
            .findByPrimaryKey( ShipmentLineService.getShipmentLine( iShipment ).get( 0 ) );

      return lShipShipmentLine.getInventory();

   }


   @SuppressWarnings( "static-access" )
   private void receiveNewPart( PartNoKey aReceivingPart ) throws MxException, TriggerException {

      ReceiveShipmentLineTO lLineTo = createReceiveShipmentLineTO( aReceivingPart );
      lLineTo.setSerialNo( SERIAL_NO_OEM );

      iShipmentLineService.receiveNewPart( iShipment, lLineTo, iDockLocation, RefInvCondKey.INSPREQ,
            null, 0, true, iHr, true, false );
   }


   private void enableConfigParmForInvMatching() {
      GlobalParametersFake lConfigParm = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParm.setBoolean( PARAMETER_NAME, true );
      GlobalParameters.setInstance( lConfigParm );
   }


   private void enableUserConfigParm() {
      iUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();
      UserParametersFake lUserParms = new UserParametersFake( iUserId, "LOGIC" );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParms );
   }


   /**
    * Set up engine assembly information which to be used to change the part number in Active
    * Inventory Match and Failed Inventory Match scenarios.
    *
    * @param aAlternatePart
    * @param aReceivingPart
    * @return
    */
   private AssemblyKey setupEngineAssemblyData( final PartNoKey aAlternatePart,
         final PartNoKey aReceivingPart ) {
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENG_ASSMBL_CD );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( CONFIG_SLOT_POS );
                                          aBuilder.setCode( CONFIG_SLOT_CD );
                                          aBuilder.setMandatoryFlag( true );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aPartGroup ) {
                                                      aPartGroup.setCode( PART_GROUP );
                                                      aPartGroup.setInventoryClass(
                                                            RefInvClassKey.SER );
                                                      aPartGroup.addPart( aAlternatePart );
                                                      aPartGroup.addPart( aReceivingPart );
                                                   }

                                                } );
                                       }

                                    } );
                           }
                        } );
               }
            } );
      return lEngineAssy;
   }


   /**
    * creates active or archived inventory for failed match scenario when inventory receipt
    *
    * @return String created active or archived inventory descriptions concatenated
    */
   private String setupDataForFailedMatchWithMultipleActiveOrArchivedInventory(
         final PartNoKey aReceivingPart, final Boolean aIsArchived ) throws Exception {

      final PartNoKey lAlternatePart1 = createPartBuilder( PART_NO_ALT_ONE ).build();
      final PartNoKey lAlternatePart2 = createPartBuilder( PART_NO_ALT_TWO ).build();

      // create part group with receiving part attached. This also has two alternative parts
      Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aPartGroup ) {
            aPartGroup.setCode( PART_GROUP );
            aPartGroup.setInventoryClass( RefInvClassKey.SER );
            aPartGroup.addPart( aReceivingPart );
            aPartGroup.addPart( lAlternatePart1 );
            aPartGroup.addPart( lAlternatePart2 );
         }

      } );

      // create two archived inventories with same serial as with receiving part
      if ( aIsArchived ) {
         createArchivedInventory( lAlternatePart1 ).withDescription( INV_DESC ).build();
         createArchivedInventory( lAlternatePart2 ).withDescription( INV_DESC ).build();
      }
      // create two active inventories with same serial as with receiving part
      else {
         createInventory( RefInvClassKey.SER, lAlternatePart1, 0 ).withOwner( iOwner )
               .withDescription( INV_DESC ).build();
         createInventory( RefInvClassKey.SER, lAlternatePart2, 0 ).withOwner( iOwner )
               .withDescription( INV_DESC ).build();
      }

      // return the inventory descriptions in a string
      return new StringBuilder().append( INV_DESC ).append( ", " ).append( INV_DESC ).toString();
   }


   /**
    * Returns latest event for an inventory
    *
    * @param aInventory
    *           inventory primary key
    *
    * @return first returned inventory.
    */
   private InvCndChgEventKey getLatestEventForInv( InventoryKey aInventory ) {
      List<InvCndChgEventKey> lEvents = getLatestEventsForInv( aInventory, 1 );
      if ( lEvents.isEmpty() ) {
         return null;
      } else {
         return lEvents.get( 0 );
      }
   }


   /**
    * Returns latest event for an inventory
    *
    * @param aInventory
    *           inventory primary key
    * @param aCount
    *           the maximum number of rows to return
    *
    * @return first returned inventory.
    */
   private List<InvCndChgEventKey> getLatestEventsForInv( InventoryKey aInventory, int aCount ) {

      // cols to select
      String[] lCols = { "event_db_id", "event_id" };

      DataSetArgument lWhereClause = new DataSetArgument();
      lWhereClause.add( aInventory, "inv_no_db_id", "inv_no_id" );

      // execute query
      QuerySet lDataSet =
            QuerySetFactory.getInstance().executeQuery( lCols, "inv_cnd_chg_inv", lWhereClause );

      int lRemaining = aCount;

      List<InvCndChgEventKey> lEvents = new ArrayList<InvCndChgEventKey>( lDataSet.getRowCount() );
      while ( lDataSet.next() && ( lRemaining > 0 ) ) {
         lEvents.add( lDataSet.getKey( InvCndChgEventKey.class, "event_db_id", "event_id" ) );
         lRemaining--;
      }

      lEvents.sort(
            ( InvCndChgEventKey key1, InvCndChgEventKey key2 ) -> key1.getId() - key2.getId() );

      return lEvents;
   }


   private void assertEventLdesc( String aExpected ) {

      // Check if the aExpected parameter is blank; the database treats null
      // and blank both as null, so convert aExpected to null if it is blank
      if ( ( aExpected != null ) && aExpected.equalsIgnoreCase( "" ) ) {
         aExpected = null;
      }

      MxAssert.assertEquals( "event_ldesc", aExpected, iInvCndChgEventTable.getEventLdesc() );
   }

}
