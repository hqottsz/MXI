package com.mxi.mx.core.ejb.shipment;

import java.util.List;

import javax.ejb.SessionContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mockito;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.PositiveValueException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.ejb.MxSessionBean;
import com.mxi.mx.core.exception.DuplicateMpiKeyException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.shipment.ShipmentLineService;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.services.shipment.ShipmentTO;
import com.mxi.mx.core.table.ship.ShipShipmentTable;


/**
 *
 * @author Akila Peiris
 * @created June 05, 2018
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ShipmentBeanTest {

   private static final String PART_OEM = "PART";
   private static final String MANUFACTURER = "MANUF";
   private static final String VENDOR_LOCATION = "YOW";
   private static final String DOCK_LOCATION = "CMB";
   private static final Double EXPECTED_QTY = 3.0;
   private static final String MP_KEY_1 = "MPKEY001";
   private static final String MP_KEY_2 = "MPKEY002";
   private static final String MP_KEY_3 = "MPKEY003";

   private LocationKey iVendorLocation;
   private LocationKey iDockLocation;
   private HumanResourceKey iHr;
   private PartNoKey iBatchPart;
   private PartNoKey iSerialPart;
   private ShipmentBean iShipmentBean;
   private SessionContext iSessionContext;
   private MxSessionBean iSessionBean;
   private ShipmentKey iShipment;
   private ShipmentLineKey iMpShipmentLine;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() throws Exception {

      iHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      iSessionContext = Mockito.mock( SessionContext.class, Mockito.RETURNS_DEEP_STUBS );

      iSessionBean = Mockito.mock( MxSessionBean.class, Mockito.RETURNS_DEEP_STUBS );

      Mockito.when( iSessionBean.getHumanResource() ).thenReturn( iHr );

      iShipmentBean = new ShipmentBean();
      iShipmentBean.ejbCreate();
      iShipmentBean.setSessionContext( iSessionContext );

      iVendorLocation = new LocationDomainBuilder().withCode( VENDOR_LOCATION )
            .withType( RefLocTypeKey.VENDOR ).build();

      iDockLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).withCode( DOCK_LOCATION ).build();

      ManufacturerKey lManufacturer = Domain.createManufacturer( aManufacturer -> {
         aManufacturer.setCode( MANUFACTURER );
      } );

      iBatchPart = new PartNoBuilder().withOemPartNo( PART_OEM ).manufacturedBy( lManufacturer )
            .withInventoryClass( RefInvClassKey.BATCH ).build();

      iSerialPart = new PartNoBuilder().withOemPartNo( "abc" ).manufacturedBy( lManufacturer )
            .withInventoryClass( RefInvClassKey.SER ).build();

      iShipment = new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
            .withType( RefShipmentTypeKey.PURCHASE ).build();

      iMpShipmentLine = new ShipmentLineBuilder( iShipment ).forPart( iBatchPart )
            .withExpectedQuantity( EXPECTED_QTY ).withMpKeySdesc( MP_KEY_1 ).build();

   }


   /**
    * GIVEN a shipment generated through MPI (Material Planning Integration) for a batch part, WHEN
    * the shipment is created in Maintenix, THEN the created shipment line's column MP_KEY_SDESC
    * should have the same value sent by MPI
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testMpiGeneratedShipmentCreationForBatchPart() throws Exception {

      // setup a shipment transfer object
      ShipmentTO lShipmentTO = setupShipmentTO();
      lShipmentTO.setPartNoKey( iBatchPart );
      lShipmentTO.setExpectedQuantity( EXPECTED_QTY, "quantity" );

      ShipmentKey lShipment = iShipmentBean.create( lShipmentTO, iHr );

      // assert whether the shipment is created with a shipment line that contains the correct
      // MP_KEY_SDESC
      assertShipmentAndLinesCreatedWithMpKey( lShipment );
   }


   /**
    * GIVEN a shipment generated through MPI (Material Planning Integration) for a serial part, WHEN
    * the shipment is created in Maintenix with a quantity greater than one, THEN the created
    * shipment lines in the shipment should have the same MP_KEY_SDESC
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testMpiGeneratedShipmentCreationForSerializedPart() throws Exception {

      // setup a shipment transfer object
      ShipmentTO lShipmentTO = setupShipmentTO();
      lShipmentTO.setPartNoKey( iSerialPart );
      lShipmentTO.setExpectedQuantity( EXPECTED_QTY, "quantity" );

      ShipmentKey lShipment = iShipmentBean.create( lShipmentTO, iHr );

      // assert whether the shipment is created with lines that contain the correct MP_KEY_SDESC
      assertShipmentAndLinesCreatedWithMpKey( lShipment );
   }


   /**
    *
    * GIVEN a shipment generated through MPI (Material Planning Integration), WHEN a new shipment
    * line is added in Maintenix, THEN the created shipment line's column MP_KEY_SDESC should have
    * the same value sent by MPI
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAddShipmentLineForMpShipment() throws Exception {

      // add a shipment line with MP_KEY_DESC set to a value to simulate an MPI generated shipment
      // line through the ShipmentBean
      List<ShipmentLineKey> lMpShipmentLines = iShipmentBean.addShipmentLine( iShipment, PART_OEM,
            null, MANUFACTURER, null, RefInvCondKey.RFI.getCd(), EXPECTED_QTY, MP_KEY_3 );

      // assert the mp_key for the newly added shipment line
      Assert.assertEquals( MP_KEY_3,
            ShipmentLineService.getShipmentLineMpKeySdesc( lMpShipmentLines.get( 0 ) ) );

   }


   /**
    *
    * GIVEN a shipment with a shipment line generated through MPI (Material Planning Integration),
    * WHEN a new shipment line with the same MP_KEY_SDESC is added in Maintenix, THEN a
    * DuplicateMpiKeyException should be triggered
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDuplicateMpKeyException() throws Exception {

      // assert that trying to add a second shipment line with the same reference as
      // iMpShipmentLineKey fails with a DuplicateMpiKeyException
      try {
         iShipmentBean.addShipmentLine( iShipment, PART_OEM, null, MANUFACTURER, null,
               RefInvCondKey.RFI.getCd(), EXPECTED_QTY, MP_KEY_1 );
         Assert.fail( "Expected DuplicateMpiKeyException." );
      } catch ( DuplicateMpiKeyException e ) {
         // do nothing. This exception is expected since there cannot be duplicate MPI keys when
         // new MP generated shipment lines are added in Maintenix
      }
   }


   /**
    *
    * GIVEN a shipment with a shipment line generated through MPI (Material Planning Integration),
    * WHEN the shipment line is deleted, THEN the corresponding MP_KEY should be cleared from the
    * SHIP_SHIPMENT_LINE_MP table
    *
    * @throws Exception
    *            is an error occurs
    */
   @Test
   public void testMpKeyClearedWhenShipmentLineRemoved() throws Exception {

      // remove the shipment line added in the data setup
      iShipmentBean.removeShipmentLine( new ShipmentLineKey[] { iMpShipmentLine } );

      // assert that the MP reference has been cleared from the shipment line (the shipment line
      // should not exist at this point)
      Assert.assertEquals( null, ShipmentLineService.getShipmentLineMpKeySdesc( iMpShipmentLine ) );
   }


   private void assertShipmentAndLinesCreatedWithMpKey( ShipmentKey aShipment ) {
      Assert.assertNotNull( ShipShipmentTable.findByPrimaryKey( aShipment ) );

      for ( ShipmentLineKey lShipmentLine : ShipmentService.getShipmentLines( aShipment ) ) {
         Assert.assertEquals( MP_KEY_2,
               ShipmentLineService.getShipmentLineMpKeySdesc( lShipmentLine ) );
      }
   }


   private ShipmentTO setupShipmentTO() throws MandatoryArgumentException, PositiveValueException {
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setPriority( RefReqPriorityKey.NORMAL, "priority" );
      lShipmentTO.setShipmentType( RefShipmentTypeKey.PURCHASE, "shipment type" );
      lShipmentTO.setShipFrom( iVendorLocation, "ship from" );
      lShipmentTO.setShipTo( DOCK_LOCATION, "ship to" );
      lShipmentTO.setMpKeySdesc( MP_KEY_2 );
      return lShipmentTO;
   }


   @After
   public void tearDown() {
      iShipmentBean.setSessionContext( null );
   }
}
