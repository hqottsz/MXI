package com.mxi.am.api.resource.materials.tracking.shipment;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.materials.tracking.shipment.impl.ShipmentResourceBean;
import com.mxi.am.api.resource.sys.refterm.eventstatus.impl.dao.EventStatusDao;
import com.mxi.am.api.resource.sys.refterm.eventstatus.impl.dao.JpaEventStatusDao;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Manufacturer;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.ejb.shipment.ShipmentLocalHomeStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.dao.shipment.JdbcShipShipmentLineMpDao;
import com.mxi.mx.core.dao.shipment.ShipShipmentLineMpDao;
import com.mxi.mx.core.ejb.shipment.ShipmentLocalHome;
import com.mxi.mx.core.exception.DuplicateMpiKeyException;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.location.LocalLocationException;
import com.mxi.mx.core.services.shipment.NoDockLocationException;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgInvDao;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class ShipmentResourceBeanTest extends ResourceBeanTest {

   // Location Data
   private static final String LOCATION_CODE_1 = "Location/Code/1";
   private static final String LOCATION_NAME_1 = "Location Name 1";
   private static final String LOCATION_CODE_2 = "Location/Code/2";
   private static final String LOCATION_NAME_2 = "Location Name 2";
   private static final String LOCATION_CODE_3 = "Location/Code/3";
   private static final String LOCATION_NAME_3 = "Location Name 3";
   private static final ShiftKey SHIFT_KEY = new ShiftKey( 11, 11 );

   // Shipment Data
   private static ShipmentKey SHIPMENT_KEY_1;
   private static String SHIPMENT_ID_1;
   private static ShipmentKey SHIPMENT_KEY_2;
   private static String SHIPMENT_ID_2;
   private static ShipmentKey SHIPMENT_KEY_3;
   private static String SHIPMENT_ID_3;
   private static ShipmentKey SHIPMENT_KEY_4;
   private static String SHIPMENT_ID_4;
   private static LocationKey LOCATION_KEY_1;
   private static LocationKey LOCATION_KEY_2;
   private static LocationKey LOCATION_KEY_3;
   private static LocationKey VENDOR_LOCATION_KEY;
   private static LocationKey SHIPPING_LOCATION_KEY;
   private static VendorKey VENDOR_KEY;
   private static PurchaseOrderKey PURCHASE_ORDER_KEY_1;
   private static PurchaseOrderKey PURCHASE_ORDER_KEY_2;
   private static PartNoKey PART_NO_KEY;
   private static InventoryKey INV_KEY;
   private static final RefEventStatusKey SHIPMENT_STATUS_PEND = RefEventStatusKey.IXPEND;
   private static final RefEventStatusKey SHIPMENT_STATUS_CANCEL = RefEventStatusKey.IXCANCEL;
   private static final Date SHIPMENT_DATE = new Date( 99999 );
   private static final Date SHIP_BY_DATE = new Date();

   // ShipmentLine Data
   private static final InventoryKey INVENTORY_KEY = new InventoryKey( 11, 11 );
   private static final String PART_ID = "3804733CE8F311E4BDE96F2A22D6A4CB";
   private static final String CARRIER_NAME = "C2222";
   private static final String WAYBILL_SDESC = "B12342";
   private static final Double WEIGHT_QT = 5.0;

   // Entities for comparison
   private static Shipment SHIPMENT_1;
   private static Shipment SHIPMENT_2;
   private static Shipment SHIPMENT_3;
   private static Shipment SHIPMENT_4;

   // Purchase Order Data
   private static final String VENDOR_CODE = "VENDOR_CD";
   private RefCurrencyKey defaultCurrency;
   private static final String PO_NUMBER = "P0100450";
   private static final String PO_NUMBER_2 = "P0100451";

   private static final String PEND_TO_PEND_ERROR =
         "Could not update status of shipment %s, the transition from IXPEND to IXPEND is unsupported";
   private static final String CANCEL_TO_PEND_ERROR =
         "Could not update status of shipment %s, the transition from IXCANCEL to IXPEND is unsupported";
   private static final String CANCEL_TO_INTR_ERROR =
         "Could not update status of shipment %s, the transition from IXCANCEL to IXINTR is unsupported";
   private static final String LOCATION_NOT_FOUND_ERROR =
         "The ship from location with id [ %s ] could not be found.";
   private static final String SAME_SHIP_LOCATION_ERROR_CODE = "MXERR-30223";
   private static final String LOCATION_NOT_DOCK_ERROR_CODE = "MXERR-30127";
   private static final String PRIORITY_CODE_ERROR = "The priority code 'INVALID' is not valid.";
   private static final String TYPE_CODE_ERROR = "The shipment type code 'INVALID' is not valid.";
   private static final String REASON_CODE_ERROR = "The reason code 'INVALID' is not valid.";
   private static final String MATERIAL_PLANNER_EXT_REF = "MP123";
   private static final String PART_NUM = "EDFT8077";
   private static final String PART_DESCRIPTION = "Part description";
   private static final String MANUFACT_CODE = "MANUFACT_CD";
   private static final String SERIAL_NO = "SN000281";

   @Inject
   ShipmentResourceBean iShipmentResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( ShipmentResource.class ).to( ShipmentResourceBean.class );
               bind( EventStatusDao.class ).to( JpaEventStatusDao.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( ShipmentLocalHome.class ).to( ShipmentLocalHomeStub.class );
               bind( EJBContext.class ).toInstance( iEJBContext );
               bind( ShipShipmentLineMpDao.class ).to( JdbcShipShipmentLineMpDao.class );
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
            }
         } );

   public LegacyKeyUtil iLegacyKeyUtil = new LegacyKeyUtil();


   @Before
   public void setUp() throws NamingException, MxException, KeyConversionException,
         AmApiBusinessException, AmApiResourceNotFoundException {

      InjectorContainer.get().injectMembers( this );

      iShipmentResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

      loadData();
   }


   @Test
   public void testGetShipmentByIdSuccess()
         throws KeyConversionException, AmApiResourceNotFoundException {

      String lAltId = iLegacyKeyUtil.legacyKeyToAltId( SHIPMENT_KEY_1 );
      Shipment lShipmentActual = iShipmentResourceBean.get( lAltId );

      Assert.assertEquals( SHIPMENT_1, lShipmentActual );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetShipmentByIdShipmentNotFound()
         throws KeyConversionException, AmApiResourceNotFoundException {

      String lAltId = "12345";
      iShipmentResourceBean.get( lAltId );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testGetShipmentByIdCannotConvertAltId()
         throws KeyConversionException, AmApiResourceNotFoundException {
      String lAltId = "INVALID";
      iShipmentResourceBean.get( lAltId );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testGetShipmentByIdAltIdBlank()
         throws KeyConversionException, AmApiResourceNotFoundException {
      String lAltId = "";
      iShipmentResourceBean.get( lAltId );
   }


   @Test
   public void testSearchShipmentsSuccess()
         throws KeyConversionException, AmApiResourceNotFoundException {
      String lAltId = iLegacyKeyUtil.legacyKeyToAltId( INVENTORY_KEY );
      String lStatus = StringUtils.substringAfterLast( SHIPMENT_STATUS_PEND.toString(), ":" );
      ShipmentSearchParameters lSearchParams =
            new ShipmentSearchParameters().inventoryId( lAltId ).status( lStatus );

      List<Shipment> lShipmentExpected = new ArrayList<Shipment>();
      lShipmentExpected.add( SHIPMENT_1 );
      lShipmentExpected.add( SHIPMENT_2 );
      lShipmentExpected.add( SHIPMENT_3 );
      lShipmentExpected.add( SHIPMENT_4 );
      List<Shipment> lShipmentsActual = iShipmentResourceBean.search( lSearchParams );

      Assert.assertEquals( lShipmentExpected.size(), lShipmentsActual.size() );
      Assert.assertTrue( lShipmentsActual.containsAll( lShipmentExpected ) );
   }


   @Test
   public void testSearchShipmentsWithBarcodeSuccess()
         throws KeyConversionException, AmApiResourceNotFoundException {
      ShipmentSearchParameters lSearchParams =
            new ShipmentSearchParameters().barcode( SHIPMENT_1.getBarcode() );

      List<Shipment> lShipmentExpected = new ArrayList<Shipment>();
      lShipmentExpected.add( SHIPMENT_1 );
      List<Shipment> lShipmentsActual = iShipmentResourceBean.search( lSearchParams );

      Assert.assertEquals( lShipmentExpected.size(), lShipmentsActual.size() );
      Assert.assertTrue( lShipmentsActual.containsAll( lShipmentExpected ) );
   }


   @Test
   public void testSearchShipmentsWithOrderIdSuccess()
         throws KeyConversionException, AmApiResourceNotFoundException {
      ShipmentSearchParameters lSearchParams =
            new ShipmentSearchParameters().orderId( SHIPMENT_1.getOrderId() );

      List<Shipment> lShipmentExpected = new ArrayList<Shipment>();
      lShipmentExpected.add( SHIPMENT_1 );
      lShipmentExpected.add( SHIPMENT_2 );
      List<Shipment> lShipmentsActual = iShipmentResourceBean.search( lSearchParams );

      Assert.assertEquals( lShipmentExpected.size(), lShipmentsActual.size() );
      Assert.assertTrue( lShipmentsActual.containsAll( lShipmentExpected ) );
   }


   @Test
   public void testSearchShipmentsNotFound() throws AmApiResourceNotFoundException {

      ShipmentSearchParameters lSearchParams =
            new ShipmentSearchParameters().inventoryId( "12345" ).status( "IXPEND" );

      List<Shipment> lResults = iShipmentResourceBean.search( lSearchParams );
      Assert.assertTrue( lResults.isEmpty() );

   }


   @Test
   public void testSearchShipmentsWithBarcodeNotFound() throws AmApiResourceNotFoundException {

      ShipmentSearchParameters lSearchParams = new ShipmentSearchParameters().barcode( "12345" );

      List<Shipment> lResults = iShipmentResourceBean.search( lSearchParams );
      Assert.assertTrue( lResults.isEmpty() );

   }


   @Test( expected = AmApiBadRequestException.class )
   public void testSearchShipmentsEmptySearchField()
         throws KeyConversionException, AmApiResourceNotFoundException {

      ShipmentSearchParameters lSearchParams = new ShipmentSearchParameters();

      iShipmentResourceBean.search( lSearchParams );

   }


   @Test
   public void testUpdateShipmentStatusSuccess() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setStatusCode( "IXCANCEL" );
      lShipment.setHistoric( true );

      Shipment lUpdatedShipment = iShipmentResourceBean.update( SHIPMENT_ID_1, lShipment );

      Assert.assertEquals( lShipment, lUpdatedShipment );
   }


   @Test
   public void testUpdateStatusToIntransitAndUpdateDetailsSuccess() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = iShipmentResourceBean.get( SHIPMENT_ID_4 );
      lShipment.setStatusCode( "IXINTR" );
      lShipment.setCarrierName( CARRIER_NAME );
      lShipment.setWaybillDesc( WAYBILL_SDESC );
      lShipment.setWeightQt( WEIGHT_QT );
      Shipment lUpdatedShipment = iShipmentResourceBean.update( SHIPMENT_ID_4, lShipment );
      lShipment.setDate( SHIPMENT_DATE );
      Assert.assertEquals( lShipment, lUpdatedShipment );
   }


   @Test
   public void testUpdateShipmentDetailsSuccess() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setStatusCode( null );
      lShipment.setCarrierName( CARRIER_NAME );
      lShipment.setWaybillDesc( WAYBILL_SDESC );
      lShipment.setWeightQt( WEIGHT_QT );
      lShipment.setShipByDate( SHIP_BY_DATE );
      Shipment lUpdatedShipment = iShipmentResourceBean.update( SHIPMENT_ID_1, lShipment );

      lShipment.setStatusCode( SHIPMENT_STATUS_PEND.getCd() );
      Assert.assertEquals( lShipment, lUpdatedShipment );
   }


   public void testUpdateShipmentStatusPendToIntransitSuccess() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = iShipmentResourceBean.get( SHIPMENT_ID_4 );
      lShipment.setStatusCode( "IXINTR" );
      Shipment lUpdatedShipment = iShipmentResourceBean.update( SHIPMENT_ID_4, lShipment );
      lShipment.setDate( SHIPMENT_DATE ); // Shipment_date
      Assert.assertEquals( lShipment, lUpdatedShipment );
   }


   @Test
   public void testUpdateShipmentWrongStatusCancelToIntransit() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setStatusCode( "IXCANCEL" );
      Shipment lUpdatedShipment = iShipmentResourceBean.update( SHIPMENT_ID_1, lShipment );
      lUpdatedShipment.setStatusCode( "IXINTR" );

      try {
         iShipmentResourceBean.update( SHIPMENT_ID_1, lUpdatedShipment );
         Assert.fail();
      } catch ( Exception aE ) {
         Assert.assertEquals( String.format( CANCEL_TO_INTR_ERROR, lUpdatedShipment.getBarcode() ),
               aE.getMessage() );
      }
   }


   @Test
   public void testUpdateShipmentStatusWrongStatusPendToPend() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setStatusCode( "IXPEND" );
      try {
         iShipmentResourceBean.update( SHIPMENT_ID_1, lShipment );
      } catch ( Exception aE ) {
         Assert.assertEquals( String.format( PEND_TO_PEND_ERROR, lShipment.getBarcode() ),
               aE.getMessage() );
      }
   }


   @Test
   public void testUpdateShipmentStatusWrongStatusCancelToPend() throws AmApiBusinessException,
         AmApiResourceNotFoundException, KeyConversionException, TriggerException, MxException {
      Shipment lShipment = SHIPMENT_3;
      lShipment.setStatusCode( SHIPMENT_STATUS_CANCEL.getCd() );
      lShipment.setHistoric( true );
      iShipmentResourceBean.update( SHIPMENT_ID_3, lShipment );

      lShipment.setStatusCode( "IXPEND" );

      try {
         iShipmentResourceBean.update( SHIPMENT_ID_3, lShipment );
      } catch ( Exception aE ) {
         Assert.assertEquals( String.format( CANCEL_TO_PEND_ERROR, lShipment.getBarcode() ),
               aE.getMessage() );
      }

   }


   @Test( expected = AmApiBadRequestException.class )
   public void testUpdateShipmentByEmptyObject()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Shipment shipment = new Shipment();
      iShipmentResourceBean.update( SHIPMENT_1.getId(), shipment );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testUpdateStatusToCancelAndUpdateShipmentDetailsFailure()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Shipment shipment = SHIPMENT_1;
      shipment.setStatusCode( "IXCANCEL" );
      shipment.setCarrierName( CARRIER_NAME );
      shipment.setWaybillDesc( WAYBILL_SDESC );
      shipment.setWeightQt( WEIGHT_QT );
      iShipmentResourceBean.update( SHIPMENT_1.getId(), shipment );
   }


   @Test
   public void testCreateShipmentNoLinesSuccess()
         throws KeyConversionException, AmApiBusinessException, CreateException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setId( null );
      lShipment.setBarcode( null );
      lShipment.setShipmentLines( null );

      Shipment lCreatedShipment = iShipmentResourceBean.create( lShipment );
      lShipment.setId( lCreatedShipment.getId() );
      lShipment.setBarcode( lCreatedShipment.getBarcode() );

      Assert.assertEquals( lShipment, lCreatedShipment );
   }


   @Test
   public void testCreateShipmentWithLinesSuccess()
         throws KeyConversionException, AmApiBusinessException {
      Shipment lShipment = SHIPMENT_1;

      Shipment lCreatedShipment = iShipmentResourceBean.create( lShipment );
      lShipment.setId( lCreatedShipment.getId() );
      lShipment.setBarcode( lCreatedShipment.getBarcode() );

      Assert.assertEquals( lShipment, lCreatedShipment );
   }


   @Test
   public void testCreateShipmentWithRoundedQuantity() throws AmApiBusinessException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.getShipmentLines().get( 0 ).setQuantity( new BigDecimal( 1.4 ) );

      Shipment lCreatedShipment = iShipmentResourceBean.create( lShipment );
      lShipment.setId( lCreatedShipment.getId() );
      lShipment.setBarcode( lCreatedShipment.getBarcode() );

      Assert.assertEquals( lShipment, lCreatedShipment );
      Assert.assertEquals( 1,
            lCreatedShipment.getShipmentLines().get( 0 ).getQuantity().doubleValue(), 0 );
   }


   @Test
   public void testCreateShipmentWithInvalidPart() throws KeyConversionException {
      Shipment lShipment = SHIPMENT_1;
      ShipmentLine lShipmentLine = new ShipmentLine();
      lShipmentLine.setPartId( "3804733CE8F311E4BDE96F2A22D6A4C1" );
      lShipment.addShipmentLine( lShipmentLine );

      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( AmApiBusinessException e ) {
         Assert.assertEquals(
               "The part with id [3804733CE8F311E4BDE96F2A22D6A4C1] could not be found.",
               e.getMessage() );
      }
   }


   @Test
   public void testCreateShipFromLocationNotFound() {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setShipFromLocationId( "location" );
      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( Exception aE ) {
         Assert.assertEquals(
               String.format( LOCATION_NOT_FOUND_ERROR, lShipment.getShipFromLocationId() ),
               aE.getMessage() );
      }
   }


   @Test
   public void testCreateShipmentPriorityCodeNotProvided() throws AmApiBusinessException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setPriorityCode( null );
      lShipment.setId( null );

      Shipment lCreatedShipment = iShipmentResourceBean.create( lShipment );
      lShipment.setId( lCreatedShipment.getId() );
      lShipment.setBarcode( lCreatedShipment.getBarcode() );

      Assert.assertEquals( lShipment, lCreatedShipment );
   }


   @Test
   public void testCreateShipFromSameAsShipToLocation() throws KeyConversionException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setShipToLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ) );
      lShipment.setShipFromLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ) );

      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( AmApiBusinessException e ) {
         Assert.assertEquals( e.getCause().getClass(), LocalLocationException.class );
         Assert.assertTrue( "unexpected MXERR code",
               e.getMessage().contains( SAME_SHIP_LOCATION_ERROR_CODE ) );
      } catch ( Exception e ) {
         Assert.fail( "unexpected Exception: " + e.getClass() + " caused by " + e.getCause() );
      }
   }


   @Test
   public void testCreateInvalidPriority() {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setPriorityCode( "INVALID" );

      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( Exception aE ) {
         Assert.assertEquals( PRIORITY_CODE_ERROR, aE.getMessage() );
      }
   }


   @Test
   public void testCreateInvalidShipmentType() {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setTypeCode( "INVALID" );

      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( Exception aE ) {
         Assert.assertEquals( TYPE_CODE_ERROR, aE.getMessage() );
      }
   }


   @Test
   public void testCreateInvalidReasonCode() {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setReasonCode( "INVALID" );
      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( Exception aE ) {
         Assert.assertEquals( REASON_CODE_ERROR, aE.getMessage() );
      }
   }


   @Test
   public void testCreateLocationNotDock() throws KeyConversionException {
      Shipment lShipment = SHIPMENT_1;
      lShipment.setShipFromLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_3 ) );

      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( AmApiBusinessException e ) {
         Assert.assertEquals( e.getCause().getClass(), NoDockLocationException.class );
         Assert.assertTrue( "unexpected MXERR code",
               e.getMessage().contains( LOCATION_NOT_DOCK_ERROR_CODE ) );
      } catch ( Exception e ) {
         // any other exception is unexpected
         Assert.fail( "unexpected Exception " + e.getClass() + " with cause " + e.getCause() );
      }
   }


   @Test
   public void testCreateShipmentWithMPExtRef()
         throws KeyConversionException, AmApiBusinessException {

      Shipment lShipment = getShipmentToCreate();
      ShipmentLine lShipmentLine = lShipment.getShipmentLines().get( 0 );
      // set the material planning external reference
      lShipmentLine.setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );

      Shipment lCreatedShipment = iShipmentResourceBean.create( lShipment );

      lShipment.setId( lCreatedShipment.getId() );
      lShipment.setBarcode( lCreatedShipment.getBarcode() );
      Assert.assertEquals( lShipment, lCreatedShipment );
   }


   @Test
   public void testCreateShipmentWithDuplicateMPExtRef()
         throws KeyConversionException, AmApiBusinessException {
      // first create a shipment with a material planning external reference
      Shipment lShipment = getShipmentToCreate();
      ShipmentLine lShipmentLine = lShipment.getShipmentLines().get( 0 );
      lShipmentLine.setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );
      try {
         iShipmentResourceBean.create( lShipment );
      } catch ( Exception e ) {
         Assert.fail( "unexpected Exception " + e.getClass() + " with cause " + e.getCause() );
      }

      // now try to create a second shipment with the same material planning external reference
      Shipment lDuplicateShipment = getShipmentToCreate();
      ShipmentLine lDuplicateShipmentLine = lDuplicateShipment.getShipmentLines().get( 0 );
      lDuplicateShipmentLine.setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );
      try {
         iShipmentResourceBean.create( lDuplicateShipment );
      } catch ( AmApiBusinessException e ) {
         Assert.assertEquals( e.getCause().getClass(), DuplicateMpiKeyException.class );
      } catch ( Exception e ) {
         // any other exception is unexpected
         Assert.fail( "unexpected Exception " + e.getClass() + " with cause " + e.getCause() );
      }

   }


   @Test
   public void testCreateShipmentWithSerializedQuantityGreaterThanOne()
         throws AmApiBusinessException, ParseException, KeyConversionException {

      Shipment lShipment = getShipmentToCreate();
      ShipmentLine lShipmentLine = lShipment.getShipmentLines().get( 0 );
      // setting quantity to 10 for a serialized part will generate 10 lines
      lShipmentLine.setQuantity( BigDecimal.TEN );
      lShipmentLine.setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );

      Shipment lCreatedShipment = iShipmentResourceBean.create( lShipment );

      // assert there are 10 lines and each has the given material planner ext ref
      Assert.assertEquals( 10, lCreatedShipment.getShipmentLines().size() );
      for ( ShipmentLine lLine : lCreatedShipment.getShipmentLines() ) {
         Assert.assertEquals( MATERIAL_PLANNER_EXT_REF,
               lLine.getMaterialPlannerExternalReference() );
      }
   }


   /**
    * Loads required data for the test.
    *
    * @throws KeyConversionException
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   private void loadData()
         throws KeyConversionException, AmApiBusinessException, AmApiResourceNotFoundException {
      // Create Location
      VENDOR_LOCATION_KEY = Domain.createLocation( loc -> loc.setType( RefLocTypeKey.VENDOR ) );
      SHIPPING_LOCATION_KEY = Domain.createLocation( loc -> loc.setType( RefLocTypeKey.DOCK ) );

      // Create Vendor
      VENDOR_KEY =
            new VendorBuilder().atLocation( VENDOR_LOCATION_KEY ).withCode( VENDOR_CODE ).build();

      // Create PurchaseOrder
      PURCHASE_ORDER_KEY_1 = Domain.createPurchaseOrder( order -> {
         order.orderType( RefPoTypeKey.PURCHASE );
         order.status( RefEventStatusKey.POAUTH );
         order.vendor( VENDOR_KEY );
         order.setOrderNumber( PO_NUMBER );
         order.usingCurrency( defaultCurrency );
         order.shippingTo( SHIPPING_LOCATION_KEY );
      } );

      PURCHASE_ORDER_KEY_2 = Domain.createPurchaseOrder( order -> {
         order.orderType( RefPoTypeKey.PURCHASE );
         order.status( RefEventStatusKey.POAUTH );
         order.vendor( VENDOR_KEY );
         order.setOrderNumber( PO_NUMBER_2 );
         order.usingCurrency( defaultCurrency );
         order.shippingTo( SHIPPING_LOCATION_KEY );
      } );

      LOCATION_KEY_1 = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.DOCK );
            aLocation.setCode( LOCATION_CODE_1 );
            aLocation.setName( LOCATION_NAME_1 );
            aLocation.setOvernightShift( SHIFT_KEY );
            aLocation.setTimeZone( TimeZoneKey.TORONTO );
            aLocation.setHubLocation( null );
            aLocation.setParent( null );
            aLocation.setIsSupplyLocation( true );
            aLocation.setIsDefaultDock( true );
         }
      } );

      LOCATION_KEY_2 = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.DOCK );
            aLocation.setCode( LOCATION_CODE_2 );
            aLocation.setName( LOCATION_NAME_2 );
            aLocation.setOvernightShift( SHIFT_KEY );
            aLocation.setTimeZone( TimeZoneKey.TORONTO );
            aLocation.setHubLocation( null );
            aLocation.setParent( null );
            aLocation.setIsSupplyLocation( true );
            aLocation.setIsDefaultDock( true );
         }
      } );

      LOCATION_KEY_3 = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
            aLocation.setCode( LOCATION_CODE_3 );
            aLocation.setName( LOCATION_NAME_3 );
            aLocation.setOvernightShift( SHIFT_KEY );
            aLocation.setTimeZone( TimeZoneKey.TORONTO );
            aLocation.setHubLocation( null );
            aLocation.setParent( null );
            aLocation.setIsSupplyLocation( true );
            aLocation.setIsDefaultDock( true );
         }
      } );

      PART_NO_KEY = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setCode( PART_NUM );// part_no
            aPart.setShortDescription( PART_DESCRIPTION );
            aPart.setInventoryClass( RefInvClassKey.TRK );
            aPart.setQtyUnitKey( RefQtyUnitKey.EA );
            aPart.setManufacturer(
                  Domain.createManufacturer( new DomainConfiguration<Manufacturer>() {

                     @Override
                     public void configure( Manufacturer aManufacturer ) {
                        aManufacturer.setCode( MANUFACT_CODE );
                     }
                  } ) );
         }
      } );

      INV_KEY = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aRfbInventoryBuilder ) {
            aRfbInventoryBuilder.setPartNumber( PART_NO_KEY );
            aRfbInventoryBuilder.setCondition( RefInvCondKey.RFI );
            aRfbInventoryBuilder.setLocation( LOCATION_KEY_1 );
            aRfbInventoryBuilder.setSerialNumber( SERIAL_NO );
            aRfbInventoryBuilder.setComplete( false );
            aRfbInventoryBuilder.setIssued( true );
            aRfbInventoryBuilder.setOwner( null );
         }
      } );

      Shipment lShipment1 = getShipment( iLegacyKeyUtil.legacyKeyToAltId( PURCHASE_ORDER_KEY_1 ),
            iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ),
            iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_2 ), false, "IXPEND" );

      SHIPMENT_1 = iShipmentResourceBean.create( lShipment1 );
      SHIPMENT_KEY_1 = iLegacyKeyUtil.altIdToLegacyKey( SHIPMENT_1.getId(), ShipmentKey.class );

      Shipment lShipment2 = getShipment( iLegacyKeyUtil.legacyKeyToAltId( PURCHASE_ORDER_KEY_1 ),
            iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_2 ),
            iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ), true, "IXPEND" );

      SHIPMENT_2 = iShipmentResourceBean.create( lShipment2 );
      SHIPMENT_KEY_2 = iLegacyKeyUtil.altIdToLegacyKey( SHIPMENT_2.getId(), ShipmentKey.class );

      Shipment lShipment3 = getShipment( iLegacyKeyUtil.legacyKeyToAltId( PURCHASE_ORDER_KEY_2 ),
            iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ),
            iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_2 ), false, "IXCANCEL" );

      SHIPMENT_3 = iShipmentResourceBean.create( lShipment3 );
      SHIPMENT_KEY_3 = iLegacyKeyUtil.altIdToLegacyKey( SHIPMENT_3.getId(), ShipmentKey.class );

      Shipment lShipment4 = getShipmentToCreateWithoutShipmentLines();
      SHIPMENT_4 = iShipmentResourceBean.create( lShipment4 );
      SHIPMENT_KEY_4 = iLegacyKeyUtil.altIdToLegacyKey( SHIPMENT_4.getId(), ShipmentKey.class );

      Domain.createShipmentLine( aShipLineConfiguration -> {
         aShipLineConfiguration.inventory( INV_KEY );
         aShipLineConfiguration.shipmentKey( SHIPMENT_KEY_4 );
         aShipLineConfiguration.part( PART_NO_KEY );
      } );

      // DB query for the generated Alt_Id
      List<String> lInventoryId = new ArrayList<String>();
      lInventoryId.add( iLegacyKeyUtil.legacyKeyToAltId( INVENTORY_KEY ) );
      SHIPMENT_ID_1 = iLegacyKeyUtil.legacyKeyToAltId( SHIPMENT_KEY_1 );
      SHIPMENT_1.setId( SHIPMENT_ID_1 );
      SHIPMENT_ID_2 = iLegacyKeyUtil.legacyKeyToAltId( SHIPMENT_KEY_2 );
      SHIPMENT_2.setId( SHIPMENT_ID_2 );
      SHIPMENT_ID_3 = iLegacyKeyUtil.legacyKeyToAltId( SHIPMENT_KEY_3 );
      SHIPMENT_3.setId( SHIPMENT_ID_3 );
      SHIPMENT_4 = iShipmentResourceBean.get( SHIPMENT_4.getId() );
      SHIPMENT_ID_4 = iLegacyKeyUtil.legacyKeyToAltId( SHIPMENT_KEY_4 );

   }


   /**
    * Get a shipment with the given details.
    *
    * @return the shipment
    * @throws KeyConversionException
    */
   private Shipment getShipment( String aOrder, String aShipFromLocId, String aShipToLocId,
         boolean aHistoric, String aStatus ) throws KeyConversionException {
      Shipment lShipment = new Shipment();
      lShipment.setShipFromLocationId( aShipFromLocId );
      lShipment.setShipToLocationId( aShipToLocId );
      lShipment.setHistoric( aHistoric );
      lShipment.setStatusCode( aStatus );
      lShipment.setTypeCode( "STKTRN" );
      lShipment.setPriorityCode( "NORMAL" );
      lShipment.setNote( "Note" );
      lShipment.setShipByDate( SHIPMENT_DATE );
      lShipment.setOrderId( aOrder );

      ShipmentLine lLine = new ShipmentLine();
      lLine.setPartId( PART_ID );
      lLine.setQuantity( BigDecimal.ONE );
      lShipment.setShipmentLines( Arrays.asList( lLine ) );

      return lShipment;
   }


   /**
    * Get a shipment with the given details.
    *
    * @return the shipment
    * @throws KeyConversionException
    */
   private Shipment getShipmentToCreate() throws KeyConversionException {
      Shipment lShipment = new Shipment();
      lShipment.setShipFromLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ) );
      lShipment.setShipToLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_2 ) );
      lShipment.setHistoric( false );
      lShipment.setStatusCode( "IXPEND" );
      lShipment.setTypeCode( "STKTRN" );
      lShipment.setPriorityCode( "NORMAL" );
      lShipment.setNote( "Note" );
      lShipment.setShipByDate( SHIPMENT_DATE );

      ShipmentLine lLine = new ShipmentLine();
      lLine.setPartId( PART_ID );
      lLine.setQuantity( BigDecimal.ONE );
      lShipment.setShipmentLines( Arrays.asList( lLine ) );

      return lShipment;
   }


   private Shipment getShipmentToCreateWithoutShipmentLines() throws KeyConversionException {
      Shipment lShipment = new Shipment();
      lShipment.setShipFromLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_1 ) );
      lShipment.setShipToLocationId( iLegacyKeyUtil.legacyKeyToAltId( LOCATION_KEY_2 ) );
      lShipment.setHistoric( false );
      lShipment.setStatusCode( "IXPEND" );
      lShipment.setTypeCode( "STKTRN" );
      lShipment.setPriorityCode( "NORMAL" );
      lShipment.setNote( "Note" );
      lShipment.setShipByDate( SHIPMENT_DATE );
      lShipment.setDate( SHIPMENT_DATE );
      lShipment.setShipByDate( SHIPMENT_DATE );

      return lShipment;
   }

}
