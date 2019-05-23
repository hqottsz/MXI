package com.mxi.mx.core.dao.workpackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.reservation.auto.AbstractAutoReservationTest.DemandLocationMode;


public class JdbcWorkPackageDaoTest {

   private static final Optional<Date> THREE_DAYS_BEFORE_MAX_DATE =
         Optional.of( new GregorianCalendar( 2018, Calendar.JANUARY, 1 ).getTime() );
   private static final Optional<Date> TWO_DAYS_BEFORE_MAX_DATE =
         Optional.of( new GregorianCalendar( 2018, Calendar.JANUARY, 2 ).getTime() );
   private static final Optional<Date> ONE_DAY_BEFORE_MAX_DATE =
         Optional.of( new GregorianCalendar( 2018, Calendar.JANUARY, 3 ).getTime() );
   private static final Optional<Date> MAX_DATE =
         Optional.of( new GregorianCalendar( 2018, Calendar.JANUARY, 4 ).getTime() );
   private static final Optional<Date> ONE_DAY_AFTER_MAX_DATE =
         Optional.of( new GregorianCalendar( 2018, Calendar.JANUARY, 5 ).getTime() );
   private static final RefEventStatusKey WP_COMMIT_STATUS = RefEventStatusKey.COMMIT;

   private static final RefEventStatusKey WP_IN_WORK_STATUS = RefEventStatusKey.IN_WORK;
   private static final RefEventStatusKey WP_ACTV_STATUS = RefEventStatusKey.ACTV;
   private static final RefEventStatusKey WP_COMPLETE_STATUS = RefEventStatusKey.COMPLETE;

   private LocationKey iAirportLocation;
   private LocationKey iLineLocationKey;
   private InventoryKey iAircraft;
   private List<LocationKey> iLocationList;
   private WorkPackageDao iJdbcWorkPackageDao;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      iJdbcWorkPackageDao = new JdbcWorkPackageDao();

