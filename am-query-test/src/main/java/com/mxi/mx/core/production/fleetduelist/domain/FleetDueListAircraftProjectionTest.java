package com.mxi.mx.core.production.fleetduelist.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.hr.domain.HumanResourceAssignedAuthoritiesEvent;
import com.mxi.mx.core.hr.domain.HumanResourceCreatedEvent;
import com.mxi.mx.core.hr.domain.HumanResourceDeletedEvent;
import com.mxi.mx.core.hr.domain.HumanResourceGrantedAllAuthorityEvent;
import com.mxi.mx.core.hr.domain.HumanResourceRevokedAllAuthorityEvent;
import com.mxi.mx.core.hr.domain.HumanResourceRevokedAuthoritiesEvent;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftArchivedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftAuthorityChangedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftLockedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftScrappedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftUnarchivedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftUnlockedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftUnscrappedEvent;


/**
 * Test event handlers that impact fleet due list projection table.
 *
 */
public class FleetDueListAircraftProjectionTest {

   private static final String HUMAN_RESOURCE_DB_ID_COL = "hr_db_id";
   private static final String HUMAN_RESOURCE_ID_COL = "hr_id";
   private static final String AIRCRAFT_DB_ID_COL = "inv_no_db_id";
   private static final String AIRCRAFT_ID_COL = "inv_no_id";
   private static final String AUTORITY_DB_ID_COL = "authority_db_id";
   private static final String AUTORITY_ID_COL = "authority_id";
   private static final String MT_INV_AC_AUTHORITY_TABLE = "mt_inv_ac_authority";

   private static final String[] MT_INV_AC_AUTHORITY_COLS =
         { HUMAN_RESOURCE_DB_ID_COL, HUMAN_RESOURCE_ID_COL, AIRCRAFT_DB_ID_COL, AIRCRAFT_ID_COL,
               AUTORITY_DB_ID_COL, AUTORITY_ID_COL };

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   private FleetDueListAircraftProjection fleetDueListAircraftProjection =
         new FleetDueListAircraftProjection();

   private AircraftKey aircraftKeyWithNoAuthority;
   private AircraftKey aircraftKeyWithAirCanadaAuthority;
   private AircraftKey aircraftKeyWithWestJetAuthority;

   private AuthorityKey authorityKeyAirCanada;
   private AuthorityKey authorityKeyWestJet;

   private AircraftKey archivedAircraftKeyWithNoAuthority;
   private AircraftKey archivedAircraftKeyWithAuthority;

   private AircraftKey scrappedAircraftKeyWithNoAuthority;
   private AircraftKey scrappedAircraftKeyWithAuthority;


