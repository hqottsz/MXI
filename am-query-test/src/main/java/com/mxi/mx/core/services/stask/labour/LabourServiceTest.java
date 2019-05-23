
package com.mxi.mx.core.services.stask.labour;

import static com.mxi.mx.core.key.RefLabourSkillKey.LBR;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.exception.StartDateAfterEndDateException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefLabourTimeKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.SchedLabourRoleStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.esigner.EsignatureInterface;
import com.mxi.mx.core.services.esigner.EsignatureService;
import com.mxi.mx.core.services.stask.EditSchedLabourTO;
import com.mxi.mx.core.services.stask.panel.SchedPanelService;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.table.sched.SchedLabourRoleAccessor;
import com.mxi.mx.core.table.sched.SchedLabourRoleReader;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusAccessor;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusReader;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.sched.SchedLabour;
import com.mxi.mx.core.unittest.table.sched.SchedLabourRole;
import com.mxi.mx.core.unittest.table.sched.SchedLabourRoleStatus;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


public final class LabourServiceTest {

   private static final RefLabourTimeKey LABOUR_TIME = new RefLabourTimeKey( 4650, "TEST" );
   private static final SchedLabourKey CERT_LABOUR_KEY = new SchedLabourKey( 4650, 1000 );
   private static final SchedLabourKey INSP_LABOUR_KEY = new SchedLabourKey( 4650, 11000 );
   private static final SchedLabourKey EDIT_LABOUR_KEY = new SchedLabourKey( 4650, 21000 );
   private static final SchedLabourKey COMPLETE_LABOUR_KEY = new SchedLabourKey( 4650, 30001 );
   private static final SchedLabourRoleKey CERT_LABOUR_ROLE_KEY =
         new SchedLabourRoleKey( 4650, 2000 );
   private static final SchedLabourRoleKey INSP_LABOUR_ROLE_KEY =
         new SchedLabourRoleKey( 4650, 12000 );
   private static final SchedLabourRoleKey COMPLETE_LABOUR_ROLE_KEY =
         new SchedLabourRoleKey( 4650, 30002 );
   private static final SchedLabourRoleStatusKey CERT_LABOUR_ROLE_STATUS_KEY =
         new SchedLabourRoleStatusKey( 4650, 5000 );
   private static final SchedLabourRoleStatusKey INSP_LABOUR_ROLE_STATUS_KEY =
         new SchedLabourRoleStatusKey( 4650, 15000 );
   private static final SchedLabourKey PASS_INSP_LABOUR_KEY = new SchedLabourKey( 4650, 47660 );
   private static final SchedLabourRoleKey PASS_INSP_LABOUR_ROLE_KEY =
         new SchedLabourRoleKey( 4650, 48056 );
   private static final SchedLabourRoleStatusKey PASS_INSP_LABOUR_ROLE_STATUS_KEY =
         new SchedLabourRoleStatusKey( 4650, 48354 );
   private static final HumanResourceKey HR_KEY = new HumanResourceKey( 4650, 999 );
   private static final Double HOURS = 1.5;
   private static final boolean PASS_INSP = true;
   private static final boolean REJECT_INSP = false;

   private static final boolean NA_INSP_REQUIRED = true;
   private static final boolean NA_CERT_REQUIRED = false;
   private static final double NA_INSP_SCHED_HOURS = 3.0d;
   private static final double NA_CERT_SCHED_HOURS = 2.0d;
   private static final double NA_TECH_SCHED_HOURS = 1.0d;

   private EsignatureInterface iEsignatureService;
   private LabourRoleUtils iLabourRoleUtils;
   private GlobalParametersStub iGlobalParametersStub;

