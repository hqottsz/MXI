package com.mxi.mx.core.dao.flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.flight.dao.FlightLegEntity;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.InventoryKey;


public class JdbcFlightDaoTest {

   private static final InventoryKey AIRCRAFT_WITH_CURRENT_FLIGHT_AND_FUTURE_FLIGHTS =
         new InventoryKey( "4650:111222" );
   private static final InventoryKey AIRCRAFT_WITH_INVALID_FLIGHT =
         new InventoryKey( "4650:111333" );

   private static final FlightLegId CURRENT_FLIGHT_LEG_ID =
         new FlightLegId( "6D859FB370A842B5A4250EA2363782C2" );

   private static final FlightLegId NEXT_FLIGHT_LEG_ID =
         new FlightLegId( "6D859FB370A842B5A4250EA2363782C3" );

   private FlightDao iJdbcFlightDao;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iJdbcFlightDao = new JdbcFlightDao();
   }


   @Test( expected = NullPointerException.class )
   public void findCurrentFlightByAircraft_nullAircraftKey() {
      iJdbcFlightDao.findCurrentFlightByAircraft( null );
   }


   @Test
   public void findCurrentFlightByAircraft_flightTimesInvalid() {
      Optional<FlightLegEntity> lFlightLegEntity =
            iJdbcFlightDao.findCurrentFlightByAircraft( AIRCRAFT_WITH_INVALID_FLIGHT );
      assertFalse( lFlightLegEntity.isPresent() );
   }


   @Test
   public void findCurrentFlightByAircraft_flightTimesValid() {
      Optional<FlightLegEntity> lFlightLegEntity = iJdbcFlightDao
            .findCurrentFlightByAircraft( AIRCRAFT_WITH_CURRENT_FLIGHT_AND_FUTURE_FLIGHTS );

      assertTrue( lFlightLegEntity.isPresent() );
      assertEquals( CURRENT_FLIGHT_LEG_ID, lFlightLegEntity.get().getId() );
   }


   @Test( expected = NullPointerException.class )
   public void findNextFlightByAircraft_nullAircraftKey() {
      iJdbcFlightDao.findNextFlightByAircraft( null );
   }


   @Test
   public void findNextFlightByAircraft_noNextFlight() {
      Optional<FlightLegEntity> lFlightLegEntity =
            iJdbcFlightDao.findNextFlightByAircraft( AIRCRAFT_WITH_INVALID_FLIGHT );
      assertFalse( lFlightLegEntity.isPresent() );
   }


   @Test
   public void findNextFlightByAircraft_multipleFutureFlights() {
      Optional<FlightLegEntity> lFlightLegEntity = iJdbcFlightDao
            .findNextFlightByAircraft( AIRCRAFT_WITH_CURRENT_FLIGHT_AND_FUTURE_FLIGHTS );
      assertTrue( lFlightLegEntity.isPresent() );
      assertEquals( NEXT_FLIGHT_LEG_ID, lFlightLegEntity.get().getId() );
   }

}
