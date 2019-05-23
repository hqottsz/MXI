package com.mxi.mx.core.services.stask.step;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.exception.InvalidEventStatusException;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.JdbcSchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepApplLogTable;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;
import com.mxi.mx.core.task.model.StepStatus;


public class MxStepServiceTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private HumanResourceKey iHr;
   private SchedStepKey iSchedStepKey;
   private TaskKey iTaskKey;
   private InventoryKey aircraftKey;

   private static final String NOTES = "Test Notes";
   private static final String AIRCRAFT_APPLICABILITY_CD = "100";

   private SchedStepDao schedStepDao;

   // subject under test
   private MxStepService stepService;


   @Test
   public void toggleApplicability_valid() throws Exception {

      // data setup
      iSchedStepKey = new SchedStepKey( iTaskKey, 1 );
      SchedStepTableRow lSchedStepTable = schedStepDao.create( iSchedStepKey );
      lSchedStepTable.setStepStatus( StepStatus.MXNA );
      schedStepDao.insert( lSchedStepTable );

      stepService.toggleApplicability( iSchedStepKey, iHr, NOTES );

      // assert the job card step status was toggled
      assertEquals( StepStatus.MXPENDING,
            schedStepDao.findByPrimaryKey( iSchedStepKey ).getStepStatus() );

      // assert a log was created
      DataSetArgument lArguments = new DataSetArgument();
      {
         lArguments.add( iSchedStepKey, SchedStepApplLogTable.ColumnName.SCHED_DB_ID.toString(),
               SchedStepApplLogTable.ColumnName.SCHED_ID.toString(),
               SchedStepApplLogTable.ColumnName.STEP_ID.toString() );
      }

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( SchedStepApplLogTable.TABLE_NAME,
            lArguments, SchedStepApplLogTable.ColumnName.NOTES_LDESC.toString() );

      lQs.next();
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( NOTES,
            lQs.getString( SchedStepApplLogTable.ColumnName.NOTES_LDESC.toString() ) );
   }


   @Test( expected = InvalidEventStatusException.class )
   public void toggleApplicability_invalidStepStatus() throws Exception {

      // data setup
      iSchedStepKey = new SchedStepKey( iTaskKey, 2 );
      SchedStepTableRow lSchedStepTable = schedStepDao.create( iSchedStepKey );
      lSchedStepTable.setStepStatus( StepStatus.MXCOMPLETE );
      schedStepDao.insert( lSchedStepTable );

      stepService.toggleApplicability( iSchedStepKey, iHr, NOTES );
   }


   /**
    *
    * Test that steps on a baseline task are marked as not applicable when not applicable, and left
    * as pending when they are applicable.
    *
    */
   @Test
   public void setApplicability_baselineTask() throws MxException {

      // ARRANGE
      TaskTaskKey reqTaskDefn = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REQ );
         req.setExecutable( true );
         req.addStep( step -> {
            step.setDescription( "Step 1 is not applicable" );
            step.setApplicabilityRange( "200-300" );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 2 is applicable" );
            step.setApplicabilityRange( AIRCRAFT_APPLICABILITY_CD );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 3 has no applicability" );
         } );
      } );

      TaskKey taskKey = Domain.createRequirement( req -> {
         req.setInventory( aircraftKey );
         req.setDefinition( reqTaskDefn );
      } );

      // ACT
      stepService.setStepsApplicability( taskKey, iHr );

      // ASSERT

      // step 1 should be marked as NA
      SchedStepKey schedStepKey = new SchedStepKey( taskKey, 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXNA, schedStepTableRow.getStepStatus() );

      // step 2 is applicable so status should it stay pending
      schedStepKey = new SchedStepKey( taskKey, 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

      // step 3 has no applicability so status should it stay pending
      schedStepKey = new SchedStepKey( taskKey, 3 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

   }


   /**
    *
    * Test that baseline steps on a fault are marked as not applicable when not applicable, and left
    * as pending when they are applicable.
    *
    */
   @Test
   public void setApplicability_fault() throws MxException {

      // ARRANGE
      TaskTaskKey repairReferenceKey = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REPREF );
         req.setExecutable( true );
         req.addStep( step -> {
            step.setDescription( "Step 1 is not applicable" );
            step.setApplicabilityRange( "200-300" );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 2 is applicable" );
            step.setApplicabilityRange( AIRCRAFT_APPLICABILITY_CD );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 3 has no applicability" );
         } );
      } );

      TaskKey taskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftKey );
      } );
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceKey );
         fault.setCorrectiveTask( taskKey );
      } );

      // ACT
      stepService.setStepsApplicability( taskKey, iHr );

      // ASSERT

      // step 1 is not applicable so it should be marked as NA
      SchedStepKey schedStepKey = new SchedStepKey( taskKey, 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXNA, schedStepTableRow.getStepStatus() );

      // step 2 is applicable so status should stay pending
      schedStepKey = new SchedStepKey( taskKey, 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

      // step 3 has no applicability so status should stay pending
      schedStepKey = new SchedStepKey( taskKey, 3 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

   }


   /**
    *
    * Test that task steps are not marked as NA when the task is historic.
    *
    */
   @Test
   public void setApplicability_historicTask() throws MxException {

      // ARRANGE
      TaskTaskKey reqTaskDefn = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REQ );
         req.setExecutable( true );
         req.addStep( step -> {
            step.setDescription( "Step 1 is not applicable" );
            step.setApplicabilityRange( "200-300" );
         } );
      } );

      TaskKey taskKey = Domain.createRequirement( req -> {
         req.setInventory( aircraftKey );
         req.setDefinition( reqTaskDefn );
         req.setStatus( RefEventStatusKey.COMPLETE );
      } );

      // ACT
      stepService.setStepsApplicability( taskKey, iHr );

      // ASSERT

      // step 1 should still be marked as PENDING
      SchedStepKey schedStepKey = new SchedStepKey( taskKey, 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

   }


   /**
    *
    * Test that steps on a baseline task are not updated to NA if they have a non-pending status.
    *
    */
   @Test
   public void setApplicability_baselineTaskNotPendingStatus() throws MxException {

      // ARRANGE
      // create an executable requirement with steps
      TaskTaskKey reqTaskDefn = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REQ );
         req.setExecutable( true );
         req.addLabourRequirement( new LabourRequirement( RefLabourSkillKey.ENG, BigDecimal.TEN,
               BigDecimal.ONE, BigDecimal.ZERO ) );
         req.addStep( step -> {
            step.setDescription( "Step 1 is not applicable" );
            step.setApplicabilityRange( "200-300" );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 2 is not applicable" );
            step.setApplicabilityRange( "200-300" );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 3 has no applicability" );
         } );
      } );

      // create a task from the requirement
      TaskKey taskKey = Domain.createRequirement( req -> {
         req.setInventory( aircraftKey );
         req.setDefinition( reqTaskDefn );
      } );

      // set step 1 to partially complete
      SchedStepKey stepKey = new SchedStepKey( taskKey.getDbId(), taskKey.getId(), 1 );
      stepService.setStepStatusNoIncrementRevision( stepKey, StepStatus.MXPARTIAL );

      // ACT
      stepService.setStepsApplicability( taskKey, iHr );

      // ASSERT

      // step 1 is not applicable but should remain partial
      SchedStepKey schedStepKey = new SchedStepKey( taskKey, 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPARTIAL, schedStepTableRow.getStepStatus() );

      // step 2 is not applicable so status should be NA
      schedStepKey = new SchedStepKey( taskKey, 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXNA, schedStepTableRow.getStepStatus() );

      // step 3 has no applicability so status should stay pending
      schedStepKey = new SchedStepKey( taskKey, 3 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

   }


   @Before
   public void setUp() {

      stepService = new MxStepService();
      iHr = new HumanResourceDomainBuilder().build();
      schedStepDao = new JdbcSchedStepDao();

      // create aircraft inventory
      aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setApplicabilityCode( AIRCRAFT_APPLICABILITY_CD );
      } );

      // create engine inventory
      final InventoryKey lEngine = Domain.createEngine( engine -> {
         engine.setParent( aircraftKey );
      } );

      // create task for engine
      iTaskKey = Domain.createJobCard( task -> {
         task.setStatus( RefEventStatusKey.ACTV );
         task.setInventory( lEngine );
      } );

      // create work-package for aircraft
      Domain.createWorkPackage( workPackage -> {
         workPackage.setAircraft( aircraftKey );
         workPackage.addTask( iTaskKey );
         workPackage.setStatus( RefEventStatusKey.IN_WORK );

      } );

   }
}
