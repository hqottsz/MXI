
package com.mxi.mx.core.services.stask.labour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.EditSchedLabourTO;
import com.mxi.mx.core.services.stask.TaskServiceFactory;
import com.mxi.mx.core.table.sched.SchedLabourRoleAccessor;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;


/**
 * Tests the copyBaselineLabourRequirements method of the {@link LabourService}
 */
public final class LabourServiceCopyLabourRequirementTest {

   private LabourService service;

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( 999, "username" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private TaskKey correctiveTask;
   private HumanResourceKey hrKey = new HumanResourceKey( "0:111" );


   @Before
   public void setUp() throws Exception {
      service = ( LabourService ) TaskServiceFactory.getInstance().getLabourService();

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();

      InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setLocation( new LocationKey( "1:1" ) );
         aircraft.setApplicabilityCode( "06" );
      } );

      correctiveTask = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setBarcode( "ABC" );
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

   }


   /*-
    * - Fault has no current ACTV or IN-WORK labor row
    * - Action Taken : Create new one
    * - This is a case where no labor currently exists that matches labor from the repair reference.
    *   We will create a new one.
    */
   @Test
   public void copyLabourReqs_faultHasNoCurrentActiveOrInWorkRows()
         throws MxException, TriggerException {
      TaskTaskKey taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.ENG ),
                  getLabourRequirement( RefLabourSkillKey.PILOT ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      QuerySet query = getTaskLabourRequirement( correctiveTask );
      assertEquals( 2, query.getRowCount() );

   }


   /*-
    * - Fault has a single ACTV labor row not from a reference
    * - Action Taken : Overwrite
    * - This is a case where the labor skill already exists prior to selecting a reference. Overwrite the
    *   scheduled hours (the assumption is the work is no longer required, so we will only use the hours from
    *   the new reference)
    */
   @Test
   public void copyLabourReqs_faultHasNoCurrentActiveorInWorkRows()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;
      SchedLabourKey schedLabourKey =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );

      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      QuerySet query = getTaskLabourRequirement( correctiveTask );
      assertEquals( 1, query.getRowCount() );
      assertTrue( "ACT Labour requirement was not overriden", compareLabourRequirement(
            schedLabourKey, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );
   }


   /*-
    * - Fault has a single ACTV labor row from a previous reference
    * - Action Taken : Overwrite
    * - This is a case where another reference was selected and now they have picked a different one.
    *   Overwrite the scheduled hours (the assumption is the work from the previous reference is no longer required,
    *   so we will only use the hours from the new reference)
    */
   @Test
   public void copyLabourReqs_faultHasSingleActiveLabourRowFromPreviousReference()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      TaskTaskKey taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.ENG, valueOf( 2 ),
                  valueOf( 2 ), valueOf( 2 ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 1, schedLabourRequirements.size() );

      LabourRequirement updatedLabourRequirement = schedLabourRequirements.get( 0 );

      assertTrue( "ACT Labour requirement was not overriden", compareLabourRequirement(
            updatedLabourRequirement, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );
   }


   /*-
    * - Fault has two or more ACTV labor rows not from a reference
    * - Action Taken : Overwrite one of them
    * - This is a case where the same labor skill appears more then once prior to selecting a reference.
    *   This can happen if they know the fault needs 2 or more people to work on it and require multiple
    *   sign-offs of the work. Overwrite one of them
    */
   @Test
   public void copyLabourReqs_faultHasTwoOrMoreActiveLabourNotFromReference()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      SchedLabourKey schedLabourKey1 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );

      SchedLabourKey schedLabourKey2 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 2d, true, 3d, true, 1d );

      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 2, schedLabourRequirements.size() );

      LabourRequirement updatedLabourRequirement1 = schedLabourRequirements.stream()
            .filter( l -> l.getSchedLabourKey().equals( schedLabourKey1 ) ).findFirst()
            .orElse( null );

      assertTrue( "ACT Labour requirement was not overriden", compareLabourRequirement(
            updatedLabourRequirement1, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );

      LabourRequirement updatedLabourRequirement2 = schedLabourRequirements.stream()
            .filter( l -> l.getSchedLabourKey().equals( schedLabourKey2 ) ).findFirst()
            .orElse( null );

      assertTrue( "ACT Labour requirement was overriden",
            compareLabourRequirement( updatedLabourRequirement2, 2, 3, 1 ) );
   }


   /*-
    * - Fault has an ACTV and IN-WORK labor row not from a reference
    * - Action Taken : Overwrite the ACTV one
    * - This is a case where the same labor skill appears more then once and they have started one of
    *   them already. We will overwrite the ACTV one.
    */
   @Test
   public void copyLabourReqs_faultHasActiveAndInWorkNotFromReference()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      SchedLabourKey schedLabourKey1 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );
      // set labour row to INWORK
      SchedLabourTable schedLabourTable = SchedLabourTable.findByPrimaryKey( schedLabourKey1 );
      schedLabourTable.setLabourStage( RefLabourStageKey.IN_WORK );
      schedLabourTable.update();

      SchedLabourKey schedLabourKey2 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 2d, true, 3d, true, 1d );

      // create a definition with the same type of labour requirement
      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 2, schedLabourRequirements.size() );

      LabourRequirement labourRequirement1 = schedLabourRequirements.stream()
            .filter( l -> l.getSchedLabourKey().equals( schedLabourKey1 ) ).findFirst()
            .orElse( null );

      assertTrue( "IN-WORK Labour requirement was overriden",
            compareLabourRequirement( labourRequirement1, 1, 1, 0 ) );

      LabourRequirement updatedLabourRequirement = schedLabourRequirements.stream()
            .filter( l -> l.getSchedLabourKey().equals( schedLabourKey2 ) ).findFirst()
            .orElse( null );

      assertTrue( "ACT Labour requirement was not overriden", compareLabourRequirement(
            updatedLabourRequirement, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );

   }


   /*-
    * - Fault has two or more ACTV labor rows from a previous reference
    * - Action Taken : Overwrite
    * - This is the case where a previous reference added the same labor more then once.
    *   They now pick a different reference that has the same labor rows. We will overwrite them.
    */
   @Test
   public void copyLabourReqs_faultHasTwoOrMoreActiveLabourFromPreviousReference()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( 1 ), valueOf( 1 ), valueOf( 1 ),
                  BigDecimal.ONE ),
            getLabourRequirement( RefLabourSkillKey.PILOT, valueOf( 1 ), valueOf( 1 ), valueOf( 1 ),
                  BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ),
            getLabourRequirement( RefLabourSkillKey.PILOT, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 2, schedLabourRequirements.size() );

      schedLabourRequirements.forEach( l -> {
         assertTrue( "ACT Labour requirement was not overriden",
               compareLabourRequirement( l, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );
      } );

   }


   /*-
    * - Fault has an ACTV and IN-WORK labor row from a previous reference
    * - Action Taken : Overwrite the ACTV one and create a new one
    * - This is an odd case where the previous reference added 2 of the same skill.
    *   One was started and they now pick a different reference which adds the same 2 labor skills.
    *   The ACTV one will be overwritten and a new one created as the original is still IN-WORK.
    */
   @Test
   public void copyLabourReqs_faultHasActiveAndInWorkLabourFromPreviousReference()
         throws MxException, TriggerException {

      double taskDefWorkHr = 1;
      double taskDefCertHr = 2;
      double taskDefInspHr = 3;

      // create a definition with the same type of labour requirement
      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ),
            getLabourRequirement( RefLabourSkillKey.PILOT, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      LabourRequirement updatedLabourRequirement1 = schedLabourRequirements.get( 0 );
      updatedLabourRequirement1.setCertificationScheduledHours( 20 );
      updatedLabourRequirement1.setWorkPerformedScheduledHours( 10 );
      updatedLabourRequirement1.setIndependentInspectionScheduledHours( 5 );
      updateLabourRow( updatedLabourRequirement1 );

      // set labour row to INWORK
      SchedLabourTable schedLabourTable =
            SchedLabourTable.findByPrimaryKey( updatedLabourRequirement1.getSchedLabourKey() );
      schedLabourTable.setLabourStage( RefLabourStageKey.IN_WORK );
      schedLabourTable.update();

      LabourRequirement updatedLabourRequirement2 = schedLabourRequirements.get( 1 );
      updatedLabourRequirement2.setCertificationScheduledHours( 15 );
      updatedLabourRequirement2.setWorkPerformedScheduledHours( 25 );
      updatedLabourRequirement1.setIndependentInspectionScheduledHours( 30 );
      updateLabourRow( updatedLabourRequirement2 );

      // copy again steps from the same labour row
      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      schedLabourRequirements = service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 3, schedLabourRequirements.size() );

      // make sure the labour row IN-WORK has not been overwritten

      LabourRequirement labourRequirement = schedLabourRequirements.stream().filter(
            l -> l.getSchedLabourKey().equals( updatedLabourRequirement1.getSchedLabourKey() ) )
            .findFirst().orElse( null );

      assertTrue( "IN-WORK Labour requirement was overriden",
            compareLabourRequirement( labourRequirement, 10, 20, 5 ) );

      // make sure the existing ACTIVE labour row has been overwritten
      labourRequirement = schedLabourRequirements.stream().filter(
            l -> l.getSchedLabourKey().equals( updatedLabourRequirement2.getSchedLabourKey() ) )
            .findFirst().orElse( null );

      assertTrue( "ACT Labour requirement was not overriden", compareLabourRequirement(
            labourRequirement, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );

   }


   /*-
    * - Fault has a single ACTV labor and reference has multiple
    * - Action Taken : Overwrite the ACTV one and create a new one
    * - This is a case where the fault has an existing ACTV labor skill and the reference has 2 or
    *   more of the same skill. We will overwrite one of them and add the other.
    */
   @Test
   public void copyLabourReqs_faultHasSingleActiveAndReferenceHasMultiple()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      SchedLabourKey schedLabourKey1 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );

      // create a definition with the same type of labour requirement for 3 people
      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), valueOf( 3 ) ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 3, schedLabourRequirements.size() );

      assertTrue( "ACT Labour requirement was not overriden", compareLabourRequirement(
            schedLabourKey1, taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );

   }


   /*-
    * - Fault only has a current IN-WORK labor row
    * - Action Taken : Create new one
    * - This is a case where the fault has a pre-existing labor skill that is IN-WORK.
    *   We will leave it as is and create a new one.
    */
   @Test
   public void copyLabourReqs_faultHasOnlyCurrentInWorkLabour()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      SchedLabourKey schedLabourKey1 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );
      // set labour row to INWORK
      SchedLabourTable schedLabourTable = SchedLabourTable.findByPrimaryKey( schedLabourKey1 );
      schedLabourTable.setLabourStage( RefLabourStageKey.IN_WORK );
      schedLabourTable.update();

      // create a definition with the same type of labour requirement
      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 2, schedLabourRequirements.size() );

      LabourRequirement labourRequirement1 = schedLabourRequirements.stream()
            .filter( l -> l.getSchedLabourKey().equals( schedLabourKey1 ) ).findFirst()
            .orElse( null );

      assertTrue( "IN-WORK Labour requirement was overriden",
            compareLabourRequirement( labourRequirement1, 1, 1, 0 ) );

   }


   /*-
    * - Fault only has a COMPLETE labor row
    * - Action Taken : Create new one
    * - This is a case where the fault has the same labor as the repair but it has been completed.
    *   We will create a new one.
    */
   @Test
   public void copyLabourReqs_faultHasOnlyCompleteLabourRow() throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      SchedLabourKey schedLabourKey1 =
            LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );
      // set labour row to INWORK
      SchedLabourTable schedLabourTable = SchedLabourTable.findByPrimaryKey( schedLabourKey1 );
      schedLabourTable.setLabourStage( RefLabourStageKey.COMPLETE );
      schedLabourTable.update();

      // create a definition with the same type of labour requirement
      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 2, schedLabourRequirements.size() );

      LabourRequirement labourRequirement1 = schedLabourRequirements.stream()
            .filter( l -> l.getSchedLabourKey().equals( schedLabourKey1 ) ).findFirst()
            .orElse( null );

      assertTrue( "COMPLETE Labour requirement was overriden",
            compareLabourRequirement( labourRequirement1, 1, 1, 0 ) );

   }


   /*-
    * - Fault only has a non-certifying ACTV labor row
    * - Action Taken : Overwrite
    * - This is a case where a labor skill currently exists that does not require certification.
    *   The repair reference will still overwrite this one and it will now require certification
    *   (even though the initial work did not require it).
    */
   @Test
   public void copyLabourReqs_faultHasOnlyNonCertifyingActiveLabourRow()
         throws MxException, TriggerException {

      double taskDefWorkHr = 20;
      double taskDefCertHr = 15;
      double taskDefInspHr = 10;

      LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, false, 0d, false, 0d );

      TaskTaskKey taskDefinitionKey = createTaskDefnWithSteps(
            getLabourRequirement( RefLabourSkillKey.ENG, valueOf( taskDefWorkHr ),
                  valueOf( taskDefCertHr ), valueOf( taskDefInspHr ), BigDecimal.ONE ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      List<LabourRequirement> schedLabourRequirements =
            service.getTaskLabourRequeriments( correctiveTask );

      assertEquals( 1, schedLabourRequirements.size() );

      assertTrue( "Active labour row was not overriden", compareLabourRequirement(
            schedLabourRequirements.get( 0 ), taskDefWorkHr, taskDefCertHr, taskDefInspHr ) );

   }


   /**
    * Verify that baseline labour requirements are copied into a task that does not have labour
    * requirement
    *
    * @throws TriggerException
    */
   @Test
   public void copyBaselineLabourRequirementsToTaskWithoutSkills()
         throws MxException, TriggerException {
      TaskTaskKey taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.ENG ),
                  getLabourRequirement( RefLabourSkillKey.LBR ) );

      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      QuerySet query = getTaskLabourRequirement( correctiveTask );
      assertEquals( 2, query.getRowCount() );

   }


   /**
    * Verify that baseline labour requirements from multiple task definitions are copied into a task
    * that does not have labour requirement
    *
    * @throws TriggerException
    */
   @Test
   public void copyBaselineLabourRequirementsFromMultipleTaskDefinitions()
         throws MxException, TriggerException {
      TaskTaskKey taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.ENG ),
                  getLabourRequirement( RefLabourSkillKey.LBR ) );
      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.PILOT ) );
      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      QuerySet query = getTaskLabourRequirement( correctiveTask );

      assertEquals( 3, query.getRowCount() );

   }


   /**
    * Verify that baseline labour requirements from a task definitions are copied into a task that
    * already has labour requirements
    *
    * @throws TriggerException
    */

   @Test
   public void copyBaselineLabourRequirementsToTaskWithSkills()
         throws MxException, TriggerException {

      // add manual step first
      LabourService.add( correctiveTask, RefLabourSkillKey.ENG, 1d, true, 1d, false, 0d );

      TaskTaskKey taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.LBR ),
                  getLabourRequirement( RefLabourSkillKey.PILOT ) );
      // copy step from task definition
      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      QuerySet query = getTaskLabourRequirement( correctiveTask );
      assertEquals( 3, query.getRowCount() );

   }


   /**
    * Verify when a labour requirement has been added with multiple number of people, the same
    * amount of labour requirement rows will have to be copied to the task
    *
    * @throws TriggerException
    */

   @Test
   public void copyBaselineLabourRequirementsWithMultipleNumberOfPeople()
         throws MxException, TriggerException {
      BigDecimal numberOfPeopleRequired = new BigDecimal( 5 );

      TaskTaskKey taskDefinitionKey =
            createTaskDefnWithSteps( getLabourRequirement( RefLabourSkillKey.LBR, BigDecimal.ONE,
                  BigDecimal.ONE, BigDecimal.ONE, numberOfPeopleRequired ) );
      // copy step from task definition
      service.copyBaselineLabourRequirements( correctiveTask, taskDefinitionKey, hrKey );

      QuerySet query = getTaskLabourRequirement( correctiveTask );

      assertEquals( numberOfPeopleRequired.intValue(), query.getRowCount() );

   }


   private boolean compareLabourRequirement( SchedLabourKey schedLabourKey, double workPerfSchedHrs,
         double certSchedHrs, double inspSchedHrs ) {

      SchedLabourRoleAccessor<SchedLabourRoleKey> schedLabourRole =
            SchedLabourRoleTable.findByForeignKey( schedLabourKey, RefLabourRoleTypeKey.TECH );
      double workPerf = schedLabourRole.getSchedHours();
      schedLabourRole =
            SchedLabourRoleTable.findByForeignKey( schedLabourKey, RefLabourRoleTypeKey.CERT );
      double cert = schedLabourRole.getSchedHours();
      schedLabourRole =
            SchedLabourRoleTable.findByForeignKey( schedLabourKey, RefLabourRoleTypeKey.INSP );
      double inps = schedLabourRole.getSchedHours();
      return workPerf == workPerfSchedHrs && cert == certSchedHrs && inps == inspSchedHrs;
   }


   private QuerySet getTaskLabourRequirement( TaskKey taskKey ) {
      DataSetArgument taskArgs = new DataSetArgument();
      taskArgs.add( taskKey, "aTaskDbId", "aTaskId" );
      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.query.stask.labour.LabourRequirementsByTask", taskArgs );

   }


   private TaskTaskKey
         createTaskDefnWithSteps( com.mxi.am.domain.LabourRequirement... labourRequirements ) {

      TaskTaskKey taskTask = Domain.createRequirementDefinition( reqDefinition -> {
         for ( com.mxi.am.domain.LabourRequirement labourRequirement : labourRequirements ) {
            reqDefinition.addLabourRequirement( labourRequirement );
         }
      } );

      return taskTask;
   }


   private com.mxi.am.domain.LabourRequirement getLabourRequirement(
         RefLabourSkillKey labourSkillKey, BigDecimal workPerfSchedHrs, BigDecimal certSchedHrs,
         BigDecimal inspSchedHrs, BigDecimal numberOfRequiredPeople ) {
      return new com.mxi.am.domain.LabourRequirement( labourSkillKey, workPerfSchedHrs,
            certSchedHrs, inspSchedHrs, numberOfRequiredPeople );

   }


   private com.mxi.am.domain.LabourRequirement
         getLabourRequirement( RefLabourSkillKey labourSkillKey ) {
      return new com.mxi.am.domain.LabourRequirement( labourSkillKey, new BigDecimal( 1 ),
            new BigDecimal( 2 ), new BigDecimal( 3 ), new BigDecimal( 1 ) );
   }


   private BigDecimal valueOf( double value ) {
      return BigDecimal.valueOf( value );
   }


   private boolean compareLabourRequirement( LabourRequirement labourRequirement,
         double workPerfSchedHrs, double certSchedHrs, double inspSchedHrs ) {

      return labourRequirement.getWorkPerformedScheduledHours() == workPerfSchedHrs
            && labourRequirement.getCertificationScheduledHours() == certSchedHrs
            && labourRequirement.getIndependentInspectionScheduledHours() == inspSchedHrs;
   }


   public void updateLabourRow( LabourRequirement labourRequirement )
         throws MxException, TriggerException {
      EditSchedLabourTO editSchedLabourTO = new EditSchedLabourTO();

      editSchedLabourTO.setCertificationRequired( labourRequirement.isCertificationRequired() );
      editSchedLabourTO
            .setCertificationScheduledHours( labourRequirement.getCertificationScheduledHours() );
      editSchedLabourTO.setIndependentInspectionRequired(
            labourRequirement.isIndependentInspectionRequired() );
      editSchedLabourTO.setIndependentInspectionScheduledHours(
            labourRequirement.getIndependentInspectionScheduledHours() );
      editSchedLabourTO
            .setWorkPerformedScheduledHours( labourRequirement.getWorkPerformedScheduledHours() );
      editSchedLabourTO.setSchedLabour( labourRequirement.getSchedLabourKey() );

      service.edit( Arrays.asList( editSchedLabourTO ), hrKey );
   }
}
