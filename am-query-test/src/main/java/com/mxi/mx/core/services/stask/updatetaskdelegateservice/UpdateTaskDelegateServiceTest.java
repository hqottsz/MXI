package com.mxi.mx.core.services.stask.updatetaskdelegateservice;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.stask.delegate.UpdateTaskDelegateService;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;


/**
 * Test class for {@linkplain UpdateTaskDelegateService}
 *
 */
public class UpdateTaskDelegateServiceTest {

   private static final BigDecimal INTERVAL = BigDecimal.TEN;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Given an active recurring requirement definition with a calendar-day based scheduling rule
    * And a completed task along with an active task
    * When the completion date of the completed task is modified
    * Then the deadline of the active task gets adjusted according to the modification to the completion date of the completed task
    *
    * </pre>
    *
    */
   @Test
   public void itAdjustsDeadlineOfDependentActiveTaskWhenEndDateOfCompletedTaskModified()
         throws Exception {

      // Given
      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      HumanResourceKey lUser = Domain.createHumanResource();

      InventoryKey lAircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( lAircraftAssembly ) );

      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssembly ) );
         aReqDefn.setRecurring( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      Date lCompleteTaskDueDate = new Date();
      Date lCompleteTaskEndDate = DateUtils.addDays( new Date(), -4 );
      TaskKey lCompletedTask = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( REQ );
         aReq.setDefinition( lReqDefn );
         aReq.setInventory( lAircraft );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
         aReq.setActualEndDate( lCompleteTaskEndDate );
         aReq.addCalendarDeadline( CDY, RefSchedFromKey.EFFECTIV, BigDecimal.ZERO, INTERVAL,
               lCompleteTaskDueDate );
      } );

      Date lActiveTaskDueDate = DateUtils.addDays( lCompleteTaskEndDate, INTERVAL.intValue() );
      TaskKey lActiveTask = Domain.createRequirement( aReq -> {
         aReq.setPreviousTask( lCompletedTask );
         aReq.setTaskClass( REQ );
         aReq.setDefinition( lReqDefn );
         aReq.setInventory( lAircraft );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.addCalendarDeadline( CDY, RefSchedFromKey.LASTEND, BigDecimal.ZERO, INTERVAL,
               lActiveTaskDueDate );
      } );

      // When
      UpdateTaskDelegateService lUpdateTaskDelegateService = new UpdateTaskDelegateService();
      UsageSnapshot[] lUsageSnapshots = null;
      Date lCompleteTaskModifiedEndDate = DateUtils.addDays( lCompleteTaskEndDate, 5 );
      lUpdateTaskDelegateService.modifyCompletionData( lCompletedTask, lAircraft, lUsageSnapshots,
            lCompleteTaskModifiedEndDate, true, lUser, true );

      // Then
      EvtSchedDeadTable lEvtSchedDeadTable = EvtSchedDeadTable.findByPrimaryKey( lActiveTask, CDY );
      Date lActiveTaskExpectedDueDate = DateUtils
            .getEndOfDay( DateUtils.addDays( lCompleteTaskModifiedEndDate, INTERVAL.intValue() ) );
      Date lActiveTaskActualDueDate = lEvtSchedDeadTable.getDeadlineDate();
      assertEquals(
            "Unexpectedly, the modification of completed task's completion date didn't adjust the next task's due date",
            lActiveTaskExpectedDueDate.toString(), lActiveTaskActualDueDate.toString() );

   }


   /**
    * <pre>
    * Given an aircraft based on an aircraft assembly
    * Given a completed work package with a usage snapshot for the aircraft
    * And a completed task with a usage snapshot for the aircraft and a usage deadline
    * And an active task with a usage deadline
    * When the usage snapshot of the work package is modified
    * Then the usage deadline of the active task gets modified by the delta of the work package snapshot
    *
    * </pre>
    *
    */
   @Test
   public void
         itAdjustsUsageOfCompletedTaskWithinWorkPackageWhenUsageSnapshotOfWorkPackageModified()
               throws Exception {

      // Given
      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      HumanResourceKey lUser = Domain.createHumanResource();

      FcModelKey lFcModel = Domain.createForecastModel();

      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
         aAircraft.setForecastModel( lFcModel );
      } );

      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssembly ) );
         aReqDefn.setRecurring( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      Date lCompleteTaskDueDate = new Date();
      BigDecimal lCompletedTaskUsage = BigDecimal.TEN;
      TaskKey lCompletedTask = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( REQ );
         aReq.setInventory( lAircraft );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
         aReq.addUsage( new UsageSnapshot( lAircraft, DataTypeKey.HOURS, lCompletedTaskUsage ) );
         aReq.addUsageDeadline( DataTypeKey.HOURS, INTERVAL, lCompletedTaskUsage );
      } );

      BigDecimal lWorkPackageUsage = BigDecimal.TEN;
      TaskKey lWorkPackage = Domain.createWorkPackage( aWp -> {
         aWp.setAircraft( lAircraft );
         aWp.addTask( lCompletedTask );
         aWp.addUsageSnapshot( lAircraft, DataTypeKey.HOURS, lWorkPackageUsage );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      BigDecimal lActiveTaskUsage = BigDecimal.TEN.add( INTERVAL );
      TaskKey lActiveTask = Domain.createRequirement( aReq -> {
         aReq.setPreviousTask( lCompletedTask );
         aReq.setTaskClass( REQ );
         aReq.setDefinition( lReqDefn );
         aReq.setInventory( lAircraft );
         aReq.setStatus( RefEventStatusKey.ACTV );
         aReq.addUsageDeadline( DataTypeKey.HOURS, INTERVAL, lActiveTaskUsage );
      } );

      // When
      BigDecimal lModifiedHoursUsageForWorkPackage = lWorkPackageUsage.add( INTERVAL );
      UpdateTaskDelegateService lUpdateTaskDelegateService = new UpdateTaskDelegateService();
      UsageSnapshot[] lModifiedUsageSnapshots = {
            new UsageSnapshot( lAircraft, DataTypeKey.HOURS, lModifiedHoursUsageForWorkPackage ) };
      lUpdateTaskDelegateService.modifyCompletionData( lWorkPackage, lAircraft,
            lModifiedUsageSnapshots, lCompleteTaskDueDate, true, lUser, true );

      // Then
      EvtSchedDeadTable lEvtSchedDeadTable =
            EvtSchedDeadTable.findByPrimaryKey( lActiveTask.getEventKey(), DataTypeKey.HOURS );
      Double lActiveTaskActualUsageDeadline = lEvtSchedDeadTable.getDeadlineQt();
      Double lActiveTaskExpectedUsageDeadline =
            lModifiedHoursUsageForWorkPackage.add( INTERVAL ).doubleValue();
      assertEquals(
            "Unexpectedly, the modification of Work Package's usage snapshot didn't adjust the the active task's deadline",
            lActiveTaskExpectedUsageDeadline, lActiveTaskActualUsageDeadline );

   }

}
