package com.mxi.mx.core.services.stask.labour.stop;

import static com.mxi.mx.core.key.RefLabourRoleStatusKey.ACTV;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefReferenceRequestStatusKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.esigner.EsignatureService;
import com.mxi.mx.core.services.stask.labour.DefaultLabourRoleStatusFinder;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourRoleUtils;
import com.mxi.mx.core.services.stask.labour.LabourService;
import com.mxi.mx.core.services.stask.labour.SchedLabourTO;
import com.mxi.mx.core.services.stask.panel.SchedPanelService;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


/**
 * This class contains tests for {@linkplain LabourService#stop}
 *
 */
public class LabourService_StopTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException expectedExpectation = ExpectedException.none();

   public LabourService labourService;

   private UserKey user;
   private HumanResourceKey humanResource;


   @Before
   public void setup() {

      // Required by the service
      user = Domain.createUser( aUser -> aUser.setUserId( 9999999 ) );
      humanResource = Domain.createHumanResource( aHr -> aHr.setUser( user ) );
      SecurityIdentificationInterface securityIdentification =
            new SecurityIdentificationStub( humanResource );
      SecurityIdentificationUtils.setInstance( securityIdentification );
      labourService = new LabourService( new LabourRoleUtils(), new EsignatureService(),
            new DefaultLabourRoleStatusFinder(), new WorkCaptureService(), new SchedPanelService(),
            new SequentialUuidGenerator() );
   }


   /**
    *
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a deferral reference
      - AND MOC authorization required on that deferral reference
      - When a technician attempts to stop a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void
         itThrowsValidationExceptionWhenDeferralReferenceRequiresMOCApprovalButDeferRequestNotApproved()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      JobStopTO lJobStopTO = new JobStopTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lJobStopTO.setTechnician( humanResource, "" );
      lJobStopTO.setSchedLabourTO( lSchedLabourTO );
      lJobStopTO.setRemainingHours( BigDecimal.TEN.doubleValue(), "" );

      expectedExpectation.expect( ValidationException.class );
      expectedExpectation
            .expectMessage( StringContains.containsString( i18n.get( "core.err.33910" ) ) );

      labourService.stop( schedLabourKey, lJobStopTO );

   }


   /**
    *
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a deferral reference
      - AND MOC authorization required on that deferral reference
      - And the deferral request is APPROVED
      - When a technician attempts to stop a labour on the corrective task
      - Then the labour is stopped
    *
    * </pre>
    *
    */
   @Test
   public void itStopsLabourWhenDeferralReferenceRequiresMOCApprovalAndDeferralRequestIsApproved()
         throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      FaultKey fault = Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      Domain.createFaultReferenceRequest( request -> {
         request.setReferenceRequestByFaultsCurrentRequest( fault );
         request.setRequestStatus( RefReferenceRequestStatusKey.APPROVED.getCd() );
         request.setHumanResourceKey( humanResource );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      JobStopTO lJobStopTO = new JobStopTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lJobStopTO.setTechnician( humanResource, "" );
      lJobStopTO.setSchedLabourTO( lSchedLabourTO );
      lJobStopTO.setRemainingHours( BigDecimal.TEN.doubleValue(), "" );

      labourService.stop( schedLabourKey, lJobStopTO );

   }


   /**
    *
    * Ensure that when a new labour row is created as a result of a job stop (a new row added to the
    * sched_labour table) that the key of the associated job stopped labour row is stored in that
    * new sched_labour row.
    *
    */
   @Test
   public void itStoresAssociatedJobStoppedLabourRowKeyInNewLabourRow() throws Exception {

      // Given a task in a started work package and the task has a labour requirement.
      TaskKey task = Domain.createRequirement( req -> {
         req.addLabour();
      } );
      Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.ACTV );
         wp.addTask( task );
      } );

      List<SchedLabourKey> labourReqs = Domain.readLabourRequirement( task );
      assertThat( "Unexpected number of labour rows during data setup.", labourReqs.size(),
            is( 1 ) );
      SchedLabourKey labourReqToStop = labourReqs.get( 0 );

      // When the labour row is job stopped.
      labourService.stop( labourReqToStop, buildJobStopTO() );

      // Then a new row is added to the sched_labour table.
      labourReqs = Domain.readLabourRequirement( task );
      assertThat( "Unexpected number of labour rows.", labourReqs.size(), is( 2 ) );
      SchedLabourTable schedLabourRow =
            SchedLabourTable.findByPrimaryKey( getNewLabourReq( task, labourReqToStop ) );

      // Then the job-stop-labour key is set to the key of the job stopped labour row.
      assertThat(
            "Newly created labour row does not have a link to the job stopped labour row. (unexpected job stop labour key)",
            schedLabourRow.getSourceJobStopLabourRow(), is( labourReqToStop ) );
   }


   private JobStopTO buildJobStopTO() throws Exception {
      JobStopTO jobStopTO = new JobStopTO();
      // Not required for tests but job stop logic requires that this be set to a non-zero value.
      jobStopTO.setRemainingHours( 1.0d, "" );
      return jobStopTO;
   }


   /**
    * Given the provided task has two labour requirements this returns the labour requirement key
    * that does not match the provided labour requirement key.
    */
   private SchedLabourKey getNewLabourReq( TaskKey task, SchedLabourKey labourReqToIgnore ) {
      List<SchedLabourKey> labourReqs = Domain.readLabourRequirement( task );
      assertTrue( "Expected labour requirement [" + labourReqToIgnore
            + "] no longer assigned to task: " + task, labourReqs.remove( labourReqToIgnore ) );
      assertThat( "Labour row unexpectedly not added to task.", labourReqs.size(), is( 1 ) );
      return labourReqs.get( 0 );
   }

}
