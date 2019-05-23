
package com.mxi.mx.core.services.order.exception;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.servlet.EntityContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.exception.Spec2kOrderLimitExceededException;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.EditOrderDetailsTO;
import com.mxi.mx.core.services.order.OrderDetailsTO;
import com.mxi.mx.core.table.org.OrgVendor;


/**
 * Test whether an exception is threw when a PO Number exceeds 9 characters when creating and
 * editing a purchase order with the conditions spec2000 required vendor and config parm
 * SPEC2000_ORDER_NUM_LIMIT is trueDOCUMENT_ME
 *
 */
public class Spec2kOrderLimitExceededExceptionTest {

   /**
    *
    *
    * @throws java.lang.Exception
    */
   private VendorKey iSpec2kReqVendor;
   private VendorKey iSpec2kNotReqVendor;
   private static final String PO_NUMBER = "PO12345678910";
   private Boolean iSpec2kReq;
   private Boolean iSpec2kNotReq;
   private Boolean iParmNameOrderNumLimitTrue = new Boolean( true );
   private Boolean iParmNameOrderNumLimitFalse = new Boolean( false );
   private EntityContextFake iEntityContextFake = new EntityContextFake();
   private OrderDetailsTO iPODetailsTO = new OrderDetailsTO();
   private EditOrderDetailsTO iEditOrderDetailsTO = new EditOrderDetailsTO();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws Exception {

      // Create a spec2000 required vendor
      iSpec2kReqVendor = new VendorBuilder().withCode( "TESTVENDOR" ).isNoPrintReq( true ).build();

      // Create a spec2000 not required vendor
      iSpec2kNotReqVendor =
            new VendorBuilder().withCode( "TESTVENDOR" ).isNoPrintReq( false ).build();

      iSpec2kReq = new OrgVendor( iSpec2kReqVendor ).isNoPrintReq();

      iSpec2kNotReq = new OrgVendor( iSpec2kNotReqVendor ).isNoPrintReq();

   }


   /**
    * DOCUMENT_ME
    *
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception {
      iEntityContextFake.setPrimaryKey( null );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters created against a spec2k
    * enabled vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is enabled, THEN a
    * Spec2kOrderLimitExceededException should be raised.
    *
    */
   @Test( expected = Spec2kOrderLimitExceededException.class )
   public void testSpec2kReqVendorAndConfigParmTrueWhenCreateOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kReq, iParmNameOrderNumLimitTrue );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters created against a
    * non-spec2k vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is enabled, THEN a
    * Spec2kOrderLimitExceededException should not be raised.
    *
    */
   @Test
   public void testSpec2kNotReqVendorAndConfigParmTrueWhenCreateOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kNotReq, iParmNameOrderNumLimitTrue );

      // Set PONumber greater than 9 characters to a purchase order
      iPODetailsTO.setBarcode( PO_NUMBER );
      String lPONumber = iPODetailsTO.getBarcode();

      assertEquals( lPONumber, PO_NUMBER );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters edited against a spec2k
    * enabled vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is enabled, THEN a
    * Spec2kOrderLimitExceededException should be raised
    *
    */
   @Test( expected = Spec2kOrderLimitExceededException.class )
   public void testSpec2kReqVendorAndConfigParmTrueWhenEditOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kReq, iParmNameOrderNumLimitTrue );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters edited against a
    * non-spec2k vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is enabled, THEN a
    * Spec2kOrderLimitExceededException should not be raised.
    *
    */
   @Test
   public void testSpec2kNotReqVendorAndConfigParmTrueWhenEditOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kNotReq, iParmNameOrderNumLimitTrue );

      // Set PONumber greater than 9 characters to a purchase order
      iEditOrderDetailsTO.setPONumber( PO_NUMBER );
      String lPONumber = iEditOrderDetailsTO.getPONumber();

      assertEquals( lPONumber, PO_NUMBER );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters created against a spec2k
    * enabled vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is disabled, THEN a
    * Spec2kOrderLimitExceededException should not be raised.
    *
    */
   @Test
   public void testSpec2kReqVendorAndConfigParmFalseWhenCreateOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kReq, iParmNameOrderNumLimitFalse );

      // Set PONumber greater than 9 characters to a purchase order
      iPODetailsTO.setBarcode( PO_NUMBER );
      String lPONumber = iPODetailsTO.getBarcode();

      assertEquals( lPONumber, PO_NUMBER );
   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters edited against a spec2k
    * enabled vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is disabled, THEN a
    * Spec2kOrderLimitExceededException should not be raised.
    *
    */
   @Test
   public void testSpec2kReqVendorAndConfigParmFalseWhenEditOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kReq, iParmNameOrderNumLimitFalse );

      // Set PONumber greater than 9 characters to a purchase order
      iEditOrderDetailsTO.setPONumber( PO_NUMBER );
      String lPONumber = iEditOrderDetailsTO.getPONumber();

      assertEquals( lPONumber, PO_NUMBER );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters created against a
    * non-spec2k vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is disabled, THEN a
    * Spec2kOrderLimitExceededException should not be raised.
    *
    */
   @Test
   public void testSpec2kNotReqVendorAndConfigParmFalseWhenCreateOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kNotReq, iParmNameOrderNumLimitFalse );

      // Set PONumber greater than 9 characters to a purchase order
      iPODetailsTO.setBarcode( PO_NUMBER );
      String lPONumber = iPODetailsTO.getBarcode();

      assertEquals( lPONumber, PO_NUMBER );

   }


   /**
    * GIVEN a purchase order with an order number greater than 9 characters edited against a
    * non-spec2k vendor, WHEN the SPEC2000_ORDER_NUM_LIMIT parameter is disabled, THEN a
    * Spec2kOrderLimitExceededException should not be raised.
    *
    */
   @Test
   public void testSpec2kNotReqVendorAndConfigParmFalseWhenEditOrder()
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      checkConditions( iSpec2kNotReq, iParmNameOrderNumLimitFalse );

      // Set PONumber greater than 9 characters to a purchase order
      iEditOrderDetailsTO.setPONumber( PO_NUMBER );
      String lPONumber = iEditOrderDetailsTO.getPONumber();

      assertEquals( lPONumber, PO_NUMBER );

   }


   public void checkConditions( Boolean aSpec2kreq, Boolean aParmNameOrderNumLimit )
         throws MandatoryArgumentException, StringTooLongException,
         Spec2kOrderLimitExceededException {

      // Check whether vendor is spec2000 enabled and config parm is TRUE
      if ( ( aSpec2kreq ) && ( aParmNameOrderNumLimit ) ) {
         Spec2kOrderLimitExceededException.validate( "aPONumber", PO_NUMBER, 9 );
      }

   }

}
