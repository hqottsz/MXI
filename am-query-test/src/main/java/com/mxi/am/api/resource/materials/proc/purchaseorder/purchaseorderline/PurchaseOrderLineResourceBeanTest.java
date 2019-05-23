package com.mxi.am.api.resource.materials.proc.purchaseorder.purchaseorderline;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine.LineType;
import com.mxi.am.api.resource.materials.proc.purchaseorder.purchaseorderline.impl.PurchaseOrderLineResourceBean;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for purchase order line api
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PurchaseOrderLineResourceBeanTest extends ResourceBeanTest {

   private static final String PURCHASE_ORDER_ID = "ABCD1FF71A5D4C58B873A75D2F23EB99";
   private static final String PURCHASE_ORDER_ID_NOT_EXIST = "ABCD1FF71A5D4C58B873A75D2F237777";
   private static final String PURCHASE_ORDER_LINE_ID = "CDAF832E574311E7BB0189BEFD1F2951";
   private static final String PURCHASE_ORDER_LINE_ID_NOT_EXIST =
         "CDAF832E574311E7BB0189BEFD155555";
   private static final String PURCHASE_ORDER_LINE_INVALID_UUID_FORMAT =
         "CDAF832E574311E7BB0189BEFD1555#*";
   private static final String PART_NO_ID1 = "650F832E574311E7BB0189BEFD1F23D3";
   private static final String PART_NO_ID2 = "317D29C72FCD4FD79EEF8DE38455B53C";
   private static final String PART_NO_ID_NOT_EXIST = "317D29C72FCD4FD79EEF8DE384557891";
   private static final BigDecimal QUANTITY = BigDecimal.ONE;
   private static final BigDecimal UNIT_PRICE = BigDecimal.TEN;
   private static final BigDecimal PRICE = BigDecimal.TEN;
   private static final String UNIT_EACH = "EA";
   private static final String UNIT_EACH_NOT_EXIST = "EB";
   private static final String INVALID_UNIT_FOR_PART = "BOX";
   private static final String PO_LINE_PRICE_TYPE_CODE = "CNFRMD";
   private static final String PO_LINE_INVALID_PRICE_TYPE_CODE = "CNFRM";
   private static final LineType PO_LINE_CODE = LineType.PURCHASE;
   private static final LineType PO_LINE_CODE_EXCHANGE = LineType.EXCHANGE;
   private static final int PO_LINE_NO1 = 1;
   private static final int PO_LINE_NO2 = 2;
   private static final Boolean DELETED_BOOL = false;
   private static final String LINE_DESCRIPTION1 = "TEST_PART_1 (null)";
   private static final String LINE_DESCRIPTION2 = "TEST_PART_2 (null)";
   private static final String OWNER_ID = "850F832E574318A7BB0189BEFD1F2A8C";
   private static final String FINANCE_ACCOUNT = "EXPENSE";
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
         new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
   private static final String DATE_STRING = "2017-08-23 18:00:00";
   private Date promisedByDate;

   private static final boolean DELETED_BOOL_TO_UPDATE = true;

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Inject
   private PurchaseOrderLineResourceBean purchaseOrderLineResourceBean;


   @Before
   public void setUp() throws ParseException {

      InjectorContainer.get().injectMembers( this );
      purchaseOrderLineResourceBean.setEJBContext( ejbContext );

      initializeDataLoader();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      promisedByDate = SIMPLE_DATE_FORMAT.parse( DATE_STRING );
   }


   @Test
   public void get_success() throws AmApiResourceNotFoundException {
      PurchaseOrderLine purchaseOrderLine =
            purchaseOrderLineResourceBean.get( PURCHASE_ORDER_LINE_ID );
      Assert.assertEquals( getPurchaseOrderLine(), purchaseOrderLine );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_invalidId() throws AmApiResourceNotFoundException {

      purchaseOrderLineResourceBean.get( PURCHASE_ORDER_LINE_ID_NOT_EXIST );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_invalUuidIdFormat() throws AmApiResourceNotFoundException {

      purchaseOrderLineResourceBean.get( PURCHASE_ORDER_LINE_INVALID_UUID_FORMAT );
   }


   @Test
   public void create_success() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();

      PurchaseOrderLine purchaseOrderLineActual =
            purchaseOrderLineResourceBean.create( purchaseOrderLine );

      purchaseOrderLine.setId( purchaseOrderLineActual.getId() );
      purchaseOrderLine.setUnitPrice( purchaseOrderLineActual.getUnitPrice() );
      purchaseOrderLine.setPrice( purchaseOrderLineActual.getPrice() );

      Assert.assertEquals( purchaseOrderLine, purchaseOrderLineActual );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_nullPayload() {

      purchaseOrderLineResourceBean.create( null );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_nullPOId() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setPurchaseOrderId( null );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_invalidPOId() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setPurchaseOrderId( PURCHASE_ORDER_ID_NOT_EXIST );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_existingPartId() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setPartNoId( PART_NO_ID1 );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_invalidPartId() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setPartNoId( PART_NO_ID_NOT_EXIST );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_nullQuantityUnit() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setQtyUnit( null );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_invalidQuantityUnit() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setQtyUnit( UNIT_EACH_NOT_EXIST );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_invalidLineCode() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setLineTypeCode( PO_LINE_CODE_EXCHANGE );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_invalidPriceType() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setPriceType( PO_LINE_INVALID_PRICE_TYPE_CODE );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void create_failure_invalidQuantityUnitForPart() {

      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine();
      purchaseOrderLine.setQtyUnit( INVALID_UNIT_FOR_PART );
      purchaseOrderLineResourceBean.create( purchaseOrderLine );
   }


   @Test
   public void update_success_purchaseOrderLineDeletedBool() throws AmApiResourceNotFoundException {

      PurchaseOrderLine expectedPOLine = getPurchaseOrderLine();
      expectedPOLine.setDeleted( DELETED_BOOL_TO_UPDATE );

      PurchaseOrderLine actualPOLine =
            purchaseOrderLineResourceBean.update( PURCHASE_ORDER_LINE_ID, expectedPOLine );

      Assert.assertEquals( expectedPOLine, actualPOLine );
   }


   @Test
   public void update_failure_nullPurchaseOrderLinePayload() throws AmApiResourceNotFoundException {

      try {
         purchaseOrderLineResourceBean.update( PURCHASE_ORDER_LINE_ID, null );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing Purchase Order Line Payload.", exception.getMessage() );
      }
   }


   @Test
   public void update_failure_nullPurchaseOrderId() throws AmApiResourceNotFoundException {

      PurchaseOrderLine expectedPOLine = getPurchaseOrderLine();
      expectedPOLine.setDeleted( DELETED_BOOL_TO_UPDATE );
      expectedPOLine.setPurchaseOrderId( null );

      try {
         purchaseOrderLineResourceBean.update( PURCHASE_ORDER_LINE_ID, expectedPOLine );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Purchase Order with ID " + expectedPOLine.getPurchaseOrderId()
               + " cannot be found.", exception.getMessage() );
      }
   }


   @Test
   public void update_failure_invalidPurchaseOrderId() throws AmApiResourceNotFoundException {

      PurchaseOrderLine expectedPOLine = getPurchaseOrderLine();
      expectedPOLine.setDeleted( DELETED_BOOL_TO_UPDATE );
      expectedPOLine.setPurchaseOrderId( PURCHASE_ORDER_ID_NOT_EXIST );

      try {
         purchaseOrderLineResourceBean.update( PURCHASE_ORDER_LINE_ID, expectedPOLine );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Purchase Order with ID " + expectedPOLine.getPurchaseOrderId()
               + " cannot be found.", exception.getMessage() );
      }
   }


   @Test
   public void update_failure_nullPurchaseOrderLineId() throws AmApiResourceNotFoundException {

      PurchaseOrderLine expectedPOLine = getPurchaseOrderLine();
      expectedPOLine.setDeleted( DELETED_BOOL_TO_UPDATE );
      expectedPOLine.setPurchaseOrderId( PURCHASE_ORDER_ID );

      try {
         purchaseOrderLineResourceBean.update( null, expectedPOLine );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Purchase Order Line with ID " + null + " cannot be found.",
               exception.getMessage() );
      }
   }


   @Test
   public void update_failure_invalidPurchaseOrderLineId() throws AmApiResourceNotFoundException {

      PurchaseOrderLine expectedPOLine = getPurchaseOrderLine();
      expectedPOLine.setDeleted( DELETED_BOOL_TO_UPDATE );
      expectedPOLine.setPurchaseOrderId( PURCHASE_ORDER_ID );

      try {
         purchaseOrderLineResourceBean.update( PURCHASE_ORDER_LINE_ID_NOT_EXIST, expectedPOLine );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Purchase Order Line with ID " + PURCHASE_ORDER_LINE_ID_NOT_EXIST
               + " cannot be found.", exception.getMessage() );
      }
   }


   private PurchaseOrderLine getPurchaseOrderLine() {

      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
      purchaseOrderLine.setId( PURCHASE_ORDER_LINE_ID );
      purchaseOrderLine.setPurchaseOrderId( PURCHASE_ORDER_ID );
      purchaseOrderLine.setPartNoId( PART_NO_ID1 );
      purchaseOrderLine.setPromisedByDate( promisedByDate );
      purchaseOrderLine.setQuantity( QUANTITY );
      purchaseOrderLine.setQtyUnit( UNIT_EACH );
      purchaseOrderLine.setDeleted( DELETED_BOOL );
      purchaseOrderLine.setLineNo( PO_LINE_NO1 );
      purchaseOrderLine.setLineTypeCode( PO_LINE_CODE );
      purchaseOrderLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );
      purchaseOrderLine.setUnitPrice( UNIT_PRICE );
      purchaseOrderLine.setPrice( PRICE );
      purchaseOrderLine.setLineDescription( LINE_DESCRIPTION1 );
      purchaseOrderLine.setPartRequestIds( new ArrayList<String>() );

      return purchaseOrderLine;
   }


   private PurchaseOrderLine buildPurchaseOrderLine() {

      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
      purchaseOrderLine.setPurchaseOrderId( PURCHASE_ORDER_ID );
      purchaseOrderLine.setPartNoId( PART_NO_ID2 );
      purchaseOrderLine.setPromisedByDate( promisedByDate );
      purchaseOrderLine.setQuantity( QUANTITY );
      purchaseOrderLine.setQtyUnit( UNIT_EACH );
      purchaseOrderLine.setDeleted( DELETED_BOOL );
      purchaseOrderLine.setLineTypeCode( PO_LINE_CODE );
      purchaseOrderLine.setLineNo( PO_LINE_NO2 );
      purchaseOrderLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );
      purchaseOrderLine.setLineDescription( LINE_DESCRIPTION2 );
      purchaseOrderLine.setOwnerId( OWNER_ID );
      purchaseOrderLine.setFinanceAccountCode( FINANCE_ACCOUNT );
      purchaseOrderLine.setPartRequestIds( new ArrayList<String>() );

      return purchaseOrderLine;
   }
}
