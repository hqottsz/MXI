package com.mxi.am.api.resource.materials.proc.purchaseorder;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJBContext;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.collections.CollectionUtils;
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
import com.ibm.icu.util.Calendar;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine.LineType;
import com.mxi.am.api.resource.materials.proc.purchaseorder.impl.PurchaseOrderResourceBean;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.ejb.DAOLocalStub;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.ejb.po.PurchaseOrderLocalHomeStub;
import com.mxi.mx.common.ejb.security.SecurityIdentityStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.auth.UserPrincipal;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.dao.po.JdbcPoLineMpTableDao;
import com.mxi.mx.core.dao.po.PoLineMpTableDao;
import com.mxi.mx.core.ejb.po.PurchaseOrderLocalHome;
import com.mxi.mx.core.services.user.UserService;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgEventDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgInvDao;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for purchase order api
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PurchaseOrderResourceBeanTest extends ResourceBeanTest {

   private static final String PURCHASE_ORDER_ID_NOT_EXIST = "EFAC1FF71A5D4C58B873A75D2F239512";
   private static final String PURCHASE_ORDER_NUMBER = "P0100013";
   private static final String PURCHASE_ORDER_NUMBER_NEW = "P2100044";
   private static final String PRIORITY_NORMAL = "NORMAL";
   private static final String USER_ID = "76881FF71A5D4C58B873A75D2F23E111";
   private static final String INVALID_USER_ID = "12341FF71A5D4C58B873A75D2F23AAAA";
   private static final String ORG_ID = "317E87556F1911E6A7B99167A0CBBCCF";
   private static final String INVALID_ORG_ID = "222287556F1911E6A7B99167A0CB1111";
   private static final String DEFAULT_VENDOR_ID = "D674CEC634764C5DBED594086FD461E8";
   private static final String SPEC2K_VENDOR_ID_1 = "F572CEC634764C5DBED594086FD461E9";
   private static final String SPEC2K_VENDOR_ID_2 = "A60694FEA9F54DE4B83DB48046EFF57C";
   private static final String UNAPPROVED_VENDOR = "5C6FB7F4DBF243F6BA47858CC9F7EEFE";
   private static final String VENDOR_NOT_ON_ORG_LIST = "259E31234A594B35B5EF4F632C57D0E9";
   private static final String INVALID_VENDOR_ID = "4343CEC634764C5DBED594086FD42121";
   private static final String INVALID_SPEC2K_VENDOR_ID = "0A1D6D2DE05C4F8EB32E3B09C3486EB0";
   private static final String NONSPEC2K_VENDOR_ID = "8C2E8BF443844A0B8C814C09CEE0DA4C";
   private static final String SHIP_TO_LOC_ID = "E1E7509A31464F729C9B765E532F451A";
   private static final String INVALID_SHIP_TO_LOC_ID = "DD22509A31464F729C9B765E532FEE33";
   private static final String CURRENCY_USD = "USD";
   private static final double EXCHANGE_RATE = 1.0;
   private static final String PART_NO_ID = "650F832E574311E7BB0189BEFD1F23D3";
   private static final String INVALID_PART_NO_ID = "BAEF832E574311E7BB0189BEFD1F8888";
   private static final BigDecimal QUANTITY = BigDecimal.ONE;
   private static final String UNIT_EACH = "EA";
   private static final String UNIT_BOX = "BOX";
   private static final String UNIT_ROLL = "ROLL";
   private static final String INVALID_ORDER_NUMBER = "XX123456";
   private static final String SPEC2K_CODE_CTIRE = "CTIRE";
   private static final String PO_LINE_PRICE_TYPE_CODE = "CNFRMD";
   private static final LineType PO_LINE_CODE = LineType.PURCHASE;
   private static final int PO_LINE_NO = 1;
   private static final int PO_LINE_NO_MISC = 2;
   private static final Boolean DELETED_BOOL = false;
   private static final String NOTE_TO_VENDOR = "Test Vendor Note";
   private static final String NOTE_TO_RECEIVER = "Test Receiver Note";
   private static final String SHIP_TO_CODE = "12345";
   private static final String EXT_REFERENCE = "Test Ext Reference 01";
   private static final String TERMS_AND_CONDITIONS_CODE = "NET90";
   private static final String INVALID_TERMS_AND_CONDITIONS_CODE = "No Terms";
   private static final String VENDOR_ACCOUNT_CODE = "TEST";
   private static final String MATERIAL_PLANNER_EXT_REF = "MP123";
   private static final BigDecimal VENDOR_PART_PRICE_MINORDERQTY = new BigDecimal( "2" );
   private static final int VENDOR_PART_PRICE_LEADTIME_DAYS = 3;
   private static final BigDecimal VENDOR_PART_PRICE_UNITPRICE = new BigDecimal( "59.99" );
   private static final String LINE_DESCRIPTION1 = "TEST_PART_1 (null)";
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
         new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
   private static final String STATUS_OPEN = "POOPEN";
   private static final String STATUS_AUTH = "POAUTH";
   private static final String STATUS_ISSUED = "POISSUED";
   private static final String STATUS_ACKNOWLEDGED = "POACKNOWLEDGED";
   private static final String STATUS_CANCEL = "POCANCEL";
   private static final String AUTH_STATUS_PENDING = "PENDING";
   private static final String AUTH_STATUS_REQUESTED = "REQUESTED";
   private static final String AUTH_STATUS_APPROVED = "APPROVED";
   private static final String AUTH_FLOW = "PURCHASE";
   private static Date sPROMISED_BY_DATE;

   private static final String PURCHASE_AGENT = "purchaseagent";

   private static final String UPDATE_ERROR =
         "No update occured. Purchase Order update only supports authorization,issue and acknowledge requests.";
   private static final String PART_NO_TO_UPDATE = "317D29C72FCD4FD79EEF8DE38455B53C";
   private static final String PROMISED_BY_DATE_TO_UPDATE = "2007-01-11T05:00:00Z";
   private static final BigDecimal QUANTITY_TO_UPDATE = new BigDecimal( "120" );
   private static final BigDecimal UNIT_PRICE_TO_UPDATE = new BigDecimal( "100" );
   private static final BigDecimal LINE_PRICE_TO_UPDATE = new BigDecimal( "12000" );
   private static final String VENDOR_NOTE_TO_UPDATE = "Updated Vendor Note";
   private static final String VENDOR_NOTE_UPDATE_MISC_LINE = "Updated Vendor Note for Line one";
   private static final String QUANTITY_UNIT_TO_UPDATE = "BOX";
   private static final String OWNER_ID = "850F832E574318A7BB0189BEFD1F2A8C";

   private static final String MISC_DESCRIPTION = "Misc Line";
   private static final LineType PO_LINE_CODE_MISC = LineType.MISC;
   private static final BigDecimal MISC_UNITPRICE = new BigDecimal( "60.12" );
   private static final BigDecimal MISC_LINEPRICE = new BigDecimal( "60.12" );
   private static final String FINANCE_ACCOUNT = "EXPENSE";
   private static final BigDecimal PO_LINE_PRICE = new BigDecimal( "119.98" );

   private static final String PO_ID = "ABCD1FF71A5D4C58B873A75D2F23EB99";
   private static final String PO_LINE_ID = "CDAF832E574311E7BB0189BEFD1F2951";
   private static final String PART_REQUEST_ID1 = "AAAA832E574311E7BB0189BEFD1F2951";
   private static final String PART_REQUEST_ID2 = "AAAA832E574311E7BB0189BEFD1F2952";
   private static final String NEEDED_QUANTITY = "1";
   private static final String WHERE_NEEDED_TO_CREATE = "GBLDOCK";
   private static final String NEEDED_BY_DATE = "2007-01-21T05:00:00Z";
   private static final String ORDER_NO = "P0100012";

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( PurchaseOrderResource.class ).to( PurchaseOrderResourceBean.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( PurchaseOrderLocalHome.class ).to( PurchaseOrderLocalHomeStub.class );
               bind( PoLineMpTableDao.class ).to( JdbcPoLineMpTableDao.class );
               bind( InvCndChgEventDao.class ).to( JdbcInvCndChgEventDao.class );
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
            }
         } );

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   @Inject
   private PurchaseOrderResourceBean iPurchaseOrderResourceBean;

   private PurchaseOrder iExpectedPurchaseOrder;


   @Before
   public void setUp() throws ParseException, MxException, AmApiBusinessException {

      InjectorContainer.get().injectMembers( this );
      iPurchaseOrderResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializePoTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

      sPROMISED_BY_DATE = SIMPLE_DATE_FORMAT.parse( "2017-08-23 18:00:00" );

      iExpectedPurchaseOrder = createPurchaseOrder( PURCHASE_ORDER_NUMBER );
   }


   @Test
   public void testPurchaseOrderGetSuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException, ParseException {
      PurchaseOrder lPurchaseOrder =
            iPurchaseOrderResourceBean.get( iExpectedPurchaseOrder.getId() );
      assertPurchaseOrderRetrieved( iExpectedPurchaseOrder, lPurchaseOrder );
   }


   @Test
   public void testPurchaseOrdersSearchByParamsSuccess() throws ParseException {
      List<PurchaseOrder> lResult = iPurchaseOrderResourceBean
            .search( getPurchaseOrderSearchParameters( iExpectedPurchaseOrder.getOrderNumber() ) );
      assertPurchaseOrderRetrieved( iExpectedPurchaseOrder, lResult.get( 0 ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPurchaseOrderByIdNotFound() throws AmApiResourceNotFoundException {

      // Call PO get method
      iPurchaseOrderResourceBean.get( PURCHASE_ORDER_ID_NOT_EXIST );

   }


   @Test
   public void testPurchaseOrdersSearchByInvalidParams() {

      List<PurchaseOrder> lResult = iPurchaseOrderResourceBean
            .search( getPurchaseOrderSearchParameters( INVALID_ORDER_NUMBER ) );

      Assert.assertEquals( 0, lResult.size() );

   }


   @Test
   public void testGetMultiplePartRequestsToPoLineSuccess() throws AmApiResourceNotFoundException {

      PurchaseOrder lPurchaseOrderToGet;
      try {
         lPurchaseOrderToGet = getPurchaseOrderWithPartRequestList();
         PurchaseOrder lPurchaseOrder = iPurchaseOrderResourceBean.get( PO_ID );

         List<PurchaseOrderLine> lPurchaseOrderLines = lPurchaseOrder.getPurchaseOrderLines();

         lPurchaseOrderLines.get( 0 )
               .setPartRequestIds( lPurchaseOrderLines.get( 0 ).getPartRequestIds().stream()
                     .sorted( ( o1, o2 ) -> o1.compareTo( o2 ) ).collect( Collectors.toList() ) );

         lPurchaseOrder.setPurchaseOrderLines( lPurchaseOrderLines );

         Assert.assertEquals( "Purchase Order with part request id lists in PO lines mismatched:",
               lPurchaseOrderToGet, lPurchaseOrder );
      } catch ( Exception e ) {
         fail( "Exception returned while getting the PO with part requests in PO lines" );
      }
   }


   @Test
   public void testCreatePurchaseOrderSuccess() throws AmApiBusinessException, ParseException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );
      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );

      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setPrice( lCreatedPurchaseOrder.getPurchaseOrderLines().get( 0 ).getPrice() );

      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder );
   }


   @Test
   public void testCreatePurchaseOrderSuccessWithVendorPartPriceAndMinimumFields()
         throws AmApiBusinessException, ParseException {

      PurchaseOrder lPurchaseOrder = getMinimumFieldsPurchaseOrder();
      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );

      // there is a vendor-part-price for the vendor and based on that we get:
      // unit price and quantity are taken directly from the vendor-part-price
      BigDecimal lExpectedUnitPrice = VENDOR_PART_PRICE_UNITPRICE;
      BigDecimal lExpectedQty = VENDOR_PART_PRICE_MINORDERQTY;
      // promised by date is calculated by taking the lead time from vendor-part-price and
      // adding it to the system date
      Calendar lExpectedPromisedByDate = Calendar.getInstance();
      lExpectedPromisedByDate.add( Calendar.SECOND,
            Math.round( ( float ) ( VENDOR_PART_PRICE_LEADTIME_DAYS * 24.0 * 60.0 * 60.0 ) ) );
      BigDecimal lExpectedPrice = PO_LINE_PRICE;

      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder, lExpectedUnitPrice,
            lExpectedQty, lExpectedPromisedByDate.getTime(), lExpectedPrice );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidPriceType()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setPriceType( "I_AM_INVALID" );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidOrderLineType()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      // Just change to repair order
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setLineTypeCode( null );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidQuantityUnitCode()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setQtyUnit( "I_AM_INVALID" );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidPartNumberId()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setPartNoId( INVALID_PART_NO_ID );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidPriority()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setReqPriority( "I_AM_INVALID" );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidCurrency()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setCurrencyCode( "I_AM_INVALID" );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidSpec2kCustomerCode()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setSpec2kCustomerCode( "I_AM_INVALID" );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidPurchaseContactId()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setPurchaseContactUserId( INVALID_USER_ID );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidReceiptOrganizationId()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setReceiptOrganizationId( INVALID_ORG_ID );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidVendorId()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setVendorId( INVALID_VENDOR_ID );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidShipToLocationId()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();

      lPurchaseOrder.setShipToLocationId( INVALID_SHIP_TO_LOC_ID );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidTermsAndConditions()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );

      lPurchaseOrder.setTermsAndConditionsCode( INVALID_TERMS_AND_CONDITIONS_CODE );
      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidVendorAccount()
         throws ParseException, AmApiBusinessException {
      PurchaseOrder lPurchaseOrder = getPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );
      lPurchaseOrder.setVendorAccountCode( "INVALID" );
      iPurchaseOrderResourceBean.create( lPurchaseOrder );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithDuplicatePONumber()
         throws ParseException, AmApiBusinessException {

      // create a PO
      createPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );

      // try to create another PO with the same Order number
      PurchaseOrder lPurchaseOrder = getPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );

   }


   @Test
   public void testCreatePurchaseOrderWithAlternatePurchasingUnitOfMeasure()
         throws ParseException, AmApiBusinessException {
      PurchaseOrder lPurchaseOrder = getPurchaseOrder();
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setQtyUnit( UNIT_BOX );

      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );

      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setPrice( lCreatedPurchaseOrder.getPurchaseOrderLines().get( 0 ).getPrice() );

      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithInvalidAlternatePurchasingUnitOfMeasure()
         throws ParseException, AmApiBusinessException {
      PurchaseOrder lPurchaseOrder = getPurchaseOrder();
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setQtyUnit( UNIT_ROLL );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithVendorNotApprovedForOrg()
         throws ParseException, AmApiBusinessException {
      PurchaseOrder lPurchaseOrder = getPurchaseOrder();
      lPurchaseOrder.setVendorId( UNAPPROVED_VENDOR );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithVendorNotOnOrgList()
         throws ParseException, AmApiBusinessException {
      PurchaseOrder lPurchaseOrder = getPurchaseOrder();
      lPurchaseOrder.setVendorId( VENDOR_NOT_ON_ORG_LIST );

      iPurchaseOrderResourceBean.create( lPurchaseOrder );
   }


   @Test
   public void testCreatePurchaseOrderWithSPEC2KVendorWithoutPurchaseCommand()
         throws ParseException, AmApiBusinessException {

      // a vendor is only considered to be truly "SPEC2K" for purchase orders if
      // (1) it has the SPEC2K boolean = true AND
      // (2) it has the S1BOOKED SPEC2K command
      PurchaseOrder lPurchaseOrder = getSPEC2KPurchaseOrder( INVALID_SPEC2K_VENDOR_ID );
      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );

      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setPrice( lCreatedPurchaseOrder.getPurchaseOrderLines().get( 0 ).getPrice() );

      // PO should be created as if the vendor was not SPEC2K
      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder );

   }


   @Test
   public void testCreatePurchaseOrderWithMPExtRef() throws AmApiBusinessException, ParseException {

      // create a PO with material planning external reference field
      PurchaseOrder lPurchaseOrder = getPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );
      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setPrice( lCreatedPurchaseOrder.getPurchaseOrderLines().get( 0 ).getPrice() );

      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder );
   }


   @Test( expected = AmApiBusinessException.class )
   public void testCreatePurchaseOrderWithDuplicateMPExtRef()
         throws AmApiBusinessException, ParseException {

      // create a PO with a material planning external reference
      PurchaseOrder lPurchaseOrder = getPurchaseOrder( PURCHASE_ORDER_NUMBER_NEW );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );
      try {
         iPurchaseOrderResourceBean.create( lPurchaseOrder );
      } catch ( Exception e ) {
         Assert.fail( "Test failed due to an unknown exception: " + e );
      }

      // try to create another PO with the same material planning external reference
      PurchaseOrder lDuplicatePurchaseOrder = getPurchaseOrder();
      lDuplicatePurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setMaterialPlannerExternalReference( MATERIAL_PLANNER_EXT_REF );
      iPurchaseOrderResourceBean.create( lDuplicatePurchaseOrder );
   }


   @Test
   public void testCreatePurchaseOrderWithSPEC2KVendorWithVendorPartPrice()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getSPEC2KPurchaseOrder( SPEC2K_VENDOR_ID_1 );
      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );

      // there is a vendor-part-price for the SPEC2K vendor and based on that we get:
      // unit price and quantity are taken directly from the vendor-part-price
      BigDecimal lExpectedUnitPrice = VENDOR_PART_PRICE_UNITPRICE;
      BigDecimal lExpectedQty = VENDOR_PART_PRICE_MINORDERQTY;
      // promised by date is calculated by taking the lead time from vendor-part-price and
      // adding it to the system date
      Calendar lExpectedPromisedByDate = Calendar.getInstance();
      lExpectedPromisedByDate.add( Calendar.SECOND,
            Math.round( ( float ) ( VENDOR_PART_PRICE_LEADTIME_DAYS * 24.0 * 60.0 * 60.0 ) ) );
      BigDecimal lExpectedPrice = PO_LINE_PRICE;

      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder, lExpectedUnitPrice,
            lExpectedQty, lExpectedPromisedByDate.getTime(), lExpectedPrice );
   }


   @Test
   public void testCreatePurchaseOrderWithSPEC2KVendorWithoutVendorPartPrice()
         throws ParseException, AmApiBusinessException {

      PurchaseOrder lPurchaseOrder = getSPEC2KPurchaseOrder( SPEC2K_VENDOR_ID_2 );
      PurchaseOrder lCreatedPurchaseOrder = iPurchaseOrderResourceBean.create( lPurchaseOrder );

      // there is no vendor-part-price for the SPEC2K vendor, so we get the following:
      // unit price will default to zero
      BigDecimal lExpectedUnitPrice = BigDecimal.ZERO;
      // quantity should be the value passed in
      BigDecimal lExpectedQty = lPurchaseOrder.getPurchaseOrderLines().get( 0 ).getQuantity();
      // promised by date will be defaulted to the system date
      GregorianCalendar lExpectedPromisedByDate = new GregorianCalendar();
      lPurchaseOrder.getPurchaseOrderLines().get( 0 )
            .setPromisedByDate( lExpectedPromisedByDate.getTime() );
      BigDecimal lExpectedPrice = BigDecimal.ZERO;

      assertPurchaseOrderCreated( lPurchaseOrder, lCreatedPurchaseOrder, lExpectedUnitPrice,
            lExpectedQty, lExpectedPromisedByDate.getTime(), lExpectedPrice );
   }


   @Test
   public void testUpdatePOLinesSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            purchaseOrderToUpdate );
      purchaseOrderToUpdate.getPurchaseOrderLines().get( 0 ).setPrice( LINE_PRICE_TO_UPDATE );
      assertTrue( updatedPO.getPurchaseOrderLines()
            .equals( purchaseOrderToUpdate.getPurchaseOrderLines() ) );
   }


   @Test
   public void testUpdatePOFromOpenToCancelSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();
      purchaseOrderToUpdate.setStatus( STATUS_CANCEL );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            purchaseOrderToUpdate );

      // Assert the new PO status
      Assert.assertEquals( updatedPO.getStatus(), STATUS_CANCEL );

   }


   @Test
   public void testUpdatePOFromAuthToCancelSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );

      // Authorize the PO
      iExpectedPurchaseOrder.setStatus( STATUS_AUTH );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );
      Assert.assertEquals( updatedPO.getStatus(), STATUS_AUTH );

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();
      purchaseOrderToUpdate.setStatus( STATUS_CANCEL );
      updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            purchaseOrderToUpdate );

      // Assert the new PO status
      Assert.assertEquals( updatedPO.getStatus(), STATUS_CANCEL );

   }


   @Test
   public void testUpdatePOFromIssuedToCancelSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );

      // Authorize the PO
      iExpectedPurchaseOrder.setStatus( STATUS_AUTH );
      iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), iExpectedPurchaseOrder );

      // Issue the PO
      iExpectedPurchaseOrder.setStatus( STATUS_ISSUED );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );
      Assert.assertEquals( updatedPO.getStatus(), STATUS_ISSUED );

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();
      purchaseOrderToUpdate.setStatus( STATUS_CANCEL );
      updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            purchaseOrderToUpdate );

      // Assert the new PO status
      Assert.assertEquals( updatedPO.getStatus(), STATUS_CANCEL );

   }


   @Test
   public void testUpdatePOFromAcknowledgeToCancelSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );

      // Set the PO to POAUTH state
      iExpectedPurchaseOrder.setAuthStatus( AUTH_STATUS_REQUESTED );
      iExpectedPurchaseOrder.setPurchaseOrderLines( null );
      iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), iExpectedPurchaseOrder );

      // Set the PO to issued state
      iExpectedPurchaseOrder.setStatus( STATUS_ISSUED );
      iExpectedPurchaseOrder.setPurchaseOrderLines( null );
      iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), iExpectedPurchaseOrder );

      // Set the PO to acknowledged state
      iExpectedPurchaseOrder.setStatus( STATUS_ACKNOWLEDGED );
      iExpectedPurchaseOrder.setPurchaseOrderLines( null );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );
      Assert.assertEquals( updatedPO.getStatus(), STATUS_ACKNOWLEDGED );

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();
      purchaseOrderToUpdate.setStatus( STATUS_CANCEL );
      updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            purchaseOrderToUpdate );

      // Assert the new PO status
      Assert.assertEquals( updatedPO.getStatus(), STATUS_CANCEL );

   }


   @Test
   public void testUpdatePOLinesPOLineIdNotFound()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();
      purchaseOrderToUpdate.getPurchaseOrderLines().get( 0 ).setId( null );
      try {
         iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), purchaseOrderToUpdate );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "The PO Line with ID "
               + purchaseOrderToUpdate.getPurchaseOrderLines().get( 0 ).getId()
               + " could not be found in Maintenix ", exception.getMessage() );
      }
   }


   @Test
   public void testUpdatePoAuthorizationRequestWithInsufficientAuthorization()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      iExpectedPurchaseOrder.setAuthStatus( AUTH_STATUS_REQUESTED );

      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );

      Assert.assertEquals( STATUS_OPEN, updatedPO.getStatus() );
      Assert.assertEquals( AUTH_STATUS_REQUESTED, updatedPO.getAuthStatus() );
   }


   @Test
   public void testUpdatePoAuthorizationRequestWithSufficientAuthorization()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      iExpectedPurchaseOrder.setAuthStatus( AUTH_STATUS_REQUESTED );

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );

      Assert.assertEquals( STATUS_AUTH, updatedPO.getStatus() );
      Assert.assertEquals( AUTH_STATUS_APPROVED, updatedPO.getAuthStatus() );
   }


   @Test
   public void testUpdatePoFromAuthToIssued()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );

      // Set the PO in the desired state. [POAUTH]
      iExpectedPurchaseOrder.setAuthStatus( AUTH_STATUS_REQUESTED );
      iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), iExpectedPurchaseOrder );

      iExpectedPurchaseOrder.setStatus( STATUS_ISSUED );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );

      Assert.assertEquals( updatedPO.getStatus(), STATUS_ISSUED );
      Assert.assertEquals( updatedPO.getAuthStatus(), AUTH_STATUS_APPROVED );
   }


   @Test
   public void testUpdatePoFromIssuedToAcknowledged()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );

      // Set the PO to POAUTH state
      iExpectedPurchaseOrder.setAuthStatus( AUTH_STATUS_REQUESTED );
      iExpectedPurchaseOrder.setPurchaseOrderLines( null );
      iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), iExpectedPurchaseOrder );

      // Set the PO to issued state
      iExpectedPurchaseOrder.setStatus( STATUS_ISSUED );
      iExpectedPurchaseOrder.setPurchaseOrderLines( null );
      iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(), iExpectedPurchaseOrder );

      iExpectedPurchaseOrder.setStatus( STATUS_ACKNOWLEDGED );
      iExpectedPurchaseOrder.setPurchaseOrderLines( null );
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );

      Assert.assertEquals( updatedPO.getStatus(), STATUS_ACKNOWLEDGED );
   }


   @Test
   public void testUpdatePoFromOpenToIssuedWithSufficientAuthorization()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      iExpectedPurchaseOrder.setStatus( STATUS_ISSUED );
      iExpectedPurchaseOrder.setAuthStatus( AUTH_STATUS_REQUESTED );

      Mockito.when( iPrincipal.getName() ).thenReturn( PURCHASE_AGENT );

      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            iExpectedPurchaseOrder );

      Assert.assertEquals( updatedPO.getStatus(), STATUS_ISSUED );
      Assert.assertEquals( updatedPO.getAuthStatus(), AUTH_STATUS_APPROVED );
   }


   @Test
   public void testUpdatePoWithOtherStatuses()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      iExpectedPurchaseOrder.setStatus( "OTHER STATUS" );

      try {
         iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
               iExpectedPurchaseOrder );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( e.getMessage(), UPDATE_ERROR );
      }

   }


   @Test
   public void testUpdatePoWithOtherFieldChanges()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      iExpectedPurchaseOrder.setShipToCode( "RANDOM STRING" );

      try {
         iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
               iExpectedPurchaseOrder );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( e.getMessage(), UPDATE_ERROR );
      }
   }


   @Test
   public void testCreateMiscellaneousPOLineSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      PurchaseOrder purchaseOrderToUpdate = getPurchaseOrderToUpdate();

      PurchaseOrderLine lPOLine = getMiscPoLine();
      purchaseOrderToUpdate.addPurchaseOrderLine( lPOLine );

      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            purchaseOrderToUpdate );
      boolean check = false;
      for ( PurchaseOrderLine lPurchaseOrderLine : purchaseOrderToUpdate.getPurchaseOrderLines() ) {
         for ( PurchaseOrderLine lPurchaseOrderLineActual : updatedPO.getPurchaseOrderLines() ) {
            if ( lPurchaseOrderLine.getLineNo() == 2
                  && lPurchaseOrderLineActual.getLineNo() == 2 ) {
               lPurchaseOrderLine.setId( lPurchaseOrderLineActual.getId() );
               lPurchaseOrderLine.setPrice( MISC_LINEPRICE );
               Assert.assertEquals( lPurchaseOrderLine, lPurchaseOrderLineActual );
               check = true;
            }
         }
      }
      if ( !check ) {
         Assert.fail( "Assertion Failure: Miscellaneous Line with Line number 2 couldn't be find" );
      }
   }


   // In order to verify another new Misc line is not added while updating a PO with Misc line
   @Test
   public void testUpdatePoWithMiscellaneousPOLineSuccess()
         throws ParseException, AmApiBusinessException, AmApiResourceNotFoundException {

      PurchaseOrder purchaseOrderToUpdate = new PurchaseOrder();
      purchaseOrderToUpdate.setId( iExpectedPurchaseOrder.getId() );
      PurchaseOrderLine lMiscPOLine = getMiscPoLine();
      purchaseOrderToUpdate.addPurchaseOrderLine( lMiscPOLine );

      PurchaseOrder updatedPOWithMiscLine = iPurchaseOrderResourceBean
            .update( iExpectedPurchaseOrder.getId(), purchaseOrderToUpdate );
      for ( PurchaseOrderLine purchaseOrderLine : updatedPOWithMiscLine.getPurchaseOrderLines() ) {
         if ( purchaseOrderLine.getLineNo() == PO_LINE_NO ) {
            purchaseOrderLine.setVendorNote( VENDOR_NOTE_UPDATE_MISC_LINE );
         }
      }
      PurchaseOrder updatedPO = iPurchaseOrderResourceBean.update( iExpectedPurchaseOrder.getId(),
            updatedPOWithMiscLine );
      Assert.assertEquals( "Number of Purchase Order Lines mismatched",
            updatedPOWithMiscLine.getPurchaseOrderLines().size(),
            updatedPO.getPurchaseOrderLines().size() );
      for ( PurchaseOrderLine purchaseOrder : updatedPO.getPurchaseOrderLines() ) {
         if ( purchaseOrder.getLineNo() == PO_LINE_NO ) {
            Assert.assertEquals( "Assertion Fail: Vendor note is not get updated",
                  purchaseOrder.getVendorNote(), purchaseOrder.getVendorNote() );
         }
      }
      for ( PurchaseOrderLine poLine : updatedPOWithMiscLine.getPurchaseOrderLines() ) {
         Optional<PurchaseOrderLine> purchaseOrderLine = updatedPO.getPurchaseOrderLines().stream()
               .filter( o -> Integer.toString( o.getLineNo() )
                     .equals( Integer.toString( poLine.getLineNo() ) ) )
               .findFirst();
         assertTrue( "PurchaseOrderLines mismatched", purchaseOrderLine.isPresent() );
      }
   }


   /**
    * Get Miscellaneous Lines
    *
    * @return lPOLine
    */
   private PurchaseOrderLine getMiscPoLine() {

      PurchaseOrderLine lPOLine = new PurchaseOrderLine();
      lPOLine.setLineDescription( MISC_DESCRIPTION );
      lPOLine.setPromisedByDate( sPROMISED_BY_DATE );
      lPOLine.setQuantity( QUANTITY );
      lPOLine.setQtyUnit( UNIT_EACH );
      lPOLine.setLineNo( PO_LINE_NO_MISC );
      lPOLine.setLineTypeCode( PO_LINE_CODE_MISC );
      lPOLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );
      lPOLine.setOwnerId( OWNER_ID );
      lPOLine.setVendorNote( NOTE_TO_VENDOR );
      lPOLine.setReceiverNote( NOTE_TO_RECEIVER );
      lPOLine.setUnitPrice( MISC_UNITPRICE );
      lPOLine.setFinanceAccountCode( FINANCE_ACCOUNT );
      lPOLine.setPartRequestIds( new ArrayList<String>() );
      return lPOLine;
   }


   private PurchaseOrder getPurchaseOrder( String aOrderNumber ) throws ParseException {

      PurchaseOrder lPurchaseOrder = new PurchaseOrder();
      lPurchaseOrder.setOrderNumber( aOrderNumber );
      lPurchaseOrder.setReqPriority( PRIORITY_NORMAL );
      lPurchaseOrder.setPurchaseContactUserId( USER_ID );
      lPurchaseOrder.setReceiptOrganizationId( ORG_ID );
      lPurchaseOrder.setVendorId( DEFAULT_VENDOR_ID );
      lPurchaseOrder.setShipToLocationId( SHIP_TO_LOC_ID );
      lPurchaseOrder.setCurrencyCode( CURRENCY_USD );
      lPurchaseOrder.setExchangeRate( new BigDecimal( EXCHANGE_RATE ) );
      lPurchaseOrder.setShipToCode( SHIP_TO_CODE );
      lPurchaseOrder.setNoteToReceiver( NOTE_TO_RECEIVER );
      lPurchaseOrder.setNoteToVendor( NOTE_TO_VENDOR );
      lPurchaseOrder.setOrderExternalReference( EXT_REFERENCE );
      lPurchaseOrder.setStatus( STATUS_OPEN );
      lPurchaseOrder.setAuthStatus( AUTH_STATUS_PENDING );
      lPurchaseOrder.setTermsAndConditionsCode( TERMS_AND_CONDITIONS_CODE );
      lPurchaseOrder.setVendorAccountCode( VENDOR_ACCOUNT_CODE );
      lPurchaseOrder.setPoAuthFlowCd( AUTH_FLOW );

      // set order line
      PurchaseOrderLine lPOLine = new PurchaseOrderLine();
      lPOLine.setPartNoId( PART_NO_ID );
      lPOLine.setPromisedByDate( sPROMISED_BY_DATE );
      lPOLine.setQuantity( QUANTITY );
      lPOLine.setQtyUnit( UNIT_EACH );
      lPOLine.setDeleted( DELETED_BOOL );
      lPOLine.setLineNo( PO_LINE_NO );
      lPOLine.setLineTypeCode( PO_LINE_CODE );
      lPOLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );
      lPOLine.setLineDescription( LINE_DESCRIPTION1 );
      lPOLine.setFinanceAccountCode( FINANCE_ACCOUNT );
      lPOLine.setOwnerId( OWNER_ID );
      lPOLine.setPartRequestIds( new ArrayList<String>() );

      lPurchaseOrder.addPurchaseOrderLine( lPOLine );

      return lPurchaseOrder;
   }


   private PurchaseOrder getPurchaseOrder() throws ParseException {
      // order number will be generated if left null
      return getPurchaseOrder( null );
   }


   private PurchaseOrder getMinimumFieldsPurchaseOrder() throws ParseException {

      PurchaseOrder lPurchaseOrder = new PurchaseOrder();
      lPurchaseOrder.setReqPriority( PRIORITY_NORMAL );
      lPurchaseOrder.setPurchaseContactUserId( USER_ID );
      lPurchaseOrder.setReceiptOrganizationId( ORG_ID );
      lPurchaseOrder.setVendorId( NONSPEC2K_VENDOR_ID );
      lPurchaseOrder.setShipToLocationId( SHIP_TO_LOC_ID );
      lPurchaseOrder.setCurrencyCode( CURRENCY_USD );
      lPurchaseOrder.setExchangeRate( new BigDecimal( EXCHANGE_RATE ) );
      lPurchaseOrder.setStatus( STATUS_OPEN );
      lPurchaseOrder.setAuthStatus( AUTH_STATUS_PENDING );
      lPurchaseOrder.setPoAuthFlowCd( AUTH_FLOW );

      // set order line
      PurchaseOrderLine lPOLine = new PurchaseOrderLine();
      lPOLine.setPartNoId( PART_NO_ID );
      lPOLine.setQtyUnit( UNIT_EACH );
      lPOLine.setLineNo( PO_LINE_NO );
      lPOLine.setLineTypeCode( PO_LINE_CODE );
      lPOLine.setFinanceAccountCode( FINANCE_ACCOUNT );
      lPOLine.setOwnerId( OWNER_ID );
      lPOLine.setLineDescription( LINE_DESCRIPTION1 );
      lPOLine.setPartRequestIds( new ArrayList<String>() );
      lPurchaseOrder.addPurchaseOrderLine( lPOLine );

      return lPurchaseOrder;
   }


   private PurchaseOrder getSPEC2KPurchaseOrder( String aVendorId ) throws ParseException {

      PurchaseOrder lPurchaseOrder = new PurchaseOrder();
      lPurchaseOrder.setReqPriority( PRIORITY_NORMAL );
      lPurchaseOrder.setPurchaseContactUserId( USER_ID );
      lPurchaseOrder.setReceiptOrganizationId( ORG_ID );
      lPurchaseOrder.setShipToLocationId( SHIP_TO_LOC_ID );
      lPurchaseOrder.setCurrencyCode( CURRENCY_USD );
      lPurchaseOrder.setExchangeRate( new BigDecimal( EXCHANGE_RATE ) );
      lPurchaseOrder.setShipToCode( SHIP_TO_CODE );
      lPurchaseOrder.setStatus( STATUS_OPEN );
      lPurchaseOrder.setAuthStatus( AUTH_STATUS_PENDING );
      lPurchaseOrder.setPoAuthFlowCd( AUTH_FLOW );

      // set SPEC2K vendor and set SPEC2K customer code
      lPurchaseOrder.setVendorId( aVendorId );
      lPurchaseOrder.setSpec2kCustomerCode( SPEC2K_CODE_CTIRE );

      // set order line
      PurchaseOrderLine lPOLine = new PurchaseOrderLine();
      lPOLine.setPartNoId( PART_NO_ID );
      lPOLine.setQuantity( QUANTITY );
      lPOLine.setQtyUnit( UNIT_EACH );
      lPOLine.setLineNo( PO_LINE_NO );
      lPOLine.setLineTypeCode( PO_LINE_CODE );
      lPOLine.setFinanceAccountCode( FINANCE_ACCOUNT );
      lPOLine.setOwnerId( OWNER_ID );
      lPOLine.setLineDescription( LINE_DESCRIPTION1 );
      lPOLine.setPartRequestIds( new ArrayList<String>() );

      lPurchaseOrder.addPurchaseOrderLine( lPOLine );

      return lPurchaseOrder;
   }


   /**
    * Create a PO for testing
    *
    * @param aPurchaseOrderNumber
    * @return the created PO
    * @throws AmApiBusinessException
    * @throws ParseException
    */
   private PurchaseOrder createPurchaseOrder( String aPurchaseOrderNumber )
         throws AmApiBusinessException, ParseException {
      PurchaseOrder lPurchaseOrder = getPurchaseOrder( aPurchaseOrderNumber );
      return iPurchaseOrderResourceBean.create( lPurchaseOrder );
   }


   /**
    * Assert the retrieved PO is as expected.
    *
    * @param aExpectedPurchaseOrder
    * @param aActualPurchaseOrder
    */
   private void assertPurchaseOrderRetrieved( PurchaseOrder aExpectedPurchaseOrder,
         PurchaseOrder aActualPurchaseOrder ) {
      Assert.assertEquals( aExpectedPurchaseOrder, aActualPurchaseOrder );
   }


   /**
    * Assert the created PO is as expected.
    *
    * @param aExpectedPurchaseOrder
    * @param aActualPurchaseOrder
    */
   private void assertPurchaseOrderCreated( PurchaseOrder aExpectedPurchaseOrder,
         PurchaseOrder aActualPurchaseOrder ) {
      // ID is generated so need to update the original PO with this field for it to match
      aExpectedPurchaseOrder.setId( aActualPurchaseOrder.getId() );

      if ( aExpectedPurchaseOrder.getOrderNumber() == null ) {
         // order number was also generated
         aExpectedPurchaseOrder.setOrderNumber( aActualPurchaseOrder.getOrderNumber() );
      }

      // do the same for the lines
      Assert.assertEquals( aExpectedPurchaseOrder.getPurchaseOrderLines().size(),
            aActualPurchaseOrder.getPurchaseOrderLines().size() );
      for ( PurchaseOrderLine lExpectedPOLine : aExpectedPurchaseOrder.getPurchaseOrderLines() ) {
         PurchaseOrderLine lActualPOLine = ( PurchaseOrderLine ) CollectionUtils.find(
               aActualPurchaseOrder.getPurchaseOrderLines(),
               p -> ( ( PurchaseOrderLine ) p ).getLineNo() == lExpectedPOLine.getLineNo() );
         lExpectedPOLine.setId( lActualPOLine.getId() );

         // if promised by date is not set, it will default to the system date
         if ( lExpectedPOLine.getPromisedByDate() == null ) {
            lExpectedPOLine.setPromisedByDate( Calendar.getInstance().getTime() );
         }

         // we currently don't set unit price in the PO API
         // so we always expect to get back a unit price of 0
         lExpectedPOLine.setUnitPrice( BigDecimal.ZERO );

      }
      Assert.assertEquals( aExpectedPurchaseOrder, aActualPurchaseOrder );
   }


   /**
    * Assert the created PO is as expected with the given unit price, quantity and promised by date.
    *
    * @param aExpectedPurchaseOrder
    * @param aActualPurchaseOrder
    */
   private void assertPurchaseOrderCreated( PurchaseOrder aExpectedPurchaseOrder,
         PurchaseOrder aActualPurchaseOrder, BigDecimal aUnitPrice, BigDecimal aQuantity,
         Date aPromisedByDate, BigDecimal aExpectedPrice ) {
      // ID is generated so need to update the original PO with this field for it to match
      aExpectedPurchaseOrder.setId( aActualPurchaseOrder.getId() );

      if ( aExpectedPurchaseOrder.getOrderNumber() == null ) {
         // order number was also generated
         aExpectedPurchaseOrder.setOrderNumber( aActualPurchaseOrder.getOrderNumber() );
      }

      // do the same for the lines
      Assert.assertEquals( aExpectedPurchaseOrder.getPurchaseOrderLines().size(),
            aActualPurchaseOrder.getPurchaseOrderLines().size() );
      for ( PurchaseOrderLine lExpectedPOLine : aExpectedPurchaseOrder.getPurchaseOrderLines() ) {
         PurchaseOrderLine lActualPOLine = ( PurchaseOrderLine ) CollectionUtils.find(
               aActualPurchaseOrder.getPurchaseOrderLines(),
               p -> ( ( PurchaseOrderLine ) p ).getLineNo() == lExpectedPOLine.getLineNo() );
         lExpectedPOLine.setId( lActualPOLine.getId() );
         lExpectedPOLine.setUnitPrice( aUnitPrice );
         lExpectedPOLine.setQuantity( aQuantity );
         lExpectedPOLine.setPromisedByDate( aPromisedByDate );
         lExpectedPOLine.setPrice( aExpectedPrice );
      }

      Assert.assertEquals( aExpectedPurchaseOrder, aActualPurchaseOrder );
   }


   private PurchaseOrderSearchParameters getPurchaseOrderSearchParameters( String aOrderNumber ) {
      PurchaseOrderSearchParameters lPurchaseOrderSearchParameters =
            new PurchaseOrderSearchParameters();
      lPurchaseOrderSearchParameters.setOrderNumber( aOrderNumber );

      return lPurchaseOrderSearchParameters;
   }


   private PurchaseOrder getPurchaseOrderToUpdate() throws ParseException {
      PurchaseOrder purchaseOrderToUpdate = new PurchaseOrder();
      List<PurchaseOrderLine> purchaseOrderLineList = new ArrayList<PurchaseOrderLine>();
      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();

      if ( CollectionUtils.isNotEmpty( iExpectedPurchaseOrder.getPurchaseOrderLines() ) ) {
         purchaseOrderLine.setId( iExpectedPurchaseOrder.getPurchaseOrderLines().get( 0 ).getId() );
         purchaseOrderLine.setPartNoId( PART_NO_TO_UPDATE );
         Date promisedByDateToUpdate =
               new SimpleDateFormat( "yyyy-MM-dd" ).parse( PROMISED_BY_DATE_TO_UPDATE );
         purchaseOrderLine.setPromisedByDate( promisedByDateToUpdate );
         purchaseOrderLine.setQuantity( QUANTITY_TO_UPDATE );
         purchaseOrderLine.setUnitPrice( UNIT_PRICE_TO_UPDATE );
         purchaseOrderLine.setVendorNote( VENDOR_NOTE_TO_UPDATE );
         purchaseOrderLine.setQtyUnit( QUANTITY_UNIT_TO_UPDATE );
         purchaseOrderLine.setLineTypeCode( PO_LINE_CODE );
         purchaseOrderLine.setLineNo( PO_LINE_NO );
         purchaseOrderLine.setFinanceAccountCode( FINANCE_ACCOUNT );
         purchaseOrderLine.setOwnerId( OWNER_ID );
         purchaseOrderLine.setPartRequestIds( new ArrayList<String>() );
         purchaseOrderLineList.add( purchaseOrderLine );
      }

      purchaseOrderToUpdate.setId( iExpectedPurchaseOrder.getId() );
      purchaseOrderToUpdate.setPurchaseOrderLines( purchaseOrderLineList );

      return purchaseOrderToUpdate;
   }


   private PurchaseOrder getPurchaseOrderWithPartRequestList() throws ParseException {

      PurchaseOrder lPurchaseOrder = getPurchaseOrder();
      lPurchaseOrder.setId( PO_ID );
      lPurchaseOrder.setStatus( null );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setId( PO_LINE_ID );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setOwnerId( null );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setLineDescription( null );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setFinanceAccountCode( null );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setPrice( new BigDecimal( "0" ) );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setUnitPrice( new BigDecimal( "0" ) );
      lPurchaseOrder.setSpec2kCustomerCode( SPEC2K_CODE_CTIRE );
      lPurchaseOrder.setOrderNumber( ORDER_NO );
      lPurchaseOrder.setPoAuthFlowCd( null );

      List<String> lPartRequestIds = new ArrayList<String>();
      lPartRequestIds.add( PART_REQUEST_ID1 );
      lPartRequestIds.add( PART_REQUEST_ID2 );
      lPurchaseOrder.getPurchaseOrderLines().get( 0 ).setPartRequestIds( lPartRequestIds );
      return lPurchaseOrder;
   }


   private void initializePoTest() throws MxException {

      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      iUnauthorizedSecurityContext = new SecurityContext() {

         @Override
         public Principal getUserPrincipal() {
            return new UserPrincipal( getUnauthorizedUser() );
         }


         @Override
         public boolean isUserInRole( String role ) {
            return false;
         }


         @Override
         public boolean isSecure() {
            return false;
         }


         @Override
         public String getAuthenticationScheme() {
            return null;
         }
      };

      iAuthorizedSecurityContext = new SecurityContext() {

         @Override
         public Principal getUserPrincipal() {
            return new UserPrincipal( getAuthorizedUser() );
         }


         @Override
         public boolean isUserInRole( String role ) {
            return false;
         }


         @Override
         public boolean isSecure() {
            return false;
         }


         @Override
         public String getAuthenticationScheme() {
            return null;
         }
      };

      // Purchase Agent Security Context
      new SecurityContext() {

         @Override
         public Principal getUserPrincipal() {
            return new UserPrincipal( PURCHASE_AGENT );
         }


         @Override
         public boolean isUserInRole( String role ) {
            return false;
         }


         @Override
         public boolean isSecure() {
            return false;
         }


         @Override
         public String getAuthenticationScheme() {
            return null;
         }
      };

      int lAuthorizedUserId = new UserService().find( getAuthorizedUser() ).getId();
      UserParameters.setInstance( lAuthorizedUserId, "SECURED_RESOURCE",
            new UserParametersFake( lAuthorizedUserId, "SECURED_RESOURCE" ) );

      int lUnauthorizedUserId = new UserService().find( getUnauthorizedUser() ).getId();
      UserParameters.setInstance( lUnauthorizedUserId, "SECURED_RESOURCE",
            new UserParametersFake( lUnauthorizedUserId, "SECURED_RESOURCE" ) );

      int lPurchaseAgentUserId = new UserService().find( PURCHASE_AGENT ).getId();
      UserParameters.setInstance( lPurchaseAgentUserId, "SECURED_RESOURCE",
            new UserParametersFake( lPurchaseAgentUserId, "SECURED_RESOURCE" ) );

      EjbFactory.setSingleton( new EjbFactoryStub(
            new SecurityIdentityStub( getAuthorizedUser(), 2 ), new DAOLocalStub() ) );

   }
}
