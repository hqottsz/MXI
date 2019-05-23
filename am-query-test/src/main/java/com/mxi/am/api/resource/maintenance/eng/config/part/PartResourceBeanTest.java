package com.mxi.am.api.resource.maintenance.eng.config.part;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.maintenance.eng.config.part.impl.PartResourceBean;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.fnc.InvalidAccountCodeException;
import com.mxi.mx.core.services.part.PartNoService;
import com.mxi.mx.core.services.part.PartVendorTO;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class PartResourceBeanTest extends ResourceBeanTest {

   private static final String INVALID_ERROR_MSG =
         "The '%s' refterm does not exist, and is therefore invalid";
   private static final String INVALID_ACC_CD = "The account code '%s' is not valid.";

   private static final String PART_NO_OEM = "PART_NO_OEM";
   private static final String PART_NAME = "Part Name";
   private static final String PART_ID = "00000000000000000000000000000002";

   private static final String INV_CLASS_CD_TRK = "TRK";
   private static final String INV_CLASS_CD_SER = "SER";

   private static final String MANUFACT_ID = "10000000000000000000000000000001";
   private static final String MANUFACT_CD_1 = "12345";

   private static final String FINANCIAL_CLASS = "EXPENDABLE";
   private static final String FINANCE_ACC = "EXPENSE-01";
   private static final Double SCRAP_RATE = 0.01;

   private static final String ABC_CLASS_D = "D";
   private static final String ABC_CLASS_C = "C";
   private static final String UNIT_CD = "EA";

   private static final String PART_STATUS_CD_ACTV = "ACTV";
   private static final String PART_STATUS_CD_BUILD = "BUILD";

   private static final String INVALID_CLASS_CD = "ICC";
   private static final String INVALID_UNIT_CD = "BOX";
   private static final String INVALID_FINANCIAL_CLASS = "IFC";
   private static final String INVALID_FINANCE_ACCOUNT = "IFA";
   private static final String INVALID_ABC_CLASS = "Z";

   private static final String PART_NO_OEM_1 = "PART-1";
   private static final String PART_NAME_1 = "Part 1";

   private static final String PART_NO_OEM_2 = "PART-2";
   private static final String PART_NAME_2 = "Part 2";

   private static final String PART_NO_OEM_3 = "PART-3";
   private static final String PART_NAME_3 = "Part 3";
   private static final Double SCRAP_RATE_3 = 3.67;

   private static final String NON_EXISTENT_PART_ID = "20000000000000000000000000000004";
   private static final String PREF_VENDOR_ID = "00000000000000000000000000000003";

   private static final String STOCK_ID = "00000000000000000000000000000005";
   private static final String MANUFACT_ID_2 = "10000000000000000000000000000005";
   private static final String MANUFACT_CD_2 = "6789";
   private static final String PART_NO_OEM_4 = "PART_NO";

   @Inject
   PartResourceBean iPartResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public LegacyKeyUtil iLegacyKeyUtil = new LegacyKeyUtil();


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );
      iPartResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testCreateTrackedPart()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Part lPartToCreate = defaultTrackedPart();
      lPartToCreate.setStatusCode( PART_STATUS_CD_BUILD );
      Part lCreatedPart = iPartResourceBean.create( lPartToCreate );

      Part lPart = iPartResourceBean.get( lCreatedPart.getId() );

      assertPart( lPartToCreate, lPart );

   }


   @Test
   public void testCreatePartWithInvalidInvClass() {

      try {
         Part lPart = defaultTrackedPart();
         lPart.setClassCode( INVALID_CLASS_CD );
         iPartResourceBean.create( lPart );

      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue(
               "Incorrect error class was returned. Expected: [InvalidReftermException]\n Actual:["
                     + e.getCause().getClass().toString() + "].",
               e.getCause().getClass().equals( InvalidReftermException.class ) );
         Assert.assertTrue(
               "Incorrect error message was returned. Expected: ["
                     + String.format( INVALID_ERROR_MSG, INVALID_CLASS_CD ) + "]\n Actual: ["
                     + e.getMessage() + "].",
               e.getMessage().contains( String.format( INVALID_ERROR_MSG, INVALID_CLASS_CD ) ) );
      }
   }


   @Test
   public void testCreatePartWithInvalidUnitCode() {

      try {
         Part lPart = defaultTrackedPart();
         lPart.setUnitCode( INVALID_UNIT_CD );
         iPartResourceBean.create( lPart );

      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue(
               "Incorrect error class was returned. Expected: [InvalidReftermException]\n Actual:["
                     + e.getCause().getClass().toString() + "].",
               e.getCause().getClass().equals( InvalidReftermException.class ) );
         Assert.assertTrue(
               "Incorrect error message was returned. Expected: ["
                     + String.format( INVALID_ERROR_MSG, INVALID_UNIT_CD ) + "]\n Actual: ["
                     + e.getMessage() + "].",
               e.getMessage().contains( String.format( INVALID_ERROR_MSG, INVALID_UNIT_CD ) ) );
      }
   }


   @Test
   public void testCreatePartWithInvalidFinancialClass() {

      try {
         Part lPart = defaultTrackedPart();
         lPart.setFinancialClassCode( INVALID_FINANCIAL_CLASS );
         iPartResourceBean.create( lPart );

      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue(
               "Incorrect error class was returned. Expected: [InvalidReftermException]\n Actual:["
                     + e.getCause().getClass().toString() + "].",
               e.getCause().getClass().equals( InvalidReftermException.class ) );
         Assert.assertTrue(
               "Incorrect error message was returned. Expected: ["
                     + String.format( INVALID_ERROR_MSG, INVALID_FINANCIAL_CLASS ) + "]\n Actual: ["
                     + e.getMessage() + "].",
               e.getMessage()
                     .contains( String.format( INVALID_ERROR_MSG, INVALID_FINANCIAL_CLASS ) ) );
      }
   }


   @Test
   public void testCreatePartWithInvalidFinanceAccount() {

      try {
         Part lPart = defaultTrackedPart();
         lPart.setFinanceAccountCode( INVALID_FINANCE_ACCOUNT );
         iPartResourceBean.create( lPart );

      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue(
               "Incorrect error class was returned. Expected: [InvalidAccountCodeException]\n Actual:["
                     + e.getCause().getClass().toString() + "].",
               e.getCause().getClass().equals( InvalidAccountCodeException.class ) );
         Assert.assertTrue(
               "Incorrect error message was returned. Expected: ["
                     + String.format( INVALID_ACC_CD, INVALID_FINANCE_ACCOUNT ) + "]\n Actual: ["
                     + e.getMessage() + "].",
               e.getMessage()
                     .contains( String.format( INVALID_ACC_CD, INVALID_FINANCE_ACCOUNT ) ) );
      }
   }


   @Test
   public void testCreatePartWithInvalidAbcClass() {

      try {
         Part lPart = defaultTrackedPart();
         lPart.setAbcClass( INVALID_ABC_CLASS );
         iPartResourceBean.create( lPart );

      } catch ( AmApiBusinessException e ) {
         Assert.assertTrue(
               "Incorrect error message was returned. Expected: ["
                     + String.format( INVALID_ERROR_MSG, INVALID_ABC_CLASS ) + "]\n Actual: ["
                     + e.getMessage() + "].",

               e.getMessage().contains( String.format( INVALID_ERROR_MSG, INVALID_ABC_CLASS ) ) );
      }
   }


   @Test
   public void testUpdatePart() throws AmApiResourceNotFoundException, AmApiBusinessException {
      Part lPartToUpdate = defaultTrackedPart();
      lPartToUpdate.setName( PART_NAME_1 );
      lPartToUpdate.setOemPartNumber( PART_NO_OEM_1 );

      lPartToUpdate = iPartResourceBean.create( lPartToUpdate );

      lPartToUpdate.setStatusCode( PART_STATUS_CD_ACTV );

      Part lUpdatedPart = iPartResourceBean.update( lPartToUpdate.getId(), lPartToUpdate );

      assertPart( lPartToUpdate, lUpdatedPart );

   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testUpdatePartPartNotFound()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Part lPartToUpdate = defaultTrackedPart();

      iPartResourceBean.update( NON_EXISTENT_PART_ID, lPartToUpdate );

   }


   @Test
   public void testGetPart() throws AmApiResourceNotFoundException, AmApiBusinessException,
         MandatoryArgumentException, KeyConversionException {
      Part lExpectedPart = defaultTrackedPart();
      lExpectedPart.setName( PART_NAME_1 );
      lExpectedPart.setOemPartNumber( PART_NO_OEM_1 );
      lExpectedPart.setStatusCode( PART_STATUS_CD_BUILD );

      Part lReturnedPart = iPartResourceBean.create( lExpectedPart );
      PartVendorTO lPartVendorTO = new PartVendorTO();
      lPartVendorTO.setPrefBool( true );
      lPartVendorTO.setVendorStatus( RefVendorStatusKey.APPROVED.getCd() );
      PartNoService.addPurchaseVendor(
            iLegacyKeyUtil.altIdToLegacyKey( lReturnedPart.getId(), PartNoKey.class ),
            iLegacyKeyUtil.altIdToLegacyKey( lExpectedPart.getPreferredVendorId(),
                  VendorKey.class ),
            lPartVendorTO );

      Part lFoundPart = iPartResourceBean.get( lExpectedPart.getId() );
      Assert.assertEquals( PREF_VENDOR_ID, lFoundPart.getPreferredVendorId() );
      assertPart( lExpectedPart, lFoundPart );

   }


   @Test
   public void testGetPartWithStockNoId() throws AmApiResourceNotFoundException {
      Part lExpectedPart = buildPartWithStockId();
      Part lActualPart = iPartResourceBean.get( PART_ID );
      assertPartWithStockId( lExpectedPart, lActualPart );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPartPartNotFound() throws AmApiResourceNotFoundException {
      iPartResourceBean.get( NON_EXISTENT_PART_ID );

   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchPart()
         throws AmApiBusinessException, MandatoryArgumentException, KeyConversionException {
      List<Part> lPartList = partList();
      Map<String, Part> lPartMap = new HashMap<String, Part>();

      PartVendorTO lPartVendorTO = new PartVendorTO();
      lPartVendorTO.setPrefBool( true );
      lPartVendorTO.setVendorStatus( RefVendorStatusKey.APPROVED.getCd() );

      for ( Part lPart : lPartList ) {
         Part lCreatedPart = iPartResourceBean.create( lPart );
         PartNoService.addPurchaseVendor(
               iLegacyKeyUtil.altIdToLegacyKey( lCreatedPart.getId(), PartNoKey.class ),
               iLegacyKeyUtil.altIdToLegacyKey( lPart.getPreferredVendorId(), VendorKey.class ),
               lPartVendorTO );
         lPartMap.put( lCreatedPart.getId(), lCreatedPart );
      }

      PartSearchParameters lPartSearchParameters = new PartSearchParameters();
      lPartSearchParameters.setManufactCode( MANUFACT_CD_1 );

      List<Part> lFoundPartList = iPartResourceBean.search( lPartSearchParameters );

      Assert.assertEquals( "Incorrect number of parts returned: ", 3, lFoundPartList.size() );

      for ( Part lFoundPart : lFoundPartList ) {
         Assert.assertEquals( "Incorrect Preferred Vendor ID: ", PREF_VENDOR_ID,
               lFoundPart.getPreferredVendorId() );
         assertPart( lPartMap.get( lFoundPart.getId() ), lFoundPart );
      }

   }


   private void assertPart( Part aExpectedPart, Part aActualPart ) {

      Assert.assertEquals( "Incorrect OEM Part Number: ", aExpectedPart.getOemPartNumber(),
            aActualPart.getOemPartNumber() );
      Assert.assertEquals( "Incorrect Class Code: ", aExpectedPart.getClassCode(),
            aActualPart.getClassCode() );
      Assert.assertEquals( "Incorrect Name: ", aExpectedPart.getName(), aActualPart.getName() );
      Assert.assertEquals( "Incorrect Financial Class Code: ",
            aExpectedPart.getFinancialClassCode(), aActualPart.getFinancialClassCode() );
      Assert.assertEquals( "Incorrect Finance Account Code: ",
            aExpectedPart.getFinanceAccountCode(), aActualPart.getFinanceAccountCode() );
      Assert.assertEquals( "Incorrect Scrap Rate: ", aExpectedPart.getScrapRate(),
            aActualPart.getScrapRate() );
      Assert.assertEquals( "Incorrect Manufacturer Code: ", aExpectedPart.getManufacturerCode(),
            aActualPart.getManufacturerCode() );
      Assert.assertEquals( "Incorrect Abc Class: ", aExpectedPart.getAbcClass(),
            aActualPart.getAbcClass() );
      Assert.assertEquals( "Incorrect Unit Code: ", aExpectedPart.getUnitCode(),
            aActualPart.getUnitCode() );
      Assert.assertEquals( "Incorrect Status Code: ", aExpectedPart.getStatusCode(),
            aActualPart.getStatusCode() );
   }


   private void assertPartWithStockId( Part aExpectedPart, Part aActualPart ) {

      Assert.assertEquals( aExpectedPart.getId(), aActualPart.getId() );
      Assert.assertEquals( aExpectedPart.getOemPartNumber(), aActualPart.getOemPartNumber() );
      Assert.assertEquals( aExpectedPart.getName(), aActualPart.getName() );
      Assert.assertEquals( aExpectedPart.getManufacturerCode(), aActualPart.getManufacturerCode() );
      Assert.assertEquals( aExpectedPart.getManufacturerId(), aActualPart.getManufacturerId() );
      Assert.assertEquals( aExpectedPart.getPreferredVendorId(),
            aActualPart.getPreferredVendorId() );
      Assert.assertEquals( aExpectedPart.getStockId(), aActualPart.getStockId() );
   }


   private static Part defaultTrackedPart() {
      Part lPart = new Part();
      lPart.setOemPartNumber( PART_NO_OEM );
      lPart.setClassCode( INV_CLASS_CD_TRK );
      lPart.setName( PART_NAME );
      lPart.setFinancialClassCode( FINANCIAL_CLASS );
      lPart.setFinanceAccountCode( FINANCE_ACC );
      lPart.setScrapRate( SCRAP_RATE );
      lPart.setManufacturerId( MANUFACT_ID );
      lPart.setManufacturerCode( MANUFACT_CD_1 );
      lPart.setAbcClass( ABC_CLASS_D );
      lPart.setUnitCode( UNIT_CD );
      lPart.setPreferredVendorId( PREF_VENDOR_ID );
      return lPart;
   }


   private static List<Part> partList() {
      List<Part> lPartList = new ArrayList<Part>();

      lPartList.add(
            buildPart( PART_NO_OEM_1, INV_CLASS_CD_TRK, PART_NAME_1, SCRAP_RATE, ABC_CLASS_D ) );
      lPartList.add(
            buildPart( PART_NO_OEM_2, INV_CLASS_CD_TRK, PART_NAME_2, SCRAP_RATE, ABC_CLASS_D ) );
      lPartList.add(
            buildPart( PART_NO_OEM_3, INV_CLASS_CD_SER, PART_NAME_3, SCRAP_RATE_3, ABC_CLASS_C ) );

      return lPartList;
   }


   private static Part buildPart( String aPartNoOem, String aClassCd, String aName,
         Double aScrapRate, String aAbcClass ) {
      Part lPart = new Part();
      lPart.setOemPartNumber( aPartNoOem );
      lPart.setClassCode( aClassCd );
      lPart.setName( aName );
      lPart.setFinancialClassCode( FINANCIAL_CLASS );
      lPart.setFinanceAccountCode( FINANCE_ACC );
      lPart.setScrapRate( aScrapRate );
      lPart.setManufacturerId( MANUFACT_ID );
      lPart.setManufacturerCode( MANUFACT_CD_1 );
      lPart.setAbcClass( aAbcClass );
      lPart.setUnitCode( UNIT_CD );
      lPart.setStatusCode( PART_STATUS_CD_BUILD );
      lPart.setPreferredVendorId( "00000000000000000000000000000003" );
      return lPart;
   }


   private static Part buildPartWithStockId() {
      Part lPart = new Part();
      lPart.setId( PART_ID );
      lPart.setPreferredVendorId( PREF_VENDOR_ID );
      lPart.setStockId( STOCK_ID );
      lPart.setManufacturerCode( MANUFACT_CD_2 );
      lPart.setManufacturerId( MANUFACT_ID_2 );
      lPart.setOemPartNumber( PART_NO_OEM_4 );
      lPart.setName( PART_NAME_1 );
      return lPart;
   }

}
