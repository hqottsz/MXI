
package com.mxi.mx.core.query.adapter.finance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.adapter.CoreAdapterUtils;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefVendorTypeKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.org.OrgVendor;
import com.mxi.mx.core.table.po.PoHeaderTable;


/**
 * Test case for GetOrderInformation.qrx
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOrderInformationTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iShippingLocation;
   private LocationKey iReShippingLocation;
   private VendorKey iVendor;
   private HumanResourceKey iContactHR;
   private QuerySet iQs;
   private VendorKey iBroker;
   private PurchaseOrderKey iPo;


   /**
    *
    * Verify that that query returns only one record
    *
    */
   @Test
   public void testOrderExist() {

      // set last modification date as today
      PoHeaderTable lPoHeaderTable = PoHeaderTable.findByPrimaryKey( iPo );
      lPoHeaderTable.setLastModDt( new Date() );
      lPoHeaderTable.update();

      // execute the query with a since date older than PO last modified date
      execute( CoreAdapterUtils.toDate( "2015-01-01 00:00:00" ) );

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

      // set last modification date in the past
      PoHeaderTable lPoHeaderTable = PoHeaderTable.findByPrimaryKey( iPo );
      lPoHeaderTable.setLastModDt( CoreAdapterUtils.toDate( "2015-01-01 00:00:00" ) );
      lPoHeaderTable.update();

      // execute the query with a since date later than the PO last modified date
      execute( new Date() );

      // assert row count
      assertTrue( "Expected the query to not return results.", iQs.isEmpty() );

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() {
      iShippingLocation =
            new LocationDomainBuilder().withName( "SHIPPING_LOCATION" ).isSupplyLocation().build();
      iReShippingLocation =
            new LocationDomainBuilder().withName( "RE-SHIPPING_LOCATION" ).isSupplyLocation().build();
      iContactHR = new HumanResourceDomainBuilder().build();
      iVendor = new VendorBuilder().withCode( "VC00001" )
            .withVendorType( RefVendorTypeKey.PURCHASE ).build();
      iBroker = new VendorBuilder().withCode( "VC00002" ).withVendorType( RefVendorTypeKey.BROKER )
            .build();

      // build PO
      iPo = new OrderBuilder().shippingTo( iShippingLocation ).reexpediteTo( iReShippingLocation )
            .withContactHR( iContactHR ).withVendor( iVendor ).withBroker( iBroker ).build();

   }


   /**
    * Execute the query.
    *
    */
   private void execute( Date aSinceDate ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSinceDate", aSinceDate );

      iQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.adapter.finance.GetOrderInformation", lArgs );
      iQs.first();
   }

}
