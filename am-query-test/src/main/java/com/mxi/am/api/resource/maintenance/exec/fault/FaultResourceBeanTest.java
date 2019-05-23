package com.mxi.am.api.resource.maintenance.exec.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiAuthorizationException;
import com.mxi.am.api.exception.AmApiInternalServerException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.provider.serialization.Iso8601DateTime;
import com.mxi.am.api.resource.maintenance.exec.fault.Fault.Deadline;
import com.mxi.am.api.resource.maintenance.exec.fault.impl.FaultResourceBean;
import com.mxi.am.api.resource.maintenance.exec.fault.resultevent.ResultEvent;
import com.mxi.am.api.resource.maintenance.exec.task.Labour;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Test suite for {@link FaultResourceBean} class.
 */
@RunWith( MockitoJUnitRunner.class )
public class FaultResourceBeanTest extends ResourceBeanTest {

   private static final String NAME = "MXI Fault";
   private static final String TASK_ID = "00000000000000000000000000000001";
   private static final String INVALID_TASK_ID = "00005000000000000000000000000001";
   private static final String FAULT_ID = "00000000000000000000000000000001";
   private static final String BARCODE = "AAAA00";
   private static final boolean HISTORIC = false;
   private static final String STATUS_ACTV = "ACTV";
   private static final String STATUS_CFCERT = "CERTIFY";
   private static final String SEVERITY_MINOR = "MINOR";
   private static final String HIGHEST_INV_ID = "00000000000000000000000000000002";
   private static final String INVENTORY_ID = "00000000000000000000000000000002";
   private static final String DEFERRAL_REASON = "0";
   private static final Double ESTIMATED_DURATION2 = new Double( 1.0 );
   private static final String ESTIMATED_DURATION = "1.0";
   private static final String FAILED_SYSTEM_ASSEMBLY_BOM_CODE = "108";
   private static final String FAILED_SYSTEM = "TRK-on-TRK-on-TRK Middle (PN: A0000021, SN: XXX)";
   private static final String FAILED_SYSTEM_ID = "00000000000000000000000000000002";
   private static final String LOGBOOK_TYPE = "CABIN";
   private static final String OPERATIONAL_RESTRICTION = "CoreTesting";
   private static final String PARAMETER1 = "AOH";
   private static final String PARAMETER_DESCRIPTION1 = "APU Hours";
   private static final Float TSN_QT1 = new Float( 2348.1 );
   private static final Float TSO_QT1 = new Float( 2346.1 );
   private static final Float TSI_QT1 = new Float( 2347.1 );
   private static final String PARAMETER2 = "AOC";
   private static final String PARAMETER_DESCRIPTION2 = "APU Cycles";
   private static final Float TSN_QT2 = new Float( 2358.1 );
   private static final Float TSO_QT2 = new Float( 2356.1 );
   private static final Float TSI_QT2 = new Float( 2357.1 );
   private static final boolean DRIVING_TASK_BOOL1 = true;
   private static final Double USAGE_DEADLINE1 = 2000.0;
   private static final Double USAGE_START1 = 5.0;
   private static final Double USAGE_REMAINING_QT1 = 247.75;
   private static final Double DEVIATION_QT1 = 113.0;
   private static final Double NOTIFY_QT1 = 113.0;
   private static final String SCHED_DEAD_DATE1 = "2007-01-11T05:00:00Z";
   private static final String START_DATE1 = "2007-01-11T05:00:00Z";
   private static final String EXTENDED_DEADLINE_DATE1 = "2007-05-04T05:00:00Z";
   private static final boolean DRIVING_TASK_BOOL2 = false;
   private static final Double USAGE_DEADLINE2 = 3000.0;
   private static final Double USAGE_START2 = 5.0;
   private static final Double USAGE_REMAINING_QT2 = 347.75;
   private static final Double DEVIATION_QT2 = 313.0;
   private static final Double NOTIFY_QT2 = 313.0;
   private static final String SCHED_DEAD_DATE2 = "2007-01-21T05:00:00Z";
   private static final String START_DATE2 = "2007-01-21T05:00:00Z";
   private static final String EXTENDED_DEADLINE_DATE2 = "2007-11-30T05:00:00Z";
   private static final String DEFERRAL_REFERENCE = "Deferral reference";
   private static final String WORKPACKAGE_ID = "00000000000000000000000000000003";
   private static final String FOUND_PERSON_ID = "001CFAD3986445E09B51A4B4690FB4D2";
   private static final String COMPLETED_PERSON_ID = "00BE91E190AF4E029197BA072D0EF023";
   private static final String SCHEDULING_RULES_START_CALENDAR = "2007-01-11T05:00:00Z";
   private static final Float SCHEDULING_RULES_START_USAGE = new Float( 5.0 );
   private static final String LABOUR_ID1 = "00000000000000000000000000000004";
   private static final String LABOUR_SKILL_CD1 = "AFT";
   private static final Float SCHEDULED_HOURS1 = new Float( 1 );
   private static final Float ACTUAL_HOURS1 = new Float( 3 );
   private static final Float ADJUSTED_BILLING_HOURS1 = new Float( 5 );
   private static final String LABOUR_ID2 = "00000000000000000000000000000005";
   private static final String LABOUR_SKILL_CD2 = "SHMTL";
   private static final Float SCHEDULED_HOURS2 = new Float( 2 );
   private static final Float ACTUAL_HOURS2 = new Float( 4 );
   private static final Float ADJUSTED_BILLING_HOURS2 = new Float( 6 );
   private static final String EXTERNAL_REFERENCE = "041523091";
   private static final String DEFERRAL_AUTHORIZATION = "1234 Deferral Code";
   private static final String COMPLETED_ON_DATE = "2007-01-13T05:00:00Z";
   private static final String SCHED_START_DATE = "2007-01-15T05:00:00Z";
   private static final String SCHED_END_DATE = "2007-01-20T05:00:00Z";
   private static final String FOUND_DATE = "2007-01-16T05:00:00Z";
   private static final String COMPLETED_DATE = "2007-01-13T05:00:00Z";
   private static final String TASK_PRIORITY_CD = "NORMAL";
   private static final String LEG_ID = "86F5B4EA3ECA448CB2E2D9439C61FC4C";
   private static final String DEFERRAL_CLASS = "MELA";
   private static final String REF_RESULT_EVENT_DESCRIPTION1 = "Aborted Approach";
   private static final String RESULT_CODE1 = "AAAA01";
   private static final String REF_RESULT_EVENT_DESCRIPTION2 = "Aborted Approached";
   private static final String RESULT_CODE2 = "AAAA02";
   private static final String CORRECTIVE_ACTION = "Opened panels";
   private static final String FAULT_CODE1 = "FC001";
   private static final String LOGBOOK_REFERENCE = "API_LOGBOOK_REF_102";
   private static final String LOGBOOK_REFERENCE2 = "API_LOGBOOK_REF_104";
   private static final String DESCRIPTION = "MXI Fault Description";
   private static final String LOGBOOK_TYPE_CABIN = "CABIN";
   private static final String FLIGHT_LEG_ID = "00000000000000000000000000000004";
   private static final String FLIGHT_PHASE_ICL = "ICL";
   private static final String FLIGHT_PHASE_CR = "CR";
   private static final String FAULT_SOURCE_PILOT = "PILOT";
   private static final String FAKE_WORKPACKAGE_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String INVALID_FAULT_ID = "00000000000110000000000000000001";
   private static final String FOUND_IN_TASKID = "00000000000000000000000000000001";
   private static final String DUE_DATE_RANGE_START = "2007-01-10T05:00:00Z";
   private static final String DUE_DATE_RANGE_END = "2007-01-12T05:00:00Z";
   private static final String COMPLETED_DATE_RANGE_START = "2007-01-12T05:00:00Z";
   private static final String COMPLETED_DATE_RANGE_END = "2007-01-14T05:00:00Z";
   private static final String INVALID_FLIGHT_PHASE = "FFFFF";
   private static final String INVALID_LOGBOOK_TYPE = "FFFFF";
   private static final String INVALID_INVENTORY_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String INVALID_USER_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String AUTHORIZED_FOUND_BY_PERSON_ID = "17D32432CDB811E8B4DF9F760000122A";
   private static final String USAGE_LAST_MODIFIED_DATE = "2938-08-24T00:00:00Z";
   private static final String LABOUR_LAST_MODIFIED_DATE1 = "2007-01-11T00:00:00Z";
   private static final String LABOUR_LAST_MODIFIED_DATE2 = "2938-08-23T00:00:00Z";
   private static final String DEADLINE_LAST_MODIFIED_DATE1 = "2938-08-22T00:00:00Z";
   private static final String DEADLINE_LAST_MODIFIED_DATE2 = "2018-07-21T00:00:00Z";

