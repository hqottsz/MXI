package com.mxi.mx.core.services.stask;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createAircraftAssembly;
import static com.mxi.am.domain.Domain.createLocation;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.RefWorkTypeKey.TURN;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Integration unit test for {@linkplain WarningCheckCapability}
 *
 */
public class WarningCheckCapabilityTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String LOCATION_CODE = "LOCATION_CODE";


   /**
    * Verify that a warning is not generated when a location has the capability required for a work
    * package.
    *
    */
   @Test
   public void locationHasTheCapabilityRequiredByWorkPackage() throws Exception {

      // Given an aircraft based on an aircraft assembly.
      AssemblyKey aircraftAssembly = createAircraftAssembly();
      InventoryKey aircraft = createAircraft( acft -> acft.setAssembly( aircraftAssembly ) );

      // Given a work package against the aircraft
      // and the work package requires a work type capability.
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.setAircraft( aircraft );
         wp.addWorkType( TURN );
      } );

      // Given a location that is capable of performing the work type of the work package against
      // the assembly of the aircraft.
      LocationKey location = createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
         loc.addCapability( aircraftAssembly, TURN );
      } );

      // When the capability of the location is validated for the work package.
      WarningCheckCapability warning =
            new WarningCheckCapability( workPackage, workPackage, location );
      warning.validate();

      // Then no warning is generated.
      assertFalse( "Warning unexpectedly generated.", warning.isWarning() );
   }


   /**
    * Verify that a warning is generated when a location does not have the capability required for a
    * work package.
    *
    */
   @Test
   public void locationDoesNotHaveCapabilityRequiredByWorkPackage() throws Exception {

      // Given an aircraft based on an aircraft assembly.
      AssemblyKey aircraftAssembly = createAircraftAssembly();
      InventoryKey aircraft = createAircraft( acft -> acft.setAssembly( aircraftAssembly ) );

      // Given a work package against the aircraft
      // and the work package requires a work type capability.
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.setAircraft( aircraft );
         wp.addWorkType( TURN );
      } );

      // Given a location that is NOT capable of performing the work type of the work package
      // against the assembly of the aircraft.
      LocationKey location = createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
      } );

      // When the capability of the location is validated for the work package.
      WarningCheckCapability warning =
            new WarningCheckCapability( workPackage, workPackage, location );
      warning.validate();

      // Then a warning is generated.
      assertTrue( "Warning unexpectedly not generated.", warning.isWarning() );
      assertThat( "Warning message does not contain the expected location code.",
            warning.getWarningMessage(), containsString( LOCATION_CODE ) );
   }

}
