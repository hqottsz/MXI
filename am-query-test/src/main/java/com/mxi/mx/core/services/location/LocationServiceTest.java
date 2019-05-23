package com.mxi.mx.core.services.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.inv.InvLocZoneTable;


/**
 * This class tests the LocationService class.
 *
 */
public class LocationServiceTest {

   private static final String BIN_CODE = RefLocTypeKey.BIN.getCd();
   private static final String STORE_CODE = RefLocTypeKey.STORE.getCd();

   private LocationDetailsTO locationSetDetailsTO;
   private LocationDetailsTO locationGetDetailsTO;
   private HumanResourceKey humanResourceKey;
   private LocationKey storeLocationKey;
   private LocationKey binLocationKey;

   private final int ROUTE_ORDER = 2;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() throws Exception {

      locationSetDetailsTO = new LocationDetailsTO();
      locationGetDetailsTO = new LocationDetailsTO();

      humanResourceKey = Domain.createHumanResource();

      // Create a Store type location
      storeLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ATL/STORE" );
         aLocation.setName( "Atlanta" );
         aLocation.setTimeZone( TimeZoneKey.LONDON );
         aLocation.setType( RefLocTypeKey.STORE );
      } );

      // Create a Bin type location
      binLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ATL/BIN" );
         aLocation.setName( "Atlanta" );
         aLocation.setType( RefLocTypeKey.BIN );
      } );

   }


   /**
    * GIVEN a bin location WHEN the route order is set to the bin THEN route order is preserved for
    * the bin
    *
    * @throws MxException
    *            if an error occurs.
    * @throws TriggerException
    */
   @Test
   public void testRouteOrderPreserveForBinLocation() throws MxException, TriggerException {

      locationSetDetailsTO.setTypeCode( BIN_CODE );
      locationSetDetailsTO.setRouteOrder( ROUTE_ORDER );
      LocationService.setDetails( binLocationKey, locationSetDetailsTO, humanResourceKey );

      locationGetDetailsTO = LocationService.getDetails( binLocationKey );

      assertEquals( ROUTE_ORDER, locationGetDetailsTO.getRouteOrder(), 0 );

   }


   /**
    * GIVEN a store location WHEN the route order is set to the store THEN route order is not
    * preserved for the store
    *
    * @throws MxException
    *            if an error occurs.
    * @throws TriggerException
    */
   @Test
   public void testRouteOrderNotPreserveForNonBinLocation() throws MxException, TriggerException {

      locationSetDetailsTO.setTypeCode( STORE_CODE );
      locationSetDetailsTO.setRouteOrder( ROUTE_ORDER );
      LocationService.setDetails( storeLocationKey, locationSetDetailsTO, humanResourceKey );

      locationGetDetailsTO = LocationService.getDetails( storeLocationKey );

      assertNull( "Route order is null for non-bin locations",
            locationGetDetailsTO.getRouteOrder() );
   }


   /**
    * GIVEN a bin location WHEN the parent location is changed THEN route order is not preserved for
    * the bin
    *
    * @throws MxException
    *            if an error occurs.
    * @throws TriggerException
    */
   @Test
   public void testRouteOrderRemovedWhenParentLocationChanged()
         throws MxException, TriggerException {

      // Create a new parent location
      LocationKey lNewParentLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setCode( "BOS" );
         aLocation.setName( "Boston" );
         aLocation.setTimeZone( TimeZoneKey.MOSCOW );
         aLocation.setType( RefLocTypeKey.STORE );
      } );

      // set parent to the bin
      binLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setParent( storeLocationKey );
      } );

      // Set route order for the BIN location
      locationSetDetailsTO.setTypeCode( BIN_CODE );
      locationSetDetailsTO.setRouteOrder( ROUTE_ORDER );
      LocationService.setDetails( binLocationKey, locationSetDetailsTO, humanResourceKey );

      // Assign a new parent location for the BIN location
      LocationService.assignParentLocation( binLocationKey, lNewParentLocationKey );

      // Call to remove the route order of the BIN
      LocationService.removeRouteOrder( binLocationKey );

      locationGetDetailsTO = LocationService.getDetails( binLocationKey );

      assertNull( "Route order becomes null when parent location is changed",
            locationGetDetailsTO.getRouteOrder() );
   }


   /**
    * GIVEN a bin location WHEN the location is removed THEN route order details are not preserved
    * for the bin in INV_LOC_ZONE table
    *
    * @throws MxException
    *            if an error occurs.
    * @throws TriggerException
    */
   @Test
   public void testRouteOrderDetailsWhenLocationRemoved() throws MxException, TriggerException {

      // Set route order for the BIN location
      locationSetDetailsTO.setTypeCode( BIN_CODE );
      locationSetDetailsTO.setRouteOrder( ROUTE_ORDER );
      LocationService.setDetails( binLocationKey, locationSetDetailsTO, humanResourceKey );

      // Delete the BIN location
      LocationService.delete( binLocationKey );

      assertFalse( "Table row is deleted",
            InvLocZoneTable.findByPrimaryKey( binLocationKey ).exists() );

   }


   /**
    *
    * GIVEN vendor track or vendor line location WHEN location's auto_issue_bool is set to true AND
    * we find location by task key THEN it returns true
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAutoIssueBoolOfLocation() throws Exception {

      // add task
      TaskKey lTaskKey = Domain.createAdhocTask( aTaskKey -> {
         aTaskKey.addLabour( aLabour -> {
            aLabour.setSkill( RefLabourSkillKey.ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
      } );

      // create Vendor Line/Vendor Track location
      LocationKey lLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.VENLINE );
         aLocation.setAutoIssueBool( true );
      } );

      // create the work package
      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lTaskKey );
         aWorkPackage.setLocation( lLocationKey );
      } );

      Boolean lAutoIssue = new LocationService().getAutoIssueBool( lTaskKey );

      // assert that auto_issue_bool is true when work package location is Vendor Line and Vendor
      // Track
      assertEquals( true, lAutoIssue );

   }

}