      iAircraft = Domain.createAircraft();
      iAirportLocation = new LocationDomainBuilder().withCode( DemandLocationMode.AIRPORT.name() )
            .withType( RefLocTypeKey.AIRPORT ).build();
      iLineLocationKey = createLocation( RefLocTypeKey.LINE );
      iLocationList = Arrays.asList( iLineLocationKey );
   }


   @Test
   public void findExistingWorkPackages_noExistingWorkPackages() {
      List<TaskKey> lTaskKeys = iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft, MAX_DATE,
            iLocationList, WP_COMMIT_STATUS );
      assertTrue( lTaskKeys.isEmpty() );
   }


   @Test
   public void findExistingWorkPackages_workPackageDateAfterMaxDate() {
      createWorkPackage( iAircraft, iLineLocationKey, ONE_DAY_AFTER_MAX_DATE.get(),
            WP_COMMIT_STATUS );

      List<TaskKey> lTaskKeys = iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft, MAX_DATE,
            iLocationList, WP_COMMIT_STATUS );
      assertTrue( lTaskKeys.isEmpty() );
   }


   @Test( expected = NullPointerException.class )
   public void findExistingWorkPackage_nullAircraft() {
      iJdbcWorkPackageDao.findExistingWorkPackages( null, ONE_DAY_BEFORE_MAX_DATE, iLocationList,
            WP_COMMIT_STATUS );
   }


   @Test( expected = NullPointerException.class )
   public void findExistingWorkPackage_nullMaxDate() {
      iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft, null, iLocationList,
            WP_COMMIT_STATUS );
   }


   @Test( expected = NullPointerException.class )
   public void findExistingWorkPackage_nullLocations() {
      iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft, ONE_DAY_BEFORE_MAX_DATE, null,
            WP_COMMIT_STATUS );
   }


   @Test( expected = NullPointerException.class )
   public void findExistingWorkPackage_nullStatus() {
      iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft, ONE_DAY_BEFORE_MAX_DATE,
            iLocationList, null );
   }


   @Test
   public void findExistingWorkPackage_emptyMaxDate() {
      TaskKey lWorkPackage = createWorkPackage( iAircraft, iLineLocationKey,
            ONE_DAY_AFTER_MAX_DATE.get(), WP_COMMIT_STATUS );

      List<TaskKey> lTaskKeys = iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft,
            Optional.<Date>empty(), iLocationList, WP_COMMIT_STATUS );

      assertEquals( lTaskKeys.get( 0 ), lWorkPackage );

   }


   @Test( expected = NullPointerException.class )
   public void getNumInWorkPackagesByAircraft_nullAircraftKey() {
      iJdbcWorkPackageDao.getNumInWorkWorkPackagesByAircraft( null );
   }


   @Test
   public void getNumInWorkPackagesByAircraft_noWorkPackages() {
      AircraftKey lAircraftKeyNoWorkPackages = new AircraftKey( "1111:9999" );

      int lNumInWorkPackages =
            iJdbcWorkPackageDao.getNumInWorkWorkPackagesByAircraft( lAircraftKeyNoWorkPackages );

      assertEquals( lNumInWorkPackages, 0 );
   }


   @Test
   public void getNumInWorkPackageByAircraft_oneInWorkWorkPackage() {

      // Creates 3 work packages for aircraft inventory. 1 IN-WORK, 1 ACTV, and 1 COMPLETE
      createWorkPackage( iAircraft, null, new Date(), WP_IN_WORK_STATUS );
      createWorkPackage( iAircraft, null, new Date(), WP_ACTV_STATUS );
      createWorkPackage( iAircraft, null, new Date(), WP_COMPLETE_STATUS );

      AircraftKey lValidAircraftKey = new AircraftKey( iAircraft );

      int lNumInWorkPackages =
            iJdbcWorkPackageDao.getNumInWorkWorkPackagesByAircraft( lValidAircraftKey );

      assertEquals( lNumInWorkPackages, 1 );
   }


   @Test
   public void getNumInWorkPackageByAircraft_mulitpleInWorkWorkPackage() {

      // Creates 3 work packages for aircraft inventory. 2 IN-WORK, and 1 COMPLETE
      createWorkPackage( iAircraft, null, new Date(), WP_IN_WORK_STATUS );
      createWorkPackage( iAircraft, null, new Date(), WP_IN_WORK_STATUS );
      createWorkPackage( iAircraft, null, new Date(), WP_COMPLETE_STATUS );

      AircraftKey lValidAircraftKey = new AircraftKey( iAircraft );

      int lNumInWorkPackages =
            iJdbcWorkPackageDao.getNumInWorkWorkPackagesByAircraft( lValidAircraftKey );

      assertEquals( lNumInWorkPackages, 2 );
   }


   /**
    *
    * This test creates 4 work packages before executing the query. 2 work packages are valid but
    * have different scheduled start times. The other 2 work packages are invalid, one having an
    * invalid line location and the other having an invalid status. The query should retrieve the
    * valid work package with the earliest scheduled time
    *
    */
   @Test
   public void findExistingWorkPackages_multipleExistingWorkPackages() {

      TaskKey lCommittedWorkPackage1 = createWorkPackage( iAircraft, iLineLocationKey,
            ONE_DAY_BEFORE_MAX_DATE.get(), WP_COMMIT_STATUS );
      TaskKey lCommittedWorkPackage2 = createWorkPackage( iAircraft, iLineLocationKey,
            TWO_DAYS_BEFORE_MAX_DATE.get(), WP_COMMIT_STATUS );
      createWorkPackage( iAircraft, null, THREE_DAYS_BEFORE_MAX_DATE.get(), WP_COMMIT_STATUS );
      createWorkPackage( iAircraft, iLineLocationKey, THREE_DAYS_BEFORE_MAX_DATE.get(),
            RefEventStatusKey.ACTV );

      List<TaskKey> lTaskKeys = iJdbcWorkPackageDao.findExistingWorkPackages( iAircraft, MAX_DATE,
            iLocationList, WP_COMMIT_STATUS );

      assertEquals( lTaskKeys.get( 0 ), lCommittedWorkPackage2 );
      assertEquals( lTaskKeys.get( 1 ), lCommittedWorkPackage1 );

   }


   private LocationKey createLocation( RefLocTypeKey aLocType ) {
      return new LocationDomainBuilder().withParent( iAirportLocation ).withType( aLocType ).build();
   }


   private TaskKey createWorkPackage( InventoryKey aAircraft, LocationKey aLocation,
         Date aScheduledStartDate, RefEventStatusKey aStatus ) {

      TaskKey lWorkPackageKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setAircraft( aAircraft );
            aBuilder.setLocation( aLocation );
            aBuilder.setScheduledStartDate( aScheduledStartDate );
            aBuilder.setStatus( aStatus );
         }
      } );
      return lWorkPackageKey;
   }
}
