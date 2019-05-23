package com.mxi.mx.web.servlet.assembly.capabilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Aircraft.CapabilityLevel;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class contains tests for
 * {@link com.mxi.mx.web.servlet.assembly.capabilities.AssignCapabilities} class
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class AssignCapabilitiesTest {

   private static final CapabilityLevel ETOPS120 =
         new CapabilityLevel( 10, "ETOPS", 10, "ETOPS120", "", 10, "ETOPS120" );
   private static final CapabilityLevel ETOPS90 =
         new CapabilityLevel( 10, "ETOPS", 10, "ETOPS_90", "", 10, "ETOPS120" );
   private static final CapabilityLevel CATI =
         new CapabilityLevel( 10, "ALAND", 10, "CATI", "", 10, "CATII" );

   private AssignCapabilities iAssignCapabilities = new AssignCapabilities();
   private AssemblyKey iAssemblyKey = new AssemblyKey( "10:A320" );
   private InventoryKey iAcft1, iAcft2, iAcft3, iAcft4;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    *
    * <p>
    * Creates four aircrafts with "Capability:ConfiguredLevel:CurrentLvel" assigned as follows:
    * </p>
    *
    * Aircraft1 is set with "ETOPS:ETOPS120:ETOPS120"<br>
    * Aircraft2 is set with "ETOPS:ETOPS120:ETOPS_90"<br>
    * Aircraft3 is set with "ETOPS:ETOPS120:ETOPS_90"<br>
    * Aircraft4 is set with "ALAND:CATII:CATI"<br>
    *
    * @throws Exception
    */
   @Before
   public void setUp() {
      iAcft1 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( ETOPS120 );

         }
      } );

      iAcft2 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( ETOPS90 );
         }
      } );

      iAcft3 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( ETOPS90 );
         }
      } );

      iAcft4 = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAssemblyKey );
            aAircraft.addCapabilityLevel( CATI );
         }
      } );
   }


   /**
    * Verifies that when removing capabilities from assembly, the returned affected aircraft count
    * is correct.
    *
    * @throws MxException
    */
   @Test
   public void getAffectedAcftWhenRemovingCapabilitiesFromAssembly() throws MxException {
      // ARRANGE
      List<CapabilityKey> lRemovedCapabilities = new ArrayList<CapabilityKey>();
      lRemovedCapabilities.add( new CapabilityKey( "10:ETOPS" ) );

      // ACT
      Set<InventoryKey> lAffectedAcftCount = iAssignCapabilities
            .getAffectedAcftByRemovingCapabilities( iAssemblyKey, lRemovedCapabilities );

      // ASSERT
      assertEquals( "The affected aircraft count is incorrect", 3, lAffectedAcftCount.size() );
      assertTrue( lAffectedAcftCount.contains( iAcft1 ) );
      assertTrue( lAffectedAcftCount.contains( iAcft2 ) );
      assertTrue( lAffectedAcftCount.contains( iAcft3 ) );
      assertFalse( lAffectedAcftCount.contains( iAcft4 ) );
   }


   /**
    * Verifies that when removing capability levels from assembly, the returned affected aircraft
    * count is correct.
    *
    * @throws MxException
    */
   @Test
   public void getAffectedAcftWhenRemovingLevelsFromAssembly() throws MxException {
      // ARRANGE
      List<CapabilityLevelKey> lRemovedLevels = new ArrayList<CapabilityLevelKey>();
      lRemovedLevels.add( new CapabilityLevelKey( "10:ETOPS_90:10:ETOPS" ) );

      // ACT
      Set<InventoryKey> lAffectedAcftCount = iAssignCapabilities
            .getAffectedAcftCountByRemovingLevels( iAssemblyKey, lRemovedLevels );

      // ASSERT
      assertEquals( "The affected aircraft count is incorrect", 2, lAffectedAcftCount.size() );
      assertTrue( lAffectedAcftCount.contains( iAcft2 ) );
      assertTrue( lAffectedAcftCount.contains( iAcft3 ) );
      assertFalse( lAffectedAcftCount.contains( iAcft1 ) );
      assertFalse( lAffectedAcftCount.contains( iAcft4 ) );
   }

}
