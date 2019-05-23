
package com.mxi.mx.web.query.po;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.VendorKey;


/**
 * This class performs unit testing on the query files for POInventoryRetruns and ExchangeReturns
 *
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InventoryReturnsAndExchangeReturnsTest {

   /** The PO Inventory Returns */
   private QuerySet iPOInventoryReturnsDs = null;

   /** The Exchange Returns */
   private QuerySet iExchangeReturnsDs = null;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iRepairLocation = null;
   private LocationKey iDockLocation = null;
   private LocationKey iInventoryLocation = null;
   private PurchaseOrderKey iRepairOrder = null;
   private PurchaseOrderLineKey iOrderLineOne = null;
   private PurchaseOrderLineKey iOrderLineTwo = null;
   private TaskKey iWorkPackageOne = null;
   private VendorKey iVendor;
   private OrgKey iReceiptOrganization;
   private PartNoKey iSerialPartA = null;
   private PartNoKey iSerialPartB = null;
   private InventoryKey iSerialInventoryKeyA = null;
   private InventoryKey iSerialInventoryKeyB = null;
   private FncAccountKey iFnCAccount = null;
   private RefCurrencyKey iCurrency = null;

   private static final String LOC_CODE_ABQ = "ABQ";
   private static final String LOC_CODE_ORG = "ORG";
   private static final RefLocTypeKey LOC_TYPE = RefLocTypeKey.VENDOR;
   private static final RefInvClassKey SERIAL = RefInvClassKey.SER;
   private static final RefQtyUnitKey UNIT = RefQtyUnitKey.EA;
   private static final RefInvCondKey CONDITION = RefInvCondKey.REPREQ;
   private static final RefAccountTypeKey ACC_TYPE = RefAccountTypeKey.INVASSET;
   private static final RefPoTypeKey PO_TYPE = RefPoTypeKey.REPAIR;
   private static final RefEventStatusKey WORKPKG_STATUS = RefEventStatusKey.ACTV;
   private static final BigDecimal ORDER_QTY = BigDecimal.ONE;
   private static final String ACC_CD = "ChargeToAccount";


   @Before
   public void loadData() throws Exception {

      // create Organization
      iReceiptOrganization = Domain.createOrganization();

      // create repair location
      iRepairLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( LOC_CODE_ABQ );
         aLocation.setType( LOC_TYPE );
      } );

      // create repair location
      iDockLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( LOC_CODE_ORG );
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      // create inventory location
      iInventoryLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( "BIA" );
         aLocation.setType( RefLocTypeKey.USSTG );
      } );

      // create the default currency as USD
      iCurrency = new CurrencyBuilder( "USD" ).isDefault().build();
      // Create Vendor
      iVendor = new VendorBuilder().atLocation( iRepairLocation ).withCurrency( iCurrency )
            .withCode( "TestVendor" ).build();

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
         aPurchaseOrder.status( RefEventStatusKey.POISSUED );

      } );

      // create a workpackage assigned task for inventory A
      iWorkPackageOne = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( iSerialInventoryKeyA );
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
         aLine.lineType( RefPoLineTypeKey.REPAIR );
      } );

      // add second order line to repair order
      iOrderLineTwo = Domain.createPurchaseOrderLine( aLine -> {
         aLine.orderKey( iRepairOrder );
         aLine.orderQuantity( ORDER_QTY );
         aLine.unitPrice( BigDecimal.ONE );
         aLine.account( iFnCAccount );
         aLine.part( iSerialPartB );
         aLine.unitType( UNIT );
         aLine.lineType( RefPoLineTypeKey.EXCHANGE );
      } );
      ShipmentKey iInboundShipmentKey = Domain.createShipment( aShipment -> {
         aShipment.setType( RefShipmentTypeKey.REPAIR );
         aShipment.setPurchaseOrder( iRepairOrder );
         aShipment.setStatus( RefEventStatusKey.IXPEND );
         aShipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iRepairLocation );
            segment.setToLocation( iDockLocation );
         } );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineOne );
         aShipmentLine.shipmentKey( iInboundShipmentKey );
         aShipmentLine.part( iSerialPartA );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineTwo );
         aShipmentLine.shipmentKey( iInboundShipmentKey );
         aShipmentLine.part( iSerialPartB );
      } );

      ShipmentKey iOutboundShipment = Domain.createShipment( aShipment -> {
         aShipment.setType( RefShipmentTypeKey.SENDREP );
         aShipment.setPurchaseOrder( iRepairOrder );
         aShipment.setStatus( RefEventStatusKey.IXPEND );
         aShipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iRepairLocation );
         } );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineOne );
         aShipmentLine.shipmentKey( iOutboundShipment );
         aShipmentLine.part( iSerialPartA );
         aShipmentLine.inventory( iSerialInventoryKeyA );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineTwo );
         aShipmentLine.shipmentKey( iOutboundShipment );
         aShipmentLine.part( iSerialPartB );
         aShipmentLine.inventory( iSerialInventoryKeyB );
      } );

   }


   /**
    * Test scenario : given a repair order when repair router converts a repair line to exchange
    * order line an inventory added as a return inventory should be able to retrieved by
    * POInventoryReturns.qrx.
    *
    *
    */
   @Test
   public void executePOInventoryReturns() {

      // get PO Inventory Returns Ds
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iRepairOrder, new String[] { "aPoDbId", "aPoId" } );

      iPOInventoryReturnsDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.po.POInventoryReturns", lArgs );
      assertEquals( 1, iPOInventoryReturnsDs.getRowCount() );
      while ( iPOInventoryReturnsDs.next() ) {
         assertEquals( iOrderLineTwo,
               iPOInventoryReturnsDs.getKey( PurchaseOrderLineKey.class, "po_line_key" ) );
      }
   }


   /**
    * Test scenario : given a repair order when repair router converts a repair line to exchange
    * order line and the repair oder is issued then exchange inventory line should be retrieved by
    * ExchangeReturns.qrx file
    *
    * @throws SQLException
    *
    *
    */
   @Test
   public void executeExchangeReturns() throws MxException, TriggerException, SQLException {

      // set arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInstalled", "-1" );

      // get data set for ExchangeReturns.qrx
      iExchangeReturnsDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.todolist.ExchangeReturns", lArgs );
      assertEquals( 1, iExchangeReturnsDs.getRowCount() );
      while ( iExchangeReturnsDs.next() ) {
         assertEquals( iOrderLineTwo,
               iExchangeReturnsDs.getKey( PurchaseOrderLineKey.class, "po_line_key" ) );
      }
   }

}
