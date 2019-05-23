package com.mxi.am.api.resource.materials.asset.aircraft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.asset.aircraft.impl.AircraftResourceBean;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke Test for AircraftResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class AircraftResourceBeanTest extends ResourceBeanTest {

   @Inject
   AircraftResourceBean aircraftResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   private static final String ID1 = "601435E495494F34965B1588F5A6036B";
   private static final String NAME1 = "Boeing 767-232 - CTCx";
   private static final String LOCATION_ID1 = "2D00C440168543888573A63D52A62F9C";
   private static final String REGISTRATION_CODE1 = "CTCx";
   private static final String OEM_LINE_NO1 = "OEM Line No";
   private static final String OEM_VAR_NO1 = "OEM Var No";
   private static final String LAST_MODIFIED_DATE1 = "2018-01-12 00:00:00";
   private static final boolean LOCKED_BOOL1 = false;
   private static final String FIN_NUMBER1 = "Fin Number";
   private static final String OPERATING_STATUS_CODE1 = "OPEN";
   private static final String SERIAL_NO1 = "2345";
   private static final String OPERATING_STATUS_CODE_DESCRIPTION1 = "Open Fault";
   private static final String RECEIVED_DATE1 = "2007-01-15 00:00:00";
   private static final String IATA_CODE1 = null;
   private static final String ICAO_CODE1 = null;
   private static final String BARCODE1 = "I000ACKH";
   private static final String OPERATOR_CODE1 = null;
   private static final String PART_NO_OEM1 = "B767-232";
   private static final String PART_ID1 = "E9190224B0234161BB10DD724B33316B";
   private static final String OWNER_ID1 = "68FD8EDF9131483DBE3BA6ADAB1AE023";
   private static final String ASSEMBLY_ID1 = "92078DB90B2740589C72D651D415AB46";
   private static final String MANUFACTURER_CODE1 = "1000";
   private static final String CAPABILITY_CODE1 = "NORM";
   private static final String MANUFACTURED_DATE1 = "2007-01-11 00:00:00";
   private static final String INVENTORY_CONDITION_CODE1 = "REPREQ";
   private static final String APPLICABILITY_CODE1 = "123";
   private static final String ISSUE_TO_ACCOUNT_ID1 = "16579940B24411E884276D0900001111";
   private static final String ASSEMBLY_SUBTYPE_ID1 = "3397DFB7E7CA11E68E3BB36E9D4E73A5";
   private static final String FORECAST_MODULE_ID1 = "9A010C0C767D46038A6C9D6AA3914203";
   private static final boolean APPLY_MANUFACTURED_DATE_TO_SUB_COMPONENTS_BOOL1 = false;
   private static final boolean APPLY_RECEIVED_DATE_TO_SUB_COMPONENTS_BOOL1 = false;
   private static final String CREDIT_ACCOUNT_ID1 = null;

   private static final String ID2 = "905435E495494F34965B1884F5A60379";
   private static final String NAME2 = "Boeing 767-232 - ASD4";
   private static final String LOCATION_ID2 = "903DC440168543888573A635D2A62FC9";
   private static final String REGISTRATION_CODE2 = "ASD4";
   private static final String OEM_LINE_NO2 = "OEM Line No2";
   private static final String OEM_VAR_NO2 = "OEM Var No2";
   private static final String LAST_MODIFIED_DATE2 = "2018-02-24 00:00:00";
   private static final boolean LOCKED_BOOL2 = false;
   private static final String FIN_NUMBER2 = "Fin Number2";
   private static final String OPERATING_STATUS_CODE2 = "AOG";
   private static final String SERIAL_NO2 = "4567";
   private static final String OPERATING_STATUS_CODE_DESCRIPTION2 = "Aircraft on Ground";
   private static final String RECEIVED_DATE2 = "2006-06-20 00:00:00";
   private static final String IATA_CODE2 = null;
   private static final String ICAO_CODE2 = null;
   private static final String BARCODE2 = "J111ACKS";
   private static final String OPERATOR_CODE2 = null;
   private static final String PART_NO_OEM2 = "B767-232";
   private static final String PART_ID2 = "19E90224B0234161BB10DD724B333B62";
   private static final String OWNER_ID2 = "A34D8EDF9138983DBE3BA6ADAB1A7777";
   private static final String ASSEMBLY_ID2 = "92078DB90B2740589C72D651D415AB46";
   private static final String MANUFACTURER_CODE2 = "1001";
   private static final String CAPABILITY_CODE2 = "NORM";
   private static final String MANUFACTURED_DATE2 = "2006-06-16 00:00:00";
   private static final String INVENTORY_CONDITION_CODE2 = "REPREQ";
   private static final String APPLICABILITY_CODE2 = "346";
   private static final String ISSUE_TO_ACCOUNT_ID2 = "16579940B24411E884276D0900001111";
   private static final String ASSEMBLY_SUBTYPE_ID2 = "3397DFB7E7CA11E68E3BB36E9D4E73A5";
   private static final String FORECAST_MODULE_ID2 = "89810C0C767D460387AC9D6AA3914921";
   private static final boolean APPLY_MANUFACTURED_DATE_TO_SUB_COMPONENTS_BOOL2 = false;
   private static final boolean APPLY_RECEIVED_DATE_TO_SUB_COMPONENTS_BOOL2 = false;
   private static final String CREDIT_ACCOUNT_ID2 = null;

   private static final String ID3 = "406B7E4DFD164C9691B0D6314EB3384D";
   private static final String NAME3 = "Boeing 767-232 - QWE4";
   private static final String LOCATION_ID3 = "903DC440168543888573A635D2A62FD9";
   private static final String REGISTRATION_CODE3 = "QWE4";
   private static final String OEM_LINE_NO3 = "OEM Line No3";
   private static final String OEM_VAR_NO3 = "OEM Var No3";
   private static final String LAST_MODIFIED_DATE3 = "2018-02-24 00:00:00";
   private static final boolean LOCKED_BOOL3 = true;
   private static final String FIN_NUMBER3 = "Fin Number3";
   private static final String OPERATING_STATUS_CODE3 = "AOG";
   private static final String SERIAL_NO3 = "2345";
   private static final String OPERATING_STATUS_CODE_DESCRIPTION3 = "Aircraft on Ground";
   private static final String RECEIVED_DATE3 = "2006-06-20 00:00:00";
   private static final String IATA_CODE3 = null;
   private static final String ICAO_CODE3 = null;
   private static final String BARCODE3 = "J111ASD4";
   private static final String OPERATOR_CODE3 = null;
   private static final String PART_NO_OEM3 = "B767-232";
   private static final String PART_ID3 = "E9190224B0234161BB10DD724B33316B";
   private static final String OWNER_ID3 = "A34D8EDF9138983DBE3BA6ADAB1A6666";
   private static final String ASSEMBLY_ID3 = "92078DB90B2740589C72D651D415AB46";
   private static final String MANUFACTURER_CODE3 = "1000";
   private static final String CAPABILITY_CODE3 = "NORM";
   private static final String MANUFACTURED_DATE3 = "2006-06-16 00:00:00";
   private static final String INVENTORY_CONDITION_CODE3 = "ARCHIVE";
   private static final String APPLICABILITY_CODE3 = "347";
   private static final String ISSUE_TO_ACCOUNT_ID3 = "16579940B24411E884276D0900001111";
   private static final String ASSEMBLY_SUBTYPE_ID3 = "3397DFB7E7CA11E68E3BB36E9D4E73A5";
   private static final String FORECAST_MODULE_ID3 = "89810C0C767D460387AC9D6AA3914922";
   private static final boolean APPLY_MANUFACTURED_DATE_TO_SUB_COMPONENTS_BOOL3 = false;
   private static final boolean APPLY_RECEIVED_DATE_TO_SUB_COMPONENTS_BOOL3 = false;
   private static final String CREDIT_ACCOUNT_ID3 = null;

   private static final String INVALID_ID = "100000000000000000000000001";
   private static final String INVALID_BARCODE1 = "barcode1";
   private static final String INVALID_BARCODE2 = "barcode2";
   private static final List<String> INVALID_BARCODE_LIST =
         Arrays.asList( INVALID_BARCODE1, INVALID_BARCODE2 );

   private static final List<String> BARCODE_LIST = Arrays.asList( BARCODE1, BARCODE2 );
   private static final List<String> FIN_NUMBER_LIST = Arrays.asList( FIN_NUMBER1, FIN_NUMBER2 );
   private static final List<String> REGISTRATION_CODE_LIST =
         Arrays.asList( REGISTRATION_CODE1, REGISTRATION_CODE2 );
   private static final List<String> OPERATIONAL_CODE_LIST =
         Arrays.asList( OPERATING_STATUS_CODE1, OPERATING_STATUS_CODE2 );
   private static final List<String> PART_NO_ID_LIST = Arrays.asList( PART_ID1, PART_ID2 );
   private static final List<String> ASSEMBLY_ID_LIST = Arrays.asList( ASSEMBLY_ID1, ASSEMBLY_ID2 );
   private static final boolean LOCKED_STATUS_PARAM = false;

   private static final String INVALID_INVENTORY_CONDITION_CODE = "INVALID_CD";

   private static final String ERROR_MESSAGE = "Inventory Condition Code "
         + INVALID_INVENTORY_CONDITION_CODE + " is invalid for unarchiving aircraft";

   private Aircraft expectedAircraft1;
   private Aircraft expectedAircraft2;
   private Aircraft expectedAircraft3;


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), AircraftResourceBeanTest.class );
      initializeSecurityContext();
   }


   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule( () -> {
            disableTriggers();
         }, () -> {
            enableTriggers();
         } );


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      aircraftResourceBean.setEJBContext( ejbContext );
      constructExpectedResults();
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   static void enableTriggers() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_INV_AC_REG" );
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_INV_INV" );
   }


   static void disableTriggers() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_INV_AC_REG" );
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_INV_INV" );
   }


   private void constructExpectedResults() throws ParseException {
      expectedAircraft1 = new Aircraft();

      expectedAircraft1.setId( ID1 );
      expectedAircraft1.setName( NAME1 );
      expectedAircraft1.setLocation( LOCATION_ID1 );
      expectedAircraft1.setOemLineNumber( OEM_LINE_NO1 );
      expectedAircraft1.setOemVariableNumber( OEM_VAR_NO1 );
      expectedAircraft1.setLastModifiedDate( simpleDateFormat.parse( LAST_MODIFIED_DATE1 ) );
      expectedAircraft1.setLocked( LOCKED_BOOL1 );
      expectedAircraft1.setSerialNumber( SERIAL_NO1 );
      expectedAircraft1.setOperatingStatusCodeDescription( OPERATING_STATUS_CODE_DESCRIPTION1 );
      expectedAircraft1.setReceivedDate( simpleDateFormat.parse( RECEIVED_DATE1 ) );
      expectedAircraft1.setIcaoCode( ICAO_CODE1 );
      expectedAircraft1.setBarcode( BARCODE1 );
      expectedAircraft1.setOperatorCode( OPERATOR_CODE1 );
      expectedAircraft1.setIataCode( IATA_CODE1 );
      expectedAircraft1.setPartNumberOem( PART_NO_OEM1 );
      expectedAircraft1.setFinNumber( FIN_NUMBER1 );
      expectedAircraft1.setPartId( PART_ID1 );
      expectedAircraft1.setOwnerId( OWNER_ID1 );
      expectedAircraft1.setAssemblyId( ASSEMBLY_ID1 );
      expectedAircraft1.setManufacturerCode( MANUFACTURER_CODE1 );
      expectedAircraft1.setCapabilityCode( CAPABILITY_CODE1 );
      expectedAircraft1.setManufacturedDate( simpleDateFormat.parse( MANUFACTURED_DATE1 ) );
      expectedAircraft1.setRegistrationCode( REGISTRATION_CODE1 );
      expectedAircraft1.setOperatingStatusCode( OPERATING_STATUS_CODE1 );
      expectedAircraft1.setInventoryConditionCode( INVENTORY_CONDITION_CODE1 );
      expectedAircraft1.setApplicabilityCode( APPLICABILITY_CODE1 );
      expectedAircraft1.setIssueToAccountId( ISSUE_TO_ACCOUNT_ID1 );
      expectedAircraft1.setAssemblySubTypeId( ASSEMBLY_SUBTYPE_ID1 );
      expectedAircraft1.setForecastModelId( FORECAST_MODULE_ID1 );
      expectedAircraft1.setApplyManufacturedDtToSubComponents(
            APPLY_MANUFACTURED_DATE_TO_SUB_COMPONENTS_BOOL1 );
      expectedAircraft1
            .setApplyReceivedDtToSubComponents( APPLY_RECEIVED_DATE_TO_SUB_COMPONENTS_BOOL1 );
      expectedAircraft1.setCreditAccountId( CREDIT_ACCOUNT_ID1 );

      expectedAircraft2 = new Aircraft();

      expectedAircraft2.setId( ID2 );
      expectedAircraft2.setName( NAME2 );
      expectedAircraft2.setLocation( LOCATION_ID2 );
      expectedAircraft2.setOemLineNumber( OEM_LINE_NO2 );
      expectedAircraft2.setOemVariableNumber( OEM_VAR_NO2 );
      expectedAircraft2.setLastModifiedDate( simpleDateFormat.parse( LAST_MODIFIED_DATE2 ) );
      expectedAircraft2.setLocked( LOCKED_BOOL2 );
      expectedAircraft2.setSerialNumber( SERIAL_NO2 );
      expectedAircraft2.setOperatingStatusCodeDescription( OPERATING_STATUS_CODE_DESCRIPTION2 );
      expectedAircraft2.setReceivedDate( simpleDateFormat.parse( RECEIVED_DATE2 ) );
      expectedAircraft2.setIcaoCode( ICAO_CODE2 );
      expectedAircraft2.setBarcode( BARCODE2 );
      expectedAircraft2.setOperatorCode( OPERATOR_CODE2 );
      expectedAircraft2.setIataCode( IATA_CODE2 );
      expectedAircraft2.setPartNumberOem( PART_NO_OEM2 );
      expectedAircraft2.setFinNumber( FIN_NUMBER2 );
      expectedAircraft2.setPartId( PART_ID2 );
      expectedAircraft2.setOwnerId( OWNER_ID2 );
      expectedAircraft2.setAssemblyId( ASSEMBLY_ID2 );
      expectedAircraft2.setManufacturerCode( MANUFACTURER_CODE2 );
      expectedAircraft2.setCapabilityCode( CAPABILITY_CODE2 );
      expectedAircraft2.setManufacturedDate( simpleDateFormat.parse( MANUFACTURED_DATE2 ) );
      expectedAircraft2.setRegistrationCode( REGISTRATION_CODE2 );
      expectedAircraft2.setOperatingStatusCode( OPERATING_STATUS_CODE2 );
      expectedAircraft2.setInventoryConditionCode( INVENTORY_CONDITION_CODE2 );
      expectedAircraft2.setApplicabilityCode( APPLICABILITY_CODE2 );
      expectedAircraft2.setIssueToAccountId( ISSUE_TO_ACCOUNT_ID2 );
      expectedAircraft2.setAssemblySubTypeId( ASSEMBLY_SUBTYPE_ID2 );
      expectedAircraft2.setForecastModelId( FORECAST_MODULE_ID2 );
      expectedAircraft2.setApplyManufacturedDtToSubComponents(
            APPLY_MANUFACTURED_DATE_TO_SUB_COMPONENTS_BOOL2 );
      expectedAircraft2
            .setApplyReceivedDtToSubComponents( APPLY_RECEIVED_DATE_TO_SUB_COMPONENTS_BOOL2 );
      expectedAircraft2.setCreditAccountId( CREDIT_ACCOUNT_ID2 );

      expectedAircraft3 = new Aircraft();

      expectedAircraft3.setId( ID3 );
      expectedAircraft3.setName( NAME3 );
      expectedAircraft3.setLocation( LOCATION_ID3 );
      expectedAircraft3.setOemLineNumber( OEM_LINE_NO3 );
      expectedAircraft3.setOemVariableNumber( OEM_VAR_NO3 );
      expectedAircraft3.setLastModifiedDate( simpleDateFormat.parse( LAST_MODIFIED_DATE3 ) );
      expectedAircraft3.setLocked( LOCKED_BOOL3 );
      expectedAircraft3.setSerialNumber( SERIAL_NO3 );
      expectedAircraft3.setOperatingStatusCodeDescription( OPERATING_STATUS_CODE_DESCRIPTION3 );
      expectedAircraft3.setReceivedDate( simpleDateFormat.parse( RECEIVED_DATE3 ) );
      expectedAircraft3.setIcaoCode( ICAO_CODE3 );
      expectedAircraft3.setBarcode( BARCODE3 );
      expectedAircraft3.setOperatorCode( OPERATOR_CODE3 );
      expectedAircraft3.setIataCode( IATA_CODE3 );
      expectedAircraft3.setPartNumberOem( PART_NO_OEM3 );
      expectedAircraft3.setFinNumber( FIN_NUMBER3 );
      expectedAircraft3.setPartId( PART_ID3 );
      expectedAircraft3.setOwnerId( OWNER_ID3 );
      expectedAircraft3.setAssemblyId( ASSEMBLY_ID3 );
      expectedAircraft3.setManufacturerCode( MANUFACTURER_CODE3 );
      expectedAircraft3.setCapabilityCode( CAPABILITY_CODE3 );
      expectedAircraft3.setManufacturedDate( simpleDateFormat.parse( MANUFACTURED_DATE3 ) );
      expectedAircraft3.setRegistrationCode( REGISTRATION_CODE3 );
      expectedAircraft3.setOperatingStatusCode( OPERATING_STATUS_CODE3 );
      expectedAircraft3.setInventoryConditionCode( INVENTORY_CONDITION_CODE3 );
      expectedAircraft3.setApplicabilityCode( APPLICABILITY_CODE3 );
      expectedAircraft3.setIssueToAccountId( ISSUE_TO_ACCOUNT_ID3 );
      expectedAircraft3.setAssemblySubTypeId( ASSEMBLY_SUBTYPE_ID3 );
      expectedAircraft3.setForecastModelId( FORECAST_MODULE_ID3 );
      expectedAircraft3.setApplyManufacturedDtToSubComponents(
            APPLY_MANUFACTURED_DATE_TO_SUB_COMPONENTS_BOOL3 );
      expectedAircraft3
            .setApplyReceivedDtToSubComponents( APPLY_RECEIVED_DATE_TO_SUB_COMPONENTS_BOOL3 );
      expectedAircraft3.setCreditAccountId( CREDIT_ACCOUNT_ID3 );

   }


   @Test
   @CSIContractTest( { Project.SWA_AC_ATTRIBUTES, Project.SWA_AC_CAPABILITIES,
         Project.SWA_FAULT_STATUS, Project.SWA_FQC, Project.SWA_WP_STATUS, Project.UPS } )
   public void get_success() throws Exception {

      Aircraft aircraft = aircraftResourceBean.get( ID1 );
      assertTrue( expectedAircraft1.equals( aircraft ) );

   }


   @Test
   public void get_failure_aircraftNotFound() throws Exception {
      try {
         aircraftResourceBean.get( INVALID_ID );
      } catch ( AmApiResourceNotFoundException exception ) {
         Assert.assertEquals( INVALID_ID, exception.getId() );
      }

   }


   @Test
   public void get_failure_nullIdParameter() throws Exception {
      try {
         aircraftResourceBean.get( null );
      } catch ( AmApiResourceNotFoundException exception ) {
         Assert.assertEquals( null, exception.getId() );
      }

   }


   @Test
   public void search_success_allParameters() {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setBarCodes( BARCODE_LIST );
      aircraftSearchParameters.setRegistrationCodes( REGISTRATION_CODE_LIST );
      aircraftSearchParameters.setFinNumbers( FIN_NUMBER_LIST );
      aircraftSearchParameters.setAircraftStatus( LOCKED_STATUS_PARAM );
      aircraftSearchParameters.setPartNoIds( PART_NO_ID_LIST );
      aircraftSearchParameters.setOperationalCodes( OPERATIONAL_CODE_LIST );
      aircraftSearchParameters.setAssemblyIds( ASSEMBLY_ID_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );
      assertEquals( 2, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );

   }


   @Test
   @CSIContractTest( Project.SWA_AC_STATUS )
   public void search_success_byLockedStatus() {
      AircraftSearchParameters params = new AircraftSearchParameters();
      params.setAircraftStatus( true );

      List<Aircraft> aircraftList = aircraftResourceBean.search( params );
      assertEquals( 1, aircraftList.size() );

      assertEquals( "The aircraft that was returned did not match the expected result: ",
            expectedAircraft3, aircraftList.get( 0 ) );

   }


   @Test
   @CSIContractTest( { Project.SWA_ACODS, Project.SWA_FQC, } )
   public void search_success_byBarcodeList() {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setBarCodes( BARCODE_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );

      assertEquals( 2, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );

   }


   @Test
   @CSIContractTest( { Project.SWA_ACODS, Project.SWA_FQC, Project.SWA_MOPP } )
   public void search_success_byFinNumberList() throws Exception {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setFinNumbers( FIN_NUMBER_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );

      assertEquals( 2, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );
   }


   @Test
   @CSIContractTest( { Project.SWA_ACODS, Project.SWA_FQC, Project.SWA_MOPP, Project.UPS } )
   public void search_success_byRegistrationCodeList() throws Exception {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setRegistrationCodes( REGISTRATION_CODE_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );

      assertEquals( 2, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );
   }


   @Test
   @CSIContractTest( { Project.SWA_AC_STATUS } )
   public void search_success_byOperationaLCodeList() throws Exception {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setOperationalCodes( OPERATIONAL_CODE_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );

      assertEquals( 3, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );
      assertTrue( aircraftList.contains( expectedAircraft3 ) );
   }


   @Test
   public void search_success_byPartNumberIdList() throws Exception {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setPartNoIds( PART_NO_ID_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );

      assertEquals( 3, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );
      assertTrue( aircraftList.contains( expectedAircraft3 ) );
   }


   @Test
   public void search_success_byAssemblyIdList() {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setAssemblyIds( ASSEMBLY_ID_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );

      assertEquals( 3, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );
      assertTrue( aircraftList.contains( expectedAircraft3 ) );
   }


   @Test
   @CSIContractTest( { Project.SWA_AC_ATTRIBUTES, Project.SWA_AC_CAPABILITIES } )
   public void search_success_nullParametersReturnAllAircraft() {
      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setBarCodes( null );
      aircraftSearchParameters.setRegistrationCodes( null );
      aircraftSearchParameters.setFinNumbers( null );
      aircraftSearchParameters.setAircraftStatus( null );
      aircraftSearchParameters.setPartNoIds( null );
      aircraftSearchParameters.setOperationalCodes( null );
      aircraftSearchParameters.setAssemblyIds( null );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );
      assertEquals( 3, aircraftList.size() );
      assertTrue( aircraftList.contains( expectedAircraft1 ) );
      assertTrue( aircraftList.contains( expectedAircraft2 ) );
      assertTrue( aircraftList.contains( expectedAircraft3 ) );
   }


   @Test
   public void search_failure_byBarcodeList() throws Exception {

      AircraftSearchParameters aircraftSearchParameters = new AircraftSearchParameters();
      aircraftSearchParameters.setBarCodes( INVALID_BARCODE_LIST );

      List<Aircraft> aircraftList = aircraftResourceBean.search( aircraftSearchParameters );
      assertEquals( 0, aircraftList.size() );

   }


   @Test
   public void update_failure_aircraftNotFound()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Aircraft aircraft = new Aircraft();

      try {
         aircraftResourceBean.update( INVALID_ID, aircraft );
         Assert.fail();
      } catch ( AmApiResourceNotFoundException exception ) {
         Assert.assertEquals( INVALID_ID, exception.getId() );
      }

   }


   @Test
   public void update_failure_invalidInventoryConditionCode()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Aircraft aircraft = aircraftResourceBean.get( ID3 );
      aircraft.setInventoryConditionCode( INVALID_INVENTORY_CONDITION_CODE );

      try {
         aircraftResourceBean.update( ID3, aircraft );
         Assert.fail();
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertTrue( exception.getMessage().contains( ERROR_MESSAGE ) );
      }

   }

}
