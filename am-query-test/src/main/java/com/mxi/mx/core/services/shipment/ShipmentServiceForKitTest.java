package com.mxi.mx.core.services.shipment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.MxBeforeTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.part.KitUtils;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.trigger.MxCoreTriggerType;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.ship.ShipShipmentLine;
import com.mxi.mx.web.constant.inventory.InspectInventoryConstants;


/**
 * This class tests the shipment service related to Kit PO.
 *
 * @author lcai and fzhang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ShipmentServiceForKitTest {

   /** the bin quantity */
   private static final String USERNAME_TESTUSER = "testuser";

   private static final int USERID_TESTUSER = 999;

   private HumanResourceKey iHr;

   private LocationKey iDockLocation;

   private LocationKey iSupplyLocation;

   private LocationKey iVendorLocation;

   private PartNoKey iSerPart;

   private PartGroupKey iSerPartGroup;

   private PartNoKey iKitPartWithoutComponents;

   private PartNoKey iKitPart;

   private PurchaseOrderLineKey iOrderLine;

   private PurchaseOrderKey iPO;

   private InspectInventoryTO iInspectInventoryTO;

   private ShipmentLineKey iShipmentLine;

   private ShipmentKey iShipment;

   private ReceiveShipmentLineTO[] iReceiveShipmentLines;

   private InventoryKey iKitItemKey;

   private InvInv iKitItemInv;

   private InventoryKey iKitInvKey;

   private InvInv iKitInv;

   private List<InventoryKey> iInventoryFiredfTriggers = new ArrayList<InventoryKey>();

   private DefaultConditionService iDefaultConditionService;

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test the the receiving of shipment for PO of kit with force-inspect-on-receipt.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testReceivePOForKitWithInspection() throws Exception {

      // set up kit item and kit that need inspection after receiving
      setupKitData( true );

      // set up PO with POISSUED status
      setupPoData();

      // stage 1: receive shipment and assert

      // setup shipment that requires inspection
      setupShipmentData( RefInvCondKey.INSPREQ );

      // receive the shipment
      ShipmentService.receive( iShipment, new Date(), iReceiveShipmentLines, null, iHr );

      // get kit and kit item
      getKitInvData();

      iKitInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW );
      iKitItemInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW );

      // assert that no order-inventory-received message was sent out after receiving the shipment
      Assert.assertEquals( 0, iInventoryFiredfTriggers.size() );

      // stage 2: inspect kit item and kit as serviceable, and assert

      // set up inventory data for inspection
      setupInspectInventoryTO();

      // inspect kit item, then kit
      iDefaultConditionService = new DefaultConditionService();
      iDefaultConditionService.inspectInventory( iKitItemKey, iInspectInventoryTO, iHr );
      iDefaultConditionService.inspectInventory( iKitInvKey, iInspectInventoryTO, iHr );

      // execute query to make sure that there was a finance transaction for kit component inventory
      QuerySet lDs = QuerySetFactory.getInstance().executeQuery( "FNC_XACTION_LOG",
            iKitItemKey.getPKWhereArg(), "XACTION_DB_ID", "XACTION_ID", "XACTION_TYPE_CD",
            "XACTION_LDESC" );

      Assert.assertEquals( 1, lDs.getRowCount() );
      lDs.next();
      Assert.assertEquals( RefXactionTypeKey.INSP.getCd(), lDs.getString( "XACTION_TYPE_CD" ) );

      Assert.assertEquals( i18n.get( "core.msg.RECEIVED_SUB_KIT_SER_INV_AS_INSPREQ_TRANSACTION",
            "TRACKPARTSN", "SERPART", "SN20001", null ), lDs.getString( "XACTION_LDESC" ) );

      /*
       * After inspect sub-kit as serviceable: the condition of sub-kit is RFI, no
       * order-inventory-received message sent out. After inspect kit as serviceable: kit finance
       * status code is INSP, the condition of kit is RFI, an order-inventory-received message sent
       * out
       */

      // make sure finance status is INSP for subkit inventory
      getKitInvData();

      iKitItemInv.assertCondCd( "RFI" );

      iKitInv.assertCondCd( "RFI" );
      iKitInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.INSP );

      // assert that there was one fired trigger ( for order-inventory-received message)
      Assert.assertEquals( 2, iInventoryFiredfTriggers.size() );
      // assert that the event was triggered for the kit inventory
      Assert.assertEquals( iKitInvKey, iInventoryFiredfTriggers.get( 1 ) );
   }


   /**
    * Test that the total quantity and total value of a kit part is increased only after inspection
    * of the kit.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testKitTotalQtyIsIncreasedAfterInspection() throws Exception {

      // set up kit item and kit that need inspection after receiving
      setupKitData( true );

      // set up PO with POISSUED status
      setupPoData();

      // stage 1: receive shipment and assert

      // setup shipment that requires inspection
      setupShipmentData( RefInvCondKey.INSPREQ );

      // receive the shipment
      ShipmentService.receive( iShipment, new Date(), iReceiveShipmentLines, null, iHr );

      // assert that total qty and total value of kit part is still 0
      EqpPartNoTable lKitPartTable = EqpPartNoTable.findByPrimaryKey( iKitPart );
      Assert.assertEquals( 0L, lKitPartTable.getTotalQt().longValue() );
      Assert.assertEquals( 0L, lKitPartTable.getTotalValue().longValue() );

      // stage 2: inspect kit item and kit as serviceable, and assert

      // set up inventory data for inspection
      setupInspectInventoryTO();

      // inspect kit item, then kit
      getKitInvData();

      iDefaultConditionService = new DefaultConditionService();
      iDefaultConditionService.inspectInventory( iKitItemKey, iInspectInventoryTO, iHr );
      iDefaultConditionService.inspectInventory( iKitInvKey, iInspectInventoryTO, iHr );

      // assert that total qty of kit part is changed to 1 and the total value is 10
      lKitPartTable = EqpPartNoTable.findByPrimaryKey( iKitPart );
      Assert.assertEquals( 1L, lKitPartTable.getTotalQt().longValue() );
      Assert.assertEquals( 10L, lKitPartTable.getTotalValue().longValue() );
   }


   /**
    * Test the the receiving of shipment for PO of kit without force-inspect-on-receipt.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testReceivePOForKitNoInspection() throws Exception {

      // set up part and PO data
      setupKitData( false );

      setupPoData();

      // stage 1: receive shipment and assert

      // setup shipment with RFI code
      setupShipmentData( RefInvCondKey.RFI );

      // receive the shipment
      ShipmentService.receive( iShipment, new Date(), iReceiveShipmentLines, null, iHr );

      // get kit inventory
      getKitInvData();

      // assert that the finance status code of kit and its sub-kit are INSP
      iKitInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.INSP );
      iKitItemInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.INSP );

      // assert that Financial transaction for sub-kit is created with the correct description
      QuerySet lDs = QuerySetFactory.getInstance().executeQuery( "FNC_XACTION_LOG",
            iKitItemKey.getPKWhereArg(), "XACTION_DB_ID", "XACTION_ID", "XACTION_TYPE_CD",
            "XACTION_LDESC" );

      Assert.assertEquals( 1, lDs.getRowCount() );
      lDs.next();
      Assert.assertEquals( RefXactionTypeKey.INSP.getCd(), lDs.getString( "XACTION_TYPE_CD" ) );
      Assert.assertEquals( i18n.get( "core.msg.RECEIVED_SUB_KIT_SER_INV_AS_INSPREQ_TRANSACTION",
            "TRACKPARTSN", "SERPART", "SN20001", null ), lDs.getString( "XACTION_LDESC" ) );

      // assert that there was one fired trigger ( for order-inventory-received message)
      Assert.assertEquals( 1, iInventoryFiredfTriggers.size() );
      // assert that the event was triggered for the kit inventory
      Assert.assertEquals( iKitInvKey, iInventoryFiredfTriggers.get( 0 ) );

   }


   /**
    *
    * mock the MX Before trigger so that we know how many times Before triggers are fired and which
    * inventories are associated with them.
    *
    */
   private class MyMxBeforeTriggerForINSPSRV implements MxBeforeTrigger<Object> {

      /**
       * Method called <b>before</b> the status change for the entity has occurred.
       *
       * @param aPk
       *           Entity PK
       *
       * @throws TriggerException
       *            if an error in a trigger class occurs
       */
      @Override
      public void before( Object aPk ) throws TriggerException {

         iInventoryFiredfTriggers.add( ( InventoryKey ) aPk );
      }

   }


   /**
    * setup MyMxBeforeTriggerForINSPSRV
    *
    * @throws MandatoryArgumentException
    * @throws StringTooLongException
    * @throws NegativeValueException
    */
   @Before
   public void setup()
         throws MandatoryArgumentException, StringTooLongException, NegativeValueException {

      Map<String, Object> lTriggerMap = new HashMap<String, Object>( 1 );
      lTriggerMap.put( MxCoreTriggerType.MX_IN_INSPSRV.toString(),
            new MyMxBeforeTriggerForINSPSRV() );

      TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
      TriggerFactory.setInstance( lTriggerFactoryStub );

      // basic data
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR )
            .withCode( "TESTVENLOC" ).build();

      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // build a quarantine location for the part of force-inspect-on-receipt
      new LocationDomainBuilder().withType( RefLocTypeKey.QUAR )
            .withSupplyLocation( iSupplyLocation ).build();

      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // create the default currency as USD
      new CurrencyBuilder( "USD" ).isDefault().build();

      new OwnerDomainBuilder().isDefault().build();

   }


   // reset TriggerFactory
   @After
   public void tearDown() {
      TriggerFactory.setInstance( null );
   }


   /**
    *
    * setup kit data
    *
    * @param aReceiptInsp
    *           true - build kit component and kit with attribute force-inspect-on-receipt; false -
    *           build kit component and kit without force-inspect-on-receipt
    */
   private void setupKitData( boolean aReceiptInsp ) {

      // initialize serial part and kit with total quantity equals 0 and total value equals 0
      // respectively
      if ( aReceiptInsp ) {
         iSerPart = new PartNoBuilder().withOemPartNo( "SERPART" )
               .withInventoryClass( RefInvClassKey.SER ).isReceiptInsp()
               .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
               .withUnitType( RefQtyUnitKey.EA ).withAbcClass( RefAbcClassKey.D )
               .withTotalQuantity( BigDecimal.ZERO ).withTotalValue( BigDecimal.ZERO ).build();

         iSerPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).withPartNo( iSerPart ).build();

         // kit is first created via PartNoBuilder as a part without any sub-component
         iKitPartWithoutComponents = new PartNoBuilder().withInventoryClass( RefInvClassKey.KIT )
               .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.KIT )
               .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( BigDecimal.ZERO )
               .withTotalValue( BigDecimal.ZERO ).isReceiptInsp().build();

         // kit is then further built via KitBuilder
         iKitPart = new KitBuilder( iKitPartWithoutComponents )
               .withContent( iSerPart, iSerPartGroup ).build();
      } else {
         iSerPart = new PartNoBuilder().withOemPartNo( "SERPART" )
               .withInventoryClass( RefInvClassKey.SER ).withStatus( RefPartStatusKey.ACTV )
               .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( RefQtyUnitKey.EA )
               .withAbcClass( RefAbcClassKey.D ).withTotalQuantity( BigDecimal.ZERO )
               .withTotalValue( BigDecimal.ZERO ).build();

         iSerPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).withPartNo( iSerPart ).build();

         iKitPartWithoutComponents = new PartNoBuilder().withInventoryClass( RefInvClassKey.KIT )
               .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.KIT )
               .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( BigDecimal.ZERO )
               .withTotalValue( BigDecimal.ZERO ).build();

         iKitPart = new KitBuilder( iKitPartWithoutComponents )
               .withContent( iSerPart, iSerPartGroup ).build();
      }
   }


   /**
    * setup PO with POISSUED status
    *
    * @return
    */
   private void setupPoData() {
      iPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .withStatus( RefEventStatusKey.POISSUED ).build();

      // order quantity is 1, unit price is 10, and accrued value is 10
      iOrderLine = new OrderLineBuilder( iPO ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( iKitPartWithoutComponents ).withLinePrice( BigDecimal.ONE )
            .withOrderQuantity( BigDecimal.ONE ).withUnitPrice( BigDecimal.TEN )
            .withUnitType( RefQtyUnitKey.EA ).withAccruedValue( BigDecimal.TEN ).build();
   }


   /**
    * setup shipment with route condition which is usually the condition of the inventories involved
    * in the shipment after receiving the PO
    *
    * @throws MandatoryArgumentException
    * @throws StringTooLongException
    * @throws NegativeValueException
    */
   private void setupShipmentData( RefInvCondKey aRefInvCondKey )
         throws MandatoryArgumentException, StringTooLongException, NegativeValueException {
      iShipment = new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
            .withType( RefShipmentTypeKey.PURCHASE ).build();

      iShipmentLine = new ShipmentLineBuilder( iShipment ).forPart( iKitPartWithoutComponents )
            .forOrderLine( iOrderLine ).withSerialNo( "SN2001" ).withExpectedQuantity( 1.0 )
            .build();
      List<ReceiveShipmentLineTO> lContentLines = new ArrayList<ReceiveShipmentLineTO>();

      // set iReceiveShipmentLines as parameter to ShipmentService.receive()
      ReceiveShipmentLineTO lContentTO = new ReceiveShipmentLineTO( iShipmentLine );
      lContentTO.setPartNo( iSerPart );
      lContentTO.setRouteCond( aRefInvCondKey, "" );
      lContentTO.setSerialNo( "TrackPartSN" );
      lContentTO.setContentOfKit( iKitPartWithoutComponents );
      lContentTO.setReceivedQty( 1.0, "" );
      lContentLines.add( lContentTO );

      // setup the shipment receipt
      ReceiveShipmentLineTO iShipmentLineTO = new ReceiveShipmentLineTO( iShipmentLine );
      iShipmentLineTO.setPartNo( iKitPartWithoutComponents );
      iShipmentLineTO.setSerialNo( "SN20001" );
      iShipmentLineTO.setReceivedQty( 1.0 );
      iShipmentLineTO.setRouteCond( aRefInvCondKey, "route condition" );
      iShipmentLineTO.setContentLines( lContentLines );

      iReceiveShipmentLines = new ReceiveShipmentLineTO[] { iShipmentLineTO };
   }


   /**
    * setup InspectInventoryTO
    *
    * @throws StringTooLongException
    */
   private void setupInspectInventoryTO() throws StringTooLongException {

      // inspect the kit and sub-component
      iInspectInventoryTO = new InspectInventoryTO();
      iInspectInventoryTO.setMode( InspectInventoryConstants.MODE_INSPECT_AS_SERVICEABLE );
      iInspectInventoryTO.setAlertTechnicalRecords( true );
      iInspectInventoryTO.setNote( "NOTE" );
      iInspectInventoryTO.setQuantity( 1.0 );

   }


   /**
    * get kit and kit item inventories
    *
    */
   private void getKitInvData() {
      iKitInvKey = new ShipShipmentLine( iShipmentLine ).getInventory();
      iKitInv = new InvInv( iKitInvKey );

      List<InventoryKey> lKitContents = KitUtils.getInventoriesInKit( iKitInvKey );
      iKitItemKey = lKitContents.get( 0 );
      iKitItemInv = new InvInv( iKitItemKey );
   }

}
