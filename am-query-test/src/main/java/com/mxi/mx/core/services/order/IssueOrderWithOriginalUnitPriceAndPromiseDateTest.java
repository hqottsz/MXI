
package com.mxi.mx.core.services.order;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactoryFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoHeaderTable;


/**
 * This class test the issue order service method whit the purpose of checking for the original
 * promise date and original price values within the order lines
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class IssueOrderWithOriginalUnitPriceAndPromiseDateTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final HumanResourceKey HR = HumanResourceKey.ADMIN;
   private static final Date PROMISE_BY_DATE = new Date();
   private static final BigDecimal LINE_UNIT_PRICE = new BigDecimal( 10.0 );
   private static final BigDecimal LINE_QUANTITY = new BigDecimal( 1.0 );
   private static final DateFormat DATE_FORMATTER = new SimpleDateFormat( "yyyy-MM-dd HH-mm-ss" );
   private static final int REVISION_0 = 0;
   private static final int REVISION_1 = 1;
   private static final int REVISION_2 = 2;
   private PurchaseOrderKey iOrder;
   private PurchaseOrderLineKey iFirstOrderLineKey;
   private PurchaseOrderLineKey iSecondOrderLineKey;
   private VendorKey iVendor;
   private PartNoKey iPart;
   private OrderService iOrderService;


   /*
    * User actions
    */
   private enum Action {
      CANCEL, ISSUE, UNAUTHORIZE, REOPEN
   }


   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Checks the values of original promise date and original price for an order line after the
    * order has been issued
    *
    * @throws TriggerException
    * @throws MxException
    *
    */
   @Test
   public void testIssueOrderForTheFirstTime() throws MxException, TriggerException {

      // set up test data
      withTestDataAs( REVISION_0 );

      // issue order
      iOrderService.issue( iOrder, null, HR );

      // assert order is issued first time
      assertOrderRevisionNro( REVISION_1 );

      // assert original price and original promise date for order line
      asserOrderLinetOriginalPriceAndPromiseDate( iFirstOrderLineKey, false, Action.ISSUE );

      // assert order status
      assertOrderStatus( Action.ISSUE );
   }


   /**
    * Checks the values of original promise date and original price for an order line after the
    * order has been issued for a second time. The unit price and promise date for the order line
    * changed before the second issue.
    *
    * @throws TriggerException
    * @throws MxException
    * @throws ParseException
    *
    */
   @Test
   public void testReIssueOrderWithOriginalUnitPriceAndPromiseDate()
         throws MxException, TriggerException, ParseException {

      // set up test data
      withTestDataAs( REVISION_1 );
      whenOrderHasBeenAlreadyIssued( iOrder );
      havingOriginalPriceAndPromiseDateSetForOrderLine( iFirstOrderLineKey );

      // issue order
      iOrderService.issue( iOrder, null, HR );

      // assert order is issued second time
      assertOrderRevisionNro( REVISION_2 );

      // assert order status
      assertOrderStatus( Action.ISSUE );

      // assert original price and original promise date order line
      asserOrderLinetOriginalPriceAndPromiseDate( iFirstOrderLineKey, true, Action.ISSUE );

   }


   /**
    * Checks the values of original promise date and original price for an order line after the
    * order has been canceled and re-opened. The original unit price and promise date for the order
    * line are removed.
    *
    *
    * @throws TriggerException
    * @throws MxException
    * @throws ParseException
    *
    */
   @Test
   public void testCancelAndReopenOrderWithOriginalUnitPriceAndPromiseDate()
         throws MxException, TriggerException, ParseException {

      // set up test data
      withTestDataAs( REVISION_1 );
      whenOrderHasBeenAlreadyIssued( iOrder );
      havingOriginalPriceAndPromiseDateSetForOrderLine( iFirstOrderLineKey );

      // cancel order
      OrderService.cancel( iOrder, HR, null, null, null );

      // assert order status
      assertOrderStatus( Action.CANCEL );

      // assert original price and original promise date order line
      asserOrderLinetOriginalPriceAndPromiseDate( iFirstOrderLineKey, true, Action.CANCEL );

      // re-open order
      iOrderService.reOpen( iOrder, RefEventStatusKey.POOPEN, HR );

      // assert order status
      assertOrderStatus( Action.REOPEN );

      // assert original price and original promise date order line
      asserOrderLinetOriginalPriceAndPromiseDate( iFirstOrderLineKey, true, Action.REOPEN );

   }


   /**
    * Checks the values of original promise date and original price for an order line after the
    * issued order has been un-authorized.
    *
    * @throws TriggerException
    * @throws MxException
    * @throws ParseException
    *
    */
   @Test
   public void testUnautorizeOrderWithOriginalUnitPriceAndPromiseDate()
         throws MxException, TriggerException, ParseException {

      // set up test data
      withTestDataAs( REVISION_1 );
      whenOrderHasBeenAlreadyIssued( iOrder );
      havingOriginalPriceAndPromiseDateSetForOrderLine( iFirstOrderLineKey );

      // un-authorize order
      OrderService.unauthorize( iOrder, null, null, HR );

      // assert order status
      assertOrderStatus( Action.UNAUTHORIZE );

      // assert original price and original promise date order line
      asserOrderLinetOriginalPriceAndPromiseDate( iFirstOrderLineKey, true, Action.UNAUTHORIZE );

   }


   /**
    * Checks the values of original promise date and original price for the order lines after the
    * order has been issued for a second time.
    *
    * The unit price and promise date for the first order line changed before the second issue.
    * There is a new order line added to the order before the second issue.
    *
    * @throws TriggerException
    * @throws MxException
    * @throws ParseException
    *
    */
   @Test
   public void testReIssueOrderAfterAddingNewOrderLine()
         throws MxException, TriggerException, ParseException {

      // set up test data
      withTestDataAs( REVISION_1 );
      whenOrderHasBeenAlreadyIssued( iOrder );
      havingOriginalPriceAndPromiseDateSetForOrderLine( iFirstOrderLineKey );

      // add a new line to the order
      iSecondOrderLineKey = new OrderLineBuilder( iOrder ).withUnitPrice( LINE_UNIT_PRICE )
            .promisedBy( PROMISE_BY_DATE ).withAccount( FncAccountKey.CONSIGN )
            .withLineType( RefPoLineTypeKey.PURCHASE ).forPart( iPart )
            .withOrderQuantity( LINE_QUANTITY ).withUnitType( RefQtyUnitKey.EA ).build();

      // issue order
      iOrderService.issue( iOrder, null, HR );

      // assert order is issued second time
      assertOrderRevisionNro( REVISION_2 );

      // assert original price and original promise date for each order line
      asserOrderLinetOriginalPriceAndPromiseDate( iFirstOrderLineKey, true, Action.ISSUE );
      asserOrderLinetOriginalPriceAndPromiseDate( iSecondOrderLineKey, false, Action.ISSUE );

      // assert order status
      assertOrderStatus( Action.ISSUE );
   }


   /**
    * Set up the test case
    *
    * @throws Exception
    *            the exception
    */
   @Before
   public void before() throws Exception {

      iOrderService = new OrderService();
      StringBundles.setSingleton( StringBundles.getInstance() );
      GlobalParametersStub lGlobalParamsLogic =
            new GlobalParametersStub( ParmTypeEnum.LOGIC.name() );
      lGlobalParamsLogic.setBoolean( "SPEC2000_UPPERCASE_VENDOR_CD", false );
      lGlobalParamsLogic.setBoolean( "ADD_SHIPPING_PROCESSING_TIME", false );
      lGlobalParamsLogic.setBoolean( "ENABLE_ORDER_BUDGET_INTEGRATION", false );
      GlobalParametersStub lGlobalParamsSecure =
            new GlobalParametersStub( ParmTypeEnum.SECURED_RESOURCE.name() );
      lGlobalParamsSecure.setBoolean( "ENFORCE_WARRANTY_REP_ORDER_LINES", false );

      GlobalParameters.setInstance( lGlobalParamsLogic );
      GlobalParameters.setInstance( lGlobalParamsSecure );
      PrimaryKeyService.setSingleton( new PrimaryKeyServiceStub() );
      SequenceGeneratorFactory.setInstance( new SequenceGeneratorFactoryFake() );
      EjbFactory.setSingleton( new EjbFactoryStub() );
      TriggerFactory.setInstance( new TriggerFactoryStub() );

   }


   /**
    * Tears down the test case
    *
    * @throws Exception
    *            the exception
    */
   @After
   public void after() throws Exception {

      StringBundles.setSingleton( null );
      PrimaryKeyService.setSingleton( null );
      SequenceGeneratorFactory.setInstance( null );
      EjbFactory.setSingleton( null );
      TriggerFactory.setInstance( null );
      GlobalParameters.setInstance( ParmTypeEnum.LOGIC.name(), null );
      GlobalParameters.setInstance( ParmTypeEnum.SECURED_RESOURCE.name(), null );

      iDatabaseConnectionRule.getConnection().rollback();
   }


   /**
    * Set up order and order information
    *
    * @param aRevisionNumber
    *           order revision number
    *
    */
   private void withTestDataAs( int aRevisionNumber ) {

      // build part
      iPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).build();

      // build vendor
      iVendor = new VendorBuilder().build();

      // build order
      iOrder = new OrderBuilder().withStatus( RefEventStatusKey.POAUTH ).withVendor( iVendor )
            .withRevisionNumber( aRevisionNumber ).build();

      // build order line
      iFirstOrderLineKey = new OrderLineBuilder( iOrder ).withUnitPrice( LINE_UNIT_PRICE )
            .promisedBy( PROMISE_BY_DATE ).withAccount( FncAccountKey.CONSIGN )
            .withLineType( RefPoLineTypeKey.PURCHASE ).forPart( iPart )
            .withOrderQuantity( LINE_QUANTITY ).withUnitType( RefQtyUnitKey.EA ).build();
   }


   /**
    * Set original promise date and original price for an order line
    *
    * @param aPurchaseOrderLineKey
    *           order line key
    * @throws ParseException
    *
    */
   private void havingOriginalPriceAndPromiseDateSetForOrderLine(
         PurchaseOrderLineKey aPurchaseOrderLineKey ) throws ParseException {

      POLineTable lPOLineTable = POLineTable.findByPrimaryKey( aPurchaseOrderLineKey );

      lPOLineTable.setOriginalPromisedBy( DATE_FORMATTER.parse( "2014-01-01 10-30-00" ) );
      lPOLineTable.setOriginalUnitPrice( new BigDecimal( 1.0 ) );
      lPOLineTable.update();
   }


   /**
    * Assert order revision number
    *
    * @param aRevisionNumber
    *           order revision number
    */
   private void assertOrderRevisionNro( int aRevisionNumber ) {

      PoHeaderTable lPOHeaderTable = PoHeaderTable.findByPrimaryKey( iOrder );
      assertThat( lPOHeaderTable.getRevisionNo(), is( equalTo( aRevisionNumber ) ) );

   }


   /**
    * Assert order status
    *
    * @param aAction
    *           user action
    */
   private void assertOrderStatus( Action aAction ) {

      EvtEventTable lEvtEventTable = EvtEventTable.findByPrimaryKey( iOrder );

      switch ( aAction ) {

         case ISSUE:
            assertThat( lEvtEventTable.getEventStatus(),
                  is( equalTo( RefEventStatusKey.POISSUED ) ) );
            break;

         case CANCEL:
            assertThat( lEvtEventTable.getEventStatus(),
                  is( equalTo( RefEventStatusKey.POCANCEL ) ) );
            break;

         case UNAUTHORIZE:
            assertThat( lEvtEventTable.getEventStatus(),
                  is( equalTo( RefEventStatusKey.POOPEN ) ) );
            break;

         case REOPEN:
            assertThat( lEvtEventTable.getEventStatus(),
                  is( equalTo( RefEventStatusKey.POOPEN ) ) );
            break;
      }

   }


   /**
    * Set the issued date for a given order
    *
    * @param aPurchaseOrderKey
    *           order key
    */
   private void whenOrderHasBeenAlreadyIssued( PurchaseOrderKey aPurchaseOrderKey ) {

      PoHeaderTable lPOHeaderTable = PoHeaderTable.findByPrimaryKey( aPurchaseOrderKey );
      lPOHeaderTable.setIssuedDt( new Date() );
      lPOHeaderTable.update();
   }


   /**
    * Assert the original promise date and original unit price for a given order line.
    *
    * @param aPurchaseOrderLineKey
    *           order line key
    *
    * @param aExists
    *           if the original promise date and original unit price already exists for the order
    *           line
    *
    * @param aAction
    *           user action
    */
   private void asserOrderLinetOriginalPriceAndPromiseDate(
         PurchaseOrderLineKey aPurchaseOrderLineKey, boolean aExists, Action aAction ) {

      // get order line info
      POLineTable lPOLineTable = POLineTable.findByPrimaryKey( aPurchaseOrderLineKey );
      Date lOriginalPromiseDate = lPOLineTable.getOriginalPromisedBy();
      BigDecimal lOriginalUnitPrice = lPOLineTable.getOriginalUnitPrice();

      switch ( aAction ) {

         case ISSUE:

            if ( aExists ) {
               // if the original promise date and original unit price already exists for an order
               // line then they should be different to the order line's promise date and unit price
               // if they changed after the order was issued.
               assertThat( DATE_FORMATTER.format( lOriginalPromiseDate ),
                     not( equalTo( DATE_FORMATTER.format( PROMISE_BY_DATE ) ) ) );

               assertThat( lOriginalUnitPrice, not( equalTo( LINE_UNIT_PRICE ) ) );
            } else {
               // if the original promise date and original unit price did not exist for an order
               // line then they should be the same to the order line's promise date and unit price
               // after the order was issued.
               assertThat( DATE_FORMATTER.format( lOriginalPromiseDate ),
                     is( equalTo( DATE_FORMATTER.format( PROMISE_BY_DATE ) ) ) );

               assertThat( lOriginalUnitPrice, is( equalTo( LINE_UNIT_PRICE ) ) );
            } ;

            break;

         case CANCEL:
            assertThat( lOriginalPromiseDate, is( equalTo( null ) ) );
            assertThat( lOriginalUnitPrice, is( equalTo( null ) ) );
            break;

         case UNAUTHORIZE:
            if ( aExists ) {
               // if the original promise date and original unit price already exists for an order
               // line then they should be different to the order line's promise date and unit price
               // if they changed after the order was issued.
               assertThat( DATE_FORMATTER.format( lOriginalPromiseDate ),
                     not( equalTo( DATE_FORMATTER.format( PROMISE_BY_DATE ) ) ) );

               assertThat( lOriginalUnitPrice, not( equalTo( LINE_UNIT_PRICE ) ) );
            } else {
               // if the original promise date and original unit price did not exist for an order
               // line then they should be the same to the order line's promise date and unit price
               // after the order was issued.
               assertThat( DATE_FORMATTER.format( lOriginalPromiseDate ),
                     is( equalTo( DATE_FORMATTER.format( PROMISE_BY_DATE ) ) ) );

               assertThat( lOriginalUnitPrice, is( equalTo( LINE_UNIT_PRICE ) ) );
            } ;

            break;

         case REOPEN:
            assertThat( lOriginalPromiseDate, is( equalTo( null ) ) );
            assertThat( lOriginalUnitPrice, is( equalTo( null ) ) );
            break;
      }
   }

}
