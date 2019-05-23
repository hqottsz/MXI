
package com.mxi.mx.web.query.location.locationdetails;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Test case for ScheduledWorkPackages.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ScheduledWorkPackagesTest {

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey locationKey;
   private InventoryKey aircraftKey;
   private InventoryKey engineKey;
   private HumanResourceKey hrWithAllAuthority;


   @Before
   public void loadData() {

      hrWithAllAuthority = Domain.createHumanResource( hr -> {
         hr.setAllAuthority( true );
      } );

      locationKey = Domain.createLocation( location -> {
         location.setCode( "TEST" );
         location.setType( RefLocTypeKey.LINE );
      } );

   }


   /**
    * Tests multiple Work Packages are returned for a location.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testMultipleWorkPackagesReturned() throws Exception {

      DataLoaders.load( databaseConnectionRule.getConnection(), ScheduledWorkPackagesTest.class );

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aLocDbId", 4650 );
      lArgs.add( "aLocId", 1000001 );
      lArgs.add( "aHrDbId", hrWithAllAuthority.getDbId() );
      lArgs.add( "aHrId", hrWithAllAuthority.getId() );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 3, lDs.getRowCount() );

      lDs.next();
      testRow( lDs, "4650:122287", "T00014P6", "TestAllowDifferentUserLabourSignoffWorkPackage",
            new LocationKey( "4650:1000001" ), "ABQ/LINE", "07-Aug-0009 03:31:12", "COMMIT",
            new InventoryKey( "4650:65617" ), "Falcon 2000 - 123" );

      lDs.next();
      testRow( lDs, "4650:122418", "T00014Q2",
            "TestAllowDifferentUserWorkPackageSignoffWorkPackage",
            new LocationKey( "4650:1000001" ), "ABQ/LINE", "07-Aug-0021 03:31:12", "IN WORK",
            new InventoryKey( "4650:332970" ), "Boeing 767-232 - UT1" );
      lDs.next();
      testRow( lDs, "4650:133857", "T00017SP", "Workpackage1", new LocationKey( "4650:1000001" ),
            "ABQ/LINE", "09-Jul-0030 03:31:12", "IN WORK", new InventoryKey( "4650:370873" ),
            "Airbus A319/A320 - 0903JUT" );
   }


   /**
    * Test IN-WORK work packages are returned. Also verifies completed work packages are not
    * returned.
    */
   @Test
   public void testInWorkWorkPackageReturned() {

      createDefaultWorkPackageTestData();

      // ARRANGE
      TaskKey inWorkWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "In Work WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftKey );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( inWorkWPKey, ds.getKey( TaskKey.class, "wp_key" ) );

   }


   @Test
   public void testCommittedWorkPackageReturned() {

      createDefaultWorkPackageTestData();

      // ARRANGE
      TaskKey committedWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Committed WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftKey );
         wp.setStatus( RefEventStatusKey.COMMIT );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( committedWPKey, ds.getKey( TaskKey.class, "wp_key" ) );

   }


   /**
    * Test scheduled work packages are returned. Scheduled work packages have a status of 'ACTV'.
    */
   @Test
   public void testScheduledWorkPackageReturned() {

      createDefaultWorkPackageTestData();

      // ARRANGE
      TaskKey scheduledWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Scheduled WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( scheduledWPKey, ds.getKey( TaskKey.class, "wp_key" ) );

   }


   /**
    * Test IN-WORK component work packages are returned. Also verifies completed component work
    * packages are not returned.
    */
   @Test
   public void testInWorkComponentWorkPackageReturned() {

      createDefaultComponentWorkPackageTestData();

      // ARRANGE
      TaskKey inWorkWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( inWorkWPKey, ds.getKey( TaskKey.class, "wp_key" ) );

   }


   @Test
   public void testCommittedComponentWorkPackageReturned() {

      createDefaultComponentWorkPackageTestData();

      // ARRANGE
      TaskKey committedWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.COMMIT );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( committedWPKey, ds.getKey( TaskKey.class, "wp_key" ) );

   }


   /**
    * Test scheduled component work packages are returned. Scheduled component work packages have a
    * status of 'ACTV'.
    */
   @Test
   public void testScheduledComponentWorkPackageReturned() {

      createDefaultComponentWorkPackageTestData();

      // ARRANGE

      TaskKey scheduledWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( scheduledWPKey, ds.getKey( TaskKey.class, "wp_key" ) );

   }


   /**
    * Test that component work packages are returned for locations associated to a task in the
    * component wp - tasks in a component work package could be assigned to different shop
    * locations. The component work package should show up (only once) when on the sublocation's
    * details page.
    */
   @Test
   public void testComponentWorkPackageReturnedForTasksAtSameLocation() {

      // ARRANGE
      LocationKey triageLocation = Domain.createLocation();

      LocationKey subLocation1 = Domain.createLocation( location -> {
         location.setParent( triageLocation );
      } );

      LocationKey subLocation2 = Domain.createLocation( location -> {
         location.setParent( triageLocation );
      } );

      // create tasks at different locations from the ro
      TaskKey taskAtLocation1 = Domain.createAdhocTask( task -> {
         task.setLocationKey( subLocation1 );
      } );

      TaskKey taskAtLocation2 = Domain.createAdhocTask( task -> {
         task.setLocationKey( subLocation2 );
      } );

      TaskKey secondTaskAtLocation2 = Domain.createAdhocTask( task -> {
         task.setLocationKey( subLocation2 );
      } );

      TaskKey scheduledROKey = Domain.createComponentWorkPackage( ro -> {
         ro.atLocation( triageLocation );
         ro.assignTask( taskAtLocation1 );
         ro.assignTask( taskAtLocation2 );
         ro.assignTask( secondTaskAtLocation2 );
         ro.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - test that component work package is returned for triage location
      DataSet ds = execute( triageLocation, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( scheduledROKey, ds.getKey( TaskKey.class, "wp_key" ) );

      // ACT - test that component work package is returned for sublocation 1
      ds = execute( subLocation1, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( scheduledROKey, ds.getKey( TaskKey.class, "wp_key" ) );

      // ACT - test that the component work package is returned for sublocation 2 only once (no
      // duplicates)
      ds = execute( subLocation2, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getRowCount() );
      assertEquals( scheduledROKey, ds.getKey( TaskKey.class, "wp_key" ) );
   }


   @Test
   public void testComponentWorkPackageNotReturnedForScrappedInventory() {

      // ARRANGE
      InventoryKey scrappedEngineKey = Domain.createEngine( engine -> {
         engine.setCondition( RefInvCondKey.SCRAP );
      } );

      TaskKey scheduledWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( scrappedEngineKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertFalse( "Expecting no rows to be returned.", ds.next() );

   }


   @Test
   public void testComponentWorkPackageNotReturnedForArchivedInventory() {

      // ARRANGE
      InventoryKey archivedEngineKey = Domain.createEngine( engine -> {
         engine.setCondition( RefInvCondKey.ARCHIVE );
      } );

      TaskKey scheduledWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( archivedEngineKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertFalse( "Expecting no rows to be returned.", ds.next() );

   }


   @Test
   public void testWorkPackageNotReturnedForScrappedAircraft() {

      // ARRANGE
      InventoryKey scrappedAircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setCondition( RefInvCondKey.SCRAP );
      } );

      TaskKey scheduledWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Scheduled WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( scrappedAircraftKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertFalse( ds.next() );

   }


   @Test
   public void testWorkPackageNotReturnedForArchivedAircraft() {

      // ARRANGE
      InventoryKey archivedAircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setCondition( RefInvCondKey.ARCHIVE );
      } );

      TaskKey scheduledWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Scheduled WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( archivedAircraftKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertFalse( ds.next() );

   }


   /**
    * Test authorities on aircraft. Verifies that a user with all authorities can see the work
    * packages for the aircraft.
    */
   @Test
   public void testWorkPackageReturnedForUserWithAllAuthority() {

      // ARRANGE

      AuthorityKey authorityKey = Domain.createAuthority();

      // aircraft with authority
      InventoryKey aircraftWithAuthorityKey = Domain.createAircraft( aircraft -> {
         aircraft.withAuthority( authorityKey );
      } );

      TaskKey scheduledWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Scheduled WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftWithAuthorityKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - verify that user with all authorities can see the work packages for the aircraft
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );

   }


   /**
    * Test authorities on aircraft. Verifies that a user with all authorities set to false and no
    * specific authority can not see the work packages for the aircraft.
    */
   @Test
   public void testWorkPackageNotReturnedForUserWithoutAuthority() {

      // ARRANGE

      AuthorityKey authorityKey = Domain.createAuthority();

      // user with no authority
      HumanResourceKey hrWithNoAuthorityKey = Domain.createHumanResource( hr -> {
         hr.setAllAuthority( false );
      } );

      // aircraft with authority
      InventoryKey aircraftWithAuthorityKey = Domain.createAircraft( aircraft -> {
         aircraft.withAuthority( authorityKey );
      } );

      TaskKey scheduledWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Scheduled WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftWithAuthorityKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - verify that user with no authorities and all authorities set to false can not see the
      // work packages for the aircraft
      DataSet ds = execute( locationKey, hrWithNoAuthorityKey );

      // ASSERT
      assertFalse( ds.next() );

   }


   /**
    * Test authorities on aircraft. Verifies that a user with all authorities set to false and
    * assigned the authority can see the work packages for the aircraft.
    */
   @Test
   public void testWorkPackageReturnedForUserWithAuthority() {

      // ARRANGE

      AuthorityKey authorityKey = Domain.createAuthority();

      // user with authority
      HumanResourceKey hrWithAuthorityKey = Domain.createHumanResource( hr -> {
         hr.setAllAuthority( false );
         hr.addAuthority( authorityKey );
      } );

      // aircraft with authority
      InventoryKey aircraftWithAuthorityKey = Domain.createAircraft( aircraft -> {
         aircraft.withAuthority( authorityKey );
      } );

      TaskKey scheduledWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Scheduled WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftWithAuthorityKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - verify that user with the specific authority can see the work packages for the
      // aircraft
      DataSet ds = execute( locationKey, hrWithAuthorityKey );

      // ASSERT
      assertTrue( ds.next() );

   }


   /**
    * Test authorities on components. Verifies that a user with all authorities can see the
    * component work packages for the component.
    */
   @Test
   public void testComponentWorkPackageReturnedForUserWithAllAuthority() {

      // ARRANGE

      AuthorityKey authorityKey = Domain.createAuthority();

      // aircraft with authority
      InventoryKey engineWithAuthorityKey = Domain.createEngine( engine -> {
         engine.setAuthority( authorityKey );
      } );

      TaskKey scheduledWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineWithAuthorityKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - verify that user with all authorities can see the component work package
      DataSet ds = execute( locationKey, hrWithAllAuthority );

      // ASSERT
      assertTrue( ds.next() );

   }


   /**
    * Test authorities on components. Verifies that a user with all authorities set to false and no
    * specific authority can not see the component work packages for the component.
    *
    */
   @Test
   public void testComponentWorkPackageNotReturnedForUserWithoutAuthority() {

      // ARRANGE

      AuthorityKey authorityKey = Domain.createAuthority();

      // user with no authority
      HumanResourceKey hrWithNoAuthorityKey = Domain.createHumanResource( hr -> {
         hr.setAllAuthority( false );
      } );

      // aircraft with authority
      InventoryKey engineWithAuthorityKey = Domain.createEngine( engine -> {
         engine.setAuthority( authorityKey );
      } );

      TaskKey scheduledWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineWithAuthorityKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - verify that user with no authorities and all authorities set to false can not see the
      // the component work package
      DataSet ds = execute( locationKey, hrWithNoAuthorityKey );

      // ASSERT
      assertFalse( ds.next() );

   }


   /**
    * Test authorities on components. Verifies that a user with all authorities set to false and
    * assigned the authority can see the component work packages for the component.
    */
   @Test
   public void testComponentWorkPackageReturnedForUserWitAuthority() {

      // ARRANGE

      AuthorityKey authorityKey = Domain.createAuthority();

      // user with authority
      HumanResourceKey hrWithAuthorityKey = Domain.createHumanResource( hr -> {
         hr.setAllAuthority( false );
         hr.addAuthority( authorityKey );
      } );

      // aircraft with authority
      InventoryKey engineWithAuthorityKey = Domain.createEngine( engine -> {
         engine.setAuthority( authorityKey );
      } );

      TaskKey scheduledWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineWithAuthorityKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT - verify that user with the specific authority can see the the component work package
      DataSet ds = execute( locationKey, hrWithAuthorityKey );

      // ASSERT
      assertTrue( ds.next() );

   }


   private void createDefaultWorkPackageTestData() {

      // aircraft for work packages
      aircraftKey = Domain.createAircraft();

      // Create a completed wp against the aircraft. The completed wp should never be returned by
      // the query.
      TaskKey completeWPKey = Domain.createWorkPackage( wp -> {
         wp.setName( "Complete WP" );
         wp.setLocation( locationKey );
         wp.setAircraft( aircraftKey );
         wp.setStatus( RefEventStatusKey.COMPLETE );
      } );

   }


   private void createDefaultComponentWorkPackageTestData() {

      // engine for component work packages
      engineKey = Domain.createEngine();

      // Create a completed component wp against the engine. The completed wp should never be
      // returned by the
      // query.
      TaskKey completedWPKey = Domain.createComponentWorkPackage( wp -> {
         wp.setInventory( engineKey );
         wp.atLocation( locationKey );
         wp.setStatus( RefEventStatusKey.COMPLETE );
      } );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aWorkPackage
    *           the work package.
    * @param aWorkPackageId
    *           the work package id.
    * @param aWorkPackageName
    *           the work package name.
    * @param aLocationKey
    *           the location key.
    * @param aLocationCd
    *           the location cd.
    * @param aEventGdt
    *           the event gdt.
    * @param aEventStatusCd
    *           the event status cd.
    * @param aInventory
    *           the inventory key.
    * @param aInventorySdesc
    *           the inventory sdesc.
    */
   private void testRow( DataSet aDs, String aWorkPackage, String aWorkPackageId,
         String aWorkPackageName, LocationKey aLocationKey, String aLocationCd, String aEventGdt,
         String aEventStatusCd, InventoryKey aInventory, String aInventorySdesc ) {

      // Check for the Work Package Key
      MxAssert.assertEquals( "wp_key", aWorkPackage, aDs.getString( "wp_key" ) );

      // Check for the work package id
      MxAssert.assertEquals( "wp_id", aWorkPackageId, aDs.getString( "wp_id" ) );

      // Check for the work package name
      MxAssert.assertEquals( "wp_name", aWorkPackageName, aDs.getString( "wp_name" ) );

      // Check for the location key
      MxAssert.assertEquals( "location_key", aLocationKey, aDs.getString( "location_key" ) );

      // Check for the locationCd
      MxAssert.assertEquals( "loc_cd", aLocationCd, aDs.getString( "loc_cd" ) );

      // Check for the event gdt
      MxAssert.assertEquals( "event_gdt", aEventGdt, aDs.getString( "event_gdt" ) );

      // Check for the event status cd
      MxAssert.assertEquals( "status_cd", aEventStatusCd, aDs.getString( "status_cd" ) );

      // Check for the inventory key
      MxAssert.assertEquals( "inv_key", aInventory, aDs.getString( "inv_key" ) );

      // Check for the inventory no sdesc
      MxAssert.assertEquals( "inv_no_sdesc", aInventorySdesc, aDs.getString( "inv_no_sdesc" ) );
   }


   private DataSet execute( LocationKey locationKey, HumanResourceKey hrKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocDbId", locationKey.getDbId() );
      lArgs.add( "aLocId", locationKey.getId() );
      lArgs.add( "aHrDbId", hrKey.getDbId() );
      lArgs.add( "aHrId", hrKey.getId() );

      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