   // subject under test
   private LabourService iService;

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( 999, "username" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that the certify method works when given a labour role key.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void certify() throws Exception {
      GlobalParameters lLogicParameters = GlobalParameters.getInstance( ParmTypeEnum.LOGIC );
      lLogicParameters.setProperty( "END_DATE_COMPLETION_THRESHOLD", Double.toString( -1.0 ) );
      lLogicParameters.setBoolean( "ENFORCE_QUALIFICATION", false );
      lLogicParameters.setBoolean( "ENFORCE_LICENSE", false );
      lLogicParameters.setBoolean( "ENABLE_JC_ESIG", false );

      CertifyLabourTO lCertifyTO = buildCertifyTO( CERT_LABOUR_ROLE_KEY );
      iService.certify( CERT_LABOUR_KEY, lCertifyTO );

      SchedLabour lSchedLabour = new SchedLabour( CERT_LABOUR_KEY );
      lSchedLabour.assertCertification( true );

      SchedLabourRole lSchedLabourRole = new SchedLabourRole( CERT_LABOUR_ROLE_KEY );
      lSchedLabourRole.assertActualEndDate( lCertifyTO.getEndDate() );
      lSchedLabourRole.assertActualStartDate( lCertifyTO.getStartDate() );
      lSchedLabourRole.assertActualHours( HOURS );
      lSchedLabourRole.assertAdjustedBillingHours( HOURS );
      lSchedLabourRole.assertLabourTime( lCertifyTO.getTimeTracking() );

      SchedLabourRoleStatus lSchedLabourRoleStatus =
            new SchedLabourRoleStatus( CERT_LABOUR_ROLE_STATUS_KEY );
      lSchedLabourRoleStatus.assertHr( HR_KEY );
      lSchedLabourRoleStatus.assertLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
   }


   /**
    * Tests that certification will create the labour role if it is null.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void certifyNullLabourRole() throws Exception {
      iLabourRoleUtils.remove( CERT_LABOUR_ROLE_KEY );

      GlobalParameters lLogicParameters = GlobalParameters.getInstance( ParmTypeEnum.LOGIC );
      lLogicParameters.setProperty( "END_DATE_COMPLETION_THRESHOLD", Double.toString( -1.0 ) );
      lLogicParameters.setBoolean( "ENFORCE_QUALIFICATION", false );
      lLogicParameters.setBoolean( "ENFORCE_LICENSE", false );
      lLogicParameters.setBoolean( "ENABLE_JC_ESIG", false );

      CertifyLabourTO lCertifyTO = buildCertifyTO( null );
      iService.certify( CERT_LABOUR_KEY, lCertifyTO );

      SchedLabour lSchedLabour = new SchedLabour( CERT_LABOUR_KEY );
      lSchedLabour.assertCertification( true );

      SchedLabourRole lSchedLabourRole = new SchedLabourRole( new SchedLabourRoleKey( 4650, 1 ) );
      lSchedLabourRole.assertActualEndDate( lCertifyTO.getEndDate() );
      lSchedLabourRole.assertActualStartDate( lCertifyTO.getStartDate() );
      lSchedLabourRole.assertActualHours( HOURS );
      lSchedLabourRole.assertAdjustedBillingHours( HOURS );
      lSchedLabourRole.assertLabourTime( lCertifyTO.getTimeTracking() );

      SchedLabourRoleStatus lSchedLabourRoleStatus =
            new SchedLabourRoleStatus( new SchedLabourRoleStatusKey( 4650, 1 ) );
      lSchedLabourRoleStatus.assertHr( HR_KEY );
      lSchedLabourRoleStatus.assertLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
   }


   /**
    * Tests the LabourService.complete(SchedLabourKey, Date) method.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void completeLabour() throws Exception {
      final Date lCompleteDate = new Date();
      iService.complete( COMPLETE_LABOUR_KEY, lCompleteDate );

      // check that the labour stage is now complete
      SchedLabour lSchedLabour = new SchedLabour( COMPLETE_LABOUR_KEY );
      lSchedLabour.assertLabourStage( RefLabourStageKey.COMPLETE );

      // check that the labour role's hours and dates were auto-filled ith 0 and the passed in date
      // respectively
      SchedLabourRole lSchedLabourRole = new SchedLabourRole( COMPLETE_LABOUR_ROLE_KEY );
      lSchedLabourRole.assertActualHours( 0.0 );
      lSchedLabourRole.assertAdjustedBillingHours( 0.0 );
      lSchedLabourRole.assertActualStartDate( lCompleteDate );
      lSchedLabourRole.assertActualEndDate( lCompleteDate );
   }


   /**
    * Tests that edit properly updates the labour roles.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void edit() throws Exception {
      List<EditSchedLabourTO> lEditList = new LinkedList<EditSchedLabourTO>();
      lEditList.add( buildEditSchedLabourTO() );
      iService.edit( lEditList, HR_KEY );

      // check that the cert role now exists
      SchedLabourRoleReader<SchedLabourRoleKey> lCertTable =
            SchedLabourRoleTable.findByForeignKey( EDIT_LABOUR_KEY, RefLabourRoleTypeKey.CERT );
      assertTrue( lCertTable.exists() );

      SchedLabourRoleStatusReader lCertStatusTable = SchedLabourRoleStatusTable
            .findByForeignKey( EDIT_LABOUR_KEY, RefLabourRoleTypeKey.CERT );
      assertTrue( lCertStatusTable.exists() );
      assertEquals( RefLabourRoleStatusKey.PENDING, lCertStatusTable.getLabourRoleStatus() );

      // check that the insp role now exists
      SchedLabourRoleReader<SchedLabourRoleKey> lInspTable =
            SchedLabourRoleTable.findByForeignKey( EDIT_LABOUR_KEY, RefLabourRoleTypeKey.INSP );
      assertTrue( lInspTable.exists() );

      SchedLabourRoleStatusReader lInspStatusTable = SchedLabourRoleStatusTable
            .findByForeignKey( EDIT_LABOUR_KEY, RefLabourRoleTypeKey.INSP );
      assertTrue( lInspStatusTable.exists() );
      assertEquals( RefLabourRoleStatusKey.PENDING, lInspStatusTable.getLabourRoleStatus() );
   }


   /**
    * Test that the inspect method works when given a labour role key.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void inspect() throws Exception {
      GlobalParameters lLogicParameters = GlobalParameters.getInstance( ParmTypeEnum.LOGIC );
      lLogicParameters.setProperty( "END_DATE_COMPLETION_THRESHOLD", Double.toString( -1.0 ) );
      lLogicParameters.setBoolean( "ENFORCE_QUALIFICATION", false );
      lLogicParameters.setBoolean( "ENFORCE_LICENSE", false );
      lLogicParameters.setBoolean( "ENABLE_JC_ESIG", false );

      InspectLabourTO lInspectTO = buildInspectTO( INSP_LABOUR_ROLE_KEY );
      iService.inspect( INSP_LABOUR_KEY, lInspectTO );

      SchedLabour lSchedLabour = new SchedLabour( INSP_LABOUR_KEY );
      lSchedLabour.assertIndependentInspection( true );

      SchedLabourRole lSchedLabourRole = new SchedLabourRole( INSP_LABOUR_ROLE_KEY );
      lSchedLabourRole.assertActualEndDate( lInspectTO.getEndDate() );
      lSchedLabourRole.assertActualStartDate( lInspectTO.getStartDate() );
      lSchedLabourRole.assertActualHours( HOURS );
      lSchedLabourRole.assertAdjustedBillingHours( HOURS );
      lSchedLabourRole.assertLabourTime( lInspectTO.getTimeTracking() );

      SchedLabourRoleStatus lSchedLabourRoleStatus =
            new SchedLabourRoleStatus( INSP_LABOUR_ROLE_STATUS_KEY );
      lSchedLabourRoleStatus.assertHr( HR_KEY );
      lSchedLabourRoleStatus.assertLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
   }


   /**
    * Tests that inspection will create the labour role if it is null.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInspectNullLabourRole() throws Exception {
      iLabourRoleUtils.remove( INSP_LABOUR_ROLE_KEY );

      GlobalParameters lLogicParameters = GlobalParameters.getInstance( ParmTypeEnum.LOGIC );
      lLogicParameters.setProperty( "END_DATE_COMPLETION_THRESHOLD", Double.toString( -1.0 ) );
      lLogicParameters.setBoolean( "ENFORCE_QUALIFICATION", false );
      lLogicParameters.setBoolean( "ENFORCE_LICENSE", false );
      lLogicParameters.setBoolean( "ENABLE_JC_ESIG", false );

      InspectLabourTO lInspectTO = buildInspectTO( null );
      iService.inspect( INSP_LABOUR_KEY, lInspectTO );

      SchedLabour lSchedLabour = new SchedLabour( INSP_LABOUR_KEY );
      lSchedLabour.assertIndependentInspection( true );

      SchedLabourRole lSchedLabourRole = new SchedLabourRole( new SchedLabourRoleKey( 4650, 1 ) );
      lSchedLabourRole.assertActualEndDate( lInspectTO.getEndDate() );
      lSchedLabourRole.assertActualStartDate( lInspectTO.getStartDate() );
      lSchedLabourRole.assertActualHours( HOURS );
      lSchedLabourRole.assertAdjustedBillingHours( HOURS );
      lSchedLabourRole.assertLabourTime( lInspectTO.getTimeTracking() );

      SchedLabourRoleStatus lSchedLabourRoleStatus =
            new SchedLabourRoleStatus( new SchedLabourRoleStatusKey( 4650, 1 ) );
      lSchedLabourRoleStatus.assertHr( HR_KEY );
      lSchedLabourRoleStatus.assertLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
   }


   /**
    * Test that the inspect method works when given a labour role key and it's a PASS inspection.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void passInspect() throws Exception {
      iGlobalParametersStub.storeParameter( "END_DATE_COMPLETION_THRESHOLD",
            Double.toString( -1.0 ) );
      iGlobalParametersStub.storeParameter( "ENFORCE_QUALIFICATION", "false" );
      iGlobalParametersStub.storeParameter( "ENFORCE_LICENSE", "false" );
      iGlobalParametersStub.storeParameter( "ENABLE_JC_ESIG", "false" );

      InspectLabourTO lInspectTO = buildPassInspectTO( PASS_INSP );
      SchedLabourKey lNewSchedLabourKey = iService.inspect( PASS_INSP_LABOUR_ROLE_KEY, lInspectTO );

      // assert that the the current labour, labour role, and labour role status are handled
      // properly
      SchedLabour lSchedLabour = new SchedLabour( PASS_INSP_LABOUR_KEY );
      lSchedLabour.assertIndependentInspection( true );

      SchedLabourRole lSchedLabourRole = new SchedLabourRole( PASS_INSP_LABOUR_ROLE_KEY );
      lSchedLabourRole.assertActualEndDate( lInspectTO.getEndDate() );
      lSchedLabourRole.assertActualStartDate( lInspectTO.getStartDate() );
      lSchedLabourRole.assertActualHours( HOURS );
      lSchedLabourRole.assertAdjustedBillingHours( HOURS );
      lSchedLabourRole.assertLabourTime( lInspectTO.getTimeTracking() );

      SchedLabourRoleStatus lSchedLabourRoleStatus =
            new SchedLabourRoleStatus( PASS_INSP_LABOUR_ROLE_STATUS_KEY );
      lSchedLabourRoleStatus.assertHr( HR_KEY );
      lSchedLabourRoleStatus.assertLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );

      // assert that no new labour is created for PASS inspection
      assertNull( lNewSchedLabourKey );
   }


   /**
    * Test that the inspect method works when given a labour role key and it's a REJECT inspection.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void rejectInspect() throws Exception {
      iGlobalParametersStub.storeParameter( "END_DATE_COMPLETION_THRESHOLD",
            Double.toString( -1.0 ) );
      iGlobalParametersStub.storeParameter( "ENFORCE_QUALIFICATION", "false" );
      iGlobalParametersStub.storeParameter( "ENFORCE_LICENSE", "false" );
      iGlobalParametersStub.storeParameter( "ENABLE_JC_ESIG", "false" );

      InspectLabourTO lInspectTO = buildPassInspectTO( REJECT_INSP );
      SchedLabourKey lNewSchedLabourKey = iService.inspect( PASS_INSP_LABOUR_ROLE_KEY, lInspectTO );

      // assert that the current labour, labour role, and labour role status are handled properly
      SchedLabour lSchedLabour = new SchedLabour( PASS_INSP_LABOUR_KEY );
      lSchedLabour.assertIndependentInspection( true );

      SchedLabourRole lSchedLabourRole = new SchedLabourRole( PASS_INSP_LABOUR_ROLE_KEY );
      lSchedLabourRole.assertActualEndDate( lInspectTO.getEndDate() );
      lSchedLabourRole.assertActualStartDate( lInspectTO.getStartDate() );
      lSchedLabourRole.assertActualHours( HOURS );
      lSchedLabourRole.assertAdjustedBillingHours( HOURS );
      lSchedLabourRole.assertLabourTime( lInspectTO.getTimeTracking() );

      SchedLabourRoleStatus lSchedLabourRoleStatus =
            new SchedLabourRoleStatus( PASS_INSP_LABOUR_ROLE_STATUS_KEY );
      lSchedLabourRoleStatus.assertHr( HR_KEY );
      lSchedLabourRoleStatus.assertLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );

      // assert that a new labour is created with labour role and labour role status set properly
      SchedLabourTable lNewSchedLabourTable =
            SchedLabourTable.findByPrimaryKey( lNewSchedLabourKey );

      SchedLabourRoleAccessor<SchedLabourRoleKey> lTechSchedLabourRole =
            SchedLabourRoleTable.findByForeignKey( lNewSchedLabourKey, RefLabourRoleTypeKey.TECH );
      SchedLabourRoleStatusAccessor lTechLabourRoleStatusTable = SchedLabourRoleStatusTable
            .findByForeignKey( lNewSchedLabourKey, RefLabourRoleTypeKey.TECH );

      SchedLabourRoleAccessor<SchedLabourRoleKey> lCertSchedLabourRole =
            SchedLabourRoleTable.findByForeignKey( lNewSchedLabourKey, RefLabourRoleTypeKey.CERT );
      SchedLabourRoleStatusAccessor lCertLabourRoleStatusTable = SchedLabourRoleStatusTable
            .findByForeignKey( lNewSchedLabourKey, RefLabourRoleTypeKey.CERT );

      SchedLabourRoleAccessor<SchedLabourRoleKey> lInspSchedLabourRole =
            SchedLabourRoleTable.findByForeignKey( lNewSchedLabourKey, RefLabourRoleTypeKey.INSP );
      SchedLabourRoleStatusAccessor lInspLabourRoleStatusTable = SchedLabourRoleStatusTable
            .findByForeignKey( lNewSchedLabourKey, RefLabourRoleTypeKey.INSP );

      assertEquals( lNewSchedLabourTable.getLabourSkill(), RefLabourSkillKey.ENG );
      assertEquals( lNewSchedLabourTable.getLabourStage(), RefLabourStageKey.ACTV );

      MxAssert.assertEquals( lTechSchedLabourRole.getActualStartDate(), null );
      MxAssert.assertEquals( lCertSchedLabourRole.getActualStartDate(), null );
      MxAssert.assertEquals( lInspSchedLabourRole.getActualStartDate(), null );

      assertEquals( lTechLabourRoleStatusTable.getLabourRoleStatus(), RefLabourRoleStatusKey.ACTV );
      assertEquals( lCertLabourRoleStatusTable.getLabourRoleStatus(),
            RefLabourRoleStatusKey.PENDING );
      assertEquals( lInspLabourRoleStatusTable.getLabourRoleStatus(),
            RefLabourRoleStatusKey.PENDING );
   }


   @Test
   public void updateScheduledLabor() throws Exception {

      // data setup
      final Date lCurrentDate = new Date();
      final Date lStartDate = DateUtils.addDays( lCurrentDate, 3 );
      final Date lEndDate = DateUtils.addDays( lCurrentDate, 5 );
      final RefLabourTimeKey lRefLabourTimeKey = new RefLabourTimeKey( 4650, "TEST" );

      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      {
         lSchedLabourTO.setWorkPassed( true );
         lSchedLabourTO.setStartDate( lStartDate, "start date" );
         lSchedLabourTO.setEndDate( lEndDate, "end date" );
         lSchedLabourTO.setTimeTracking( lRefLabourTimeKey );
         lSchedLabourTO.setHours( HOURS, "hours" );
      }

      // execute
      LabourService.updateSchedLabourRole( COMPLETE_LABOUR_ROLE_KEY, lSchedLabourTO );

      // assert labour role was updated
      SchedLabourRole lSchedLabourRoleTable =
            SchedLabourRole.findByPrimaryKey( COMPLETE_LABOUR_ROLE_KEY );
      {
         lSchedLabourRoleTable.assertActualStartDate( lStartDate );
         lSchedLabourRoleTable.assertActualEndDate( lEndDate );
         assertEquals( lRefLabourTimeKey, lSchedLabourRoleTable.getLabourTime() );
         assertEquals( HOURS, lSchedLabourRoleTable.getActualHours() );
      }
   }


   @Test( expected = StartDateAfterEndDateException.class )
   public void updateScheduledLabor_invalidDates() throws Exception {

      // data setup
      final Date lCurrentDate = new Date();
      final Date lStartDate = DateUtils.addDays( lCurrentDate, 6 );
      final Date lEndDate = DateUtils.addDays( lCurrentDate, 5 );
      final RefLabourTimeKey lRefLabourTimeKey = new RefLabourTimeKey( 4650, "TEST" );

      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      {
         lSchedLabourTO.setWorkPassed( true );
         lSchedLabourTO.setStartDate( lStartDate, "start date" );
         lSchedLabourTO.setEndDate( lEndDate, "end date" );
         lSchedLabourTO.setTimeTracking( lRefLabourTimeKey );
         lSchedLabourTO.setHours( HOURS, "hours" );
      }

      // execute
      LabourService.updateSchedLabourRole( COMPLETE_LABOUR_ROLE_KEY, lSchedLabourTO );
   }


   /**
    *
    * Ensure that when a new labour row is added (but not as a result of a job stop) that the new
    * sched_labour row stores a null as the associated job stop labour row.
    *
    */
   @Test
   public void addedLabourRowHasNullAssocaitedJobStopLabourRow() throws Exception {

      // Needed to create a requirement.
      iGlobalParametersStub.storeParameter( "SPEC2000_UPPERCASE_TASK_CD", "false" );

      // Given a task.
      TaskKey task = Domain.createRequirement();

      // When a labour requirement is added to the task.
      LabourService.add( task, LBR, NA_TECH_SCHED_HOURS, NA_CERT_REQUIRED, NA_CERT_SCHED_HOURS,
            NA_INSP_REQUIRED, NA_INSP_SCHED_HOURS );

      // Then the newly added labour requirement has its associated job stop labour row set to null.
      List<SchedLabourKey> labourReqs = Domain.readLabourRequirement( task );
      assertThat( "Unexpected number of labour rows.", labourReqs.size(), is( 1 ) );
      SchedLabourTable schedLabourRow = SchedLabourTable.findByPrimaryKey( labourReqs.get( 0 ) );
      assertNull(
            "Expected assocaited job stop labour key to be null in the newly added labour row.",
            schedLabourRow.getSourceJobStopLabourRow() );
   }


   @Before
   public void setUp() throws Exception {
      iEsignatureService = new EsignatureService();
      iLabourRoleUtils = new LabourRoleUtils();
      iService = new LabourService( iLabourRoleUtils, iEsignatureService,
            new DefaultLabourRoleStatusFinder(), new WorkCaptureService(), new SchedPanelService(),
            new SequentialUuidGenerator() );

      iGlobalParametersStub = new GlobalParametersStub( "LOGIC" );
      GlobalParameters.setInstance( iGlobalParametersStub );

   }


   /**
    * Builds a Certify Transfer Object
    *
    * @param aLabourRoleKey
    *           The labour role key
    *
    * @return The new transfer object.
    *
    * @throws Exception
    *            If an error occurs.
    */
   private CertifyLabourTO buildCertifyTO( SchedLabourRoleKey aLabourRoleKey ) throws Exception {
      CertifyLabourTO lTO = new CertifyLabourTO();

      Calendar lCalendar = Calendar.getInstance();

      lTO.setEndDate( lCalendar.getTime(), "aEndDate" );
      lCalendar.add( Calendar.HOUR, -2 );
      lTO.setStartDate( lCalendar.getTime(), "aStartDate" );

      lTO.setTimeTracking( LABOUR_TIME );
      lTO.setHours( HOURS, "aHours" );

      return lTO;
   }


   /**
    * Builds an EditSchedLabour transfer object.
    *
    * @return The transfer object.
    *
    * @throws Exception
    *            If an error occurs.
    */
   private EditSchedLabourTO buildEditSchedLabourTO() throws Exception {
      EditSchedLabourTO lTO = new EditSchedLabourTO();

      lTO.setSchedLabour( EDIT_LABOUR_KEY );
      lTO.setCertificationRequired( true );
      lTO.setIndependentInspectionRequired( true );

      return lTO;
   }


   /**
    * Builds an Inspect Transfer Object
    *
    * @param aLabourRoleKey
    *           The labour role key
    *
    * @return The new transfer object.
    *
    * @throws Exception
    *            If an error occurs.
    */
   private InspectLabourTO buildInspectTO( SchedLabourRoleKey aLabourRoleKey ) throws Exception {
      InspectLabourTO lTO = new InspectLabourTO();

      Calendar lCalendar = Calendar.getInstance();

      lTO.setEndDate( lCalendar.getTime(), "aEndDate" );
      lCalendar.add( Calendar.HOUR, -2 );
      lTO.setStartDate( lCalendar.getTime(), "aStartDate" );

      lTO.setTimeTracking( LABOUR_TIME );
      lTO.setHours( HOURS, "aHours" );

      return lTO;
   }


   /**
    * Builds an Inspect Transfer Object
    *
    * @param aLabourRoleKey
    *           The labour role key
    *
    * @return The new transfer object.
    *
    * @throws Exception
    *            If an error occurs.
    */
   private InspectLabourTO buildPassInspectTO( boolean aPassInsp ) throws Exception {
      InspectLabourTO lTO = new InspectLabourTO();

      Calendar lCalendar = Calendar.getInstance();

      lTO.setEndDate( lCalendar.getTime(), "aEndDate" );
      lCalendar.add( Calendar.HOUR, -2 );
      lTO.setStartDate( lCalendar.getTime(), "aStartDate" );

      lTO.setTimeTracking( LABOUR_TIME );
      lTO.setHours( HOURS, "aHours" );

      lTO.setWorkPassed( aPassInsp );

      return lTO;
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
