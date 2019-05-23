package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
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
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.table.InjectionOverrideRule;
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
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.part.KitUtils;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.unittest.table.ship.ShipShipmentLine;


/**
 * This test verifies the functionality of the GetPoLineKitLine.qrx
 */
public class GetPOKitLineForPartTest {

   private static final String USERNAME_TESTUSER = "testuser";

   private static final int USERID_TESTUSER = 999;

   private HumanResourceKey iHr;

   private LocationKey iDockLocation;

   private LocationKey iSupplyLocation;

   private LocationKey iVendorLocation;

   private PartNoKey iSerPart;

   private PartGroupKey iSerPartGroup;

   private PartNoKey iKitPartWithoutComponents;

   private PurchaseOrderLineKey iOrderLine;

   private PurchaseOrderKey iPO;

   private ShipmentLineKey iShipmentLine;

   private ShipmentKey iShipment;

   private ReceiveShipmentLineTO[] iReceiveShipmentLines;

   private InventoryKey iKitItemKey;

   private InventoryKey iKitInvKey;

   private PartNoKey iKitPart;

   private static final String LOCATION = "TESTVENLOC";

   private static final String CURRENCY = "USD";

   private static final String SERPART = "SERPART";

   private static final String PARTGROUP = "MYGROUP";

   private static final String SERIALNO1 = "SN2001";

   private static final String SERIALNO2 = "TrackPartSN";

   private static final String SERIALNO3 = "SN20001";

   private static final String ROUTE = "route condition";

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
    * Tests whether a given inventory received after a shipment belongs to a KitContent
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testRetrievedPOKitLine() throws Exception {

      // set up kit item and kit that need inspection after receiving
      setupKitData();

      // set up PO with POISSUED status
      setupPoData();

      // setup shipment that requires inspection
      setupShipmentData( RefInvCondKey.INSPREQ );

      // receive the shipment
      ShipmentService.receive( iShipment, new Date(), iReceiveShipmentLines, null, iHr );

      // get kit and kit item
      getKitInvData();

      // set up arguments for the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iKitItemKey, "aInvNoDbId", "aInvNoId" );

      QuerySet lResultSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.inventory.GetPoLineKitLine", lArgs );

      assertEquals( "Number of retrieved rows", 1, lResultSet.getRowCount() );

   }


   /**
    * set up data
    *
    * @throws MandatoryArgumentException
    * @throws StringTooLongException
    * @throws NegativeValueException
    */
   @Before
   public void setup()
         throws MandatoryArgumentException, StringTooLongException, NegativeValueException {

      // basic data
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR )
            .withCode( LOCATION ).build();

      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // build a quarantine location for the part of force-inspect-on-receipt
      new LocationDomainBuilder().withType( RefLocTypeKey.QUAR )
            .withSupplyLocation( iSupplyLocation ).build();

      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // create the default currency as USD
      new CurrencyBuilder( CURRENCY ).isDefault().build();

      new OwnerDomainBuilder().isDefault().build();

   }


   /**
    *
    * setup kit data build kit component and kit
    *
    */
   private void setupKitData() {

      iSerPart =
            createPart( SERPART, RefInvClassKey.SER, RefFinanceTypeKey.CONSUM, RefAbcClassKey.D );

      iSerPartGroup = new PartGroupDomainBuilder( PARTGROUP ).withPartNo( iSerPart ).build();

      iKitPartWithoutComponents =
            createPart( null, RefInvClassKey.KIT, RefFinanceTypeKey.KIT, null );

      iKitPart = new KitBuilder( iKitPartWithoutComponents ).withContent( iSerPart, iSerPartGroup )
            .build();
   }


   /**
    * setup PO with POISSUED status
    *
    *
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
            .forOrderLine( iOrderLine ).withSerialNo( SERIALNO1 ).withExpectedQuantity( 1.0 )
            .build();
      List<ReceiveShipmentLineTO> lContentLines = new ArrayList<ReceiveShipmentLineTO>();

      // set iReceiveShipmentLines as parameter to ShipmentService.receive()
      ReceiveShipmentLineTO lContentTO = new ReceiveShipmentLineTO( iShipmentLine );
      lContentTO.setPartNo( iSerPart );
      lContentTO.setRouteCond( aRefInvCondKey, "" );
      lContentTO.setSerialNo( SERIALNO2 );
      lContentTO.setContentOfKit( iKitPartWithoutComponents );
      lContentTO.setReceivedQty( 1.0, "" );
      lContentLines.add( lContentTO );

      // setup the shipment receipt
      ReceiveShipmentLineTO iShipmentLineTO = new ReceiveShipmentLineTO( iShipmentLine );
      iShipmentLineTO.setPartNo( iKitPartWithoutComponents );
      iShipmentLineTO.setSerialNo( SERIALNO3 );
      iShipmentLineTO.setReceivedQty( 1.0 );
      iShipmentLineTO.setRouteCond( aRefInvCondKey, ROUTE );
      iShipmentLineTO.setContentLines( lContentLines );

      iReceiveShipmentLines = new ReceiveShipmentLineTO[] { iShipmentLineTO };
   }


   /**
    * get kit and kit item inventories
    *
    */
   private void getKitInvData() {
      iKitInvKey = new ShipShipmentLine( iShipmentLine ).getInventory();

      List<InventoryKey> lKitContents = KitUtils.getInventoriesInKit( iKitInvKey );
      iKitItemKey = lKitContents.get( 0 );
   }


   /**
    *
    * creates a part for kit or KitItems
    *
    * @param aPartName
    *           the part name
    * @param aClass
    *           the inventory class of part
    * @param aFinType
    *           the Financial type
    * @param aClassKey
    *           the AbcClass key
    * @return PartNoKey Part number of the created part
    */
   private PartNoKey createPart( String aPartName, RefInvClassKey aClass,
         RefFinanceTypeKey aFinType, RefAbcClassKey aClassKey ) {

      return new PartNoBuilder().withOemPartNo( aPartName ).withInventoryClass( aClass )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( aFinType )
            .withUnitType( RefQtyUnitKey.EA ).withAbcClass( aClassKey )
            .withTotalQuantity( BigDecimal.ZERO ).withTotalValue( BigDecimal.ZERO ).build();
   }
}
