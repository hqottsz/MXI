package com.mxi.am.api.resource.maintenance.exec.task;

import static com.mxi.am.api.resource.maintenance.exec.task.impl.TaskResourceBean.SELECT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
import com.mxi.am.api.exception.AmApiAuthorizationException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiInternalServerException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.provider.serialization.Iso8601DateTime;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.JobCardDefinition;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.impl.JobCardDefinitionResourceBean;
import com.mxi.am.api.resource.maintenance.exec.task.impl.TaskResourceBean;
import com.mxi.am.api.resource.maintenance.exec.task.labour.Labour;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourRole;
import com.mxi.am.api.resource.maintenance.exec.task.technicalreference.TechnicalReference;
import com.mxi.am.api.util.AuthorizationUtil;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class TaskResourceBeanTest extends ResourceBeanTest {

   @ClassRule
   public static InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Inject
   JobCardDefinitionResourceBean jobCardDefinitionResourceBean;

   @Inject
   TaskResourceBean taskResourceBean;

   @Inject
   LegacyKeyUtil legacyKeyUtil;

   @Inject
   EvtEventDao evtEventDao;

   @Mock
   private EJBContext ejbContext;
   @Mock
   private AuthorizationUtil authorizationUtil;
   @Mock
   private Principal principal;

   private List<TaskPanel> panels;

   private static final String CONFIG_SLOT_ID = "8F90DF4C52224E6694C686915FE2F249";
   private static final String ASSEMBLY_ID = "17FBB18D95F347A38C8B887ADD4D635E";
   private static final String ORGANIZATION_ID = "F49D9445729911E7B349CF53298AE07C";

   private static final String JCD_CODE_1 = "JCD_CODE_1";
   private static final String JCD_NAME_1 = "JCD_NAME_1";

   private static final String ACTION_DESC_1 = "ACTION_DESC_1";
   private static final String ACTION_DESC_2 = "ACTION_DESC_2";

   private static final String T_INV_1 = "9CFBA066DA9011E587B1FB2D7B2472DF";
   private static final Boolean T_HIST = true;

   private static final String NOT_FOUND_TASK_ID = "A054FC32D41C4DDBBC37D76B3E6412F8";
   private static final String NOT_FOUND_BARCODE = "TRFX9008U77N";
   private static final String TASK_ID = "A054FC32D41C4DDBBC37D76B3E6412F3";
   private static final String TASK_ID2 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA0002";
   private static final String DATE = "Tue Apr 17 12:00:00 EDT 2018";
   private static final String INV_ID = "9CFBA066DA9011E587B1FB2D7B2472DF";
   private static final String MEASUREMENT_PARM_CD = "CYCLES";
   private static final String MEASUREMENT_PARM_DESC = "Cycles";

   private static final String USER_ID = "A054FC32D41C4DDBBC37D76B3E6412F2";
   public static final String ORIGINATOR_CODE = "AWL";
   public static final String ISSUE_TO_ACCOUNT_ID = "ABB61BC8CCC3445E8AFEBC1E57E5DDAA";
   public static final BigDecimal MINIMUM_PLANNING_YIELD = new BigDecimal( 12.12 );
   public static final String REASON_FOR_REMOVAL_CODE = "LOANER";

   private static final String WORKPACKAGE1_ID = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA0003";

   private static final String HIGHEST_INV_ID = "AAAAAAAAAAAAAAAAAAAAAAAAAA123458";

   // Actions
   private static final String ACTION_DESC = "ACTION_DESC";

   // JobCard Step
   private static final String STEP_STATUS_CD = "STEP_STATUS_CD";
   private static final String STEP_DESC = "STEP_DESC";
   private static final int ORDER_NUMBER = 1;
   private static final String STEP_NOTES = "NOTES_DESC";

   // Zones
   private static final String ZONE_ID = "8AABC066DA9011E587B1FB2D7BFE8759";
   private static final String ZONE_DESC = "ZONE_DESCRIPTION";
   private static final String ZONE_CD = "ZONE_CD";

   // impacts
   private static final String IMPACT_DESC = "LONG_DESC";
   private static final String IMPACT = "IMPACT_CD (IMPACT_DESC)";

   // Part Requirements
   private static final String PART_REQ_ID = "DA47AF1E434711E8AD672FD4D6493B64";
   private static final String PART_REQ_DESC = "PART_CD(PART_NAME)";
   private static final String PART_REQ_POSITION = "POSITION_CD";
   private static final int PART_REQ_QT = 2;
   private static final String REASON = "REASON";
   private static final String PART_REQ_NOTE = "PART_NOTE";
   private static final String RMVD_INV_ID = "9FABA066DA9011E587B1FB2D7B8375FA";
   private static final String PART_REQ_LABOUR_ID = "DA490EB1434711E8AD672FD4D6493B64";
   private static final String INST_INV_ID = "9CFBA066DA9011E587B1FB2D7B2472EC";

   // Usage
   private static final int CYCLES_NUM = 10;

   // Tool Requirements
   private static final String TOOL_PART_ID = "38A80BDD43EC11E8B18DF5D70C615CDC";
   private static final String TOOL_INV_ID = "38A7E4CC43EC11E8B18DF5D70C615CDC";
   private static final int TOOL_SCHED_HR = 1;
   private static final int TOOL_ACTUAL_HR = 0;

   // Found faults
   private static final String FOUND_FAULT_ID = "89114C6E43F911E8A0BEB1D3D906B783";

   // Labour 1
   private static final String LABOUR_ID_1 = "DA490EB1434711E8AD672FD4D6493B64";
   private static final Double LABOUR_1_SCHED_HR = 1.0;
   private static final Double LABOUR_1_ACTUAL_HR = 1.0;
   private static final Double LABOUR_1_AB_HR = 1.0;
   private static final String LABOUR_1_CERT_USER = "unauthorized";
   private static final String LABOUR_1_INSP_USER = "authorized";

   // Labour 2
   private static final String LABOUR_ID_2 = "43C9EE8C4E1F11E8AB6C83FA2CBCBC8F";
   private static final Double LABOUR_2_SCHED_HR = 2.0;
   private static final Double LABOUR_2_ACTUAL_HR = 2.0;
   private static final Double LABOUR_2_AB_HR = 2.0;

   // Labour 3
   private static final String LABOUR_ID_3 = "43CD98114E1F11E8AB6C83FA2CBCBC8F";
   private static final Double LABOUR_3_SCHED_HR = 3.0;
   private static final Double LABOUR_3_ACTUAL_HR = 3.0;
   private static final Double LABOUR_3_AB_HR = 3.0;

   // Technical Reference
   private static final String TECH_REF_NAME = "TECH_REF_NAME";
   private static final String TECH_REF_TYPE = "ESPM";
   private static final String TECH_REF_LINK = "TECH_REF_LINK";
   private static final String TECH_REF_DESC = "TECH_REF_DESC";
   private static final String TECH_REF_NOTE = "TECH_REF_NOTE";
   private static final boolean TECH_REF_PRINT = true;

   // Tasks common variables
   private static final String ADHOC_TASK_CLASS = "ADHOC";
   private static final String ADHOCTLD_TASK_SUBCLASS = "ADHOCTLD";
   private static final String STATUS_ACTV = "ACTV";
   private static final String STATUS_DESC = "Active";
   private static final String TASKS_INV_ID = "601435E495494F34965B1588F5A6036D";
   private static final String TASKS_WP_ID = "29B2673ABD6249DBBB36F2B3173B086D";
   private static final String TASKS_ASSEMBLY_ID = "17FBB18D95F347A38C8B887ADD4D635D";

   // MPC tasks common variables
   private static final String MPC_TASKS_TASK_DEFN_ID = "2F56B97138F011E9AD5B81A20000122A";
   private static final String MPC_TASKS_CLASS = "MPC";
   private static final String MPC_TASKS_CD = "MPC1";

   // Maintenance Task
   private static final String MAINT_TASK_ID = "A034FC32D41C4DDBBC37D76B3E6412F2";

   // Task Panels
   private static final String MPC_PANEL_STATUS = "UNSTARTED";
   private static final String PANEL_1_ID = "F16CF7120C41409AAE9C60B53711488D";
   private static final String PANEL_1_CD = "P3002";
   private static final String PANEL_1_ZONE_CD = "101";
   private static final String PANEL_2_ID = "F16CF7120C41409AAE9C60B537114D8D";
   private static final String PANEL_2_CD = "P3003";
   private static final String PANEL_2_ZONE_CD = "102";
   private static final String PANEL_3_ID = "F16CF7120C41409AAE9C60B537114DFF";
   private static final String PANEL_3_CD = "P3004";
   private static final String PANEL_3_ZONE_CD = "102";


   @Before
   public void setUp() throws MxException, SQLException {

      InjectorContainer.get().injectMembers( this );

      taskResourceBean.setEJBContext( ejbContext );

      jobCardDefinitionResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( AUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      setupPanelData();

      int userId = 2;
      UserParametersFake lUserParms = new UserParametersFake( userId, ParmTypeEnum.LOGIC.name() );
      lUserParms.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( userId, ParmTypeEnum.LOGIC.name(), lUserParms );

   }


   private void setupPanelData() {

      panels = new ArrayList<TaskPanel>();

      TaskPanel panel = new TaskPanel();
      panel.setId( "DA4BCDD3434711E8AD672FD4D6493B64" );
      panel.setPanelDescription( "P1_DESC" );
      panel.setCode( "P1_CD" );
      panel.setZoneCode( "ZONE_CD" );

      TaskPanelOpenCloseRecord lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1001, John1001" );
      panel.setOpenPanel( lRecord );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1003, John1003" );
      panel.setClosePanel( lRecord );

      panels.add( panel );

      panel = new TaskPanel();
      panel.setId( "DA4BCDD3434711E8AD672FD4D6493B64" );
      panel.setPanelDescription( "P1_DESC" );
      panel.setCode( "P1_CD" );
      panel.setZoneCode( "ZONE_CD" );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1002, John1002" );
      panel.setOpenPanel( lRecord );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1004, John1004" );
      panel.setClosePanel( lRecord );

      panels.add( panel );

      panel = new TaskPanel();
      panel.setId( "EA4BCDD3434711E8AD672FD4D6493B64" );
      panel.setPanelDescription( "P2_DESC" );
      panel.setCode( "P2_CD" );
      panel.setZoneCode( "ZONE_CD" );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1001, John1001" );
      panel.setOpenPanel( lRecord );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1003, John1003" );
      panel.setClosePanel( lRecord );

      panels.add( panel );

      panel = new TaskPanel();
      panel.setId( "EA4BCDD3434711E8AD672FD4D6493B64" );
      panel.setPanelDescription( "P2_DESC" );
      panel.setCode( "P2_CD" );
      panel.setZoneCode( "ZONE_CD" );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1002, John1002" );
      panel.setOpenPanel( lRecord );

      lRecord = new TaskPanelOpenCloseRecord();
      lRecord.setDate( "17-APR-2018 12:00" );
      lRecord.setSkill( "SKILL_CD" );
      lRecord.setAccomplishedBy( "Smith1004, John1004" );
      panel.setClosePanel( lRecord );

      panels.add( panel );
   }


   /**
    * Test the creation of a historic task from a task definition.
    *
    * @throws KeyConversionException
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @CSIContractTest( Project.SWA_MOPP )
   @Test
   public void create_success()
         throws KeyConversionException, AmApiBusinessException, AmApiResourceNotFoundException {
      JobCardDefinition lJobCardDefinition = getDefaultJobCardDefinition();

      // create job card definition
      JobCardDefinition lCreatedJobCardDefinition =
            jobCardDefinitionResourceBean.post( lJobCardDefinition );

      // activate task definition
      JobCardDefinition lActivatedJobCardDefinition =
            activateJobCardDefinition( lCreatedJobCardDefinition );

      // create actual task
      Task lTask = getDefaultTask();
      lTask.setTaskDefnId( lActivatedJobCardDefinition.getId() );

      // create task actions
      Action lAction = new Action();
      lAction.setDescription( ACTION_DESC_1 );
      lAction.setUpdatedDate( new Date() );
      lAction.setDescription( ACTION_DESC_2 );
      lAction.setUpdatedDate( new Date() );
      lTask.addAction( lAction );

      TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
      Task lCreatedTask = taskResourceBean.create( lTask, lCreateTaskFilterParameters );

      TaskSearchParameters lGetTaskFilterParameters = new TaskSearchParameters();
      lGetTaskFilterParameters.setDrivingDeadline( false );
      Task lQueriedTask = taskResourceBean.get( lCreatedTask.getId(), lGetTaskFilterParameters );

      assertTaskObjectsEquals( lCreatedTask, lQueriedTask );
   }


   @Test
   public void get_success_byId() throws AmApiResourceNotFoundException, AmApiBusinessException {

      TaskSearchParameters lGetTaskFilterParameters = new TaskSearchParameters();
      Task lQueriedTask = taskResourceBean.get( TASK_ID, lGetTaskFilterParameters );
      assertLoadActions( lQueriedTask );
      assertLoadMeasurements( lQueriedTask );
      assertLoadJobCardSteps( lQueriedTask );
      assertLoadZones( lQueriedTask );
      assertLoadPanels( lQueriedTask );
      assertLoadImpacts( lQueriedTask );
      assertLoadPartRequirements( lQueriedTask );
      assertLoadUsages( lQueriedTask );
      assertLoadToolRequirements( lQueriedTask );
      assertLoadFoundFaults( lQueriedTask );
      assertLoadLabours( lQueriedTask );
      assertLoadTechnicalReference( lQueriedTask );

      assertEquals( MINIMUM_PLANNING_YIELD.doubleValue(),
            lQueriedTask.getMinimumPlanningYield().doubleValue(), 0 );
      assertEquals( ORIGINATOR_CODE, lQueriedTask.getOriginatorCode() );
      assertEquals( REASON_FOR_REMOVAL_CODE, lQueriedTask.getReasonForRemovalCode() );
      assertEquals( ISSUE_TO_ACCOUNT_ID, lQueriedTask.getIssueToAccountId() );
   }


   /**
    * A general test to test out multiple parameters in the search function.
    *
    */
   @CSIContractTest( Project.SWA_MOPP )
   @Test
   public void search_success_byParameters() {

      TaskSearchParameters parameters = new TaskSearchParameters();
      parameters.setWorkPackageId( WORKPACKAGE1_ID );
      parameters.setTaskClass( ADHOC_TASK_CLASS );
      parameters.setSubclass( ADHOCTLD_TASK_SUBCLASS );
      parameters.setHighestInvId( HIGHEST_INV_ID );
      parameters.setDrivingDeadline( true );

      List<Task> tasks = taskResourceBean.search( parameters );

      assertEquals( "Unexpected number of tasks returned from search: ", 1, tasks.size() );
      Task task = tasks.get( 0 );

      assertEquals( "Returned task did not have the expected id: ", TASK_ID2, task.getId() );
      assertEquals( "Returned task did not have the expected workpackage id: ", WORKPACKAGE1_ID,
            task.getWorkPackageId() );
      assertEquals( "Returned task did not have the expected class code: ", ADHOC_TASK_CLASS,
            task.getTaskClass() );
      assertEquals( "Returned task did not have the expected subclass code: ",
            ADHOCTLD_TASK_SUBCLASS, task.getSubclass() );

      DrivingDeadline deadline = task.getDrivingDeadline();
      assertNotNull( "Task did not include a driving deadline when it should have.", deadline );
      assertEquals( "Incorrect driving deadline data type:", "CYR", deadline.getDataTypeCode() );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_idNotFound() throws AmApiResourceNotFoundException {
      TaskSearchParameters lTaskSearchParm = new TaskSearchParameters();
      taskResourceBean.get( NOT_FOUND_TASK_ID, lTaskSearchParm );
   }


   @Test
   public void get_failure_barcodeNotFound() {
      TaskSearchParameters lTaskSearchParameters = new TaskSearchParameters();
      lTaskSearchParameters.setBarcode( NOT_FOUND_BARCODE );
      List<Task> lTasks = taskResourceBean.search( lTaskSearchParameters );

      Assert.assertTrue( lTasks.isEmpty() );
   }


   @Test
   public void create_failure_inventoryNotFound() throws AmApiBusinessException {
      JobCardDefinition lJobCardDefinition = getDefaultJobCardDefinition();

      // create job card definition definition
      JobCardDefinition lCreatedJobCardDefinition =
            jobCardDefinitionResourceBean.post( lJobCardDefinition );

      // activate task definition
      JobCardDefinition lActivatedJobCardDefinition;
      try {
         lActivatedJobCardDefinition = activateJobCardDefinition( lCreatedJobCardDefinition );

         // create actual task and fail
         Task lTask = getDefaultTask();
         lTask.setTaskDefnId( lActivatedJobCardDefinition.getId() );
         lTask.setInventoryId( "NOT-EXIST" );

         TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
         lCreateTaskFilterParameters.setSelect( SELECT_ID );
         try {
            taskResourceBean.create( lTask, lCreateTaskFilterParameters );
            Assert.fail( "Expected exception" );
         } catch ( AmApiInternalServerException aE ) {
            String lMessage = aE.getMessage();
            assertTrue( lMessage
                  .contains( "Unable to create a COMPLETE task based on the task definition" ) );
         }
      } catch ( AmApiResourceNotFoundException aE ) {
         String lMessage = aE.getMessage();
         assertTrue( lMessage.contains( "Unable to activate task definition" ) );
      }
   }


   @Test
   public void create_failure_taskDefNotFound() throws AmApiBusinessException {
      // create actual task and fail
      Task lTask = getDefaultTask();
      lTask.setTaskDefnId( "NOT-EXIST" );

      TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
      lCreateTaskFilterParameters.setSelect( SELECT_ID );
      try {
         taskResourceBean.create( lTask, lCreateTaskFilterParameters );
         Assert.fail( "Expected exception" );
         // AmApiInternalServerException is mapped to code 500
      } catch ( AmApiInternalServerException aE ) {
         String lMessage = aE.getMessage();
         assertTrue( lMessage.contains( "task definition" ) );
      }
   }


   @Test
   public void create_failure_inactiveDefinition() throws AmApiBusinessException {
      JobCardDefinition lJobCardDefinition = getDefaultJobCardDefinition();

      // create job card definition definition
      JobCardDefinition lCreatedJobCardDefinition =
            jobCardDefinitionResourceBean.post( lJobCardDefinition );

      // create actual task and fail
      Task lTask = getDefaultTask();
      lTask.setTaskDefnId( lCreatedJobCardDefinition.getId() );

      TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
      lCreateTaskFilterParameters.setSelect( SELECT_ID );
      try {
         taskResourceBean.create( lTask, lCreateTaskFilterParameters );
         Assert.fail( "Expected exception" );
      } catch ( AmApiInternalServerException aE ) {
         String lMessage = aE.getMessage();
         assertTrue( lMessage
               .contains( "Unable to create a COMPLETE task based on the task definition" ) );
      }
   }


   @Test
   public void create_failure_historicTaskEndsInFuture() throws AmApiBusinessException {
      JobCardDefinition lJobCardDefinition = getDefaultJobCardDefinition();

      // create job card definition definition
      JobCardDefinition lCreatedJobCardDefinition =
            jobCardDefinitionResourceBean.post( lJobCardDefinition );

      // activate task definition
      JobCardDefinition lActivatedJobCardDefinition;
      try {
         lActivatedJobCardDefinition = activateJobCardDefinition( lCreatedJobCardDefinition );

         // create actual task
         Task lTask = getDefaultTask();
         lTask.setTaskDefnId( lActivatedJobCardDefinition.getId() );
         lTask.setEndDate( DateUtils.addDays( new Date(), 1 ) );

         // override future date validation config parm set in database
         GlobalParameters lGlobalParameters = GlobalParameters.getInstance( "LOGIC" );
         lGlobalParameters.setInteger( "END_DATE_COMPLETION_THRESHOLD", 1 );

         TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
         lCreateTaskFilterParameters.setSelect( SELECT_ID );
         try {
            taskResourceBean.create( lTask, lCreateTaskFilterParameters );
            Assert.fail( "Expected exception" );
         } catch ( AmApiInternalServerException aE ) {
            String lMessage = aE.getMessage();
            assertTrue( lMessage
                  .contains( "Unable to create a COMPLETE task based on the task definition" ) );
         }
      } catch ( AmApiResourceNotFoundException aE ) {
         String lMessage = aE.getMessage();
         assertTrue( lMessage.contains( "Unable to activate task definition" ) );
      }
   }


   @Test
   public void get_failure_unauthorized() throws MxException, AmApiResourceNotFoundException {

      Mockito.when( principal.getName() ).thenReturn( UNAUTHORIZED );

      TaskSearchParameters lGetTaskFilterParameters = new TaskSearchParameters();
      lGetTaskFilterParameters.setDrivingDeadline( false );
      try {
         taskResourceBean.get( JCD_CODE_1, lGetTaskFilterParameters );
         Assert.fail( "Expected exception" );
      } catch ( AmApiAuthorizationException aE ) {
         Assert.assertEquals( UNAUTHORIZED, aE.getPrincipalName() );
      }

   }


   /**
    *
    * Test post method for unauthorized access
    *
    */
   @Test( expected = AmApiAuthorizationException.class )
   public void create_failure_unauthorized() throws AmApiBusinessException {
      Mockito.when( principal.getName() ).thenReturn( UNAUTHORIZED );

      Task lTask = new Task();

      TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
      lCreateTaskFilterParameters.setSelect( SELECT_ID );

      taskResourceBean.create( lTask, lCreateTaskFilterParameters );

   }


   /**
    *
    * Test put method for unauthorized access
    *
    */
   @Test( expected = AmApiAuthorizationException.class )
   public void update_failure_unauthorized()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Mockito.when( principal.getName() ).thenReturn( UNAUTHORIZED );

      Task lTask = new Task();

      TaskSearchParameters lCreateTaskFilterParameters = new TaskSearchParameters();
      lCreateTaskFilterParameters.setSelect( SELECT_ID );

      taskResourceBean.update( JCD_CODE_1, lTask );

   }


   @Test
   public void get_success_simpleCorrectiveTaskWithImpacts()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      // given
      // - Minimally populated Corrective task
      // - with 2 impacts
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleCorrectiveTaskWithImpacts" );

      Task lGivenTask = new Task( "A054FC22D41C4DDBBC37D76B3E6412F2" );
      EvtEventTable lExpectedTaskEvent = evtEventDao.findByPrimaryKey( new EventKey( "4650:555" ) );
      lGivenTask.setStatus( "ACTV" );
      lGivenTask.setCreationDate( lExpectedTaskEvent.getCreationDt() );
      lGivenTask.setTaskClass( "CORR" );
      lGivenTask.setRevisionDate( lExpectedTaskEvent.getRevisionDt() );
      lGivenTask.setTaskClassMode( "REQ" );
      lGivenTask.setTaskType( TaskType.Requirement );
      lGivenTask.setEndDate( new Iso8601DateTime( "2018-04-17T16:00:00Z" ) );
      lGivenTask.setSoftDeadline( false );
      lGivenTask.setEtopsSignificant( false );
      lGivenTask.setHistoric( false );
      lGivenTask.setInventoryId( "8CFBA065DA9011E587B1FB2D7B2472DF" );
      lGivenTask.setStatusDescription( "Active" );
      lGivenTask.setTechnicalReference( new ArrayList<TechnicalReference>() );
      lGivenTask.setDuration( "0" );
      lGivenTask.setTaskDefnId( "E41043B61E3211E9884421590000122A" );
      lGivenTask.setMinimumPlanningYield( new BigDecimal( 80 ) ); // default value from config parm

      Impact lImpact = new Impact();
      lImpact.setImpact( "IMPACT7 (Impact 7 long description)" );
      lImpact.setImpactDescription( "This is impact 7" );
      lGivenTask.addImpact( lImpact );

      lImpact = new Impact();
      lImpact.setImpact( "IMPACT8 (Impact 8 long description)" );
      lImpact.setImpactDescription( "This is impact 8" );
      lGivenTask.addImpact( lImpact );

      // when
      // - I get the the Task by Id
      Task lRetrievedTask = taskResourceBean.get( lGivenTask.getId(), new TaskSearchParameters() );

      // then
      // - The given task = retrieved task
      System.out.println( lGivenTask );
      System.out.println( lRetrievedTask );
      Assert.assertEquals( lGivenTask, lRetrievedTask );

   }


   /**
    *
    * Test search method for unauthorized access
    *
    */
   @Test( expected = AmApiAuthorizationException.class )
   public void search_failure_unauthorized() {

      Mockito.when( principal.getName() ).thenReturn( UNAUTHORIZED );

      TaskSearchParameters lSearchTaskFilterParameters = new TaskSearchParameters();

      taskResourceBean.search( lSearchTaskFilterParameters );
      Assert.fail( "Expected exception" );

   }


   /**
    * Updates a task by assigning it to a new workpackage.
    *
    * @throws AmApiResourceNotFoundException
    * @throws AmApiBusinessException
    *
    */
   @CSIContractTest( Project.SWA_MOPP )
   @Test
   public void update_success_assignToWorkpackage()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      final String taskId = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA0006";
      final String wpId = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA0005";

      Task task = taskResourceBean.get( taskId, new TaskSearchParameters() );

      assertNull(
            "Task should not be assigned to a workpackage yet, but its workpackage id is non-null.",
            task.getWorkPackageId() );

      task.setWorkPackageId( wpId );

      Task updatedTask = taskResourceBean.update( taskId, task );

      assertEquals( "Returned task id does not match previous task id: ", task.getId(),
            updatedTask.getId() );
      assertEquals( "Returned task's workpackage id is not correct: ", wpId,
            updatedTask.getWorkPackageId() );

   }


   /**
    * Asserts that the correct technical reference is returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadTechnicalReference( Task aQueriedTask ) {
      Assert.assertEquals( TECH_REF_NAME, aQueriedTask.getTechnicalReference().get( 0 ).getName() );
      Assert.assertEquals( TECH_REF_TYPE, aQueriedTask.getTechnicalReference().get( 0 ).getType() );
      Assert.assertEquals( TECH_REF_LINK, aQueriedTask.getTechnicalReference().get( 0 ).getLink() );
      Assert.assertEquals( TECH_REF_DESC,
            aQueriedTask.getTechnicalReference().get( 0 ).getDescription() );
      Assert.assertEquals( TECH_REF_NOTE, aQueriedTask.getTechnicalReference().get( 0 ).getNote() );
      Assert.assertEquals( TECH_REF_PRINT,
            aQueriedTask.getTechnicalReference().get( 0 ).getPrintBool() );
   }


   /**
    * Asserts that the correct labours are returned by the task api
    *
    * @param aQueriedTask
    */
   private void assertLoadLabours( Task aQueriedTask ) {

      for ( Labour lLabour : aQueriedTask.getLabour() ) {
         for ( LabourRole lLabourRole : lLabour.getLabourRoles() ) {
            if ( lLabourRole.getRole() == "TECH" ) {
               if ( lLabour.getId().equalsIgnoreCase( LABOUR_ID_1 ) ) {
                  // Assert labour 1 with cert and insp
                  Assert.assertEquals( LABOUR_ID_1, lLabour.getId() );
                  Assert.assertEquals( LABOUR_1_SCHED_HR, lLabourRole.getScheduledHours(), 0 );
                  Assert.assertEquals( LABOUR_1_ACTUAL_HR, lLabourRole.getActualHours(), 0 );
                  Assert.assertEquals( LABOUR_1_AB_HR, lLabourRole.getAdjustedBillingHours(), 0 );
               } else if ( lLabour.getId().equalsIgnoreCase( LABOUR_ID_2 ) ) {
                  // Assert labour 2
                  Assert.assertEquals( LABOUR_ID_2, lLabour.getId() );
                  Assert.assertEquals( LABOUR_2_SCHED_HR, lLabourRole.getScheduledHours(), 0 );
                  Assert.assertEquals( LABOUR_2_ACTUAL_HR, lLabourRole.getActualHours(), 0 );
                  Assert.assertEquals( LABOUR_2_AB_HR, lLabourRole.getAdjustedBillingHours(), 0 );
               } else if ( lLabour.getId().equalsIgnoreCase( LABOUR_ID_3 ) ) {
                  // Assert labour 3
                  Assert.assertEquals( LABOUR_ID_3, lLabour.getId() );
                  Assert.assertEquals( LABOUR_3_SCHED_HR, lLabourRole.getScheduledHours(), 0 );
                  Assert.assertEquals( LABOUR_3_ACTUAL_HR, lLabourRole.getActualHours(), 0 );
                  Assert.assertEquals( LABOUR_3_AB_HR, lLabourRole.getAdjustedBillingHours(), 0 );
               }
            } else if ( lLabourRole.getRole() == "CERT" ) {
               if ( lLabour.getId().equalsIgnoreCase( LABOUR_ID_1 ) ) {
                  Assert.assertEquals( LABOUR_1_CERT_USER, lLabourRole.getUsername() );
               }
            } else if ( lLabourRole.getRole() == "INSP" ) {
               if ( lLabour.getId().equalsIgnoreCase( LABOUR_ID_1 ) ) {
                  Assert.assertEquals( LABOUR_1_INSP_USER, lLabourRole.getUsername() );
               }
            }
         }

      }

   }


   /**
    * Assert that the correct found faults are returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadFoundFaults( Task aQueriedTask ) {
      Assert.assertEquals( FOUND_FAULT_ID, aQueriedTask.getFoundFaultIds().get( 0 ) );
   }


   /**
    * Assert that the correct tool requirements are returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadToolRequirements( Task aQueriedTask ) {
      Assert.assertEquals( TOOL_PART_ID, aQueriedTask.getToolRequirements().get( 0 ).getPartId() );
      Assert.assertEquals( TOOL_INV_ID,
            aQueriedTask.getToolRequirements().get( 0 ).getInventoryId() );
      Assert.assertEquals( TOOL_SCHED_HR,
            aQueriedTask.getToolRequirements().get( 0 ).getScheduledHours(), 0 );
      Assert.assertEquals( TOOL_ACTUAL_HR,
            aQueriedTask.getToolRequirements().get( 0 ).getActualHours(), 0 );
      Assert.assertEquals( USER_ID, aQueriedTask.getToolRequirements().get( 0 ).getCheckedOutTo() );
      Assert.assertEquals( USER_ID, aQueriedTask.getToolRequirements().get( 0 ).getSignedBy() );

   }


   /**
    * Asserts that the correct usage is returned by the Task API
    *
    * @param aQueriedTask
    */
   private void assertLoadUsages( Task aQueriedTask ) {
      Assert.assertEquals( CYCLES_NUM, aQueriedTask.getUsageSnapshots().get( 0 ).getTSO(), 0 );
      Assert.assertEquals( CYCLES_NUM, aQueriedTask.getUsageSnapshots().get( 0 ).getTSI(), 0 );
      Assert.assertEquals( CYCLES_NUM, aQueriedTask.getUsageSnapshots().get( 0 ).getTSN(), 0 );
      Assert.assertEquals( CYCLES_NUM, aQueriedTask.getUsageSnapshots().get( 0 ).getAssemblyTSN(),
            0 );
      Assert.assertEquals( CYCLES_NUM, aQueriedTask.getUsageSnapshots().get( 0 ).getAssemblyTSO(),
            0 );
      Assert.assertEquals( MEASUREMENT_PARM_CD,
            aQueriedTask.getUsageSnapshots().get( 0 ).getUsageParameter() );

   }


   /**
    * Asserts that the correct part requirement is returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadPartRequirements( Task aQueriedTask ) {
      Assert.assertEquals( PART_REQ_ID, aQueriedTask.getPartRequirements().get( 0 ).getId() );
      Assert.assertEquals( PART_REQ_DESC,
            aQueriedTask.getPartRequirements().get( 0 ).getDescription() );
      Assert.assertEquals( PART_REQ_POSITION,
            aQueriedTask.getPartRequirements().get( 0 ).getPosition() );
      Assert.assertEquals( PART_REQ_QT, aQueriedTask.getPartRequirements().get( 0 ).getQuantity(),
            0 );
      Assert.assertEquals( PART_REQ_NOTE, aQueriedTask.getPartRequirements().get( 0 ).getNote() );
      Assert.assertEquals( RMVD_INV_ID,
            aQueriedTask.getPartRequirements().get( 0 ).getRemoval().getInventoryId() );
      Assert.assertEquals( PART_REQ_LABOUR_ID,
            aQueriedTask.getPartRequirements().get( 0 ).getRemoval().getLabourId() );
      Assert.assertEquals( PART_REQ_QT,
            aQueriedTask.getPartRequirements().get( 0 ).getRemoval().getQuantity(), 0 );
      Assert.assertEquals( REASON,
            aQueriedTask.getPartRequirements().get( 0 ).getRemoval().getReasonCode() );
      Assert.assertEquals( DATE, aQueriedTask.getPartRequirements().get( 0 ).getRemoval()
            .getCompletionDate().toString() );
      Assert.assertEquals( INST_INV_ID,
            aQueriedTask.getPartRequirements().get( 0 ).getInstallation().getInventoryId() );
      Assert.assertEquals( PART_REQ_LABOUR_ID,
            aQueriedTask.getPartRequirements().get( 0 ).getInstallation().getLabourId() );
      Assert.assertEquals( PART_REQ_QT,
            aQueriedTask.getPartRequirements().get( 0 ).getInstallation().getQuantity(), 0 );
      Assert.assertEquals( DATE, aQueriedTask.getPartRequirements().get( 0 ).getInstallation()
            .getCompletionDate().toString() );
   }


   /**
    * Asserts that the correct impact is returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadImpacts( Task aQueriedTask ) {
      Assert.assertEquals( IMPACT_DESC, aQueriedTask.getImpacts().get( 0 ).getImpactDescription() );
      Assert.assertEquals( IMPACT, aQueriedTask.getImpacts().get( 0 ).getImpact() );
   }


   /**
    * Asserts that the correct panels are returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadPanels( Task aQueriedTask ) {

      Comparator<TaskPanel> lComparator = new Comparator<TaskPanel>() {

         @Override
         public int compare( TaskPanel aPanel1, TaskPanel aPanel2 ) {
            return ( aPanel1.getId() + aPanel1.getClosePanel().getAccomplishedBy()
                  + aPanel1.getOpenPanel().getAccomplishedBy() )
                        .compareTo( aPanel2.getId() + aPanel2.getClosePanel().getAccomplishedBy()
                              + aPanel2.getOpenPanel().getAccomplishedBy() );
         }

      };

      panels.sort( lComparator );
      aQueriedTask.getPanels().sort( lComparator );

      Assert.assertTrue( "\nExpected:\n" + panels + "\nActuals:\n" + aQueriedTask.getPanels(),
            panels.equals( aQueriedTask.getPanels() ) );
   }


   /**
    * Asserts that the correct zone is returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadZones( Task aQueriedTask ) {
      Assert.assertEquals( ZONE_ID, aQueriedTask.getZones().get( 0 ).getId() );
      Assert.assertEquals( ZONE_DESC, aQueriedTask.getZones().get( 0 ).getDescription() );
      Assert.assertEquals( ZONE_CD, aQueriedTask.getZones().get( 0 ).getCode() );
   }


   /**
    * Asserts that the correct job card steps were returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadJobCardSteps( Task aQueriedTask ) {
      Assert.assertEquals( STEP_STATUS_CD,
            aQueriedTask.getJobCardSteps().get( 0 ).getStatusCode() );
      Assert.assertEquals( STEP_DESC, aQueriedTask.getJobCardSteps().get( 0 ).getDescription() );
      Assert.assertEquals( ORDER_NUMBER, aQueriedTask.getJobCardSteps().get( 0 ).getOrderNumber() );
      Assert.assertEquals( ORDER_NUMBER,
            aQueriedTask.getJobCardSteps().get( 0 ).getLabourSteps().get( 0 ).getOrderNumber() );
      Assert.assertEquals( STEP_NOTES,
            aQueriedTask.getJobCardSteps().get( 0 ).getLabourSteps().get( 0 ).getNotes() );
   }


   /**
    * Asserts that the correct measurements were returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadMeasurements( Task aQueriedTask ) {
      Assert.assertEquals( INV_ID, aQueriedTask.getMeasurements().get( 0 ).getInventoryId() );
      Assert.assertEquals( MEASUREMENT_PARM_CD,
            aQueriedTask.getMeasurements().get( 0 ).getParameterCode() );
      Assert.assertEquals( MEASUREMENT_PARM_CD,
            aQueriedTask.getMeasurements().get( 0 ).getUnitOfMeasure() );
      Assert.assertEquals( MEASUREMENT_PARM_DESC,
            aQueriedTask.getMeasurements().get( 0 ).getParameterDescription() );
   }


   /**
    * Asserts that the correct action was returned by the task API
    *
    * @param aQueriedTask
    */
   private void assertLoadActions( Task aQueriedTask ) {
      Assert.assertEquals( ACTION_DESC, aQueriedTask.getActions().get( 0 ).getDescription() );
      Assert.assertEquals( DATE, aQueriedTask.getActions().get( 0 ).getUpdatedDate().toString() );
   }


   private Task getDefaultTask() {
      Task lTask = new Task();
      lTask.setHistoric( T_HIST );
      lTask.setInventoryId( T_INV_1 );

      return lTask;
   }


   private JobCardDefinition getDefaultJobCardDefinition() {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      lJobCardDefinition.setCode( JCD_CODE_1 );
      lJobCardDefinition.setName( JCD_NAME_1 );
      lJobCardDefinition.setConfigSlotId( CONFIG_SLOT_ID );
      lJobCardDefinition.setAssemblyId( ASSEMBLY_ID );
      lJobCardDefinition.setTaskClass( RefTaskClassKey.JIC.getCd() );
      lJobCardDefinition.setOrganization( ORGANIZATION_ID );
      lJobCardDefinition.setStatus( RefTaskDefinitionStatusKey.BUILD.getCd() );
      return lJobCardDefinition;
   }


   private void assertTaskObjectsEquals( Task aExpectedTask, Task aActualTask ) {
      assertEquals( JCD_CODE_1 + " (" + JCD_NAME_1 + ")", aActualTask.getName() );
      assertEquals( aExpectedTask.getInventoryId(), aActualTask.getInventoryId() );
      assertEquals( aExpectedTask.getTaskDefnId(), aActualTask.getTaskDefnId() );
      assertEquals( aExpectedTask.isHistoric(), aActualTask.isHistoric() );
   }


   private JobCardDefinition activateJobCardDefinition( JobCardDefinition aJobCardDefintion )
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      aJobCardDefintion.setStatus( RefTaskDefinitionStatusKey.ACTV.getCd() );
      JobCardDefinition lActivatedJobCardDefinition =
            jobCardDefinitionResourceBean.put( aJobCardDefintion.getId(), aJobCardDefintion );
      lActivatedJobCardDefinition = jobCardDefinitionResourceBean.get( aJobCardDefintion.getId() );
      return lActivatedJobCardDefinition;
   }


   @Test
   public void get_success_simpleAdhocTaskWithImpacts()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      // given
      // - Minimally populated ADHOC task
      // - with 2 impacts
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleAdhocTaskWithImpacts" );

      Task lGivenTask = new Task( "A054FC32D41C4DDBBC37D76B3E6412F2" );
      EvtEventTable lExpectedTaskEvent = evtEventDao.findByPrimaryKey( new EventKey( "4650:777" ) );
      lGivenTask.setStatus( "ACTV" );
      lGivenTask.setCreationDate( lExpectedTaskEvent.getCreationDt() );
      lGivenTask.setTaskClass( "ADHOC" );
      lGivenTask.setRevisionDate( lExpectedTaskEvent.getRevisionDt() );
      lGivenTask.setTaskClassMode( "ADHOC" );
      lGivenTask.setTaskType( TaskType.AdhocTask );
      lGivenTask.setEndDate( new Iso8601DateTime( "2018-04-17T16:00:00Z" ) );
      lGivenTask.setSoftDeadline( false );
      lGivenTask.setEtopsSignificant( false );
      lGivenTask.setHistoric( false );
      lGivenTask.setInventoryId( "8CFBA066DA9011E587B1FB2D7B2472DF" );
      lGivenTask.setStatusDescription( "Active" );
      lGivenTask.setTechnicalReference( new ArrayList<TechnicalReference>() );
      lGivenTask.setDuration( "0" );
      lGivenTask.setMinimumPlanningYield( new BigDecimal( 12 ) ); // provided value in
                                                                  // sched_stask

      Impact lImpact = new Impact();
      lImpact.setImpact( "IMPACT1 (Impact 1 long description)" );
      lImpact.setImpactDescription( "This is impact 1" );
      lGivenTask.addImpact( lImpact );

      lImpact = new Impact();
      lImpact.setImpact( "IMPACT2 (Impact 2 long description)" );
      lImpact.setImpactDescription( "This is impact 2" );
      lGivenTask.addImpact( lImpact );

      // when
      // - I get the the Task by Id
      Task lRetrievedTask = taskResourceBean.get( lGivenTask.getId(), new TaskSearchParameters() );

      // then
      // - The given task = retrieved task
      Assert.assertEquals( lGivenTask, lRetrievedTask );

   }


   @Test
   public void get_success_simpleBaselinedFaultTaskWithImpacts()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      // given
      // - Minimally populated Fault task
      // - Fault is baselined (i.e. is based off of a task definition)
      // - with 2 impacts (which are associated with the faults's task definition, instead of
      // directly against the task
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleBaselinedFaultTaskWithImpacts" );

      Task lGivenTask = new Task( "A044FC32D41C4DDBBC37D76B3E6412F2" );
      EvtEventTable lExpectedTaskEvent = evtEventDao.findByPrimaryKey( new EventKey( "4650:888" ) );
      lGivenTask.setStatus( "ACTV" );
      lGivenTask.setCreationDate( lExpectedTaskEvent.getCreationDt() );
      lGivenTask.setTaskClass( "CORR" );
      lGivenTask.setRevisionDate( lExpectedTaskEvent.getRevisionDt() );
      lGivenTask.setTaskClassMode( "REQ" );
      lGivenTask.setTaskType( TaskType.Fault );
      lGivenTask.setEndDate( new Iso8601DateTime( "2018-04-17T16:00:00Z" ) );
      lGivenTask.setSoftDeadline( false );
      lGivenTask.setHistoric( false );
      lGivenTask.setEtopsSignificant( false );
      lGivenTask.setInventoryId( "8CFBA066DA9011E587B1FB2D7B2472DF" );
      lGivenTask.setStatusDescription( "Active" );
      lGivenTask.setTechnicalReference( new ArrayList<TechnicalReference>() );
      lGivenTask.setDuration( "0" );
      lGivenTask.setTaskDefnId( "B4D40E161B3A11E99DAE4E6F0000122A" );
      lGivenTask.setFaultSeverity( "MINOR" );
      lGivenTask.setMinimumPlanningYield( new BigDecimal( 80 ) ); // default value from config parm

      Impact lImpact = new Impact();
      lImpact.setImpact( "IMPACT3 (Impact 3 long description)" );
      lImpact.setImpactDescription( "This is impact 3" );
      lGivenTask.addImpact( lImpact );

      lImpact = new Impact();
      lImpact.setImpact( "IMPACT4 (Impact 4 long description)" );
      lImpact.setImpactDescription( "This is impact 4" );
      lGivenTask.addImpact( lImpact );

      // when
      // - I get the the Task by Id
      Task lRetrievedTask = taskResourceBean.get( lGivenTask.getId(), new TaskSearchParameters() );

      // then
      // - The given task = retrieved task
      Assert.assertEquals( lGivenTask, lRetrievedTask );
   }


   @Test
   public void get_success_simpleNonBaselinedFaultTaskWithImpacts()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      // given
      // - Minimally populated Fault task
      // - Fault is not baselined (i.e. it is not based off of a task definition)
      // - with 2 impacts, which are directly associated with the faults (in contrast to impacts
      // that are taken from a task't task definition)
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleNonBaselinedFaultTaskWithImpacts" );

      Task lGivenTask = new Task( "A034FC32D41C4DDBBC37D76B3E6412F2" );
      EvtEventTable lExpectedTaskEvent = evtEventDao.findByPrimaryKey( new EventKey( "4650:988" ) );
      lGivenTask.setStatus( "ACTV" );
      lGivenTask.setCreationDate( lExpectedTaskEvent.getCreationDt() );
      lGivenTask.setTaskClass( "CORR" );
      lGivenTask.setRevisionDate( lExpectedTaskEvent.getRevisionDt() );
      lGivenTask.setTaskClassMode( "REQ" );
      lGivenTask.setTaskType( TaskType.Fault );
      lGivenTask.setEndDate( new Iso8601DateTime( "2018-04-17T16:00:00Z" ) );
      lGivenTask.setSoftDeadline( false );
      lGivenTask.setEtopsSignificant( false );
      lGivenTask.setHistoric( false );
      lGivenTask.setInventoryId( "8CEBA066DA9011E587B1FB2D7B2472DF" );
      lGivenTask.setStatusDescription( "Active" );
      lGivenTask.setTechnicalReference( new ArrayList<TechnicalReference>() );
      lGivenTask.setDuration( "0" );
      lGivenTask.setFaultSeverity( "MINOR" );
      lGivenTask.setMinimumPlanningYield( new BigDecimal( 80 ) );

      Impact lImpact = new Impact();
      lImpact.setImpact( "IMPACT5 (Impact 5 long description)" );
      lImpact.setImpactDescription( "This is impact 5" );
      lGivenTask.addImpact( lImpact );

      lImpact = new Impact();
      lImpact.setImpact( "IMPACT6 (Impact 6 long description)" );
      lImpact.setImpactDescription( "This is impact 6" );
      lGivenTask.addImpact( lImpact );

      // when
      // - I get the the Task by Id
      Task lRetrievedTask = taskResourceBean.get( lGivenTask.getId(), new TaskSearchParameters() );

      // then
      // - The given task = retrieved task
      Assert.assertEquals( lGivenTask, lRetrievedTask );

   }


   @Test
   public void search_success_withMPCPanelsByWorkPackageId() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleAdhocTaskWithMPCPanels" );

      List<Task> givenTasks = buildMPCTasks();

      TaskSearchParameters taskParams = new TaskSearchParameters();
      taskParams.setWorkPackageId( TASKS_WP_ID );

      List<Task> retrievedTasks = taskResourceBean.search( taskParams );

      assertTasks( givenTasks, retrievedTasks );
   }


   @Test
   public void search_success_withMPCPanelsByBarcode() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleAdhocTaskWithMPCPanels" );

      List<Task> tasks = buildMPCTasks();
      Task givenTask = tasks.get( 0 );

      TaskSearchParameters taskParams = new TaskSearchParameters();
      taskParams.setBarcode( givenTask.getBarcode() );

      List<Task> retrievedTasks = taskResourceBean.search( taskParams );

      assertTasks( Arrays.asList( givenTask ), retrievedTasks );
   }


   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_withPanelsByWorkPackageId() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleAdhocTasksWithNonMPCPanels" );

      List<Task> givenTasks = buildNonMPCTasks();

      TaskSearchParameters taskParams = new TaskSearchParameters();
      taskParams.setWorkPackageId( TASKS_WP_ID );

      List<Task> retrievedTasks = taskResourceBean.search( taskParams );

      assertTasks( givenTasks, retrievedTasks );

   }


   @Test
   public void get_success_withPanelsByTaskId() throws AmApiResourceNotFoundException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass(),
            "TaskResourceBeanTest_simpleAdhocTasksWithNonMPCPanels" );

      List<Task> tasks = buildNonMPCTasks();
      Task givenTask = tasks.get( 0 );
      givenTask.setTechnicalReference( new ArrayList<TechnicalReference>() );

      Task retrievedTask = taskResourceBean.get( givenTask.getId(), new TaskSearchParameters() );

      assertTasks( Arrays.asList( givenTask ), Arrays.asList( retrievedTask ) );

   }


   private List<Task> buildMPCTasks() {

      // Build panels and then add MPC information to them
      List<TaskPanel> maintTaskPanels = buildTaskPanels( PANEL_2_ID, PANEL_2_CD, PANEL_2_ZONE_CD );
      List<TaskPanel> mpcTaskPanels = buildTaskPanels( PANEL_2_ID, PANEL_2_CD, PANEL_2_ZONE_CD );

      for ( TaskPanel panel : maintTaskPanels ) {
         TaskPanelOpenCloseRecord openRecord = new TaskPanelOpenCloseRecord();
         TaskPanelOpenCloseRecord closeRecord = new TaskPanelOpenCloseRecord();
         openRecord.setDate( "web.lbl.PENDING" );
         closeRecord.setDate( "web.lbl.PENDING" );

         panel.setOpenPanel( openRecord );
         panel.setClosePanel( closeRecord );
      }

      for ( TaskPanel panel : mpcTaskPanels ) {
         panel.setMaintenanceTaskId( MAINT_TASK_ID );
         panel.setStatus( MPC_PANEL_STATUS );
      }

      Task maintTask = buildTask( MAINT_TASK_ID, "4650:788", ADHOC_TASK_CLASS, null,
            maintTaskPanels, null, "MPC Test - Adhoc Task", null, null, "T000171E" );
      maintTask.setTaskType( TaskType.AdhocTask );

      Task openTask = buildTask( "A034FC32D41C4DDBBC37D76B3E6412F3", "4650:789", MPC_TASKS_CLASS,
            "MPCOPEN", mpcTaskPanels, MPC_TASKS_TASK_DEFN_ID, "MPC Test - MPC OPEN Task",
            MPC_TASKS_CD, "OPEN", "T000171F" );

      Task closeTask = buildTask( "A034FC32D41C4DDBBC37D76B3E6412F4", "4650:790", MPC_TASKS_CLASS,
            "MPCCLOSE", mpcTaskPanels, MPC_TASKS_TASK_DEFN_ID, "MPC Test - MPC CLOSE Task",
            MPC_TASKS_CD, "CLOSE", "T000171G" );

      return Arrays.asList( maintTask, openTask, closeTask );
   }


   private List<Task> buildNonMPCTasks() {

      Task task1 = buildTask( "A034FC32D41C4DDBBC37D76B3E6412FF", "4650:688", ADHOC_TASK_CLASS,
            null, buildTaskPanels( PANEL_2_ID, PANEL_2_CD, PANEL_2_ZONE_CD ), null,
            "Panel Test - Adhoc Task 1", null, null, "T000172E" );
      task1.setTaskType( TaskType.AdhocTask );

      Task task2 = buildTask( "A034FC32D41C4DDBBC37D76B3E6412FD", "4650:689", ADHOC_TASK_CLASS,
            null, buildTaskPanels( PANEL_3_ID, PANEL_3_CD, PANEL_3_ZONE_CD ), null,
            "Panel Test - Adhoc Task 2", null, null, "T000172F" );
      task2.setTaskType( TaskType.AdhocTask );

      return Arrays.asList( task1, task2 );

   }


   private List<TaskPanel> buildTaskPanels( String panelId, String panelCd, String panelZoneCd ) {
      TaskPanel panel1 = new TaskPanel();
      panel1.setId( PANEL_1_ID );
      panel1.setCode( PANEL_1_CD );
      panel1.setZoneCode( PANEL_1_ZONE_CD );

      TaskPanel panel2 = new TaskPanel();
      panel2.setId( panelId );
      panel2.setCode( panelCd );
      panel2.setZoneCode( panelZoneCd );
      return Arrays.asList( panel1, panel2 );

   }


   private Task buildTask( String taskId, String taskKey, String taskClass, String taskSubclass,
         List<TaskPanel> taskPanels, String taskDefnId, String taskName, String taskCd,
         String userSubclass, String taskBarcode ) {

      Task task = new Task( taskId );
      EvtEventTable expectedTaskEvent = evtEventDao.findByPrimaryKey( new EventKey( taskKey ) );
      task.setCreationDate( expectedTaskEvent.getCreationDt() );
      task.setTaskClass( taskClass );
      task.setSubclass( taskSubclass );
      task.setRevisionDate( expectedTaskEvent.getRevisionDt() );
      task.setTaskClassMode( taskClass );
      task.setPanels( taskPanels );
      task.setTaskDefnId( taskDefnId );
      task.setName( taskName );
      task.setTaskCode( taskCd );
      task.setUserSubclass( userSubclass );
      task.setBarcode( taskBarcode );
      task.setStatus( STATUS_ACTV );
      task.setHistoric( false );
      task.setEtopsSignificant( false );
      task.setInventoryId( TASKS_INV_ID );
      task.setWorkPackageId( TASKS_WP_ID );
      task.setMinimumPlanningYield( new BigDecimal( 80 ) );
      task.setEndDate( new Iso8601DateTime( "2018-04-17T16:00:00Z" ) );
      task.setStatusDescription( STATUS_DESC );
      task.setDuration( "0" );
      task.setParentAssemblyId( TASKS_ASSEMBLY_ID );

      return task;
   }


   private void assertTasks( List<Task> expectedTasks, List<Task> actualTasks ) {

      Assert.assertEquals( "There were " + actualTasks.size() + " tasks retrieved instead of "
            + expectedTasks.size(), expectedTasks.size(), actualTasks.size() );

      /** We need to sort both the task and panel lists **/
      Comparator<TaskPanel> taskPanelComparator = new Comparator<TaskPanel>() {

         @Override
         public int compare( TaskPanel panel1, TaskPanel panel2 ) {
            return ( panel1.getId() + panel1.getZoneCode() )
                  .compareTo( panel2.getId() + panel2.getZoneCode() );
         }
      };

      Comparator<Task> taskComparator = new Comparator<Task>() {

         @Override
         public int compare( Task task1, Task task2 ) {
            return task1.getId().compareTo( task2.getId() );
         }
      };

      for ( int i = 0; i < actualTasks.size(); i++ ) {
         Task eTask = expectedTasks.get( i );
         Task aTask = actualTasks.get( i );

         eTask.getPanels().sort( taskPanelComparator );
         aTask.getPanels().sort( taskPanelComparator );
      }

      expectedTasks.sort( taskComparator );
      actualTasks.sort( taskComparator );

      Assert.assertEquals( expectedTasks, actualTasks );
   }

}
