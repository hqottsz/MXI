package com.mxi.mx.web.servlet.shipment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.services.location.InvalidLocationCodeException;
import com.mxi.mx.core.services.location.LocalLocationException;
import com.mxi.mx.core.services.shipment.InvalidExpectedQuantityException;
import com.mxi.mx.core.services.shipment.NoDockLocationException;
import com.mxi.mx.core.services.shipment.ShipmentTO;
import com.mxi.mx.core.utils.ValidationResults;


/**
 * This class contains tests for CreateEditShipmentTransferObjectValidator
 *
 */
public class CreateEditShipmentTransferObjectValidatorTest {

   private static final String LOCATION_FROM = "ABC/DOCK";
   private static final String LOCATION_TO_LOCAL = "ABC/DOCK1";
   private static final String LOCATION_TO = "DEF/DOCK";
   private static final String LOCATION_FROM_NODOCK = "DEF/NO_DOCK";
   private static final String LOCATION_INVALID = "INVALID";

   private LocationKey iFromSupplyLocationKey;
   private LocationKey iToSupplyLocationKey;
   private LocationKey iFromShipmentLocationKey;
   private LocationKey iFromNoDockLocationKey;
   private InventoryKey iInventoryItemKey;
   private InventoryKey iBatchInventoryItemKey;
   private PartNoKey iPartItemKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() {
      iFromSupplyLocationKey = new LocationDomainBuilder().isSupplyLocation().build();
      iFromShipmentLocationKey =
            new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).isDefaultDock( true )
                  .withCode( LOCATION_FROM ).withSupplyLocation( iFromSupplyLocationKey ).build();
      new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).isDefaultDock( false )
            .withCode( LOCATION_TO_LOCAL ).withSupplyLocation( iFromSupplyLocationKey ).build();

      iToSupplyLocationKey = new LocationDomainBuilder().isSupplyLocation().build();
      new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).isDefaultDock( false )
            .withCode( LOCATION_TO ).withSupplyLocation( iToSupplyLocationKey ).build();

      iFromNoDockLocationKey = new LocationDomainBuilder().withType( RefLocTypeKey.SCRAP )
            .isDefaultDock( false ).withCode( LOCATION_FROM_NODOCK )
            .withSupplyLocation( iToSupplyLocationKey ).build();

      iInventoryItemKey = new InventoryBuilder().atLocation( iFromShipmentLocationKey ).build();

      iBatchInventoryItemKey =
            new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withBinQt( 10 ).build();

      iPartItemKey = Domain.createPart();
   }


   /**
    * Verifies that when unexpected quantity is specified
    * {@linkplain com.mxi.mx.core.services.shipment.InvalidExpectedQuantityException} is added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults}.
    *
    * @throws MxException
    */
   @Test
   public void errorIsReportedIfUnexpectedQuantitySpecified() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 11 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setInventoryKey( iBatchInventoryItemKey );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_INV,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( lResults.hasErrors() );
      assertTrue( 1 == lResults.getErrors().size() );
      assertTrue( lResults.getErrors().get( 0 )
            .getException() instanceof InvalidExpectedQuantityException );
   }


   /**
    * Verifies that when mandatory parameter inventory is missing
    * {@linkplain com.mxi.mx.common.exception.MandatoryArgumentException} is added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults}.
    *
    * @throws MxException
    */
   @Test
   public void errorIsReportedIfInventoryIsNotSet() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_INV,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( lResults.hasErrors() );
      assertEquals( 1, lResults.getErrors().size() );
      assertTrue(
            lResults.getErrors().get( 0 ).getException() instanceof MandatoryArgumentException );
      assertTrue(
            lResults.getErrors().get( 0 ).getException().getMessage() + " contains "
                  + i18n.get( "web.lbl.INVENTORY" ),
            lResults.getErrors().get( 0 ).getException().getMessage()
                  .contains( i18n.get( "web.lbl.INVENTORY" ) ) );
   }


   /**
    * Verifies that when mandatory parameter part number is missing
    * {@linkplain com.mxi.mx.common.exception.MandatoryArgumentException} is added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults}.
    *
    * @throws MxException
    */
   @Test
   public void errorIsReportedIfPartNumberIsNotSet() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_PART,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( lResults.hasErrors() );
      assertEquals( 1, lResults.getErrors().size() );
      assertTrue(
            lResults.getErrors().get( 0 ).getException() instanceof MandatoryArgumentException );
      assertTrue(
            lResults.getErrors().get( 0 ).getException().getMessage() + " contains "
                  + i18n.get( "web.lbl.PART_NUMBER" ),
            lResults.getErrors().get( 0 ).getException().getMessage()
                  .contains( i18n.get( "web.lbl.PART_NUMBER" ) ) );
   }


   /**
    * Verifies that when ship from dock hierarchy is incorrect setup
    * {@linkplain com.mxi.mx.core.services.shipment.NoDockLocationException} is added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults}.
    *
    * @throws MxException
    */
   @Test
   public void errorIsReportedIfShipFromDockHierarchyIsInvalid() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setInventoryKey( iInventoryItemKey );
      lShipmentTO.setShipFrom( iFromNoDockLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_INV,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( lResults.hasErrors() );
      // In this case validation lResults will have both: NoDockLocationException and
      // LocalLocationException
      assertTrue( 2 == lResults.getErrors().size() );
      assertTrue( lResults.getErrors().get( 0 ).getException() instanceof NoDockLocationException );
   }


   /**
    * Verifies that when code of the ship to location is incorrect
    * {@linkplain com.mxi.mx.core.services.location.InvalidLocationCodeException} is added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults}.
    *
    * @throws MxException
    */
   @Test
   public void errorIsReportedIfShipToLocationCodeIsInvalid() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setInventoryKey( iInventoryItemKey );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_INVALID, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_INV,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( lResults.hasErrors() );
      assertTrue( 1 == lResults.getErrors().size() );
      assertTrue(
            lResults.getErrors().get( 0 ).getException() instanceof InvalidLocationCodeException );
   }


   /**
    * Verifies that when shipment form and shipment to locations are not remote to each other
    * {@linkplain com.mxi.mx.core.services.location.LocalLocationException} is added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults}.
    *
    * @throws MxException
    */
   @Test
   public void errorIsReportedIfLocationsAreNotRemote() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setInventoryKey( iInventoryItemKey );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO_LOCAL, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_INV,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( lResults.hasErrors() );
      assertTrue( 1 == lResults.getErrors().size() );
      assertTrue( lResults.getErrors().get( 0 ).getException() instanceof LocalLocationException );
   }


   /**
    * Verifies that when {@linkplain com.mxi.mx.core.services.shipment.ShipmentTO} is valid in
    * <code>Context.CREATE_INV</code> no errors added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults} .
    *
    * @throws MxException
    */
   @Test
   public void noErrorsReportedInCreateInventoryContext() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setInventoryKey( iInventoryItemKey );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_INV,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( !lResults.hasErrors() );
   }


   /**
    * Verifies that when {@linkplain com.mxi.mx.core.services.shipment.ShipmentTO} is valid in
    * <code>Context.CREATE_PART</code> no errors added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults} .
    *
    * @throws MxException
    */
   @Test
   public void noErrorsReportedInCreatePartContext() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setExpectedQuantity( Double.valueOf( 1 ), i18n.get( "web.lbl.QUANTITY" ) );
      lShipmentTO.setPartNoKey( iPartItemKey );
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_PART,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( !lResults.hasErrors() );

   }


   /**
    * Verifies that when {@linkplain com.mxi.mx.core.services.shipment.ShipmentTO} is valid in
    * <code>Context.CREATE_ADHOC</code> no errors added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults} .
    *
    * @throws MxException
    */
   @Test
   public void noErrorsReportedInAdhocContext() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.CREATE_ADHOC,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( !lResults.hasErrors() );
   }


   /**
    * Verifies that when {@linkplain com.mxi.mx.core.services.shipment.ShipmentTO} is valid in
    * <code>Context.EDIT_SHIPMENT</code> no errors added to
    * {@linkplain com.mxi.mx.core.utils.ValidationResults} .
    *
    * @throws MxException
    */
   @Test
   public void noErrorsReportedInEditShipmentContext() throws MxException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setShipFrom( iFromSupplyLocationKey, i18n.get( "web.lbl.SHIP_FROM" ) );
      lShipmentTO.setShipTo( LOCATION_TO, i18n.get( "web.lbl.SHIP_TO" ) );

      CreateEditShipmentTransferObjectValidator lValidator =
            new CreateEditShipmentTransferObjectValidator( CreateEditShipment.Context.EDIT_SHIPMENT,
                  lShipmentTO );
      ValidationResults lResults = lValidator.validate();
      assertTrue( !lResults.hasErrors() );
   }

}