   // TIBR_EVT_EVENT trigger disabling was removed because it affected the core post method testing.
   // Last_modified_date was checked with a future date in xml in a different table
   private static final String LAST_MODIFIED_DATE = "2938-08-25T00:00:00Z";

   FaultResourceBean faultResourceBean;

   @Mock
   private EJBContext mockEJBSecurityContext;

   @Mock
   private Principal mockPrincipal;

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), FaultResourceBeanTest.class );
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
   public void setUp() throws MxException {

      faultResourceBean = InjectorContainer.get().getInstance( FaultResourceBean.class );
      faultResourceBean.setEJBContext( mockEJBSecurityContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( mockEJBSecurityContext.getCallerPrincipal() ).thenReturn( mockPrincipal );
      Mockito.when( mockPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   static void enableTriggers() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SCHED_STASK" );
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SD_FAULT" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_EVT_SCHED_DEAD" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_EVT_EVENT_REL" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SCHED_LABOUR" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_EVT_INV_USAGE" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SD_FAULT_RESULT" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SCHED_LABOUR_ROLE_STATUS" );

   }


   static void disableTriggers() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SCHED_STASK" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SD_FAULT" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_EVT_SCHED_DEAD" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_EVT_EVENT_REL" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SCHED_LABOUR" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_EVT_INV_USAGE" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SD_FAULT_RESULT" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SCHED_LABOUR_ROLE_STATUS" );
   }


   /**
    * Test get method for success
    *
    * @throws Exception
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void get_success() throws Exception {

      Fault fault = faultResourceBean.get( FAULT_ID );

      AssertFault( getFault(), fault );

   }


   /**
    *
    * Test get method for not found
    *
    * @throws Exception
    */
   @Test
   public void get_failure_faultNotFound() throws Exception {

      try {
         faultResourceBean.get( INVALID_FAULT_ID );
         Assert.fail();
      } catch ( AmApiResourceNotFoundException e ) {
         Assert.assertEquals( INVALID_FAULT_ID, e.getId() );
      }

   }


   @Test
   @CSIContractTest( Project.SWA_MOPP )
   public void search_success() throws ParseException {
      FaultSearchParameters faultSearchParameters = new FaultSearchParameters();

      List<String> highestInvIds = new ArrayList<String>();
      highestInvIds.add( HIGHEST_INV_ID );
      List<String> invIds = new ArrayList<String>();
      invIds.add( HIGHEST_INV_ID );
      List<String> severityCodes = new ArrayList<String>();
      severityCodes.add( SEVERITY_MINOR );
      List<String> statusCodes = new ArrayList<String>();
      statusCodes.add( STATUS_ACTV );

      faultSearchParameters.setHighestInvIds( highestInvIds );
      faultSearchParameters.setInvIds( invIds );
      faultSearchParameters.setSeverityCodes( severityCodes );
      faultSearchParameters.setStatusCodes( statusCodes );
      faultSearchParameters.setHistoric( HISTORIC );
      faultSearchParameters.setDueDtRangeStart( new Iso8601DateTime( DUE_DATE_RANGE_START ) );
      faultSearchParameters.setDueDtRangeEnd( new Iso8601DateTime( DUE_DATE_RANGE_END ) );
      faultSearchParameters
            .setCompletedDtRangeStart( new Iso8601DateTime( COMPLETED_DATE_RANGE_START ) );
      faultSearchParameters
            .setCompletedDtRangeEnd( new Iso8601DateTime( COMPLETED_DATE_RANGE_END ) );
      List<Fault> faultList = faultResourceBean.search( faultSearchParameters );

      Assert.assertFalse( CollectionUtils.isEmpty( faultList ) );

      Assert.assertEquals( 1, faultList.size() );
   }


   @Test
   @CSIContractTest( { Project.SWA_WP_STATUS, Project.UPS } )
   public void search_success_byWorkpackageId() throws ParseException {
      FaultSearchParameters faultSearchParameters = new FaultSearchParameters();
      faultSearchParameters.setWorkpackageId( WORKPACKAGE_ID );
      List<Fault> faultList = faultResourceBean.search( faultSearchParameters );

      if ( CollectionUtils.isEmpty( faultList ) ) {
         Assert.fail( "Retrieved fault list was empty." );
      }

      Assert.assertEquals( "Retrieved fault list should contain only 1 fault.", 1,
            faultList.size() );
      AssertFault( getFault(), faultList.get( 0 ) );

   }


   @Test
   public void search_success_noResultsByIncorrectWorkpackageId() {
      FaultSearchParameters faultSearchParameters = new FaultSearchParameters();
      faultSearchParameters.setWorkpackageId( FAKE_WORKPACKAGE_ID );
      List<Fault> faultList = faultResourceBean.search( faultSearchParameters );

      assertTrue(
            "Expected the returned list to be empty, but has a size of " + faultList.size() + ".",
            faultList.isEmpty() );

   }


   @Test
   @CSIContractTest( Project.SWA_FAULT_STATUS )
   public void search_success_byBarcode() throws ParseException {
      FaultSearchParameters faultParameters = new FaultSearchParameters();
      faultParameters.setBarcode( BARCODE );

      List<Fault> returnedFaults = faultResourceBean.search( faultParameters );
      assertEquals( "Unexpected number of returned faults: ", 1, returnedFaults.size() );

      AssertFault( getFault(), returnedFaults.get( 0 ) );
   }


   @Test
   public void create_failure_invalidFlightPhase() {
      Fault newFault = getNewFault();
      newFault.setFoundDuringFlightPhase( INVALID_FLIGHT_PHASE );

      try {
         faultResourceBean.post( newFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "Flight phase " + INVALID_FLIGHT_PHASE + " is invalid" ) );
      }
   }


   @Test
   public void create_failure_invalidLogbookType() {
      Fault newFault = getNewFault();
      newFault.setLogbookType( INVALID_LOGBOOK_TYPE );

      try {
         faultResourceBean.post( newFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "Logbook type " + INVALID_LOGBOOK_TYPE + " is invalid" ) );
      }
   }


   @Test
   public void create_failure_invalidInventoryId() {
      Fault newFault = getNewFault();
      newFault.setInventoryId( INVALID_INVENTORY_ID );

      try {
         faultResourceBean.post( newFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Inventory key not found for id " + INVALID_INVENTORY_ID ) );
      }
   }


   @Test
   public void create_failure_invalidFailedSystemId() {
      Fault newFault = getNewFault();
      newFault.setFailedSystemId( INVALID_INVENTORY_ID );

      try {
         faultResourceBean.post( newFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Inventory key not found for id " + INVALID_INVENTORY_ID ) );
      }
   }


   @Test
   public void create_failure_invalidFoundByPerson() {
      Fault newFault = getNewFault();
      newFault.setFoundByPerson( INVALID_USER_ID );

      try {
         faultResourceBean.post( newFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "User key not found for id " + INVALID_USER_ID ) );
      }
   }


   @Test
   public void create_success_nullFoundByPerson() {
      Fault newFault = getNewFault();
      newFault.setFoundByPerson( null );

      Fault createdFault = faultResourceBean.post( newFault, new FaultSearchParameters() );
      Assert.assertEquals( AUTHORIZED_FOUND_BY_PERSON_ID, createdFault.getFoundByPerson() );
   }


   @Test
   public void create_success() {

      // Create Fault object with new data
      Fault newFault = getNewFault();

      Fault createdFault = faultResourceBean.post( newFault, new FaultSearchParameters() );

      // Validate all the values inserted
      Assert.assertEquals( newFault.getLogbookReference(), createdFault.getLogbookReference() );
      Assert.assertEquals( newFault.getDescription(), createdFault.getDescription() );
      Assert.assertEquals( newFault.getFailedSystemId(), createdFault.getFailedSystemId() );
      Assert.assertEquals( newFault.getLogbookType(), createdFault.getLogbookType() );
      Assert.assertEquals( newFault.getInventoryId(), createdFault.getInventoryId() );
      Assert.assertEquals( newFault.getFailedSystemId(), createdFault.getFailedSystemId() );
      Assert.assertEquals( newFault.getFlightLegId(), createdFault.getFlightLegId() );
      Assert.assertEquals( newFault.getFoundDuringFlightPhase(),
            createdFault.getFoundDuringFlightPhase() );
      Assert.assertEquals( newFault.getFoundDate().toString(),
            createdFault.getFoundDate().toString() );
      Assert.assertEquals( newFault.getFoundByPerson(), createdFault.getFoundByPerson() );
      Assert.assertEquals( newFault.getFaultSource(), createdFault.getFaultSource() );
      Assert.assertEquals( newFault.getFaultCode(), createdFault.getFaultCode() );
      Assert.assertEquals( newFault.getName(), createdFault.getName() );
      Assert.assertEquals( newFault.getSeverity(), createdFault.getSeverity() );
   }


   @Test
   public void update_failure_nullParameters() {
      try {
         faultResourceBean.put( null, null, null );
         Assert.fail();
      } catch ( AmApiResourceNotFoundException e ) {
         Assert.assertEquals( null, e.getId() );
         Assert.assertEquals( "FAULT", e.getIdType() );
      }
   }


   @Test
   public void update_failure_invalidFaultId() {
      try {
         Fault updateFault = getNewFault();
         faultResourceBean.put( INVALID_TASK_ID, updateFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiResourceNotFoundException e ) {
         Assert.assertEquals( INVALID_TASK_ID, e.getId() );
         Assert.assertEquals( "FAULT", e.getIdType() );
      }
   }


   @Test
   public void update_failure_invalidFlightPhase()
         throws AmApiAuthorizationException, AmApiResourceNotFoundException {
      Fault updateFault = getNewFault();
      updateFault.setFoundDuringFlightPhase( INVALID_FLIGHT_PHASE );

      try {
         faultResourceBean.put( TASK_ID, updateFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "Flight phase " + INVALID_FLIGHT_PHASE + " is invalid" ) );
      }
   }


   @Test
   public void update_failure_invalidLogbookType()
         throws AmApiAuthorizationException, AmApiResourceNotFoundException {
      Fault updateFault = getNewFault();
      updateFault.setLogbookType( INVALID_LOGBOOK_TYPE );

      try {
         faultResourceBean.put( TASK_ID, updateFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "Logbook type " + INVALID_LOGBOOK_TYPE + " is invalid" ) );
      }
   }


   @Test
   public void update_failure_invalidInventoryId()
         throws AmApiAuthorizationException, AmApiResourceNotFoundException {
      Fault updateFault = getNewFault();
      updateFault.setInventoryId( INVALID_INVENTORY_ID );

      try {
         faultResourceBean.put( TASK_ID, updateFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Inventory key not found for id " + INVALID_INVENTORY_ID ) );
      }
   }


   @Test
   public void update_failure_invalidFailedSystemId()
         throws AmApiAuthorizationException, AmApiResourceNotFoundException {
      Fault newFault = getNewFault();
      newFault.setFailedSystemId( INVALID_INVENTORY_ID );

      try {
         faultResourceBean.put( TASK_ID, newFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Inventory key not found for id " + INVALID_INVENTORY_ID ) );
      }
   }


   @Test
   public void update_failure_invalidFoundByPerson()
         throws AmApiAuthorizationException, AmApiResourceNotFoundException {
      Fault lUpdateFault = getNewFault();
      lUpdateFault.setFoundByPerson( INVALID_USER_ID );

      try {
         faultResourceBean.put( TASK_ID, lUpdateFault, new FaultSearchParameters() );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "User key not found for id " + INVALID_USER_ID ) );
      }
   }


   @Test
   public void get_v2_success() throws Exception {
      FaultV2 faultV2 = faultResourceBean.getV2( FAULT_ID );
      AssertFaultV2( getFaultV2(), faultV2 );
   }


   @Test
   public void get_v2_failure_faultNotFound() throws Exception {
      try {
         faultResourceBean.getV2( INVALID_FAULT_ID );
         Assert.fail( "Exception expected." );
      } catch ( AmApiResourceNotFoundException e ) {
         Assert.assertEquals( INVALID_FAULT_ID, e.getId() );
      }
   }


   @Test
   public void create_v2_failure_invalidLogbookType() {
      FaultV2 newFaultV2 = getNewFaultV2();
      newFaultV2.setLogbookTypeCode( INVALID_LOGBOOK_TYPE );
      try {
         faultResourceBean.createV2( newFaultV2 );
         Assert.fail();
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "Logbook type " + INVALID_LOGBOOK_TYPE + " is invalid" ) );
      }
   }


   @Test
   public void create_v2_failure_invalidInventoryId() {
      FaultV2 newFaultV2 = getNewFaultV2();
      newFaultV2.setInventoryId( INVALID_INVENTORY_ID );
      try {
         faultResourceBean.createV2( newFaultV2 );
         Assert.fail( "Exception expected." );
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Inventory key not found for id " + INVALID_INVENTORY_ID ) );
      }
   }


   @Test
   public void create_v2_failure_invalidFailedSystemId() {
      FaultV2 newFaultV2 = getNewFaultV2();
      newFaultV2.setFailedSystemId( INVALID_INVENTORY_ID );
      try {
         faultResourceBean.createV2( newFaultV2 );
         Assert.fail( "Exception expected." );
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Inventory key not found for id " + INVALID_INVENTORY_ID ) );
      }
   }


   @Test
   public void create_v2_failure_invalidFoundByUserId() {
      FaultV2 newFaultV2 = getNewFaultV2();
      newFaultV2.setFoundByUserId( INVALID_USER_ID );
      try {
         faultResourceBean.createV2( newFaultV2 );
         Assert.fail( "Exception expected." );
      } catch ( AmApiInternalServerException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue(
               e.getMessage().contains( "User key not found for id " + INVALID_USER_ID ) );
      }
   }


   @Test
   public void create_v2_success_nullFoundByUser() {
      FaultV2 newFaultV2 = getNewFaultV2();
      newFaultV2.setFoundByUserId( null );
      FaultV2 createdFaultV2 = faultResourceBean.createV2( newFaultV2 );
      Assert.assertEquals( AUTHORIZED_FOUND_BY_PERSON_ID, createdFaultV2.getFoundByUserId() );
   }


   @Test
   public void create_v2_success() {
      // Create Fault object with new data
      FaultV2 newFaultV2 = getNewFaultV2();
      FaultV2 createdFaultV2 = faultResourceBean.createV2( newFaultV2 );

      // Validate all the values inserted
      Assert.assertEquals( newFaultV2.getDescription(), createdFaultV2.getDescription() );
      Assert.assertEquals( newFaultV2.getFailedSystemId(), createdFaultV2.getFailedSystemId() );
      Assert.assertEquals( newFaultV2.getFaultCode(), createdFaultV2.getFaultCode() );
      Assert.assertEquals( newFaultV2.getFaultSourceCode(), createdFaultV2.getFaultSourceCode() );
      Assert.assertEquals( newFaultV2.getFoundByUserId(), createdFaultV2.getFoundByUserId() );
      Assert.assertEquals( newFaultV2.getFoundDate().toString(),
            createdFaultV2.getFoundDate().toString() );
      Assert.assertEquals( newFaultV2.getInventoryId(), createdFaultV2.getInventoryId() );
      Assert.assertEquals( newFaultV2.getName(), createdFaultV2.getName() );
      Assert.assertEquals( newFaultV2.getLogbookReference(), createdFaultV2.getLogbookReference() );
      Assert.assertEquals( newFaultV2.getLogbookTypeCode(), createdFaultV2.getLogbookTypeCode() );
      Assert.assertEquals( newFaultV2.getSeverityCode(), createdFaultV2.getSeverityCode() );
   }


   private Fault getFault() throws ParseException {
      Fault fault = new Fault();

      fault.setName( NAME );
      fault.setBarcode( BARCODE );
      fault.setHistoric( HISTORIC );
      fault.setId( TASK_ID );
      fault.setStatus( STATUS_CFCERT );
      fault.setSeverity( SEVERITY_MINOR );
      fault.setHighestInventoryId( HIGHEST_INV_ID );
      fault.setLogbookReference( LOGBOOK_REFERENCE2 );
      fault.setDescription( DESCRIPTION );

      fault.setInventoryId( INVENTORY_ID );
      fault.setDeferralReason( DEFERRAL_REASON );
      fault.setFoundByPerson( FOUND_PERSON_ID );
      fault.setCompletedByPerson( COMPLETED_PERSON_ID );
      fault.setEstimatedDuration( ESTIMATED_DURATION );
      fault.setFoundInTaskBarcode( BARCODE );
      fault.setFailedSystemAssemblyBomCode( FAILED_SYSTEM_ASSEMBLY_BOM_CODE );
      fault.setPriorityCode( TASK_PRIORITY_CD );
      fault.setFlightLegId( LEG_ID );
      fault.setFaultId( FAULT_ID );
      fault.setFailedSystem( FAILED_SYSTEM );
      fault.setDeferralClass( DEFERRAL_CLASS );
      fault.setSeverityType( SEVERITY_MINOR );
      fault.setFaultSource( FAULT_SOURCE_PILOT );
      fault.setFailedSystemId( INVENTORY_ID );

      fault.setSchedulingRulesStartCalendar( getFormattedDate( SCHEDULING_RULES_START_CALENDAR ) );
      fault.setCompletedOnDate( getFormattedDate( COMPLETED_ON_DATE ) );
      fault.setEstimatedDueDate( getFormattedDate( SCHED_DEAD_DATE1 ) );
      fault.setSchedStartDate( getFormattedDate( SCHED_START_DATE ) );
      fault.setSchedEndDate( getFormattedDate( SCHED_END_DATE ) );
      fault.setCompletedDate( getFormattedDate( COMPLETED_DATE ) );
      fault.setFoundDate( getFormattedDate( FOUND_DATE ) );

      fault.setSchedulingRulesStartUsage( SCHEDULING_RULES_START_USAGE );
      fault.setCorrectiveAction( CORRECTIVE_ACTION );
      fault.setFaultCode( FAULT_CODE1 );
      fault.setFoundDuringFlightPhase( FLIGHT_PHASE_CR );
      fault.setFoundInTaskId( FOUND_IN_TASKID );
      fault.setWorkpackageId( WORKPACKAGE_ID );
      fault.setLogbookType( LOGBOOK_TYPE );
      fault.setOperationalRestriction( OPERATIONAL_RESTRICTION );
      fault.setExternalReference( EXTERNAL_REFERENCE );
      fault.setDeferralAuthorization( DEFERRAL_AUTHORIZATION );
      fault.setDeferralReference( DEFERRAL_REFERENCE );
      fault.setLastModifiedDate( getFormattedDate( LAST_MODIFIED_DATE ) );

      fault.setUsageDeadlines( getFaultDeadlines() );

      fault.setUsages( getFaultUsages() );

      fault.setLabour( getLabours() );

      fault.setResultEvents( getResultEvents() );

      return fault;
   }


   private List<Deadline> getFaultDeadlines() throws ParseException {

      Deadline deadline1 = new Deadline();
      deadline1.setUsageParameter( PARAMETER1 );
      deadline1.setUsageDeadline( USAGE_DEADLINE1 );
      deadline1.setUsageRemaining( USAGE_REMAINING_QT1 );
      deadline1.setDeviationQuantity( DEVIATION_QT1 );
      deadline1.setDrivingTaskBool( DRIVING_TASK_BOOL1 );
      deadline1.setNotificationQuantity( NOTIFY_QT1 );
      deadline1.setDueDate( getFormattedDate( SCHED_DEAD_DATE1 ) );
      deadline1.setEstimatedDate( getFormattedDate( SCHED_DEAD_DATE1 ) );
      deadline1.setExtendedDeadlineDate( getFormattedDate( EXTENDED_DEADLINE_DATE1 ) );
      deadline1.setUsageStart( USAGE_START1 );
      deadline1.setStartDate( getFormattedDate( START_DATE1 ) );
      deadline1.setLastModifiedDate( getFormattedDate( DEADLINE_LAST_MODIFIED_DATE1 ) );

      Deadline deadline2 = new Deadline();
      deadline2.setUsageParameter( PARAMETER2 );
      deadline2.setUsageDeadline( USAGE_DEADLINE2 );
      deadline2.setUsageRemaining( USAGE_REMAINING_QT2 );
      deadline2.setDeviationQuantity( DEVIATION_QT2 );
      deadline2.setDrivingTaskBool( DRIVING_TASK_BOOL2 );
      deadline2.setNotificationQuantity( NOTIFY_QT2 );
      deadline2.setDueDate( getFormattedDate( SCHED_DEAD_DATE2 ) );
      deadline2.setEstimatedDate( getFormattedDate( SCHED_DEAD_DATE2 ) );
      deadline2.setExtendedDeadlineDate( getFormattedDate( EXTENDED_DEADLINE_DATE2 ) );
      deadline2.setUsageStart( USAGE_START2 );
      deadline2.setStartDate( getFormattedDate( START_DATE2 ) );
      deadline2.setLastModifiedDate( getFormattedDate( DEADLINE_LAST_MODIFIED_DATE2 ) );

      List<Deadline> faultDeadlines = new ArrayList<Deadline>();
      faultDeadlines.add( deadline1 );
      faultDeadlines.add( deadline2 );

      return faultDeadlines;
   }


   private List<Usage> getFaultUsages() throws ParseException {

      Usage usage1 = new Usage();
      usage1.setParameter( PARAMETER1 );
      usage1.setParameterDescription( PARAMETER_DESCRIPTION1 );
      usage1.setTSI( TSI_QT1 );
      usage1.setTSN( TSN_QT1 );
      usage1.setTSO( TSO_QT1 );
      usage1.setLastModifiedDate( getFormattedDate( USAGE_LAST_MODIFIED_DATE ) );

      Usage usage2 = new Usage();
      usage2.setParameter( PARAMETER2 );
      usage2.setParameterDescription( PARAMETER_DESCRIPTION2 );
      usage2.setTSI( TSI_QT2 );
      usage2.setTSN( TSN_QT2 );
      usage2.setTSO( TSO_QT2 );
      usage2.setLastModifiedDate( getFormattedDate( USAGE_LAST_MODIFIED_DATE ) );

      List<Usage> faultUsages = new ArrayList<Usage>();
      faultUsages.add( usage1 );
      faultUsages.add( usage2 );

      return faultUsages;
   }


   private List<Labour> getLabours() throws ParseException {

      Labour labour1 = new Labour();
      labour1.setId( LABOUR_ID1 );
      labour1.setActualHours( ACTUAL_HOURS1 );
      labour1.setAdjustedBillingHours( ADJUSTED_BILLING_HOURS1 );
      labour1.setScheduledHours( SCHEDULED_HOURS1 );
      labour1.setSkill( LABOUR_SKILL_CD1 );
      labour1.setLastModifiedDate( getFormattedDate( LABOUR_LAST_MODIFIED_DATE1 ) );

      Labour labour2 = new Labour();
      labour2.setId( LABOUR_ID2 );
      labour2.setActualHours( ACTUAL_HOURS2 );
      labour2.setAdjustedBillingHours( ADJUSTED_BILLING_HOURS2 );
      labour2.setScheduledHours( SCHEDULED_HOURS2 );
      labour2.setSkill( LABOUR_SKILL_CD2 );
      labour2.setLastModifiedDate( getFormattedDate( LABOUR_LAST_MODIFIED_DATE2 ) );

      List<Labour> labours = new ArrayList<Labour>();
      labours.add( labour1 );
      labours.add( labour2 );

      return labours;
   }


   private List<ResultEvent> getResultEvents() throws ParseException {

      ResultEvent resultEvent1 = new ResultEvent();
      resultEvent1.setFaultId( FAULT_ID );
      resultEvent1.setDescription( REF_RESULT_EVENT_DESCRIPTION1 );
      resultEvent1.setResultCode( RESULT_CODE1 );
      resultEvent1.setEventDate( getFormattedDate( LAST_MODIFIED_DATE ) );

      ResultEvent resultEvent2 = new ResultEvent();
      resultEvent2.setFaultId( FAULT_ID );
      resultEvent2.setDescription( REF_RESULT_EVENT_DESCRIPTION2 );
      resultEvent2.setResultCode( RESULT_CODE2 );
      resultEvent2.setEventDate( getFormattedDate( LAST_MODIFIED_DATE ) );

      List<ResultEvent> resultEvents = new ArrayList<ResultEvent>();
      resultEvents.add( resultEvent1 );
      resultEvents.add( resultEvent2 );

      return resultEvents;
   }


   private void AssertFault( Fault expectedFault, Fault actualFault ) {
      Assert.assertEquals( "Incorrect Name for retrieved fault: ", expectedFault.getName(),
            actualFault.getName() );
      Assert.assertEquals( "Incorrect Found in Task Barcode for retrieved fault: ",
            expectedFault.getFoundInTaskBarcode(), actualFault.getFoundInTaskBarcode() );
      Assert.assertEquals( "Incorrect Found in Task ID for retrieved fault: ",
            expectedFault.getFoundInTaskId(), actualFault.getFoundInTaskId() );
      Assert.assertEquals( "Incorrect Inventory ID for retrieved fault: ",
            expectedFault.getInventoryId(), actualFault.getInventoryId() );
      Assert.assertEquals( "Incorrect Status for retrieved fault: ", expectedFault.getStatus(),
            actualFault.getStatus() );
      Assert.assertEquals( "Incorrect Logbook Reference for retrieved fault: ",
            expectedFault.getLogbookReference(), actualFault.getLogbookReference() );
      Assert.assertEquals( "Incorrect Failed System ID for retrieved fault: ",
            expectedFault.getFailedSystemId(), actualFault.getFailedSystemId() );
      Assert.assertEquals( "Incorrect Description for retrieved fault: ",
            expectedFault.getDescription(), actualFault.getDescription() );
      Assert.assertEquals( "Incorrect Logbook Type for retrieved fault: ",
            expectedFault.getLogbookType(), actualFault.getLogbookType() );
      Assert.assertEquals( "Incorrect Severity for retrieved fault: ", expectedFault.getSeverity(),
            actualFault.getSeverity() );
      Assert.assertEquals( "Incorrect Found Date for retrieved fault: ",
            expectedFault.getFoundDate(), actualFault.getFoundDate() );
      Assert.assertEquals( "Incorrect Fault Source for retrieved fault: ",
            expectedFault.getFaultSource(), actualFault.getFaultSource() );
      Assert.assertEquals( "Incorrect Operational Restriction for retrieved fault: ",
            expectedFault.getOperationalRestriction(), actualFault.getOperationalRestriction() );
      Assert.assertEquals( "Incorrect Scheduling Rules Start Usage for retrieved fault: ",
            expectedFault.getSchedulingRulesStartUsage(),
            actualFault.getSchedulingRulesStartUsage() );
      Assert.assertEquals( "Incorrect External Reference for retrieved fault: ",
            expectedFault.getExternalReference(), actualFault.getExternalReference() );
      Assert.assertEquals( "Incorrect Completed by Person for retrieved fault: ",
            expectedFault.getCompletedByPerson(), actualFault.getCompletedByPerson() );
      Assert.assertEquals( "Incorrect Completed on Date for retrieved fault: ",
            expectedFault.getCompletedOnDate(), actualFault.getCompletedOnDate() );
      Assert.assertEquals( "Incorrect Deferral Authorization for retrieved fault: ",
            expectedFault.getDeferralAuthorization(), actualFault.getDeferralAuthorization() );
      Assert.assertEquals( "Incorrect Deferral Reason for retrieved fault: ",
            expectedFault.getDeferralReason(), actualFault.getDeferralReason() );
      Assert.assertEquals( "Incorrect Corrective Action for retrieved fault: ",
            expectedFault.getCorrectiveAction(), actualFault.getCorrectiveAction() );
      Assert.assertEquals( "Incorrect Found During Flight Phase for retrieved fault: ",
            expectedFault.getFoundDuringFlightPhase(), actualFault.getFoundDuringFlightPhase() );
      Assert.assertEquals( "Incorrect Scheduling Rules Start Calendar for retrieved fault: ",
            expectedFault.getSchedulingRulesStartCalendar(),
            actualFault.getSchedulingRulesStartCalendar() );
      Assert.assertEquals( "Incorrect Deferral Reference for retrieved fault: ",
            expectedFault.getDeferralReference(), actualFault.getDeferralReference() );
      Assert.assertEquals( "Incorrect Highest Inventory ID for retrieved fault: ",
            expectedFault.getHighestInventoryId(), actualFault.getHighestInventoryId() );
      Assert.assertEquals( "Incorrect Estimated Duration for retrieved fault: ",
            expectedFault.getEstimatedDuration(), actualFault.getEstimatedDuration() );
      Assert.assertEquals( "Incorrect Estimated Due Date for retrieved fault: ",
            expectedFault.getEstimatedDueDate(), actualFault.getEstimatedDueDate() );
      Assert.assertEquals( "Incorrect Sched Start Date for retrieved fault: ",
            expectedFault.getSchedStartDate(), actualFault.getSchedStartDate() );
      Assert.assertEquals( "Incorrect Failed System Assembly BOM Code for retrieved fault: ",
            expectedFault.getFailedSystemAssemblyBomCode(),
            actualFault.getFailedSystemAssemblyBomCode() );
      Assert.assertEquals( "Incorrect Sched End Date for retrieved fault: ",
            expectedFault.getSchedEndDate(), actualFault.getSchedEndDate() );
      Assert.assertEquals( "Incorrect Priority Code for retrieved fault: ",
            expectedFault.getPriorityCode(), actualFault.getPriorityCode() );
      Assert.assertEquals( "Incorrect Flight Leg ID for retrieved fault: ",
            expectedFault.getFlightLegId(), actualFault.getFlightLegId() );
      Assert.assertEquals( "Incorrect Work Package ID for retrieved fault: ",
            expectedFault.getWorkpackageId(), actualFault.getWorkpackageId() );

      Assert.assertEquals( "Incorrect Completed Date for retrieved fault: ",
            expectedFault.getCompletedDate(), actualFault.getCompletedDate() );
      Assert.assertEquals( "Incorrect Fault ID for retrieved fault: ", expectedFault.getFaultId(),
            actualFault.getFaultId() );
      Assert.assertEquals( "Incorrect Failed System for retrieved fault: ",
            expectedFault.getFailedSystem(), actualFault.getFailedSystem() );
      Assert.assertEquals( "Incorrect Historic value for retrieved fault: ",
            expectedFault.isHistoric(), actualFault.isHistoric() );
      Assert.assertEquals( "Incorrect Found By Person for retrieved fault: ",
            expectedFault.getFoundByPerson(), actualFault.getFoundByPerson() );
      Assert.assertEquals( "Incorrect Fault Code for retrieved fault: ",
            expectedFault.getFaultCode(), actualFault.getFaultCode() );
      Assert.assertEquals( "Incorrect Deferral Class for retrieved fault: ",
            expectedFault.getDeferralClass(), actualFault.getDeferralClass() );
      Assert.assertEquals( "Incorrect Barcode for retrieved fault: ", expectedFault.getBarcode(),
            actualFault.getBarcode() );
      Assert.assertEquals( "Incorrect Severity Type for retrieved fault: ",
            expectedFault.getSeverityType(), actualFault.getSeverityType() );
      Assert.assertEquals( "Incorrect Last Modified Date for retrieved fault: ",
            expectedFault.getLastModifiedDate(), actualFault.getLastModifiedDate() );

      AssertResultEvents( expectedFault.getResultEvents(), actualFault.getResultEvents() );

      AssertUsages( expectedFault.getUsages(), actualFault.getUsages() );

      AssertLabours( expectedFault.getLabour(), actualFault.getLabour() );

      AssertUsageDeadlines( expectedFault.getUsageDeadlines(), actualFault.getUsageDeadlines() );

   }


   private void AssertUsages( List<Usage> expectedUsages, List<Usage> actualUsages ) {
      Boolean result = false;

      result = expectedUsages.contains( actualUsages.get( 0 ) );
      result = result && expectedUsages.contains( actualUsages.get( 1 ) );

      assertTrue(
            "The usages are different. Expected: " + expectedUsages + "\n Actual: " + actualUsages,
            result );
   }


   private void AssertLabours( List<Labour> expectedLabours, List<Labour> actualLabours ) {
      Boolean result = false;

      result = expectedLabours.contains( actualLabours.get( 0 ) );
      result = result && expectedLabours.contains( actualLabours.get( 1 ) );

      assertTrue( "The labours are different. Expected: " + expectedLabours + "\n Actual: "
            + actualLabours, result );
   }


   private void AssertResultEvents( List<ResultEvent> expectedResultEvents,
         List<ResultEvent> actualResultEvents ) {

      Boolean result = false;

      result = expectedResultEvents.contains( actualResultEvents.get( 0 ) );
      result = result && expectedResultEvents.contains( actualResultEvents.get( 1 ) );

      assertTrue( "The result events are different. Expected: " + expectedResultEvents
            + "\n Actual: " + actualResultEvents, result );

   }


   private void AssertUsageDeadlines( List<Deadline> expectedUsageDeadlines,
         List<Deadline> actualUsageDeadlines ) {
      Boolean result = false;

      result = expectedUsageDeadlines.contains( actualUsageDeadlines.get( 0 ) );
      result = result && expectedUsageDeadlines.contains( actualUsageDeadlines.get( 1 ) );

      assertTrue( "The usage deadlines are different. Expected: " + expectedUsageDeadlines
            + "\n Actual: " + actualUsageDeadlines, result );
   }


   private Date getFormattedDate( String strDate ) throws ParseException {
      Date date;
      try {
         date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( strDate );
      } catch ( ParseException e ) {
         throw ( e );
      }
      return date;
   }


   private Fault getNewFault() {
      Fault lNewFault = new Fault();
      lNewFault.setLogbookReference( LOGBOOK_REFERENCE );
      lNewFault.setDescription( DESCRIPTION );
      lNewFault.setFailedSystemId( HIGHEST_INV_ID );
      lNewFault.setLogbookType( LOGBOOK_TYPE_CABIN );
      lNewFault.setInventoryId( HIGHEST_INV_ID );
      lNewFault.setFailedSystemId( HIGHEST_INV_ID );
      lNewFault.setFlightLegId( FLIGHT_LEG_ID );
      lNewFault.setFoundDuringFlightPhase( FLIGHT_PHASE_ICL );
      Date lFoundDate = new Date();
      lNewFault.setFoundDate( lFoundDate );
      lNewFault.setFoundByPerson( FOUND_PERSON_ID );
      lNewFault.setFaultSource( FAULT_SOURCE_PILOT );
      lNewFault.setFaultCode( FAULT_CODE1 );
      lNewFault.setName( NAME );
      lNewFault.setSeverity( SEVERITY_MINOR );
      return lNewFault;
   }


   private FaultV2 getFaultV2() throws ParseException {
      FaultV2 faultV2 = new FaultV2();
      faultV2.setDescription( DESCRIPTION );
      faultV2.setEstimatedDuration( ESTIMATED_DURATION2 );
      faultV2.setFailedSystemId( FAILED_SYSTEM_ID );
      faultV2.setFaultCode( FAULT_CODE1 );
      faultV2.setFaultSourceCode( FAULT_SOURCE_PILOT );
      faultV2.setFoundByUserId( FOUND_PERSON_ID );
      faultV2.setFoundInTaskId( FOUND_IN_TASKID );
      faultV2.setFoundDate( getFormattedDate( FOUND_DATE ) );
      faultV2.setId( TASK_ID );
      faultV2.setInventoryId( INVENTORY_ID );
      faultV2.setName( NAME );
      faultV2.setLogbookReference( LOGBOOK_REFERENCE2 );
      faultV2.setLogbookTypeCode( LOGBOOK_TYPE );
      faultV2.setPriorityCode( TASK_PRIORITY_CD );
      faultV2.setSeverityCode( SEVERITY_MINOR );
      return faultV2;
   }


   private void AssertFaultV2( FaultV2 expectedFault, FaultV2 actualFault ) {
      Assert.assertEquals( expectedFault.getDescription(), actualFault.getDescription() );
      Assert.assertEquals( expectedFault.getEstimatedDuration(),
            actualFault.getEstimatedDuration() );
      Assert.assertEquals( expectedFault.getFailedSystemId(), actualFault.getFailedSystemId() );
      Assert.assertEquals( expectedFault.getFaultCode(), actualFault.getFaultCode() );
      Assert.assertEquals( expectedFault.getFaultSourceCode(), actualFault.getFaultSourceCode() );
      Assert.assertEquals( expectedFault.getFoundByUserId(), actualFault.getFoundByUserId() );
      Assert.assertEquals( expectedFault.getFoundInTaskId(), actualFault.getFoundInTaskId() );
      Assert.assertEquals( expectedFault.getFoundDate(), actualFault.getFoundDate() );
      Assert.assertEquals( expectedFault.getInventoryId(), actualFault.getInventoryId() );
      Assert.assertEquals( expectedFault.getName(), actualFault.getName() );
      Assert.assertEquals( expectedFault.getLogbookReference(), actualFault.getLogbookReference() );
      Assert.assertEquals( expectedFault.getLogbookTypeCode(), actualFault.getLogbookTypeCode() );
      Assert.assertEquals( expectedFault.getPriorityCode(), actualFault.getPriorityCode() );
      Assert.assertEquals( expectedFault.getSeverityCode(), actualFault.getSeverityCode() );
   }


   private FaultV2 getNewFaultV2() {
      FaultV2 lNewFaultV2 = new FaultV2();
      lNewFaultV2.setDescription( DESCRIPTION );
      lNewFaultV2.setFailedSystemId( HIGHEST_INV_ID );
      lNewFaultV2.setFaultCode( FAULT_CODE1 );
      lNewFaultV2.setFaultSourceCode( FAULT_SOURCE_PILOT );
      lNewFaultV2.setFoundByUserId( FOUND_PERSON_ID );
      Date lFoundDate = new Date();
      lNewFaultV2.setFoundDate( lFoundDate );
      lNewFaultV2.setInventoryId( HIGHEST_INV_ID );
      lNewFaultV2.setName( NAME );
      lNewFaultV2.setLogbookReference( LOGBOOK_REFERENCE );
      lNewFaultV2.setLogbookTypeCode( LOGBOOK_TYPE_CABIN );
      lNewFaultV2.setSeverityCode( SEVERITY_MINOR );
      return lNewFaultV2;
   }
}
