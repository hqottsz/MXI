package com.mxi.mx.core.adapter.flight.logic.flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Aircraft.CapabilityLevel;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftcapabilities.AircraftCapabilitiesCoordinator10;
import com.mxi.mx.core.adapter.flight.model.AircraftIdentifier;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft.Capabilities;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft.Capabilities.Capability;


/**
 * Test Class to test the Aircraft Capabilities Coordinator returns the correct response message
 *
 */
public class AircraftCapabilitiescoordinator10Test {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private ManufacturerKey iManufacturerKey = new ManufacturerKey( 5000000, "ABI" );
   private PartNoKey iPart;
   private InventoryKey iInventoryKey1 = null;
   private InventoryKey iInventoryKey2 = null;
   private AircraftCapabilitiesCoordinator10 iCoordinator = new AircraftCapabilitiesCoordinator10();

   private CapabilityLevel iCapabilityLevel1 =
         new CapabilityLevel( 10, "RVSM", 10, "NO", "", 10, "YES" );
   private CapabilityLevel iCapabilityLevel2 =
         new CapabilityLevel( 10, "SEATNUM", 10, "", "121", 10, "137" );

   private CapabilityLevel iCapabilityLevel3 =
         new CapabilityLevel( 10, "ETOPS", 10, "ETOPS_90", "", 10, "ETOPS120" );
   List<CapabilityLevel> iCapabilityLevels = new ArrayList<CapabilityLevel>();


   /**
    *
    * Insert Data into Db using Domain Objects
    *
    * @throws Exception
    */
   @Before
   public void insertDataToDb() throws Exception {
      iPart = new PartNoBuilder().withOemPartNo( "A319/A320" ).manufacturedBy( iManufacturerKey )
            .build();
      iCapabilityLevels.add( iCapabilityLevel1 );
      iCapabilityLevels.add( iCapabilityLevel2 );
      iInventoryKey1 = createAircraftWithCapabilities( iCapabilityLevels );
      iCapabilityLevels.clear();
      iCapabilityLevels.add( iCapabilityLevel3 );
      iInventoryKey2 = createAircraftWithCapabilities( iCapabilityLevels );
   }


