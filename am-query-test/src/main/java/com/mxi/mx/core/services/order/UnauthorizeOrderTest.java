
package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderAuthKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.po.PoAuthTable;


/**
 * This class test the un-authorize method of the OrderService class
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UnauthorizeOrderTest {

   private FncAccountKey iAccount;

   private LocationKey iDockLocation;

   private GlobalParametersStub iGlobalParams;

   private HumanResourceKey iHrHighLevelAuthorizer;

   private HumanResourceKey iHrLowLevelAuthorizer;

   private PurchaseOrderKey iOrder;

   private PurchaseOrderAuthKey iOrderAuthHighLevelHrKey;

   private PurchaseOrderAuthKey iOrderAuthLowLevelHrKey;

   private PartNoKey iPartNoKey;

   private LocationKey iSupplyLocation;

   private VendorKey iVendor;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test case executes the un-authorize method of the OrderService class. The order status is
    * OPEN and has been requested for authorization. A higher hr level of authorization rejects the
    * order.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testHigherAuthorizationHrLevelUnauthorizeOpenOrder() throws Exception {

      // pre-assumptions
      // 1 - order status is POOPEN
      assertEquals( RefEventStatusKey.POOPEN,
            EvtEventTable.findByPrimaryKey( iOrder.getEventKey() ).getEventStatusCd() );

      // 2 - order has been requested for authorization
      assertEquals( PoAuthTable.findByPrimaryKey( iOrderAuthLowLevelHrKey ).getAuthLvlStatusKey(),
            RefPoAuthLvlStatusKey.APPROVED );
      assertFalse( PoAuthTable.findByPrimaryKey( iOrderAuthLowLevelHrKey ).isHistoric() );
      assertEquals( PoAuthTable.findByPrimaryKey( iOrderAuthHighLevelHrKey ).getAuthLvlStatusKey(),
            RefPoAuthLvlStatusKey.REQUESTED );

      // service call: higher level authorization hr rejects authorization
      OrderService.unauthorize( iOrder, null, null, iHrHighLevelAuthorizer );

      // assert that low level authorization hr record was reverted
      assertTrue( PoAuthTable.findByPrimaryKey( iOrderAuthLowLevelHrKey ).isHistoric() );

      // purchase order remains in POOPEN status
      assertEquals( RefEventStatusKey.POOPEN,
            EvtEventTable.findByPrimaryKey( iOrder.getEventKey() ).getEventStatusCd() );
   }


   /**
    * Set up the test case.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void loadData() throws Exception {
      // create HR with low level of autorizathion
      iHrLowLevelAuthorizer = new HumanResourceDomainBuilder().build();

      // create HR with high level of autorizathion
      iHrHighLevelAuthorizer = new HumanResourceDomainBuilder().build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // build finance account
      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // build purchase order with status OPEN
      iOrder = new OrderBuilder().withStatus( RefEventStatusKey.POOPEN ).withVendor( iVendor )
            .withIssueDate( new Date() ).shippingTo( iDockLocation ).build();

      // build part
      iPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "SER_PART" )
            .withShortDescription( "SER part for testing" ).withInventoryClass( RefInvClassKey.SER )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).build();

      // build order line
      new OrderLineBuilder( iOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
            .withOrderQuantity( new BigDecimal( 5.0 ) ).promisedBy( new Date() )
            .forPart( iPartNoKey ).withAccount( iAccount ).build();

      // build order authorization
      // approved
      PoAuthTable lPoAuthTable = PoAuthTable.create( iOrder );
      lPoAuthTable.setAuthHr( iHrLowLevelAuthorizer );
      lPoAuthTable.setAuthDate( new Date() );
      lPoAuthTable.setPoAuthLvlStatus( RefPoAuthLvlStatusKey.APPROVED );
      iOrderAuthLowLevelHrKey = lPoAuthTable.insert();

      // requested
      lPoAuthTable = PoAuthTable.create( iOrder );
      lPoAuthTable.setAuthHr( iHrHighLevelAuthorizer );
      lPoAuthTable.setPoAuthLvlStatus( RefPoAuthLvlStatusKey.REQUESTED );
      iOrderAuthHighLevelHrKey = lPoAuthTable.insert();
   }
}
