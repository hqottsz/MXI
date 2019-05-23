package com.mxi.am.api.resource.materials.proc.rfq;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBContext;

import org.apache.commons.collections.CollectionUtils;
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
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.rfq.impl.RFQResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test for RFQResourceBean
 *
 * @author "Dasuni Kumarapperuma" <dasuni.kumarapperuma@ifsworld.com>
 */
@RunWith( MockitoJUnitRunner.class )
public class RFQResourceBeanTest extends ResourceBeanTest {

   @Inject
   RFQResourceBean iRfqResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;
   private static final String RFQ_STATUS_OPEN = "RFQOPEN";
   private static final String RFQ_STATUS_SENT = "RFQSENT";

   private static final String ID1 = "ACF52996EED24447BA0C73FA58921C04";
   private static final String RFQ_NO1 = "Q0100007";
   private static final String CUSTOMER_CODE = "CTIRE";
   private static final String RESPOND_BY_DATE = "2007-01-11T05:00:00Z";
   private static final String NEEDED_BY_DATE = "2007-01-21T05:00:00Z";
   private static final String PURCHASING_CONTACT = "authorized";
   private static final String PURCHASING_CONTACT_ID = "FBB6ADDF6E8342ECA5955B1CA243B9FB";

   private static final String INVALID_ID = "00000000000110000000000000000001";
   private static final String INVALID_RFQ_NO = "00000000000000001";
   private static final String RFQ_LINE_ID_NOT_BELONEGS_TO_RFQ = "322C2B65ABD74679B037218A09E34CC2";

   private static final String INVALID_PART_REQUEST_ID = "00000000000000001";
   private static final String INVALID_PRIORITY_CODE = "AO";

   private static BigDecimal RFQ_QTY1 = new BigDecimal( 5 );
   private static final String PART_ID1 = "1BFE0F7AE9354D838C9C8EF3ACC11182";
   private static final String PRIORITY_CODE1 = "NORMAL";
   private static final String UNIT_CODE1 = "EA";
   private static final String VENDOR_NOTE1 = "RFQ line example vendor note 1";
   private static final String RFQ_LINE_ID1 = "07E283CBC7B547B9AE2BA7AAE901F04C";
   private static final int RFQ_LINE_NO1 = 1;

   private static BigDecimal RFQ_QTY2 = new BigDecimal( 2 );
   private static final String PART_ID2 = "1B7C80E8513249AA95012406EA67DA29";
   private static final String PRIORITY_CODE2 = "NORMAL";
   private static final String UNIT_CODE2 = "EA";
   private static final String VENDOR_NOTE2 = "RFQ line example vendor note 2";
   private static final String RFQ_LINE_ID2 = "192EFB727447493A9185BEF40E13463C";
   private static final int RFQ_LINE_NO2 = 2;

   private static BigDecimal RFQ_QTY3 = new BigDecimal( 1 );
   private static final String PART_ID3 = "1EACF8E41803412EA5E23B326D12D594";
   private static final String PRIORITY_CODE3 = "NORMAL";
   private static final String UNIT_CODE3 = "EA";
   private static final String VENDOR_NOTE3 = "RFQ line example vendor note 3";
   private static final String RFQ_LINE_ID3 = "DD9FE0BFF52C404CB6DE9945BA3EF467";
   private static final int RFQ_LINE_NO3 = 3;

   private static BigDecimal RFQ_QTY4 = new BigDecimal( 0 );
   private static final String PART_ID4 = "E500C190358E41E99A2D7702DB1B2574";
   private static final String PRIORITY_CODE4 = "AOG";
   private static final String UNIT_CODE4 = "EA";
   private static final int RFQ_LINE_NO4 = 1;

   private static final String RFQ_VENDOR1 = "E5BD78FBC27C4AACA42136DE4943F59B";
   private static final String CURRENCY_CODE1 = "CAD";

   private static final String QUOT_NO1 = "111";
   private static final Double QUOT_QTY1 = 3.0;
   private static final BigDecimal UNIT_PRICE1 = new BigDecimal( 3 );
   private static final BigDecimal LINE_PRICE1 = new BigDecimal( 9 );
   private static final Double LEAD_TIME1 = 2.0;
   private static final String QUOT_DATE1 = "2007-01-11T05:00:00Z";
   private static final String LINE_VENDOR_NOTE1 = "RFQLineVendor1 vendor note";
   private static final String CONDITION_CODE1 = "NEW";
   private static final String QTY_UNIT_CODE1 = "EA";
   private static final String EFFECTIVE_TO_DATE1 = "2007-01-11T05:00:00Z";
   private static final String QUOTE_PART_ID1 = "1BFE0F7AE9354D838C9C8EF3ACC11182";
   private static final boolean DID_NOT_QUOTE_BOOL1 = false;

   private static final String QUOT_NO2 = "222";
   private static final Double QUOT_QTY2 = 2.0;
   private static final BigDecimal UNIT_PRICE2 = new BigDecimal( 3 );
   private static final BigDecimal LINE_PRICE2 = new BigDecimal( 6 );
   private static final Double LEAD_TIME2 = 2.0;
   private static final String QUOT_DATE2 = "2007-01-11T05:00:00Z";
   private static final String LINE_VENDOR_NOTE2 = "RFQLineVendor2 vendor note";
   private static final String CONDITION_CODE2 = "NEW";
   private static final String QTY_UNIT_CODE2 = "EA";
   private static final String EFFECTIVE_TO_DATE2 = "2007-01-11T05:00:00Z";
   private static final String QUOTE_PART_ID2 = "1B7C80E8513249AA95012406EA67DA29";
   private static final boolean DID_NOT_QUOTE_BOOL2 = false;

