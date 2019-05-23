package com.mxi.mx.core.services.stask.workcapture;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;


/**
 * Tests the behaviour of the StepSkillDoesNotMatchLaborSkillException.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class StepSkillDoesNotMatchLaborSkillExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LabourRequirement iLabourReq_ENG;

   private LabourRequirement iLabourReq_PILOT;

   private TaskKey iActualTask;

   private TaskTaskKey iReqDefn;

   private UUID iStepSkillId_ENG;

   private Collection<LabourStepTO> iLabourStepTOs_ENG = new ArrayList<LabourStepTO>();


   /**
    * Ensure the validator does not throw StepSkillDoesNotMatchLaborSkillException
    */
   @Test
   public void testValid() throws MxException {

      dataSetup( iLabourReq_ENG );

      // DATA RETRIEVAL: Get labor row of the ENG skill
      SchedLabourKey lSchedLabour_ENG =
            getSchedLabourKey( iActualTask, iLabourReq_ENG.getLabourSkill() );

      try {
         StepSkillDoesNotMatchLaborSkillValidator.validate( lSchedLabour_ENG, iLabourStepTOs_ENG );

      } catch ( StepSkillDoesNotMatchLaborSkillException e ) {
         fail( "LaborSkillNotMarkedAsCertRequiredException should not be thrown." );
      }
   }


   /**
    * Ensure the validator throws a StepSkillDoesNotMatchLaborSkillException when the skill of the
    * selected job card step does not match the skill of the labour row being finished or job
    * stopped.
    */
   @Test
   public void testStepSkillDoesNotMatchLaborSkill() throws MxException {

      dataSetup( iLabourReq_ENG, iLabourReq_PILOT );

      // DATA RETRIEVAL: Get labor row of the PILOT skill
      SchedLabourKey lSchedLabour_PILOT =
            getSchedLabourKey( iActualTask, iLabourReq_PILOT.getLabourSkill() );

      try {
         StepSkillDoesNotMatchLaborSkillValidator.validate( lSchedLabour_PILOT,
               iLabourStepTOs_ENG );

         fail( "Expected LaborSkillNotMarkedAsCertRequiredException but it was not thrown." );

      } catch ( StepSkillDoesNotMatchLaborSkillException e ) {
         ;
      }
   }


   /**
    * Ensure the validator throws a StepSkillDoesNotMatchLaborSkillException when the skill of the
    * labour row being finished or job stopped match the job step skill of a different task
    * definition
    */
   @Test
   public void testStepSkillFromDiffTaskDefn() throws MxException {

      dataSetup( iLabourReq_ENG );

      // DATA SETUP: Create another requirement with step skill PILOT
      TaskTaskKey lReqDefn2 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.REQ );
                  aReqDefn.setExecutable( true );
                  aReqDefn.addLabourRequirement( iLabourReq_ENG );
                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        aStep.addStepSkill( iLabourReq_ENG.getLabourSkill(), false );
                     }

                  } );
               }
            } );

      // DATA RETRIEVAL: Get labor row of the ENG skill
      SchedLabourKey lSchedLabour_ENG =
            getSchedLabourKey( iActualTask, iLabourReq_ENG.getLabourSkill() );

      // DATA RETRIEVAL: Get the task defn step skill key for ENG skill
      UUID lStepSkillId_ENG2 = getStepSkillId( lReqDefn2, iLabourReq_ENG.getLabourSkill() );

      // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG from another task
      // definition
      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setStepSkillId( lStepSkillId_ENG2 );

      try {
         StepSkillDoesNotMatchLaborSkillValidator.validate( lSchedLabour_ENG,
               Arrays.asList( lLabourStepTO ) );

         fail( "Expected LaborSkillNotMarkedAsCertRequiredException but it was not thrown." );

      } catch ( StepSkillDoesNotMatchLaborSkillException e ) {
         ;
      }
   }


   private void dataSetup( LabourRequirement... aLabourRequirements ) {

      // DATA SETUP: Create a requirement definition
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.REQ );
                  aReqDefn.setExecutable( true );

                  for ( LabourRequirement lLabourReq : aLabourRequirements ) {
                     aReqDefn.addLabourRequirement( lLabourReq );
                  }

                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {

                        for ( LabourRequirement lLabourReq : aLabourRequirements ) {
                           aStep.addStepSkill( lLabourReq.getLabourSkill(), false );
                        }
                     }

                  } );
               }
            } );

      // DATA SETUP: Create an actual task with a labour row based on the labour requirement.
      iActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( iReqDefn );

            for ( LabourRequirement lLabourReq : aLabourRequirements ) {
               aRequirement.addLabour( aLabour -> {
                  aLabour.setSkill( lLabourReq.getLabourSkill() );
                  aLabour.setTechnicianRole( tech -> tech
                        .setScheduledHours( lLabourReq.getWorkPerformedScheduleHours() ) );
                  aLabour.setCertifierRole(
                        cert -> cert.setScheduledHours( lLabourReq.getCertSchedHrs() ) );
               } );
            }
         }
      } );

      // DATA RETRIEVAL: Get the task defn step skill key for ENG skill
      iStepSkillId_ENG = getStepSkillId( iReqDefn, iLabourReq_ENG.getLabourSkill() );

      // DATA SETUP: Sign off labor row with ENG skill
      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setStepSkillId( iStepSkillId_ENG );

      iLabourStepTOs_ENG.add( lLabourStepTO );
   }


   private SchedLabourKey getSchedLabourKey( TaskKey aActualTask, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aActualTask, "sched_db_id", "sched_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "sched_labour", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();

      SchedLabourKey lSchedLabourKey =
            lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      return lSchedLabourKey;
   }


   private UUID getStepSkillId( TaskTaskKey aTaskDefn, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskDefn, "task_db_id", "task_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "task_step_skill", lArgs,
            new String[] { "step_skill_id" } );

      lQs.next();

      return lQs.getUuid( "step_skill_id" );
   }


   @Before
   public void setUp() {

      iLabourReq_ENG = new LabourRequirement( RefLabourSkillKey.ENG, new BigDecimal( 2 ),
            new BigDecimal( 2 ), null );

      iLabourReq_PILOT = new LabourRequirement( RefLabourSkillKey.PILOT, new BigDecimal( 1 ),
            new BigDecimal( 1 ), null );

   }

}
