package com.mxi.mx.web.query.shipment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartVendorBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OrgVendorPoTypeKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.RefVendorTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentLineService;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.org.OrgVendorPoTypeTable;


/**
 * Vendor unapproved bool check for Existing inventories
 *
 */
public final class VendorApprovalStatusTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iVendorLocation;
   private HumanResourceKey iHr;
   private LocationKey iDockLocation;
   private OrgKey iOrgKey;
   private OrgVendorPoTypeTable iOrgVendorPoTypeTable;

   private final String SERIAL_NO_OEM_1 = "Serial No 1";

   private final String PART_NO_OEM_1 = "PART NO OEM 1";

   private static final String USERNAME_TESTUSER = "testUser";

   private static final String VENDOR_CODE = "vendorCode";

   private static final String VENDOR_LOC_CODE = "vendorLocCode";

   private static final int USERID_TESTUSER = 999;

   private static final Double QUANTITY = 1.0;


   /**
    * @Given Inbound shipment for repairable part with approved vendor.
    * @When Vendor status changes to unapproved.
    * @Then The vendor_unaprvd_bool will be true.
    */
   @Test
   public void checkVendorUnapprovedWhenReceiveShipmentTest() throws Exception {

      // Create a Repairable part
      PartNoKey lSerPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withOemPartNo( PART_NO_OEM_1 ).withStatus( RefPartStatusKey.ACTV )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withRepairBool( true ).build();

      // Vendor, vendor RO type, part repair vendor
      VendorKey lVendorKey = new VendorBuilder().withCode( VENDOR_CODE )
            .withVendorType( RefVendorTypeKey.REPAIR ).atLocation( iVendorLocation ).build();

      // Assign an approved, preferred vendor to the part
      new PartVendorBuilder( lVendorKey, lSerPart ).isPreferred( true )
            .withVendorStatus( RefVendorStatusKey.APPROVED ).build();

      // Create an org_vendor_po_type key
      OrgVendorPoTypeKey lOrgVendorPoTypeKey =
            new OrgVendorPoTypeKey( iOrgKey, lVendorKey, RefPoTypeKey.REPAIR );

      iOrgVendorPoTypeTable = OrgVendorPoTypeTable.create( lOrgVendorPoTypeKey );
      iOrgVendorPoTypeTable.setVendorStatus( RefVendorStatusKey.APPROVED );
      iOrgVendorPoTypeTable.insert();

      // Create RepairOrder
      PurchaseOrderKey lRepairOrderKey = new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR )
            .withVendor( lVendorKey ).withReceiptOrganization( iOrgKey ).build();
      PurchaseOrderLineKey lOrderLine =
            new OrderLineBuilder( lRepairOrderKey ).forPart( lSerPart ).build();

      // Create inventory
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.SER )
            .withPartNo( lSerPart ).withSerialNo( SERIAL_NO_OEM_1 ).withBinQt( QUANTITY )
            .withCondition( RefInvCondKey.REPREQ ).atLocation( iDockLocation ).build();

      // Create outbound shipment
      ShipmentKey lOutBoundShipment =
            new ShipmentDomainBuilder().fromLocation( iDockLocation ).toLocation( iVendorLocation )
                  .withStatus( RefEventStatusKey.IXPEND ).withOrder( lRepairOrderKey ).build();

      // Create outbound shipment line
      ShipmentLineKey lOutBoundShipmentLine = new ShipmentLineBuilder( lOutBoundShipment )
            .forPart( lSerPart ).withExpectedQuantity( QUANTITY ).forOrderLine( lOrderLine )
            .withSerialNo( SERIAL_NO_OEM_1 ).forInventory( lInventory )
            .withReceivedQuantity( QUANTITY ).build();

      // Create inbound shipment
      ShipmentKey lInBoundShipment =
            new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
                  .withStatus( RefEventStatusKey.IXPEND ).withOrder( lRepairOrderKey ).build();

      // Create inbound shipmenttlineTO
      ReceiveShipmentLineTO lLineTo =
            new ReceiveShipmentLineTO( new ShipmentLineBuilder( lInBoundShipment )
                  .forPart( lSerPart ).withExpectedQuantity( QUANTITY ).forOrderLine( lOrderLine )
                  .withSerialNo( SERIAL_NO_OEM_1 ).forInventory( lInventory )
                  .withReceivedQuantity( QUANTITY ).build() );

      // Send outbound shipment
      sendShipment( lOutBoundShipment, lOutBoundShipmentLine );

      // Unapprove Vendor
      unapproveVendor();

      // Receive inbound shipment
      receiveShipment( lInBoundShipment );

      checkVendorUnapprovedBool( lInBoundShipment, "REPAIR" );
   }


   private void checkVendorUnapprovedBool( ShipmentKey aInBoundShipmentKey, String atypeCode ) {

      DataSetArgument args = new DataSetArgument();
      args.add( aInBoundShipmentKey, "aShipmentDbId", "aShipmentId" );
      args.add( "aTypeCode", atypeCode );

      QuerySet lArgs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.shipment.ShipmentLinesToReceive", args );

      assertEquals( 1, lArgs.getRowCount() );

      if ( lArgs.next() ) {

         assertEquals( true, lArgs.getBoolean( "vendor_unaprvd_bool" ) );

      }
   }


   private void sendShipment( ShipmentKey aOutBoundShipmentKey, ShipmentLineKey aShipemntLineKey )
         throws MxException, TriggerException {
      ShipmentLineService.sendShipmentLine( aOutBoundShipmentKey, aShipemntLineKey, iHr );

      // Check vendor status
      RefVendorStatusKey lRefVendorStatusKey = iOrgVendorPoTypeTable.getVendorStatus();
      assertEquals( RefVendorStatusKey.APPROVED, lRefVendorStatusKey );
   }


   private void unapproveVendor() {
      iOrgVendorPoTypeTable.setVendorStatus( RefVendorStatusKey.UNAPPROVED );
      iOrgVendorPoTypeTable.update();

      // Check vendor status
      RefVendorStatusKey lRefVendorStatusKey = iOrgVendorPoTypeTable.getVendorStatus();
      assertEquals( RefVendorStatusKey.UNAPPROVED, lRefVendorStatusKey );
   }


   private void receiveShipment( ShipmentKey aInboundShipmentKey )
         throws MxException, TriggerException {
      ShipmentService.receive( aInboundShipmentKey, iHr );

      // Check vendor status
      RefVendorStatusKey lRefVendorStatusKey = iOrgVendorPoTypeTable.getVendorStatus();
      assertEquals( RefVendorStatusKey.UNAPPROVED, lRefVendorStatusKey );
   }


   @Before
   public void setup() {

      // create a human resource
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // create a organization
      iOrgKey = new OrgKey( 4650, 10001 );

      // create a vendor location
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR )
            .withCode( VENDOR_LOC_CODE ).build();

      // create a supply location
      LocationKey lSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( lSupplyLocation ).isDefaultDock( true ).build();

   }

}
