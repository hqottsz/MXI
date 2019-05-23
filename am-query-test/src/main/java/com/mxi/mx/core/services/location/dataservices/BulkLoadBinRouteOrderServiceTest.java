package com.mxi.mx.core.services.location.dataservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.dao.location.InvLocZoneDao;
import com.mxi.mx.core.key.InvLocZoneKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.services.dataservices.transferobject.BulkLoadElementTO;
import com.mxi.mx.core.table.inv.InvLocZoneTable;


/**
 * Tests for BulkLoadBinRouteOrderService class
 *
 * @author deatlk
 *
 */
public class BulkLoadBinRouteOrderServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private LocationKey iParentLocationKey;
   private LocationKey iBinLocationKey;
   private BulkLoadElementTO iBulkLoadElementTO;


   @Before
   public void setUp() throws Exception {
      // Create a parent location
      iParentLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setCode( "PARENTSRVSTORE1/STORE" );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
         aLocation.setName( "Parent Store" );
         aLocation.setTimeZone( TimeZoneKey.MOSCOW );
      } );
      // Create a bin location
      iBinLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setCode( "PARENTSRVSTORE1/STORE/BINA" );
         aLocation.setType( RefLocTypeKey.BIN );
         aLocation.setName( "Parent Store Bin A" );
         aLocation.setParent( iParentLocationKey );
      } );
      iBulkLoadElementTO = new BulkLoadElementTO();

   }


   /**
    *
    * GIVEN a valid data row in the form of a BulkLoadElementTO transfer object, for a bin route
    * order that does not exist in the Maintenix db, WHEN the TO is passed to the
    * {@link BulkLoadBinRouteOrderService.processRow()} method, THEN a bin route order record is
    * created AND all fields are set as expected.
    *
    */
   @Test
   public void testBinRouteOrderForHappyPath() {

      // Insert new route value for a bin location

      iBulkLoadElementTO.setC0( "PARENTSRVSTORE1/STORE" );
      iBulkLoadElementTO.setC1( "PARENTSRVSTORE1/STORE/BINA" );
      iBulkLoadElementTO.setC2( "10" );

      BulkLoadBinRouteOrderService lBulkLoadBinRouteOrderService =
            new BulkLoadBinRouteOrderService();
      lBulkLoadBinRouteOrderService.init( iBulkLoadElementTO );

      lBulkLoadBinRouteOrderService.processRow( iBulkLoadElementTO );

      InvLocZoneTable lInvLocZoneTable = InvLocZoneTable.findByPrimaryKey( iBinLocationKey );

      assertEquals( new Integer( 10 ), lInvLocZoneTable.getRouteOrder() );

   }


   /**
    *
    * GIVEN a valid data row in the form of a BulkLoadElementTO transfer object, for a bin route
    * order that exists in the Maintenix db, WHEN the TO is passed to the
    * {@link BulkLoadBinRouteOrderService.processRow()} method, THEN a bin route order record is
    * updated AND all fields are set as expected.
    *
    */
   @Test
   public void testBinRouteUpdateForHappyPath() {
      // Create a bin route order record
      addRouteOrder( iBinLocationKey, 10 );
      InvLocZoneTable lInvLocZoneTable = InvLocZoneTable.findByPrimaryKey( iBinLocationKey );
      assertEquals( new Integer( 10 ), lInvLocZoneTable.getRouteOrder() );

      // Update the route order of the created bin location
      BulkLoadBinRouteOrderService lBulkLoadBinRouteOrderService =
            new BulkLoadBinRouteOrderService();
      iBulkLoadElementTO.setC0( "PARENTSRVSTORE1/STORE" );
      iBulkLoadElementTO.setC1( "PARENTSRVSTORE1/STORE/BINA" );
      iBulkLoadElementTO.setC2( "100" );

      lBulkLoadBinRouteOrderService.init( iBulkLoadElementTO );
      lBulkLoadBinRouteOrderService.processRow( iBulkLoadElementTO );
      lInvLocZoneTable = InvLocZoneTable.findByPrimaryKey( iBinLocationKey );

      assertEquals( new Integer( 100 ), lInvLocZoneTable.getRouteOrder() );
   }


   /**
    *
    * GIVEN a valid data row in the form of a BulkLoadElementTO transfer object, for a bin route
    * order that exists in the Maintenix db, WHEN the TO is passed to the
    * {@link BulkLoadBinRouteOrderService.processRow()} method and it's route order is erased, THEN
    * the bin route order record is deleted from the database.
    *
    */
   @Test
   public void testBinRouteDeleteForHappyPath() {
      // Create a bin route order record
      addRouteOrder( iBinLocationKey, 10 );
      InvLocZoneTable lInvLocZoneTable = InvLocZoneTable.findByPrimaryKey( iBinLocationKey );
      assertEquals( new Integer( 10 ), lInvLocZoneTable.getRouteOrder() );

      // Delete the route order of the created bin location
      BulkLoadBinRouteOrderService lBulkLoadBinRouteOrderService =
            new BulkLoadBinRouteOrderService();
      iBulkLoadElementTO.setC0( "PARENTSRVSTORE1/STORE" );
      iBulkLoadElementTO.setC1( "PARENTSRVSTORE1/STORE/BINA" );
      iBulkLoadElementTO.setC2( "" );

      lBulkLoadBinRouteOrderService.init( iBulkLoadElementTO );
      lBulkLoadBinRouteOrderService.processRow( iBulkLoadElementTO );

      lInvLocZoneTable = new InvLocZoneTable( iBinLocationKey );
      assertFalse( lInvLocZoneTable.exists() );
   }


   /**
    *
    * Adds a route order to a bin location
    *
    * @param aBinLocationKey
    *           the bin location key
    * @param aRouteOrder
    *           the route order
    */
   public static void addRouteOrder( LocationKey aBinLocationKey, Integer aRouteOrder ) {
      InvLocZoneDao lInvLocZoneDao = InjectorContainer.get().getInstance( InvLocZoneDao.class );
      InvLocZoneTable lInvLocZoneTable =
            lInvLocZoneDao.create( new InvLocZoneKey( aBinLocationKey ) );
      lInvLocZoneTable.setRouteOrder( aRouteOrder );
      lInvLocZoneDao.insert( lInvLocZoneTable );
   }

}
