package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoAuthFlowKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.OrderService;


/**
 * This class test the Authorization Required POs tab page
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AuthorizationRequiredPOsTest {

   private FncAccountKey iAccount;

   private LocationKey iDockLocation;

   private HumanResourceKey iHrHighLevelAuthorizer;

   private HumanResourceKey iHrLowLevelAuthorizer;

   private PurchaseOrderKey iOrder;

   private PartNoKey iPartNoKey;

   private LocationKey iSupplyLocation;

   private VendorKey iVendor;

   private QuerySet iQuerySet;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final Date PROMISEDBY_DATE = new GregorianCalendar( 2018, 2, 12 ).getTime();


   /**
    * This tests that query returns the order created in the setup for user with high authorization
    * level
    */
   @Test
   public void testOpenOrdersReturnedForHighAuthLvlUser() throws Exception {
      // execute the query with high authorization level user
      execute( iHrHighLevelAuthorizer );

      // verify 1 row is returned
      assertEquals( "item is returned", 1, iQuerySet.getRowCount() );
      iQuerySet.next();

      // verify that data is correct for returned row
      assertEquals( iOrder, iQuerySet.getKey( PurchaseOrderKey.class, "po_key" ) );
      assertEquals( "PO1", iQuerySet.getString( "event_sdesc" ) );
      assertEquals( iVendor, iQuerySet.getKey( VendorKey.class, "vendor_key" ) );
      assertEquals( "TESTVENDOR", iQuerySet.getString( "vendor_cd" ) );
      assertEquals( "TESTVENDOR", iQuerySet.getString( "vendor_name" ) );
      assertEquals( "BLK", iQuerySet.getString( "currency_cd" ) );
      assertEquals( "PURCHASE", iQuerySet.getString( "po_type_cd" ) );
      assertEquals( "NORMAL", iQuerySet.getString( "req_priority_cd" ) );
      assertEquals( 50.0, iQuerySet.getDouble( "total_price" ), 1.0 );
      assertEquals( "P_POMGR", iQuerySet.getString( "next_auth_cd" ) );
      assertEquals( null, iQuerySet.getString( "spec2k_cust_cd" ) );
      assertEquals( "USER, TEST", iQuerySet.getString( "purchasing_contact" ) );
      assertEquals( iDockLocation, iQuerySet.getKey( LocationKey.class, "po_ship_to_loc_key" ) );
      assertEquals( PROMISEDBY_DATE, iQuerySet.getDate( "promised_by" ) );

   }


   /**
    * This tests that query returns no data created in the setup for user with low authorization
    * level
    */
   @Test
   public void testNoOrdersReturnedForLowAuthLvlUser() throws Exception {
      // execute the query with low authorization level user
      execute( iHrLowLevelAuthorizer );

      // verify no row is returned
      assertEquals( "item is returned", 0, iQuerySet.getRowCount() );

   }


   /**
    * Set up the test case.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void loadData() throws Exception {

      // create a supply location
      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // create HR
      iHrLowLevelAuthorizer = new HumanResourceDomainBuilder().withUsername( "USER1" )
            .withFirstName( "TEST" ).withLastName( "USER" ).build();

      // create HR
      iHrHighLevelAuthorizer = new HumanResourceDomainBuilder().withUsername( "USER2" )
            .withSupplyLocation( iSupplyLocation ).build();

      // build finance account
      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).withName( "TESTVENDOR" ).build();

      iOrder = new OrderBuilder().withStatus( RefEventStatusKey.POOPEN ).withVendor( iVendor )
            .withDescription( "PO1" ).withCreationDate( new Date() )
            .withContactHR( iHrLowLevelAuthorizer ).withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iDockLocation ).build();

      // build part
      iPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "SER_PART" )
            .withShortDescription( "SER part for testing" ).withInventoryClass( RefInvClassKey.SER )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).build();

      // build order line
      new OrderLineBuilder( iOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
            .withOrderQuantity( new BigDecimal( 5.0 ) ).promisedBy( PROMISEDBY_DATE )
            .forPart( iPartNoKey ).withAccount( iAccount ).build();

      // add data in ref tables since query-test DB does not contain any
      addRefPOAuthFlowData();
      addRefPOAuthLvlData();
      addOrgHrPOAuthLvlData();

      // request authorization for order
      OrderService.requestAuthorization( iOrder, new RefPoAuthFlowKey( 10, "PURCHASE" ),
            "requesting authorization", iHrLowLevelAuthorizer );

   }


   // insert data in ref_po_auth_flow table
   private void addRefPOAuthFlowData() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "po_auth_flow_db_id", 10 );
      lArgs.add( "po_auth_flow_cd", "PURCHASE" );
      lArgs.add( "po_type_db_id", 0 );
      lArgs.add( "po_type_cd", "PURCHASE" );
      lArgs.add( "desc_sdesc", "Purchasing Authorization" );
      MxDataAccess.getInstance().executeInsert( "ref_po_auth_flow", lArgs );

   }


   // insert data in ref_po_auth_lvl table
   private void addRefPOAuthLvlData() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "po_auth_lvl_db_id", 10 );
      lArgs.add( "po_auth_lvl_cd", "P_POMGR" );
      lArgs.add( "po_auth_flow_db_id", 10 );
      lArgs.add( "po_auth_flow_cd", "PURCHASE" );
      lArgs.add( "user_cd", "POMGR" );
      lArgs.add( "limit_price", 999999 );
      MxDataAccess.getInstance().executeInsert( "ref_po_auth_lvl", lArgs );
   }


   // insert data in ref_po_auth_lvl table
   private void addOrgHrPOAuthLvlData() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "hr_db_id", iHrHighLevelAuthorizer.getDbId() );
      lArgs.add( "hr_id", iHrHighLevelAuthorizer.getId() );
      lArgs.add( "po_auth_lvl_db_id", 10 );
      lArgs.add( "po_auth_lvl_cd", "P_POMGR" );
      MxDataAccess.getInstance().executeInsert( "org_hr_po_auth_lvl", lArgs );
   }


   /**
    * Execute the query.
    *
    * @param aUserHrKey
    *           the user for whom the to-be-authorized-orders data needs to be tested.
    */
   private void execute( HumanResourceKey aUserHrKey ) {

      // Build the arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aUserHrKey, "aHrDbId", "aHrId" );
      lArgs.add( "aOnlyShowYourAttention", 1 );

      // execute the query with arguments
      iQuerySet = QueryExecutor.executeQuery( getClass(), lArgs );
   }
}
