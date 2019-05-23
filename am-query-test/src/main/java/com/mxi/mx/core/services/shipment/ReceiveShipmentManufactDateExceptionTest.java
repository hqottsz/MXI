package com.mxi.mx.core.services.shipment;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.org.OrgHr;


public class ReceiveShipmentManufactDateExceptionTest {

   private static final String PART_NO_OEM = "TEST_PART";
   private static final String SERIAL_NO_OEM = "ABC";
   private static final String INVASSET_ACCOUNT = "INVASSET";

   private HumanResourceKey iHr;
   private int iUserId;

   private LocationKey iVendorLocation;
   private LocationKey iDockLocation;
   private OwnerKey iOwner;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() throws Exception {
      // create a human resource
      iHr = Domain.createHumanResource();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      iUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();
      UserParametersStub lUserParametersStub =
            new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      lUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            false );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", lUserParametersStub );

      new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).withCode( INVASSET_ACCOUNT )
            .isDefault().build();

      // create default owner
      iOwner = Domain.createOwner();

      // create a default receipt location
      new InvOwnerTable( iOwner ).getOrgKey();

      iVendorLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.VENDOR );
         aLocationConfiguration.setCode( "TESTVENLOC" );
      } );

      // Create a vendor
      new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();

      // create a supply location
      LocationKey lSupplyLocation = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );

      // create a dock at the supply location
      iDockLocation = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setSupplyLocation( lSupplyLocation );
         loc.setIsDefaultDock( true );
      } );

   }


   @After
   public void tearDown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", null );
   }


   /**
    *
    * Test receiving a shipment with a part and serial number already existing in the system, where
    * the Manufactured date entered on the shipment is empty, the part has no Manufactured Date, the
    * user has permission to change manufactured date, and the part has a Task Definition against
    * it.
    *
    * @throws Exception
    */
   @Test( expected = ReceiveShipmentManufactDateException.class )
   public void validate_WithDirectMatchAndNoManufactDtAndEmptyShipmentManufacDtAndPartTaskDefn()
         throws Exception {

      PartNoKey lPartNo = Domain.createPart( aPartConfiguration -> {
         aPartConfiguration.setInventoryClass( RefInvClassKey.TRK );
         aPartConfiguration.setCode( PART_NO_OEM );
         aPartConfiguration.setPartStatus( RefPartStatusKey.ACTV );
      } );

      Domain.createRequirementDefinition(
            ( RequirementDefinition aRequirementDefinitionBuilder ) -> {

               aRequirementDefinitionBuilder.setExecutable( true );
               aRequirementDefinitionBuilder.setScheduledFromManufacturedDate();
               aRequirementDefinitionBuilder.addPartNo( lPartNo );

            } );

      // create archived inventory
      Domain.createTrackedInventory( aInventoryConfiguration -> {
         aInventoryConfiguration.setPartNumber( lPartNo );
         aInventoryConfiguration.setSerialNumber( SERIAL_NO_OEM );
         aInventoryConfiguration.setCondition( RefInvCondKey.ARCHIVE );
         aInventoryConfiguration.setLocation( iVendorLocation );
         aInventoryConfiguration.setOwner( iOwner );
      } );

      ShipmentKey lInboundShipment = Domain.createShipment( aShipmentConfiguration -> {
         aShipmentConfiguration.addShipmentSegment( aSegment -> {
            aSegment.setFromLocation( iVendorLocation );
            aSegment.setToLocation( iDockLocation );
         } );
         aShipmentConfiguration.setType( RefShipmentTypeKey.PURCHASE );
      } );

      ShipmentLineKey lInboundShipmentLine = Domain.createShipmentLine( aShipLineConfiguration -> {
         aShipLineConfiguration.part( lPartNo );
         aShipLineConfiguration.shipmentKey( lInboundShipment );
      } );

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( null );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SERIAL_NO_OEM );
      lReceiveShipmentLineTO.setLineNo( 1 );

      // Expect this to throw an error about no Manufactured date

      ReceiveShipmentManufactDateException.validate( lReceiveShipmentLineTO );

   }


   /**
    *
    * Test receiving a shipment with a part and serial number already existing in the system, where
    * the Manufactured date entered on the shipment is different from the part and the user has
    * permission to change manufactured date, and the part has a Task Definition against it.
    *
    * @throws Exception
    */
   @Test
   public void
         validate_WithDirectMatchAndExistingManufactDtWithPermissionAndEmptyShipmentManufacDtAndPartTaskDefn()
               throws Exception {
      PartNoKey lPartNo = Domain.createPart( aPartConfiguration -> {
         aPartConfiguration.setInventoryClass( RefInvClassKey.TRK );
         aPartConfiguration.setCode( PART_NO_OEM );
         aPartConfiguration.setPartStatus( RefPartStatusKey.ACTV );
      } );

      Domain.createRequirementDefinition(
            ( RequirementDefinition aRequirementDefinitionBuilder ) -> {

               aRequirementDefinitionBuilder.setExecutable( true );
               aRequirementDefinitionBuilder.setScheduledFromManufacturedDate();
               aRequirementDefinitionBuilder.addPartNo( lPartNo );

            } );

      Calendar lManufacturedDate = Calendar.getInstance();
      // Preset the inventory manufactured date to a week ago
      lManufacturedDate.add( Calendar.DAY_OF_MONTH, -7 );
      Date lInvManufacturedDate = lManufacturedDate.getTime();

      // create archived inventory
      Domain.createTrackedInventory( aInventoryConfiguration -> {
         aInventoryConfiguration.setPartNumber( lPartNo );
         aInventoryConfiguration.setSerialNumber( SERIAL_NO_OEM );
         aInventoryConfiguration.setCondition( RefInvCondKey.ARCHIVE );
         aInventoryConfiguration.setLocation( iVendorLocation );
         aInventoryConfiguration.setOwner( iOwner );
         aInventoryConfiguration.setManufactureDate( lInvManufacturedDate );
      } );

      ShipmentKey lInboundShipment = Domain.createShipment( aShipmentConfiguration -> {
         aShipmentConfiguration.addShipmentSegment( aSegment -> {
            aSegment.setFromLocation( iVendorLocation );
            aSegment.setToLocation( iDockLocation );
         } );
         aShipmentConfiguration.setType( RefShipmentTypeKey.PURCHASE );
      } );

      ShipmentLineKey lInboundShipmentLine = Domain.createShipmentLine( aShipLineConfiguration -> {
         aShipLineConfiguration.part( lPartNo );
         aShipLineConfiguration.shipmentKey( lInboundShipment );
      } );

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( null );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SERIAL_NO_OEM );

      // Validate that the Manufacture date exception does NOT fire since the part has a task
      // against it and no manufactured date was entered, but the INventory already has a
      // manufactured date.
      ReceiveShipmentManufactDateException.validate( lReceiveShipmentLineTO );

   }

}
