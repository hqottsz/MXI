package com.mxi.am.api.resource.materials.proc.vendor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

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
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.Contact;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.ShippingAddress;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval.Approval;
import com.mxi.am.api.resource.materials.proc.vendor.impl.VendorResourceBean;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Location;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class VendorResourceBeanTest extends ResourceBeanTest {

   private static final String CODE = "CODE";
   private static final String CODE_2 = "CODE_2";
   private static final String TYPE_CODE = "PURCHASE";
   private static final String CURRENCY_CODE = "CAD";
   private static final String ORGANIZATION_CODE = "MXI";
   private static final String CERT_NUMBER = "CERT_NUMBER";
   private static final Date CERT_EXP_DATE = new Date( 100000 );
   private static final Date APPROVAL_EXP_DATE = new Date( 90000 );
   private static final String APPROVAL_CODE = "DEFAULT";
   private static final String TERMS_AND_CONDITION = "TCC";
   private static final String MIN_PURCHASE_AMOUNT = "100";
   private static final String EXTERNAL_KEY = "EXTERNAL_KEY";
   private static final String STD_BORROW_RATE = "BRC";
   private static final String DEFAULT_AIRPORT = "DEFAULT_AIRPORT";
   private static final String NOTE = "NOTE";
   private static final String ADDRESS_1 = "ADDRESS_1";
   private static final String ADDRESS_2 = "ADDRESS_2";
   private static final String CITY = "CITY";
   private static final String COUNTRY = "USA";
   private static final String STATE = "DAL";
   private static final String ZIP = "ZIP";
   private static final String TIME_ZONE = "TIME_ZONE";
   private static final String VENDOR_NAME = "NAME";
   private static final String LOCATION_CODE = "DEFAULT_AIRPORT";
   private static final String DOCK_LOCATION_CODE = "DOCK_CODE";
   private static final String LOCATION_NAME = "Default Airport";
   private static final ShiftKey SHIFT_KEY = new ShiftKey( 11, 11 );
   private static final String CONTACT_NAME = "CONTACT_NAME";
   private static final String APPROVAL_STATUS = "APPROVED";
   private static final String APPROVAL_TYPE_CODE = "BORROW";
   private static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
   private static final String ACCOUNT_DESCRIPTION = "ACCOUNT_DESCRIPTION";

   @Inject
   private VendorResourceBean iVendorResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public LegacyKeyUtil iLegacyKeyUtil = new LegacyKeyUtil();

   private Vendor iVendor1;

   private Vendor iVendor2;


   @Before
   public void setUp()
         throws NamingException, MxException, KeyConversionException, AmApiBusinessException {

      InjectorContainer.get().injectMembers( this );
      iVendorResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

      loadData();

   }


   @Test
   public void testCreateVendorSuccess() throws AmApiBusinessException {
      iVendor2.setCode( CODE_2 );
      iVendor2.setId( null );
      Vendor lVendor = iVendorResourceBean.create( iVendor2 );
      assertVendorEquals( iVendor2, lVendor );
   }


   @Test
   public void testCreateVendorFailure() {
      Vendor lVendor = iVendor1;
      lVendor.setCurrencyCode( null );
      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException exception ) {
         Assert.assertTrue( "The error code [MXERR-10000] was expected in the message: "
               + exception.getMessage(), exception.getMessage().contains( "[MXERR-10000]" ) );
      }
   }


   @Test
   public void testCreateVendorDuplicateCodes() {
      Vendor lVendor = iVendor1;
      lVendor.setDefaultAirport( DEFAULT_AIRPORT );
      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-30147] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-30147]" ) );
      }
   }


   @Test
   public void testCreateVendorInvalidOrgCode() {
      Vendor lVendor = iVendor1;
      lVendor.setDefaultAirport( DEFAULT_AIRPORT );
      lVendor.setOrganizationCode( "INVALID" );

      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-10000] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-10000]" ) );
      }
   }


   @Test
   public void testCreateVendorInvalidCertNumber() {
      Vendor lVendor = iVendor1;
      lVendor.setDefaultAirport( DEFAULT_AIRPORT );
      lVendor.setCertificateNumber( "INVALID_CERT_NUMBER_TOO_LONG" );

      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-10003] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-10003]" ) );
      }
   }


   @Test
   public void testCreateVendorInvalidMinPurchaseAmount() {
      Vendor lVendor = iVendor1;
      lVendor.setDefaultAirport( DEFAULT_AIRPORT );
      lVendor.setMinPurchaseAmount( "-1" );

      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-10002] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-10002]" ) );
      }
   }


   @Test
   public void testCreateVendorInvalidLocationTypeCode() {
      Vendor lVendor = iVendor1;
      lVendor.setDefaultAirport( DOCK_LOCATION_CODE );

      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-30200] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-30200]" ) );
      }
   }


   @Test
   public void testCreateVendorInvalidCountry() {
      Vendor lVendor = iVendor1;
      lVendor.getShippingAddress().setCountry( "INVALID" );
      lVendor.setDefaultAirport( DEFAULT_AIRPORT );
      lVendor.setCode( "CODE_3" );

      try {
         iVendorResourceBean.create( lVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-30332] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-30332]" ) );
      }
   }


   @Test
   public void testGetVendorByIdSuccess() throws AmApiResourceNotFoundException {
      Vendor lSearchedVendor = iVendorResourceBean.get( iVendor1.getId() );
      assertVendorEquals( iVendor1, lSearchedVendor );
   }


   @Test
   public void testGetVendorByInvalidId() {
      try {
         iVendorResourceBean.get( "INVALID" );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( "VENDOR INVALID not found", aE.getMessage() );
      }
   }


   @Test
   public void testSearchVendorByCodeSuccess() {
      String lVendorCode = iVendor1.getCode();
      VendorSearchParameters lVendorSearchParameters = new VendorSearchParameters();
      List<String> lVendorCodes = new ArrayList<String>();
      lVendorCodes.add( lVendorCode );
      lVendorSearchParameters.setVendorCodes( lVendorCodes );
      List<Vendor> lSearchedVendor = iVendorResourceBean.search( lVendorSearchParameters );

      assertVendorEquals( iVendor1, lSearchedVendor.get( 0 ) );
   }


   @Test
   public void testSearchVendorByNullParams() {
      try {
         iVendorResourceBean.search( null );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals(
               "The required search paramaters are empty. This operation is not supported",
               aE.getMessage() );
      }
   }


   @Test
   public void testSearchVendorByEmptyParams() {
      VendorSearchParameters lVendorSearchParameters = new VendorSearchParameters();
      try {
         iVendorResourceBean.search( lVendorSearchParameters );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals(
               "The required search paramaters are empty. This operation is not supported",
               aE.getMessage() );
      }
   }


   @Test
   public void testUpdateSpec2000CommandsSuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      Vendor lNewVendor = iVendor1;
      lNewVendor.setSpec2000Enabled( true );
      List<String> lSpec2000Commands = new ArrayList<String>();
      lSpec2000Commands.add( "SPEC2K" );
      lNewVendor.setSpec2000Commands( lSpec2000Commands );
      Vendor lUpdatedVendor = iVendorResourceBean.update( iVendor1.getId(), lNewVendor );

      Assert.assertEquals( lNewVendor.getSpec2000Commands().get( 0 ), "SPEC2K" );
      assertVendorEquals( lNewVendor, lUpdatedVendor );
   }


   @Test
   public void testUpdateApprovalsSuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      Vendor lNewVendor = iVendor1;
      VendorOrgApproval lVendorOrgApproval = lNewVendor.getVendorOrgApprovals().get( 0 );
      Approval lApproval = lVendorOrgApproval.getApproval( APPROVAL_TYPE_CODE );
      lApproval.setStatus( APPROVAL_STATUS );
      lApproval.setApprovalExpiryDate( APPROVAL_EXP_DATE );

      Vendor lUpdatedVendor = iVendorResourceBean.update( iVendor1.getId(), lNewVendor );

      Assert.assertEquals( APPROVAL_STATUS, lUpdatedVendor.getVendorOrgApprovals().get( 0 )
            .getApproval( APPROVAL_TYPE_CODE ).getStatus() );
      Assert.assertEquals( APPROVAL_EXP_DATE, lUpdatedVendor.getVendorOrgApprovals().get( 0 )
            .getApproval( APPROVAL_TYPE_CODE ).getApprovalExpiryDate() );

      assertVendorEquals( lNewVendor, lUpdatedVendor );
   }


   @Test
   public void testUpdateVendorFailure() throws AmApiResourceNotFoundException {
      Vendor lVendor = iVendor1;
      lVendor.setCode( "WAY_TOO_LONG_VENDOR_CODE" );
      try {
         iVendorResourceBean.update( iVendor1.getId(), lVendor );
      } catch ( AmApiBusinessException exception ) {
         Assert.assertTrue( "The error code [MXERR-10003] was expected in the message: "
               + exception.getMessage(), exception.getMessage().contains( "[MXERR-10003]" ) );
      }
   }


   @Test
   public void testUpdateApprovalDateAfterCertDate() throws AmApiResourceNotFoundException {
      Vendor lNewVendor = iVendor1;
      VendorOrgApproval lVendorOrgApproval = lNewVendor.getVendorOrgApprovals().get( 0 );
      Approval lApproval = lVendorOrgApproval.getApproval( APPROVAL_TYPE_CODE );
      lApproval.setStatus( APPROVAL_STATUS );
      lApproval.setApprovalExpiryDate( new Date( 110000 ) );

      try {
         iVendorResourceBean.update( iVendor1.getId(), lNewVendor );
      } catch ( AmApiBusinessException aE ) {
         Assert.assertTrue(
               "The error code [MXERR-30077] was expected in the message: " + aE.getMessage(),
               aE.getMessage().contains( "[MXERR-30077]" ) );
      }
   }


   /**
    * used to check equality of 2 vendor objects
    *
    * @param aiVendor1
    * @param aSearchedVendor
    */
   private void assertVendorEquals( Vendor aExpectedVendor, Vendor aActualVendor ) {
      Assert.assertEquals( aExpectedVendor.getCode(), aActualVendor.getCode() );
      Assert.assertEquals( aExpectedVendor.getVendorTypeCode(), aActualVendor.getVendorTypeCode() );
      Assert.assertEquals( aExpectedVendor.getCurrencyCode(), aActualVendor.getCurrencyCode() );
      Assert.assertEquals( aExpectedVendor.getOrganizationCode(),
            aActualVendor.getOrganizationCode() );
      Assert.assertEquals( aExpectedVendor.getCertificateNumber(),
            aActualVendor.getCertificateNumber() );
      Assert.assertEquals( aExpectedVendor.getCertificateExpiryDate().toString(),
            aActualVendor.getCertificateExpiryDate().toString() );
      Assert.assertEquals( aExpectedVendor.getApprovalTypeCode(),
            aActualVendor.getApprovalTypeCode() );
      Assert.assertEquals( aExpectedVendor.getTermsAndConds(), aActualVendor.getTermsAndConds() );
      Assert.assertEquals( aExpectedVendor.isSpec2000Enabled(), aActualVendor.isSpec2000Enabled() );
      Assert.assertEquals( aExpectedVendor.getMinPurchaseAmount(),
            aActualVendor.getMinPurchaseAmount() );
      Assert.assertEquals( aExpectedVendor.getExternalKey(), aActualVendor.getExternalKey() );
      Assert.assertEquals( aExpectedVendor.getStdBorrowRate(), aActualVendor.getStdBorrowRate() );
      Assert.assertEquals( aExpectedVendor.getNotes(), aActualVendor.getNotes() );
      Assert.assertEquals( aExpectedVendor.getShippingAddress().getAddress1(),
            aActualVendor.getShippingAddress().getAddress1() );
      Assert.assertEquals( aExpectedVendor.getShippingAddress().getAddress2(),
            aActualVendor.getShippingAddress().getAddress2() );
      Assert.assertEquals( aExpectedVendor.getShippingAddress().getCity(),
            aActualVendor.getShippingAddress().getCity() );
      Assert.assertEquals( aExpectedVendor.getShippingAddress().getCountry(),
            aActualVendor.getShippingAddress().getCountry() );
      Assert.assertEquals( aExpectedVendor.getShippingAddress().getState(),
            aActualVendor.getShippingAddress().getState() );
      Assert.assertEquals( aExpectedVendor.getShippingAddress().getZip(),
            aActualVendor.getShippingAddress().getZip() );
      Assert.assertEquals( aExpectedVendor.getContacts().get( 0 ).getName(),
            aActualVendor.getContacts().get( 0 ).getName() );
      Assert.assertEquals( aExpectedVendor.getContacts().get( 0 ).isMainContact(),
            aActualVendor.getContacts().get( 0 ).isMainContact() );
      Assert.assertEquals( aExpectedVendor.getTimeZone(), aActualVendor.getTimeZone() );
      Assert.assertEquals( aExpectedVendor.getVendorName(), aActualVendor.getVendorName() );

      Assert.assertEquals( aExpectedVendor.getVendorAccounts(), aActualVendor.getVendorAccounts() );

   }


   /**
    * Loads the test data
    *
    * @throws AmApiBusinessException
    *
    */
   private void loadData() throws AmApiBusinessException {
      Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
            aLocation.setCode( LOCATION_CODE );
            aLocation.setName( LOCATION_NAME );
            aLocation.setOvernightShift( SHIFT_KEY );
            aLocation.setTimeZone( TimeZoneKey.TORONTO );
            aLocation.setHubLocation( null );
            aLocation.setParent( null );
            aLocation.setIsSupplyLocation( true );
            aLocation.setIsDefaultDock( true );
         }
      } );

      Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.DOCK );
            aLocation.setCode( DOCK_LOCATION_CODE );
            aLocation.setName( LOCATION_NAME );
            aLocation.setOvernightShift( SHIFT_KEY );
            aLocation.setTimeZone( TimeZoneKey.TORONTO );
            aLocation.setHubLocation( null );
            aLocation.setParent( null );
            aLocation.setIsSupplyLocation( true );
            aLocation.setIsDefaultDock( true );
         }
      } );
      Vendor lVendor = new Vendor();
      lVendor.setCode( CODE );
      lVendor.setVendorTypeCode( TYPE_CODE );
      lVendor.setCurrencyCode( CURRENCY_CODE );
      lVendor.setOrganizationCode( ORGANIZATION_CODE );
      lVendor.setCertificateNumber( CERT_NUMBER );
      lVendor.setCertificateExpiryDate( CERT_EXP_DATE );
      lVendor.setApprovalTypeCode( APPROVAL_CODE );
      lVendor.setTermsAndConds( TERMS_AND_CONDITION );
      lVendor.setSpec2000Enabled( true );
      lVendor.setMinPurchaseAmount( MIN_PURCHASE_AMOUNT );
      lVendor.setExternalKey( EXTERNAL_KEY );
      lVendor.setStdBorrowRate( STD_BORROW_RATE );
      lVendor.setDefaultAirport( DEFAULT_AIRPORT );
      lVendor.setNotes( NOTE );
      ShippingAddress lShippinAddress = new ShippingAddress();
      lShippinAddress.setAddress1( ADDRESS_1 );
      lShippinAddress.setAddress2( ADDRESS_2 );
      lShippinAddress.setCity( CITY );
      lShippinAddress.setCountry( COUNTRY );
      lShippinAddress.setState( STATE );
      lShippinAddress.setZip( ZIP );
      lVendor.setShippingAddress( lShippinAddress );
      lVendor.setTimeZone( TIME_ZONE );

      lVendor.setVendorName( VENDOR_NAME );

      List<Contact> lContacts = new ArrayList<Contact>();
      Contact lContact = new Contact();
      lContact.setName( CONTACT_NAME );
      lContact.setMainContact( true );
      lContacts.add( lContact );
      lVendor.setContacts( lContacts );

      VendorAccount lVendorAccount = new VendorAccount();
      lVendorAccount.setAccountNumber( ACCOUNT_NUMBER );
      lVendorAccount.setDefault( true );
      lVendorAccount.setDescription( ACCOUNT_DESCRIPTION );

      lVendor.addVendorAccount( lVendorAccount );

      iVendor1 = iVendorResourceBean.create( lVendor );
      iVendor2 = lVendor;
   }

}
