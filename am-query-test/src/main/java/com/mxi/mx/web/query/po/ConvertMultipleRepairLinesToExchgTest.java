package com.mxi.mx.web.query.po;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.ConvertOrdersTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.order.OrderServiceFactory;
import com.mxi.mx.core.table.ship.ShipShipmentTable;


/**
 * This unit tests covers the conditions after converting a repair order with multiple lines to an
 * exchange order
 *
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ConvertMultipleRepairLinesToExchgTest {

   private LocationKey iRepairLocation = null;
   private LocationKey iInventoryLocation = null;
   private PurchaseOrderKey iRepairOrder = null;
   private PurchaseOrderLineKey iOrderLineOne = null;
   private PurchaseOrderLineKey iOrderLineTwo = null;
   private TaskKey iWorkPackageOne = null;
   private TaskKey iWorkPackageTwo = null;
   private VendorKey iVendor;
   private OrgKey iReceiptOrganization;
   private PartNoKey iSerialPartA = null;
   private PartNoKey iSerialPartB = null;
   private HumanResourceKey iHumanResourceKey = null;
   private InventoryKey iSerialInventoryKeyA = null;
   private InventoryKey iSerialInventoryKeyB = null;
   private List<ConvertOrdersTO> iRoLineList = new ArrayList<ConvertOrdersTO>();
   private FncAccountKey iFnCAccount = null;

   private static final String ERROR_LABEL = "error_lable";
   private static final String ACC_ATTR = "Default_account";
   private static final String LOC_CODE = "ABQ";
   private static final RefLocTypeKey LOC_TYPE = RefLocTypeKey.DOCK;
   private static final RefInvClassKey SERIAL = RefInvClassKey.SER;
   private static final RefQtyUnitKey UNIT = RefQtyUnitKey.EA;
   private static final RefInvCondKey CONDITION = RefInvCondKey.REPREQ;
   private static final RefAccountTypeKey ACC_TYPE = RefAccountTypeKey.INVASSET;
   private static final RefPoTypeKey PO_TYPE = RefPoTypeKey.REPAIR;
   private static final RefEventStatusKey WORKPKG_STATUS = RefEventStatusKey.ACTV;
   private static final BigDecimal ORDER_QTY = BigDecimal.ONE;
   private static final String ACC_CD = "ChargeToAccount";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   // Test scenario : given a repair order when repair router converts a repair line to exchange
   @Before
   public void loadData() throws Exception {

      // create HR
      iHumanResourceKey = Domain.createHumanResource();

      // create Organization
      iReceiptOrganization = Domain.createOrganization();

      // create repair location
      iRepairLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( LOC_CODE );
         aLocation.setType( LOC_TYPE );
      } );
      // create vendor
      iVendor = Domain.createVendor( aVendor -> {
         aVendor.setLocation( iRepairLocation );
      } );

      // create a serial part
      iSerialPartA = Domain.createPart( aPart -> {
         aPart.setInventoryClass( SERIAL );
         aPart.setQtyUnitKey( UNIT );
      } );

      // create another serial part
      iSerialPartB = Domain.createPart( aPart -> {
         aPart.setInventoryClass( SERIAL );
         aPart.setQtyUnitKey( UNIT );
      } );

      // create inventory for serial part A
      iSerialInventoryKeyA = Domain.createSerializedInventory( aSerInv -> {
         aSerInv.setLocation( iInventoryLocation );
         aSerInv.setCondition( CONDITION );
         aSerInv.setPartNumber( iSerialPartA );
      } );
      // create inventory for serial part B
      iSerialInventoryKeyB = Domain.createSerializedInventory( aSerInv -> {
         aSerInv.setLocation( iInventoryLocation );
         aSerInv.setCondition( CONDITION );
         aSerInv.setPartNumber( iSerialPartB );
      } );
      // create a finance account
      iFnCAccount = new AccountBuilder().withType( ACC_TYPE ).withCode( ACC_CD ).build();

      // create new repair order
      iRepairOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( PO_TYPE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iRepairLocation );
         aPurchaseOrder.status( RefEventStatusKey.POAUTH );

      } );

      // create a workpackage assigned task for inventory A
      iWorkPackageOne = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( iSerialInventoryKeyA );
         aWorkPackage.setLocation( iInventoryLocation );
         aWorkPackage.setStatus( WORKPKG_STATUS );
      } );

      // create a workpackage assigned task for inventory B
      iWorkPackageTwo = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( iSerialInventoryKeyB );
         aWorkPackage.setLocation( iInventoryLocation );
         aWorkPackage.setStatus( WORKPKG_STATUS );
      } );

      // add first order line to repair order
      iOrderLineOne = Domain.createPurchaseOrderLine( aLine -> {
         aLine.orderKey( iRepairOrder );
         aLine.task( iWorkPackageOne );
         aLine.orderQuantity( ORDER_QTY );
         aLine.unitPrice( BigDecimal.ZERO );
         aLine.account( iFnCAccount );
         aLine.part( iSerialPartA );
         aLine.unitType( UNIT );
      } );

      // add second order line to repair order
      iOrderLineTwo = Domain.createPurchaseOrderLine( aLine -> {
         aLine.orderKey( iRepairOrder );
         aLine.task( iWorkPackageTwo );
         aLine.orderQuantity( ORDER_QTY );
         aLine.unitPrice( BigDecimal.ONE );
         aLine.account( iFnCAccount );
         aLine.part( iSerialPartB );
         aLine.unitType( UNIT );
      } );

      ShipmentKey iShipmentKey = Domain.createShipment( aShipment -> {
         aShipment.setType( RefShipmentTypeKey.SENDREP );
         aShipment.setPurchaseOrder( iRepairOrder );
         aShipment.setStatus( RefEventStatusKey.IXPEND );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineOne );
         aShipmentLine.shipmentKey( iShipmentKey );
         aShipmentLine.part( iSerialPartA );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineTwo );
         aShipmentLine.shipmentKey( iShipmentKey );
         aShipmentLine.part( iSerialPartB );
      } );
      // set orderline to converted from repair to exchange to a transfer object
      ConvertOrdersTO aLineTobeconverted = new ConvertOrdersTO( iOrderLineTwo );
      aLineTobeconverted.setUnitPrice( BigDecimal.ZERO, ERROR_LABEL );
      aLineTobeconverted.setAccountCode( ACC_CD, ACC_ATTR );
      aLineTobeconverted.setReturnInv( iSerialInventoryKeyB );
      iRoLineList.add( aLineTobeconverted );

      // Convert a repair line to exchange
      OrderServiceFactory.getInstance().getLineConversionService()
            .convertRepairLineToXchg( iRoLineList, iHumanResourceKey );

   }


   /**
    * Tests whether the outboud shipment remains sendrep when at least one ro line exist
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testOutboundShipmentLineConverted() throws Exception {

      List<ShipmentKey> shipments = OrderService.getShipments( iOrderLineTwo );
      // fetch outbound shipment
      ShipShipmentTable lShipmentTable = ShipShipmentTable.findByPrimaryKey( shipments.get( 0 ) );
      assertThat( RefShipmentTypeKey.SENDREP, is( equalTo( lShipmentTable.getShipmentType() ) ) );

   }


   /**
    * Tests whether the inbound shipment remains repair when at least one ro line exist
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testInboundShipmentLineConverted() throws Exception {

      // issue the repair order
      ShipmentKey inbound = new OrderService().issue( iRepairOrder, "note", iHumanResourceKey );
      // fetch the inbound shipment
      ShipShipmentTable lShipmentTable = ShipShipmentTable.findByPrimaryKey( inbound );
      assertThat( RefShipmentTypeKey.REPAIR, is( equalTo( lShipmentTable.getShipmentType() ) ) );

   }
}
