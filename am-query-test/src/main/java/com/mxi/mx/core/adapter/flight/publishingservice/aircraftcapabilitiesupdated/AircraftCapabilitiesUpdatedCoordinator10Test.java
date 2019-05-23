package com.mxi.mx.core.adapter.flight.publishingservice.aircraftcapabilitiesupdated;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftcapabilitiesupdated.AircraftCapabilitiesUpdatedCoordinator10;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.AircraftCapabilitiesDocument;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.AircraftCapabilitiesDocument.AircraftCapabilities.Aircraft;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.AircraftCapabilitiesDocument.AircraftCapabilities.Aircraft.Capabilities;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.AircraftCapabilitiesDocument.AircraftCapabilities.Aircraft.Capabilities.Capability;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.MxAircraftIdentifier;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.MxAircraftIdentifier.InventoryIdentifier;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.MxPartNumberIdentifier;
import com.mxi.xml.xsd.core.flight.aircraftCapabilitiesUpdatedEvent.x10.MxPartNumberIdentifier.ManufacturerId;


/**
 * Test class for aircraft capabilities updated coordinate to build a message
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class AircraftCapabilitiesUpdatedCoordinator10Test {

   private AircraftCapabilitiesUpdatedCoordinator10 iCoordinator =
         new AircraftCapabilitiesUpdatedCoordinator10();

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            AircraftCapabilitiesUpdatedCoordinator10Test.class );
   }


   /**
    * Test Case: Builds message for aircraft with capabilities.
    */
   @Test
   public void testBuildMessageForAircraftWithCapabilities() {

      // ARRANGE
      InventoryKey lAircraftKey = new InventoryKey( 4650, 300785 );

      // ACT
      AircraftCapabilitiesDocument lAircraftCapabilitiesDocument =
            iCoordinator.coordinate( lAircraftKey );

      // ASSERT
      // ensure the document passes schema validation
      lAircraftCapabilitiesDocument.validate();

      // validate aircraft identifier
      Aircraft lAircraft = lAircraftCapabilitiesDocument.getAircraftCapabilities().getAircraft();
      MxAircraftIdentifier lAircraftIdentifier = lAircraft.getAircraftIdentifier();
      InventoryIdentifier lInvIdentifier = lAircraftIdentifier.getInventoryIdentifier();
      MxPartNumberIdentifier lPartNumberIdentifier = lInvIdentifier.getPartNumberIdentifier();
      ManufacturerId lManufacturerId = lPartNumberIdentifier.getManufacturerId();

      assertEquals( "Incorrect Aircraft Barcode", "I000ADYB", lAircraftIdentifier.getBarcode() );
      assertEquals( "Incorrect Aircraft Registration Code", "TEST1",
            lAircraftIdentifier.getAircraftRegistrationCode() );
      assertEquals( "Incorrect Aircraft Internal Identifier", "4650:300785",
            lAircraftIdentifier.getInternalIdentifier() );
      assertEquals( "Incorrect OEM Serial Number", "Serial1", lInvIdentifier.getOemSerialNumber() );
      assertEquals( "Incorrect OEM Part Number", "A319/A320",
            lPartNumberIdentifier.getOemPartNumber() );
      assertEquals( "Incorrect Manufacture Code", "ABI", lManufacturerId.getManufacturerCode() );
      assertEquals( "Incorrect Manufacturer Name", "Airbus Industrie",
            lManufacturerId.getManufacturerName() );

      // ensure one capability exists for the aircraft
      Capabilities lCapabilities = lAircraft.getCapabilities();

      assertTrue( "Number of capabilities", lCapabilities.sizeOfCapabilityArray() == 1 );

      // validate the capability content
      Capability lCapability = lCapabilities.getCapabilityArray()[0];

      assertEquals( "Capability code", "ETOPS", lCapability.getCapabilityCode() );
      assertEquals( "Capability description", "Extended Operations",
            lCapability.getCapabilityDescription() );
      assertEquals( "Configured level code", "ETOPS_90", lCapability.getConfiguredLevelCode() );
      assertEquals( "Current level code", "NO_ETOPS", lCapability.getCurrentLevelCode() );
   }


   /**
    * Test Case: Builds message for aircraft without capabilities.
    */
   @Test
   public void testBuildMessageForAircraftWithoutCapabilities() {

      // ARRANGE
      InventoryKey lAircraftKey = new InventoryKey( 4650, 300786 );

      // ACT
      AircraftCapabilitiesDocument lAircraftCapabilitiesDocument =
            iCoordinator.coordinate( lAircraftKey );

      // ASSERT
      // ensure the document passes schema validation
      lAircraftCapabilitiesDocument.validate();

      // validate aircraft identifier
      Aircraft lAircraft = lAircraftCapabilitiesDocument.getAircraftCapabilities().getAircraft();
      MxAircraftIdentifier lAircraftIdentifier = lAircraft.getAircraftIdentifier();
      InventoryIdentifier lInvIdentifier = lAircraftIdentifier.getInventoryIdentifier();
      MxPartNumberIdentifier lPartNumberIdentifier = lInvIdentifier.getPartNumberIdentifier();
      ManufacturerId lManufacturerId = lPartNumberIdentifier.getManufacturerId();

      assertEquals( "Incorrect Aircraft Barcode", "I000ADYC", lAircraftIdentifier.getBarcode() );
      assertEquals( "Incorrect Aircraft Registration Code", "TEST2",
            lAircraftIdentifier.getAircraftRegistrationCode() );
      assertEquals( "Incorrect Aircraft Internal Identifier", "4650:300786",
            lAircraftIdentifier.getInternalIdentifier() );
      assertEquals( "Incorrect OEM Serial Number", "Serial2", lInvIdentifier.getOemSerialNumber() );
      assertEquals( "Incorrect OEM Part Number", "A319/A320",
            lPartNumberIdentifier.getOemPartNumber() );
      assertEquals( "Incorrect Manufacture Code", "ABI", lManufacturerId.getManufacturerCode() );
      assertEquals( "Incorrect Manufacturer Name", "Airbus Industrie",
            lManufacturerId.getManufacturerName() );

      // ensure one capability exists for the aircraft
      Capabilities lCapabilities =
            lAircraftCapabilitiesDocument.getAircraftCapabilities().getAircraft().getCapabilities();

      assertTrue( "Number of capabilities", lCapabilities.sizeOfCapabilityArray() == 0 );
   }

}
