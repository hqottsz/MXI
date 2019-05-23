package com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBContext;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.persistence.QueryTestEntityManagerProviderBean;
import com.mxi.am.api.resource.maintenance.exec.task.JobCardStep;
import com.mxi.am.api.resource.maintenance.exec.task.Measurement;
import com.mxi.am.api.resource.maintenance.exec.task.PartMovement;
import com.mxi.am.api.resource.maintenance.exec.task.PartRequirement;
import com.mxi.am.api.resource.maintenance.exec.task.Task;
import com.mxi.am.api.resource.maintenance.exec.task.TaskPanel;
import com.mxi.am.api.resource.maintenance.exec.task.TaskSearchParameters;
import com.mxi.am.api.resource.maintenance.exec.task.ToolRequirement;
import com.mxi.am.api.resource.maintenance.exec.task.impl.TaskResourceBean;
import com.mxi.am.api.resource.maintenance.exec.task.labour.InstalledInventory;
import com.mxi.am.api.resource.maintenance.exec.task.labour.Labour;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourRole;
import com.mxi.am.api.resource.maintenance.exec.task.labour.RemovedInventory;
import com.mxi.am.api.resource.maintenance.exec.task.labour.Tool;
import com.mxi.am.api.resource.maintenance.exec.task.labour.impl.LabourResourceBean;
import com.mxi.am.api.resource.materials.asset.inventory.Inventory;
import com.mxi.am.api.resource.materials.asset.inventory.impl.InventoryResourceBean;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.alert.AlertEngine;
import com.mxi.mx.common.alert.AlertEngineStub;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.esigner.EsignatureService;
import com.mxi.mx.core.services.stask.EditSchedLabourTO;
import com.mxi.mx.core.services.stask.SchedStaskUtils;
import com.mxi.mx.core.services.stask.labour.DefaultLabourRoleStatusFinder;
import com.mxi.mx.core.services.stask.labour.LabourRoleUtils;
import com.mxi.mx.core.services.stask.labour.LabourService;
import com.mxi.mx.core.services.stask.labour.workcapture.WorkCaptureHelper;
import com.mxi.mx.core.services.stask.measurement.MeasurementDetailsTO;
import com.mxi.mx.core.services.stask.measurement.MeasurementService;
import com.mxi.mx.core.services.stask.panel.SchedPanelService;
import com.mxi.mx.core.services.stask.status.StatusService;
import com.mxi.mx.core.services.stask.step.MxStepService;
import com.mxi.mx.core.services.stask.taskpart.PartRequirementTO;
import com.mxi.mx.core.services.stask.taskpart.ScheduledPartService;
import com.mxi.mx.core.services.stask.tool.ToolService;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.db.jpa.TestEntityManagerProvider;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Tests the Work Capture API functionality
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class WorkCaptureResourceBeanTest extends ResourceBeanTest {

   private static final int AUTHORIZED_USER_ID = 2;
   private static final int MXI_USER_ID = 3;
   private static final String MXI_USER = "mxi";
   private static final HumanResourceKey hrKey = new HumanResourceKey( 4650, 103 );

   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private static final String WC_START_DATE = "2019-01-17 00:56:00";
   private static final String WC_END_DATE = "2019-01-17 10:56:00";

   private static final String WP_ID = "29B2673ABD6249DBBB36F2B3173B086D";
   private static final String WP_INV_ID = "601435E495494F34965B1588F5A6036D";
   private static final String WP_NUMBER = "wo - 131745";
   private static final LocationKey WP_LOC_KEY = new LocationKey( 4650, 1 );
   private static final String WP_SCHED_START_DATE = "2019-01-17 08:00:00";
   private static final String WP_SCHED_END_DATE = "2019-01-20 08:00:00";
   private static final String WP_ACTUAL_START_DATE = "2019-01-16 08:01:00";

   private static final String ASSMBL_CD = "B767-200";

   private static final String TASK_CLASS = "ADHOC";
   private static final String TASK_NAME = "Test Work Capture Task ";
   private static final String TASK_SELECT_ID = "ID";

   private static final String ACTV_STATUS = "ACTV";
   private static final String COMPLETE_STATUS = "COMPLETE";
   private static final String PENDING_STATUS = "PENDING";
   private static final String MXCOMPLETE_STATUS = "MXCOMPLETE";

   private static final double LABOUR_HOURS = 10.0;
   private static final String LBR_LABOUR_SKILL = "LBR";
   private static final String INSP_LABOUR_SKILL = "INSP";
   private static final String TECH_LABOUR_ROLE = "TECH";
   private static final String CERT_LABOUR_ROLE = "CERT";
   private static final String INSP_LABOUR_ROLE = "INSP";

   private static final String TOOL_INV_ID = "FF97DFB7E7CFE1E68E3BB36E9D4E73AA";
   private static final PartGroupKey TOOL_PART_GRP_KEY = new PartGroupKey( 4650, 101 );

   private static final ConfigSlotPositionKey PART_ASSMBL_POS_KEY =
         new ConfigSlotPositionKey( 4650, ASSMBL_CD, 0, 1 );
   private static final ConfigSlotPositionKey PART_POS_KEY =
         new ConfigSlotPositionKey( 4650, ASSMBL_CD, 11, 1 );
   private static final PartGroupKey PART_PART_GRP_KEY = new PartGroupKey( 4650, 2 );
   private static final PartNoKey PART_KEY = new PartNoKey( 4650, 1001 );

   private static final String RMV_PART_INV_ID = "60143EFA95494F34965B1588F5A6036D";
   private static final String INSTALL_PART_INV_ID = "60143EFA95494F34965B1588FFABF36D";

   private static final String PANEL_ID = "F16CF7120C41409AAE9C60B53711488D";
   private static final PanelKey PANEL_KEY = new PanelKey( 4650, 388 );

   private static final String INVALID_ID = "00000000000000000000000000000000";

   private static final DataTypeKey LBS_KEY = new DataTypeKey( 4650, 301 );
   private static final RefDomainTypeKey TEXT_KEY = new RefDomainTypeKey( 0, "TEXT" );
   private static final RefReqActionKey NO_REQ_KEY = new RefReqActionKey( 0, "NOREQ" );
   private static final RefWorkTypeKey LINE_KEY = new RefWorkTypeKey( 10, "LINE" );
   private static final String VENDOR_RETURN_CD = "VENDRET";
   private static final String SHIFT_CHANGE_CD = "TPSHFT";

   @ClassRule
   public static InjectionOverrideRule fakeGuiceDaoRule;

   static {

      Module overrideModule = new AbstractModule() {

         @Override
         protected void configure() {

            bind( Security.class ).to( CoreSecurity.class );
            bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
            bind( SchedStaskDao.class ).to( JdbcSchedStaskDao.class );
            bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );

            bind( EntityManager.class ).toProvider( TestEntityManagerProvider.class );
            bind( TestEntityManagerProvider.class ).to( QueryTestEntityManagerProviderBean.class );

            bind( AlertEngine.class ).to( AlertEngineStub.class );

         }
      };

      fakeGuiceDaoRule = new InjectionOverrideRule( overrideModule );
   }

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Mock
   private WorkCaptureHelper workCaptureHelper;

   @Inject
   LegacyKeyUtil legacyKeyUtil;

   @Inject
   private WorkCaptureResourceBean workCaptureResourceBean;

   @Inject
   private LabourResourceBean labourResourceBean;

   @Inject
   private TaskResourceBean taskResourceBean;

   @Inject
   private InventoryResourceBean inventoryResourceBean;

   private ToolService toolService;
   private LabourService labourService;
   private MxStepService stepService;
   private SchedPanelService schedPanelService;
   private StatusService statusService;
   private ScheduledPartService schedPartService;

   private Task task;
   private Labour expectedLabour;

   private InventoryKey invKey;
   private TaskKey taskKey;


   @Before
   public void setUp() throws MxException, AmApiBusinessException, AmApiResourceNotFoundException,
         KeyConversionException {

      InjectorContainer.get().injectMembers( this );

      workCaptureResourceBean.setEJBContext( ejbContext );
      workCaptureResourceBean.setTestWorkCaptureHelper( workCaptureHelper );

      labourResourceBean = InjectorContainer.get().getInstance( LabourResourceBean.class );
      labourResourceBean.setEJBContext( ejbContext );
      taskResourceBean = InjectorContainer.get().getInstance( TaskResourceBean.class );
      taskResourceBean.setEJBContext( ejbContext );
      inventoryResourceBean = InjectorContainer.get().getInstance( InventoryResourceBean.class );
      inventoryResourceBean.setEJBContext( ejbContext );

      toolService = new ToolService();
      labourService = new LabourService( new LabourRoleUtils(), new EsignatureService(),
            new DefaultLabourRoleStatusFinder(), new WorkCaptureService(), new SchedPanelService(),
            new SequentialUuidGenerator() );
      stepService = new MxStepService();
      schedPanelService = new SchedPanelService();
      statusService = new StatusService( new TaskKey( 4650, 100111 ) );
      schedPartService = new ScheduledPartService();

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      initUserParameters();

      task = buildDefaultAdhocTask();
      expectedLabour = buildDefaultExpectedLabour( task );

   }


   @After
   public void after() {
      UserParameters.setInstance( MXI_USER_ID, ParmTypeEnum.LOGIC.name(), null );
      UserParameters.setInstance( AUTHORIZED_USER_ID, ParmTypeEnum.LOGIC.name(), null );
      UserParameters.setInstance( MXI_USER_ID, ParmTypeEnum.SECURED_RESOURCE.name(), null );
      UserParameters.setInstance( MXI_USER_ID, ParmTypeEnum.MXWEB.name(), null );
   }


   @Test
   public void create_success_mandatoryFields() throws Exception {

      WorkCapture workCapture = buildDefaultWorkCapture();
      scheduleAndStartWorkPackage();

      WorkCapture createdWorkCapture =
            workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );

      Mockito.verify( workCaptureHelper ).lockLabour( Mockito.any( SchedLabourKey.class ) );
      Mockito.verify( workCaptureHelper ).lockTask( Mockito.any( TaskKey.class ) );

      Assert.assertEquals( "Work Capture object has been modified.", workCapture,
            createdWorkCapture );
      Labour actualLabour = labourResourceBean.get( expectedLabour.getId() );
      assertLabour( expectedLabour, actualLabour );
   }


   @Test
   @CSIContractTest( Project.AFKLM_IMECH )
   public void create_success_allFields() throws Exception {

      WorkCapture workCapture = buildDefaultWorkCapture();
      workCapture.setTimeTracking( "REG" );
      workCapture.setAction( "All Fields Test Action" );

      workCapture.addToolsItem( buildTool( TOOL_INV_ID, TOOL_PART_GRP_KEY, BigDecimal.ONE ) );
      workCapture.addMeasurementsItem( buildMeasurement( LBS_KEY, TEXT_KEY, "13" ) );
      workCapture.setNotes( buildNote( true, false, true, false, "Test Doc Reference" ) );
      workCapture.addStepsItem(
            buildStep( "All fields work capture test step", "This step should be completed" ) );

      Parts partItem = buildPart( PART_ASSMBL_POS_KEY, PART_POS_KEY, PART_PART_GRP_KEY, false, true,
            NO_REQ_KEY, 1.0, PART_KEY );

      partItem.setRemovedPart( buildRemovedPart( RMV_PART_INV_ID, BigDecimal.ONE, VENDOR_RETURN_CD,
            partItem.getPartRequirementId() ) );

      // TODO
      // Install Part will be added once OPER-31115 is done.
      // partItem.setInstalledPart( buildInstalledPart("60143EFA95494F34965B1588FFABF36D",
      // BigDecimal.ONE, partItem.getPartRequirementId(), null));

      workCapture.addPartsItem( partItem );
      workCapture.setJobStop( buildJobStop( "Need to install part.", SHIFT_CHANGE_CD, false,
            BigDecimal.valueOf( 2 ) ) );
      workCapture.setLabourId( expectedLabour.getId() );

      scheduleAndStartWorkPackage();

      WorkCapture createdWorkCapture =
            workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );

      Mockito.verify( workCaptureHelper ).lockLabour( Mockito.any( SchedLabourKey.class ) );
      Mockito.verify( workCaptureHelper ).lockTask( Mockito.any( TaskKey.class ) );

      Assert.assertEquals( "Work Capture object has been modified.", workCapture,
            createdWorkCapture );

      Labour actualLabour = labourResourceBean.get( workCapture.getLabourId() );
      assertLabour( expectedLabour, actualLabour );

      task = getTask( task.getId() );

      assertMeasurement( workCapture.getMeasurements().get( 0 ), task.getMeasurements().get( 0 ) );
      assertTool( workCapture.getTools().get( 0 ), task.getToolRequirements().get( 0 ) );
      assertStep( workCapture.getSteps().get( 0 ), task.getJobCardSteps().get( 0 ) );
      assertPartRequirement( workCapture.getParts().get( 0 ), task.getPartRequirements().get( 0 ) );

      Labour expectedCreatedLabour = new Labour();
      expectedCreatedLabour.setTaskId( task.getId() );
      expectedCreatedLabour.setSkill( LBR_LABOUR_SKILL );
      expectedCreatedLabour.setLabourStageCode( ACTV_STATUS );
      expectedCreatedLabour.setInspectionRequired( false );
      expectedCreatedLabour.setCertificationRequired( true );
      expectedCreatedLabour.addLabourRole(
            buildLabourRole( TECH_LABOUR_ROLE, null, 2.0, null, ACTV_STATUS, null ) );
      expectedCreatedLabour.addLabourRole(
            buildLabourRole( CERT_LABOUR_ROLE, null, 0.0, null, PENDING_STATUS, null ) );

      assertJobStop( expectedCreatedLabour, task );

   }


   @Test
   public void create_success_validPanel() throws Exception {
      WorkCapture workCapture = buildDefaultWorkCapture();

      workCapture.addPanelsItem( buildPanel( PANEL_KEY ) );
      generateWorkscope();
      scheduleAndStartWorkPackage();

      WorkCapture createdWorkCapture =
            workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );

      Mockito.verify( workCaptureHelper ).lockLabour( Mockito.any( SchedLabourKey.class ) );
      Mockito.verify( workCaptureHelper ).lockTask( Mockito.any( TaskKey.class ) );

      Assert.assertEquals( "Work Capture object has been modified.", workCapture,
            createdWorkCapture );

      Labour actualLabour = labourResourceBean.get( expectedLabour.getId() );
      assertLabour( expectedLabour, actualLabour );

      task = getTask( task.getId() );
      assertPanel( workCapture.getPanels().get( 0 ), task.getPanels().get( 0 ) );
   }


   @Test
   public void create_failure_workCaptureAlreadyCreated()
         throws MxException, TriggerException, ParseException, AmApiResourceNotFoundException {

      WorkCapture workCapture = buildDefaultWorkCapture();
      scheduleAndStartWorkPackage();

      workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error message: ",
               "Capture work cannot be done because Work Performed by Technician on the labour with ID ["
                     + expectedLabour.getId() + "] is in COMPLETE status.",
               e.getMessage() );
      }

   }


   @Test
   public void create_failure_invalidTimeTracking()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException {

      WorkCapture workCapture = buildDefaultWorkCapture();
      workCapture.setTimeTracking( "OT" );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ", "The Time Tracking value [OT] is invalid.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_invalidMeasurement()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Measurements measurement = new Measurements();
      measurement.setInventoryId( WP_INV_ID );
      measurement.setParameterCode( "LBS" );
      measurement.setValue( "3" );

      workCapture.addMeasurementsItem( measurement );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "The Measurement not found for combination of parameter code [LBS] and inventory id ["
                     + WP_INV_ID + "]",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_certifyWorkTrueAndCertificationRequiredFalse()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();
      workCapture
            .setNotes( buildNote( false, true, true, false, "CertReq = false, CertWork = true" ) );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ", "Labour [" + expectedLabour.getId()
               + "] has 'Certification' disabled and 'Certify Work Performed' or 'Independent Inspection Required' cannot be enabled.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_inspectionReqTrueAndCertificationRequiredFalse()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();
      workCapture.setNotes( buildNote( false, false, false, true,
            "CertReq = false, CertWork = false, InspReq = true" ) );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ", "Labour [" + expectedLabour.getId()
               + "] has 'Certification' disabled and 'Certify Work Performed' or 'Independent Inspection Required' cannot be enabled.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_invalidPanelId() throws ParseException, MxException, TriggerException,
         AmApiResourceNotFoundException, AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Panels panel = new Panels();
      panel.setId( INVALID_ID );

      workCapture.addPanelsItem( panel );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ", "Panel [" + INVALID_ID + "] not found.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_panelNotAddedToTask()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Panels panel = new Panels();
      panel.setId( PANEL_ID );

      workCapture.addPanelsItem( panel );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ", "Panel [" + PANEL_ID + "] not found.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_invalidStep() throws ParseException, MxException, TriggerException,
         AmApiResourceNotFoundException, AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();
      workCapture.addStepsItem( buildStep( "Invalid Step Test", "This should fail." ) );
      workCapture.getSteps().get( 0 ).setOrder( 2 );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "Step order not found for the task [" + expectedLabour.getTaskId() + "].",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_toolInventoryDoesNotExist()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Tools tool = new Tools();
      tool.setInventoryId( INVALID_ID );
      tool.setHours( BigDecimal.ONE );

      workCapture.addToolsItem( tool );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ", "Inventory id [" + INVALID_ID + "] is invalid.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_installedPartInvalidInventory()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Parts partItem = buildPart( PART_ASSMBL_POS_KEY, PART_POS_KEY, PART_PART_GRP_KEY, true, true,
            NO_REQ_KEY, 1.0, PART_KEY );

      partItem.setRemovedPart( buildRemovedPart( RMV_PART_INV_ID, BigDecimal.ONE, VENDOR_RETURN_CD,
            partItem.getPartRequirementId() ) );

      partItem.setInstalledPart( buildInstalledPart( INVALID_ID, BigDecimal.ONE,
            partItem.getPartRequirementId(), null ) );

      workCapture.addPartsItem( partItem );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "Installed part inventory id [" + INVALID_ID + "] is invalid.", e.getMessage() );
      }
   }


   @Test
   public void create_failure_installedPartInvalidQuantity()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Parts partItem = buildPart( PART_ASSMBL_POS_KEY, PART_POS_KEY, PART_PART_GRP_KEY, true, true,
            NO_REQ_KEY, 1.0, PART_KEY );

      partItem.setRemovedPart( buildRemovedPart( RMV_PART_INV_ID, BigDecimal.ONE, VENDOR_RETURN_CD,
            partItem.getPartRequirementId() ) );

      partItem.setInstalledPart( buildInstalledPart( INSTALL_PART_INV_ID, BigDecimal.TEN,
            partItem.getPartRequirementId(), null ) );

      workCapture.addPartsItem( partItem );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "Installed parts quantity [10.0] does not match with part requirement installed part quantity [1.0] in Maintenix.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_removedPartInvalidInventory()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Parts partItem = buildPart( PART_ASSMBL_POS_KEY, PART_POS_KEY, PART_PART_GRP_KEY, true, true,
            NO_REQ_KEY, 1.0, PART_KEY );

      partItem.setRemovedPart( buildRemovedPart( INVALID_ID, BigDecimal.ONE, VENDOR_RETURN_CD,
            partItem.getPartRequirementId() ) );

      workCapture.addPartsItem( partItem );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "Removed part inventory id [" + INVALID_ID + "] is invalid.", e.getMessage() );
      }
   }


   @Test
   public void create_failure_removedPartInvalidQuantity()
         throws ParseException, MxException, TriggerException, AmApiResourceNotFoundException,
         AmApiBusinessException, KeyConversionException {

      WorkCapture workCapture = buildDefaultWorkCapture();

      Parts partItem = buildPart( PART_ASSMBL_POS_KEY, PART_POS_KEY, PART_PART_GRP_KEY, true, true,
            NO_REQ_KEY, 1.0, PART_KEY );

      partItem.setRemovedPart( buildRemovedPart( RMV_PART_INV_ID, BigDecimal.TEN, VENDOR_RETURN_CD,
            partItem.getPartRequirementId() ) );
      workCapture.addPartsItem( partItem );

      scheduleAndStartWorkPackage();

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "Removed parts quantity [10.0] does not match with part requirement removed part quantity [1.0] in Maintenix.",
               e.getMessage() );
      }
   }


   @Test
   public void create_failure_invalidLabourID() throws ParseException {
      WorkCapture workCapture = buildDefaultWorkCapture();

      try {
         workCaptureResourceBean.createWorkCapture( INVALID_ID, null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "The Labour with ID [" + INVALID_ID + "] not found.", e.getMessage() );
      }
   }


   @Test
   public void create_failure_mismatchedLabourID() throws ParseException {
      WorkCapture workCapture = buildDefaultWorkCapture();
      workCapture.setLabourId( INVALID_ID );

      try {
         workCaptureResourceBean.createWorkCapture( expectedLabour.getId(), null, workCapture );
         Assert.fail( "Work Capture API should have thrown an AmApiBadRequestException." );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Error Message: ",
               "Labour id [" + expectedLabour.getId()
                     + "] in the url path does not match with labour id ["
                     + workCapture.getLabourId() + "] in the work capture.",
               e.getMessage() );
      }
   }


   private void assertLabour( Labour expectedLabour, Labour actualLabour ) {

      Assert.assertEquals( "Labour Task ID: ", expectedLabour.getTaskId(),
            actualLabour.getTaskId() );
      Assert.assertEquals( "Labour Stage Code: ", expectedLabour.getLabourStageCode(),
            actualLabour.getLabourStageCode() );
      Assert.assertEquals( "Labour Certification Required Value: ",
            expectedLabour.isCertificationRequired(), actualLabour.isCertificationRequired() );
      Assert.assertEquals( "Labour Inspection Required Value: ",
            expectedLabour.isInspectionRequired(), actualLabour.isInspectionRequired() );
      Assert.assertEquals( "Labour Skill: ", expectedLabour.getSkill(), actualLabour.getSkill() );

      if ( expectedLabour.getTools() != null ) {
         Assert.assertTrue(
               "Labour Tools:\n Expected [" + expectedLabour.getTools() + "]\n Actual ["
                     + actualLabour.getTools() + "]",
               expectedLabour.getTools().containsAll( actualLabour.getTools() )
                     && actualLabour.getTools().containsAll( expectedLabour.getTools() ) );
      }

      if ( expectedLabour.getRemovedInventories() != null ) {
         Assert.assertTrue(
               "Labour Removed Inventories:\n Expected [" + expectedLabour.getRemovedInventories()
                     + "]\n Actual [" + actualLabour.getRemovedInventories() + "]",
               expectedLabour.getRemovedInventories()
                     .containsAll( actualLabour.getRemovedInventories() )
                     && actualLabour.getRemovedInventories()
                           .containsAll( expectedLabour.getRemovedInventories() ) );
      }

      if ( expectedLabour.getInstalledInventories() != null ) {
         Assert.assertTrue(
               "Labour Installed Inventories:\n Expected ["
                     + expectedLabour.getInstalledInventories() + "]\n Actual ["
                     + actualLabour.getInstalledInventories() + "]",
               expectedLabour.getInstalledInventories()
                     .containsAll( actualLabour.getInstalledInventories() )
                     && actualLabour.getInstalledInventories()
                           .containsAll( expectedLabour.getInstalledInventories() ) );
      }

      Assert.assertTrue(
            "Labour Roles:\n Expected [" + expectedLabour.getLabourRoles() + "]\n Actual ["
                  + actualLabour.getLabourRoles() + "]",
            expectedLabour.getLabourRoles().containsAll( actualLabour.getLabourRoles() )
                  && actualLabour.getLabourRoles().containsAll( expectedLabour.getLabourRoles() ) );

   }


   private void assertMeasurement( Measurements expectedMeasurement,
         Measurement actualMeasurement ) {

      Assert.assertEquals( "Measurement Inventory ID: ", expectedMeasurement.getInventoryId(),
            actualMeasurement.getInventoryId() );
      Assert.assertEquals( "Measurement Parameter Code: ", expectedMeasurement.getParameterCode(),
            actualMeasurement.getParameterCode() );
      Assert.assertEquals( "Measurement Value: ", expectedMeasurement.getValue(),
            actualMeasurement.getValue() );

   }


   private void assertTool( Tools expectedTool, ToolRequirement actualToolReq ) {
      Assert.assertEquals( "Tool Inventory ID: ", expectedTool.getInventoryId(),
            actualToolReq.getInventoryId() );
      Assert.assertEquals(
            "Tool Hours: Expected:[" + expectedTool.getHours() + "] Actual:["
                  + actualToolReq.getActualHours() + "]",
            0, expectedTool.getHours()
                  .compareTo( BigDecimal.valueOf( actualToolReq.getActualHours() ) ) );

   }


   private void assertStep( Steps expectedStep, JobCardStep actualStep ) {
      Assert.assertEquals( "Step Status Code: ", expectedStep.getStatusCode(),
            actualStep.getStatusCode() );
      Assert.assertEquals( "Step Order: ", expectedStep.getOrder(),
            ( Integer ) actualStep.getOrderNumber() );
      Assert.assertEquals( "Step Notes: ", expectedStep.getNote(),
            actualStep.getLabourSteps().get( 0 ).getNotes() );
   }


   private void assertPartRequirement( Parts expectedPart, PartRequirement actualPartReq ) {

      Assert.assertEquals( "Part Position Code: ", expectedPart.getPositionCode(),
            actualPartReq.getPosition() );
      Assert.assertEquals( "Part Requirement ID: ", expectedPart.getPartRequirementId(),
            actualPartReq.getId() );

      if ( expectedPart.getRemovedPart() != null ) {
         RemovedPart expectedRemovedPart = expectedPart.getRemovedPart();
         PartMovement actualRemovedPart = actualPartReq.getRemoval();

         Assert.assertEquals( "Removed Part Inventory ID: ", expectedRemovedPart.getInventoryId(),
               actualRemovedPart.getInventoryId() );
         Assert.assertEquals(
               "Removed Part Quantity: Expected:[" + expectedRemovedPart.getQuantity()
                     + "] Actual:[" + actualRemovedPart.getQuantity() + "]",
               0, expectedRemovedPart.getQuantity()
                     .compareTo( BigDecimal.valueOf( actualRemovedPart.getQuantity() ) ) );
         Assert.assertEquals( "Removed Part Reason Code: ", expectedRemovedPart.getReasonCode(),
               actualRemovedPart.getReasonCode() );
         Assert.assertEquals( "Removed Part Labour ID: ", expectedLabour.getId(),
               actualRemovedPart.getLabourId() );
      }

   }


   private void assertJobStop( Labour expectedCreatedLabour, Task updatedTask ) {

      Assert.assertEquals( "PAUSE", task.getStatus() );
      Assert.assertEquals( "Number of labours: ", 2, updatedTask.getLabour().size() );

      Labour actualCreatedLabour = null;

      for ( Labour labour : updatedTask.getLabour() ) {
         if ( !labour.getId().equals( expectedLabour.getId() ) ) {
            actualCreatedLabour = labour;
            expectedCreatedLabour.setId( actualCreatedLabour.getId() );
         }
      }

      assertLabour( expectedCreatedLabour, actualCreatedLabour );

   }


   private void assertPanel( Panels expectedPanel, TaskPanel actualPanel ) {
      Assert.assertEquals( "Panel ID: ", expectedPanel.getId(), actualPanel.getId() );
      actualPanel.getLabourHours();
      actualPanel.getStatus();
   }


   private WorkCapture buildDefaultWorkCapture() throws ParseException {
      WorkCapture workCapture = new WorkCapture();
      workCapture.setStartDate( new SimpleDateFormat( DATE_FORMAT ).parse( WC_START_DATE ) );
      workCapture.setEndDate( new SimpleDateFormat( DATE_FORMAT ).parse( WC_END_DATE ) );
      workCapture.setHours( BigDecimal.TEN );

      Signature signature = new Signature();
      signature.setUsername( MXI_USER );
      workCapture.setSignature( signature );
      return workCapture;
   }


   private Labour buildDefaultExpectedLabour( Task task ) {

      // We build the end labour from the task's labour
      Labour taskLabour = task.getLabour().get( 0 );

      Labour labour = new Labour();
      labour.setId( taskLabour.getId() );
      labour.setTaskId( taskLabour.getTaskId() );
      labour.setSkill( LBR_LABOUR_SKILL );
      labour.setLabourStageCode( COMPLETE_STATUS );
      labour.setInspectionRequired( false );
      labour.setCertificationRequired( false );

      labour.addLabourRole( buildLabourRole( TECH_LABOUR_ROLE, LABOUR_HOURS, 0.0, LABOUR_HOURS,
            COMPLETE_STATUS, MXI_USER ) );

      return labour;

   }


   private Task buildDefaultAdhocTask()
         throws AmApiBusinessException, AmApiResourceNotFoundException, KeyConversionException {

      Task task = new Task();
      task.setName( TASK_NAME );
      task.setHistoric( false );
      task.setInventoryId( WP_INV_ID );
      task.setTaskClass( TASK_CLASS );
      task.setStatus( ACTV_STATUS );

      TaskSearchParameters taskSearchParameters = new TaskSearchParameters();
      taskSearchParameters.setSelect( TASK_SELECT_ID );
      task = taskResourceBean.create( task, taskSearchParameters );
      task.setWorkPackageId( WP_ID );

      // Set the inventory and task keys
      invKey = legacyKeyUtil.altIdToLegacyKey( task.getInventoryId(), InventoryKey.class );
      taskKey = legacyKeyUtil.altIdToLegacyKey( task.getId(), TaskKey.class );

      // Update the task work type
      SchedStaskUtils.updateWorkTypes( taskKey, Arrays.asList( LINE_KEY ) );

      task = taskResourceBean.update( task.getId(), task );

      return task;

   }


   private Measurements buildMeasurement( DataTypeKey dataTypeKey, RefDomainTypeKey domainTypeKey,
         String value ) throws AmApiResourceNotFoundException, AmApiBusinessException, MxException {

      // Add measurement to the task
      MeasurementDetailsTO measurementTO = new MeasurementDetailsTO();
      measurementTO.setDataType( dataTypeKey );
      measurementTO.setInventoryKey( invKey );
      measurementTO.setDomainType( domainTypeKey );

      MeasurementService.addMeasurement( taskKey, new MeasurementDetailsTO[] { measurementTO } );

      task = getTask( task.getId() );
      Measurement taskMeasurement = task.getMeasurements().get( task.getMeasurements().size() - 1 );

      // Build measurement for the work capture
      Measurements measurement = new Measurements();
      measurement.setInventoryId( taskMeasurement.getInventoryId() );
      measurement.setParameterCode( taskMeasurement.getParameterCode() );
      measurement.setValue( value );

      return measurement;

   }


   private Tools buildTool( String inventoryId, PartGroupKey partGroupKey, BigDecimal hours )
         throws TriggerException, AmApiResourceNotFoundException {

      // Add the tool requirement to the task
      toolService.addTool( taskKey,
            new com.mxi.mx.core.services.stask.tool.Tool( null, partGroupKey, 0.0 ) );

      // Add the tool to the expected labour
      task = getTask( task.getId() );
      Inventory inv = inventoryResourceBean.get( inventoryId );
      ToolRequirement toolReq = task.getToolRequirements().get( 0 );
      Tool labourTool = new Tool();
      labourTool.setInventoryId( inventoryId );
      labourTool.setPartId( toolReq.getPartId() );
      labourTool.setInventorySerialNumber( inv.getSerialNumber() );
      labourTool.setManufacturerCode( inv.getManufacturerCode() );
      labourTool.setPartNumber( inv.getPartNumber() );

      Set<Tool> toolSet = expectedLabour.getTools() == null ? ( Set<Tool> ) new HashSet<Tool>()
            : expectedLabour.getTools();
      toolSet.add( labourTool );
      expectedLabour.setTools( toolSet );

      // Build the tool to the work capture
      Tools tool = new Tools();
      tool.setInventoryId( inventoryId );
      tool.setHours( hours );

      return tool;

   }


   private Notes buildNote( Boolean certificationRequired, Boolean inspectionRequired,
         Boolean certifyWorkPerformed, Boolean independentInspectionRequired, String docReference )
         throws AmApiResourceNotFoundException, AmApiBusinessException, KeyConversionException,
         MxException, TriggerException {

      // Add to the expected labour
      expectedLabour.setCertificationRequired( certificationRequired );
      expectedLabour.setInspectionRequired( inspectionRequired );

      // If Certification Required is checked, we update the expected labour to have a CERT labour
      // role with PENDING or COMPLETE status (depending on the Certify Work Performed value we send
      // to the Work Capture API).
      if ( certifyWorkPerformed && certificationRequired ) {
         expectedLabour.addLabourRole(
               buildLabourRole( CERT_LABOUR_ROLE, 0.0, 0.0, 0.0, COMPLETE_STATUS, MXI_USER ) );
      } else if ( certificationRequired ) {
         expectedLabour.addLabourRole(
               buildLabourRole( CERT_LABOUR_ROLE, 0.0, 0.0, 0.0, PENDING_STATUS, null ) );
      }

      // If Independent Inspection Required is true, we add a INSP labour role with PENDING status.
      if ( independentInspectionRequired ) {
         expectedLabour.addLabourRole(
               buildLabourRole( INSP_LABOUR_ROLE, 0.0, 0.0, 0.0, PENDING_STATUS, null ) );
      }

      // Add to current labour
      EditSchedLabourTO labourTO = new EditSchedLabourTO();
      labourTO.setCertificationRequired( certificationRequired );
      labourTO.setIndependentInspectionRequired( inspectionRequired );
      labourTO.setSchedLabour(
            legacyKeyUtil.altIdToLegacyKey( expectedLabour.getId(), SchedLabourKey.class ) );
      labourService.edit( Arrays.asList( labourTO ), hrKey );

      // Build notes for the work capture
      Notes notes = new Notes();
      notes.setCertifyWorkPerformed( certifyWorkPerformed );
      notes.setIndependentInspectionRequired( independentInspectionRequired );
      notes.setDocumentReference( docReference );

      return notes;
   }


   private Steps buildStep( String stepDescription, String note )
         throws MxException, AmApiResourceNotFoundException {

      // Add step to the task
      stepService.addStep( taskKey, stepDescription );

      task = getTask( task.getId() );
      JobCardStep taskStep = task.getJobCardSteps().get( task.getJobCardSteps().size() - 1 );

      // Build step for the work capture
      Steps step = new Steps();
      step.setOrder( taskStep.getOrderNumber() );
      step.setStatusCode( MXCOMPLETE_STATUS );
      step.setNote( note );

      return step;
   }


   private JobStop buildJobStop( String notes, String reasonCd, Boolean reassignMe,
         BigDecimal remainingHours ) {

      JobStop jobStop = new JobStop();
      jobStop.setNotes( notes );
      jobStop.setReasonCode( reasonCd );
      jobStop.setReassignMe( reassignMe );
      jobStop.setRemainingHours( remainingHours );

      return jobStop;
   }


   private Panels buildPanel( PanelKey panelKey )
         throws MxException, TriggerException, AmApiResourceNotFoundException {

      // Add MPC panel to task
      schedPanelService.addPanel( taskKey, new PanelKey[] { panelKey }, hrKey );

      task = getTask( task.getId() );
      TaskPanel taskPanel = task.getPanels().get( task.getPanels().size() - 1 );

      // Build panel for the work capture
      Panels panel = new Panels();
      panel.setId( taskPanel.getId() );

      return panel;
   }


   private Parts buildPart( ConfigSlotPositionKey assyPositionKey,
         ConfigSlotPositionKey positionKey, PartGroupKey partGroupKey, Boolean isInstall,
         Boolean isRemove, RefReqActionKey requestActionKey, Double quantity, PartNoKey partNoKey )
         throws MxException, TriggerException, AmApiResourceNotFoundException {

      PartRequirementTO partReqTO = new PartRequirementTO();
      partReqTO.setAssyBomItemPositionKey( assyPositionKey );
      partReqTO.setBomItemPositionKey( positionKey );
      partReqTO.setBomPartKey( partGroupKey );
      partReqTO.setHumanResource( hrKey );
      partReqTO.setIsInstall( isInstall );
      partReqTO.setIsRemove( isRemove );
      partReqTO.setRequestAction( requestActionKey );
      partReqTO.setSchedQty( quantity );
      partReqTO.setSpecPartNoKey( partNoKey );

      // Add part requirement to the task
      schedPartService.schedulePartRequirement( taskKey, partReqTO, true, true, true );

      task = getTask( task.getId() );
      PartRequirement partReq =
            task.getPartRequirements().get( task.getPartRequirements().size() - 1 );

      // Build part for the work capture
      Parts part = new Parts();
      part.setPartRequirementId( partReq.getId() );
      part.setPositionCode( partReq.getPosition() );

      return part;
   }


   private RemovedPart buildRemovedPart( String inventoryId, BigDecimal quantity, String reasonCd,
         String partRequirementId ) {

      RemovedInventory removedInv = new RemovedInventory();
      removedInv.setInventoryId( inventoryId );
      removedInv.setPartRequirementId( partRequirementId );
      removedInv.setRemovalReasonCode( reasonCd );
      removedInv.setRemovedQuantity( quantity.doubleValue() );

      // Add removed part to expected labour
      expectedLabour.addRemovedInventory( removedInv );

      // Build removed part for the work capture
      RemovedPart removedPart = new RemovedPart();
      removedPart.setInventoryId( inventoryId );
      removedPart.setQuantity( quantity );
      removedPart.setReasonCode( reasonCd );

      return removedPart;
   }


   private InstalledPart buildInstalledPart( String inventoryId, BigDecimal quantity,
         String partRequirementId, String partRequestId ) {

      InstalledInventory installedInv = new InstalledInventory();
      installedInv.setInventoryId( inventoryId );
      installedInv.setPartRequirementId( partRequirementId );
      installedInv.setPartRequestId( partRequestId );
      installedInv.setInstalledQuantity( quantity.doubleValue() );

      // Add installed part to the expected labour
      expectedLabour.addInstalledInventory( installedInv );

      // Build installed part for the work capture
      InstalledPart installedPart = new InstalledPart();
      installedPart.setInventoryId( inventoryId );
      installedPart.setQuantity( quantity );

      return installedPart;
   }


   private LabourRole buildLabourRole( String role, Double actualHours, Double scheduledHours,
         Double adjustedBillingHours, String statusCode, String username ) {
      LabourRole labourRole = new LabourRole();
      labourRole.setActualHours( actualHours );
      labourRole.setRole( role );
      labourRole.setScheduledHours( scheduledHours );
      labourRole.setAdjustedBillingHours( adjustedBillingHours );
      labourRole.setStatusCode( statusCode );
      labourRole.setUsername( username );
      return labourRole;
   }


   private void generateWorkscope() throws MxException, TriggerException {
      statusService.generateWorkscope( hrKey );
      statusService.commitWorkscope( hrKey, null, null, null );

      TaskSearchParameters taskParms = new TaskSearchParameters();
      taskParms.setWorkPackageId( WP_ID );
      List<Task> tasks = taskResourceBean.search( taskParms );
      for ( Task tmpTask : tasks ) {
         if ( tmpTask.getTaskClass().equals( "MPC" ) ) {
            task = tmpTask;
            expectedLabour = buildDefaultExpectedLabour( task );
            expectedLabour.setSkill( INSP_LABOUR_SKILL );
            break;
         }
      }
   }


   private void scheduleAndStartWorkPackage()
         throws MxException, TriggerException, ParseException, AmApiResourceNotFoundException {

      statusService.scheduleInternal( hrKey, WP_NUMBER, WP_LOC_KEY,
            new SimpleDateFormat( DATE_FORMAT ).parse( WP_SCHED_START_DATE ),
            new SimpleDateFormat( DATE_FORMAT ).parse( WP_SCHED_END_DATE ), null, false, null );
      statusService.startWork( hrKey,
            new SimpleDateFormat( DATE_FORMAT ).parse( WP_ACTUAL_START_DATE ), null, true );

   }


   private Task getTask( String taskId ) throws AmApiResourceNotFoundException {
      return taskResourceBean.get( taskId, new TaskSearchParameters() );
   }


   private void initUserParameters() {
      // We need to set a value for ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP to assign a task to a work
      // package for both mxi and authorized users.
      UserParametersStub userParametersStub =
            new UserParametersStub( MXI_USER_ID, ParmTypeEnum.LOGIC.name() );
      userParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( MXI_USER_ID, ParmTypeEnum.LOGIC.name(), userParametersStub );

      userParametersStub = new UserParametersStub( AUTHORIZED_USER_ID, ParmTypeEnum.LOGIC.name() );
      userParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( AUTHORIZED_USER_ID, ParmTypeEnum.LOGIC.name(),
            userParametersStub );

      // We need to set TRUE for ACTION_CERTIFY_TASK to certify work in the task
      userParametersStub =
            new UserParametersStub( MXI_USER_ID, ParmTypeEnum.SECURED_RESOURCE.name() );
      userParametersStub.setBoolean( "ACTION_CERTIFY_TASK", true );
      UserParameters.setInstance( MXI_USER_ID, ParmTypeEnum.SECURED_RESOURCE.name(),
            userParametersStub );

      // We need to set a value for REASON_FOR_PAUSE_WORK_MANDATORY for Job Stop
      userParametersStub = new UserParametersStub( MXI_USER_ID, ParmTypeEnum.MXWEB.name() );
      userParametersStub.setBoolean( "REASON_FOR_PAUSE_WORK_MANDATORY", false );
      UserParameters.setInstance( MXI_USER_ID, ParmTypeEnum.MXWEB.name(), userParametersStub );
   }

}