   @Before
   public void setUp() {
      authorityKeyAirCanada = Domain.createAuthority( authority -> {
         authority.setAuthorityCode( "ACC" );
         authority.setAuthorityName( "AIR CANADA" );
      } );

      authorityKeyWestJet = Domain.createAuthority( authority -> {
         authority.setAuthorityCode( "WJ" );
         authority.setAuthorityName( "West Jet" );
      } );

      aircraftKeyWithNoAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setLocked( false );
         aircraft.withAuthority( null );
      } ) );

      aircraftKeyWithAirCanadaAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setLocked( false );
         aircraft.withAuthority( authorityKeyAirCanada );
      } ) );

      aircraftKeyWithWestJetAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setLocked( false );
         aircraft.withAuthority( authorityKeyWestJet );
      } ) );

      archivedAircraftKeyWithNoAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setCondition( RefInvCondKey.ARCHIVE );
         aircraft.withAuthority( null );
      } ) );

      archivedAircraftKeyWithAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setCondition( RefInvCondKey.ARCHIVE );
         aircraft.withAuthority( authorityKeyAirCanada );
      } ) );

      scrappedAircraftKeyWithNoAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setCondition( RefInvCondKey.SCRAP );
         aircraft.withAuthority( null );
      } ) );

      scrappedAircraftKeyWithAuthority = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setCondition( RefInvCondKey.SCRAP );
         aircraft.withAuthority( authorityKeyAirCanada );
      } ) );
   }


   @Test
   public void
         whenUnlockAircraftWithNoAuthorityThenHumanResourceWithAllAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftUnlockedEvent( aircraftKeyWithNoAuthority,
            null, null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnlockAircraftWithNoAuthorityThenHumanResourceWithNoAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftUnlockedEvent( aircraftKeyWithNoAuthority,
            null, null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnlockAircraftWithNoAuthorityThenHumanResourceWithSpecificyAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftUnlockedEvent( aircraftKeyWithNoAuthority,
            null, null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnlockAircraftWithAuthorityThenHumanResourceWithSameAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftUnlockedEvent(
            aircraftKeyWithAirCanadaAuthority, null, null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );;
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void whenUnlockAircraftWithAuthorityThenHumamResourceWithAllAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftUnlockedEvent(
            aircraftKeyWithAirCanadaAuthority, null, null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );;
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnlockAircraftWithAuthorityThenHumanResourceWithNoAuthorityShouldNotSeeAircraft() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftUnlockedEvent(
            aircraftKeyWithAirCanadaAuthority, null, null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );;
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenTwoUnlockedEventsForTheSameAircraftArePublishedThenNoDuplicateRecordAreCreated()
         throws InterruptedException {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      ExecutorService executor = Executors.newFixedThreadPool( 2 );
      Callable<Void> eventOne = () -> {
         fleetDueListAircraftProjection.on( new AircraftUnlockedEvent( aircraftKeyWithNoAuthority,
               null, null, HumanResourceKey.ADMIN ) );
         return null;
      };
      Callable<Void> eventTwo = () -> {
         fleetDueListAircraftProjection.on( new AircraftUnlockedEvent( aircraftKeyWithNoAuthority,
               null, null, HumanResourceKey.ADMIN ) );
         return null;
      };
      List<Callable<Void>> events = Arrays.asList( eventOne, eventTwo );

      // ACT
      executor.invokeAll( events );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void whenAircraftAuthorityChangedThenHumanResourceWithOldAuthorityShouldNotSeeAircraft() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftAuthorityChangedEvent( aircraftKeyWithAirCanadaAuthority,
                  authorityKeyAirCanada, new Date(), HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenAircraftAuthorityChangedThenHumanResourceWithNewAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftAuthorityChangedEvent( aircraftKeyWithAirCanadaAuthority,
                  authorityKeyAirCanada, new Date(), HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenLockAircraftEventIsPublishedForAnAircraftThenTheAircraftRecordIsRemovedFromfleetDueList() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithNoAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      QuerySet querySetInitial = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySetInitial.getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftLockedEvent( aircraftKeyWithNoAuthority, null,
            null, HumanResourceKey.ADMIN ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 0, querySet.getRowCount() );

   }


   @Test
   public void
         whenScrapAircraftEventIsPublishedForAnAircraftThenTheAircraftRecordIsRemovedFromfleetDueList() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithNoAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      QuerySet querySetInitial = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySetInitial.getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftScrappedEvent( aircraftKeyWithNoAuthority,
            null, null, HumanResourceKey.ADMIN, new FncAccountKey( "4650:106" ),
            new RefInvCondKey( 0, "SCRAP" ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 0, querySet.getRowCount() );

   }


   @Test
   public void
         whenArchiveAircraftEventIsPublishedForAnAircraftThenTheAircraftRecordIsRemovedFromfleetDueList() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithNoAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      QuerySet querySetInitial = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySetInitial.getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new AircraftArchivedEvent( aircraftKeyWithNoAuthority,
            null, null, HumanResourceKey.ADMIN, new FncAccountKey( "4650:106" ),
            new RefInvCondKey( 0, "ARCHIVE" ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 0, querySet.getRowCount() );

   }


   @Test
   public void
         whenUnarchivAircraftWithNoAuthorityThenHumanResourceWithAllAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithNoAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnarchiveAircraftWithNoAuthorityThenHumanResourceWithNoAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithNoAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnarchiveAircraftWithNoAuthorityThenHumanResourceWithSpecificyAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithNoAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnarchiveAircraftWithAuthorityThenHumanResourceWithSameAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnarchiveAircraftWithAuthorityThenHumamResourceWithAllAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnarchiveAircraftWithAuthorityThenHumanResourceWithNoAuthorityShouldNotSeeAircraft() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenTwoUnarchiveEventsForTheSameAircraftArePublishedThenNoDuplicateRecordAreCreated()
         throws InterruptedException {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      ExecutorService executor = Executors.newFixedThreadPool( 2 );
      Callable<Void> eventOne = () -> {
         fleetDueListAircraftProjection
               .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithNoAuthority, null, null,
                     null, HumanResourceKey.ADMIN, null, null ) );
         return null;
      };
      Callable<Void> eventTwo = () -> {
         fleetDueListAircraftProjection
               .on( new AircraftUnarchivedEvent( archivedAircraftKeyWithNoAuthority, null, null,
                     null, HumanResourceKey.ADMIN, null, null ) );
         return null;
      };
      List<Callable<Void>> events = Arrays.asList( eventOne, eventTwo );

      // ACT
      executor.invokeAll( events );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, archivedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnscrapAircraftWithNoAuthorityThenHumanResourceWithAllAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithNoAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnscrapAircraftWithNoAuthorityThenHumanResourceWithNoAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithNoAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnscrapAircraftWithNoAuthorityThenHumanResourceWithSpecificyAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithNoAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnscrapAircraftWithAuthorityThenHumanResourceWithSameAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnscrapAircraftWithAuthorityThenHumamResourceWithAllAuthorityShouldSeeAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenUnscrapAircraftWithAuthorityThenHumanResourceWithNoAuthorityShouldNotSeeAircraft() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithAuthority, null, null, null,
                  HumanResourceKey.ADMIN, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenTwoUnscrapEventsForTheSameAircraftArePublishedThenNoDuplicateRecordAreCreated()
         throws InterruptedException {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      ExecutorService executor = Executors.newFixedThreadPool( 2 );
      Callable<Void> eventOne = () -> {
         fleetDueListAircraftProjection
               .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithNoAuthority, null, null,
                     null, HumanResourceKey.ADMIN, null, null ) );
         return null;
      };
      Callable<Void> eventTwo = () -> {
         fleetDueListAircraftProjection
               .on( new AircraftUnscrappedEvent( scrappedAircraftKeyWithNoAuthority, null, null,
                     null, HumanResourceKey.ADMIN, null, null ) );
         return null;
      };
      List<Callable<Void>> events = Arrays.asList( eventOne, eventTwo );

      // ACT
      executor.invokeAll( events );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, scrappedAircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenAllAuthorityIsGrantedToHrWhoHasAuthorityThenShouldStillSeeAircraftWithNoAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( null, querySet.getKey( AuthorityKey.class,
            new String[] { AUTORITY_DB_ID_COL, AUTORITY_ID_COL } ) );
   }


   @Test
   public void
         whenAllAuthorityIsGrantedToHrWhoHasNoAuthorityThenShouldStillSeeAircraftWithNoAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( null, querySet.getKey( AuthorityKey.class,
            new String[] { AUTORITY_DB_ID_COL, AUTORITY_ID_COL } ) );
   }


   @Test
   public void whenAllAuthorityIsGrantedToHrWhoHasNoAuthorityThenShouldSeeAircraftWithAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( authorityKeyAirCanada, querySet.getKey( AuthorityKey.class,
            new String[] { AUTORITY_DB_ID_COL, AUTORITY_ID_COL } ) );
   }


   @Test
   public void
         whenAllAuthorityIsGrantedToHrWhoHasDifferentAuthorityThenShouldSeeAircraftWithAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( authorityKeyAirCanada, querySet.getKey( AuthorityKey.class,
            new String[] { AUTORITY_DB_ID_COL, AUTORITY_ID_COL } ) );
   }


   @Test
   public void
         whenAllAuthorityIsGrantedToHrWhoHasSameAuthorityThenShouldSeeAircraftWithAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( authorityKeyAirCanada, querySet.getKey( AuthorityKey.class,
            new String[] { AUTORITY_DB_ID_COL, AUTORITY_ID_COL } ) );
   }


   @Test
   public void whenAllAuthorityIsGrantedToHrWhoHasTheSameAuthorityThenNoDuplicateShouldBeCreated() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // Insert into MT_INV_AC_AUTHORITY_TABLE
      insertIntoMtInvAcAuthorityTable( aircraftKeyWithAirCanadaAuthority, humanResourceKey,
            authorityKeyAirCanada );

      assertEquals( 1,
            getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority ).getRowCount() );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( authorityKeyAirCanada, querySet.getKey( AuthorityKey.class,
            new String[] { AUTORITY_DB_ID_COL, AUTORITY_ID_COL } ) );
   }


   @Test
   public void
         whenAllAuthorityIsRevokedAndUserHasAuthorityThenHumanResourceSeeAircraftWithoutAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );

      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceRevokedAllAuthorityEvent( null, humanResourceKey, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenAllAuthorityIsRevokedAndUserHasNoAuthorityThenHumanResourceSeeAircraftWithoutAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );

      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceRevokedAllAuthorityEvent( null, humanResourceKey, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenAllAuthorityIsRevokedAndUserHasSameAuthorityAsAircraftThenHumanResourceSeeAircraftWithAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceRevokedAllAuthorityEvent( null, humanResourceKey, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenAllAuthorityIsRevokedAndUserHasDifferentAuthorityFromAircraftThenHumanResourceSeeNoAircraftWithAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceRevokedAllAuthorityEvent( null, humanResourceKey, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void
         whenAllAuthorityIsRevokedAndUserHasNoAuthorityThenHumanResourceSeeNoAircraftWithAuthority() {

      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
      } );

      // ACT
      fleetDueListAircraftProjection
            .on( new HumanResourceRevokedAllAuthorityEvent( null, humanResourceKey, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenHumanResourceIsCreatedThenHumanResourceShouldNotSeeAircraftWithAuthority() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );

      } );

      // ACT
      fleetDueListAircraftProjection.on(
            new HumanResourceCreatedEvent( humanResourceKey, null, null, null, null, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenHumanResourceIsCreatedThenHumanResourceShouldSeeAircraftWithoutAuthority() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );

      } );

      // ACT
      fleetDueListAircraftProjection.on(
            new HumanResourceCreatedEvent( humanResourceKey, null, null, null, null, null, null ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   private void insertIntoMtInvAcAuthorityTable( AircraftKey aircraftKey,
         HumanResourceKey humanResourceKey, AuthorityKey authorityKey ) {

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKey, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      if ( authorityKey != null ) {
         lArgs.add( authorityKey, new String[] { "authority_db_id", "authority_id" } );
      }
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );
   }


   @Test
   public void
         whenAssigningHumanResourceWithAuthorityThenHumanResourceMustSeeAircraftWithTheSameAuthority() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceAssignedAuthoritiesEvent(
            humanResourceKey, Arrays.asList( authorityKeyAirCanada ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenAssigningHumanResourceWithMultipleAuthoritiesThenHumanResourceMustSeeAircraftWithTheSameAuthorities() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceAssignedAuthoritiesEvent(
            humanResourceKey, Arrays.asList( authorityKeyAirCanada, authorityKeyWestJet ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
      querySet = getQueryResult( humanResourceKey, aircraftKeyWithWestJetAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void whenAssigningHumanResourceWithNewAuthorityTwiceThenNoDuplicateShouldBeCreated()
         throws InterruptedException {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      ExecutorService executor = Executors.newFixedThreadPool( 2 );
      Callable<Void> eventOne = () -> {
         fleetDueListAircraftProjection.on( new HumanResourceAssignedAuthoritiesEvent(
               humanResourceKey, Arrays.asList( authorityKeyWestJet ) ) );
         return null;
      };
      Callable<Void> eventTwo = () -> {
         fleetDueListAircraftProjection.on( new HumanResourceAssignedAuthoritiesEvent(
               humanResourceKey, Arrays.asList( authorityKeyWestJet ) ) );
         return null;
      };
      List<Callable<Void>> events = Arrays.asList( eventOne, eventTwo );

      // ACT
      executor.invokeAll( events );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithWestJetAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenHumanResourceHasAllAuthorityThenAssigningSpecificAuthorityShouldNotCreateNewRecord() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
         humanResource.addAuthority( authorityKeyWestJet );
      } );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceAssignedAuthoritiesEvent(
            humanResourceKey, Arrays.asList( authorityKeyWestJet ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithWestJetAuthority );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void
         whenHumanResourceIsDeletedThenAllHisRecordsInFleetDueListProjectionTableShouldBeDeleted() {
      // ARRANGE
      String userName = "spiderman";
      String firstName = "spider";
      String lastName = "man";
      String email = "spiderman@spider.zzz";

      UserKey userKey = Domain.createUser( user -> {
         user.setEmailId( email );
         user.setFirstName( firstName );
         user.setLastName( lastName );
         user.setUsername( userName );
      } );

      HumanResourceKey humanResourceKey = Domain.createHumanResource( hr -> {
         hr.setUser( userKey );
      } );

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithAirCanadaAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      lArgs.add( authorityKeyAirCanada, new String[] { "authority_db_id", "authority_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      assertEquals( 1,
            getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority ).getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceDeletedEvent( humanResourceKey, userName,
            firstName, lastName, null, email, null ) );

      // ASSERT
      assertEquals( 0,
            getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority ).getRowCount() );

   }


   @Test
   public void
         whenRevokeSpecificAuthorityFromHumanResourceThenHumanResourceShouldNotSeeAircraftWithThatSpecificAuthority() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithAirCanadaAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      lArgs.add( authorityKeyAirCanada, new String[] { "authority_db_id", "authority_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      QuerySet querySetInitial =
            getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySetInitial.getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceRevokedAuthoritiesEvent( humanResourceKey,
            Arrays.asList( authorityKeyAirCanada ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 0, querySet.getRowCount() );

   }


   @Test
   public void
         whenRevokeSpecificAuthorityFromHumanResourceWithAllAuthorityThenHumanResourceShouldStillSeeAllAircraft() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( true );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // Insert a row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithAirCanadaAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      lArgs.add( authorityKeyAirCanada, new String[] { "authority_db_id", "authority_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      QuerySet querySetInitial =
            getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySetInitial.getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceRevokedAuthoritiesEvent( humanResourceKey,
            Arrays.asList( authorityKeyAirCanada ) ) );

      // ASSERT
      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithAirCanadaAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenRevokeSpecificAuthorityFromHumanResourceThenHumanResourceShouldStillSeeAircraftWithNoAuthority() {
      // ARRANGE
      HumanResourceKey humanResourceKey = Domain.createHumanResource( humanResource -> {
         humanResource.setAllAuthority( false );
         humanResource.addAuthority( authorityKeyAirCanada );
      } );

      // Insert rows
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aircraftKeyWithNoAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      lArgs.clear();

      lArgs.add( aircraftKeyWithAirCanadaAuthority, new String[] { "inv_no_db_id", "inv_no_id" } );
      lArgs.add( humanResourceKey, new String[] { "hr_db_id", "hr_id" } );
      lArgs.add( authorityKeyAirCanada, new String[] { "authority_db_id", "authority_id" } );
      MxDataAccess.getInstance().executeInsert( MT_INV_AC_AUTHORITY_TABLE, lArgs );

      QuerySet querySetInitial = getQueryResult( humanResourceKey );
      assertEquals( 2, querySetInitial.getRowCount() );

      // ACT
      fleetDueListAircraftProjection.on( new HumanResourceRevokedAuthoritiesEvent( humanResourceKey,
            Arrays.asList( authorityKeyAirCanada ) ) );

      // ASSERT
      QuerySet querySetRow = getQueryResult( humanResourceKey );
      assertEquals( 1, querySetRow.getRowCount() );

      QuerySet querySet = getQueryResult( humanResourceKey, aircraftKeyWithNoAuthority );
      assertEquals( 1, querySet.getRowCount() );
   }


   private QuerySet getQueryResult( HumanResourceKey humanResourceKey, AircraftKey aircraftKey ) {
      DataSetArgument dataSetArguments = new DataSetArgument();
      dataSetArguments.add( humanResourceKey, HUMAN_RESOURCE_DB_ID_COL, HUMAN_RESOURCE_ID_COL );
      dataSetArguments.add( aircraftKey, AIRCRAFT_DB_ID_COL, AIRCRAFT_ID_COL );
      return QuerySetFactory.getInstance().executeQuery( MT_INV_AC_AUTHORITY_COLS,
            MT_INV_AC_AUTHORITY_TABLE, dataSetArguments );
   }


   private QuerySet getQueryResult( HumanResourceKey humanResourceKey ) {
      DataSetArgument dataSetArguments = new DataSetArgument();
      dataSetArguments.add( humanResourceKey, HUMAN_RESOURCE_DB_ID_COL, HUMAN_RESOURCE_ID_COL );
      return QuerySetFactory.getInstance().executeQuery( MT_INV_AC_AUTHORITY_COLS,
            MT_INV_AC_AUTHORITY_TABLE, dataSetArguments );
   }

}