   private static final String QUOT_NO3 = "333";
   private static final Double QUOT_QTY3 = 4.0;
   private static final BigDecimal UNIT_PRICE3 = new BigDecimal( 3 );
   private static final BigDecimal LINE_PRICE3 = new BigDecimal( 12 );
   private static final Double LEAD_TIME3 = 2.0;
   private static final String QUOT_DATE3 = "2007-01-11T05:00:00Z";
   private static final String LINE_VENDOR_NOTE3 = "RFQLineVendor3 vendor note";
   private static final String CONDITION_CODE3 = "NEW";
   private static final String QTY_UNIT_CODE3 = "EA";
   private static final String EFFECTIVE_TO_DATE3 = "2007-01-11T05:00:00Z";
   private static final String QUOTE_PART_ID3 = "1EACF8E41803412EA5E23B326D12D594";
   private static final boolean DID_NOT_QUOTE_BOOL3 = false;

   private static final String RFQ_NO2 = "Q0100001";
   private static final String DB_ID_AND_CUSTOMER_CODE2 = "10:CTIRE";
   private static final String CUSTOMER_CODE2 = "CTIRE";
   private static final String PURCHASING_CONTACT2 = "mxi";
   private static final String PURCHASING_CONTACT_ID2 = "FBB6ADDF6E8342ECA5955B1CA243B8FB";
   private static final String DB_ID_AND_CURRENCY2 = "10:USD";
   private static final String CURRENCY2 = "USD";
   private static BigDecimal EXCHANGE_RATE2 = new BigDecimal( 1 );

   private static final String CURRENCY_CODE1_MODIFIED = "BLK";
   private static final String QUOT_NO1_MODIFIED = "112";
   private static final Double QUOT_QTY1_MODIFIED = 4.0;
   private static final BigDecimal UNIT_PRICE1_MODIFIED = new BigDecimal( 6 );
   private static final BigDecimal LINE_PRICE1_MODIFIED = new BigDecimal( 24 );
   private static final Double LEAD_TIME1_MODIFIED = 2.0;
   private static final String QUOT_DATE1_MODIFIED = "2011-01-11T05:00:00Z";
   private static final String LINE_VENDOR_NOTE1_MODIFIED = "RFQLineVendor1 vendor note modified";
   private static final String CONDITION_CODE1_MODIFIED = "NEW";
   private static final String QTY_UNIT_CODE1_MODIFIED = "EA";
   private static final String EFFECTIVE_TO_DATE1_MODIFIED = "2011-01-11T05:00:00Z";
   private static final String QUOTE_PART_ID1_MODIFIED = "1BFE0F7AE9354D838C9C8EF3ACC11182";

   private static final String PART_REQUEST_ID1 = "D72E433C0F8C4A0E9FE22F30F733F10D";
   private static final String PART_REQUEST_ID2 = "D0EC219DD8BD407786405CC506E06D49";
   private static final String PART_REQUEST_ID3 = "D2C93BE6792E11E8B9A2AD743F8A29F1";