   /**
    *
    * Creates an aircraft with capabilities assigned
    *
    * @param aCapabilityLevels
    *           Capabilities to be added to the aircraft
    * @return
    */
   private InventoryKey
         createAircraftWithCapabilities( java.util.List<CapabilityLevel> aCapabilityLevels ) {
      // create aircraft with capability level
      InventoryKey lAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setPart( iPart );
            for ( CapabilityLevel lCapabilityLevel : iCapabilityLevels ) {
               aAircraft.addCapabilityLevel( lCapabilityLevel );
            }
         }
      } );
      return lAircraftInvKey;
   }


   /**
    *
    * Tests to see if the proper capabilities are returned for a specific aircraft
    *
    * @throws Exception
    */
   @Test
   public void itReturnTheAircraftCapabilitiesAgainstAircraft() throws Exception {
      // ARRANGE
      AircraftKey lAircraftKey = new AircraftKey( iInventoryKey1 );
      AircraftIdentifier lAircraftIdentifier =
            AircraftIdentifier.buildFromInternalIdentifier( lAircraftKey );
      // ACT
      AircraftCapabilitiesResponseDocument lAircraftCapabilitiesResponseDocument =
            iCoordinator.coordinate( lAircraftIdentifier );

      // ASSERT
      // ensure the document passes schema validation
      lAircraftCapabilitiesResponseDocument.validate();

      // validate aircraft identifier
      com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft lAircraftXml =
            lAircraftCapabilitiesResponseDocument.getAircraftCapabilitiesResponse()
                  .getAircraftArray( 0 );
      com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft.AircraftIdentifier lAircraftIdentifierXml =
            lAircraftXml.getAircraftIdentifier();
      String lInternalIdentifierXML = lAircraftIdentifierXml.getInternalIdentifier();

      assertEquals( "Incorrect Aircraft Internal Identifier", lAircraftKey.toValueString(),
            lInternalIdentifierXML );

      // ensure one capability exists for the aircraft
      Capabilities lCapabilities = lAircraftXml.getCapabilities();

      assertTrue( "Number of capabilities", lCapabilities.sizeOfCapabilityArray() == 2 );

      // validate the capability content
      Capability lCapability1Xml = lCapabilities.getCapabilityArray()[0];
      assertEquals( "Capability code", iCapabilityLevel1.getCapCd(),
            lCapability1Xml.getCapabilityCode() );
      assertEquals( "Configured level code", iCapabilityLevel1.getConfigLevelCd(),
            lCapability1Xml.getConfiguredLevelCode() );
      assertEquals( "Current level code", iCapabilityLevel1.getLevelCd(),
            lCapability1Xml.getCurrentLevelCode() );

      Capability lCapability2Xml = lCapabilities.getCapabilityArray()[1];
      assertEquals( "Capability code", iCapabilityLevel2.getCapCd(),
            lCapability2Xml.getCapabilityCode() );
      assertEquals( "Configured level code", iCapabilityLevel2.getConfigLevelCd(),
            lCapability2Xml.getConfiguredLevelCode() );
      assertEquals( "Current level code", iCapabilityLevel2.getCustomLevel(),
            lCapability2Xml.getCurrentLevelCode() );
   }


   /**
    *
    * Tests to see if the proper capabilities are returned for a fleet
    *
    * @throws Exception
    */
   @Test
   public void itReturnFleetCapabilitiesAgainstAircraft() throws Exception {

      // ACT
      AircraftCapabilitiesResponseDocument lAircraftCapabilitiesResponseDocument =
            iCoordinator.coordinate( null );

      // ASSERT
      // ensure the document passes schema validation
      lAircraftCapabilitiesResponseDocument.validate();

      // validate aircraft identifier of iInventoryKey1
      com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft lAircraftXml1 =
            lAircraftCapabilitiesResponseDocument.getAircraftCapabilitiesResponse()
                  .getAircraftArray( 0 );
      com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft.AircraftIdentifier lAircraftIdentifierXml1 =
            lAircraftXml1.getAircraftIdentifier();
      String lInternalIdentifierXML1 = lAircraftIdentifierXml1.getInternalIdentifier();

      assertEquals( "Incorrect Aircraft Internal Identifier", iInventoryKey1.toValueString(),
            lInternalIdentifierXML1 );

      // ensure one capability exists for the aircraft
      Capabilities lCapabilities = lAircraftXml1.getCapabilities();

      assertTrue( "Number of capabilities", lCapabilities.sizeOfCapabilityArray() == 2 );

      // validate the capability content of iInventoryKey1
      Capability lCapability1Xml = lCapabilities.getCapabilityArray()[0];
      assertEquals( "Capability code", iCapabilityLevel1.getCapCd(),
            lCapability1Xml.getCapabilityCode() );
      assertEquals( "Configured level code", iCapabilityLevel1.getConfigLevelCd(),
            lCapability1Xml.getConfiguredLevelCode() );
      assertEquals( "Current level code", iCapabilityLevel1.getLevelCd(),
            lCapability1Xml.getCurrentLevelCode() );

      Capability lCapability2Xml = lCapabilities.getCapabilityArray()[1];
      assertEquals( "Capability code", iCapabilityLevel2.getCapCd(),
            lCapability2Xml.getCapabilityCode() );
      assertEquals( "Configured level code", iCapabilityLevel2.getConfigLevelCd(),
            lCapability2Xml.getConfiguredLevelCode() );
      assertEquals( "Current level code", iCapabilityLevel2.getCustomLevel(),
            lCapability2Xml.getCurrentLevelCode() );

      // validate aircraft identifier of iInventoryKey2
      com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft lAircraftXml2 =
            lAircraftCapabilitiesResponseDocument.getAircraftCapabilitiesResponse()
                  .getAircraftArray( 1 );
      com.mxi.xml.xsd.core.flight.aircraftCapabilitiesResponse.x10.AircraftCapabilitiesResponseDocument.AircraftCapabilitiesResponse.Aircraft.AircraftIdentifier lAircraftIdentifierXml2 =
            lAircraftXml2.getAircraftIdentifier();
      String lInternalIdentifierXML2 = lAircraftIdentifierXml2.getInternalIdentifier();

      assertEquals( "Incorrect Aircraft Internal Identifier", iInventoryKey2.toValueString(),
            lInternalIdentifierXML2 );

      // validate the capability content of iInventoryKey2
      lCapabilities = lAircraftXml2.getCapabilities();
      Capability lCapabilityXml2 = lCapabilities.getCapabilityArray()[0];
      assertEquals( "Capability code", iCapabilityLevel3.getCapCd(),
            lCapabilityXml2.getCapabilityCode() );
      assertEquals( "Configured level code", iCapabilityLevel3.getConfigLevelCd(),
            lCapabilityXml2.getConfiguredLevelCode() );
      assertEquals( "Current level code", iCapabilityLevel3.getLevelCd(),
            lCapabilityXml2.getCurrentLevelCode() );

   }

}
