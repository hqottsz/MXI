package com.mxi.mx.core.query.stask.labour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.step.MxStepService;
import com.mxi.mx.core.task.model.StepStatus;


public class GetStepSkillsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private TaskKey correctiveTask;
   private TaskTaskKey taskDefinition1;
   private TaskTaskKey taskDefinition2;


   @Before
   public void setUp() throws Exception {

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

      taskDefinition1 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {

                  aReqDefinition.setExecutable( true );
                  aReqDefinition.addStep( createStep( "STEP ONE", RefLabourSkillKey.ENG, true ) );
                  aReqDefinition
                        .addStep( createStep( "STEP_TWO", RefLabourSkillKey.PILOT, false ) );
               }
            } );

      taskDefinition2 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {

                  aReqDefinition.setExecutable( true );
                  aReqDefinition.addStep( createStep( "STEP ONE", RefLabourSkillKey.LBR, false ) );
               }
            } );

   }


   /**
    * Checks if the steps are return with right labour skill and step status and if that step
    * requires inspection for multiple repair references associated to a fault
    */
   @Test
   public void getStepSkillsForFault() throws MxException {
      correctiveTask = Domain.createCorrectiveTask();

      MxStepService stepService = new MxStepService();
      // copy steps from two different tasks definitions
      stepService.copyBaselineSteps( correctiveTask, taskDefinition1 );
      stepService.copyBaselineSteps( correctiveTask, taskDefinition2 );

      QuerySet qsStepSkills = getStepSkills( correctiveTask );
      assertEquals( qsStepSkills.getRowCount(), 3 );
      while ( qsStepSkills.next() ) {
         RefLabourSkillKey skillKey = qsStepSkills.getKey( RefLabourSkillKey.class,
               "labour_skill_db_id", "labour_skill_cd" );
         if ( skillKey.equals( RefLabourSkillKey.ENG ) ) {
            assertTrue( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         } else if ( skillKey.equals( RefLabourSkillKey.PILOT ) ) {
            assertFalse( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         } else if ( skillKey.equals( RefLabourSkillKey.LBR ) ) {
            assertFalse( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         }
      }
   }


   /**
    * Checks if the steps are return with right labour skill and step status and if that step
    * requires inspection for the same repair references associated twice to a fault
    */
   @Test
   public void getStepSkillsForFault_SameTaskDefAddedTwice() throws MxException {
      correctiveTask = Domain.createCorrectiveTask();

      MxStepService stepService = new MxStepService();
      // copy steps from two different tasks definitions
      stepService.copyBaselineSteps( correctiveTask, taskDefinition1 );
      stepService.copyBaselineSteps( correctiveTask, taskDefinition1 );

      QuerySet qsStepSkills = getStepSkills( correctiveTask );
      assertEquals( qsStepSkills.getRowCount(), 4 );
      while ( qsStepSkills.next() ) {
         RefLabourSkillKey skillKey = qsStepSkills.getKey( RefLabourSkillKey.class,
               "labour_skill_db_id", "labour_skill_cd" );
         if ( skillKey.equals( RefLabourSkillKey.ENG ) ) {
            assertTrue( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         } else if ( skillKey.equals( RefLabourSkillKey.PILOT ) ) {
            assertFalse( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         } else if ( skillKey.equals( RefLabourSkillKey.LBR ) ) {
            assertFalse( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         }
      }
   }


   /**
    * Checks if the steps are return with right labour skill and step status and if that step
    * requires inspection baseline task
    */
   @Test
   public void getStepSkillsForBaselineTask() {
      // Create an actual task based on the executable requirement above
      TaskKey actualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( taskDefinition1 );
            aReq.addLabour( aLabour -> {
               aLabour.setSkill( RefLabourSkillKey.ENG );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
               aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
               aLabour.setInspectorRole( insp -> insp.setScheduledHours( 2 ) );
            } );
            aReq.addLabour( aLabour -> {
               aLabour.setSkill( RefLabourSkillKey.PILOT );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
               aLabour.setCertifierRole( cert -> cert.setScheduledHours( 2 ) );
               aLabour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
            } );
         }
      } );

      QuerySet qsStepSkills = getStepSkills( actualTask );
      assertEquals( qsStepSkills.getRowCount(), 2 );
      while ( qsStepSkills.next() ) {
         RefLabourSkillKey skillKey = qsStepSkills.getKey( RefLabourSkillKey.class,
               "labour_skill_db_id", "labour_skill_cd" );
         if ( skillKey.equals( RefLabourSkillKey.ENG ) ) {
            assertTrue( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         } else if ( skillKey.equals( RefLabourSkillKey.PILOT ) ) {
            assertFalse( qsStepSkills.getBoolean( "insp_bool" ) );
            assertEquals( qsStepSkills.getString( "step_status_cd" ),
                  StepStatus.MXPENDING.toString() );
         }
      }
   }


   private QuerySet getStepSkills( TaskKey taskKey ) {
      DataSetArgument taskArgs = new DataSetArgument();
      taskArgs.add( taskKey, "aSchedDbId", "aSchedId" );
      return QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.task.labour.GetStepSkills", taskArgs );

   }


   private DomainConfiguration<Step> createStep( final String aStepDesc,
         final RefLabourSkillKey aSkill, boolean inspRequired ) {
      return new DomainConfiguration<Step>() {

         @Override
         public void configure( Step aStep ) {
            aStep.setDescription( aStepDesc );
            aStep.addStepSkill( aSkill, inspRequired );
         }
      };
   }

}
