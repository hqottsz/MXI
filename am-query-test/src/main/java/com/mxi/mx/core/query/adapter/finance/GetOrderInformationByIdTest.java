
package com.mxi.mx.core.query.adapter.finance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.LocationContactBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationContactKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefVendorTypeKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.org.OrgVendor;


/**
 * Test case for GetOrderInformationById.qrx
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOrderInformationByIdTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iShippingLocation;
   private LocationKey iReShippingLocation;
   private PurchaseOrderKey iPo;
   private VendorKey iVendor;
   private HumanResourceKey iContactHR;
   private QuerySet iQs;
   private VendorKey iBroker;

   private static final PurchaseOrderKey INEXSISTENT_PO_KEY = new PurchaseOrderKey( 0, 0 );


   /**
    *
    * Verify that that query returns only one record
    *
    */
   @Test
   public void testOrderExist() {

      // build PO
      iPo = new OrderBuilder().shippingTo( iShippingLocation ).reexpediteTo( iReShippingLocation )
            .withContactHR( iContactHR ).withVendor( iVendor ).withBroker( iBroker ).build();
      execute();

      // assert row count
      assertTrue( "Expected the query to return results.", !iQs.isEmpty() );
      assertEquals( "Expected the query to return one row.", 1, iQs.getRowCount() );

      // assert individual elements
      assertEquals( iQs.getString( "broker_code" ),
            OrgVendor.findByPrimaryKey( iBroker ).getVendorCd() );

      assertEquals( iQs.getString( "vendor_code" ),
            OrgVendor.findByPrimaryKey( iVendor ).getVendorCd() );

      assertEquals( iQs.getString( "location_code" ),
            InvLocTable.findByPrimaryKey( iShippingLocation ).getLocCd() );

      assertEquals( iQs.getString( "re_ship_to_location_code" ),
            InvLocTable.findByPrimaryKey( iReShippingLocation ).getLocCd() );
   }


   /**
    *
    * Verify that that query does not return any record
    *
    */
   @Test
   public void testOrderNotExist() {

      // build PO
      iPo = INEXSISTENT_PO_KEY;

      execute();

      // assert row count
      assertTrue( "Expected the query to not return results.", iQs.isEmpty() );

   }


   /**
    *
    * Verify that that query returns only one row for PO with two contacts in the ship to location
    *
    * @throws Exception
    *
    */
   @Test
   public void testNoDuplicatedOrdersForMultipleContacts() throws Exception {

      // set up two contacts for ship to location
      setupMultipleContacts( iShippingLocation );

      // build PO
      iPo = new OrderBuilder().shippingTo( iShippingLocation ).reexpediteTo( iReShippingLocation )
            .withContactHR( iContactHR ).withVendor( iVendor ).withBroker( iBroker ).build();
      execute();

      // assert only one row is returned for the PO with multiple contacts for the ship to location
      assertTrue( "Expected the query to return results.", !iQs.isEmpty() );
      assertEquals( "Expected the query to return one row.", 1, iQs.getRowCount() );

      // assert individual elements
      assertEquals( iQs.getString( "broker_code" ),
            OrgVendor.findByPrimaryKey( iBroker ).getVendorCd() );

      assertEquals( iQs.getString( "vendor_code" ),
            OrgVendor.findByPrimaryKey( iVendor ).getVendorCd() );

      assertEquals( iQs.getString( "location_code" ),
            InvLocTable.findByPrimaryKey( iShippingLocation ).getLocCd() );

      assertEquals( iQs.getString( "re_ship_to_location_code" ),
            InvLocTable.findByPrimaryKey( iReShippingLocation ).getLocCd() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {

      iShippingLocation =
            new LocationDomainBuilder().withName( "SHIPPING_LOCATION" ).isSupplyLocation().build();

      iReShippingLocation =
            new LocationDomainBuilder().withName( "RE-SHIPPING_LOCATION" ).isSupplyLocation().build();
      iContactHR = new HumanResourceDomainBuilder().build();
      iVendor = new VendorBuilder().withCode( "VC00001" )
            .withVendorType( RefVendorTypeKey.PURCHASE ).build();
      iBroker = new VendorBuilder().withCode( "VC00002" ).withVendorType( RefVendorTypeKey.BROKER )
            .build();
   }


   /**
    *
    * set up location with two contacts
    *
    * @param aShippingLocation
    * @throws Exception
    */
   private void setupMultipleContacts( LocationKey aShippingLocation ) throws Exception {
      LocationContactKey lContact;
      lContact = buildContact( aShippingLocation, "Lee", "613-255 5500" );
      lContact = buildContact( aShippingLocation, "Mike", "613-366 9900" );
   }


   /**
    * build contact for location
    *
    * @param aContactName
    * @param aPhoneNumber
    * @throws Exception
    */
   private LocationContactKey buildContact( LocationKey aLocation, String aContactName,
         String aPhoneNumber ) throws Exception {

      LocationContactBuilder lLocationContactBuilder = new LocationContactBuilder();
      lLocationContactBuilder.withContactName( aContactName );
      lLocationContactBuilder.withPhoneNumber( aPhoneNumber );
      lLocationContactBuilder.forLocation( aLocation );
      LocationContactKey lContact = lLocationContactBuilder.build();

      return lContact;
   }


   /**
    * Execute the query.
    *
    */
   private void execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iPo, "aPoDbId", "aPoId" );

      iQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.adapter.finance.GetOrderInformationById", lArgs );
      iQs.first();
   }
}