   private RFQDefinition constructExpectedResults() throws ParseException {
      RFQDefinition lRFQDefinition = new RFQDefinition();
      lRFQDefinition.setId( ID1 );
      lRFQDefinition.setRfqNo( RFQ_NO1 );
      lRFQDefinition.setCustomerCode( CUSTOMER_CODE );
      lRFQDefinition.setPurchasingContact( PURCHASING_CONTACT );
      lRFQDefinition.setPurchasingContactId( PURCHASING_CONTACT_ID );
      Date lRespondByDate;
      Date lNeededByDate;
      try {
         lRespondByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( RESPOND_BY_DATE );
         lNeededByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( NEEDED_BY_DATE );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQDefinition.setRespondByDate( lRespondByDate );
      lRFQDefinition.setNeededByDate( lNeededByDate );

      List<RFQLine> RFQLinesList = constructExpectedRFQLines();
      lRFQDefinition.setRfqLines( RFQLinesList );

      List<RFQVendor> RFQVendorsList = constructExpectedRFQVendors();
      lRFQDefinition.setRfqVendors( RFQVendorsList );
      lRFQDefinition.setStatus( RFQ_STATUS_OPEN );

      List<String> lPartReqeustIdList = new ArrayList<String>();
      lPartReqeustIdList.add( PART_REQUEST_ID3 );
      lRFQDefinition.setPartRequestIds( lPartReqeustIdList );

      return lRFQDefinition;

   }


   private List<RFQLine> constructExpectedRFQLines() throws ParseException {
      List<RFQLine> RFQLinesList = new ArrayList<RFQLine>();

      RFQLine lRFQLine1 = new RFQLine();
      lRFQLine1.setPartId( PART_ID1 );
      lRFQLine1.setPriorityCode( PRIORITY_CODE1 );
      lRFQLine1.setRfqQuantity( RFQ_QTY1 );
      lRFQLine1.setUnitCode( UNIT_CODE1 );
      lRFQLine1.setVendorNote( VENDOR_NOTE1 );
      lRFQLine1.setRfqLineId( RFQ_LINE_ID1 );
      lRFQLine1.setRfqLineNo( RFQ_LINE_NO1 );
      RFQLinesList.add( lRFQLine1 );

      RFQLine lRFQLine2 = new RFQLine();
      lRFQLine2.setPartId( PART_ID2 );
      lRFQLine2.setPriorityCode( PRIORITY_CODE2 );
      lRFQLine2.setRfqQuantity( RFQ_QTY2 );
      lRFQLine2.setUnitCode( UNIT_CODE2 );
      lRFQLine2.setVendorNote( VENDOR_NOTE2 );
      lRFQLine2.setRfqLineId( RFQ_LINE_ID2 );
      lRFQLine2.setRfqLineNo( RFQ_LINE_NO2 );
      RFQLinesList.add( lRFQLine2 );

      RFQLine lRFQLine3 = new RFQLine();
      lRFQLine3.setPartId( PART_ID3 );
      lRFQLine3.setPriorityCode( PRIORITY_CODE3 );
      lRFQLine3.setRfqQuantity( RFQ_QTY3 );
      lRFQLine3.setUnitCode( UNIT_CODE3 );
      lRFQLine3.setVendorNote( VENDOR_NOTE3 );
      lRFQLine3.setRfqLineId( RFQ_LINE_ID3 );
      lRFQLine3.setRfqLineNo( RFQ_LINE_NO3 );
      RFQLinesList.add( lRFQLine3 );

      return RFQLinesList;
   }


   private List<RFQVendor> constructExpectedRFQVendors() throws ParseException {
      List<RFQVendor> RFQVendorsList = new ArrayList<RFQVendor>();

      RFQVendor lRFQVendor1 = new RFQVendor();
      lRFQVendor1.setVendorId( RFQ_VENDOR1 );
      lRFQVendor1.setCurrencyCode( CURRENCY_CODE1 );

      List<RFQLineVendor> RFQLineVendorsList = constructExpectedRFQLineVendors();
      lRFQVendor1.setRFQLineVendor( RFQLineVendorsList );

      RFQVendorsList.add( lRFQVendor1 );

      return RFQVendorsList;
   }


   private List<RFQLineVendor> constructExpectedRFQLineVendors() throws ParseException {
      List<RFQLineVendor> RFQLineVendorsList = new ArrayList<RFQLineVendor>();

      RFQLineVendor lRFQLineVendor1 = new RFQLineVendor();
      lRFQLineVendor1.setConditionCode( CONDITION_CODE1 );

      Date lEffectiveToDate1;
      try {
         lEffectiveToDate1 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE1 );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQLineVendor1.setEffectiveToDate( lEffectiveToDate1 );
      lRFQLineVendor1.setLeadTime( LEAD_TIME1 );
      lRFQLineVendor1.setReqPartId( PART_ID1 );
      lRFQLineVendor1.setQuotePartId( QUOTE_PART_ID1 );

      Date lQuoteDate1;
      try {
         lQuoteDate1 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( QUOT_DATE1 );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQLineVendor1.setQuoteDate( lQuoteDate1 );
      lRFQLineVendor1.setQuoteNo( QUOT_NO1 );
      lRFQLineVendor1.setQuoteQuantity( QUOT_QTY1 );
      lRFQLineVendor1.setRfqLineId( RFQ_LINE_ID1 );
      lRFQLineVendor1.setQuoteUnitCode( QTY_UNIT_CODE1 );
      lRFQLineVendor1.setUnitPrice( UNIT_PRICE1 );
      lRFQLineVendor1.setVendorNote( LINE_VENDOR_NOTE1 );
      lRFQLineVendor1.setLinePrice( LINE_PRICE1 );
      lRFQLineVendor1.setDidNotQuoteBool( DID_NOT_QUOTE_BOOL1 );
      RFQLineVendorsList.add( lRFQLineVendor1 );

      RFQLineVendor lRFQLineVendor2 = new RFQLineVendor();
      lRFQLineVendor2.setConditionCode( CONDITION_CODE2 );

      Date lEffectiveToDate2;
      try {
         lEffectiveToDate2 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE2 );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQLineVendor2.setEffectiveToDate( lEffectiveToDate2 );
      lRFQLineVendor2.setLeadTime( LEAD_TIME2 );
      lRFQLineVendor2.setReqPartId( PART_ID2 );
      lRFQLineVendor2.setQuotePartId( QUOTE_PART_ID2 );

      Date lQuoteDate2;
      try {
         lQuoteDate2 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( QUOT_DATE2 );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQLineVendor2.setQuoteDate( lQuoteDate2 );
      lRFQLineVendor2.setQuoteNo( QUOT_NO2 );
      lRFQLineVendor2.setQuoteQuantity( QUOT_QTY2 );
      lRFQLineVendor2.setRfqLineId( RFQ_LINE_ID2 );
      lRFQLineVendor2.setQuoteUnitCode( QTY_UNIT_CODE2 );
      lRFQLineVendor2.setUnitPrice( UNIT_PRICE2 );
      lRFQLineVendor2.setVendorNote( LINE_VENDOR_NOTE2 );
      lRFQLineVendor2.setLinePrice( LINE_PRICE2 );
      lRFQLineVendor2.setDidNotQuoteBool( DID_NOT_QUOTE_BOOL2 );
      RFQLineVendorsList.add( lRFQLineVendor2 );

      RFQLineVendor lRFQLineVendor3 = new RFQLineVendor();
      lRFQLineVendor3.setConditionCode( CONDITION_CODE3 );

      Date lEffectiveToDate3;
      try {
         lEffectiveToDate3 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE3 );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQLineVendor3.setEffectiveToDate( lEffectiveToDate3 );
      lRFQLineVendor3.setLeadTime( LEAD_TIME3 );
      lRFQLineVendor3.setReqPartId( PART_ID3 );
      lRFQLineVendor3.setQuotePartId( QUOTE_PART_ID3 );

      Date lQuoteDate3;
      try {
         lQuoteDate3 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( QUOT_DATE3 );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lRFQLineVendor3.setQuoteDate( lQuoteDate3 );
      lRFQLineVendor3.setQuoteNo( QUOT_NO3 );
      lRFQLineVendor3.setQuoteQuantity( QUOT_QTY3 );
      lRFQLineVendor3.setRfqLineId( RFQ_LINE_ID3 );
      lRFQLineVendor3.setQuoteUnitCode( QTY_UNIT_CODE3 );
      lRFQLineVendor3.setUnitPrice( UNIT_PRICE3 );
      lRFQLineVendor3.setVendorNote( LINE_VENDOR_NOTE3 );
      lRFQLineVendor3.setLinePrice( LINE_PRICE3 );
      lRFQLineVendor3.setDidNotQuoteBool( DID_NOT_QUOTE_BOOL3 );
      RFQLineVendorsList.add( lRFQLineVendor3 );

      return RFQLineVendorsList;
   }


   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      iRfqResourceBean.setEJBContext( iEJBContext );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testGetRFQById() throws Exception {

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      RFQDefinition lRFQDefinition = iRfqResourceBean.get( ID1, lRFQDefinitionSearchParameters );

      assertTrue( constructExpectedResults().equals( lRFQDefinition ) );

   }


   @Test
   public void testNullBadRequest() throws Exception {

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      try {
         RFQDefinition lRFQDefinition =
               iRfqResourceBean.get( null, lRFQDefinitionSearchParameters );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide RFQ alt Id", aE.getMessage() );
      }

   }


   @Test
   public void testRFQDefinitionNotFound() throws Exception {
      try {
         RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
               new RFQDefinitionSearchParameters();
         lRFQDefinitionSearchParameters.setLines( "true" );
         lRFQDefinitionSearchParameters.setVendors( "true" );
         lRFQDefinitionSearchParameters.setLineVendors( "true" );

         RFQDefinition lRFQDefinition =
               iRfqResourceBean.get( INVALID_ID, lRFQDefinitionSearchParameters );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( INVALID_ID, aE.getId() );
      }

   }


   @Test
   public void testSearchRfq() throws Exception {
      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setRfqNo( RFQ_NO1 );
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      List<RFQDefinition> lRFQDefinitions =
            iRfqResourceBean.search( lRFQDefinitionSearchParameters );

      Assert.assertEquals( 1, lRFQDefinitions.size() );

      assertTrue( constructExpectedResults().equals( lRFQDefinitions.get( 0 ) ) );

   }


   @Test
   public void testSearchRfqBadRequest() {
      try {
         RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
               new RFQDefinitionSearchParameters();
         lRFQDefinitionSearchParameters.setRfqNo( null );
         lRFQDefinitionSearchParameters.setLines( "true" );
         lRFQDefinitionSearchParameters.setVendors( "true" );
         lRFQDefinitionSearchParameters.setLineVendors( "true" );

         List<RFQDefinition> lRFQDefinitions =
               iRfqResourceBean.search( lRFQDefinitionSearchParameters );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide RFQ No", aE.getMessage() );
      }
   }


   @Test
   public void testSearchRfqInvalidRfqNo() {

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setRfqNo( INVALID_ID );
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      List<RFQDefinition> lRFQDefinitions =
            iRfqResourceBean.search( lRFQDefinitionSearchParameters );
      assertTrue( CollectionUtils.isEmpty( lRFQDefinitions ) );

   }


   /**
    *
    * Test method for create RFQ
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQSucess200()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();
      lRFQDefinition.setRfqLines( null );
      lRFQDefinition.setRfqVendors( null );

      RFQDefinition lCreatedRFQDefinition = iRfqResourceBean.post( lRFQDefinition );

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      RFQDefinition lRFQDefinitionNew =
            iRfqResourceBean.get( lCreatedRFQDefinition.getId(), lRFQDefinitionSearchParameters );

      lRFQDefinition.setCurrency( CURRENCY2 );
      lRFQDefinition.setCustomerCode( CUSTOMER_CODE2 );

      lRFQDefinitionNew.setId( null );

      assertTrue( lRFQDefinition.equals( lRFQDefinitionNew ) );

   }


   /**
    *
    * Test method for create RFQ with part lines
    *
    * @throws AmApiResourceNotFoundException
    * @throws AmApiBusinessException
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQWithLineSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();
      lRFQDefinition.setRfqVendors( null );
      RFQDefinition lCreatedRFQDefinition = iRfqResourceBean.post( lRFQDefinition );

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      RFQDefinition lRFQDefinitionNew =
            iRfqResourceBean.get( lCreatedRFQDefinition.getId(), lRFQDefinitionSearchParameters );

      lRFQDefinition.setCurrency( CURRENCY2 );
      lRFQDefinition.setCustomerCode( CUSTOMER_CODE2 );

      lRFQDefinitionNew.setId( null );
      if ( CollectionUtils.isNotEmpty( lRFQDefinitionNew.getRfqLines() ) ) {
         // We just want to assert only partID
         for ( RFQLine lRfqLine : lRFQDefinitionNew.getRfqLines() ) {
            lRfqLine.setPriorityCode( null );
            lRfqLine.setRfqQuantity( null );
            lRfqLine.setUnitCode( null );
            lRfqLine.setRfqLineId( null );
            lRfqLine.setRfqLineNo( 0 );
         }
      }
      assertTrue( lRFQDefinition.equals( lRFQDefinitionNew ) );
   }


   /**
    *
    * Test method for create RFQ with vendors
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQWithLinesAndVendorsSucess200()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();

      RFQDefinition lCreatedRFQDefinition = iRfqResourceBean.post( lRFQDefinition );

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      RFQDefinition lRFQDefinitionNew =
            iRfqResourceBean.get( lCreatedRFQDefinition.getId(), lRFQDefinitionSearchParameters );
      lRFQDefinition.setCurrency( CURRENCY2 );
      lRFQDefinition.setCustomerCode( CUSTOMER_CODE2 );

      lRFQDefinitionNew.setId( null );

      if ( CollectionUtils.isNotEmpty( lRFQDefinitionNew.getRfqLines() ) ) {
         // We just want to assert only partID
         for ( RFQLine lRfqLine : lRFQDefinitionNew.getRfqLines() ) {
            lRfqLine.setPriorityCode( null );
            lRfqLine.setRfqQuantity( null );
            lRfqLine.setUnitCode( null );
            lRfqLine.setRfqLineId( null );
            lRfqLine.setRfqLineNo( 0 );
         }
      }

      if ( CollectionUtils.isNotEmpty( lRFQDefinitionNew.getRfqVendors() ) ) {
         // We just want to assert only vendorID
         for ( RFQVendor lRfqVendor : lRFQDefinitionNew.getRfqVendors() ) {
            lRfqVendor.setCurrencyCode( null );
            lRfqVendor.setRFQLineVendor( null );
         }
      }
      assertTrue( lRFQDefinition.equals( lRFQDefinitionNew ) );
   }


   /**
    *
    * Test method for create RFQ precondition fail scenario for currency is null
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQCurrencyIsNullBadRequest() throws Exception {
      try {
         RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();

         lRFQDefinition.setCurrency( null );

         RFQDefinition lRDefinition = iRfqResourceBean.post( lRFQDefinition );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide currency code", aE.getMessage() );
      }
   }


   /**
    *
    * Test method for create RFQ precondition fail scenario for customer code is null
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQCustomerCodeIsNullBadRequest() throws Exception {
      try {
         RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();

         lRFQDefinition.setCustomerCode( null );

         RFQDefinition lRDefinition = iRfqResourceBean.post( lRFQDefinition );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide customer code", aE.getMessage() );
      }
   }


   /**
    *
    * Test method for create RFQ precondition fail scenario for Exchange Rate is null
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQExchangeRateIsNullBadRequest() throws Exception {
      try {
         RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();

         lRFQDefinition.setExchangeRate( null );

         RFQDefinition lRDefinition = iRfqResourceBean.post( lRFQDefinition );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide exchange rate", aE.getMessage() );
      }
   }


   /**
    *
    * Test method for create RFQ precondition fail scenario for Purchasing Contact is null
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQPurchasingContactIsNullBadRequest() throws Exception {
      try {
         RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();

         lRFQDefinition.setPurchasingContact( null );

         RFQDefinition lRDefinition = iRfqResourceBean.post( lRFQDefinition );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide purchasing contact", aE.getMessage() );
      }

   }


   /**
    *
    * Get RFQ object for testing
    *
    * @return RFQDefinition
    */
   private RFQDefinition getCreateDefaultRFQDefinition() {
      RFQDefinition lRFQDefinition = new RFQDefinition();

      lRFQDefinition.setCurrency( DB_ID_AND_CURRENCY2 );
      lRFQDefinition.setCustomerCode( DB_ID_AND_CUSTOMER_CODE2 );
      lRFQDefinition.setExchangeRate( EXCHANGE_RATE2 );
      lRFQDefinition.setPurchasingContact( PURCHASING_CONTACT2 );
      lRFQDefinition.setPurchasingContactId( PURCHASING_CONTACT_ID2 );
      lRFQDefinition.setRfqNo( RFQ_NO2 );
      lRFQDefinition.setStatus( RFQ_STATUS_OPEN );

      List<RFQLine> lRFQLines = new ArrayList<RFQLine>();

      RFQLine lRFQLine1 = new RFQLine();
      lRFQLine1.setPartId( PART_ID1 );

      lRFQLines.add( lRFQLine1 );

      RFQLine lRFQLine2 = new RFQLine();
      lRFQLine2.setPartId( PART_ID2 );

      lRFQLines.add( lRFQLine2 );

      RFQLine lRFQLine3 = new RFQLine();
      lRFQLine3.setPartId( PART_ID3 );

      lRFQLines.add( lRFQLine3 );

      lRFQDefinition.setRfqLines( lRFQLines );

      List<RFQVendor> lRfqVendors = new ArrayList<RFQVendor>();
      RFQVendor lRfqVendor1 = new RFQVendor();
      lRfqVendor1.setVendorId( RFQ_VENDOR1 );
      lRfqVendors.add( lRfqVendor1 );

      return lRFQDefinition;
   }


   private List<RFQLineVendor> constructModifiedRFQLineVendor() throws ParseException {

      List<RFQLineVendor> lRFQLineVendorList = constructExpectedRFQLineVendors();

      RFQLineVendor lRFQLineVendorModified = new RFQLineVendor();

      lRFQLineVendorModified.setVendorNote( LINE_VENDOR_NOTE1_MODIFIED );

      Date lEffectiveToDateModified;
      try {
         lEffectiveToDateModified =
               new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE1_MODIFIED );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQLineVendorModified.setEffectiveToDate( lEffectiveToDateModified );

      lRFQLineVendorModified.setQuoteUnitCode( QTY_UNIT_CODE1_MODIFIED );
      lRFQLineVendorModified.setConditionCode( CONDITION_CODE1_MODIFIED );
      lRFQLineVendorModified.setQuotePartId( QUOTE_PART_ID1_MODIFIED );
      lRFQLineVendorModified.setLeadTime( LEAD_TIME1_MODIFIED );
      lRFQLineVendorModified.setUnitPrice( UNIT_PRICE1_MODIFIED );
      lRFQLineVendorModified.setQuoteNo( QUOT_NO1_MODIFIED );
      lRFQLineVendorModified.setLinePrice( LINE_PRICE1_MODIFIED );

      Date lQuoteDateModified;
      try {
         lQuoteDateModified = new SimpleDateFormat( "yyyy-MM-dd" ).parse( QUOT_DATE1_MODIFIED );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQLineVendorModified.setQuoteDate( lQuoteDateModified );
      lRFQLineVendorModified.setQuoteQuantity( QUOT_QTY1_MODIFIED );
      lRFQLineVendorModified.setReqPartId( PART_ID1 );
      lRFQLineVendorModified.setRfqLineId( RFQ_LINE_ID1 );

      for ( RFQLineVendor lRfqLineVendor : lRFQLineVendorList ) {
         if ( lRfqLineVendor.getRfqLineId().equals( lRFQLineVendorModified.getRfqLineId() ) ) {
            lRfqLineVendor.setConditionCode( lRFQLineVendorModified.getConditionCode() );
            lRfqLineVendor.setEffectiveToDate( lRFQLineVendorModified.getEffectiveToDate() );
            lRfqLineVendor.setLeadTime( lRFQLineVendorModified.getLeadTime() );
            lRfqLineVendor.setQuoteDate( lRFQLineVendorModified.getQuoteDate() );
            lRfqLineVendor.setQuoteNo( lRFQLineVendorModified.getQuoteNo() );
            lRfqLineVendor.setQuotePartId( lRFQLineVendorModified.getQuotePartId() );
            lRfqLineVendor.setQuoteQuantity( lRFQLineVendorModified.getQuoteQuantity() );
            lRfqLineVendor.setQuoteUnitCode( lRFQLineVendorModified.getQuoteUnitCode() );
            lRfqLineVendor.setUnitPrice( lRFQLineVendorModified.getUnitPrice() );
            lRfqLineVendor.setVendorNote( lRFQLineVendorModified.getVendorNote() );
            lRfqLineVendor.setLinePrice( lRFQLineVendorModified.getLinePrice() );
         }
      }

      return lRFQLineVendorList;
   }


   private List<RFQLineVendor> constructModifiedRFQLineVendorWithNullLeadTime()
         throws ParseException {

      List<RFQLineVendor> lRFQLineVendorList = constructExpectedRFQLineVendors();

      RFQLineVendor lRFQLineVendorModified = new RFQLineVendor();

      lRFQLineVendorModified.setVendorNote( LINE_VENDOR_NOTE1_MODIFIED );

      Date lEffectiveToDateModified;
      try {
         lEffectiveToDateModified =
               new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE1_MODIFIED );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQLineVendorModified.setEffectiveToDate( lEffectiveToDateModified );

      lRFQLineVendorModified.setQuoteUnitCode( QTY_UNIT_CODE1_MODIFIED );
      lRFQLineVendorModified.setConditionCode( CONDITION_CODE1_MODIFIED );
      lRFQLineVendorModified.setQuotePartId( QUOTE_PART_ID1_MODIFIED );
      lRFQLineVendorModified.setLeadTime( null );
      lRFQLineVendorModified.setUnitPrice( UNIT_PRICE1_MODIFIED );
      lRFQLineVendorModified.setQuoteNo( QUOT_NO1_MODIFIED );

      Date lQuoteDateModified;
      try {
         lQuoteDateModified = new SimpleDateFormat( "yyyy-MM-dd" ).parse( QUOT_DATE1_MODIFIED );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQLineVendorModified.setQuoteDate( lQuoteDateModified );
      lRFQLineVendorModified.setQuoteQuantity( QUOT_QTY1_MODIFIED );
      lRFQLineVendorModified.setReqPartId( PART_ID1 );
      lRFQLineVendorModified.setRfqLineId( RFQ_LINE_ID1 );

      for ( RFQLineVendor lRfqLineVendor : lRFQLineVendorList ) {
         if ( lRfqLineVendor.getRfqLineId().equals( lRFQLineVendorModified.getRfqLineId() ) ) {
            lRfqLineVendor.setConditionCode( lRFQLineVendorModified.getConditionCode() );
            lRfqLineVendor.setEffectiveToDate( lRFQLineVendorModified.getEffectiveToDate() );
            lRfqLineVendor.setLeadTime( lRFQLineVendorModified.getLeadTime() );
            lRfqLineVendor.setQuoteDate( lRFQLineVendorModified.getQuoteDate() );
            lRfqLineVendor.setQuoteNo( lRFQLineVendorModified.getQuoteNo() );
            lRfqLineVendor.setQuotePartId( lRFQLineVendorModified.getQuotePartId() );
            lRfqLineVendor.setQuoteQuantity( lRFQLineVendorModified.getQuoteQuantity() );
            lRfqLineVendor.setQuoteUnitCode( lRFQLineVendorModified.getQuoteUnitCode() );
            lRfqLineVendor.setUnitPrice( lRFQLineVendorModified.getUnitPrice() );
            lRfqLineVendor.setVendorNote( lRFQLineVendorModified.getVendorNote() );
         }
      }

      return lRFQLineVendorList;
   }


   /**
    *
    * Test method for update RFQLineVendors - Success scenario
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLineVendorsSucess200() throws Exception {
      List<RFQLineVendor> lmodifiedRFQLineVendorList = constructModifiedRFQLineVendor();
      List<RFQLine> lmodifiedRfqLineList = constructExpectedRFQLines();
      RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

      lRequestRFQDefinitionObj.getRfqVendors().get( 0 ).setCurrencyCode( CURRENCY_CODE1_MODIFIED );
      lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
            .setRFQLineVendor( lmodifiedRFQLineVendorList );
      lRequestRFQDefinitionObj.setRfqLines( lmodifiedRfqLineList );

      RFQDefinition lRFQDefinition = iRfqResourceBean.put( ID1, lRequestRFQDefinitionObj );

      assertTrue( lRequestRFQDefinitionObj.equals( lRFQDefinition ) );

   }


   /**
    *
    * Test method for update RFQLineVendors - Fail scenario with Invalid RFQ Id
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLineVendorWithInvalidRFQId() throws Exception {

      try {
         List<RFQLineVendor> lmodifiedRFQLineVendorList = constructModifiedRFQLineVendor();
         RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setCurrencyCode( CURRENCY_CODE1_MODIFIED );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setRFQLineVendor( lmodifiedRFQLineVendorList );

         RFQDefinition lRFQDefinition =
               iRfqResourceBean.put( INVALID_ID, lRequestRFQDefinitionObj );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( INVALID_ID, aE.getId() );
      }
   }


   /**
    *
    * Test method for update RFQLineVendors - Fail scenario with Preconditioned fails giving lead
    * time as null when vendor quoted on a part.
    *
    * @throws ParseException
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLineVendorWithNullLeadTime()
         throws ParseException, AmApiResourceNotFoundException, AmApiBusinessException {

      List<RFQLineVendor> lmodifiedRFQLineVendorList =
            constructModifiedRFQLineVendorWithNullLeadTime();
      List<RFQLine> lmodifiedRfqLineList = constructExpectedRFQLines();
      RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

      lRequestRFQDefinitionObj.getRfqVendors().get( 0 ).setCurrencyCode( CURRENCY_CODE1_MODIFIED );
      lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
            .setRFQLineVendor( lmodifiedRFQLineVendorList );
      lRequestRFQDefinitionObj.setRfqLines( lmodifiedRfqLineList );

      try {
         RFQDefinition lRFQDefinition = iRfqResourceBean.put( ID1, lRequestRFQDefinitionObj );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertEquals( "Lead time is mandatory", aE.getMessage() );
      }
   }


   /**
    *
    * Test method for update RFQLineVendors - Fail scenario with Null RFQ Vendor Id.
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLineVendorWithNullRFQVendorId() throws Exception {
      try {
         List<RFQLineVendor> lmodifiedRFQLineVendorList = constructModifiedRFQLineVendor();
         List<RFQLine> lmodifiedRfqLineList = constructExpectedRFQLines();
         RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

         lRequestRFQDefinitionObj.getRfqVendors().get( 0 ).setVendorId( null );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setCurrencyCode( CURRENCY_CODE1_MODIFIED );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setRFQLineVendor( lmodifiedRFQLineVendorList );
         lRequestRFQDefinitionObj.setRfqLines( lmodifiedRfqLineList );

         RFQDefinition lRFQDefinition = iRfqResourceBean.put( ID1, lRequestRFQDefinitionObj );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Vendor could not be found in Maintenix: " + null, aE.getMessage() );
      }
   }


   /**
    *
    * Test send method for bad request
    *
    * @throws Exception
    */
   @Test
   public void testSendNullBadRequest() throws Exception {

      try {
         RFQDefinition lRFQDefinition = iRfqResourceBean.send( null );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide RFQ alt Id", aE.getMessage() );
      }

   }


   /**
    *
    * Test send method for not found
    *
    * @throws Exception
    */
   @Test
   public void testSendRFQDefinitionNotFound() throws Exception {
      try {
         RFQDefinition lRFQDefinition = iRfqResourceBean.send( INVALID_ID );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( INVALID_ID, aE.getId() );
      }

   }


   /**
    *
    * Test method for send RFQDefinition - Success scenario
    *
    * @throws Exception
    */
   @Test
   public void testSendRFQDefinitionSucess200() throws Exception {

      RFQDefinition lRFQDefinition = iRfqResourceBean.send( ID1 );

      Assert.assertEquals( RFQ_STATUS_SENT, lRFQDefinition.getStatus() );

   }


   /**
    *
    * Test method for update RFQLines - Fail scenario with Null RFQ Line Id.
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLineWithNullRFQLineId() throws Exception {
      try {
         List<RFQLineVendor> lmodifiedRFQLineVendorList = constructModifiedRFQLineVendor();
         List<RFQLine> lmodifiedRfqLineList = constructExpectedRFQLines();
         RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

         lmodifiedRfqLineList.get( 0 ).setRfqLineId( null );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setCurrencyCode( CURRENCY_CODE1_MODIFIED );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setRFQLineVendor( lmodifiedRFQLineVendorList );
         lRequestRFQDefinitionObj.setRfqLines( lmodifiedRfqLineList );

         RFQDefinition lRFQDefinition = iRfqResourceBean.put( ID1, lRequestRFQDefinitionObj );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Line ID cannot be found in RFQ: " + null, aE.getMessage() );
      }
   }


   /**
    *
    * Test method for update RFQLines - Fail scenario with RFQ Lines not belongs to RFQ
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLinesNotBelongsToRFQ() throws Exception {
      try {
         List<RFQLineVendor> lmodifiedRFQLineVendorList = constructModifiedRFQLineVendor();
         List<RFQLine> lmodifiedRfqLineList = constructExpectedRFQLines();
         RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

         lmodifiedRfqLineList.get( 0 ).setRfqLineId( RFQ_LINE_ID_NOT_BELONEGS_TO_RFQ );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setCurrencyCode( CURRENCY_CODE1_MODIFIED );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setRFQLineVendor( lmodifiedRFQLineVendorList );
         lRequestRFQDefinitionObj.setRfqLines( lmodifiedRfqLineList );

         RFQDefinition lRFQDefinition = iRfqResourceBean.put( ID1, lRequestRFQDefinitionObj );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Line ID cannot be found in RFQ: " + RFQ_LINE_ID_NOT_BELONEGS_TO_RFQ,
               aE.getMessage() );
      }
   }


   /**
    *
    *
    * Test method for create RFQ with part requests
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    * @throws ParseException
    *
    */
   @Test
   public void testCreateRFQFromPartRequestsSucess200()
         throws AmApiBusinessException, AmApiResourceNotFoundException, ParseException {
      RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();

      // RFQ Lines will be created based on Part Requests
      lRFQDefinition.setRfqLines( null );

      List<String> lPartRequestList = new ArrayList<String>();
      lPartRequestList.add( PART_REQUEST_ID1 );
      lPartRequestList.add( PART_REQUEST_ID2 );
      lRFQDefinition.setPartRequestIds( lPartRequestList );

      RFQDefinition lCreatedRFQDefinition = iRfqResourceBean.post( lRFQDefinition );

      RFQDefinitionSearchParameters lRFQDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      lRFQDefinitionSearchParameters.setLines( "true" );
      lRFQDefinitionSearchParameters.setVendors( "true" );
      lRFQDefinitionSearchParameters.setLineVendors( "true" );

      RFQDefinition lRFQDefinitionNew =
            iRfqResourceBean.get( lCreatedRFQDefinition.getId(), lRFQDefinitionSearchParameters );

      lRFQDefinition.setCurrency( CURRENCY2 );
      lRFQDefinition.setCustomerCode( CUSTOMER_CODE2 );

      Date lNeededByDate;
      try {
         lNeededByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( NEEDED_BY_DATE );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQDefinition.setNeededByDate( lNeededByDate );

      RFQLine lRfqLine1 = new RFQLine();
      lRfqLine1.setPartId( PART_ID4 );
      lRfqLine1.setPriorityCode( PRIORITY_CODE4 );
      lRfqLine1.setUnitCode( UNIT_CODE4 );

      RFQLine lRfqLine2 = new RFQLine();
      lRfqLine2.setPartId( PART_ID1 );
      lRfqLine2.setPriorityCode( PRIORITY_CODE1 );
      lRfqLine2.setUnitCode( UNIT_CODE1 );

      List<RFQLine> lRFQLineList = new ArrayList<RFQLine>();
      lRFQLineList.add( lRfqLine1 );
      lRFQLineList.add( lRfqLine2 );
      lRFQDefinition.setRfqLines( lRFQLineList );

      lRFQDefinitionNew.setId( null );

      if ( CollectionUtils.isNotEmpty( lRFQDefinitionNew.getRfqLines() ) ) {
         // We just want to assert only Part ID , Priority Code and Unit Code
         for ( RFQLine lRfqLine : lRFQDefinitionNew.getRfqLines() ) {
            lRfqLine.setRfqQuantity( null );
            lRfqLine.setRfqLineId( null );
            lRfqLine.setRfqLineNo( 0 );
         }
      }

      assertTrue( lRFQDefinition.equals( lRFQDefinitionNew ) );
   }


   /**
    *
    * Test method for create RFQ with part requests- Fail scenario with invalid part request id
    *
    * @throws AmApiBusinessException
    *
    * @throws Exception
    */
   @Test
   public void testCreateRFQFromPartRequestsWithInvalidPartRequestId() throws Exception {
      try {
         RFQDefinition lRFQDefinition = getCreateDefaultRFQDefinition();
         lRFQDefinition.setRfqLines( null );

         List<String> lPartRequestList = new ArrayList<String>();
         lPartRequestList.add( INVALID_PART_REQUEST_ID );
         lRFQDefinition.setPartRequestIds( lPartRequestList );

         RFQDefinition lCreatedRFQDefinition = iRfqResourceBean.post( lRFQDefinition );

      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( aE.getId(), INVALID_PART_REQUEST_ID );
      }
   }


   /**
    * Test method for update RFQLines - Fail scenario with invalid priority code
    *
    * @throws Exception
    */
   @Test
   public void testUpdateRFQLineWithInvalidPriorityCode() throws Exception {
      try {
         List<RFQLineVendor> lmodifiedRFQLineVendorList = constructModifiedRFQLineVendor();
         List<RFQLine> lmodifiedRfqLineList = constructExpectedRFQLines();
         RFQDefinition lRequestRFQDefinitionObj = constructExpectedResults();

         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setCurrencyCode( CURRENCY_CODE1_MODIFIED );
         lRequestRFQDefinitionObj.getRfqVendors().get( 0 )
               .setRFQLineVendor( lmodifiedRFQLineVendorList );
         lmodifiedRfqLineList.get( 0 ).setPriorityCode( INVALID_PRIORITY_CODE );
         lRequestRFQDefinitionObj.setRfqLines( lmodifiedRfqLineList );

         RFQDefinition lRFQDefinition = iRfqResourceBean.put( ID1, lRequestRFQDefinitionObj );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertEquals( "The Priority Code, " + INVALID_PRIORITY_CODE + ", is invalid.",
               aE.getMessage() );
      }

   }
}
