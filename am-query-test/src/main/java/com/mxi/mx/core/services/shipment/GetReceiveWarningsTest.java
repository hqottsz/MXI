package com.mxi.mx.core.services.shipment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.shipment.message.MessageReceivedPart;


/**
 * This class tests the get receive warning method
 *
 */
public final class GetReceiveWarningsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final String INV_SERIAL_NO = "SerialNo";

   public static final RefEngUnitKey SHELF_LIFE_UNIT = new RefEngUnitKey( 0, "MONTH" );

   public static final Date MANUFACTURER_DATE = new Date();

   public static final Date SHELF_LIFE_EXPIRY = DateUtils.addDays( MANUFACTURER_DATE, 60 );


   @Test
   public void testGetReceiveWarnings_ShelfLife() throws Exception {

      // DATA SETUP: Create a human resource
      HumanResourceKey lHr = Domain.createHumanResource();

      // DATA SETUP: Create location
      LocationKey lToLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      LocationKey lInvLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      // DATA SETUP: Create a serialized part
      PartNoKey lPartNo = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      // DATA SETUP: Create an inventory
      InventoryKey lInventory = Domain.createSerializedInventory( aSerializedInventory -> {
         aSerializedInventory.setLocation( lInvLocation );
         aSerializedInventory.setPartNumber( lPartNo );
         aSerializedInventory.setSerialNumber( INV_SERIAL_NO );
      } );

      // DATA SETUP: Create a shipment
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( lInvLocation )
            .toLocation( lToLocation ).withType( RefShipmentTypeKey.STKTRN )
            .withStatus( RefEventStatusKey.IXINTR ).build();

      ShipmentLineKey lShipmentLine = new ShipmentLineBuilder( lShipment ).forPart( lPartNo )
            .forInventory( lInventory ).withExpectedQuantity( 1.0 ).build();

      // DATA SETUP: Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setSerialNo( INV_SERIAL_NO );
      lReceiveShipmentLineTO.setManufacturedDate( MANUFACTURER_DATE );
      lReceiveShipmentLineTO.setShelfExpiry( SHELF_LIFE_EXPIRY );
      lReceiveShipmentLineTO.setShelfLife( 1 );
      lReceiveShipmentLineTO.setShelfLifeUnit( SHELF_LIFE_UNIT.getCd() );
      lReceiveShipmentLineTO.setReceivedQty( 1.0 );
      lReceiveShipmentLineTO.setLineNo( 1 );

      // *********************************************************************************
      // Test case 1:
      // ALLOW_BAD_MANUFACT_AND_SHELF_LIFE_EXPIRY_DATES = false
      // SHELF_LIFE_REMAINING_VALIDATED_FOR_INTERNAL_SHIPMENT = false
      // Result:
      // An exception is thrown
      // *********************************************************************************

      // DATA SETUP: Update config parms
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( "ALLOW_BAD_MANUFACT_AND_SHELF_LIFE_EXPIRY_DATES", false );
      lConfigParms.setBoolean( "SHELF_LIFE_REMAINING_VALIDATED_FOR_INTERNAL_SHIPMENT", false );
      GlobalParameters.setInstance( lConfigParms );

      // Asserts that an exception is thrown
      try {
         ShipmentService.getReceiveWarnings( lShipment,
               new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, lHr );

         fail( "Expected InvalidShelfLifeExpiryDateException to be thrown." );

      } catch ( InvalidShelfLifeExpiryDateException e ) {
         ;
      }

      // *********************************************************************************
      // Test case 2:
      // ALLOW_BAD_MANUFACT_AND_SHELF_LIFE_EXPIRY_DATES = false
      // SHELF_LIFE_REMAINING_VALIDATED_FOR_INTERNAL_SHIPMENT = true
      // Result:
      // An warning is returned
      // *********************************************************************************

      // DATA SETUP: Update config parms
      lConfigParms.setBoolean( "SHELF_LIFE_REMAINING_VALIDATED_FOR_INTERNAL_SHIPMENT", true );
      GlobalParameters.setInstance( lConfigParms );

      MessageReceivedPart[] lMessageReceivedPart = ShipmentService.getReceiveWarnings( lShipment,
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, lHr );

      // Asserts that there's one warning
      assertEquals( 1, lMessageReceivedPart.length );

      assertEquals( lMessageReceivedPart[0].getMsgTitleKey(), "core.msg.SHELF_LIFE_EXPIRED_title" );

      // *********************************************************************************
      // Test case 3:
      // ALLOW_BAD_MANUFACT_AND_SHELF_LIFE_EXPIRY_DATES = true
      // Result:
      // No error or warning
      // *********************************************************************************

      // DATA SETUP: Update config parms
      lConfigParms.setBoolean( "ALLOW_BAD_MANUFACT_AND_SHELF_LIFE_EXPIRY_DATES", true );
      GlobalParameters.setInstance( lConfigParms );

      lMessageReceivedPart = ShipmentService.getReceiveWarnings( lShipment,
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, lHr );

      // Asserts that there's no warning
      assertEquals( 0, lMessageReceivedPart.length );
   }

}
