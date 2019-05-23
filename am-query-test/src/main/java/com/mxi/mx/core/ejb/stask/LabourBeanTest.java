package com.mxi.mx.core.ejb.stask;

import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.RefLabourRoleTypeKey.TECH;
import static com.mxi.mx.core.key.RefLabourSkillKey.ENG;
import static com.mxi.mx.core.key.RefLabourSkillKey.PILOT;
import static com.mxi.mx.core.key.RefLabourStageKey.ACTV;
import static com.mxi.mx.core.key.RefLabourStageKey.COMPLETE;
import static com.mxi.mx.core.key.RefLabourStageKey.IN_WORK;
import static java.math.BigDecimal.ONE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.labour.FinishJobTO;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.SchedLabourTO;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.unittest.table.sched.SchedLabour;
import com.mxi.mx.core.unittest.table.sched.SchedLabourRole;


/**
 * Integration unit test for {@link LabourBean}
 *
 */
public class LabourBeanTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private LabourBean iLabourBean = new LabourBean();
   // Task assigned to workpackage
   private TaskKey iTask;
   // indicate the sched hour in the table sched_labour_role
   private BigDecimal ONE_HOUR = ONE;


   /**
    *
    * Verify that when a started work package contains an ACTV task with a labour row, and the
    * labour row is started, then the stage of the labour row changes to in work.
    *
    */
   @Test
   public void labourRowStageChangesToInWorkAfterLabourRowStarted() throws Exception {
      // Given a started/active task with an active labor row
      TaskKey lTask = Domain.createRequirement( lReq -> {
         lReq.setStatus( RefEventStatusKey.ACTV );
         lReq.addLabour( lLabour -> {
            lLabour.setStage( RefLabourStageKey.ACTV );
         } );
      } );
      Domain.createWorkPackage( lWp -> {
         lWp.setStatus( RefEventStatusKey.IN_WORK );
         lWp.addTask( lTask );
      } );
      List<SchedLabourKey> lLabourRows = Domain.readLabourRequirement( lTask );
      assertThat( "Unexpected number of labour rows in the data setup", lLabourRows.size(),
            is( 1 ) );
      SchedLabourKey lLabourRow = lLabourRows.get( 0 );

      // When the labor row is started
      iLabourBean.start( lLabourRow, Domain.createHumanResource() );

      // Then the labor row stage changes to In Work
      assertThat( "Unexpected labour row stage",
            SchedLabourTable.findByPrimaryKey( lLabourRow ).getLabourStage(),
            is( RefLabourStageKey.IN_WORK ) );
   }


   /**
    *
    * Verify that when task with no technician assign labour requirement, and assign a new
    * technician to labour requirement. Then new technician is assigned to labour requirement.
    *
    */
   @Test
   public void assignUserToLabourRequirement() throws Exception {

      // Given the techician to assign to labour requirement
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the task with labour requirement
      iTask = Domain.createAdhocTask( aTask -> {
         // The logic in LabourBean.assign() requires the task to have an inventory set.
         aTask.setInventory( Domain.createAircraft() );
         aTask.addLabour();
      } );

      // Given the labour requirement
      SchedLabourKey labourRequirement = Domain.readLabourRequirement( iTask ).get( 0 );

      // Given the work package with task assigned
      createWorkPackage( wp -> {
         wp.addTask( iTask );
      } );

      // When user is assigned to labour requirement
      updateTechToLabourRequirement( technician, labourRequirement );

      // Then labour requirement indicats Technician.
      assertThat( "Unexcepted that user did not assign to labour requirement properly",
            getHumanResource( labourRequirement ), is( technician ) );
   }


   /**
    *
    * Verify that when start an unassigned labor row, then the labor row assigned a technician.
    *
    * @throws Exception
    */
   @Test
   public void startingAnUnassignedLabourRowResultTechnicianAssigned() throws Exception {

      // Given the technician to assign to labor row
      HumanResourceKey lTechnician = Domain.createHumanResource();

      // Given a task with an active labor row
      TaskKey lTask = Domain.createRequirement( lReq -> {
         lReq.addLabour( lLabour -> {
            lLabour.setSkill( RefLabourSkillKey.ENG );
            lLabour.setStage( ACTV );
         } );
      } );

      // Give a start work package with assigned a task
      Domain.createWorkPackage( lWp -> {
         lWp.setStatus( RefEventStatusKey.IN_WORK );
         lWp.addTask( lTask );
      } );

      SchedLabourKey lLabourRow = Domain.readLabourRequirement( lTask ).get( 0 );

      // When the labor row is started
      iLabourBean.start( lLabourRow, lTechnician );

      // Then user reassigned as a Technician.
      assertThat( "Unexcepted the work performed techincian is unassigned",
            getHumanResource( lLabourRow ), is( lTechnician ) );

   }


   /**
    *
    * Verify that when start a assigned labor row, then the labor row stage changed from ACTV to IN
    * WORK.
    *
    */
   @Test
   public void startAssignedLabourRowResultStageInWork() throws Exception {

      // Given the techician to assign to labour
      HumanResourceKey lTechnician = Domain.createHumanResource();

      // Given a task with an active and assigned labor row
      TaskKey lTask = Domain.createRequirement( lReq -> {
         lReq.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( lTechnician ) );
            labour.setStage( ACTV );
         } );
      } );

      // Give a start work package with assigned a task
      Domain.createWorkPackage( lWp -> {
         lWp.setStatus( RefEventStatusKey.IN_WORK );
         lWp.addTask( lTask );
      } );

      SchedLabourKey lLabourRow = Domain.readLabourRequirement( lTask ).get( 0 );

      // When the labor row is started
      iLabourBean.start( lLabourRow, lTechnician );

      // Retrieve the labor stage
      DataSetArgument args = new DataSetArgument();
      args.add( lLabourRow, "labour_db_id", "labour_id" );
      QuerySet qsSchedLabour =
            QuerySetFactory.getInstance().executeQueryTable( "sched_labour", args );
      qsSchedLabour.next();
      RefLabourStageKey labourStage =
            new RefLabourStageKey( qsSchedLabour.getInteger( "labour_stage_db_id" ),
                  qsSchedLabour.getString( "labour_stage_cd" ) );

      // Then labour stage is in work.
      assertThat( "Unexpected the labour row stage is not IN WORK", labourStage,
            is( RefLabourStageKey.IN_WORK ) );

   }


   /**
    *
    * Verify that when start a assigned labor row, then the labor row technician is unchanged.
    *
    */
   @Test
   public void startAssignedLabourRowResultTechnicianUnchanged() throws Exception {

      // Given the technician to assign to labor
      HumanResourceKey lTechnician = Domain.createHumanResource();

      // Given a task with an active and assigned labor row
      TaskKey lTask = Domain.createRequirement( lReq -> {
         lReq.addLabour( lLabour -> {
            lLabour.setSkill( RefLabourSkillKey.ENG );
            lLabour.setTechnicianRole( tech -> tech.setHumanResouce( lTechnician ) );
            lLabour.setStage( ACTV );
         } );
      } );

      // Give a start work package with assigned a task
      Domain.createWorkPackage( lWp -> {
         lWp.setStatus( RefEventStatusKey.IN_WORK );
         lWp.addTask( lTask );
      } );

      SchedLabourKey lLabourRow = Domain.readLabourRequirement( lTask ).get( 0 );

      // When the labor row is started
      iLabourBean.start( lLabourRow, lTechnician );

      // Then labor row work performed technician is unchanged.
      assertThat( "Unexcepted the work performed techincian is unassigned",
            getHumanResource( lLabourRow ), is( lTechnician ) );

   }


   /**
    *
    * Verify that when task with technician assigned in the labour requirement, and unassign
    * technician from the labour requirement . Then there is no technician assigned to the labour
    * requirement.
    */
   @Test
   public void unassignUserFromLabourRequirement() throws Exception {

      // Given the task with an assigned labour requirement
      iTask = Domain.createAdhocTask( aTask -> {
         // The logic in LabourBean.assign() requires the task to have an inventory set.
         aTask.setInventory( Domain.createAircraft() );
         aTask.addLabour( lLabour -> {
            lLabour.setTechnicianRole(
                  tech -> tech.setHumanResouce( Domain.createHumanResource() ) );
         } );
      } );

      // Given the labour requirement
      SchedLabourKey schedLabourRequirement = Domain.readLabourRequirement( iTask ).get( 0 );

      // Given the work package
      createWorkPackage( wp -> {
         wp.addTask( iTask );
      } );
      // When user is unassigned from labour requirement
      // In case unassign, use Hr as null for unassigned technician from labour requirement.
      updateTechToLabourRequirement( null, schedLabourRequirement );

      // Then labour requirement indicats null as "Unassigned"
      assertNull( "Unexcepted that user did not unassign to labour requirement properly",
            getHumanResource( schedLabourRequirement ) );

   }


   /**
    *
    * Verify that when task with old technician assigned in the labour requirement, and reassign a
    * new technician to the labour requirement . Then new technician reassigned to the labour
    * requirement.
    */
   @Test
   public void reassignUserToLabourRequirement() throws Exception {

      // Given the techician to assign to labour requirement
      HumanResourceKey newTechnician = Domain.createHumanResource();

      // Given the task with an assigned labour requirement
      iTask = Domain.createAdhocTask( aTask -> {
         // The logic in LabourBean.assign() requires the task to have an inventory set.
         aTask.setInventory( Domain.createAircraft() );
         aTask.addLabour( lLabour -> {
            lLabour.setTechnicianRole(
                  tech -> tech.setHumanResouce( Domain.createHumanResource() ) );
         } );
      } );

      // Given the labour requirement
      SchedLabourKey schedlabourRequirement = Domain.readLabourRequirement( iTask ).get( 0 );

      // Given the work package
      createWorkPackage( wp -> {
         wp.addTask( iTask );
      } );

      // When user is reassigned to labour requirement
      updateTechToLabourRequirement( newTechnician, schedlabourRequirement );

      // Then user reassigned indicats Technician.
      assertThat( "Unexcepted that user did not assign to labour requirement properly",
            getHumanResource( schedlabourRequirement ), is( newTechnician ) );
   }


   @Test
   public void removeLabourRequirement() throws Exception {
      iLabourBean.setSessionContext( new SessionContextFake() );

      HumanResourceKey lAuthorizingHr = Domain.createHumanResource();

      // Given the task with 2 labour requirements to remove one of them.
      iTask = Domain.createAdhocTask( aTask -> {
         aTask.setInventory( Domain.createAircraft() );
         aTask.addLabour( lLabour -> {
            lLabour.setSkill( ENG );
            lLabour.setTechnicianRole( tech -> {
               tech.setScheduledHours( ONE_HOUR );
               tech.setHumanResouce( lAuthorizingHr );
            } );
         } );
         aTask.addLabour( lLabour -> {
            lLabour.setSkill( PILOT );
            lLabour.setTechnicianRole( tech -> {
               tech.setScheduledHours( ONE_HOUR );
               tech.setHumanResouce( lAuthorizingHr );
            } );
         } );
      } );

      // Record revision date before labour removal for assertion later
      Date lRevsionDateBeforeUpdate = getRevisionDateOfTask( iTask );

      // To ensure an update in the revision date
      Thread.sleep( 2000 );

      // Remove labour PILOT from the created task
      SchedLabour lSchedLabour = SchedLabour.findByForeignKey( iTask.getEventKey(), PILOT );
      SchedLabourKey[] lSchedLabourKeyArray = new SchedLabourKey[] { lSchedLabour.getPk() };
      iLabourBean.remove( lSchedLabourKeyArray, lAuthorizingHr );

      // Assert the labour is removed from task
      SchedLabour lSchedLabourAfterRemoved =
            SchedLabour.findByForeignKey( iTask.getEventKey(), PILOT );
      assertNull( lSchedLabourAfterRemoved.getPk() );

      // Assert the revision date of task record is updated on removal
      Date lRevsionDateAfterUpdate =
            getRevisionDateOfTask( TaskKey.getTaskKey( iTask.getEventKey() ) );
      assertTrue( "The revision date of task was not updated",
            lRevsionDateBeforeUpdate.before( lRevsionDateAfterUpdate ) );
   }


   @Test
   public void completeUnassignedWorkNonStartedLabourRowCompletesTheLabourRow() throws Exception {

      // Given a technician to perform the job stop.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is unassigned and not started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( null ) );
            labour.setStage( IN_WORK );
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      FinishJobTO finishTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      finishTO.setTechnician( technician, "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is completed.
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( COMPLETE ) );
   }


   @Test
   public void jobStoppingUnassignedNonStartedLabourRowCompletesTheLabourRow() throws Exception {

      // Given a technician to perform the job stop.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is unassigned and not started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( null ) );
            labour.setStage( ACTV );
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      // When the labour row is job stopped by the technician.
      JobStopTO jobStopTO = new JobStopTO();
      jobStopTO.setTechnician( technician, "" );
      // Required by code under test but not relevant to the test.
      jobStopTO.setRemainingHours( 1.0, "" );

      iLabourBean.stop( labour, jobStopTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is completed.
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( COMPLETE ) );
   }


   @Test
   public void jobStoppingUnassignedNonStartedLabourRowGeneratesNewActiveLabourRow()
         throws Exception {

      // Given a technician to perform the job stop.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is unassigned and not started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( null ) );
            labour.setStage( ACTV );
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      // When the labour row is job stopped by the technician.
      JobStopTO jobStopTO = new JobStopTO();
      jobStopTO.setTechnician( technician, "" );
      // Required by code under test but not relevant to the test.
      jobStopTO.setRemainingHours( 1.0, "" );

      iLabourBean.stop( labour, jobStopTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );
      List<SchedLabourKey> labours = Domain.readLabourRequirement( task );
      labours.sort( new KeyComparator() );

      SchedLabour labourRecord = SchedLabour.findByPrimaryKey( labours.get( 1 ) );
      assertThat( "Unexpected labour row stage", labourRecord.getLabourStage(), is( ACTV ) );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labours.get( 0 ), TECH );
      techRole.assertExists();
   }


   @Test
   public void jobStoppingTechAssignedNonStartedLabourRowGeneratesNewActiveLabourRow()
         throws Exception {

      // Given a technician to perform the job stop.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and not started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( ACTV );
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      // When the labour row is job stopped by the technician.
      JobStopTO jobStopTO = new JobStopTO();
      jobStopTO.setTechnician( technician, "" );
      // Required by code under test but not relevant to the test.
      jobStopTO.setRemainingHours( 1.0, "" );

      iLabourBean.stop( labour, jobStopTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );
      List<SchedLabourKey> labours = Domain.readLabourRequirement( task );
      labours.sort( new KeyComparator() );

      SchedLabour labourRecord = SchedLabour.findByPrimaryKey( labours.get( 1 ) );
      assertThat( "Unexpected labour row stage", labourRecord.getLabourStage(),
            is( RefLabourStageKey.ACTV ) );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labours.get( 0 ), TECH );
      techRole.assertExists();
   }


   /**
    *
    * It marks the work as being certified when the labour requires certification AND the user is
    * NOT permitted to certifiy work AND the user certification is disabled
    *
    * @throws Exception
    */
   @Test
   public void certifyDisabledSignOffNotRequiredAndNotPermitted() throws Exception {

      // Given a technician to perform the finish job.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician.
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( RefLabourStageKey.IN_WORK );
            labour.setRequiresCertification( false ); // <--
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labour, TECH );
      techRole.setActualStartDate( new Date() );
      techRole.setActualEndDate( new Date() );
      techRole.update();

      FinishJobTO finishTO = new FinishJobTO();
      finishTO.setTechnician( technician, "" );
      finishTO.setAutoCertifyWork( false ); // <--
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setStartDate( new Date(), "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      int userId = OrgHr.findByPrimaryKey( technician ).getUserId();
      UserParametersStub lUserParms =
            new UserParametersStub( userId, ParmTypeEnum.SECURED_RESOURCE.name() );
      // Permissions to sign off on work completed is configured to false
      lUserParms.setBoolean( "ACTION_CERTIFY_TASK", false ); // <--
      UserParameters.setInstance( userId, ParmTypeEnum.SECURED_RESOURCE.name(), lUserParms );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is IN_WORK (auto-certify was not performed).
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( COMPLETE ) );
   }


   /**
    *
    * It marks the work as being certified when the labour requires certification AND the user is
    * permitted to certifiy work AND the user certification disabled
    *
    * @throws Exception
    */
   @Test
   public void certifyDisabledSignOffNotRequiredAndPermitted() throws Exception {

      // Given a technician to perform the finish job.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( IN_WORK );
            labour.setRequiresCertification( false ); // <--
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labour, TECH );
      techRole.setActualStartDate( new Date() );
      techRole.setActualEndDate( new Date() );
      techRole.update();

      FinishJobTO finishTO = new FinishJobTO();
      finishTO.setTechnician( technician, "" );
      finishTO.setAutoCertifyWork( false ); // <--
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setStartDate( new Date(), "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      int userId = OrgHr.findByPrimaryKey( technician ).getUserId();
      UserParametersStub lUserParms =
            new UserParametersStub( userId, ParmTypeEnum.SECURED_RESOURCE.name() );
      lUserParms.setBoolean( "ACTION_CERTIFY_TASK", true ); // <--
      UserParameters.setInstance( userId, ParmTypeEnum.SECURED_RESOURCE.name(), lUserParms );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is completed (auto-certify performed).
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( COMPLETE ) );
   }


   /**
    *
    * It marks the work as being certified when the labour requires certification AND the user is
    * NOT permitted to certifiy work AND the user certifies
    *
    * @throws Exception
    */
   @Test
   public void certifyOwnWorkSignOffRequiredAndNotPermitted() throws Exception {

      // Given a technician to perform the finish job.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician.
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( IN_WORK );
            labour.setRequiresCertification( true ); // <--
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labour, TECH );
      techRole.setActualStartDate( new Date() );
      techRole.setActualEndDate( new Date() );
      techRole.update();

      FinishJobTO finishTO = new FinishJobTO();
      finishTO.setTechnician( technician, "" );
      finishTO.setAutoCertifyWork( true ); // <--
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setStartDate( new Date(), "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      int userId = OrgHr.findByPrimaryKey( technician ).getUserId();
      UserParametersStub lUserParms =
            new UserParametersStub( userId, ParmTypeEnum.SECURED_RESOURCE.name() );
      // Permissions to sign off on work completed is configured to false
      lUserParms.setBoolean( "ACTION_CERTIFY_TASK", false ); // <--
      UserParameters.setInstance( userId, ParmTypeEnum.SECURED_RESOURCE.name(), lUserParms );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is IN_WORK (auto-certify was not performed).
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( IN_WORK ) );
   }


   /**
    *
    * It marks the work as still in work when the labour requires certification AND the user is
    * permitted to certifiy work AND the user does not do certification
    *
    * @throws Exception
    */
   @Test
   public void notCertifyingOwnWorkSignOffNotRequiredAndPermitted() throws Exception {

      // Given a technician to perform the finish job.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( IN_WORK );
            labour.setRequiresCertification( true ); // <--
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labour, TECH );
      techRole.setActualStartDate( new Date() );
      techRole.setActualEndDate( new Date() );
      techRole.update();

      FinishJobTO finishTO = new FinishJobTO();
      finishTO.setTechnician( technician, "" );
      finishTO.setAutoCertifyWork( false ); // <--
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setStartDate( new Date(), "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      int userId = OrgHr.findByPrimaryKey( technician ).getUserId();
      UserParametersStub lUserParms =
            new UserParametersStub( userId, ParmTypeEnum.SECURED_RESOURCE.name() );
      lUserParms.setBoolean( "ACTION_CERTIFY_TASK", true ); // <--
      UserParameters.setInstance( userId, ParmTypeEnum.SECURED_RESOURCE.name(), lUserParms );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is completed (auto-certify performed).
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( IN_WORK ) );
   }


   /**
    *
    * It marks the work as still in work when the labour requires certification AND the user is NOT
    * permitted to certifiy work AND the user certifies
    *
    * @throws Exception
    */
   @Test
   public void notCertifyingOwnWorkSignOffRequiredAndNotPermitted() throws Exception {

      // Given a technician to perform the finish job.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician.
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( IN_WORK );
            labour.setRequiresCertification( true ); // <--
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labour, TECH );
      techRole.setActualStartDate( new Date() );
      techRole.setActualEndDate( new Date() );
      techRole.update();

      FinishJobTO finishTO = new FinishJobTO();
      finishTO.setTechnician( technician, "" );
      finishTO.setAutoCertifyWork( false ); // <--
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setStartDate( new Date(), "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      int userId = OrgHr.findByPrimaryKey( technician ).getUserId();
      UserParametersStub lUserParms =
            new UserParametersStub( userId, ParmTypeEnum.SECURED_RESOURCE.name() );
      // Permissions to sign off on work completed is configured to false
      lUserParms.setBoolean( "ACTION_CERTIFY_TASK", false ); // <--
      UserParameters.setInstance( userId, ParmTypeEnum.SECURED_RESOURCE.name(), lUserParms );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is IN_WORK (auto-certify was not performed).
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( IN_WORK ) );
   }


   /**
    *
    * It marks the work as being certified when the labour requires certification AND the user is
    * permitted to certifiy work AND the user certifies
    *
    * @throws Exception
    */
   @Test
   public void certifyOwnWorkSignOffRequiredAndPermitted() throws Exception {

      // Given a technician to perform the finish job.
      HumanResourceKey technician = Domain.createHumanResource();

      // Given the current user is set to the technician (required by the code under test but not
      // relevant to the test).
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( technician ) );

      // Given a task with a labour row and the labour row is assigned and started.
      TaskKey task = Domain.createAdhocTask( adhoc -> {
         adhoc.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setHumanResouce( technician ) );
            labour.setStage( IN_WORK );
            labour.setRequiresCertification( true ); // <--
         } );

         // Required by code under test but not relevant to the test.
         adhoc.setInventory( Domain.createAircraft() );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task ).get( 0 );

      // Given the task is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task );
      } );

      SchedLabourRole techRole = SchedLabourRole.findByForeignKey( labour, TECH );
      techRole.setActualStartDate( new Date() );
      techRole.setActualEndDate( new Date() );
      techRole.update();

      FinishJobTO finishTO = new FinishJobTO();
      finishTO.setTechnician( technician, "" );
      finishTO.setAutoCertifyWork( true ); // <--
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setStartDate( new Date(), "" );
      lSchedLabourTO.setEndDate( new Date(), "" );
      finishTO.setSchedLabourTO( lSchedLabourTO );

      int userId = OrgHr.findByPrimaryKey( technician ).getUserId();
      UserParametersStub lUserParms =
            new UserParametersStub( userId, ParmTypeEnum.SECURED_RESOURCE.name() );
      lUserParms.setBoolean( "ACTION_CERTIFY_TASK", true ); // <--
      UserParameters.setInstance( userId, ParmTypeEnum.SECURED_RESOURCE.name(), lUserParms );

      iLabourBean.finish( labour, finishTO );

      // Reset the current user.
      SecurityIdentificationUtils.setInstance( null );

      // Then the labour row is completed (auto-certify performed).
      assertThat( "Unexpected labour stage",
            SchedLabour.findByPrimaryKey( labour ).getLabourStage(), is( COMPLETE ) );
   }


   private void updateTechToLabourRequirement( HumanResourceKey techHr, SchedLabourKey labReq )
         throws Exception {
      iLabourBean.setSessionContext( new SessionContextFake() );
      iLabourBean.assign( labReq, techHr, Domain.createHumanResource() );
   }


   private HumanResourceKey getHumanResource( SchedLabourKey aLabourReq ) {

      // Retrieve the auth hr
      DataSetArgument args = new DataSetArgument();
      args.add( aLabourReq, "labour_db_id", "labour_id" );
      QuerySet qsSchedLabourRole =
            QuerySetFactory.getInstance().executeQueryTable( "sched_labour_role", args );
      qsSchedLabourRole.next();
      SchedLabourRoleKey labourRole = qsSchedLabourRole.getKey( SchedLabourRoleKey.class,
            "labour_role_db_id", "labour_role_id" );

      args.clear();
      args.add( labourRole, "labour_role_db_id", "labour_role_id" );
      QuerySet qsSchedLabourRoleStatus =
            QuerySetFactory.getInstance().executeQueryTable( "sched_labour_role_status", args );
      qsSchedLabourRoleStatus.next();

      return qsSchedLabourRoleStatus.getKey( HumanResourceKey.class, "hr_db_id", "hr_id" );
   }


   private Date getRevisionDateOfTask( TaskKey aTaskKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey.getPKWhereArg() );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", lArgs );

      if ( !lQs.first() ) {
         fail( "Task not found for key " + aTaskKey );
      }

      return lQs.getDate( "revision_dt" );
   }


   private static class KeyComparator implements Comparator<SchedLabourKey> {

      /**
       * {@inheritDoc}
       */
      @Override
      public int compare( SchedLabourKey k1, SchedLabourKey k2 ) {
         return k1.toString().compareTo( k2.toString() );
      }

   }
}
