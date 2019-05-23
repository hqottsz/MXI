package com.mxi.mx.core.services.assembly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Aircraft.CapabilityLevel;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Test Class to test get the list of aircraft affected by assembly capabilities or levels removal
 *
 */
public class GetAircraftAffectedByAssemblyCapabilityOrLevelChangeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private AssemblyService iAssemblyService;

   private AssemblyKey iAssemblyKey = new AssemblyKey( "10:A320" );

   private CapabilityLevel iCapabilityLevel1 =
         new CapabilityLevel( 10, "ETOPS", 10, "ETOPS120", "", 10, "ETOPS120" );
   private CapabilityLevel iCapabilityLevel2 =
         new CapabilityLevel( 10, "ETOPS", 10, "ETOPS120", "", 10, "ETOPS_90" );
   private CapabilityLevel iCapabilityLevel3 =
         new CapabilityLevel( 10, "ALAND", 10, "CATII", "", 10, "CATI" );
   private CapabilityLevel iCapabilityLevel4 =
         new CapabilityLevel( 10, "ALAND", 10, "CATII", "", 10, "CATII" );

   private InventoryKey iAircraft1, iAircraft2, iAircraft3;


   @Before
   public void setUp() {
      iAssemblyService = new AssemblyService();
   }


   /**
    *
    * <p>
    * Creates three aircrafts with "Capability:ConfiguredLevel:CurrentLvel" assigned:
    * </p>
    *
    * Aircraft1 is set with "ETOPS:ETOPS120:ETOPS120" and "ALAND:CATII:CATI"<br>
    * Aircraft2 is set with "ETOPS:ETOPS120:ETOPS_90" and "ALAND:CATII:CATII"<br>
    * Aircraft3 is set with "ETOPS:ETOPS120:ETOPS_90"<br>
    *
    * @throws Exception
    */
   private void buildAircraft() throws Exception {
      iAircraft1 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( iCapabilityLevel1 );
            aAircraft.addCapabilityLevel( iCapabilityLevel3 );

         }
      } );

      iAircraft2 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( iCapabilityLevel2 );
            aAircraft.addCapabilityLevel( iCapabilityLevel4 );
         }
      } );

      iAircraft3 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( iCapabilityLevel2 );
         }
      } );
   }


   /**
    *
    * Tests to see if the proper aircraft list is returned by removing a capability from assembly
    *
    * @throws Exception
    */
   @Test
   public void itReturnsAircraftAffectedByAssemblyCapabilityRemoval() throws Exception {
      buildAircraft();

      // get a list of aircraft affected by removing ETOPS capability from assembly
      List<CapabilityKey> lRemovedCapabilities = new ArrayList<CapabilityKey>();
      lRemovedCapabilities.add( new CapabilityKey( "10:ETOPS" ) );

      Set<InventoryKey> lAffectedAircraft1 =
            iAssemblyService.getAircraftAffectedByRemovalOfCapabilityOrLevel( iAssemblyKey,
                  lRemovedCapabilities, new ArrayList<CapabilityLevelKey>() );

      assertEquals( "Number of Affected Aircraft is incorrect", 3, lAffectedAircraft1.size() );
      assertTrue( lAffectedAircraft1.contains( iAircraft1 ) );
      assertTrue( lAffectedAircraft1.contains( iAircraft2 ) );
      assertTrue( lAffectedAircraft1.contains( iAircraft3 ) );

      // get a list of aircraft affected by removing ALAND capability from assembly
      lRemovedCapabilities.clear();
      lRemovedCapabilities.add( new CapabilityKey( "10:ALAND" ) );
      Set<InventoryKey> lAffectedAircraft2 =
            iAssemblyService.getAircraftAffectedByRemovalOfCapabilityOrLevel( iAssemblyKey,
                  lRemovedCapabilities, new ArrayList<CapabilityLevelKey>() );

      assertEquals( "Number of Affected Aircraft is incorrect", 2, lAffectedAircraft2.size() );
      assertTrue( lAffectedAircraft2.contains( iAircraft1 ) );
      assertTrue( lAffectedAircraft2.contains( iAircraft2 ) );
   }


   /**
    *
    * Tests to see if an empty aircraft list is returned by removing RVSM capability from assembly
    * which is not used by any aircraft
    *
    * @throws Exception
    */
   @Test
   public void itReturnsNoAircraftAffectedByAssemblyCapabilityRemoval() throws Exception {
      buildAircraft();

      // get an empty list of affected aircraft by removing capability ETOPS from assembly
      List<CapabilityKey> lRemovedCapabilities = new ArrayList<CapabilityKey>();
      lRemovedCapabilities.add( new CapabilityKey( "10:RVSM" ) );

      Set<InventoryKey> lAffectedAircraft =
            iAssemblyService.getAircraftAffectedByRemovalOfCapabilityOrLevel( iAssemblyKey,
                  lRemovedCapabilities, new ArrayList<CapabilityLevelKey>() );

      assertEquals( "Number of Affected Aircraft is incorrect", 0, lAffectedAircraft.size() );
   }


   /**
    *
    * Tests to see if the proper aircraft list is returned by removing a capability level from
    * assembly
    *
    * @throws Exception
    */
   @Test
   public void itReturnsAircraftAffectedByAssemblyCapabilityLevelRemoval() throws Exception {
      buildAircraft();

      // get a list of aircraft affected by removing ETOPS_90 capability level from assembly
      List<CapabilityLevelKey> lRemovedCapabilityLevels = new ArrayList<CapabilityLevelKey>();
      lRemovedCapabilityLevels.add( new CapabilityLevelKey( "10:ETOPS_90:10:ETOPS" ) );

      Set<InventoryKey> lAffectedAircraft1 =
            iAssemblyService.getAircraftAffectedByRemovalOfCapabilityOrLevel( iAssemblyKey,
                  new ArrayList<CapabilityKey>(), lRemovedCapabilityLevels );

      assertEquals( "Number of Affected Aircraft is incorrect", 2, lAffectedAircraft1.size() );
      assertTrue( lAffectedAircraft1.contains( iAircraft2 ) );
      assertTrue( lAffectedAircraft1.contains( iAircraft3 ) );

      // get a list of aircraft affected by removing ALAND capability
      lRemovedCapabilityLevels.clear();
      lRemovedCapabilityLevels.add( new CapabilityLevelKey( "10:CATI:10:ALAND" ) );
      Set<InventoryKey> lAffectedAircraft2 =
            iAssemblyService.getAircraftAffectedByRemovalOfCapabilityOrLevel( iAssemblyKey,
                  new ArrayList<CapabilityKey>(), lRemovedCapabilityLevels );

      assertEquals( "Number of Affected Aircraft is incorrect", 1, lAffectedAircraft2.size() );
      assertTrue( lAffectedAircraft2.contains( iAircraft1 ) );
   }


   /**
    *
    * Tests to see if the proper aircraft list is returned by removing a capability level from
    * assembly which is not in use by any aircrafts
    *
    * @throws Exception
    */
   @Test
   public void itReturnsNoAircraftAffectedByAssemblyCapabilityLevelRemoval() throws Exception {
      buildAircraft();

      // get a list of aircraft affected by removing ETOPS_90 capability level from assembly
      List<CapabilityLevelKey> lRemovedCapabilityLevels = new ArrayList<CapabilityLevelKey>();
      lRemovedCapabilityLevels.add( new CapabilityLevelKey( "10:NO_ETOPS:10:ETOPS" ) );

      Set<InventoryKey> lAffectedAircraft1 =
            iAssemblyService.getAircraftAffectedByRemovalOfCapabilityOrLevel( iAssemblyKey,
                  new ArrayList<CapabilityKey>(), lRemovedCapabilityLevels );

      assertEquals( "Number of Affected Aircraft is incorrect", 0, lAffectedAircraft1.size() );
   }
}
