package com.mxi.mx.core.query.reports.task.workcapture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test the StepsSubReport query used by the Work Capture report
 *
 */
public class StepsSubReportTest {

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey aircraftInventoryKey;

   private static final String QUERY_NAME =
         "com.mxi.mx.core.reports.task.workcapture.StepsSubReport";

   private static final String BASELINE_DESC_COL = "baselined_step_desc";
   private static final String ADHOC_DESC_COL = "adhoc_step_desc";
   private static final String HAS_SKILLS_COL = "has_skills";
   private static final String IS_ADHOC_COL = "is_adhoc";


   @Before
   public void setup() {

      // create aircraft and engine
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
      } );

   }


   /**
    * Test the query returns no data when no steps are defined.
    */
   @Test
   public void noStepsDefined() {

      // ARRANGE
      TaskKey taskKey = Domain.createJobCard( task -> {
         task.setInventory( aircraftInventoryKey );
      } );

      // ACT
      DataSet ds = execute( taskKey );

      // ASSERT
      assertFalse( ds.next() );

   }


   /**
    * Test the query returns baseline steps with no skills.
    */
   @Test
   public void baselineStepsWithNoSkillsOnATask() {

      // ARRANGE

      TaskTaskKey reqTaskDefn = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REQ );
         req.setExecutable( true );
         req.addStep( step -> {
            step.setDescription( "Step 1" );
         } );

      } );

      TaskKey taskKey = Domain.createRequirement( req -> {
         req.setInventory( aircraftInventoryKey );
         req.setDefinition( reqTaskDefn );
      } );

      // ACT
      DataSet ds = execute( taskKey );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getFullRowCount() );
      assertNull( ds.getString( ADHOC_DESC_COL ) );
      assertEquals( "Step 1", ds.getString( BASELINE_DESC_COL ) );
      assertFalse( ds.getBoolean( IS_ADHOC_COL ) );
      assertFalse( ds.getBoolean( HAS_SKILLS_COL ) );

   }


   /**
    * Test the query returns baseline steps with skills.
    */
   @Test
   public void baselineStepsWithSkillsOnATask() {

      // ARRANGE

      TaskTaskKey reqTaskDefn = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REQ );
         req.setExecutable( true );
         req.addStep( step -> {
            step.setDescription( "Step 1" );
            step.addStepSkill( RefLabourSkillKey.ENG, false );
         } );

      } );

      TaskKey taskKey = Domain.createRequirement( req -> {
         req.setInventory( aircraftInventoryKey );
         req.setDefinition( reqTaskDefn );
      } );

      // ACT
      DataSet ds = execute( taskKey );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getFullRowCount() );
      assertNull( ds.getString( ADHOC_DESC_COL ) );
      assertEquals( "Step 1", ds.getString( BASELINE_DESC_COL ) );
      assertFalse( ds.getBoolean( IS_ADHOC_COL ) );
      assertTrue( ds.getBoolean( HAS_SKILLS_COL ) );

   }


   /**
    * Test the query returns baseline steps with and without skills.
    */
   @Test
   public void baselineStepsWithAndWithoutSkillsOnATask() {

      // ARRANGE

      TaskTaskKey reqTaskDefn = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REQ );
         req.setExecutable( true );
         req.addStep( step -> {
            step.setDescription( "Step 1 with skill" );
            step.addStepSkill( RefLabourSkillKey.ENG, false );
         } );
         req.addStep( step -> {
            step.setDescription( "Step 2 with no skill" );
         } );

      } );

      TaskKey taskKey = Domain.createRequirement( req -> {
         req.setInventory( aircraftInventoryKey );
         req.setDefinition( reqTaskDefn );
      } );

      // ACT
      DataSet ds = execute( taskKey );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 2, ds.getFullRowCount() );
      assertNull( ds.getString( ADHOC_DESC_COL ) );
      assertEquals( "Step 1 with skill", ds.getString( BASELINE_DESC_COL ) );
      assertFalse( ds.getBoolean( IS_ADHOC_COL ) );
      assertTrue( ds.getBoolean( HAS_SKILLS_COL ) );

      ds.next();
      assertNull( ds.getString( ADHOC_DESC_COL ) );
      assertEquals( "Step 2 with no skill", ds.getString( BASELINE_DESC_COL ) );
      assertFalse( ds.getBoolean( IS_ADHOC_COL ) );
      assertFalse( ds.getBoolean( HAS_SKILLS_COL ) );

   }


   /**
    * Test the query returns adhoc steps.
    */
   @Test
   public void adhocStepsOnATask() {

      // ARRANGE

      TaskKey taskKey = Domain.createAdhocTask( task -> {
         task.setInventory( aircraftInventoryKey );
         task.addStep( "Step 1" );
      } );

      // ACT
      DataSet ds = execute( taskKey );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 1, ds.getFullRowCount() );
      assertTrue( ds.getBoolean( IS_ADHOC_COL ) );
      assertFalse( ds.getBoolean( HAS_SKILLS_COL ) );

   }


   /**
    * Test the query returns adhoc, and baseline steps with and without skills.
    */
   @Test
   public void adhocAndBaselineStepsOnAFault() {

      // ARRANGE

      // create a repair reference with steps
      TaskTaskKey repairReferenceKey = Domain.createRequirementDefinition( reference -> {
         reference.setTaskClass( RefTaskClassKey.REPREF );
         reference.setTaskName( "Repair Reference" );
         reference.setCode( "REPREF1" );
         reference.setStatus( RefTaskDefinitionStatusKey.ACTV );

         reference.addStep( step -> {
            step.setDescription( "Repair Step 1 with skills" );
            step.addStepSkill( RefLabourSkillKey.ENG, false );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 2 no skills" );
         } );
      } );

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );
         task.addStep( "Adhoc step" );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setCurrentRepairReference( repairReferenceKey );
      } );

      // ACT
      DataSet ds = execute( correctiveTaskKey );

      // ASSERT
      assertTrue( ds.next() );
      assertEquals( 3, ds.getFullRowCount() );

      // step 1
      assertTrue( ds.getBoolean( IS_ADHOC_COL ) );
      assertFalse( ds.getBoolean( HAS_SKILLS_COL ) );

      // step 2
      ds.next();
      assertNull( ds.getString( ADHOC_DESC_COL ) );
      assertEquals( "Repair Step 1 with skills", ds.getString( BASELINE_DESC_COL ) );
      assertFalse( ds.getBoolean( IS_ADHOC_COL ) );
      assertTrue( ds.getBoolean( HAS_SKILLS_COL ) );

      // step 3
      ds.next();
      assertNull( ds.getString( ADHOC_DESC_COL ) );
      assertEquals( "Repair Step 2 no skills", ds.getString( BASELINE_DESC_COL ) );
      assertFalse( ds.getBoolean( IS_ADHOC_COL ) );
      assertFalse( ds.getBoolean( HAS_SKILLS_COL ) );

   }


   /**
    * Executes the query.
    *
    * @return dataSet result.
    */
   private DataSet execute( TaskKey task ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSchedDbId", task.getDbId() );
      lArgs.add( "aSchedId", task.getId() );

      // Execute the query
      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(), QUERY_NAME,
            lArgs );
   }

}
