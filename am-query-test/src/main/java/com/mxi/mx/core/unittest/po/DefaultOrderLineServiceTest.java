
package com.mxi.mx.core.unittest.po;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.inventory.condition.ConditionService;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.order.OrderLineService;
import com.mxi.mx.core.services.order.OrderServiceFactory;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgOrgVendor;
import com.mxi.mx.core.table.po.PoHeaderTable;
import com.mxi.mx.core.table.ship.ShipShipmentTable;
import com.mxi.mx.core.unittest.table.po.PoLine;
import com.mxi.mx.core.unittest.table.ship.ShipShipment;


/**
 * This class tests the DefaultOrderLineService class
 *
 * @author ydai
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultOrderLineServiceTest {

   private ConditionService iConditionService;

   private OrderLineService iOrderLineService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ORG_CODE = "ORG_CODE";

   private static final String ORG_DESCRIPTION = "ORG_DESCRIPTION";

   private static final FncAccountKey EXPENSE_ACCOUNT = FncAccountKey.CONSIGN;

   private static final int USER_ID = 1;


   @Before
   public void loadData() throws Exception {
      iConditionService = new DefaultConditionService();

      iOrderLineService = OrderServiceFactory.getInstance().getLineService();

   }


   /**
    * Test scenario: If an inventory is selected to be returned on an Exchange Order, and for
    * whatever reason it gets archived manually before the outbound shipment is sent, this will
    * cancel the outbound shipment on the exchange order. Make sure that it is possible to get a new
    * outbound shipment, or to select a different return inventory for the exchange order.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testEOSetReturnInvAfterArchiveOldReturnInv() throws Exception {

      // DATA SETUP
      // create an organization
      OrgKey lOrgKey = new OrganizationDomainBuilder().withCode( ORG_CODE )
            .withDescription( ORG_DESCRIPTION ).build();

      // create a vendor location
      LocationKey lVendorLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).withCode( "TESTVENLOC" ).build();

      // create a supply location
      LocationKey lSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      LocationKey lDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( lSupplyLocation ).build();

      // create a vendor
      VendorKey lVendorKey =
            new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( lVendorLocation ).build();

      // create an owner
      OwnerKey lOwnerKey = new OwnerDomainBuilder().isDefault().isNonLocal().build();

      // create a HR
      HumanResourceKey lHr = new HumanResourceDomainBuilder().withUserId( USER_ID ).build();

      // create a part and assign it to part group
      PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withShortDescription( "Part A" )
            .withAbcClass( RefAbcClassKey.A ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 10.50 ) ).withRepairBool( false ).build();

      // create a part group
      PartGroupKey lTrkPartGroup = new PartGroupDomainBuilder( "PARTGC" )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( lPartNo ).build();

      // create a location for inventory
      LocationKey lLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).isSupplyLocation().build();

      // create an exchange order
      PurchaseOrderKey lExchangeOrder = new OrderBuilder().withOrderType( RefPoTypeKey.EXCHANGE )
            .withVendor( lVendorKey ).build();

      // set organization for po header to avoid InventoryNotInShipFromLocationException
      PoHeaderTable lPoHeader = PoHeaderTable.findByPrimaryKey( lExchangeOrder );
      lPoHeader.setOrganization( lOrgKey );
      lPoHeader.update();

      // create an exchange order line
      PurchaseOrderLineKey lExchangeOrderLine = new OrderLineBuilder( lExchangeOrder )
            .withOwner( lOwnerKey ).withLineType( RefPoLineTypeKey.EXCHANGE )
            .withOrderQuantity( BigDecimal.ONE ).forPart( lPartNo ).build();

      // create inventory based on the given part
      InventoryKey lFirstReturnInventory =
            new InventoryBuilder().withClass( RefInvClassKey.TRK ).withPartGroup( lTrkPartGroup )
                  .withPartNo( lPartNo ).atLocation( lLocation ).withOwner( lOwnerKey )
                  .withOrderLine( lExchangeOrderLine ).withSerialNo( "SN002" ).isIssued().build();

      // create a shipment for the order
      ShipmentKey lShipmentKey =
            new ShipmentDomainBuilder().fromLocation( lVendorLocation ).toLocation( lDockLocation )
                  .withType( RefShipmentTypeKey.SENDXCHG ).withOrder( lExchangeOrder ).build();

      // create a shipment line for the shipment. This is necessary for ConditionService.archive().
      ShipmentLineKey lShipmentLineKey = new ShipmentLineBuilder( lShipmentKey ).forPart( lPartNo )
            .forInventory( lFirstReturnInventory ).forOrderLine( lExchangeOrderLine ).build();

      // set vendor to inventory to avoid InventoryNotInShipFromLocationException
      InvInvTable lFirstReturnInv = InvInvTable.findByPrimaryKey( lFirstReturnInventory );
      lFirstReturnInv.setVendor( lVendorKey );
      lFirstReturnInv.update();

      // create org_org_vendor with OrgKey and VendorKey to avoid
      // InventoryNotInShipFromLocationException
      OrgOrgVendor lOrgOrgVendor = OrgOrgVendor.create();
      lOrgOrgVendor.insert( lOrgKey, lVendorKey );
      lOrgOrgVendor.update();

      // SET RETURN INVENTORY

      // set the inventory as the return inventory for this exchange order just create
      iOrderLineService.setReturnInventory( lExchangeOrderLine, lFirstReturnInventory, lHr );

      // ASSERT
      ShipmentKey lOutboundShipmentKey =
            PoLine.findByPrimaryKey( lExchangeOrderLine ).getXchgShipment();
      ShipmentLineKey[] lOutboundShipmentLineKey =
            ShipShipment.getShipmentLines( lOutboundShipmentKey );
      ShipShipmentTable lOutboundShipment =
            ShipShipmentTable.findByPrimaryKey( lOutboundShipmentKey );
      EvtEventTable lOutboundShipmentEvent = EvtEventTable.findByPrimaryKey( lOutboundShipmentKey );

      // assert that pending outbound shipment has been created associated with this to-be-returned
      // inventory for this exchange order
      assertNotNull( lOutboundShipmentKey );
      assertNotNull( lOutboundShipmentLineKey );
      assertEquals( RefEventStatusKey.IXPEND.getCd(), lOutboundShipmentEvent.getEventStatusCd() );
      assertEquals( RefShipmentTypeKey.SENDXCHG, lOutboundShipment.getShipmentType() );

      // ARCHIVE

      // archive this to-be-returned inventory
      iConditionService.archive( lFirstReturnInventory, EXPENSE_ACCOUNT, null, null, lHr, false,
            false );

      // ASSERT
      EvtEventTable lArchivedInvShipmentEvent = EvtEventTable.findByPrimaryKey( lShipmentKey );

      // assert that the outbound shipment associated with this archived to-be-returned inventory
      // has been cancelled after archive
      assertEquals( RefEventStatusKey.IXCANCEL.getCd(),
            lArchivedInvShipmentEvent.getEventStatusCd() );
      assertEquals( LocationKey.ARCHIVE, lFirstReturnInv.getLocation() );

      // create a new inventory based on the given part
      InventoryKey lSecondReturnInventory =
            new InventoryBuilder().withClass( RefInvClassKey.TRK ).withPartGroup( lTrkPartGroup )
                  .withPartNo( lPartNo ).atLocation( lLocation ).withOwner( lOwnerKey )
                  .withOrderLine( lExchangeOrderLine ).withSerialNo( "SN003" ).isIssued().build();

      // set vendor to inventory to avoid InventoryNotInShipFromLocationException
      InvInvTable lSecondReturnInv = InvInvTable.findByPrimaryKey( lSecondReturnInventory );
      lSecondReturnInv.setVendor( lVendorKey );
      lSecondReturnInv.update();

      // SET RETURN INVENTORY

      // set this new inventory as new to-be-returned inventory. This passes without exception means
      // this test passes
      iOrderLineService.setReturnInventory( lExchangeOrderLine, lSecondReturnInventory, lHr );

      // ASSERT
      ShipmentKey lNewOutboundShipmentKey =
            PoLine.findByPrimaryKey( lExchangeOrderLine ).getXchgShipment();
      ShipmentLineKey[] lNewOutboundShipmentLineKey =
            ShipShipment.getShipmentLines( lNewOutboundShipmentKey );
      ShipShipmentTable lNewOutboundShipment =
            ShipShipmentTable.findByPrimaryKey( lNewOutboundShipmentKey );
      EvtEventTable lNewOutboundShipmentEvent =
            EvtEventTable.findByPrimaryKey( lNewOutboundShipmentKey );

      // assert that new pending outbound shipment has been created associated with this new
      // to-be-returned inventory for this exchange order
      assertNotNull( lNewOutboundShipmentKey );
      assertNotNull( lNewOutboundShipmentLineKey );
      assertEquals( RefEventStatusKey.IXPEND.getCd(),
            lNewOutboundShipmentEvent.getEventStatusCd() );
      assertEquals( RefShipmentTypeKey.SENDXCHG, lNewOutboundShipment.getShipmentType() );
   }
}
